package com.shoory.framework.starter.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoory.framework.starter.api.response.BaseResponse;
import com.shoory.framework.starter.utils.PojoUtils;

@RestController
@CrossOrigin
public class ServiceController {

	@Autowired
	private MethodRouter methodRouter;
	
	@RequestMapping(value = "/**", method = RequestMethod.POST,produces="application/json")
	public String call(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) {
		String methodName = request.getRequestURI().substring(1);
		return methodRouter.jsonInvoke(methodName, json);
	}
}