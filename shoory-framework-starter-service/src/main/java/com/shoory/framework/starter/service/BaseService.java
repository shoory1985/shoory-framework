package com.shoory.framework.starter.service;

import com.shoory.framework.starter.api.request.BaseRequest;
import com.shoory.framework.starter.api.response.BaseResponse;

public abstract class BaseService<IN extends BaseRequest, OUT extends BaseResponse> {	

	public abstract Class<IN> requestClass();
	public abstract Class<OUT> responseClass();
	
	public abstract OUT invoke(IN request);
}
