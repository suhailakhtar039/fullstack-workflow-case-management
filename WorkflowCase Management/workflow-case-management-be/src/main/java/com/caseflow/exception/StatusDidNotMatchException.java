package com.caseflow.exception;

public class StatusDidNotMatchException extends RuntimeException {
    public StatusDidNotMatchException(String message) {
        super(message);
    }
}
