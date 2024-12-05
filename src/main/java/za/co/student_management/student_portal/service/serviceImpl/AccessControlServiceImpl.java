package za.co.student_management.student_portal.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.student_management.student_portal.config.security.JWTService;
import za.co.student_management.student_portal.control.Authentication;
import za.co.student_management.student_portal.control.Credentials;
import za.co.student_management.student_portal.control.TokenAuthentication;
import za.co.student_management.student_portal.exception.custome.*;
import za.co.student_management.student_portal.model.PasswordResetEntity;
import za.co.student_management.student_portal.model.StudentEntity;
import za.co.student_management.student_portal.model.UserEntity;
import za.co.student_management.student_portal.model.UserRoleEntity;
import za.co.student_management.student_portal.model.datatype.NotificationTemplateType;
import za.co.student_management.student_portal.model.datatype.Role;
import za.co.student_management.student_portal.repository.PasswordResetRepository;
import za.co.student_management.student_portal.repository.UserRepository;
import za.co.student_management.student_portal.service.AccessControlService;
import za.co.student_management.student_portal.service.NotificationManagementService;
import za.co.student_management.student_portal.service.StudentService;
import za.co.student_management.student_portal.transfer_object.ChangePasswordTo;
import za.co.student_management.student_portal.transfer_object.EmailTO;
import za.co.student_management.student_portal.transfer_object.SessionContextTo;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessControlServiceImpl implements AccessControlService {
    @Value("${student-portal.host}")
    private String host;

    private final UserRepository userRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final PasswordResetRepository passwordResetRepository;
    private final NotificationManagementService managementService;


    @Override
    public Authentication loginUser(Credentials credentials) {
        UserEntity user = userRepository.findUserEntityByEmail(credentials.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

            boolean matches = passwordEncoder.matches(credentials.getPassword(), user.getPassword());
            if (matches) {
                return new Authentication(jwtService.generateToken(user),
                        user.getId(),
                        user.getAssignedRoles().stream()
                                .map(UserRoleEntity::getRole)
                                .collect(Collectors.toSet()));
            }
            throw new InvalidCredentialsException();

    }

    @Override
    public void changePassword(ChangePasswordTo changePasswordTo) {
        Optional<PasswordResetEntity> passwordResetEntity = passwordResetRepository
                .findPasswordResetEntityByToken(changePasswordTo.getToken());
        passwordResetEntity.ifPresent(pr -> {
            UserEntity user = pr.getUser();
            user.setPassword(passwordEncoder.encode(changePasswordTo.getPassword()));
            userRepository.save(user);
            pr.setExpiryDate(LocalDateTime.now());
            passwordResetRepository.save(pr);
        });
    }



    @Transactional
    @Override
    public void resetPassword(String email) {
        UserEntity user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with email " + email));
//        passwordResetRepository.markOldPasswordRestForUserAsExpired(user.getId());
        PasswordResetEntity passwordReset = new PasswordResetEntity();
        passwordReset.setUser(user);
        passwordReset.setToken(UUID.randomUUID().toString());
        passwordReset.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        setPasswordResetEmail( passwordResetRepository.save(passwordReset));
    }

    @Override
    public Set<SessionContextTo> getUserSessionContexts() {
        Set<SessionContextTo> sessionContexts = new HashSet<>();
        UserEntity userEntity = getCurrentlyLoggedInUser();
        Set<Role> roles = getCurrentlyLoggedInUserRoles();
        for (Role role : roles) {
            if(role.equals(Role.STUDENT)) {
                StudentEntity student = studentService.getStudentById(userEntity.getId());
                sessionContexts.add(
                        new SessionContextTo(
                                student.getId(), student.getStudentNumber(), student.getLastName(),
                               student.getEmail(),
                                Role.STUDENT
                        )
                );
            }
        }
        return sessionContexts;
    }


    public Long getUserIdForCurrentUser() {
//        Object object = SecurityContextHolder
//                .getContext();
//        System.out.println("\n "+ object.toString()+"\n");
//        TokenAuthentication authentication = (TokenAuthentication) SecurityContextHolder
//                .getContext().getAuthentication();
//        String userId = authentication.getUserId();
//        return Long.parseLong(userId);
        return 1L;
    }

public UserEntity getCurrentlyLoggedInUser() {
    return userRepository.findById(getUserIdForCurrentUser())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + getUserIdForCurrentUser()));
}

    public Set<Role> getCurrentlyLoggedInUserRoles() {
        return  getCurrentlyLoggedInUser().getAssignedRoles().stream().map(UserRoleEntity::getRole).collect(Collectors.toSet());

    }
    public void setPasswordResetEmail(PasswordResetEntity passwordReset) {
        Map<String, Object> model = new HashMap<>();
        String link = host + "/change-password?token=" + passwordReset.getToken();
        model.put("link", link);
        EmailTO emailTO = new EmailTO("noreply@etalente.co.za", passwordReset.getUser().getEmail(), "Reset Password",
                NotificationTemplateType.PASSWORD_RESET, model);
        System.out.println("\n " + "Link " + link + "\n");
        managementService.sendEmailNotification(emailTO);
    }



}
