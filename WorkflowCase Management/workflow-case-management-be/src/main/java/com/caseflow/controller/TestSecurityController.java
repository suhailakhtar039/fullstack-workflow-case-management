package com.caseflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestSecurityController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint(){
        return new ResponseEntity<>("public access", HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.caseflow.util.SecurityRoles).ADMIN)")
    @GetMapping("/admin")
    public ResponseEntity<String> adminOnly(){
        return new ResponseEntity<>("Admin only", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.caseflow.util.SecurityRoles).ADMIN,T(com.caseflow.util.SecurityRoles).CASE_MANAGER)")
    @GetMapping("/manager")
    public ResponseEntity<String> managerAccess(){
        return new ResponseEntity<>("Admin or Manager access", HttpStatus.OK);
    }
}
