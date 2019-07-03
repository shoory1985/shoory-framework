package com.shoory.framework.starter.api.constants;

public class BizException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Resultable result;

	public BizException(Resultable result) {
		super(result.getCode(), null, false, false);
		this.result = result;
	}

	public Resultable getResult() {
		return result;
	}

	public void setResult(Resultable result) {
		this.result = result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
