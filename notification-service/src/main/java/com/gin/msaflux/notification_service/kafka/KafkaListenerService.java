package com.gin.msaflux.notification_service.kafka;

import com.gin.msaflux.notification_service.gmail.EmailSender;

import com.gin.msaflux.notification_service.kafka.dto.ForgotPassNotify;
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
    private final KafkaUtils kafkaUtils;

    @KafkaListener(
            topics = "forget-password-notify-topic",
            concurrency = "3",
            groupId = "forget-password-notify-topic-gr1"
    )
    public Mono<Void> forgotPasswordNotify(final String jsonNodeString) {
        return kafkaUtils.jsonNodeToObject(jsonNodeString, ForgotPassNotify.class)
                .flatMap(forgotPassNotify -> emailSender.sendEmail(forgotPassNotify.getEmail(), "new pass", forgotPassNotify.getNewPassword())
        ).then();

    }


}
