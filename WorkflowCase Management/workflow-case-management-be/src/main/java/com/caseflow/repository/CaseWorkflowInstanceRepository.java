package com.caseflow.repository;

import com.caseflow.domain.CaseWorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaseWorkflowInstanceRepository extends JpaRepository<CaseWorkflowInstance, Long> {
    Optional<CaseWorkflowInstance> findByCaseEntityId(Long caseId);
}
