package com.shoory.framework.starter.gateway.api.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenDevokeRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;

	@ApiName("凭据")
	@ApiRequired
	@NotNull(message = "凭据不能为空")
	@NotBlank(message = "凭据不能为空串")
	private String credential;

}
