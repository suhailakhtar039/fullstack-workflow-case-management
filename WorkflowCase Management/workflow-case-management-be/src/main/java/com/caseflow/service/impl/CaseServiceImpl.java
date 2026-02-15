package com.caseflow.service.impl;

import com.caseflow.domain.Case;
import com.caseflow.domain.CaseStatusHistory;
import com.caseflow.domain.User;
import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.domain.enums.CaseType;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.dto.CaseStatusHistoryResponse;
import com.caseflow.dto.DashboardResponse;
import com.caseflow.exception.CaseNotFound;
import com.caseflow.exception.StatusDidNotMatchException;
import com.caseflow.repository.CaseRepository;
import com.caseflow.repository.CaseStatusHistoryRepository;
import com.caseflow.repository.UserRepository;
import com.caseflow.service.CaseService;
import com.caseflow.service.WorkflowService;
import com.caseflow.util.CaseStatusTransitionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CaseServiceImpl implements CaseService {
    @Autowired
    CaseRepository caseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CaseStatusHistoryRepository caseStatusHistoryRepository;

    @Autowired
    WorkflowService workflowService;

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
        workflowService.startWorkflow(saved);
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
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void transitionStatus(Long caseId, CaseStatus newStatus) {
        Case c = caseRepository.findById(caseId).orElseThrow(() -> new CaseNotFound("Case with id " + caseId + " not found."));

        CaseStatus oldStatus = c.getStatus();
        Authentication authentication = SecurityContextHolder.
                getContext().
                getAuthentication();
        Set<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        if (!CaseStatusTransitionValidator.isValid(oldStatus, newStatus, roles)) {
            throw new StatusDidNotMatchException("Old Status: " + oldStatus + " new status: " + newStatus);
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

    @Override
    public List<CaseStatusHistoryResponse> getStatusHistory(Long caseId) {
        return caseStatusHistoryRepository.findByCaseEntityIdOrderByCreatedAtAsc(caseId)
                .stream()
                .map(h -> new CaseStatusHistoryResponse(
                        h.getOldStatus().name(),
                        h.getNewState().name(),
                        h.getChangedBy().getUsername(),
                        h.getCreatedAt()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CaseResponse> searchCases(String caseNumber, String title, CaseStatus status, Pageable pageable) {
        Page<Case> page;

        boolean hasSearch = (caseNumber != null && !caseNumber.isBlank()) ||
                (title != null && !title.isBlank());

        if (hasSearch && status != null) {
            page = caseRepository.searchByCaseNumberOrTitleAndStatus(
                    caseNumber, title, status, pageable);
        }
        else if (hasSearch) {
            page = caseRepository.searchByCaseNumberOrTitle(
                    caseNumber, title, pageable);
        }
        else if (status != null) {
            page = caseRepository.findByStatus(status, pageable);
        }
        else {
            page = caseRepository.findAll(pageable);
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return page.map(c -> new CaseResponse(
                c.getId(),
                c.getCaseNumber(),
                c.getTitle(),
                c.getStatus().name(),
                c.getPriority().name(),
                c.getCaseType().name(),
                username
        ));
    }

    @Override
    public Page<CaseResponse> getMyCases(Pageable pageable) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return caseRepository
                .findByCreatedByUsername(username, pageable)
                .map(c -> new CaseResponse(
                        c.getId(),
                        c.getCaseNumber(),
                        c.getTitle(),
                        c.getStatus().name(),
                        c.getPriority().name(),
                        c.getCaseType().name(),
                        username
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        long total = caseRepository.count();
        long pending = caseRepository.countByStatus(CaseStatus.DRAFT)
                + caseRepository.countByStatus(CaseStatus.FILED)
                + caseRepository.countByStatus(CaseStatus.IN_REVIEW);

        long completed = caseRepository.countByStatus(CaseStatus.CLOSED);

        return new DashboardResponse(total, pending, completed);
    }

    public Set<CaseStatus> getAllowedTransitions(Long caseId) {

        Case c = caseRepository.findById(caseId)
                .orElseThrow(() -> new CaseNotFound("Case not found"));

        CaseStatus currentStatus = c.getStatus();

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return Arrays.stream(CaseStatus.values())
                .filter(next ->
                        CaseStatusTransitionValidator
                                .isValid(currentStatus, next, roles))
                .collect(Collectors.toSet());
    }


}
