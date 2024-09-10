package com.gin.msaflux.auth_service.serviceimp;


import com.gin.msaflux.auth_service.common.RoleType;
import com.gin.msaflux.auth_service.gmail.EmailSender;
import com.gin.msaflux.auth_service.jwt.JwtUtil;
import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.RegisterRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import com.gin.msaflux.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final JwtUtil  jwtUtil;
    private final ReactiveAuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final UserServiceImp userService;
    private final TokenServiceImp tokenService;
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Override
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


    @Override
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

    @Override
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

    @Override
    public Mono<String> changePassword(final ChangPasswordRq changPasswordRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext ->
                        customUserDetailsService.findByUsername(securityContext.getAuthentication().getName())
                                .filter(user -> user.getPassword().equals(passwordEncoder.encode(changPasswordRq.getOldPassword())))
                                .switchIfEmpty(Mono.error(new RuntimeException("old pass incorrect")))
                                .flatMap(
                                        userDetails ->
                                                userService.getByUserName(userDetails.getUsername())
                                                        .flatMap(user -> {
                                                            user.setPassword(passwordEncoder.encode(changPasswordRq.getNewPassword()));
                                                            return userService.save(user)
                                                                    .thenReturn("change password successful");
                                                        })
                                )
        );
    }

    @Override
    public Mono<Object> register(RegisterRq registerRq) {
        if (registerRq.getPassword() == null || registerRq.getPassword().isEmpty()) {
            return Mono.error(new RuntimeException("Password cannot be null or empty"));
        }
        if (registerRq.getEmail() == null || registerRq.getEmail().isEmpty()) {
            return Mono.error(new RuntimeException("Email cannot be null or empty"));
        }
        if (registerRq.getUsername() == null || registerRq.getUsername().isEmpty()) {
            return Mono.error(new RuntimeException("Username cannot be null or empty"));
        }
        return userService.getByUserName(registerRq.getUsername())
                .flatMap(user -> Mono.error(new RuntimeException("User Exists")))
                .switchIfEmpty(
                        userService.save(
                                        User.builder()
                                                .roles(List.of(String.valueOf(RoleType.CUSTOMER)))
                                                .createdAt(LocalDateTime.now())
                                                .username(registerRq.getUsername())
                                                .password(passwordEncoder.encode(registerRq.getPassword()))
                                                .email(registerRq.getEmail())
                                                .build()
                                ).doOnSuccess(
                                        user -> kafkaTemplate.send("register", user.getUsername()) //send to User Service to add user
                                )
                                .then(Mono.just("register receipt"))
                );
    }
}
