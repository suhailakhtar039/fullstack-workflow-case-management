package com.caseflow.domain;

import com.caseflow.domain.enums.CaseStatus;

public record TransitionOptions(CaseStatus status, String label) {
}
