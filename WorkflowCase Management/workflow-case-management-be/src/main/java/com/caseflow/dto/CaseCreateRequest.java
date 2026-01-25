package com.caseflow.dto;


import com.caseflow.domain.enums.Priority;

public record CaseCreateRequest(
        String title,
        String description,
        String type,
        Priority priority
) {
}
