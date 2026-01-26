package com.caseflow.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="case_workflow_instances")
@Data
public class CaseWorkflowInstance extends BaseEntity{

    @OneToOne(optional = false)
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @Column(nullable = false)
    private int currentStepOrder;

}
