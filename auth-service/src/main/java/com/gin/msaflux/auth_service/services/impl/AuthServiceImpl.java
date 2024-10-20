package com.gin.msaflux.auth_service.services.impl;


import com.gin.msaflux.auth_service.common.Mapper;
import com.gin.msaflux.auth_service.common.RoleType;
import com.gin.msaflux.auth_service.dtos.UserDto;
import com.gin.msaflux.auth_service.jwt.JwtUtil;
import com.gin.msaflux.auth_service.kafka.KafkaUtils;
import com.gin.msaflux.auth_service.kafka.dto.ForgotPassNotify;
import com.gin.msaflux.auth_service.request.AuthRequest;
import com.gin.msaflux.auth_service.models.User;
import com.gin.msaflux.auth_service.repository.UserRepository;
import com.gin.msaflux.auth_service.request.ChangPasswordRq;
import com.gin.msaflux.auth_service.request.RegisterRq;
import com.gin.msaflux.auth_service.response.AuthResponse;
import com.gin.msaflux.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    private final KafkaUtils kafkaUtils;
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
                .switchIfEmpty(Mono.error(new Exception("User not found")))
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
                                            return kafkaUtils.sendMessage("forget-password-notify-topic",forgotPassNotify);
                                        }
                                        return Mono.empty();
                                    })
                                    .thenReturn("change password successful");
                        }
                );
    }

    @Override
    public Mono<String> changePassword(final ChangPasswordRq changPasswordRq) {
        return getCurrentUserId().flatMap(
                userId ->
                        customUserDetailsService.findByUserId(userId)
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
        return userService.getByUserName(registerRq.getUsername())
                .flatMap(user -> Mono.error(new RuntimeException("Username already exists")))
                .switchIfEmpty(
                        userService.getByEmail(registerRq.getEmail())
                                .flatMap(user -> Mono.error(new RuntimeException("Email already exists")))
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
                                )
                )
                .thenReturn("Register success");
    }


    @Override
    public Mono<Object> updateUser(UserDto userDto) {
        return getCurrentUserId().flatMap(
                securityContext -> userRepository.findById(securityContext)
                        .switchIfEmpty(Mono.error(new Exception("User not found")))
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


    private Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) securityContext.getAuthentication();
                    Jwt jwt = jwtAuthToken.getToken();
                    return jwt.getClaim("id");
                });
    }

}
