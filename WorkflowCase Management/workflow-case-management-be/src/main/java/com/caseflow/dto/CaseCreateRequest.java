package com.caseflow.dto;

public record CaseCreateRequest(
        String title,
        String description,
        String type,
        String priority
) {
}
