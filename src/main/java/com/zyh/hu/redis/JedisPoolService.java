package com.zyh.hu.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 初始化REDIS连接池,提供REDIS连接
 *
 * @Description:
 * @author HU
 * @date 2020年9月24日
 */
@Service("JedisPoolService")
public class JedisPoolService {

	private static Logger logger = LoggerFactory.getLogger(JedisPoolService.class);
	private static final int TIMEOUT = 10000;
	
	private JedisPool jedisPool;
	
	@Value("${REDIS.IP}")
	private String redisIp;//REDIS服务器IP
	
	@Value("${REDIS.PORT}")
	private String redisPort;//REDIS的端口号
	
	@Value("${REDIS.PWD}")
	private String redisPwd;//REDIS访问密码
	
	@Value("${REDIS.MAXTOTAL}")
	private String redisMaxtotal;//如果赋值为-1,则表示不限制.如果pool已经分配了maxActive个JEDIS实例,则此时pool的状态为exhausted(耗尽)
	
	@Value("${REDIS.MAXIDLE}")
	private String redisMaxidle;//一个pool最多有多少个状态为idle(空闲的)的JEDIS实例,默认值也是8
	
	@Value("${REDIS.MAXWAITMILLIS}")
	private String redisMaxwaitmillis;//等待可用连接的最大时间,单位毫秒,默认值为-1,表示永不超时.如果超过等待时间,则直接抛出JedisConnectionException
	
	@Value("${REDIS.TESTONBORROW}")
	private String redisTestonborrow;//在borrow一个JEDIS实例时,是否提前进行validate操作,如果为true,则得到的JEDIS实例均是可用的
    
    //初始化REDIS连接池
    @PostConstruct
    public void setJedisPool(){
    	JedisPoolConfig config = new JedisPoolConfig();
    	config.setMaxTotal(Integer.valueOf(redisMaxtotal));
        config.setMaxIdle(Integer.valueOf(redisMaxidle));
        config.setMaxWaitMillis(Integer.valueOf(redisMaxwaitmillis));
        config.setTestOnBorrow(Boolean.valueOf(redisTestonborrow));
        setJedisPool(new JedisPool(config, redisIp, Integer.valueOf(redisPort), TIMEOUT, redisPwd));
    }
    
    //销毁REDIS连接池
    @PreDestroy
    public void destryPool(){
    	if (jedisPool != null) {
    		jedisPool.destroy();
    	}
    }
    
   //获取JEDIS实例
	public Jedis getResource() {
		if (jedisPool != null) {
			Jedis resource = jedisPool.getResource();
			return resource;
		}
		logger.error("---jedisPool初始化失败");
		return null;
	}
    
    //释放JEDIS资源
	public void closeResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
}
