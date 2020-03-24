package com.zyh.hu.service.responsexml;

import javax.xml.bind.annotation.XmlElement;

public class SHBodyResopnse {

	private String husubacctno;			//子账号
	private String huworkingbal;			//余额
	private String hucustname; 			//姓名
	private String hubindcardno; 			//绑定银行卡号
	private String hureservedphone; 		//银行卡预留手机号
	private String hunewcardno; 			//新绑定银行卡号
	private String hunewreservedphone; 	//新银行卡预留手机号
	
	@XmlElement(name = "husubacctno")
	public String gethusubacctno() {
		return husubacctno;
	}
	public void sethusubacctno(String husubacctno) {
		this.husubacctno = husubacctno;
	}
	@XmlElement(name = "huworkingbal")
	public String gethuworkingbal() {
		return huworkingbal;
	}
	public void sethuworkingbal(String huworkingbal) {
		this.huworkingbal = huworkingbal;
	}
	@XmlElement(name = "hucustname")
	public String gethucustname() {
		return hucustname;
	}
	public void sethucustname(String hucustname) {
		this.hucustname = hucustname;
	}
	@XmlElement(name = "hubindcardno")
	public String gethubindcardno() {
		return hubindcardno;
	}
	public void sethubindcardno(String hubindcardno) {
		this.hubindcardno = hubindcardno;
	}
	@XmlElement(name = "hureservedphone")
	public String gethureservedphone() {
		return hureservedphone;
	}
	public void sethureservedphone(String hureservedphone) {
		this.hureservedphone = hureservedphone;
	}
	@XmlElement(name = "hunewcardno")
	public String gethunewcardno() {
		return hunewcardno;
	}
	public void sethunewcardno(String hunewcardno) {
		this.hunewcardno = hunewcardno;
	}
	@XmlElement(name = "hunewreservedphone")
	public String gethunewreservedphone() {
		return hunewreservedphone;
	}
	public void sethunewreservedphone(String hunewreservedphone) {
		this.hunewreservedphone = hunewreservedphone;
	}
	
}
