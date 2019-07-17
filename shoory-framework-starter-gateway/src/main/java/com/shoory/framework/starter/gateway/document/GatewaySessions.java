/**
 * 
 */
package com.shoory.framework.starter.gateway.document;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Data;

/**
 * @author Rui.Xu
 *
 */
@Data
@RedisHash(value = "gateway_sessions", timeToLive = 7200L)
public class GatewaySessions implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String accessToken;
	@Indexed
	private String credential;
	private Set<String> uris;
	
	private String signKey;
	private String refreshToken;
}
