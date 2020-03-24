package com.zyh.hu.service.send;

import java.util.Map;

import com.zyh.hu.service.responsexml.ResponseRest;

public interface HTTPSend<T, P> {

	/**
	 * POST请求指定-KEY
	 * @param url 请求地址
	 * @param reqMsg 请求报文XML字符串
	 * @return 返回响应报文XML字符串
	 * @throws Exception
	 */
	public String sendHttpPostAndParams(String url,String reqMsg) throws Exception;
	
	/**
	 * POST请求-对象
	 * @param clazzReq 请求报文实体类对象
	 * @param clazzRes 响应报文实体类.class
	 * @param url 请求地址
	 * @return 返回响应报文实体类对象
	 */
	@SuppressWarnings({ "hiding", "rawtypes" })
	public <P> P sendReqObj(T clazzReq,Class<P> clazzRes,Map saveMsg);
	
	/** POST请求
	 * @param type 类型 1 query 2 submit
	 * @param reqMsg 请求报文XML字符串
	 * @return 返回响应报文XML字符串
	 * @throws Exception
	 */
	public String sendHttpPostRequest(String reqMsg,String type) throws Exception;
	
	/**
	 *  上传文件到指定 SFTP 服务器
	 * @param fileName-文件名
	 * @param base64Str-文件流
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> sendFileToSFTP(String fileName,String base64Str) throws Exception;
	
	/**
	 * 从指定 SFTP 服务器下载文件
	 * @param fileName-文件名
	 * @return
	 * @throws Exception
	 */
	public ResponseRest getFileFromSFTP(String fileName) throws Exception;
	
}
