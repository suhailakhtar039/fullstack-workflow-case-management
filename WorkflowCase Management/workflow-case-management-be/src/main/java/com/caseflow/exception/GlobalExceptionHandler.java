package com.caseflow.exception;

import com.caseflow.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(CaseNotFound.class)
    public ResponseEntity<ErrorResponse> handleCaseNotFound(CaseNotFound caseNotFound,HttpServletRequest request){
        String uri = request.getRequestURI();
        ErrorResponse e = new ErrorResponse(uri, caseNotFound.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusDidNotMatchException.class)
    public ResponseEntity<ErrorResponse> handleWrongValidator(StatusDidNotMatchException statusDidNotMatchException,
                                                              HttpServletRequest request){
        String uri = request.getRequestURI();
        ErrorResponse e = new ErrorResponse(uri, statusDidNotMatchException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
