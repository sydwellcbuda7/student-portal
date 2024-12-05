package za.co.student_management.student_portal.service;

import za.co.student_management.student_portal.control.Authentication;
import za.co.student_management.student_portal.control.Credentials;
import za.co.student_management.student_portal.transfer_object.ChangePasswordTo;
import za.co.student_management.student_portal.transfer_object.SessionContextTo;

import java.util.Set;

public interface AccessControlService {

    Authentication loginUser(Credentials credentials);

    void changePassword(ChangePasswordTo changePasswordTo);

    void resetPassword(String email);

    Set<SessionContextTo> getUserSessionContexts();
}
