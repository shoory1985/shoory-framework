package com.shoory.framework.starter.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoory.framework.starter.api.response.BaseResponse;
import com.shoory.framework.starter.utils.PojoUtils;

@RestController
@CrossOrigin
public class ServiceController {

	@Autowired
	private MethodRouter methodRouter;

	@PostMapping(value = "/{methodName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String call(@PathVariable("methodName") String methodName, @RequestBody String json,
			HttpServletRequest request, HttpServletResponse response) {
		return methodRouter.jsonInvoke(methodName, json);
	}
}