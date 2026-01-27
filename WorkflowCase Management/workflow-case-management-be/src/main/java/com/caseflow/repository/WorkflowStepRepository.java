package com.caseflow.repository;

import com.caseflow.domain.Workflow;
import com.caseflow.domain.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {
    Optional<WorkflowStep> findByWorkflowAndStepOrder(Workflow workflow, int stepOrder);

    @Query("SELECT w FROM WorkflowStep w WHERE w.workflow= :workflow AND w.stepOrder = :stepOrder")
    Optional<WorkflowStep> findStep(Workflow workflow, int stepOrder);
}
