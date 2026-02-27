package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.config;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exeption.DownstreamServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.Util;
import java.io.IOException;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = null;

        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader());
            }
        } catch (IOException ignored) {}

        return new DownstreamServiceException(
                response.status(),
                body
        );
    }
}