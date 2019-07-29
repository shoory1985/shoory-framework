package com.shoory.framework.starter.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.service.document.MethodInfos;
import com.shoory.framework.starter.service.document.ServiceInfos;
import com.shoory.framework.starter.service.document.utils.ServiceUtils;
import com.shoory.framework.starter.utils.PojoUtils;

@RestController
@CrossOrigin
public class DocumentController {
	@Autowired(required = false)
	private ApiInfo apiInfo;
	@Value("${spring.profiles.active:dev}")
	private String springProfileActive;
	@Autowired
	private PojoUtils pojoUtils;
	@Autowired
	private I18nComponent i18nComponent;
	@Autowired
	private ServiceUtils serviceUtils;


	private ServiceInfos serviceInfo;
	private Map<String, MethodInfos> methodInfos;
	
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String serviceInfo(HttpServletRequest request, HttpServletResponse response) {
		if (!this.check()) {
			return "{}";
		}
		return pojoUtils.toJson(serviceInfo);
	}
	@GetMapping(value = "/i18n", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String methodInfo(HttpServletRequest request, HttpServletResponse response) {
		return pojoUtils.toJson(i18nComponent.getMessages());
	}
	
	@GetMapping(value = "/{methodName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String methodInfo(@PathVariable("methodName") String methodName, HttpServletRequest request, HttpServletResponse response) {
		if (!this.check()) {
			return "{}";
		}

		MethodInfos methodInfo = methodInfos.get(methodName);
		if (methodInfo == null) {
			return "{}";
		}
		
		return pojoUtils.toJson(methodInfo);
	}
	
	private boolean check() {
		return apiInfo != null 
				&& apiInfo.getApiClass() != null
				&& "prod".equalsIgnoreCase(springProfileActive)
				&& (serviceInfo != null || (serviceInfo = serviceUtils.getInfo(apiInfo.getApiClass())) != null)
				&& (methodInfos != null || (methodInfos = serviceUtils.getMethodInfos(apiInfo.getApiClass(), serviceInfo)) != null);
	}

	@Bean
	public SimpleUrlHandlerMapping StaticHandlerMapping(ResourceHttpRequestHandler handler) {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
		mapping.setUrlMap(Collections.singletonMap("**/*.*", handler));
		return mapping;
	}
}