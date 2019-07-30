package com.shoory.framework.starter.service.document.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.service.document.models.MethodInfos;
import com.shoory.framework.starter.service.document.models.ModelInfos;
import com.shoory.framework.starter.service.document.models.ModuleInfos;
import com.shoory.framework.starter.service.document.models.ServiceInfos;
import com.shoory.framework.starter.service.document.models.SimpleMethodInfos;

import lombok.Getter;
@Component
public class DocumentUtils {
	@Autowired
	private ApiInfo apiInfo;
	@Value("${spring.profiles.active:dev}")
	private String springProfileActive;
	@Autowired
	private ServiceUtils serviceUtils;
	
	boolean inited = false;
	@Getter
	private Map<String, ModuleInfos> mapModule = new HashMap<String, ModuleInfos>();
	@Getter
	private ServiceInfos serviceInfo = null;
	@Getter
	private Map<String, MethodInfos> mapMethod = new HashMap<String, MethodInfos>();
	@Getter
	private Map<String, ModelInfos> mapModel = new HashMap<String, ModelInfos>();

	@Bean
	public DocumentUtils getDocumentUtils() {
		return new DocumentUtils();
	}

	public boolean ready() {
		if (!inited) {
			//当前
			this.serviceInfo = serviceUtils.getInfo(apiInfo.getApiClass());
			//依赖
			//填充方法
			serviceUtils.fillMethodInfos();
			
			inited = true;
		}
		
		return inited;
	}
}
