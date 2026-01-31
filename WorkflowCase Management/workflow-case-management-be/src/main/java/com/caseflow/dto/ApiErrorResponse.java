package com.caseflow.dto;


public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path,
        long timestamp
) {
}
