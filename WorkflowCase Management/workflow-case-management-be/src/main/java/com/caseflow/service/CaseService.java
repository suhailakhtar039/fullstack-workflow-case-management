package com.caseflow.service;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.dto.CaseStatusHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CaseService {
    CaseResponse createCase(CaseCreateRequest request);
    List<CaseResponse> getAllCases();
    void transitionStatus(Long caseId, CaseStatus newStatus);
    List<CaseStatusHistoryResponse> getStatusHistory(Long caseId);
    Page<CaseResponse> searchCases(String caseNumber, String title, CaseStatus status, Pageable pageable);
}
