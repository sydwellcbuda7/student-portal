package za.co.student_management.student_portal.config.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import za.co.student_management.student_portal.config.security.JWTService;
import za.co.student_management.student_portal.config.security.PermissionsAllowed;
import za.co.student_management.student_portal.model.datatype.Role;


import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private JWTService jwtService;

    public AuthorizationInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof ResourceHttpRequestHandler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(PermissionsAllowed.class)) {
                String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (Objects.nonNull(authToken)) {
                    Jws<Claims> claimsJws = jwtService.verifyToken(authToken);
                    List<String> roles = (List<String>) claimsJws.getBody().get(JWTService.ROLES);
                    Set<Role> userRoles = roles.stream().map(Role::valueOf).collect(Collectors.toSet());
                    if (!isUserAllowed(userRoles, handlerMethod)) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                    }
                }
            }
        }
        return true;
    }

    private boolean isUserAllowed(Set<Role> userRoles, HandlerMethod method) {
        PermissionsAllowed permissionAnnotation = method.getMethodAnnotation(PermissionsAllowed.class);
        Set<Role> rolesAllowed = new HashSet<>(Arrays.asList(permissionAnnotation.value()));
        return !Collections.disjoint(rolesAllowed, userRoles);
    }
}
