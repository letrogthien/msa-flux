package com.gin.msaflux.auth_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shops")
public class Shop {
    @Id
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private String location;
    private List<String> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
