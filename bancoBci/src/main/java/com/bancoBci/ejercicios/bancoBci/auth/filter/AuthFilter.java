package com.bancoBci.ejercicios.bancoBci.auth.filter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements WebFilter {
	
  private static final List<String> authenticationFlowList = List.of("44623104", constante);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    var bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    AuthUtil.validatePrefixToken(bearerToken);

    var jwtString = com.tdp.ms.planconvergent.auth.filter.AuthUtil.getJwtTokenString(bearerToken);

    try {
      final var decodedJWT = AuthUtil.decodeJWT(jwtString);
      AuthUtil.validateJwtExpiration(decodedJWT);

      final var authenticationFlow = decodedJWT.getClaim(AUTHENTICATION_FLOW).asString();

      if (!authenticationFlowList.contains(authenticationFlow)) {
        return AuthUtil.buildAsMonoUnauthorizedException();
      }

      return authValidator.validate(exchange, chain, decodedJWT);

    } catch (Exception e) {
      log.error("Exception: {}", e.toString());
      return AuthUtil.buildAsMonoUnauthorizedException();
    }
  }

}
