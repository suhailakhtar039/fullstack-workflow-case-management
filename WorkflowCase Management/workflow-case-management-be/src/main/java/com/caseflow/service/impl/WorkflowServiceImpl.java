package com.caseflow.service.impl;

import com.caseflow.domain.*;
import com.caseflow.domain.enums.ApprovalDecision;
import com.caseflow.dto.PendingTaskResponse;
import com.caseflow.exception.ItemNotFoundException;
import com.caseflow.repository.*;
import com.caseflow.service.WorkflowService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final String WORKFLOW_NAME = "STANDARD_CASE_WORKFLOW";

    private final WorkflowRepository workflowRepository;
    private final WorkflowStepRepository stepRepository;
    private final CaseWorkflowInstanceRepository instanceRepository;
    private final ApprovalRepository approvalRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    public WorkflowServiceImpl(WorkflowRepository workflowRepository, WorkflowStepRepository stepRepository, CaseWorkflowInstanceRepository instanceRepository, ApprovalRepository approvalRepository, CaseRepository caseRepository, UserRepository userRepository) {
        this.workflowRepository = workflowRepository;
        this.stepRepository = stepRepository;
        this.instanceRepository = instanceRepository;
        this.approvalRepository = approvalRepository;
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void startWorkflow(Case caseEntity) {
        Workflow workflow = workflowRepository
                .findByNameAndActiveTrue(WORKFLOW_NAME)
                .orElseThrow(() -> new ItemNotFoundException("Not able to find workflow name"));

        CaseWorkflowInstance instance = new CaseWorkflowInstance();
        instance.setCaseEntity(caseEntity);
        instance.setWorkflow(workflow);
        instance.setCurrentStepOrder(1);

        instanceRepository.save(instance);
    }

    @Override
    public WorkflowStep getCurrentStep(Long caseId) {
        CaseWorkflowInstance instance = instanceRepository
                .findByCaseEntityId(caseId)
                .orElseThrow(() -> new ItemNotFoundException("Not able to find case instance"));
        return stepRepository
                .findByWorkflowAndStepOrder(instance.getWorkflow(), instance.getCurrentStepOrder())
                .orElseThrow(() -> new ItemNotFoundException("Step not found"));
    }

    @Override
    public void submitApproval(Long caseId, ApprovalDecision decision, String comments) {
        CaseWorkflowInstance instance = instanceRepository
                .findByCaseEntityId(caseId)
                .orElseThrow(() -> new ItemNotFoundException("instance not found"));

        WorkflowStep step = getCurrentStep(caseId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        // Role check

        boolean allowed = auth.getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_" + step.getAllowedRole()) ||
                                a.getAuthority().equals("ROLE_ADMIN")
                );


        if (!allowed) {
            throw new AccessDeniedException("User not allowed for this workflow step");
        }
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow();

        // Record approval
        Approval approval = new Approval();
        approval.setCaseEntity(caseEntity);
        approval.setWorkflowStep(step);
        approval.setApprovedBy(user);
        approval.setDecision(decision);
        approval.setComments(comments);

        approvalRepository.save(approval);

        //apply status
        caseEntity.setStatus(step.getTargetStatus());
        caseRepository.save(caseEntity);

        //move to next step
        instance.setCurrentStepOrder(instance.getCurrentStepOrder() + 1);
        instanceRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PendingTaskResponse> getPendingTasksForRole(String role){
        return null;
    }
}
