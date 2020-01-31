package com.shoory.framework.starter.service.document.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiExamples;
import com.shoory.framework.starter.api.annotation.ApiModel;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.service.I18nComponent;
import com.shoory.framework.starter.service.document.models.FieldInfos;
import com.shoory.framework.starter.service.document.models.ModelInfos;
import com.shoory.framework.starter.service.document.models.ReturnInfos;

@Component
public class FieldUtils {

	@Autowired
	private DocumentUtils documentUtils;
	@Autowired
	private I18nComponent i18nComponent;
	
	private  FieldInfos getInfo(Field field) {
		FieldInfos fieldInfo = new FieldInfos();
		String className = this.getModelClass(field).getSimpleName();
		//类名
		fieldInfo.setClassName(className + this.suffix(field));
		//字段
		fieldInfo.setField(field.getName());
		//中文名称
		Optional.ofNullable(field.getAnnotation(ApiName.class)).ifPresent(
				apiName -> fieldInfo.setName(apiName.value()));
		//是否必需
		Optional.ofNullable(field.getAnnotation(NotNull.class)).ifPresent(
				notNull -> fieldInfo.setRequired(true));
		Optional.ofNullable(field.getAnnotation(ApiRequired.class)).ifPresent(
				apiRequired -> fieldInfo.setRequired(true));
		Optional.ofNullable(field.getAnnotation(NotBlank.class)).ifPresent(
				notBlank -> fieldInfo.setRequired(true));
		//描述
		Optional.ofNullable(field.getAnnotation(ApiDescription.class)).ifPresent(
				apiDescription -> fieldInfo.setDescription(apiDescription.value()));
		//例子
		Optional.ofNullable(field.getAnnotation(ApiExamples.class)).ifPresent(
				ApiExamples -> fieldInfo.setExamples(ApiExamples.value()));
		//约束
		
		//对象参数
		fieldInfo.setParams(getApiModel(field));
		
		//填充
		if (!documentUtils.getMapModel().containsKey(className)) {
			ModelInfos mi = new ModelInfos();
			mi.setClassName(className);
			mi.setParams(fieldInfo.getParams());
			documentUtils.getMapModel().put(className, mi);
		}
		
		return fieldInfo;
	}

	private  FieldInfos getInfo(Method getterMethod) {
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
	
	

	private  FieldInfos[] getApiModel(Field field) {
		if (field.getAnnotation(ApiModel.class) != null) {
			return getList(getModelClass(field), false);
		}
		return null;
	}
	private String suffix(Field field) {
		if (field.getType().isArray()) {return "[]";}
		if (field.getType().isAssignableFrom(List.class)) {return "[]";}
		if (field.getType().isAssignableFrom(Set.class)) {return "[]";}
		if (field.getType().isAssignableFrom(Map.class)) {return "<Map>";}
		return "";
	}
	private Class<?> getModelClass(Field field) {
		return field.getType().isArray() 
			? field.getType().getComponentType() 
			: (field.getType().isAssignableFrom(List.class) || field.getType().isAssignableFrom(Set.class) || field.getType().isAssignableFrom(Map.class))
				? (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]
				: field.getType();
	}
	
	public  FieldInfos[] getList(Class<?> clazz, boolean isResponse) {
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
				.filter(field -> !(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getType()==String.class))
				.sorted(Comparator.comparing(Field::getName))
				.forEach(field -> list.add(getInfo(field)));
		}
		
		return list.toArray(new FieldInfos[list.size()]);
	}
	
	public  ReturnInfos[] getList(Class<?> requestClass) {
		List<ReturnInfos> list = new ArrayList<ReturnInfos>();
		//预置
		String[] GENERAL_CODES = new String[] {"SUCCESS", "ERROR_INTERNAL"};
		Arrays.stream(GENERAL_CODES)
			.forEach(code -> {
				list.add(new ReturnInfos(code, i18nComponent.getMessage(code,"zh_CN")));
				documentUtils.getMapCode().put(code, 
					Optional.ofNullable(documentUtils.getMapCode().get(code))
						.map(count -> count + 1)
						.orElse(1));
			});
		
		Arrays.stream(requestClass.getDeclaredFields())
			.filter(field -> !field.getName().equalsIgnoreCase("serialVersionUID"))
			.filter(field ->Modifier.isFinal(field.getModifiers())&&Modifier.isStatic(field.getModifiers())&&field.getType()==String.class)
			.sorted(Comparator.comparing(Field::getName))
			.forEach(field ->{
				String code;
				try {
					code = (String) field.get(String.class);
				} catch (Exception e) {
					code = field.getName();
				}
				list.add(new ReturnInfos(code, i18nComponent.getMessage(code, "zh_CN")));
				documentUtils.getMapCode().put(code, 
					Optional.ofNullable(documentUtils.getMapCode().get(code))
						.map(count -> count + 1)
						.orElse(1));
			});
		
		return list.toArray(new ReturnInfos[list.size()]);
	}
	
}
