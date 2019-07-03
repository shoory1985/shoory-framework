package com.shoory.framework.starter.gateway.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.gateway.api.constants.TokenError;
import com.shoory.framework.starter.gateway.api.request.TokenDevokeRequest;
import com.shoory.framework.starter.gateway.api.response.TokenDevokeResponse;
import com.shoory.framework.starter.gateway.document.GatewaySessions;
import com.shoory.framework.starter.gateway.repository.GatewaySessionRepository;
import com.shoory.framework.starter.service.BaseService;

@Service("token/devoke")
public class TokenDevokeImpl extends BaseService<TokenDevokeRequest, TokenDevokeResponse> {
	@Autowired
	private GatewaySessionRepository gatewaySessionRepository;
	
	@Override
	public TokenDevokeResponse invoke(@Valid TokenDevokeRequest request) {
		//吊销
		List<GatewaySessions> list = gatewaySessionRepository.findByCredential(request.getCredential());
		for (GatewaySessions item : list) {
			gatewaySessionRepository.delete(item);
		}
		
		//输出
		TokenDevokeResponse response = new TokenDevokeResponse();
		
		return response;
	}

	@Override
	public Class<TokenDevokeRequest> requestClass() {
		return TokenDevokeRequest.class;
	}

	@Override
	public Class<TokenDevokeResponse> responseClass() {
		return TokenDevokeResponse.class;
	}

}
