package com.gin.msaflux.auth_service.models;

import com.gin.msaflux.auth_service.common.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "jwt_tokens")
public class JwtToken {

    @Id
    private String id;

    private String token;
    private TokenType type;
    private String userId;
    private Instant issuedAt;
    private Instant expiresAt;
    private boolean expired;
    private boolean revoked;
    private boolean refresh;

}
