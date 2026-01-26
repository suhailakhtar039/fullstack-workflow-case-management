package com.caseflow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "workflows")
@Data
public class Workflow extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String name;

    private boolean active = true;
}
