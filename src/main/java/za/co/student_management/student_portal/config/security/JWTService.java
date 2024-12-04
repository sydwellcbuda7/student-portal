package za.co.student_management.student_portal.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import za.co.student_management.student_portal.exception.custome.AuthorizationException;
import za.co.student_management.student_portal.model.UserEntity;
import za.co.student_management.student_portal.model.UserRoleEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JWTService {
    @Value("${student-portal.jwtSecret}")
    private String jwtSecret;

    private SecretKey key;
    public static final String USER_ID = "user_id";
    public static final String ROLES = "roles";
    static final String EMAIL = "email";

    public String generateToken(UserEntity user) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .claim(USER_ID, String.valueOf(user.getId()))
                .claim(EMAIL, String.valueOf(user.getEmail()))
                .claim(ROLES, user.getAssignedRoles()
                        .stream()
                        .map(UserRoleEntity::getRole)
                        .collect(Collectors.toList()))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public Jws<Claims> verifyToken(String token) {
        try {
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            log.warn("Error while verifying token", e);
            throw new AuthorizationException("Invalid auth token provided");
        }
    }
}
