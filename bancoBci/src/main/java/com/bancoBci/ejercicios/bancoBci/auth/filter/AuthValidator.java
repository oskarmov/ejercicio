package com.bancoBci.ejercicios.bancoBci.auth.filter;


import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthValidator {

	private final ProductRedisService productRedisService;
	private static final String PATH_PLAN_DETAIL = "/planconvergent/v1/plandetail";;

	public Mono<Void> validate(ServerWebExchange exchange, WebFilterChain chain, DecodedJWT decodedJWT) {
		if (AuthUtil.methodAndPathMatches(exchange, HttpMethod.GET, PATH_PLAN_DETAIL)) {

			MultiValueMap<String, String> queryParams = AuthUtil.getQueryParams(exchange);
			return validateGet(decodedJWT, queryParams)
					.flatMap(AuthUtil.delegate(exchange, chain));

		} else {
			return chain.filter(exchange);
		}
	}

	private Mono<Boolean> validateGet(DecodedJWT decodedJWT, MultiValueMap<String, String> queryParams) {

		final var originProductType = queryParams.getFirst("originProductType");
		if (!"MOVIL".equals(originProductType)) {
			return Mono.just(Boolean.TRUE);
		}
		return validateGetByMovil(decodedJWT, queryParams);
	}

	private Mono<Boolean> validateGetByMovil(DecodedJWT decodedJWT, MultiValueMap<String, String> queryParams) {
		final var authenticationFlow = decodedJWT.getClaim(AUTHENTICATION_FLOW).asString();
		if(DOCUMENT.equalsIgnoreCase(authenticationFlow)) {
			return validateGetDocument(decodedJWT, queryParams);
		}else{
			return validateGetOtp(decodedJWT, queryParams);
		}
	}

	private Mono<Boolean> validateGetDocument(DecodedJWT decodedJWT, MultiValueMap<String, String> queryParams) {
		final var tokenDocumentType = decodedJWT.getClaim(DOCUMENT_TYPE).asString();
		final var tokenDocumentNumber = decodedJWT.getClaim(DOCUMENT_NUMBER).asString();

		final var mobile = queryParams.getFirst(PHONE_NUMBER);
		final var documentType = queryParams.getFirst(DOCUMENT_TYPE);
		final var documentNumber = queryParams.getFirst(DOCUMENT_NUMBER);
		
		if(!StringUtils.isAllBlank(documentType, documentNumber)) {
			return AuthUtil.validateDocument(tokenDocumentType, documentType,
					tokenDocumentNumber, documentNumber)
					.flatMap(isValid -> Boolean.FALSE.equals(isValid) ? Mono.just(Boolean.FALSE) :
						AuthUtil.validateServiceIdInRedisByDocument(mobile, documentType, 
								documentNumber, productRedisService)
							);
		}else {
		return AuthUtil
				.validateServiceIdInRedisByDocument(mobile, tokenDocumentType, tokenDocumentNumber, productRedisService);
		}
	}

	private static Mono<Boolean> validateGetOtp(DecodedJWT decodedJWT, MultiValueMap<String, String> queryParams) {
		final var tokenPhoneNumber = decodedJWT.getClaim(PHONE_NUMBER).asString();

		final var mobile = queryParams.getFirst(PHONE_NUMBER);

		return AuthUtil.validatePhoneNumber(mobile, tokenPhoneNumber);
	}

}
