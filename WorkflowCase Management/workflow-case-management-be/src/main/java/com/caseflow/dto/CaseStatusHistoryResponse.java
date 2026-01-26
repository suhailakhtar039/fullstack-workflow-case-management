package com.caseflow.dto;

import java.time.LocalDateTime;

public record CaseStatusHistoryResponse(
        String oldStatus,
        String newStatus,
        String changedBy,
        LocalDateTime changedAt
) {
}
