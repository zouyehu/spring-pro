package com.zyh.hu.redis;

import com.zyh.hu.entity.SysProductInfo;

public interface ProductInfoRedisService {

	/**
	 * 获取产品信息
	 */
	public SysProductInfo getProductInfoFromRedis(String productCode);
	
	/**
	 * 产品名称添加、修改
	 */
	public void addOrUpdateProductInfo(SysProductInfo productInfo); 
	
	/**
	 * 产品名称删除
	 */
	public void removeByProductCode(String productCode);
}
