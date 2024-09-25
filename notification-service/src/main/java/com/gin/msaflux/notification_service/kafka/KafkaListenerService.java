package com.gin.msaflux.notification_service.kafka;

import com.gin.msaflux.common.kafka.payload.ForgotPassNotify;
import com.gin.msaflux.notification_service.gmail.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenerService {
    private final EmailSender emailSender;

    @KafkaListener(
            topics = "forget-password-notify-topic",
            concurrency = "3",
            groupId = "forget-password-notify-topic-gr1"
    )
    public Mono<Void> forgotPasswordNotify(final ForgotPassNotify forgotPassNotify) {
        log.error("forgotPasswordNotify");
        return emailSender.sendEmail(forgotPassNotify.getEmail(), "new pass", forgotPassNotify.getNewPassword());
    }


}
