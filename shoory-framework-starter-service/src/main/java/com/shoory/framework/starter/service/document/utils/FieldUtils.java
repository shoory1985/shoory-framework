package com.shoory.framework.starter.service.document.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.annotation.ApiArticle;
import com.shoory.framework.starter.api.annotation.ApiArticles;
import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiExamples;
import com.shoory.framework.starter.api.annotation.ApiModel;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.annotation.ApiReturn;
import com.shoory.framework.starter.api.annotation.ApiReturns;
import com.shoory.framework.starter.api.annotation.ApiVersion;
import com.shoory.framework.starter.service.BaseService;
import com.shoory.framework.starter.service.SpringUtil;
import com.shoory.framework.starter.service.document.FieldInfos;

@Component
public class FieldUtils {
	private static FieldInfos getInfo(Field field) {
		FieldInfos fieldInfo = new FieldInfos();
		//类名
		fieldInfo.setClassName(field.getType().getTypeName());
		//字段
		fieldInfo.setField(field.getName());
		//中文名称
		Optional.ofNullable(field.getAnnotation(ApiName.class)).ifPresent(
				apiName -> fieldInfo.setName(apiName.value()));
		//是否必需
		Optional.ofNullable(field.getAnnotation(NotNull.class)).ifPresent(
				notNull -> fieldInfo.setRequired(true));
		//描述
		Optional.ofNullable(field.getAnnotation(ApiDescription.class)).ifPresent(
				apiDescription -> fieldInfo.setDescription(apiDescription.value()));
		//例子
		Optional.ofNullable(field.getAnnotation(ApiExamples.class)).ifPresent(
				ApiExamples -> fieldInfo.setExamples(ApiExamples.value()));
		//约束
		
		//对象参数
		fieldInfo.setParams(getApiModel(field));
		
		return fieldInfo;
	}

	private static FieldInfos getInfo(Method getterMethod) {
		FieldInfos fieldInfo = new FieldInfos();
		//类名
		//fieldInfo.setClassName(method.getType().getTypeName() + (method.getType().isArray() ? "[]" : ""));
		//字段
		String rawName = getterMethod.getName();
		if (rawName.indexOf("is") == 0) {
			fieldInfo.setField(rawName.substring(2, 2 + 1).toLowerCase() + rawName.substring(2 + 1));
		} else if (rawName.indexOf("get") == 0) {
			fieldInfo.setField(rawName.substring(3, 3 + 1).toLowerCase() + rawName.substring(3 + 1));
		}
		//中文名称
		Optional.ofNullable(getterMethod.getAnnotation(ApiName.class)).ifPresent(
				apiName -> fieldInfo.setName(apiName.value()));
		//是否必需
		Optional.ofNullable(getterMethod.getAnnotation(NotNull.class)).ifPresent(
				notNull -> fieldInfo.setRequired(true));
		//描述
		Optional.ofNullable(getterMethod.getAnnotation(ApiDescription.class)).ifPresent(
				apiDescription -> fieldInfo.setDescription(apiDescription.value()));
		//例子
		Optional.ofNullable(getterMethod.getAnnotation(ApiExamples.class)).ifPresent(
				ApiExamples -> fieldInfo.setExamples(ApiExamples.value()));
		//约束
		
		//对象参数
		//fieldInfo.setParams(getApiModel(method));
		//对象参数
		//fieldInfo.setParams(getApiModel(field));
		
		return fieldInfo;
	}

	private static FieldInfos[] getApiModel(Field field) {
		if (field.getAnnotation(ApiModel.class) != null) {
			return getList(field.getType().isArray() ? field.getType().getComponentType() : field.getType(), false);
		}
		return null;
	}
	
	public static FieldInfos[] getList(Class<?> clazz, boolean isResponse) {
		List<FieldInfos> list = new ArrayList<FieldInfos>();
		if (isResponse) {
			list.add(new FieldInfos("code", "返回代码", true, "", "", "String", null));
			list.add(new FieldInfos("message", "返回消息", true, "", "", "String", null));
		}
		
		if (clazz.isInterface()) {
			Arrays.stream(clazz.getDeclaredMethods())
				.sorted(Comparator.comparing(Method::getName))
				.forEach(method -> list.add(getInfo(method)));
		} else {
			Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> !field.getName().equalsIgnoreCase("serialVersionUID"))
				.sorted(Comparator.comparing(Field::getName))
				.forEach(field -> list.add(getInfo(field)));
		}
		
		return list.toArray(new FieldInfos[list.size()]);
	}
	
}
