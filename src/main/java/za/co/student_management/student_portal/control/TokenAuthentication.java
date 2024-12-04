package za.co.student_management.student_portal.control;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import za.co.student_management.student_portal.model.UserEntity;

import java.util.Collection;
import java.util.Collections;


@Getter
@Setter
public class TokenAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -1949976839306453197L;
    private String userId;
    private UserEntity user;

    public TokenAuthentication(String userId) {
        super(Collections.emptyList());
        this.userId = userId;
        this.setAuthenticated(true);
    }

    public TokenAuthentication(Collection<? extends GrantedAuthority> authorities, UserEntity user,
                               String userId) {
        super(authorities);
        this.userId = userId;
        this.user = user;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
