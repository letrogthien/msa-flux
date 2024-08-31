package com.gin.msaflux.auth_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    private Long id;
    @Column("token")
    private String jwtToken;
    private String type;
    private boolean revoked;
    private boolean expired;
    @Column("user_id")
    private Long userId;
    private boolean refresh;
}
