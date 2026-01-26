package com.caseflow.domain;

import com.caseflow.domain.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "workflow_steps")
@Data
public class WorkflowStep extends BaseEntity{

    @ManyToOne(optional = false)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @Column(nullable = false)
    private int stepOrder;

    @Column(nullable = false)
    private String allowedRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus targetStatus;

}
