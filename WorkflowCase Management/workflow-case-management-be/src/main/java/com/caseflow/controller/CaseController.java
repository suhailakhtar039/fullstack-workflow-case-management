package com.caseflow.controller;

import com.caseflow.domain.enums.CaseStatus;
import com.caseflow.dto.CaseCreateRequest;
import com.caseflow.dto.CaseResponse;
import com.caseflow.dto.CaseStatusHistoryResponse;
import com.caseflow.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CaseResponse createCase(@RequestBody CaseCreateRequest caseCreateRequest){
        return caseService.createCase(caseCreateRequest);
    }

    // get all cases
    @GetMapping
    public List<CaseResponse> getAllCase(){
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
    public List<CaseStatusHistoryResponse> getHistory(@PathVariable Long id){
        return caseService.getStatusHistory(id);
    }

}
