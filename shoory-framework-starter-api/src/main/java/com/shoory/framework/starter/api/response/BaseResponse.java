package com.shoory.framework.starter.api.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiHidden;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.api.constants.SysException;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiName("响应代码")
	@ApiRequired
	@ApiDescription("成功为SUCCESS，否则为其他值")
	private String code = "ERROR_UNKNOWN";

	@ApiName("响应消息")
	private String message;

	public boolean isSuccess() {
		return code != null && code.trim().equals("SUCCESS");
	}

	public void checkSuccess() {
		if (!isSuccess()) {
			throw new SysException(this.code, this.message);
		}
	}
}
