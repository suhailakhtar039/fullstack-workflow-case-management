package com.caseflow.repository;

import com.caseflow.domain.CaseWorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseWorkflowInstanceRepository extends JpaRepository<CaseWorkflowInstance, Long> {
    Optional<CaseWorkflowInstance> findByCaseEntityId(Long caseId);

    @Query("SELECT i FROM CaseWorkflowInstance i" +
            " JOIN WorkflowStep s " +
            "ON s.workflow = i.workflow AND s.stepOrder = i.currentStepOrder" +
            " WHERE s.allowedRole=:role")
    List<CaseWorkflowInstance> findPendingByRole(String role);
}
