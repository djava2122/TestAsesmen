package com.example.testasesmen.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.MailSender;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class SendEmail {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration configuration;

    @Async
    public void sendMailMessage(final SimpleMailMessage simpleMailMessage) {
        this.mailSender.send(simpleMailMessage);
    }

    @Async
    public void sendTemplateMessage(Mail mail) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // Set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template t = configuration.getTemplate(mail.getBody());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

            System.out.println("HTML "+html);

            helper.setText(html, true);
            helper.setSubject(Objects.requireNonNull(mail.getSubject()));
            helper.setFrom(Objects.requireNonNull(mail.getRecipient()));
            javaMailSender.send(message);

        } catch (IOException | TemplateException e) {
            System.err.println("Exception: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

}
