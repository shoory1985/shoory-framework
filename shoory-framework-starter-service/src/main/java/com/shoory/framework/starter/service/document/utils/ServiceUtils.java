package com.shoory.framework.starter.service.document.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.service.document.MethodInfos;
import com.shoory.framework.starter.service.document.ServiceInfos;
import com.shoory.framework.starter.service.document.SimpleMethodInfos;
@Component
public class ServiceUtils {
	
	@Autowired
	private MethodUtils methodUtils;
	
	public  ServiceInfos getInfo(Class<?> api) {
		ServiceInfos ret = new ServiceInfos();

		Optional.ofNullable(api.getAnnotation(FeignClient.class))
			.ifPresent(feignClient -> ret.setService(feignClient.value()));
		Optional.ofNullable(api.getAnnotation(ApiName.class))
			.ifPresent(apiName -> ret.setName(apiName.value()));
		Optional.ofNullable(api.getAnnotation(ApiDescription.class))
			.ifPresent(apiDescription -> ret.setDescription(apiDescription.value()));
		
		return ret;
	}


	public  Map<String, MethodInfos> getMethodInfos(Class<?> api, ServiceInfos serviceInfo) {
		Map<String, MethodInfos> ret = new HashMap<String, MethodInfos>();
		// 方法
		Arrays.stream(api.getDeclaredMethods())
			.forEach(method -> {
				MethodInfos methodInfo = methodUtils.getInfo(method);
				ret.put(methodInfo.getMethod(), methodInfo);
			});
		
		//拷
		List<SimpleMethodInfos> methods = new ArrayList<SimpleMethodInfos>();
		ret.values().stream()
			.sorted(Comparator.comparing(MethodInfos::getMethod))
			.forEach(methodInfo -> {
				SimpleMethodInfos smi = new SimpleMethodInfos();
				smi.setName(methodInfo.getName());
				smi.setMethod(methodInfo.getMethod());
				smi.setDescription(methodInfo.getDescription());
				smi.setModule(methodInfo.getModule());
				methods.add(smi);
			});
		serviceInfo.setMethods(methods.toArray(new SimpleMethodInfos[methods.size()]));
			
		return ret;
	}
}
