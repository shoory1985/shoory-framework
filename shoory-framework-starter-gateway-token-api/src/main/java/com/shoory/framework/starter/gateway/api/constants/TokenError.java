package com.shoory.framework.starter.gateway.api.constants;

import com.shoory.framework.starter.api.constants.Resultable;

public enum TokenError implements Resultable {
	ERROR_TOKEN_INVALID("非法令牌", ""),
	ERROR_TOKEN_MISSING("令牌未找到", ""),
	;
	private String message;
	private String description;
	
	private TokenError(String message, String description) {
		this.message = message;
		this.description = description;
	}


	@Override
	public String getCode() {
		return null;
	}

	@Override
	public String getMessage(String lang) {
		switch (lang) {
		default:
			return this.message;
		}
	}
	@Override
	public String getDescription(String lang) {
		switch (lang) {
		default:
			return this.description;
		}
	}
}