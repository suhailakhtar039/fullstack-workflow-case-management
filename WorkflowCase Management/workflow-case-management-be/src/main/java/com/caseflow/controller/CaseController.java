package com.caseflow.controller;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.dto.CaseStatusHistoryResponse;
import com.caseflow.dto.DashboardResponse;
import com.caseflow.service.CaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    CaseService caseService;

    // create case
    @PostMapping
    public CaseResponse createCase(@Valid @RequestBody CaseCreateRequest caseCreateRequest) {
        return caseService.createCase(caseCreateRequest);
    }

    // get all cases
    @GetMapping
    public List<CaseResponse> getAllCase() {
        return caseService.getAllCases();
    }

    // change case status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam CaseStatus status) {

        caseService.transitionStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    // get case status history
    @GetMapping("/{id}/history")
    public List<CaseStatusHistoryResponse> getHistory(@PathVariable Long id) {
        return caseService.getStatusHistory(id);
    }

    @GetMapping("/search")
    public Page<CaseResponse> searchCases(
            @RequestParam(required = false) String caseNumber,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) CaseStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return caseService.searchCases(caseNumber, title, status, pageable);
    }

    @GetMapping("/my")
    public Page<CaseResponse> myCases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        return caseService.getMyCases(pageable);
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(){
        return caseService.getDashboard();
    }

}
