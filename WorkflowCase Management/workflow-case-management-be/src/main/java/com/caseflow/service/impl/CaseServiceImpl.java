package com.caseflow.service.impl;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.repository.CaseRepository;
import com.caseflow.service.CaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {
    @Autowired
    CaseRepository caseRepository;

    @Override
    public CaseResponse createCase(CaseCreateRequest request) {
        return null;
    }

    @Override
    public List<CaseResponse> getAllCases() {
        return List.of();
    }

    @Override
    @PreAuthorize("""
        (hasRole('CASE_MANAGER') and #newStatus.name()=='FILED') or
        (hasRole('REVIEWER') and #newStatus.name() == 'IN_REVIEW') or
        (hasRole('APPROVER') and (#newStatus.name() == 'APPROVED' or #newStatus.name() == 'REJECTED')) or
        (hasRole('ADMIN') and #newStatus.name() == 'CLOSED')
    """
    )
    @Transactional
    public void transitionStatus(Long caseId, CaseStatus newStatus) {

    }
}
