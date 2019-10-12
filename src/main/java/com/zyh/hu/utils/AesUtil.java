package com.zyh.hu.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class AesUtil {

    //密钥 (需要前端和后端保持一致,此处使用AES-128-CBC加密模式,key需要为16位)
    private static String KEY = "CKAPP_";
    //算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    static {
         KEY = KEY.concat(new SimpleDateFormat("yyyyMMddHH").format(new Date()));
    }
    
    /**
     * aes解密
     * @param encrypt   内容
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String encrypt) {
        try {
            return aesDecrypt(encrypt, KEY);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * aes加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String content) {
        try {
            return aesEncrypt(content, KEY);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
	public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * AES加密 
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null || sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
    
    /**
     * AES解密
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null || sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

	/**
	 * base64字符串转byte[]
	 * @param base64Str
	 * @return
	 */
	public static byte[] base64String2ByteFun(String base64Str) {
		return Base64.decodeBase64(base64Str);
	}

	/**
	 * byte[]转base64字符串
	 * @param b
	 * @return
	 */
	public static String byte2Base64StringFun(byte[] b) {
		return Base64.encodeBase64String(b);
	}

	/**
	 * String转Base64
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    public static String strToBase64(String str)throws UnsupportedEncodingException {
        if (null != str){
            Base64 base = new Base64();
            return base.encodeToString(str.getBytes("UTF-8"));
        }
        return null;
    }
    
    /**
     * Base64转String
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64ToStr(String str)throws UnsupportedEncodingException {
        if (null != str){
            Base64 base = new Base64();
            return new String(base.decode(str.getBytes()), "UTF-8");
        }
        return null;
    }
    
    /**
     * 测试
     */
    public static void main(String[] args) throws Exception {
//        String content = "admin";
//        System.out.println("加密前：" + content);
//        System.out.println("加密密钥和解密密钥：" + KEY);
//        String encrypt = aesEncrypt(content, KEY); 
//        System.out.println("加密后：" + encrypt);
//        String decrypt = aesDecrypt(encrypt, KEY);
//        System.out.println("解密后：" + decrypt);
    	
    	/*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        // 需要加密的字串
        String cSrc = "我命由我不由天";
        // 加密
        String enString = AesUtil.Encrypt(cSrc, KEY);
        System.out.println("加密后的字串是:" + enString);
    	// 解密
//        String DeString = AesUtils.Decrypt(enString, KEY);
//        System.out.println("解密后的字串是：" + DeString);
        String sql = "UPDATE sys_user_info u SET u.ADDRESS='河南信阳天上人间' WHERE u.LOGIN_NAME='虎哥'";
        String base = AesUtil.strToBase64(sql);
        System.out.println("加密后的SQL:" + base);
        String result = "{\"appToken\":\"" + enString + "\",\"requestData\":\""+ base + "\",\"bussinessType\":\"CKSELECT\"}";
        System.out.println("结果如下:" + result);

    }
}
