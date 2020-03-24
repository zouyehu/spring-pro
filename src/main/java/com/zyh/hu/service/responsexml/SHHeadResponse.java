package com.zyh.hu.service.responsexml;

import javax.xml.bind.annotation.XmlElement;

public class SHHeadResponse {

	private String hutrancode;			//交易代码
	private String hutranserno;		//请求流水号
	private String huressys;			//请求来源
	private String huresdate;			//响应日期
	private String hurestime;			//响应时间
	private String hutranstate;		//交易状态-S:成功  F:失败
	private String huerrorcode;		//交易返回代码
	private String huerrormsg;			//交易返回信息
	
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
	@XmlElement(name = "huressys")
	public String gethuressys() {
		return huressys;
	}
	public void sethuressys(String huressys) {
		this.huressys = huressys;
	}
	@XmlElement(name = "huresdate")
	public String gethuresdate() {
		return huresdate;
	}
	public void sethuresdate(String huresdate) {
		this.huresdate = huresdate;
	}
	@XmlElement(name = "hurestime")
	public String gethurestime() {
		return hurestime;
	}
	public void sethurestime(String hurestime) {
		this.hurestime = hurestime;
	}
	@XmlElement(name = "hutranstate")
	public String gethutranstate() {
		return hutranstate;
	}
	public void sethutranstate(String hutranstate) {
		this.hutranstate = hutranstate;
	}
	@XmlElement(name = "huerrorcode")
	public String gethuerrorcode() {
		return huerrorcode;
	}
	public void sethuerrorcode(String huerrorcode) {
		this.huerrorcode = huerrorcode;
	}
	@XmlElement(name = "huerrormsg")
	public String gethuerrormsg() {
		return huerrormsg;
	}
	public void sethuerrormsg(String huerrormsg) {
		this.huerrormsg = huerrormsg;
	}
	
}
