package com.gin.msaflux.notification_service.gmail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;

    public Mono<Void> sendEmail(String toEmail, String sub, String body){
        return Mono.fromRunnable(
                ()->{
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("letrogthien@gmail.com");
                    message.setTo(toEmail);
                    message.setSubject(sub);
                    message.setText(body);
                    mailSender.send(message);
                }
        ).then();
    }
}