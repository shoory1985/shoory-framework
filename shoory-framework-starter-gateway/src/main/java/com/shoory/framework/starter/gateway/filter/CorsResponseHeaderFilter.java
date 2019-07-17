package com.shoory.framework.starter.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.shoory.framework.starter.gateway.document.GatewaySessions;
import com.shoory.framework.starter.gateway.repository.GatewaySessionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 跨域请求头处理过滤器扩展
 * @Author 
 * @Create 2019-04-22 14:20:06
 */
@Component
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {
	@Override
	public int getOrder() {
		// 指定此过滤器位于NettyWriteResponseFilter之后
		// 即待处理完响应体后接着处理响应头
		return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
	}

	@Override
	@SuppressWarnings("serial")
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("=====================");
		return chain.filter(exchange).then(Mono.defer(() -> {
			exchange.getResponse().getHeaders().entrySet().stream()
					.filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
					.filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN) 
							|| kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)))
					.forEach(kv -> 
			{
				kv.setValue(new ArrayList<String>() {{add(kv.getValue().get(0));}});
			});
			
			return chain.filter(exchange);
		}));
	}
}