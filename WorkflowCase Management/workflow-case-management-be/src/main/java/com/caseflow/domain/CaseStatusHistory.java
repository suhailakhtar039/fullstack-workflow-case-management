package com.caseflow.domain;

import com.caseflow.domain.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "case_state_history")
@Data
public class CaseStatusHistory  extends BaseEntity{

    @ManyToOne(optional = false)
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus newState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "changed_by")
    private User changedBy;

}
