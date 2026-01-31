package com.caseflow.dto;


import com.caseflow.domain.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CaseCreateRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Type is required")
        String type,

        @NotNull(message = "Priority is required")
        Priority priority
) {
}
