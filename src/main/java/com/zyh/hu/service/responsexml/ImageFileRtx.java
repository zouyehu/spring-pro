package com.zyh.hu.service.responsexml;

import javax.xml.bind.annotation.XmlElement;

public class ImageFileRtx {

	private String fileName;
	private String fileMessage;
	
	@XmlElement(name = "FileName")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@XmlElement(name = "FileMessage")
	public String getFileMessage() {
		return fileMessage;
	}
	public void setFileMessage(String fileMessage) {
		this.fileMessage = fileMessage;
	}
	
}
