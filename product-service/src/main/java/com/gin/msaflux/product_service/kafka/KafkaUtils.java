package com.gin.msaflux.product_service.kafka;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KafkaUtils {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private Mono<String> jsonNodeToString(JsonNode node) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(node));
    }


    private Mono<JsonNode> objectToJsonNode(Object object) {
        return Mono.fromCallable(() -> objectMapper.valueToTree(object));
    }


    private Mono<JsonNode> stringToJsonNode(String nodeString) {
        return Mono.fromCallable(() -> objectMapper.readTree(nodeString));
    }
    public <T> Mono<T> jsonNodeToObject(String jsonNodeString, Class<T> clazz) {
        return stringToJsonNode(jsonNodeString)
                .flatMap(jsonNode -> Mono.fromCallable(() -> objectMapper.convertValue(jsonNode, clazz)));
    }
    public Mono<String> sendMessage(String topic, Object object) {
        return objectToJsonNode(object)
                .flatMap(this::jsonNodeToString)
                .flatMap(jsonString -> Mono.fromFuture(kafkaTemplate.send(topic, jsonString)).thenReturn(jsonString));
    }


}
