package com.springcloud.demo.auth.security;

import com.springcloud.demo.auth.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.secret}")
    private String jwtSecret;
    @Value("${auth.expiration_ms}")
    private int jwtExpirationMs;

    String getJwtToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public String getUserNameFromJwtToken(String token) {
        // 解析jwt token获得用户名
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token.substring(7)).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            if (!authToken.startsWith("Bearer")){
                return false;
            }
            String rawToken = authToken.substring(7);
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(rawToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateToken(User user) {
        // 生成签名
        String token = Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return String.format("Bearer %s", token);
    }
}