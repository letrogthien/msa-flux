package com.gin.msaflux.auth_service.services;


import com.gin.msaflux.auth_service.gmail.EmailSender;
import com.gin.msaflux.auth_service.jwt.JwtUtil;
import com.gin.msaflux.auth_service.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil  jwtUtil;
    private final ReactiveAuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final UserService userService;
    private final TokenService tokenService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public Mono<AuthResponse> authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken)
                .flatMap(authentication -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    return userService.getByUserName(userDetails.getUsername())
                            .flatMap(user ->
                                     tokenService.getAllChangeRevokedExpired(user.getId())
                                            .then(Mono.defer(() -> {
                                                String tk = jwtUtil.generateToken(userDetails);
                                                String refreshToken = jwtUtil.generateRefreshToken(userDetails);
                                                return tokenService.saveToken(tk, user.getId(), false)
                                                        .then(tokenService.saveToken(refreshToken, user.getId(), true))
                                                        .thenReturn(new AuthResponse(tk, refreshToken));
                                            }))
                            );
                });
    }


    public Mono<AuthResponse> refreshToken() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    String userName = securityContext.getAuthentication().getName();
                    return customUserDetailsService.findByUsername(userName)
                            .flatMap(userDetails -> {
                                String name = userDetails.getUsername();
                                return userService.getByUserName(name)
                                        .flatMap(user ->
                                                tokenService.getAllChangeRevokedExpired(user.getId())
                                                        .then(Mono.defer(() -> {
                                                            String tk = jwtUtil.generateToken(userDetails);
                                                            return tokenService.saveToken(tk, user.getId(), false)
                                                                    .thenReturn(new AuthResponse(tk, null));
                                                        }))
                                        );

                            });
                });
    }

    public Mono<String> forgetPassword(final String userName, final String email) {
        return userService.getByUserName(userName)
                .filter(user -> user.getEmail().equals(email))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(
                        user -> {
                            StringBuilder result = new StringBuilder(10);
                            for (int i = 0; i < 10; i++) {
                                int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
                                result.append(CHARACTERS.charAt(randomIndex));
                            }
                            emailSender.sendEmail(email, "newpass", String.valueOf(result));
                            user.setPassword(passwordEncoder.encode(result.toString()));
                            return userService.save(user).thenReturn("change password successful");
                        }
                );
    }

    public Mono<String> changePassword(final String oldPassword, final String newPassword) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext ->
                        customUserDetailsService.findByUsername(securityContext.getAuthentication().getName())
                                .filter(user -> user.getPassword().equals(passwordEncoder.encode(oldPassword)))
                                .switchIfEmpty(Mono.error(new RuntimeException("old pass incorrect")))
                                .flatMap(
                                        userDetails ->
                                                userService.getByUserName(userDetails.getUsername())
                                                        .flatMap(user -> {
                                                            user.setPassword(passwordEncoder.encode(newPassword));
                                                            return userService.save(user)
                                                                    .thenReturn("change password successful");
                                                        })
                                )
        );
    }
}
