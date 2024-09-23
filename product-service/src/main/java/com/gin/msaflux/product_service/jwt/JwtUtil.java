package com.gin.msaflux.product_service.jwt;

import com.gin.msaflux.product_service.exception.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${security.secret.key}")
    private String secretKey;
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    @SuppressWarnings("unchecked")
    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }
    public String extractUserId(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("id",String.class);
    }
    public boolean isExpiration(String token){
        return extractExpiration(token).before(new Date());
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {

            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomExpiredTokenException("Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new CustomUnsupportedTokenException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            throw new CustomMalformedTokenException("Malformed JWT token");
        } catch (SignatureException e) {
            throw new CustomInvalidSignatureException("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            throw new CustomInvalidTokenException("JWT token is empty or null");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
