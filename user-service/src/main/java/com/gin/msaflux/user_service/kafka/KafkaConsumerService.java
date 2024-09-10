package com.gin.msaflux.user_service.kafka;

import com.gin.msaflux.user_service.models.User;
import com.gin.msaflux.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(topics = "register", groupId = "main-group", concurrency = "2")
    public void listen(String username) {
        userRepository.save(User.builder().username(username).build())
                .doOnSuccess(user -> kafkaTemplate.send("register-success", username)) //send to Notification Service
                .doOnError(err -> kafkaTemplate.send("register-failed", username))  //send to Auth Service for rollback handler
                .subscribe();
    }

}
