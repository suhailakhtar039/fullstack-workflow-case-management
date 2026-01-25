package com.caseflow.service.impl;

import com.caseflow.domain.Case;
import com.caseflow.domain.CaseStatusHistory;
import com.caseflow.domain.User;
import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.domain.enums.CaseType;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.exception.CaseNotFound;
import com.caseflow.exception.StatusDidNotMatchException;
import com.caseflow.repository.CaseRepository;
import com.caseflow.repository.CaseStatusHistoryRepository;
import com.caseflow.repository.UserRepository;
import com.caseflow.service.CaseService;
import com.caseflow.util.CaseStatusTransitionValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {
    @Autowired
    CaseRepository caseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CaseStatusHistoryRepository caseStatusHistoryRepository;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','CASE_MANAGER')")
    public CaseResponse createCase(CaseCreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User creator = userRepository.findByUsername(username).orElseThrow();//TODO: implement custom error handler

        Case c = new Case();
        c.setCaseNumber("CASE - " + System.currentTimeMillis());
        c.setTitle(request.title());
        c.setCaseType(CaseType.valueOf(request.type()));
        c.setPriority(request.priority());
        c.setStatus(CaseStatus.DRAFT);//TODO: Setting initial value to 'DRAFT'
        c.setCreatedBy(creator);

        Case saved = caseRepository.save(c);
        return new CaseResponse(
                saved.getId(),
                saved.getCaseNumber(),
                saved.getTitle(),
                saved.getStatus().name(),
                saved.getPriority().name(),
                saved.getCaseType().name(),
                creator.getUsername());
    }

    @Override
    public List<CaseResponse> getAllCases() {
        List<Case> cases = caseRepository.findAll();

        return cases
                .stream()
                .map(c -> new CaseResponse(
                        c.getId(),
                        c.getCaseNumber(),
                        c.getTitle(),
                        c.getStatus().name(),
                        c.getPriority().name(),
                        c.getCaseType().name(),
                        c.getCreatedBy().getUsername()
                ))
                .toList();
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
        Case c = caseRepository.findById(caseId).orElseThrow(() -> new CaseNotFound("Case with id " + caseId + " not found."));

        CaseStatus oldStatus = c.getStatus();
        if(!CaseStatusTransitionValidator.isValid(oldStatus, newStatus)){
            throw new StatusDidNotMatchException("Old Status: " + oldStatus + " new status: "+ newStatus);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        c.setStatus(newStatus);
        caseRepository.save(c);

        CaseStatusHistory history = new CaseStatusHistory();
        history.setCaseEntity(c);
        history.setOldStatus(oldStatus);
        history.setNewState(newStatus);
        history.setChangedBy(user);

        caseStatusHistoryRepository.save(history);
    }
}
