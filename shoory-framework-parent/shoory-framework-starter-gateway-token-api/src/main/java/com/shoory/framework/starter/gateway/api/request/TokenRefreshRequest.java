package com.shoory.framework.starter.gateway.api.request;

import com.shoory.framework.starter.api.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenRefreshRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;

}
