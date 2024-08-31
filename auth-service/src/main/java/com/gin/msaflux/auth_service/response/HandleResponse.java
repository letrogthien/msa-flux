package com.gin.msaflux.auth_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandleResponse<T> {
    private T data;
    private HttpStatus statusCode;
    private final Date timeStamp = new Date(System.currentTimeMillis());
}