package com.caseflow.repository;

import com.caseflow.domain.Case;
import com.caseflow.domain.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    Optional<Case> findByCaseNumber(String caseNumber);

    Page<Case> findByStatus(CaseStatus status, Pageable pageable);

    Page<Case> findByCaseNumberContainingIgnoreCase(String caseNumber, Pageable pageable);

    Page<Case> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Case> findByCreatedByUsername(String username, Pageable pageable);

    long countByStatus(CaseStatus status);
}
