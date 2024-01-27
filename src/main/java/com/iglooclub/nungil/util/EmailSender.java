package com.iglooclub.nungil.util;

import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.GlobalErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public void send(EmailMessage emailMessage) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setTo(emailMessage.getTo());
            messageHelper.setSubject(emailMessage.getSubject());

            String content = templateEngine.process(emailMessage.getTemplate(), emailMessage.getContext());

            messageHelper.setText(content, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new GeneralException(GlobalErrorResult.MESSAGING_EXCEPTION, e);
        }
    }
}
