package com.shoory.framework.starter.service.document.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiModule;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.service.document.models.MethodInfos;

@Component
public class MethodUtils {
	@Autowired
	private FieldUtils fieldUtils;
	
	public  MethodInfos getInfo(Method method) {
		MethodInfos ret = new MethodInfos();
		
		Optional.ofNullable(method.getAnnotation(PostMapping.class)).ifPresent(postMapping -> {
			//方法
			ret.setMethod(method.getName());
			//模块
			Optional.ofNullable(method.getAnnotation(ApiModule.class)).ifPresent(apiModule -> {
				ret.setModule(apiModule.value());
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
				.ifPresent(classRequest ->{ 
					ret.setRequestFields(fieldUtils.getList(classRequest, false));
					ret.setReturns(fieldUtils.getList(classRequest));
					//如果module为空，取request所在的包倒数第二段（第一段是request）
					if (StringUtils.isBlank(ret.getModule())) {
						String[] pieces = classRequest.getPackageName().split("\\.");
						if (pieces.length - 2 >= 0) {
							ret.setModule(pieces[pieces.length - 2]);
						}
					}
				});

			//出参
			Optional.ofNullable(method.getReturnType())
				.ifPresent(classResponse -> ret.setResponseFields(fieldUtils.getList(classResponse, true)));
			
			
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
