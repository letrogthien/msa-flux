package com.gin.msaflux.auth_service.gmail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String sub, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("letrogthien@gmail.com");
        message.setTo(toEmail);
        message.setSubject(sub);
        message.setText(body);

        javaMailSender.send(message);
    }
}