package com.shoory.framework.starter.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.api.constants.SysException;

@Component
public class PojoUtils {
	@Primary
	@Bean
	public PojoUtils getPojoUtils() {
		return new PojoUtils();
	}

	private final ObjectMapper jacksonJson = new ObjectMapper();
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public void copy(Object source, Object dest) {
		BeanUtils.copyProperties(source, dest);
	}

	public <S, D> List<D> copyList(List<S> source, Class<D> clazz) {
		List<D> ret = new ArrayList<D>();
		for (S s : source) {
			try {
				D d = clazz.newInstance();
				BeanUtils.copyProperties(s, d);
				ret.add(d);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

	public String toJson(Object obj) {
		try {
			return jacksonJson.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			return jacksonJson.readValue(json, clazz);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public <T> String validateAll(T t) {
		return Optional.ofNullable(validator.validate(t)
					.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(";"))
				)
				.orElse("");
	}

	public <T> String validateOne(T t) {
		return validator.validate(t)
				.stream()
				.findFirst()
				.map(ConstraintViolation::getMessage)
				.orElse("");
	}

	public <T> void validate(T t) {
		Optional.ofNullable(validateOne(t))
			.filter(message -> !message.isEmpty())
			.ifPresent(message -> {throw new BizException(message);});
	}

	public <T> T getOne(List<T> list) {
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public <T> T getOne(T[] array) {
		return array != null && array.length > 0 ? array[0] : null;
	}

	public <T> List<T> array2List(T[] array) {
		return Arrays.asList(array);
	}

	public <T> T[] list2Array(List<T> list) {
		return list == null ? null : list.toArray((T[]) new Object[list.size()]);
	}
}
