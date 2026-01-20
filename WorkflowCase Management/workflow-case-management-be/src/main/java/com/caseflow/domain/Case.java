package com.caseflow.domain;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.domain.enums.CaseType;
import com.caseflow.domain.enums.Priority;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cases")
@Data
public class Case extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String caseNumber;

    private String title;

    @Enumerated(EnumType.STRING)
    private CaseType caseType;

    @Enumerated(EnumType.STRING)
    private CaseStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
