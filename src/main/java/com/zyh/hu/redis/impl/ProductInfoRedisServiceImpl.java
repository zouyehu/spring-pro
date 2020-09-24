package com.zyh.hu.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.zyh.hu.entity.SysProductInfo;
import com.zyh.hu.redis.JedisPoolService;
import com.zyh.hu.redis.ProductInfoRedisService;
import com.zyh.hu.utils.JsonUtil;
import com.zyh.hu.utils.RedisUtil;
import com.zyh.hu.utils.StringsUtil;

@Component("productInfoRedisService")
public class ProductInfoRedisServiceImpl implements ProductInfoRedisService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final int EXPIRESECONDS = 24 * 60 * 60;
	
//	@Autowired
//	private RedisBasicService redisBasicService;
	
	@Autowired
	private JedisPoolService jedisPoolService;
	
	@Override
	public SysProductInfo getProductInfoFromRedis(String productCode) {
		Jedis jedis = null;
		SysProductInfo productInfo = null;
		try {
			jedis = jedisPoolService.getResource();
			String value = jedis.get(SysProductInfo.getRedisKey(productCode));
			JsonUtil.setDateFormat("yyyy-MM-dd HH:mm:ss");
			if(StringsUtil.isNotNull(value)){
				productInfo = JsonUtil.jsonToObject(value, SysProductInfo.class);
			}
		} catch (Exception e) {
			logger.error("---获取产品信息异常:{}", e);
		} finally {
			//资源释放
			jedisPoolService.closeResource(jedis);
		}
		return productInfo;
	}

	@Override
	public void addOrUpdateProductInfo(SysProductInfo productInfo) {
		Jedis jedis = null;
		try {
			jedis = jedisPoolService.getResource();
			//设置key,value和缓存时间
			RedisUtil.setExpireKeyAndVal(jedis, SysProductInfo.getRedisKey(productInfo.getProductCode()), JsonUtil.objectToJsonString(productInfo), EXPIRESECONDS);
		} catch (Exception e) {
			logger.error("---增加或更新我的页面产品信息异常:{}", e);
		} finally {
			//资源释放
			jedisPoolService.closeResource(jedis);
		}
	}

	@Override
	public void removeByProductCode(String productCode) {
		Jedis jedis = null;
		try {
			jedis = jedisPoolService.getResource();
			if (StringsUtil.isNotNull(productCode)) {
				jedis.del(SysProductInfo.getRedisKey(productCode));
			}
		} catch (Exception e) {
			logger.error("---删除产品信息异常:{}", e);
		} finally {
			//资源释放
			jedisPoolService.closeResource(jedis);
		}
	}

}
