package com.caseflow.service.impl;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.service.CaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {
    @Override
    public CaseResponse createCase(CaseCreateRequest request) {
        return null;
    }

    @Override
    public List<CaseResponse> getAllCases() {
        return List.of();
    }

    @Override
    public void transitionStatus(Long caseId, CaseStatus newStatus) {

    }
}
