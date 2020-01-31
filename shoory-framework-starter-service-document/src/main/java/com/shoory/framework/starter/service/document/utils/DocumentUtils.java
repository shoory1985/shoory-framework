package com.shoory.framework.starter.service.document.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.service.I18nComponent;
import com.shoory.framework.starter.service.document.models.MethodInfos;
import com.shoory.framework.starter.service.document.models.ModelInfos;
import com.shoory.framework.starter.service.document.models.ModuleInfos;
import com.shoory.framework.starter.service.document.models.ServiceInfos;
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
	@Getter
	private Map<String, Integer> mapCode = new HashMap<String, Integer>();

	@Autowired
	private I18nComponent i18nComponent;
	
	@Bean
	public DocumentUtils getDocumentUtils() {
		return new DocumentUtils();
	}

	public synchronized boolean ready() {
		if (!inited) {

			//填充方法
			serviceUtils.fillMethodInfos();
			
			//当前
			this.serviceInfo = serviceUtils.getInfo(apiInfo.getApiClass());
			
			//依赖
			
			//
			Arrays.stream(apiInfo.getDependentAppClasses())
				.forEach(clazz -> serviceInfo.getDependentServices().add(serviceUtils.getInfo(clazz)));
			serviceInfo.setModuleCount(mapModule.size());
			serviceInfo.setMethodCount(mapMethod.size());
			serviceInfo.setModelCount(mapModel.size());
			
			i18nComponent.getMessages().values().stream().forEach(prop -> serviceInfo.setMessageCount(serviceInfo.getMessageCount() + prop.size()));
			
			serviceInfo.setLanguageCount(i18nComponent.getMessages().size());
			
			
			inited = true;
		}
		
		return inited;
	}
}
