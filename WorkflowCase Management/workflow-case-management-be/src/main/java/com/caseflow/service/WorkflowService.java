package com.caseflow.service;

import com.caseflow.domain.Case;
import com.caseflow.domain.WorkflowStep;
import com.caseflow.domain.enums.ApprovalDecision;

public interface WorkflowService {

    void startWorkflow(Case caseEntity);

    WorkflowStep getCurrentStep(Long caseId);

    void submitApproval(Long caseId, ApprovalDecision decision, String comments);
}
