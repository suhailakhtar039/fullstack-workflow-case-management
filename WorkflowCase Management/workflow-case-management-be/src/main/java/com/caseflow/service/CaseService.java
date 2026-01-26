package com.caseflow.service;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.dto.CaseStatusHistoryResponse;

import java.util.List;

public interface CaseService {
    CaseResponse createCase(CaseCreateRequest request);
    List<CaseResponse> getAllCases();
    void transitionStatus(Long caseId, CaseStatus newStatus);
    List<CaseStatusHistoryResponse> getStatusHistory(Long caseId);
}
