package com.caseflow.dto;

public record PendingTaskResponse(
        Long caseId,
        String caseNumber,
        String title,
        String currentStatus,
        String pendingRole,
        int stepOrder,
        String targetStatus
) {
}
