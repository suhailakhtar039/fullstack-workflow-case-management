package com.caseflow.util;

import com.caseflow.domain.enums.CaseStatus;

public class CaseStatusTransitionValidator {
    private CaseStatusTransitionValidator(){}

    public static boolean isValid(CaseStatus from, CaseStatus to){
        return switch (from){
            case DRAFT -> to == CaseStatus.FILED;
            case FILED -> to == CaseStatus.IN_REVIEW;
            case IN_REVIEW -> to == CaseStatus.APPROVED || to == CaseStatus.REJECTED;
            case APPROVED, REJECTED -> to == CaseStatus.CLOSED;
            default -> false;
        };
    }
}
