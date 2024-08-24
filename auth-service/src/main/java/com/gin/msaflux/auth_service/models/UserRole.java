package com.gin.msaflux.auth_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Column("user_id")
    private Long userId;


    @Column("role_id")
    private Long roleId;
}
