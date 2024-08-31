package com.gin.msaflux.auth_service.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangPasswordRq {
    private String oldPassword;
    private String newPassword;

}
