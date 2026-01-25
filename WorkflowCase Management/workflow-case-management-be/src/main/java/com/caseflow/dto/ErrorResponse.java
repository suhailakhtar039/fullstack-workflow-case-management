package com.caseflow.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        int status,
        String timestamp
) {
    public ErrorResponse{
        timestamp = LocalDateTime.now().toString();
    }
}
