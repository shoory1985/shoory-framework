package com.shoory.framework.starter.service.document;

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
import com.shoory.framework.starter.service.I18nComponent;
import com.shoory.framework.starter.service.document.models.MethodInfos;
import com.shoory.framework.starter.service.document.models.ModelInfos;
import com.shoory.framework.starter.service.document.models.ModuleInfos;
import com.shoory.framework.starter.service.document.models.ServiceInfos;
import com.shoory.framework.starter.service.document.utils.DocumentUtils;
import com.shoory.framework.starter.service.document.utils.ServiceUtils;
import com.shoory.framework.starter.utils.PojoUtils;

@RestController
@CrossOrigin
public class DocumentMethodController {
	@Autowired
	private PojoUtils pojoUtils;
	@Autowired
	private DocumentUtils documentUtils;
	

	@GetMapping(value = "/doc/method/{method}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String methodInfo(@PathVariable("method") String method, HttpServletRequest request,
			HttpServletResponse response) {
		documentUtils.ready();
		return pojoUtils.toJson(documentUtils.getMapMethod().get(method));
	}

	@GetMapping(value = "/doc/method", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String serviceInfo(HttpServletRequest request, HttpServletResponse response) {
		documentUtils.ready();
		return pojoUtils.toJson(documentUtils.getMapMethod().keySet());
	}
}