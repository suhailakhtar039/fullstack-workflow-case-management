package com.caseflow.repository;

import com.caseflow.domain.Workflow;
import com.caseflow.domain.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {
    Optional<WorkflowStep> findByWorkflowAndStepOrder(Workflow workflow, int stepOrder);
}
