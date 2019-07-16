package com.shoory.framework.starter.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.gateway.api.request.TokenDevokeRequest;
import com.shoory.framework.starter.gateway.api.request.TokenIssueRequest;
import com.shoory.framework.starter.gateway.api.request.TokenRefreshRequest;
import com.shoory.framework.starter.gateway.api.response.TokenDevokeResponse;
import com.shoory.framework.starter.gateway.api.response.TokenIssueResponse;
import com.shoory.framework.starter.gateway.api.response.TokenRefreshResponse;

@ApiName("网关令牌服务")
@FeignClient("gateway-token")
public interface GatewayTokenApi {

	@ApiName("令牌吊销")
	@PostMapping(value = "/tokenDevoke", produces = "application/json")
	public TokenDevokeResponse tokenDevoke(TokenDevokeRequest request);

	@ApiName("令牌颁发")
	@PostMapping(value = "/tokenRefresh", produces = "application/json")
	public TokenRefreshResponse tokenRefresh(TokenRefreshRequest request);

	@ApiName("令牌刷新")
	@PostMapping(value = "/tokenIssue", produces = "application/json")
	public TokenIssueResponse tokenIssue(TokenIssueRequest request);

}
