package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exeption;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exeption.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleCustomerInfoFileException(DownstreamServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.valueOf(ex.getStatus()).getReasonPhrase(),
                ex.getBody(),
                Instant.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(ex.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                Instant.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}