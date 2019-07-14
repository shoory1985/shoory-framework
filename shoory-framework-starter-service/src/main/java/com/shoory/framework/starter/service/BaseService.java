package com.shoory.framework.starter.service;

import java.lang.reflect.ParameterizedType;

import com.shoory.framework.starter.api.request.BaseRequest;
import com.shoory.framework.starter.api.response.BaseResponse;

public abstract class BaseService<IN extends BaseRequest, OUT extends BaseResponse> {	
	private Class<IN> requestClass;
	private Class<OUT> responseClass;

	public Class<IN> requestClass() {
		return requestClass != null ? requestClass : (requestClass = (Class<IN>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	public Class<OUT> responseClass() {
		return responseClass != null ? responseClass : (responseClass = (Class<OUT>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
	}
	
	public abstract OUT invoke(IN request);
}
