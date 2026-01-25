package com.caseflow.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        String uri,
        String message,
        HttpStatus status,
        String timestamp
) {
    public ErrorResponse(String uri, String message, HttpStatus status){
        this(uri, message, status, LocalDateTime.now().toString());
    }
}
