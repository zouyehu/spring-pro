package com.zyh.hu.service.hu;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.zyh.hu.comments.ResponseBaseBean;
import com.zyh.hu.comments.ResponseStatusEnum;
import com.zyh.hu.comments.exception.SysException;
import com.zyh.hu.entity.SysProductInfo;
import com.zyh.hu.redis.JedisPoolService;
import com.zyh.hu.redis.ProductInfoRedisService;
import com.zyh.hu.repository.ProductInfoRepository;
import com.zyh.hu.service.BaseService;
import com.zyh.hu.utils.DateUtil;

/**
 * 通过REDIS设置唯一编号
 *
 * @Description:
 * @author HU
 * @date 2020年9月22日
 */
@Service("HU100004Service")
public class HU100004Service implements BaseService {

	private static final Logger logger = LoggerFactory.getLogger(HU100004Service.class);
	private static final String TRANSCODE = "HU100004";
	private static final String UNIQUESERIALNO = "UNIQUE_SERIAL_NO";
	// 设置key有效时间
	private static final int expire_time = 24*60*60;
	
//	@Autowired
//	private RedisBasicService redisBasicService;
	
	@Autowired
	private JedisPoolService jedisPoolService;
	
	@Autowired
	private ProductInfoRedisService productInfoRedisService;
	
	@Autowired
	private ProductInfoRepository productInfoRepository;
	
	@Override
	public ResponseBaseBean doExecute(JSONObject jsonO,
			HttpServletRequest request, HttpServletResponse response,
			String uuid) {
		ResponseBaseBean baseBean = new ResponseBaseBean();
		baseBean.setTransCode(TRANSCODE);
		baseBean.initStatus(ResponseStatusEnum.RESPONSE_SUCCESS);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String nowDay = DateUtil.format(new Date(), "yyyyMMdd");
		Jedis jedis = null;
		Object resultObj = null;
		SysProductInfo product = null;
		
		String businflag = String.valueOf(jsonO.get("businflag"));		//业务标识-1:查询产品信息,为空:获取唯一流水号
		String productCode = String.valueOf(jsonO.get("productCode"));	//产品编码
		
		try {
			if ("1".equals(businflag)) {
				// 默认先从REDIS缓存中查询
				product = productInfoRedisService.getProductInfoFromRedis(productCode);
				if (null == product) {
					// 再查询数据库,缓存到REDIS中
					product = productInfoRepository.findByProductCode(productCode);
					if (null != product) {
						productInfoRedisService.addOrUpdateProductInfo(product);
					}
				}
			} else {
				// 生成唯一流水号
				jedis = jedisPoolService.getResource();
				String data = String.format("%08d", jedis.incr(UNIQUESERIALNO + nowDay));
				jedis.expire(UNIQUESERIALNO + nowDay, expire_time);
				resultObj = nowDay + data;
			}
		} catch (Exception e) {
			logger.error("---HU100004发生异常:{}", e);
		}
		
		if (resultObj != null || product != null) {
			returnMap.put("productInfo", product);
			returnMap.put("uniqueNo", resultObj);
			baseBean.setResponseBody(returnMap);
		} else {
			throw new SysException(ResponseStatusEnum.RESPONSE_FAILED);
		}
		
		return baseBean;
	}

}
