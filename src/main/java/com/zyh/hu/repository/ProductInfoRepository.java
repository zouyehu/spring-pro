package com.zyh.hu.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.zyh.hu.entity.SysProductInfo;

public interface ProductInfoRepository extends BaseRepository<SysProductInfo, Long>{

	/**
	 * 根据产品编码获取产品信息
	 * @param productCode
	 * @return
	 */
	public SysProductInfo findByProductCode(String productCode);
	
	/**
	 * 根据产品编码删除产品信息
	 * @param productCode
	 */
	@Transactional
	@Modifying
	@Query(value = "delete SysProductInfo p where p.productCode=:productCode")
	public void deleteByProductCode(@Param("productCode") String productCode);
}
