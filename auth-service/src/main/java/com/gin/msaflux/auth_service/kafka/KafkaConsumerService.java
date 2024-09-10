package com.gin.msaflux.auth_service.kafka;


import com.gin.msaflux.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final UserRepository userRepository;


    @KafkaListener(topics = "register-failed", groupId = "register-failed-group", concurrency = "2") // listen if failed
    public void listen(String username) {
        rollBackUser(username);     // rollback user
    }

    @Retryable(
            backoff = @Backoff(delay = 5000),
            maxAttempts = 3
    )

    //rollback method
    private void rollBackUser(String username) {
        userRepository.findByUsername(username)
                .flatMap(userRepository::delete)
                .subscribe();
    }
}
