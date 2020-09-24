package com.zyh.hu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCodesUtil {

	public static final String SUCCESS_CODE = "SUCCESS";
	
	//产品集合
	public static final List<String> PRODUCE_CODE_LIST = new ArrayList<String>();
	static {
		PRODUCE_CODE_LIST.add("701787");
		PRODUCE_CODE_LIST.add("701788");
		PRODUCE_CODE_LIST.add("701789");
	}
	//返回异常封装
	public static final Map<String, String> RESPONSE_MESSAGE_MAP = new HashMap<String, String>();
	static {
		RESPONSE_MESSAGE_MAP.put("0010", "您当前无法使用此功能");
		RESPONSE_MESSAGE_MAP.put("0011", "该功能暂未开放,请稍后");
	}
}
