package com.zyh.hu.service.requestxml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "service")
@XmlType(propOrder = { "shHeadRequest", "shBodyRequest"}) 
public class SHRequest {

	private SHHeadRequest shHeadRequest;
	private SHBodyRequest shBodyRequest;
	
	@XmlElement(name = "head")
	public SHHeadRequest getShHeadRequest() {
		return shHeadRequest;
	}
	public void setShHeadRequest(SHHeadRequest shHeadRequest) {
		this.shHeadRequest = shHeadRequest;
	}
	@XmlElement(name = "body")
	public SHBodyRequest getShBodyRequest() {
		return shBodyRequest;
	}
	public void setShBodyRequest(SHBodyRequest shBodyRequest) {
		this.shBodyRequest = shBodyRequest;
	}
	
}
