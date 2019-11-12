package com.shoory.framework.starter.gateway.api.request;

import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenRefreshRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;

	public static final String ERROR_INVALID_ACCESS_TOKEN = "ERROR_INVALID_ACCESS_TOKEN";
	public static final String ERROR_INVALID_REFRESH_TOKEN = "ERROR_INVALID_REFRESH_TOKEN";
	
	@ApiName("要刷新的访问令牌")
	@ApiRequired
	private String accessToken;

	@ApiName("刷新令牌")
	@ApiRequired
	private String refreshToken;
}
