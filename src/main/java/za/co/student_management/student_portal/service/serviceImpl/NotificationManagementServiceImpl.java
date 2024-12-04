package za.co.student_management.student_portal.service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import za.co.student_management.student_portal.service.NotificationManagementService;
import za.co.student_management.student_portal.transfer_object.EmailTO;

import java.nio.charset.StandardCharsets;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationManagementServiceImpl implements NotificationManagementService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    @Override
    public void sendEmailNotification(EmailTO email) {
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(email.getModel());
            String html = templateEngine.process(email.getNotificationTemplateType().getName(), context);

            if (Objects.nonNull(email.getSender())) {
                helper.setFrom(email.getSender());
                helper.setReplyTo(email.getSender());

            }else{
                helper.setFrom("No-Reply <noreply@student-portal.co.za>");
                helper.setReplyTo("No-Reply <noreply@student-portal.co.za>");

            }

            helper.setSubject(email.getSubject());
            helper.setText(html, true);

            sendEmailToRecipient(email.getReceiver(), message, helper);

        } catch (MessagingException e) {
            NotificationManagementServiceImpl.log.error("Error while sending email notification ", e);
        }

    }

    private void sendEmailToRecipient(String recipient, MimeMessage message, MimeMessageHelper helper) {
        CompletableFuture.runAsync(() -> {
            try {
                helper.setTo(recipient);
                javaMailSender.send(message);
            } catch (MailException | MessagingException e) {
                NotificationManagementServiceImpl.log.error("Error while sending email notification to recipient", e);
            }
        });
    }

}
