package com.gin.msaflux.auth_service.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;


@Service

public class JwtUtil {

    @Value("${security.secret.key}")
    private String secretKey;
    @Value("${security.secret.expiration}")
    private  long jwtExpiration;
    @Value("${security.secret.refreshToken}")
    private  long refreshExpiration;





    public String extractUserId(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("id",String.class);
    }

    public String generateToken(UserDetails userDetails, String id) {
        return generateToken(new HashMap<>(), userDetails,id);
    }


    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            String id
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration, id);
    }

    public String generateRefreshToken(
            UserDetails userDetails, String id
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration, id);
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration,
            String id
    ) {
        List<String> authorities =  userDetails.getAuthorities().stream().map(Object::toString).toList();
        extraClaims.put("roles", authorities);
        extraClaims.put("id", id);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {

            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }








}
