package com.shoory.framework.starter.api.constants;

public class BizException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BizException(String message) {
		super(message, null, false, false);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
