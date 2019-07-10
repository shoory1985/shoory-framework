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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.shoory.framework.starter.service.document.MethodInfos;
import com.shoory.framework.starter.service.document.ServiceInfos;
import com.shoory.framework.starter.service.document.utils.ServiceUtils;
import com.shoory.framework.starter.utils.PojoUtils;

@RestController
@CrossOrigin
public class DocumentController {
	@Autowired(required = false)
	private Class apiClass;
	@Value("${spring.profiles.active}")
	private String springProfileActive;
	@Autowired
	private PojoUtils pojoUtils;

	private ServiceInfos serviceInfo;
	private Map<String, MethodInfos> methodInfos;
	
	@RequestMapping(value = "/**", method = RequestMethod.GET, produces = "application/json")
	public String document(HttpServletRequest request, HttpServletResponse response) {
		if (apiClass == null || "prod".equalsIgnoreCase(springProfileActive)) {
			return "{}";
		}
	
		if (serviceInfo == null) {
			//初始化
			serviceInfo = ServiceUtils.getInfo(apiClass);
			methodInfos = ServiceUtils.getMethodInfos(apiClass, serviceInfo);
		}
		

		String path = request.getRequestURI();
		if (path.equals("/")) {
			return pojoUtils.toJson(serviceInfo);
		} else {
			MethodInfos methodInfo = methodInfos.get(path);
			if (methodInfo != null) {
				return pojoUtils.toJson(methodInfo);
			}
		}
		
		return "{}";
	}
	
	@Bean
	public SimpleUrlHandlerMapping StaticHandlerMapping(ResourceHttpRequestHandler handler) {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
		mapping.setUrlMap(Collections.singletonMap("**/*.*", handler));
		return mapping;
	}
}