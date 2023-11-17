package com.bancoBci.ejercicios.bancoBci.auth.filter;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tdp.genesis.core.exception.GenesisException;
import com.tdp.ms.planconvergent.business.redis.ProductRedisService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class AuthUtil {

	private static final List<String> CEX_DOCUMENT = List.of("CEX", "C");
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String UNAUTHORIZED_EXCEPTION_ID = "SEC1001";
	private static final String FORBIDDEN_EXCEPTION_ID = "SVC1013";
	public static final AntPathMatcher antPathMatcher = new AntPathMatcher();

	private AuthUtil() {
	}

	public static void validatePrefixToken(String bearerToken) {
		if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
			throw buildUnauthorizedException();
		}
	}

	public static DecodedJWT decodeJWT(String token) {
		return JWT.decode(token);
	}

	public static void validateJwtExpiration(DecodedJWT decodedJWT) {
		if (isJwtExpired(decodedJWT)) {
			throw buildUnauthorizedException();
		}
	}

	public static boolean isJwtExpired(DecodedJWT decodedJWT) {
		Date expiresAt = decodedJWT.getExpiresAt();
		return expiresAt.before(new Date());
	}

	public static GenesisException buildUnauthorizedException() {
		return GenesisException.builder().exceptionId(UNAUTHORIZED_EXCEPTION_ID)
				.build();
	}

	public static <T> Mono<T> buildAsMonoUnauthorizedException() {
		return GenesisException.builder().exceptionId(UNAUTHORIZED_EXCEPTION_ID)
				.buildAsMono();
	}

	public static <T> Mono<T> buildAsMonoForbiddenException() {
		return GenesisException.builder().exceptionId(FORBIDDEN_EXCEPTION_ID)
				.buildAsMono();
	}

	public static String getJwtTokenString(String bearerToken) {
		return bearerToken.substring(7).trim();
	}

	public static boolean methodAndPathMatches(ServerWebExchange exchange,
			HttpMethod method, String pathPattern) {
		return Optional.ofNullable(exchange.getRequest().getMethod())
				.map(httpMethod -> httpMethod.equals(method)).orElse(false)
				&& antPathMatcher.match(pathPattern,
						exchange.getRequest().getURI().getPath());
	}

	public static MultiValueMap<String, String> getQueryParams(
			ServerWebExchange exchange) {
		return exchange.getRequest().getQueryParams();
	}

	public static Function<Boolean, Mono<Void>> delegate(
			ServerWebExchange exchange, WebFilterChain chain) {
		return isValid -> {
			if (isValid) {
				return chain.filter(exchange);
			} else {
				return buildAsMonoForbiddenException();
			}
		};
	}

	public static Mono<Boolean> validateServiceIdInRedisByDocument(
			String serviceId, String documentType, String documentNumber,
			ProductRedisService productRedisService) {
		if (StringUtils.isBlank(serviceId)) {
			return Mono.just(Boolean.FALSE);
		}
		return productRedisService
				.findByDocument(
						convertDocumentType(documentType) + HYPHEN + documentNumber)
				.map(productRedis -> productRedis.getItems().stream()
						.anyMatch(productRedisItem -> serviceId
								.equals(productRedisItem.getPublicId())))
				.defaultIfEmpty(Boolean.FALSE);
	}

	private static String convertDocumentType(String documentType) {
		if (StringUtils.equalsAny(documentType, "C", "CEX")) {
			documentType = "CEX";
		}
		return documentType;
	}
	public static Mono<Boolean> validateDocument(String tokenDocumentType,
			String documentType, String tokenDocumentNumber, String documentNumber) {
		return Mono.just(validateDocumentType(tokenDocumentType, documentType)
				&& StringUtils.equals(tokenDocumentNumber, documentNumber));
	}

	public static Boolean validateDocumentType(String tokenDocumentType,
			String documentType) {
		if (CEX_DOCUMENT.contains(tokenDocumentType)) {
			return CEX_DOCUMENT.contains(documentType);
		}
		return StringUtils.equals(tokenDocumentType, documentType);
	}

	public static Mono<Boolean> validatePhoneNumber(String phoneNumber,
			String tokenPhoneNumber) {
		if (StringUtils.isAnyBlank(phoneNumber, tokenPhoneNumber)) {
			return Mono.just(Boolean.FALSE);
		}
		return Mono.just(StringUtils.equals(phoneNumber, tokenPhoneNumber));
	}
}
