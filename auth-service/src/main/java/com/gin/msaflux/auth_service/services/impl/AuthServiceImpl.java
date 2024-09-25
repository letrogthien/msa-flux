package com.gin.msaflux.auth_service.services.impl;


import com.gin.msaflux.auth_service.common.Mapper;
import com.gin.msaflux.auth_service.common.RoleType;
import com.gin.msaflux.auth_service.dtos.UserDto;
import com.gin.msaflux.auth_service.jwt.JwtUtil;
import com.gin.msaflux.auth_service.request.AuthRequest;
import com.gin.msaflux.common.kafka.payload.ForgotPassNotify;
import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.repository.UserRepository;
import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.RegisterRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import com.gin.msaflux.auth_service.services.AuthService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
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
public class AuthServiceImpl implements AuthService {
    private final JwtUtil  jwtUtil;
    private final ReactiveAuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final TokenServiceImpl tokenService;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Mapper mapper = Mapper.INSTANCE ;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /*
    * @Param AuthRequest
    * @Response AuthResponse
    * login and return token and refresh token
     */
    @Override
    public Mono<AuthResponse> authenticate(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword());

        return authenticationManager.authenticate(authenticationToken)
                .flatMap(authentication -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    return userService.getByUserName(userDetails.getUsername())
                            .flatMap(user ->
                                     tokenService.getAllChangeRevokedExpired(user.getId())
                                            .then(Mono.defer(() -> {
                                                String tk = jwtUtil.generateToken(userDetails, user.getId());
                                                String refreshToken = jwtUtil.generateRefreshToken(userDetails, user.getId());
                                                return tokenService.saveToken(tk, user.getId(), false)
                                                        .then(tokenService.saveToken(refreshToken, user.getId(), true))
                                                        .thenReturn(new AuthResponse(tk, refreshToken));
                                            }))
                            );
                });
    }


    /*
    * @Header
    * refresh token
     */
    @Override
    public Mono<AuthResponse> refreshToken(String jwt) {
        return customUserDetailsService.findByUserId(jwtUtil.extractUserId(jwt))
                .flatMap(userDetails ->
                        userService.getByUserName(userDetails.getUsername())
                                .flatMap(u ->
                                        {
                                            String id=jwtUtil.extractUserId(jwt);
                                            return tokenService.getAllChangeRevokedExpired(id)
                                                    .then(Mono.defer(() -> {
                                                        String tk = jwtUtil.generateToken(userDetails, id);
                                                        return tokenService.saveToken(tk, id, false)
                                                                .thenReturn(new AuthResponse(tk, null));
                                                    }));
                                        }
                                )

                );
    }


    @Override
    public Mono<String> forgetPassword(final String userName, final String email) {
        return userService.getByUserName(userName)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .filter(user -> user.getEmail().equals(email))
                .flatMap(
                        user -> {
                            StringBuilder result = new StringBuilder(10);
                            for (int i = 0; i < 10; i++) {
                                int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
                                result.append(CHARACTERS.charAt(randomIndex));
                            }
                            user.setPassword(passwordEncoder.encode(result.toString()));
                            return userService.save(user)
                                    .flatMap(u ->{
                                        if (user.getEmail()!= null){
                                            ForgotPassNotify forgotPassNotify = ForgotPassNotify.builder()
                                                    .email(user.getEmail())
                                                    .newPassword(result.toString())
                                                    .build();
                                            return Mono.fromRunnable(()->kafkaTemplate.send("forget-password-notify-topic",forgotPassNotify));
                                        }
                                        return Mono.empty();
                                    })
                                    .thenReturn("change password successful");
                        }
                );
    }

    @Override
    public Mono<String> changePassword(final ChangPasswordRq changPasswordRq) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext ->
                        customUserDetailsService.findByUserId(securityContext.getAuthentication().getName())
                                .filter(user -> user.getPassword().equals(passwordEncoder.encode(changPasswordRq.getOldPassword())))
                                .filter(user -> passwordEncoder.matches(changPasswordRq.getOldPassword(), user.getPassword()))
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
                        )
                ).thenReturn("register success");
    }

    @Override
    public Mono<Object> updateUser(UserDto userDto) {
        return ReactiveSecurityContextHolder.getContext().flatMap(
                securityContext -> userRepository.findById(securityContext.getAuthentication().getName())
                        .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                        .flatMap(user -> {
                            User userNew = mapper.toUser(userDto);
                            return userRepository.save(userNew).map(mapper::toUserDto);
                        }).thenReturn("update success")

        );
    }

    @Override
    public Mono<UserDto> getUserById(String userId) {
        return userRepository.findById(userId).map(mapper::toUserDto);
    }

}
