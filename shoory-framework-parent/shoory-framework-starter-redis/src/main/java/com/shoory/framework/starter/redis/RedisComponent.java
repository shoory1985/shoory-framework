package com.shoory.framework.starter.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public Object findByKey(String key) {
		return valueOperations.get(key);
	}
	
	public <T> T findByKey(String key, Class<T> clazz) {
		try {
			return (T)this.findByKey(key);
		} catch (Throwable e) {
			return null;
		}
	}

	public void deleteByKey(String key) {
		redisTemplate.delete(key);
	}

	public String save(String key, Object object, long timeout) {
		save(key, object, timeout, TimeUnit.SECONDS);
		return key;
	}

	public String save(String key, Object object, long timeout, TimeUnit unit) {
		valueOperations.set(key, object, timeout, unit);
		return key;
	}

	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	public boolean lock(String key, long timeout, TimeUnit timeUnit) {
		if (valueOperations.setIfAbsent(key, true)) {
			//获得分布式锁
			if (timeout > 0) {
				redisTemplate.expire(key, timeout, timeUnit);
			}
			return true;
		} else {
			//获取分布式锁失败
			return false;
		}
	}
	
	public boolean expire(String key, long timeout, TimeUnit timeUnit) {
		return redisTemplate.expire(key, timeout, timeUnit);
	}
	
	public void unlock(String key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * 原子加
	 * @param key
	 * @param value
	 * @return
	 */
	public Long increase(String key, long value) {
		return valueOperations.increment(key, value);
	}
	/**
	 * 原子加1
	 * @param key
	 * @param value
	 * @return
	 */
	public Long increase(String key) {
		return valueOperations.increment(key, 1);
	}
	/**
	 * 原子减
	 * @param key
	 * @param value
	 * @return
	 */
	public Long decrease(String key, long value) {
		return valueOperations.increment(key, -value);
	}
	/**
	 * 原子减1
	 * @param key
	 * @param value
	 * @return
	 */
	public Long decrease(String key) {
		return valueOperations.increment(key, -1);
	}
}
