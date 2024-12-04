package za.co.student_management.student_portal.control;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import za.co.student_management.student_portal.config.security.JWTService;


import java.io.IOException;
import java.util.Objects;

public class JWTSecurityFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    public JWTSecurityFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Objects.nonNull(authToken)) {
            Jws<Claims> claimsJws = jwtService.verifyToken(authToken);
            String userId = (String) claimsJws.getBody().get(JWTService.USER_ID);
            Authentication authentication = new TokenAuthentication(userId);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
