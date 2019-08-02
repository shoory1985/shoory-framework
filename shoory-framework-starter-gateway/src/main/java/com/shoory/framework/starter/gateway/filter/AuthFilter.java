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

/**
 * 鉴权过滤器
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
	private final String ERROR_FORBIDDEN = "{\"code\":\"ERROR_FORBIDDEN\"}";
	private final String ERROR_ACCESS_TOKEN_MISSED = "{\"code\":\"ERROR_ACCESS_TOKEN_MISSED\"}";
	private final String ERROR_INVALID_ACCESS_TOKEN = "{\"code\":\"ERROR_INVALID_ACCESS_TOKEN\"}";
	private final String ERROR_SIGNATURE_MISSED = "{\"code\":\"ERROR_SIGNATURE_MISSED\"}";
	private final String ERROR_INVALID_SIGNATURE = "{\"code\":\"ERROR_INVALID_SIGNATURE\"}";

	@Value("${spring.profiles.active}")
	private String environment;
	@Autowired
	private GatewaySessionRepository gatewaySessionRepository;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		ServerHttpRequest request = exchange.getRequest();
		System.out.println(JSONObject.toJSONString(request));
		
		
		// 方法检查
		switch (request.getMethod()) {
		case OPTIONS:
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.OK);
			return Mono.empty();
		case GET:
			// 直接放行先
			break;
		case POST: 
		{
			// URI检查
			String uri = request.getPath().value().toLowerCase();
			String pieces[] = uri.split("/");
			// URI:/service/yyy...
			// URI Index:0/1/2....
			if (pieces.length < 3) {
				// 节数不够
				return this.forbidden(exchange.getResponse(), ERROR_FORBIDDEN);
			} else if (pieces[2].startsWith("pub")) {
				// pub放行
			} else if (pieces[2].startsWith("sys")) {
				// sys拦截
				return this.forbidden(exchange.getResponse(), ERROR_FORBIDDEN);
			} else {
				// 需要鉴权
				String accessToken = request.getHeaders().getFirst("Access-Token");
				String signature = request.getHeaders().getFirst("Signature");

				if (StringUtils.isEmpty(accessToken)) {
					return this.forbidden(exchange.getResponse(), ERROR_ACCESS_TOKEN_MISSED);
				}
				// if (StringUtils.isEmpty(signature)) {
				// return this.forbidden(ctx, ERROR_SIGNATURE_MISSED);
				// }

				// Token检查
				GatewaySessions gatewaySession = gatewaySessionRepository.findById(accessToken).orElse(null);
				if (gatewaySession == null) {
					return this.forbidden(exchange.getResponse(), ERROR_INVALID_ACCESS_TOKEN);
				}
				// 匹配URI
				List<String> list = null;
				String uriPattern = uri;
				boolean pass = false;

				do {
					if (gatewaySession.getUris().contains(uriPattern)) {
						pass = true;
						break;
					}

					if (list == null) {
						List<String> listTemp = new ArrayList<String>();
						Arrays.asList(pieces).stream().forEach(item -> {
							listTemp.add(item);
						});
						list = listTemp;
					}
					int lastIndex = list.size() - 1;
					if (list.get(lastIndex).equals("*")) {
						list.remove(list.get(lastIndex));
					} else {
						list.set(lastIndex, "*");
					}
					uriPattern = String.join("/", list);
				} while (list.size() > 2);

				if (!pass) {
					return this.forbidden(exchange.getResponse(), ERROR_FORBIDDEN);
				}

		        
		        //向headers中放文件，记得build
		        ServerHttpRequest newRequest = exchange.getRequest().mutate()
		        		.header("Credential", gatewaySession.getCredential())
		        		//.header("ClientAddress", request.getHeaders().getFirst("X-Real-IP"))
		        		.build();
				return chain.filter(exchange.mutate().request(newRequest).build());
			}
		}
		}

		return chain.filter(exchange);
	}

	private Mono<Void> forbidden(ServerHttpResponse response, String responseBody) {
		response.setStatusCode(HttpStatus.OK); // 设置浏览器返回的回执码
		response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

		return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
	}

	/**
	 * 设置过滤器的执行顺序
	 * 
	 * @return
	 */
	@Override
	public int getOrder() {
		return 1;
	}
}