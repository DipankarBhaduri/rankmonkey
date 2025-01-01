package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.auth.JwtSvc;
import com.rankmonkeysvc.constants.EventType;
import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.services.EmailSvc;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.Properties;
import com.rankmonkeysvc.utils.EventLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.rankmonkeysvc.messages.StaticMessages.*;

@Component
public class EmailSvcImpl implements EmailSvc {
    private final EventLogHelper eventLogHelper;
    private final JwtSvc jwtSvc;

    @Autowired
    public EmailSvcImpl(
            EventLogHelper eventLogHelper,
            JwtSvc jwtSvc
    ) {
        this.eventLogHelper = eventLogHelper;
        this.jwtSvc = jwtSvc;
    }

    @Override
    public MessageResponse sendEmail(
            String recipientEmail,
            User user
    ) {
        String senderEmail = SENDER_EMAIL;
        String emailContent = getEmailBody(recipientEmail, user);
        Properties props = new Properties();
        props.put(MAIL_SMTP_AUTH, TRUE);
        props.put(MAIL_SMTP_STARTTLS_ENABLE, TRUE);
        props.put(MAIL_SMTP_HOST, MAIL_GMAIL_DOT_COM);
        props.put(MAIL_GMAIL_PORT, MAIL_GMAIL_PORT_NUMBER);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(VERIFY_EMAIL_USING_EMAIL);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailContent, MAIL_CONTENT_TYPE);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception e) {
            eventLogHelper.createEventLog(EventType.SET_PASSWORD_EMAIL_SENT_FAILURE,
                    user.getId().toString(), new HashMap<String, String>() {
                {
                    put("reason", e.getMessage());
                }
            }, null);
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getEmailBody(String email, User user) {
        String token = jwtSvc.generateDummyTokenWithHS256(user, true);

        String emailContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Verification</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f3f4f6; margin: 0; padding: 0;\">\n" +
                "    <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 600px; width: 100%; margin: 20px auto; background: #ffffff; border: 1px solid #e5e7eb; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: #2a9d8f; color: #ffffff; text-align: center; padding: 20px; border-top-left-radius: 10px; border-top-right-radius: 10px;\">\n" +
                "                <h1 style=\"margin: 0; font-size: 24px; font-weight: bold;\">Welcome to RankMonkey!</h1>\n" +
                "                <p style=\"margin: 5px 0 0; font-size: 16px;\">Verify your email to get started</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px; color: #374151; line-height: 1.6;\">\n" +
                "                <p style=\"margin: 0 0 16px;\">Hi there,</p>\n" +
                "                <p style=\"margin: 0 0 16px;\">Thank you for signing up to RankMonkey! We are excited to have you on board. To get started, please verify your email address by clicking the button below:</p>\n" +
                "                <p style=\"text-align: center; margin: 24px 0;\">\n" +
                "                    <a href=\"" + ADMIN_BASE_URL + "/?token=" + token +"\"\n" +
                "                       style=\"display: inline-block; padding: 14px 28px; background-color: #333366; color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold; border-radius: 5px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\">\n" +
                "                        Verify Your Email\n" +
                "                    </a>\n" +
                "                </p>\n" +
                "                <p style=\"margin: 0 0 16px;\">If the button above does not work, copy and paste the following link into your browser:</p>\n" +
                "                <p style=\"word-wrap: break-word; color: #2a9d8f; margin: 0 0 16px;\">\n" +
                "                    <a href=\"" + ADMIN_BASE_URL + "/?token=" + token + "\" style=\"color: #2a9d8f; text-decoration: none; font-size: 14px;\">\n" +
                "                        " + ADMIN_BASE_URL + "/?token=" + token +"\n" +
                "                    </a>\n" +
                "                </p>\n" +
                "                <p style=\"margin: 0;\">Once verified, you'll be able to set your password and start exploring our platform.</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: #f9fafb; padding: 20px; text-align: center; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;\">\n" +
                "                <p style=\"margin: 0 0 8px; font-size: 14px; color: #6b7280;\">Need help? <a href=\"mailto:heyrankmonkey@gmail.com\" style=\"color: #333366; text-decoration: none;\">Contact Support</a></p>\n" +
                "                <p style=\"margin: 0; font-size: 12px; color: #6b7280;\">&copy; 2024 RankMonkey. All rights reserved.</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        return emailContent;
    }
}