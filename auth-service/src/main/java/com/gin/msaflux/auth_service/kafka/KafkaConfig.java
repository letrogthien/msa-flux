package com.gin.msaflux.auth_service.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic registerUserTopic(){
        return TopicBuilder.name("register")
                .replicas(2)
                .partitions(3)
                .build();
    }

}
