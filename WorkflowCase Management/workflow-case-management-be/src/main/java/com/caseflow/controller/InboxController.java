package com.caseflow.controller;

import com.caseflow.dto.PendingTaskResponse;
import com.caseflow.service.WorkflowService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inbox")
public class InboxController {
    private final WorkflowService workflowService;

    public InboxController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/my")
    public List<PendingTaskResponse> myPendingTasks(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities()
                .stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .flatMap(role -> workflowService.getPendingTasksForRole(role).stream())
                .toList();
    }
}
