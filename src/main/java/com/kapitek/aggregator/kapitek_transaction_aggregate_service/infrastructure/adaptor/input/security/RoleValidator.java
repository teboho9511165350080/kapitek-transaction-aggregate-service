package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.security;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.security.dto.JwtClaims;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.security.dto.JwtMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

@RequiredArgsConstructor
public final class RoleValidator implements OAuth2TokenValidator<Jwt> {

    private final String role;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {

        Map<String, Object> claims = jwt.getClaims();

        JwtClaims jwtClaims = JwtMapperUtil.fromClaimsMap(claims);

        boolean hasAccess = jwtClaims.getRealmAccess() != null &&
                jwtClaims.getRealmAccess().hasRole(role);

        if (hasAccess) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error auth2Error = new OAuth2Error(
                String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                "Token does not contain the required role",
                null);

        throw new OAuth2AuthorizationException(auth2Error);
    }
}
