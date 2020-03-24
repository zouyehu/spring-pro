package com.zyh.hu.service.send.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.XmlUtil;

import com.zyh.hu.service.SysErrorLogSystemService;
import com.zyh.hu.service.responsexml.ResponseRest;
import com.zyh.hu.service.send.HTTPSend;
import com.zyh.hu.utils.FreemarkerUtil;
import com.zyh.hu.utils.JaxbUtils;
import com.zyh.hu.utils.StringsUtil;

@SuppressWarnings({ "rawtypes", "deprecation", "resource" })
@Component
public class HTTPSendImpl implements HTTPSend{

private static final Logger logger = LoggerFactory.getLogger(HTTPSendImpl.class);
	
	@Autowired
	public SysErrorLogSystemService sysErrorLogSystemService;
	
	// 请求地址
	@Value("${HU_POST_URL}")
	private String postUrl;

	/**
	 * POST请求指定-KEY
	 * @param url 地址
	 * @param reqMsg 请求报文XML字符串
	 * @return 返回响应报文XML字符串
	 * @throws Exception
	 */
	@Override
	public String sendHttpPostAndParams(String address, String reqMsg) throws Exception {
		 	long stime = System.currentTimeMillis();
			HttpClient httpclient = new DefaultHttpClient();
	        // 设置超时时间毫秒
	        HttpConnectionParams.setSoTimeout(httpclient.getParams(), 120000);
	        // 注意:必须以post方式发送请求
	        HttpPost httppost = new HttpPost(address);
	        logger.info("---检查请求地址:" + address);
	        // 设置请求参数
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        // XML请求报文
	        params.add(new BasicNameValuePair("requestMessage", reqMsg));
	        // 注意:编码必须是UTF-8
	        HttpEntity request = new UrlEncodedFormEntity(params, "UTF-8");
	        httppost.setEntity(request);
	        // 返回内容为XML,请使用XML解析工具对返回内容进行解析
	        HttpResponse httpResponse = httpclient.execute(httppost);
	        HttpEntity entity = httpResponse.getEntity();
	        String result = null;
	        if (entity != null) {
	            result = EntityUtils.toString(entity);
	        }
	        long etime = System.currentTimeMillis();
	        logger.info("---调用接口用时:" + (etime - stime));
	        return result;
		
	}
	/**
	 * POST请求-对象
	 * @param clazzReq 请求报文实体类对象
	 * @param clazzRes 响应报文实体类对象
	 * @param url 请求地址
	 * @return 返回响应报文实体类对象
	 */
	@Override
	public Object sendReqObj(Object clazzReq, Class clazzRes, Map saveMsg) {
		String responseMsg = "";
		Object response = null;
		//初始化转换XML的工具类
		JaxbUtils jb = new JaxbUtils(clazzReq.getClass());
        try {
        	//工具类将请求报文对象转换为XML字符串,参数是:请求报文对象
			String requestMsg = jb.toXml(clazzReq, "UTF-8");
			logger.info("---请求报文:" + requestMsg);
            //发送请求-参数是:请求地址和请求报文字符串,并返回响应报文字符串
            responseMsg = sendHttpPostAndParams(postUrl, requestMsg);
            logger.info("---响应报文:" + responseMsg);
            //初始化解析响应报文的工具类,参数是:响应报文类.class
            jb = new JaxbUtils(clazzRes);
            //将返回的报文XML字符串转换成响应报文实体类对象
            response = jb.fromXml(responseMsg);
            return response;
		} catch (Exception e) {
			logger.error("---查询接口异常:", e);
		} 
		return response;
	}
	
