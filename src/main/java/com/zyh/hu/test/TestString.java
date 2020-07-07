package com.zyh.hu.test;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

public class TestString {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final String strs = "12,13,14,19,20";
    private static final List<String> lists = new ArrayList<String>();
    static{
    	lists.add("12");
    	lists.add("13");
    	lists.add("14");
    	lists.add("19");
    	lists.add("20");
    }
    public static void main(String[] args){
//        ChildClass clazz = new ChildClass();
//        ChildClass.staticMethod();
//        clazz.method();

//        System.out.println(format.format(new Date()));
    	
//    	List<Object> list = new ArrayList<Object>();
//    	for(int i=1;i<=3;i++){
//    		Map<String,String> map = new HashMap<String, String>();
//        	map.put("name", "12" + "期");
//        	map.put("value", "12");
//        	list.add(map);
//    	}
//    	System.out.println(list);
//    	HashSet h = new HashSet(list);
//    	list.clear();
//    	list.addAll(h);
//    	System.out.println(list);
    	
//    	if (strs.contains("9")){
//    		System.out.println("包含");
//    	}else{
//    		System.out.println("不包含");
//    	}
    	String s = "0.00";
    	BigDecimal big = new BigDecimal(s);
String l = big.setScale(0,BigDecimal.ROUND_DOWN).toString();
    	System.out.println(l);
    	
    }
}

class ParentClass{
    public ParentClass(){
        System.out.println("A");
    }
    public static void staticMethod(){
        System.out.println("B");
    }
    public void method(){
        System.out.println("C");
    }
}

class ChildClass extends ParentClass{
    public ChildClass(){
        System.out.println("X");
    }
    public static void staticMethod(){
        System.out.println("Y");
    }
    public void method(){
        System.out.println("Z");
    }
}
