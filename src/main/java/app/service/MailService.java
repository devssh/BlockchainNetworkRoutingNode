package app.service;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;
import java.util.Scanner;

import static app.service.RegistrationManager.RegistrationCodes;


public class MailService {

    public static void SendMailWithConfirmationCodes(String to) {
        SendMail(to, "Activate your Account", "Your activation code is " + RegistrationCodes.get(to), null);
    }

    public static void SendMail(String to, String subject, String text, InputStreamSource inputStreamSource) {

        try {
            JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
            emailSender.setHost("smtp.gmail.com");
            emailSender.setPort(587);

            Scanner scanner = new Scanner(new File("password.dat"));
            emailSender.setUsername(scanner.nextLine());
            emailSender.setPassword(scanner.nextLine());

            Properties props = emailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "false");

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            if (inputStreamSource != null) {
                helper.addAttachment("coupon.pkpass", inputStreamSource);
            }
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
