package com.gin.msaflux.auth_service.kafka.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPassNotify {
    private String email;
    private String newPassword;
}
