package com.zyh.hu.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.zyh.hu.utils.HttpClientUtil;

import java.io.InputStream;
import java.security.KeyStore;

/**
 * 密钥加密通过代理访问
 * @author HU
 */
public class HttpPostJiami {
    private static final Logger logger = LoggerFactory.getLogger(HttpPostJiami.class);
    // 远见系统数字证书
    @SuppressWarnings("deprecation")
	public static void registerSSLSocketFactory(HttpClient httpclient, String htts_pwd, String htts_path) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream instream = HttpClientUtil.class.getResourceAsStream(htts_path);
        try {
            trustStore.load(instream, htts_pwd.toCharArray());
        } finally {
            instream.close();
        }
        SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
        socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
        Scheme sch = new Scheme("https", socketFactory, 443);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
    }

    // 上传PDF文件到远见系统
    @SuppressWarnings("deprecation")
	public static String sendPostPDFToYJ(String url,  InputStream fis, String policy, String https_pwd, String host, String port, String https_path) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        // 导入数字证书并注册SSLSocketFactory
        registerSSLSocketFactory(httpclient, https_pwd, https_path);
        // 注意：必须以post方式发送请求
        HttpPost httppost = new HttpPost(url);
        // 设置代理访问IP和端口
        HttpHost proxy = new HttpHost(host, Integer.valueOf(port), "http");

        logger.info("---上传远件电子保单保单号：{}", policy);
        MultipartFile multi = new MockMultipartFile(policy, fis);
        logger.info("---上传远见保单名称：{}", multi.getName());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("policy", multi.getInputStream(),ContentType.MULTIPART_FORM_DATA,policy);
        HttpEntity entity = builder.build();
        httppost.setEntity(entity);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        // 返回内容为xml，请使用xml解析工具对返回内容进行解析
        HttpResponse httpResponse = httpclient.execute(httppost);
        HttpEntity responseEntity = httpResponse.getEntity();
        String result = null;
        if (responseEntity != null) {
            result = EntityUtils.toString(responseEntity);
            logger.info("---上传文件到远见返回：{}", result);
        }
        return result;
    }
}
