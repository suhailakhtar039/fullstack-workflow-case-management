package com.caseflow.util;

import com.caseflow.domain.enums.CaseStatus;

import java.util.Set;

public class CaseStatusTransitionValidator {
    private CaseStatusTransitionValidator(){}

    public static boolean isValid(
            CaseStatus from,
            CaseStatus to,
            Set<String> roles
    ) {

        return switch (from) {

            case DRAFT ->
                    to == CaseStatus.FILED &&
                            roles.contains("MANAGER");

            case FILED ->
                    to == CaseStatus.IN_REVIEW &&
                            roles.contains("REVIEWER");

            case IN_REVIEW ->
                    (to == CaseStatus.APPROVED || to == CaseStatus.REJECTED) &&
                            roles.contains("APPROVER");

            case APPROVED, REJECTED ->
                    to == CaseStatus.CLOSED &&
                            roles.contains("ADMIN");

            default -> false;
        };
    }

}
