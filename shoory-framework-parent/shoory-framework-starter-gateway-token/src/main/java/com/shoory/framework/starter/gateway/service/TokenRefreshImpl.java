package com.shoory.framework.starter.gateway.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.shoory.framework.starter.gateway.api.request.TokenRefreshRequest;
import com.shoory.framework.starter.gateway.api.response.TokenRefreshResponse;
import com.shoory.framework.starter.service.BaseService;

@Service("token/refresh")
public class TokenRefreshImpl extends BaseService<TokenRefreshRequest, TokenRefreshResponse> {
	
	@Override
	public TokenRefreshResponse invoke(@Valid TokenRefreshRequest request) {
		TokenRefreshResponse response = new TokenRefreshResponse();
		
		return response;
	}

	@Override
	public Class<TokenRefreshRequest> requestClass() {
		return TokenRefreshRequest.class;
	}

	@Override
	public Class<TokenRefreshResponse> responseClass() {
		return TokenRefreshResponse.class;
	}


}
