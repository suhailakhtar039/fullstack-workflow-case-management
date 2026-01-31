package com.caseflow.dto;


import jakarta.validation.constraints.NotBlank;

public record ApiErrorResponse(
        @NotBlank(message = "Always enter status")
        int status,
        @NotBlank(message = "Tell the error")
        String error,
        @NotBlank(message = "Get message from unique exception handler")
        String message,
        @NotBlank(message = "Give the path where we are getting error")
        String path,
        @NotBlank(message = "Give the timestamp")
        long timestamp
) {
}
