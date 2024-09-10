package com.gin.msaflux.user_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic registerSuccessTopic(){
        return TopicBuilder.name("register-success")
                .replicas(2)
                .partitions(3)
                .build();
    }
    @Bean
    public NewTopic registerFailTopic(){
        return TopicBuilder.name("register-failed")
                .replicas(2)
                .partitions(3)
                .build();
    }
}
