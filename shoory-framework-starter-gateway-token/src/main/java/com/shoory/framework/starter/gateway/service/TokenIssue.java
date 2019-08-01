package com.shoory.framework.starter.gateway.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.nacos.client.utils.StringUtils;
import com.shoory.framework.starter.gateway.api.request.TokenIssueRequest;
import com.shoory.framework.starter.gateway.api.response.TokenIssueResponse;
import com.shoory.framework.starter.gateway.document.GatewaySessions;
import com.shoory.framework.starter.gateway.repository.GatewaySessionRepository;
import com.shoory.framework.starter.service.BaseService;

@Service
public class TokenIssue extends BaseService<TokenIssueRequest, TokenIssueResponse> {

	@Autowired
	private GatewaySessionRepository gatewaySessionRepository;
	
	@Override
	public TokenIssueResponse invoke(TokenIssueRequest request) {
		// 作废旧的
		{
			List<GatewaySessions> list = gatewaySessionRepository.findByCredential(request.getCredential());
			for (GatewaySessions item : list) {
				gatewaySessionRepository.delete(item);
			}
		}

		// 签发新的
		GatewaySessions gatewaySession = new GatewaySessions();
		gatewaySession.setAccessToken(UUID.randomUUID().toString());
		gatewaySession.setCredential(request.getCredential());
		gatewaySession.setUris(new HashSet<String>(
				Arrays.stream(request.getUris())
				.filter(StringUtils::isNotBlank)
				.map(String::toLowerCase)
				.distinct()
				.collect(Collectors.toList())));
		gatewaySession.setRefreshToken(UUID.randomUUID().toString());
		gatewaySession.setSignKey(UUID.randomUUID().toString());
		gatewaySessionRepository.save(gatewaySession);

		// 输出到前端
		TokenIssueResponse response = new TokenIssueResponse();
		
		response.setSignKey(gatewaySession.getSignKey());
		response.setAccessToken(gatewaySession.getAccessToken());
		response.setRefreshToken(gatewaySession.getRefreshToken());
		
		return response;
	}



}
