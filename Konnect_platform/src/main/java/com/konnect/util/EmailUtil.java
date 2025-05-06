package com.konnect.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Utility class for sending emails using Mailjet SMTP
 */
public class EmailUtil {
    // Mailjet SMTP configuration
    private static final String HOST = "in-v3.mailjet.com";
    private static final String PORT = "587";
    private static final String USERNAME = "8f694513fa5e2e0b8bb9f8bfcd7a9663"; // API Key
    private static final String PASSWORD = "39809e32a9c67639b9435bb3039670cf"; // Secret Key
    private static final String FROM_EMAIL = "rameshsapkota900@gmail.com";
    private static final String FROM_NAME = "Konnect Platform";

    /**
     * Send an email
     *
     * @param toEmail Recipient email address
     * @param subject Email subject
     * @param body Email body (HTML)
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendEmail(String toEmail, String subject, String body) {
        try {
            // Set properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", HOST);
            props.put("mail.smtp.port", PORT);

            // Create session with authenticator
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Set HTML content
            message.setContent(body, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send verification email
     *
     * @param toEmail Recipient email address
     * @param verificationCode Verification code
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verify Your Email - Konnect Platform";
    
        String body = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f7fc; }"
                + ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; color: #2c3e50; font-size: 24px; margin-bottom: 20px; }"
                + ".content { font-size: 16px; color: #34495e; line-height: 1.5; }"
                + ".button { display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #3498db; color: white; text-decoration: none; border-radius: 5px; font-size: 16px; }"
                + ".footer { text-align: center; color: #7f8c8d; font-size: 14px; margin-top: 30px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>Welcome to Konnect Platform!</div>"
                + "<div class='content'>"
                + "<p>Thank you for registering. To complete your registration, please verify your email address.</p>"
                + "<p>Your verification code is: <strong style='font-size: 18px; color: #3498db;'>" + verificationCode + "</strong></p>"
                + "<p>Please enter this code on the verification page to activate your account.</p>"
                + "<p>If you did not register for a Konnect account, please ignore this email.</p>"
                + "<a href='#' class='button'>Verify Your Email</a>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Best regards,<br>The Konnect Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    
        return sendEmail(toEmail, subject, body);
    }
    
    public static boolean sendPasswordResetEmail(String toEmail, String resetCode) {
        String subject = "Password Reset - Konnect Platform";
    
        String body = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f7fc; }"
                + ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; color: #2c3e50; font-size: 24px; margin-bottom: 20px; }"
                + ".content { font-size: 16px; color: #34495e; line-height: 1.5; }"
                + ".button { display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #3498db; color: white; text-decoration: none; border-radius: 5px; font-size: 16px; }"
                + ".footer { text-align: center; color: #7f8c8d; font-size: 14px; margin-top: 30px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>Password Reset Request</div>"
                + "<div class='content'>"
                + "<p>We received a request to reset your password for your Konnect account.</p>"
                + "<p>Your password reset code is: <strong style='font-size: 18px; color: #3498db;'>" + resetCode + "</strong></p>"
                + "<p>Please enter this code on the password reset page to set a new password.</p>"
                + "<p>If you did not request a password reset, please ignore this email or contact support if you have concerns.</p>"
                + "<a href='#' class='button'>Reset Your Password</a>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Best regards,<br>The Konnect Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    
        return sendEmail(toEmail, subject, body);
    }
    
}