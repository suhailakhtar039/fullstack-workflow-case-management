package com.caseflow.controller;

import com.caseflow.domain.WorkflowStep;
import com.caseflow.dto.ApprovalRequest;
import com.caseflow.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    // view pending step
    @GetMapping("/cases/{id}/pending-step")
    public WorkflowStep getPendingStep(@PathVariable Long id){
        return workflowService.getCurrentStep(id);
    }

    // submit approval
    @PostMapping("/cases/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestBody ApprovalRequest request){
        workflowService.submitApproval(id, request.decision(), request.comments());
        return ResponseEntity.noContent().build();
    }

}