	/**
	 * POST请求
	 * @param type 类型 1 query 2 submit
	 * @param reqMsg 请求报文XML字符串
	 * @return 返回响应报文XML字符串
	 * @throws Exception
	 */
	@Override
	public String sendHttpPostRequest(String reqMsg,String type) throws Exception {
		 	long stime = System.currentTimeMillis();
			HttpClient httpclient = new DefaultHttpClient();
	        // 设置超时时间20s后续可以根据情况重设
	        HttpConnectionParams.setSoTimeout(httpclient.getParams(), 120000);
	        String address = "";
	        // 注意：必须以post方式发送请求
	        if ("1".equals(type)) {
	        	address = "查询";
			}else if ("2".equals(type)) {
				address = "提交";
			}else{
				return "请求类型有误!";
			}
	        HttpPost httppost = new HttpPost(address);
	        logger.info("---开始请求:" + address+";\n ---请求参数:" + reqMsg);
	        
			StringEntity myEntity = new StringEntity(reqMsg, "UTF-8");
			httppost.setEntity(myEntity);

	        HttpResponse httpResponse = httpclient.execute(httppost);
	        HttpEntity entity = httpResponse.getEntity();
	        String result = null;
	        if (entity != null) {
	            result = EntityUtils.toString(entity);
	        }
	        long etime = System.currentTimeMillis();
	        logger.info("---返回信息"+result+"---接口用时:" + (etime - stime));
	        return result;
		
	}

	/**
	 *  上传文件到指定 SFTP 服务器
	 * @param fileName
	 * @param base64Str
	 * @return resultCode  0代表上传成功，1代表上传失败
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> sendFileToSFTP(String fileName, String base64Str) throws Exception {
		logger.info("---开始调用上传文件到指定 SFTP 服务器 url:" + "请求地址" + "文件名-fileName:" + fileName);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> parmMap = new HashMap<String, Object>();
		Map<String, Object> fileMap = new HashMap<String, Object>();
		List<Map<String, Object>> imageFiles = new ArrayList<Map<String,Object>>();
		fileMap.put("fileName", fileName);
		fileMap.put("base64Str", base64Str);
		imageFiles.add(fileMap);
		parmMap.put("reqNo", System.nanoTime()+"");
		parmMap.put("imageFiles", imageFiles);
		String postMsg =  FreemarkerUtil.getString("/ftl/file.upload.ftl", parmMap);
		logger.info("---上传文件 SFTP 发送xml报文:" + postMsg);
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("请求地址");
		StringEntity myEntity = new StringEntity(postMsg, "UTF-8");
		httppost.setEntity(myEntity);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			result = EntityUtils.toString(entity);
		}
		logger.info("---调用上传文件到指定 SFTP 结束 返回:" + result);
		if (!StringsUtil.isBlank(result)) {
			try{
				returnMap = XmlUtil.xmlToMap(result);
			}catch (Exception e) {
				logger.error("---接口返回转化出错:", e);
				returnMap = null;
			}
		}else{
			returnMap = null;
		}
		return returnMap;
	}
	
	/**
	 * 从指定 SFTP 服务器下载文件
	 * @param fileName
	 * @return resultCode  0代表上传成功，1代表上传失败
	 * @throws Exception
	 */
	@Override
	public ResponseRest  getFileFromSFTP(String fileName) throws Exception {
		logger.info("---开始调用下载文件从 SFTP 服务器 url:" + "下载地址" + "fileName:" + fileName);
		ResponseRest responseItx = new ResponseRest();
		Map<String, Object> parmMap = new HashMap<String, Object>();
		Map<String, Object> fileMap = new HashMap<String, Object>();
		List<Map<String, Object>> imageFiles = new ArrayList<Map<String,Object>>();
		fileMap.put("fileName", fileName);				//下载文件名
		fileMap.put("base64Str", "/download/files/");	//文件目录地址
		imageFiles.add(fileMap);
		parmMap.put("reqNo", System.nanoTime()+"");
		parmMap.put("imageFiles", imageFiles);
		String postMsg =  FreemarkerUtil.getString("/ftl/file.upload.ftl", parmMap);
		logger.info("---下载文件从 SFTP 发送xml报文:" + postMsg);
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("下载地址");
		StringEntity myEntity = new StringEntity(postMsg, "UTF-8");
		httppost.setEntity(myEntity);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			result = EntityUtils.toString(entity);
		}
		logger.info("---调用下载文件从 SFTP 结束 返回:" + result);
		if (!StringsUtil.isBlank(result)) {
			try{
				JaxbUtils jax = new JaxbUtils(ResponseRest.class);
				responseItx = jax.fromXml(result);
			}catch (Exception e) {
				logger.error("---格式返回转化出错:", e);
				responseItx = null;
			}
		}else{
			responseItx = null;
		}
		
		return responseItx;
	}
	
}
