package com.zyh.hu.comments.exception;

import com.zyh.hu.comments.ResponseStatusEnum;

/**
 * 封装系统 业务异常
 * 
 * @author HU
 */
public class SysException extends RuntimeException {
	private static final long serialVersionUID = 285482460569456267L;
	private String code;

	public SysException() {
		super();
	}
	
	public SysException(ResponseStatusEnum res) {
		super(res.resultMsg + "[" + res.resultCode + "]");
		this.code = res.resultCode;
	}
	
	public SysException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}