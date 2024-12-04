package za.co.student_management.student_portal.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import za.co.student_management.student_portal.control.TokenAuthentication;
import za.co.student_management.student_portal.model.UserEntity;
import za.co.student_management.student_portal.service.StudentService;


@Component
public class JWTSecurityProvider implements AuthenticationProvider {

    private final StudentService studentService;

    @Autowired
    public JWTSecurityProvider(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        Long userId = Long.parseLong(tokenAuthentication.getUserId());
        UserEntity user = studentService.getStudentById(userId);
        tokenAuthentication.setUser(user);
        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.isAssignableFrom(authentication);
    }
}
