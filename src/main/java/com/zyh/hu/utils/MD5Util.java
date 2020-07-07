package com.zyh.hu.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密/解密
 * @author zyh
 *
 */
public class MD5Util {
	
    /**
     * MD5 16位加密
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5StrTo16(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] inputBytes = s.getBytes("UTF-8");
        MessageDigest mdInst = MessageDigest.getInstance("md5");
        mdInst.update(inputBytes);
        byte[] md = mdInst.digest();
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str).substring(8, 24);
    }
	
	/**
     * 生成32位md5加密
     * @param password
     * @return
     */
    public static String md5StrTo32(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
//            return buffer.toString();//小写
            return buffer.toString().toUpperCase();//大写
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) throws Exception{
        String str="123";
        System.out.println(md5StrTo16(str));
//        System.out.println(md5StrTo32(str));
    }
}
