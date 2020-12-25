package com.shoory.framework.starter.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.annotation.ApiHidden;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBaseRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ERROR_ACCESS_TOKEN_EXPIRED = "ERROR_ACCESS_TOKEN_EXPIRED";
	public static final String ERROR_INVALID_ACCESS_TOKEN = "ERROR_INVALID_ACCESS_TOKEN";
	public static final String ERROR_ACCESS_TOKEN_MISSED = "ERROR_ACCESS_TOKEN_MISSED";
	public static final String ERROR_INVALID_CREDENTIAL = "ERROR_INVALID_CREDENTIAL";
	
	@ApiHidden
	@NotNull(message = "无授权")
	@NotBlank(message = "无授权")
	private String _credential;
}
