package com.zyh.hu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用apache httpclient 4.3以上的版本jar 提供httpclient连接工具类
 * @author HU
 * 
 */
@SuppressWarnings("deprecation")
public class HttpClientUtil {

	private static final String APPLICATION_JSON = "application/json";
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	static {
		cm.setMaxTotal(800);
		cm.setDefaultMaxPerRoute(800);
	}

	/**
	 * 通过get方式获取指定地址的内容
	 * 
	 * @param url
	 *            需要访问的地址如：http://www.baidu.com
	 * @param chartset
	 *            字符编码，将地址返回的内容进行字符编码，如果为空则默认为：UTF-8
	 * @return 地址对应的内容
	 */
	public static String get(String url, int socketTime, int connectTimeout, String chartset)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
		RequestConfig requetConfig = RequestConfig.custom().setProxy(null).setSocketTimeout(socketTime)
				.setConnectTimeout(connectTimeout).build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requetConfig);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
			HttpEntity entity1 = response1.getEntity();
			if (chartset == null || "".equals(chartset)) {
				chartset = "UTF-8";
			}
			String responseBody = EntityUtils.toString(entity1, chartset);
			EntityUtils.consume(entity1);
			StatusLine statusLine = response1.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				byte[] contextByte = Base64.encodeBase64(responseBody.getBytes(), true);
				String responseBodyLog = new String(contextByte);
				logger.error("current request url error,satusCode:{},responseBody:{}", statusCode, responseBodyLog);
				throw new IOException("request url statusCode is 500!");
			}
			return responseBody;
		} finally {
			if (response1 != null) {
				response1.close();
			}
		}
	}

	/**
	 * 使用post方式提交参数
	 * 
	 * @param url
	 * @param params
	 *            提交的参数已key,value的形式保存在map当中
	 * @param socketTime
	 * @param connectTimeout
	 * @param chartset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> params, int socketTime, int connectTimeout,
			String chartset) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
		RequestConfig requetConfig = RequestConfig.custom().setProxy(null).setSocketTimeout(socketTime)
				.setConnectTimeout(connectTimeout).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requetConfig);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		// httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, chartset));
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpPost);
			HttpEntity entity1 = response1.getEntity();
			if (chartset == null || "".equals(chartset)) {
				chartset = "UTF-8";
			}
			String responseBody = EntityUtils.toString(entity1, chartset);
			EntityUtils.consume(entity1);
			StatusLine statusLine = response1.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				byte[] contextByte = Base64.encodeBase64(responseBody.getBytes(), true);
				String responseBodyLog = new String(contextByte);
				logger.error("current request url error,satusCode:{},responseBody:{}", statusCode, responseBodyLog);
				throw new IOException("request url statusCode is 500!");
			}
			return responseBody;
		} finally {
			if (response1 != null) {
				response1.close();
			}
		}
	}

	/**
	 * 凹凸租车卡券请求封装
	 * 
	 * @param url
	 * @param socketTime
	 * @param connectTimeout
	 * @param chartset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postAutoCar(String url, String postData, int socketTime, int connectTimeout, String chartset,
			String proxyIp, String proxyPort) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
		HttpHost proxy = new HttpHost(proxyIp, Integer.parseInt(proxyPort), "http");
		RequestConfig requetConfig = RequestConfig.custom().setProxy(proxy).setSocketTimeout(socketTime)
				.setConnectTimeout(connectTimeout).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requetConfig);

		// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// for (Map.Entry<String, String> entry : params.entrySet()) {
		// nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		// }
		// httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");
		httpPost.addHeader("User-Agent", "Autoyol");
		StringEntity entity = new StringEntity(postData, chartset);
		httpPost.setEntity(entity);
		// httpPost.setEntity(new UrlEncodedFormEntity(nvps, chartset));
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpPost);
			HttpEntity entity1 = response1.getEntity();
			if (chartset == null || "".equals(chartset)) {
				chartset = "UTF-8";
			}
			String responseBody = EntityUtils.toString(entity1, chartset);
			EntityUtils.consume(entity1);
			StatusLine statusLine = response1.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				byte[] contextByte = Base64.encodeBase64(responseBody.getBytes(), true);
				String responseBodyLog = new String(contextByte);
				logger.error("current request url error,satusCode:{},responseBody:{}", statusCode, responseBodyLog);
				throw new IOException("request url statusCode is 500!");
			}
			return responseBody;
		} finally {
			if (response1 != null) {
				response1.close();
			}
		}
	}

	/**
	 * 使用xml格式提交请求
	 * 
	 * @param url
	 *            请求路径
	 * @param content
	 *            xml报文
	 * @param socketTime
	 *            连接时间（单位毫秒）
	 * @param connectTimeout
	 *            连接等待时间（单位毫秒）
	 * @param reqChartset
	 *            请求报文字符编码，默认为UTF-8
	 * @param respChartset
	 *            返回报文字符编码，默认为UTF-8
	 * @param contentType
	 *            http内容类型
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postXml(String url, String content, int socketTime, int connectTimeout, String reqChartset,
			String respChartset, String contentType) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
		RequestConfig requetConfig = RequestConfig.custom().setProxy(null).setSocketTimeout(socketTime)
				.setConnectTimeout(connectTimeout).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requetConfig);
		if (reqChartset == null || "".equals(reqChartset)) {
			reqChartset = "UTF-8";
		}
		StringEntity myEntity = new StringEntity(content, reqChartset);
		httpPost.addHeader("Content-Type", contentType);
		httpPost.setEntity(myEntity);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpPost);
			HttpEntity entity1 = response1.getEntity();
			if (respChartset == null || "".equals(respChartset)) {
				respChartset = "UTF-8";
			}
			StatusLine statusLine = response1.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				logger.error("current request url error,satusCode:{},ReasonPhrase:{}", statusCode,
						statusLine.getReasonPhrase());
				throw new IOException("request url statusCode is 500!");
			}
			String responseBody = EntityUtils.toString(entity1, respChartset);
			EntityUtils.consume(entity1);
			return responseBody;
		} finally {
			if (response1 != null)
				response1.close();
		}
	}

	public static String postForAdapterAdmin(String url, Map<String, String> params, Map<String, String> headParams,
			int socketTime, int connectTimeout, String chartset) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
		RequestConfig requetConfig = RequestConfig.custom().setProxy(null).setSocketTimeout(socketTime)
				.setConnectTimeout(connectTimeout).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requetConfig);
		if (headParams != null) {
			for (String key : headParams.keySet()) {
				httpPost.addHeader(key, headParams.get(key));
			}
		}
		if (params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, chartset));
		}

		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpPost);
			HttpEntity entity1 = response1.getEntity();
			if (chartset == null || "".equals(chartset)) {
				chartset = "UTF-8";
			}
			String responseBody = EntityUtils.toString(entity1, chartset);
			EntityUtils.consume(entity1);
			StatusLine statusLine = response1.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				byte[] contextByte = Base64.encodeBase64(responseBody.getBytes(), true);
				String responseBodyLog = new String(contextByte);
				logger.error("current request url error,satusCode:{},responseBody:{}", statusCode, responseBodyLog);
				throw new IOException("request url statusCode is 500!");
			}
			return responseBody;
		} finally {
			if (response1 != null) {
				response1.close();
			}
		}
	}

	public static String sendPostString(String param, String urlString) {
		String returnMessage = "";
		HttpPost httpPost = new HttpPost(urlString);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			StringEntity strEntity = new StringEntity(param, "UTF-8");
			strEntity.setContentEncoding("UTF-8");
			strEntity.setContentType("application/json");
			httpPost.setEntity(strEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					returnMessage = EntityUtils.toString(entity, "utf-8");
					logger.warn("model response data" + returnMessage);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			logger.warn("exception:" + e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (Exception e2) {
				logger.warn("exception:" + e2.getMessage());
			}
		}
		return returnMessage;

	}

	
	public static String sendMessageToValidIdCard(String url,JSONObject json) {
		
		String returnMessage = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		StringEntity strEntity = new StringEntity(json.toString(), "utf-8");
		strEntity.setContentEncoding("UTF-8");
		strEntity.setContentType("application/json");
		httpPost.setEntity(strEntity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);

			// 输出调用结果
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				returnMessage = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}finally {
			try {
				httpClient.close();
			} catch (Exception e2) {
				logger.warn("exception:" + e2.getMessage());
			}
		}
		
		return returnMessage;
	}
	
	public static String postFile(String url, String policy, File file) {
		logger.info("---上传电子保单保单号：{}", policy);
		 try {
			FileInputStream fis = new FileInputStream(file);
			logger.info("---上传文件初始名称:{}", file.getName());
			MultipartFile multi = new MockMultipartFile(policy, fis);
			logger.info("---上传保单名称：{}", multi.getName());
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		    builder.addBinaryBody("policy", multi.getInputStream(),ContentType.MULTIPART_FORM_DATA,policy);
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			 HttpResponse response = httpClient.execute(httpPost);// 执行提交
			    HttpEntity responseEntity = response.getEntity();
			    if (responseEntity != null) {
			        // 将响应内容转换为字符串
			        String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
			       logger.info("---文件上传返回：{}", result);
			       return result;
			    }else {
			    	logger.info("---文件上传返回为空");
			    	return "";
			    }
		} catch (FileNotFoundException e) {
			logger.error("---文件上传异常:", e);
			return "";
		} catch (IOException e) {
			logger.error("---文件上传异常:", e);
			return "";
		}
	}
	
	public static void registerSSLSocketFactory(HttpClient httpclient, String htts_pwd, String htts_path) throws Exception {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		// 注意：请将数字证书的路径（E:\\cer\\hu_jttp.keystore）换成您保存本文件的具体路径
		//FileInputStream instream = new FileInputStream(new File("D:\\projects\\yjxt\\hu\\src\\hu\\hu_jttp.keystore"));
		//String s= SignUtils.class.getResource("/").getPath();
		InputStream instream = HttpClientUtil.class.getResourceAsStream(htts_path);
		//FileInputStream instream = new FileInputStream(new File("E:\\test.insight88financial.com.keystore"));
		try {
			trustStore.load(instream, htts_pwd.toCharArray());
			 //KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509"); 
			// kmf.init(trustStore,"yj@p2p".toCharArray());
		} finally {
			instream.close();
		}
		SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
		socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier()); 
		Scheme sch = new Scheme("https", socketFactory, 443);
		httpclient.getConnectionManager().getSchemeRegistry().register(sch);
	}
	
	public static String sentHttpPostRequest(String url,  File file, String policy, String https_pwd, String host, String port, String https_path) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();

		// 导入数字证书并注册SSLSocketFactory
		registerSSLSocketFactory(httpclient, https_pwd, https_path);

		// 设置超时时间
		/*int timeout = 60000;
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), timeout);*/

		// 注意：必须以post方式发送请求
		HttpPost httppost = new HttpPost(url);
		HttpHost proxy = new HttpHost(host, Integer.valueOf(port), "http");

		// inputstream方式
		// 注意：编码必须是UTF-8
		/*ContentProducer cp = new ContentProducer() {
			String requestMessage2 = requestMsg;

			public void writeTo(OutputStream outputStream) throws IOException {
				Writer writer = new OutputStreamWriter(outputStream, "utf-8");
				writer.write(requestMessage2);
				writer.flush();
			}
		};*/
		logger.info("---上传电子保单保单号：{}", policy);
		FileInputStream fis = new FileInputStream(file);
		logger.info("---上传文件初始名称:{}", file.getName());
		MultipartFile multi = new MockMultipartFile(policy, fis);
		logger.info("---上传保单名称：{}", multi.getName());
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		//HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	    builder.addBinaryBody("policy", multi.getInputStream(),ContentType.MULTIPART_FORM_DATA,policy);
		HttpEntity entity = builder.build();
	//	HttpEntity request = new EntityTemplate(cp);
		httppost.setEntity(entity);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		// 返回内容为xml，请使用xml解析工具对返回内容进行解析
		HttpResponse httpResponse = httpclient.execute(httppost);
		HttpEntity responseEntity = httpResponse.getEntity();
		String result = null;
		if (responseEntity != null) {
			result = EntityUtils.toString(responseEntity);
			logger.info("---上传文件到返回：{}", result);
		}

		return result;
	}
	
}
