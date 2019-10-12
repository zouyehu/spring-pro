package com.zyh.hu.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 主键,流水号工具
 * @author zyh
 *
 */
public class KeyUtil {


	/**
	 * 根据UUID生成主键(32位)
	 * @return
	 */
	public static String getKey(){
		
		UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
	}
	
	/**
	 * 根据关键字key(位数)+系统时间戳(13)+随机数(3位)
	 * @param key
	 * @return
	 */
	public static String getKey(String key){
		return key + System.currentTimeMillis() + new Random().nextInt(1000);
	}
	
	public static void main(String[] args) {
		System.out.println("UUID:" + KeyUtil.getKey());
		System.out.println("时间戳:" + KeyUtil.getKey("h"));
	}
}
