package com.shoory.framework.starter.service.document.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import com.alibaba.nacos.client.utils.StringUtils;
import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiModule;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.service.document.MethodInfos;
import com.shoory.framework.starter.service.document.ReturnInfos;

@Component
public class MethodUtils {
	public static MethodInfos getInfo(Method method) {
		MethodInfos ret = new MethodInfos();
		
		Optional.ofNullable(method.getAnnotation(PostMapping.class)).ifPresent(postMapping -> {
			//方法
			ret.setMethod(method.getName());
			//模块
			Optional.ofNullable(method.getAnnotation(ApiModule.class)).ifPresent(apiModule -> {
				ret.setDescription(apiModule.value());
			});
			//方法名
			Optional.ofNullable(method.getAnnotation(ApiName.class)).ifPresent(apiName -> {
				ret.setName(apiName.value());
			});
			//方法描述
			Optional.ofNullable(method.getAnnotation(ApiDescription.class)).ifPresent(apiDescription -> {
				ret.setDescription(apiDescription.value());
			});
			//入参
			Arrays.stream(method.getParameters())
				.findFirst()
				.map(p -> p.getType())
				.ifPresent(classRequest -> ret.setRequestFields(FieldUtils.getList(classRequest, false)));

			//出参
			Optional.ofNullable(method.getReturnType())
				.ifPresent(classResponse -> ret.setResponseFields(FieldUtils.getList(classResponse, true)));
			
			//结果
			/*List<ReturnInfos> returns = new ArrayList<ReturnInfos>();
			returns.add(new ReturnInfos("SUCCESS", "", "调用成功"));
			Arrays.stream(method.getAnnotationsByType(ApiReturn.class))
				.sorted()
				.forEach(apiReturn -> returns.add(new ReturnInfos(apiReturn.code(), apiReturn.message(), apiReturn.description())));
			returns.add(new ReturnInfos("ERROR_INTERNAL", "(异常名称)", "发生内部异常"));
			returns.add(new ReturnInfos("ERROR_UNKNOWN", "未知错误", "请与开发者联系"));
			ret.setReturns(returns.toArray(new ReturnInfos[returns.size()]));
			*/
		});

		return ret;
	}
}
