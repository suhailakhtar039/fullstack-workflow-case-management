package com.caseflow.exception;

import com.caseflow.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpServletRequest request){

        String message = methodArgumentNotValidException
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(
                        400,
                        "Validation Error",
                        message,
                        request.getRequestURI(),
                        System.currentTimeMillis()
                ));
    }
//    Put all exception above this parent exception
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
