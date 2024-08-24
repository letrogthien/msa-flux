package com.gin.msaflux.auth_service.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;



@Table(name = "roles")
@Data
public class Role{
    @Id
    private Long id;

    private String name ;
}