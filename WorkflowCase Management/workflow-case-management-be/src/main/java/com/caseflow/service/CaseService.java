package com.caseflow.service;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseResponse;

import java.util.List;

public interface CaseService {
    CaseResponse createCase(CaseCreateRequest request);
    List<CaseResponse> getAllCases();
    void transitionStatus(Long caseId, CaseStatus newStatus);
}
