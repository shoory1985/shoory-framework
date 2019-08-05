package com.shoory.framework.starter.gateway.filter;

import java.util.ArrayList;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.http.HttpHeaders;

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
		return chain.filter(exchange).then(Mono.defer(() -> {
			exchange.getResponse().getHeaders().entrySet().stream()
					.filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
					.filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN) 
							|| kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)))
					.forEach(kv -> kv.setValue(new ArrayList<String>() {{add(kv.getValue().get(0));}})
			);
			
			return chain.filter(exchange);
		}));
	}
}