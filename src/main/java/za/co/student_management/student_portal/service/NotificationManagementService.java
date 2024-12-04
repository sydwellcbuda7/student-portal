package za.co.student_management.student_portal.service;

import za.co.student_management.student_portal.transfer_object.EmailTO;

public interface NotificationManagementService {
    void sendEmailNotification(EmailTO email);

}
