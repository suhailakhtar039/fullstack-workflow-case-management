package com.caseflow.dto;

public record CaseResponse(Long id,
                           String caseNumber,
                           String title,
                           String status,
                           String priority,
                           String type,
                           String createdBy) {
}
