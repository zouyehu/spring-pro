package com.zyh.hu.service.responsexml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class ResponseRest {

	//resultCode  0代表上传成功，1代表上传失败
	private String resultCode; 
	private String errorMessage;
	private List<ImageFileRtx> imageFileList;
	
	@XmlElement(name = "resultCode")
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	@XmlElement(name = "errorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@XmlElementWrapper(name = "ImageFileList")
	@XmlElement(name = "ImageFile")
	public List<ImageFileRtx> getImageFileList() {
		return imageFileList;
	}
	public void setImageFileList(List<ImageFileRtx> imageFileList) {
		this.imageFileList = imageFileList;
	}
	
}
