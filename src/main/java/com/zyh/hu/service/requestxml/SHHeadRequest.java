package com.zyh.hu.service.requestxml;

import javax.xml.bind.annotation.XmlElement;

public class SHHeadRequest {

	private String hutrancode;		//交易代码
	private String hutranserno;		//请求流水号
	private String hureqsys;		//请求来源
	private String hureqdate;		//请求日期
	private String hureqtime;		//请求时间
	
	@XmlElement(name = "hutrancode")
	public String gethutrancode() {
		return hutrancode;
	}
	public void sethutrancode(String hutrancode) {
		this.hutrancode = hutrancode;
	}
	@XmlElement(name = "hutranserno")
	public String gethutranserno() {
		return hutranserno;
	}
	public void sethutranserno(String hutranserno) {
		this.hutranserno = hutranserno;
	}
	@XmlElement(name = "hureqsys")
	public String gethureqsys() {
		return hureqsys;
	}
	public void sethureqsys(String hureqsys) {
		this.hureqsys = hureqsys;
	}
	@XmlElement(name = "hureqdate")
	public String gethureqdate() {
		return hureqdate;
	}
	public void sethureqdate(String hureqdate) {
		this.hureqdate = hureqdate;
	}
	@XmlElement(name = "hureqtime")
	public String gethureqtime() {
		return hureqtime;
	}
	public void sethureqtime(String hureqtime) {
		this.hureqtime = hureqtime;
	}
	
}
