package com.zyh.hu.utils;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

public class UUIDUtil {

	/**
	 * 生成32位UUID基于时间
	 * @return
	 */
	public static String getuuidStrto32(){
		UUIDGenerator generator = UUIDGenerator.getInstance();
		UUID uuid = generator.generateTimeBasedUUID();
		return uuid.toString().replaceAll("-", "");
		
	}
	
	/**
	 * 生成13位系统时间
	 * @return
	 */
	public static String getsysTimeto13(){
		return String.valueOf(System.currentTimeMillis());
		
	}
	public static void main(String[] args) {
		String str = UUIDUtil.getuuidStrto32();
		System.out.println(str);
		
		String str1 = UUIDUtil.getsysTimeto13();
		System.out.println(str1);
	}
}
