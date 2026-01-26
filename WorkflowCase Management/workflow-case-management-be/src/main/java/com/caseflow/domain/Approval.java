package com.caseflow.domain;

import com.caseflow.domain.enums.ApprovalDecision;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "approvals")
@Data
public class Approval extends BaseEntity{

    @ManyToOne(optional = false)
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workflow_step_id")
    private WorkflowStep workflowStep;

    @ManyToOne(optional = false)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalDecision decision;

    private String comments;

}
