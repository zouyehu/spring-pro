package com.zyh.hu.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

public class RedisUtil {

	public static final void closeJedis(Jedis jedis){
		if (jedis != null) {
			jedis.close();
		}
	}
	
	public static final void closeResource(JedisPool jedisPool, Jedis jedis){
		if (jedisPool != null && jedis != null) {
			jedisPool.close();
		}
	}
	
	public static final void closeSentineResource(JedisSentinelPool jedisSentinelPool, Jedis jedis){
		if (jedisSentinelPool != null && jedis != null) {
			jedisSentinelPool.close();
		}
	}
	
	public static final void setExpireKeyAndVal(Jedis jedis, String key, String value, int expireSeconds){
		jedis.set(key, value);
		jedis.expire(key, expireSeconds);
	}
}
