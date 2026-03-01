package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request.AggregateRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.input.TransactionAggregationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AggregateCategorizedTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class AggregateCategorizedTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionAggregationUseCase transactionAggregationUseCase;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldReturnRecentAggregatedTransactions() throws Exception {

        String key = "TEB 001";
        int months = 3;

        AggregateResponse response = AggregateResponse.builder().build();

        when(transactionAggregationUseCase.getRecentCategorizeAggregatedTransactions(key, months))
                .thenReturn(response);

        mockMvc.perform(get("/transactions/account/{key}/aggregated/months/{months}", key, months))
                .andExpect(status().isOk());

        verify(transactionAggregationUseCase, times(1))
                .getRecentCategorizeAggregatedTransactions(key, months);
    }


    @Test
    void shouldReturnRecentAggregatedSummaryTransactions() throws Exception {

        String key = "TEB 001";
        int months = 6;

        AggregateResponse response = AggregateResponse.builder().build();
        when(transactionAggregationUseCase.getRecentTransactionsSummary(key, months))
                .thenReturn(response);

        mockMvc.perform(get("/transactions/account/{key}/aggregated-summary/months/{months}", key, months))
                .andExpect(status().isOk());

        verify(transactionAggregationUseCase, times(1))
                .getRecentTransactionsSummary(eq(key), eq(months));
    }


    @Test
    void shouldReturnAggregatedSummaryBetweenDates() throws Exception {

        AggregateRequest request = AggregateRequest.builder().build();
        AggregateResponse response = AggregateResponse.builder().build();

        when(transactionAggregationUseCase.geTransactionsSummaryBetweenDates(any()))
                .thenReturn(response);

        mockMvc.perform(post("/transactions/aggregated-summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(transactionAggregationUseCase, times(1))
                .geTransactionsSummaryBetweenDates(any());
    }

    @Test
    void shouldReturnAggregatedTransactionsBetweenDates() throws Exception {

        AggregateRequest request = AggregateRequest.builder().build();
        AggregateResponse response = AggregateResponse.builder().build();

        when(transactionAggregationUseCase.geAggregatedCategorizedTransactionsBetweenDates(any()))
                .thenReturn(response);

        mockMvc.perform(post("/transactions/aggregated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(transactionAggregationUseCase).geAggregatedCategorizedTransactionsBetweenDates(any());
    }
}