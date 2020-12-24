package com.shoory.framework.starter.service.document.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.api.annotation.Api;
import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.service.document.models.MethodInfos;
import com.shoory.framework.starter.service.document.models.ModuleInfos;
import com.shoory.framework.starter.service.document.models.ServiceInfos;
import com.shoory.framework.starter.service.document.models.SimpleMethodInfos;
@Component
public class ServiceUtils {
	@Autowired
	private ApiInfo apiInfo;
	@Autowired
	private DocumentUtils documentUtils;
	@Autowired
	private MethodUtils methodUtils;
	
	@Bean
	public ServiceUtils getServiceUtils() {
		return new ServiceUtils();
	}
	
	public  ServiceInfos getInfo(Class<?> api) {
		ServiceInfos ret = new ServiceInfos();

		Optional.ofNullable(api.getAnnotation(Api.class))
			.ifPresent(feignClient -> ret.setService(feignClient.value()));
		Optional.ofNullable(api.getAnnotation(ApiName.class))
			.ifPresent(apiName -> ret.setName(apiName.value()));
		Optional.ofNullable(api.getAnnotation(ApiDescription.class))
			.ifPresent(apiDescription -> ret.setDescription(apiDescription.value()));
		
		return ret;
	}


	public  void fillMethodInfos() {
		Map<String, MethodInfos> mapMethod = documentUtils.getMapMethod();
		Map<String, ModuleInfos> mapModule = documentUtils.getMapModule();

		// 方法
		Arrays.stream(apiInfo.getApiClass().getDeclaredMethods())
			.forEach(method -> {
				MethodInfos methodInfo = methodUtils.getInfo(method);
				mapMethod.put(methodInfo.getMethod(), methodInfo);
			});
		
		//拷
		List<SimpleMethodInfos> methods = new ArrayList<SimpleMethodInfos>();
		mapMethod.values().stream()
			.sorted(Comparator.comparing(MethodInfos::getMethod))
			.forEach(methodInfo -> {
				SimpleMethodInfos smi = new SimpleMethodInfos();
				smi.setName(methodInfo.getName());
				smi.setMethod(methodInfo.getMethod());
				smi.setDescription(methodInfo.getDescription());
				smi.setModule(methodInfo.getModule());
				methods.add(smi);
				
				//填充框架
				ModuleInfos mi = mapModule.get(smi.getModule());
				if (mi == null) {
					mapModule.put(smi.getModule(), mi = new ModuleInfos());
					mi.setMethods(new HashMap<String, SimpleMethodInfos>());
					mi.setModule(smi.getModule());
				}
				mi.getMethods().put(smi.getMethod(), smi);
			});
		
	}
}
