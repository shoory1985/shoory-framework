package com.shoory.framework.starter.gateway.api.response;

import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.response.BaseResponse;
import com.shoory.framework.starter.gateway.api.request.TokenRefreshRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenIssueResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiName("验名密钥")
	@ApiRequired
	private String signKey;

	@ApiName("访问令牌")
	@ApiRequired
	private String accessToken;
	
	@ApiName("刷新令牌")
	@ApiRequired
	private String refreshToken;
}