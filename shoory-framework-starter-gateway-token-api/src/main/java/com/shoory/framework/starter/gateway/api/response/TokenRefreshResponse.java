package com.shoory.framework.starter.gateway.api.response;

import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiExample1;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.response.BaseResponse;
import com.shoory.framework.starter.gateway.api.request.TokenRefreshRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenRefreshResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiName("新的的访问令牌")
	@ApiRequired
	private String accessToken;

	@ApiName("新的的刷新令牌")
	@ApiRequired
	private String refreshToken;
}