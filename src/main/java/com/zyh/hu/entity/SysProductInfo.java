package com.zyh.hu.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 产品模型
 * @author HU
 *
 */
@Entity
@Table(name = "SYS_PRODUCT_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysProductInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6308958324303721595L;
	
	@JsonIgnore
	public static String getRedisKey(String code){
		return "SYS_PRODUCT_INFO" + "||" + code;
	}

	@Id
	@Column(name="ID")
	@GeneratedValue
	private int id;
	
	/**
	 * 产品名
	 */
	@Column(length = 30, name = "PRODUCT_NAME")
	private String productName;
	
	/**
	 * 产品代码
	 */
	@Column(length = 200, name = "PRODUCT_CODE")
	private String productCode;
	
	/**
	 * 产品描述
	 */
	@Column(name = "PRODUCT_DESCRIBE")
	private String productDescribe;
	
	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescribe() {
		return productDescribe;
	}

	public void setProductDescribe(String productDescribe) {
		this.productDescribe = productDescribe;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
