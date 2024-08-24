package com.gin.msaflux.auth_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandleResponse<T> {
    private T data;
    private Integer statusCode;
    private Date timeStamp;
}