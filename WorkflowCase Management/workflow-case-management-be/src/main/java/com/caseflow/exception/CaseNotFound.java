package com.caseflow.exception;

public class CaseNotFound extends RuntimeException {
    public CaseNotFound(String message) {
        super(message);
    }
}
