package com.shoory.framework.starter.gateway.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.nacos.client.utils.StringUtils;
import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.gateway.api.request.TokenRefreshRequest;
import com.shoory.framework.starter.gateway.api.response.TokenIssueResponse;
import com.shoory.framework.starter.gateway.api.response.TokenRefreshResponse;
import com.shoory.framework.starter.gateway.document.GatewaySessions;
import com.shoory.framework.starter.gateway.repository.GatewaySessionRepository;
import com.shoory.framework.starter.service.BaseService;

@Service
public class TokenRefresh extends BaseService<TokenRefreshRequest, TokenRefreshResponse> {

	@Autowired
	private GatewaySessionRepository gatewaySessionRepository;
	
	@Override
	public TokenRefreshResponse invoke(@Valid TokenRefreshRequest request) {
		//取得授权Token
		GatewaySessions oldGatewaySession = gatewaySessionRepository.findById(request.getAccessToken())
				.orElseThrow(() -> new BizException(TokenRefreshRequest.ERROR_INVALID_ACCESS_TOKEN));
		//检查刷新Token
		if (!oldGatewaySession.getRefreshToken().equals(request.getRefreshToken())) {
			throw new BizException(TokenRefreshRequest.ERROR_INVALID_REFRESH_TOKEN);
		}
		
		// 签发新的
		GatewaySessions gatewaySession = new GatewaySessions();
		gatewaySession.setAccessToken(UUID.randomUUID().toString());
		gatewaySession.setCredential(oldGatewaySession.getCredential());
		gatewaySession.setUris(oldGatewaySession.getUris());
		gatewaySession.setRefreshToken(UUID.randomUUID().toString());
		gatewaySession.setSignKey(UUID.randomUUID().toString());
		gatewaySessionRepository.save(gatewaySession);

		//吊销原AccessToken
		new Thread(() -> {
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	//延迟10秒吊销原令牌
			gatewaySessionRepository.delete(oldGatewaySession);
		}).start();
		
		//输出到前端
		TokenRefreshResponse response = new TokenRefreshResponse();
		
		response.setSignKey(gatewaySession.getSignKey());
		response.setAccessToken(gatewaySession.getAccessToken());
		response.setRefreshToken(gatewaySession.getRefreshToken());
		
		return response;
	}


}
