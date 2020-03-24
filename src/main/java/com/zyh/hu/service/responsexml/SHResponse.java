package com.zyh.hu.service.responsexml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "service")
public class SHResponse {

	private SHHeadResponse shHeadResponse;
	private SHBodyResopnse shBodyResopnse;
	
	@XmlElement(name = "head")
	public SHHeadResponse getShHeadResponse() {
		return shHeadResponse;
	}
	public void setShHeadResponse(SHHeadResponse shHeadResponse) {
		this.shHeadResponse = shHeadResponse;
	}
	@XmlElement(name = "body")
	public SHBodyResopnse getShBodyResopnse() {
		return shBodyResopnse;
	}
	public void setShBodyResopnse(SHBodyResopnse shBodyResopnse) {
		this.shBodyResopnse = shBodyResopnse;
	}
	
}
