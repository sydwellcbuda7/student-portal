package za.co.student_management.student_portal.transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.student_management.student_portal.model.datatype.NotificationTemplateType;

import java.io.File;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTO {
    private String sender;
    private String receiver;
    private String subject;
    private NotificationTemplateType notificationTemplateType;
    private Map<String, Object> model = new HashMap<>();
}
