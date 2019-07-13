package com.shoory.framework.starter.service;

import java.lang.reflect.ParameterizedType;

import com.shoory.framework.starter.api.request.BaseRequest;
import com.shoory.framework.starter.api.response.BaseResponse;

public abstract class BaseService<IN extends BaseRequest, OUT extends BaseResponse> {	
	private Class<IN> requestClass;
	private Class<OUT> responseClass;

	public Class<IN> requestClass() {
		return requestClass;
	}
	public Class<OUT> responseClass() {
		return responseClass;
	}

	BaseService() {
		ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
		requestClass = (Class<IN>)ptype.getActualTypeArguments()[0];
		responseClass = (Class<OUT>)ptype.getActualTypeArguments()[1];
	}
	
	public abstract OUT invoke(IN request);
}
