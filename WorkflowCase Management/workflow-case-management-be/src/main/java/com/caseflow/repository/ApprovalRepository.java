package com.caseflow.repository;

import com.caseflow.domain.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByCaseEntityIdOrderByCreatedAtAsc(Long caseId);
}
