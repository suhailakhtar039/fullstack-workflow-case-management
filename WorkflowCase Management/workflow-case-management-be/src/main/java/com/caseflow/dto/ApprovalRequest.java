package com.caseflow.dto;

import com.caseflow.domain.enums.ApprovalDecision;

public record ApprovalRequest(
        ApprovalDecision decision,
        String comments
) {
}
