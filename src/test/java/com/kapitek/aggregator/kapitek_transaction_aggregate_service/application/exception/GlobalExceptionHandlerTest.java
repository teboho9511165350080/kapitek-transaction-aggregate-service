package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exception;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exeption.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;


    @Test
    void handleCustomerInfoFileException_shouldReturnProperResponse() {

        int statusCode = 404;
        String body = "Customer file not found";

        DownstreamServiceException exception = new DownstreamServiceException(statusCode, body);

        Instant beforeCall = Instant.now();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCustomerInfoFileException(exception);

        Instant afterCall = Instant.now();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();

        assertThat(errorResponse.getError()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());

        assertThat(errorResponse.getMessage()).isEqualTo(body);

        assertThat(errorResponse.getTimestamp()).isBetween(beforeCall, afterCall);
    }

    @Test
    void handleCustomerInfoFileException_shouldHandleDifferentStatusCodes() {

        int statusCode = 400;
        String body = "Bad request from downstream";

        DownstreamServiceException exception = new DownstreamServiceException(statusCode, body);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCustomerInfoFileException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody().getError()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());

        assertThat(response.getBody().getMessage()).isEqualTo(body);
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {

        Exception exception = new RuntimeException("Unexpected");

        Instant beforeCall = Instant.now();

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        Instant afterCall = Instant.now();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        ErrorResponse errorResponse = response.getBody();

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getError()).isEqualTo("INTERNAL_ERROR");

        assertThat(errorResponse.getMessage()).isEqualTo("An unexpected error occurred");

        assertThat(errorResponse.getTimestamp()).isBetween(beforeCall, afterCall);
    }
}