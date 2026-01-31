package com.caseflow.exception;

import com.caseflow.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(CaseNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleCaseNotFound(CaseNotFound caseNotFound, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(404,
                        "Case not found",
                        caseNotFound.getMessage(),
                        request.getRequestURI(),
                        System.currentTimeMillis()));
    }

    @ExceptionHandler(StatusDidNotMatchException.class)
    public ResponseEntity<ApiErrorResponse> handleWrongValidator(StatusDidNotMatchException statusDidNotMatchException,
                                                                 HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(
                        409,
                        "Status didn't match",
                        statusDidNotMatchException.getMessage(),
                        request.getRequestURI(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleItemNotFoundException(ItemNotFoundException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        404,
                        "Item not found",
                        e.getMessage(),
                        request.getRequestURI(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ItemNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        404,
                        "NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse(
                        403,
                        "FORBIDDEN",
                        ex.getMessage(),
                        request.getRequestURI(),
                        System.currentTimeMillis()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        500,
                        "INTERNAL_ERROR",
                        "Something went wrong",
                        request.getRequestURI(),
                        System.currentTimeMillis()
                ));
    }

}
