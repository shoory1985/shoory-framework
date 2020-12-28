package com.shoory.framework.starter.service;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shoory.framework.starter.api.request.UserBaseRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.api.constants.SysException;
import com.shoory.framework.starter.api.request.BaseRequest;
import com.shoory.framework.starter.api.request.DataFrameBaseRequest;
import com.shoory.framework.starter.api.response.BaseResponse;
import com.shoory.framework.starter.api.response.DataFrameBaseResponse;
import com.shoory.framework.starter.utils.PojoUtils;

@Component
public class MethodRouter {
	private static final Logger logger = LoggerFactory.getLogger(MethodRouter.class);
	@Autowired
	private PojoUtils pojoUtils;
	@Autowired
	private I18nComponent i18nComponent;
	@Autowired
	private JwtUtils jwtUtils;

	public String jsonInvoke(String methodName, String json) {
		try {
			BaseService method = this.getMethod(methodName);
			// 入参
			BaseRequest request = (BaseRequest) pojoUtils.fromJson(json, method.requestClass());
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			request.set_clientAddress(attributes.getRequest().getHeader("ClientAddress"));

			// 
			if (request instanceof UserBaseRequest) {
				//拣出JWT
				String token = attributes.getRequest().getHeader("Authorization");
				if (StringUtils.isBlank(token)) {
					throw new SysException(UserBaseRequest.ERROR_ACCESS_TOKEN_MISSED, "");
				}
				//检查令牌有效性（合法性和是否过期）
				jwtUtils.checkAccessToken(token);

				DecodedJWT jwt = JWT.decode(token);
				String credential = jwt.getSubject();
				if (StringUtils.isBlank(credential)) {
					throw new SysException(UserBaseRequest.ERROR_INVALID_CREDENTIAL, "");
				} else {
					//注入入参
					UserBaseRequest userBaseRequest = (UserBaseRequest) request;
					userBaseRequest.set_credential(credential);
				}
				
			}

			//入参打印
			logger.info(methodName + ">>>>" + json);
			String responseString = pojoUtils.toJson(this.baseInvoke(method, request));
			//出参打印
			logger.info(methodName + "<<<<" + responseString);
			
			//入参打印
			return responseString;
		} catch (SysException se) {
			BaseResponse response = new BaseResponse();
			response.setCode(BaseRequest.ERROR_INTERNAL);
			response.setMessage(se.getMessage());
			return pojoUtils.toJson(response);
		} catch (Exception e) {
			logger.info(e.getMessage());
			BaseResponse response = new BaseResponse();
			e.printStackTrace();
			response.setCode(BaseRequest.ERROR_INTERNAL);
			response.setMessage(e.getMessage());
			return pojoUtils.toJson(response);
		}
	}

	public  byte[] dataFrameInvoke(String methodName, byte[] payload, String clientId) {
		return this.dataFrameInvoke(getMethod(methodName), payload, clientId);
	}
	
	
	
	
	
	
	
	private BaseService getMethod(String methodName) {
		Object method = SpringUtil.getBean(methodName);
		// 检查类型
		if (method == null || !(method instanceof BaseService)) {
			// 没找到对应的接口
			throw new BizException(BaseRequest.ERROR_METHOD_NOT_FOUND);
		} else {
			return (BaseService)method;
		}
	}
	
	private BaseRequest getRequest(BaseService method) {
		try {
			return (BaseRequest)method.requestClass().newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private byte[] dataFrameInvoke(BaseService method, byte[] dataFrame, String clientId) {
		if (!DataFrameBaseRequest.class.isAssignableFrom(method.requestClass())) {
			//入参不支持数据帧转换而来
			return null;
		}
		
		//入参转化
		BaseRequest request = this.getRequest(method);
		((DataFrameBaseRequest)request).fromDataFrame(dataFrame);
		//会话信息相关
		if (StringUtils.isNotBlank(clientId) && request instanceof UserBaseRequest) {
			((UserBaseRequest)request).set_credential(clientId);
		}
		//调用
		BaseResponse response = this.baseInvoke(method, request);
		
		//出参检查
		if (response == null || !(response instanceof DataFrameBaseResponse)) {
			//出参为空或出参不支持转换为数据帧
			return null;
		}
		
		return ((DataFrameBaseResponse)response).toDataFrame();
	}
	
	private BaseResponse baseInvoke(BaseService method, BaseRequest request) {
		try {
			BaseResponse response = null;
			
			try {
				// 验证入参（验证失败则抛出异常）
				pojoUtils.validate(request);
				
				// 乐观锁重试
				int numAttempts = 0;
				int maxRetries = 2;
				for (numAttempts = 0; numAttempts <= maxRetries; numAttempts++) {
					try {
						response = method.invoke(request);
						response.setCode("SUCCESS");;
						break;
					} catch (Throwable ex) { // 乐观锁
						if (ex.getClass().getSimpleName().indexOf("PessimisticLockingFailureExceptionException") >= 0) {
							continue;
						} else {
							throw ex;
						}
					}
				}
			} catch (BizException be) {
				response = new BaseResponse();
				response.setCode(be.getMessage());
			} catch (Throwable e) {
				response = new BaseResponse();
				response.setCode(BaseRequest.ERROR_INTERNAL);
				response.setMessage(e.getMessage());
				e.printStackTrace();
			}

			// 调用完成
			
			//i18n
			if (response.getMessage() == null) {
				response.setMessage(Optional.ofNullable(i18nComponent.getMessage(response.getCode(), request.getLang()))
						.orElse(response.getCode()));
			}
			
			return response;
		} catch (SysException se) {
			BaseResponse response = new BaseResponse();
			response.setCode(se.getCode());
			response.setMessage(se.getMessage());
			return response;
		} catch (Exception e) {
			logger.info(e.getMessage());
			BaseResponse response = new BaseResponse();
			e.printStackTrace();
			response.setCode(BaseRequest.ERROR_INTERNAL);
			response.setMessage(e.getMessage());
			return response;
		}
	}
}
