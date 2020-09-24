package com.zyh.hu.redis;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Redis哨兵模式
 *
 * @Description:
 * @author HU
 * @date 2020年9月24日
 */
@Component("RedisBasicService")
public class RedisBasicService {

	/*@Autowired
	private JedisPoolConfig redisConfig;
	
	private JedisSentinelPool jedisSentinelPool;
	
	@Value("${REDIS.SENTINELS}")
	private String redisSentinels;
	
	@Value("${REDIS.MASTERNAME}")
	private String redisMasterName;
	
	@Value("${REDIS.PWD}")
	private String redisPwd;
	
	@PostConstruct
	public void setSentinelPool() {
		String[] sentinelAddresses = redisSentinels.split(";");
		Set<String> sentinelsSet = new HashSet<String>();
		for (String sentinelAddress : sentinelAddresses) {
			sentinelsSet.add(sentinelAddress);
		}
		setJedisSentinelPool(new JedisSentinelPool(redisMasterName, sentinelsSet,
				redisConfig, redisPwd));
	}
	
	@PreDestroy
	public void destryPool(){
		if(jedisSentinelPool!=null){
			jedisSentinelPool.destroy();
		}
	}
	
	public JedisSentinelPool getJedisSentinelPool() {
		return jedisSentinelPool;
	}

	public void setJedisSentinelPool(JedisSentinelPool jedisSentinelPool) {
		this.jedisSentinelPool = jedisSentinelPool;
	}
	
	public Jedis getResource(){
		return jedisSentinelPool.getResource();
	}
	
	public void closeResource(){
		jedisSentinelPool.close();
	}*/
}