package com.shoory.framework.starter.gateway.api.request;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenIssueRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;

	@ApiName("凭据")
	@ApiRequired
	@NotNull(message = "凭据不能为空")
	@NotBlank(message = "凭据不能为空串")
	private String credential;
	
	@ApiName("超时毫秒数")
	@ApiRequired
	@Min(value = 1, message = "错误的超时毫秒数")
	private long timeoutInMilliseconds;

	@ApiName("URIs")
	@ApiRequired
	@NotNull(message = "URIs不能为空")
	@NotEmpty(message = "URIs不能为空集")
	private String[] uris;
}
