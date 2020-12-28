package com.shoory.framework.starter.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.api.constants.SysException;
import com.shoory.framework.starter.api.request.UserBaseRequest;
import com.shoory.framework.starter.api.response.BaseResponse;

@Component
public class JwtUtils {
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.accessTokenSeconds:7200}")
	private long jwtAccessTokenSeconds;
	@Value("${jwt.refreshTokenSeconds:864000}")
	private long jwtRefreshTokenSeconds;
	
	private JWTVerifier jwtVerifer = null;
	
	@Bean
	public JwtUtils getJwtUtils() {
		return new JwtUtils();
	}
	
	public boolean checkAccessTokenIgnoreExpiration(String accessToken) {
		try {
			if (jwtVerifer == null) {
				jwtVerifer = JWT.require(Algorithm.HMAC256(this.jwtSecret)).build();
			}
			jwtVerifer.verify(accessToken);
			return true;
		} catch (TokenExpiredException e) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public void checkAccessToken(String accessToken) {
		try {
			if (jwtVerifer == null) {
				jwtVerifer = JWT.require(Algorithm.HMAC256(this.jwtSecret)).build();
			}
			jwtVerifer.verify(accessToken);
		} catch (TokenExpiredException e) {
			throw new BizException(UserBaseRequest.ERROR_ACCESS_TOKEN_EXPIRED);
		} catch (Exception e) {
			throw new BizException(UserBaseRequest.ERROR_INVALID_ACCESS_TOKEN);
		}
	}
	public boolean checkRefreshToken(String accessToken, String key, String refreshToken) {
		return refreshToken != null ? refreshToken.equals(this.refreshToken(accessToken, key)) : false;
	}

	public String accessToken(String issuer, String audience, long userId) {
		// 通信令牌
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		String ret = JWT.create().withHeader(map) // header
				.withIssuer(issuer) // payload
				.withAudience(audience)
				.withSubject(String.valueOf(userId))
				.withIssuedAt(new Date()) // sign time
				.withExpiresAt(new Date(System.currentTimeMillis() + 1000L * jwtAccessTokenSeconds)) // expire time
				.sign(Algorithm.HMAC256(this.jwtSecret));

		return ret;
	}

	public String refreshToken(String accessToken, String key) {
		return DigestUtils.md5Hex(accessToken + key);
	}
}
