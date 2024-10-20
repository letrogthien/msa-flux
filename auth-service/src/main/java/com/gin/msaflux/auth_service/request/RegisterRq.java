package com.gin.msaflux.auth_service.request;

import lombok.*;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRq {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
}
