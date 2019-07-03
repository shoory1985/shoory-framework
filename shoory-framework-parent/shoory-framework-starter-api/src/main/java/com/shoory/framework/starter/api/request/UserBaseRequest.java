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

	@ApiHidden
	@NotNull(message = "无授权")
	@NotBlank(message = "无授权")
	private String _credential;
}
