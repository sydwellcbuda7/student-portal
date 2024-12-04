package za.co.student_management.student_portal.model.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTemplateType {

    PASSWORD_RESET("password-reset");

    private final String name;

    }
