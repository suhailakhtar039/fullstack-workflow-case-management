package com.caseflow.repository;

import com.caseflow.domain.Case;
import com.caseflow.domain.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c FROM Case c " +
            "WHERE (:caseNumber IS NOT NULL AND LOWER(c.caseNumber)" +
            " LIKE LOWER(CONCAT('%', :caseNumber, '%')))" +
            " OR (:title IS NOT NULL AND LOWER(c.title)" +
            " LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Case> searchByCaseNumberOrTitle(
            @Param("caseNumber") String caseNumber,
            @Param("title") String title,
            Pageable pageable
    );

    @Query("SELECT c FROM Case c" +
            " WHERE ( (:caseNumber IS NOT NULL AND LOWER(c.caseNumber) LIKE LOWER(CONCAT('%', :caseNumber, '%')))" +
            " OR (:title IS NOT NULL AND LOWER(c.title)" +
            " LIKE LOWER(CONCAT('%', :title, '%')))) AND c.status = :status")
    Page<Case> searchByCaseNumberOrTitleAndStatus(
            @Param("caseNumber") String caseNumber,
            @Param("title") String title,
            @Param("status") CaseStatus status,
            Pageable pageable
    );

}
