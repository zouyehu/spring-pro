package com.zyh.hu.service.requestxml;

import javax.xml.bind.annotation.XmlElement;

public class SHBodyRequest {

	private String husubacctno;		//子账号
	private String humerserno;			//合作平台流水号
	private String hucustname;			//姓名
	private String huidno;				//身份证号
	private String hubindcardno;		//原绑定银行卡号
	private String hunewcardno;		//新绑定银行卡号
	private String hureservedphone;	//银行卡预留手机号
	private String hunewreservedphone;	//新银行卡预留手机号
	private String humoditype;			//修改类型-00:换卡  01:修改绑定卡手机号
	
	@XmlElement(name = "husubacctno")
	public String gethusubacctno() {
		return husubacctno;
	}
	public void sethusubacctno(String husubacctno) {
		this.husubacctno = husubacctno;
	}
	@XmlElement(name = "humerserno")
	public String gethumerserno() {
		return humerserno;
	}
	public void sethumerserno(String humerserno) {
		this.humerserno = humerserno;
	}
	@XmlElement(name = "hucustname")
	public String gethucustname() {
		return hucustname;
	}
	public void sethucustname(String hucustname) {
		this.hucustname = hucustname;
	}
	@XmlElement(name = "huidno")
	public String gethuidno() {
		return huidno;
	}
	public void sethuidno(String huidno) {
		this.huidno = huidno;
	}
	@XmlElement(name = "hubindcardno")
	public String gethubindcardno() {
		return hubindcardno;
	}
	public void sethubindcardno(String hubindcardno) {
		this.hubindcardno = hubindcardno;
	}
	@XmlElement(name = "hunewcardno")
	public String gethunewcardno() {
		return hunewcardno;
	}
	public void sethunewcardno(String hunewcardno) {
		this.hunewcardno = hunewcardno;
	}
	@XmlElement(name = "hureservedphone")
	public String gethureservedphone() {
		return hureservedphone;
	}
	public void sethureservedphone(String hureservedphone) {
		this.hureservedphone = hureservedphone;
	}
	@XmlElement(name = "hunewreservedphone")
	public String gethunewreservedphone() {
		return hunewreservedphone;
	}
	public void sethunewreservedphone(String hunewreservedphone) {
		this.hunewreservedphone = hunewreservedphone;
	}
	@XmlElement(name = "humoditype")
	public String gethumoditype() {
		return humoditype;
	}
	public void sethumoditype(String humoditype) {
		this.humoditype = humoditype;
	}
	
}
