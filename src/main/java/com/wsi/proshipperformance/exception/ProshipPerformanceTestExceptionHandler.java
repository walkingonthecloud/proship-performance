package com.wsi.proshipperformance.exception;

import com.wsi.proshipperformance.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ProshipPerformanceTestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<ApiError> handleConflict(RuntimeException ex, WebRequest request) {
        ApiError response = new ApiError();
        response.setErrorCode("0001");
        response.setErrorMessage("Unhandled exception happened in Proship Performance Test");
        return new ResponseEntity<ApiError>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
