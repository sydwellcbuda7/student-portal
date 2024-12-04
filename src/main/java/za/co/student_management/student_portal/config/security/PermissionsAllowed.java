package za.co.student_management.student_portal.config.security;



import za.co.student_management.student_portal.model.datatype.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface PermissionsAllowed {
    Role[] value();
}