package za.co.student_management.student_portal.config.interceptor;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import za.co.student_management.student_portal.config.security.JWTService;
import za.co.student_management.student_portal.config.security.PermissionsAllowed;
import za.co.student_management.student_portal.exception.custome.AuthorizationException;
import za.co.student_management.student_portal.exception.custome.ForbiddenException;
import za.co.student_management.student_portal.model.datatype.Role;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PermissionsInterceptor implements HandlerInterceptor {

    private final JWTService jwtService;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public PermissionsInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        PermissionsAllowed permissionAnnotation = handlerMethod.getMethodAnnotation(PermissionsAllowed.class);

        if (Objects.isNull(permissionAnnotation)) {
            return true;
        }

        String jwt = request.getHeader(AUTHORIZATION_HEADER);
        String authToken = null;
        if (jwt != null) {
            authToken = jwt.startsWith("Bearer ")
                    ? jwt.substring(7)
                    : jwt;
        }

        if (Objects.isNull(authToken)) {
            throw new ForbiddenException("User is not logged in");
        }

        Jws<Claims> claimsJws;
        try {
            claimsJws = jwtService.verifyToken(authToken);
        } catch (JwtException e) {
            throw new AuthorizationException("Invalid token");
        }

        List<?> rolesRaw = claimsJws.getBody().get("roles", List.class);

        List<String> roles = new ArrayList<>();
        if (rolesRaw != null) {
            for (Object role : rolesRaw) {
                if (role instanceof String string) {
                    roles.add(string);
                } else {
                    throw new AuthorizationException("Role in the claim is not a string");
                }
            }
        } else {
            throw new ForbiddenException("User has no roles assigned");
        }

        Set<Role> userRoles = roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        if (!isUserAllowed(userRoles, permissionAnnotation)) {
            throw new AuthorizationException("User is unauthorized");
        }else{
            return true;
        }

    }

    private boolean isUserAllowed(Set<Role> userRoles, PermissionsAllowed permissionAnnotation) {
        Set<Role> rolesAllowed = EnumSet.copyOf(Arrays.asList(permissionAnnotation.value()));
        return !Collections.disjoint(rolesAllowed, userRoles);
    }

}
