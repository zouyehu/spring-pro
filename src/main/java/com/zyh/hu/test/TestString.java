package com.zyh.hu.test;


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
    public static void main(String[] args){
//        ChildClass clazz = new ChildClass();
//        ChildClass.staticMethod();
//        clazz.method();

//        System.out.println(format.format(new Date()));

//        String json = "{\n" +
//                "\t\"applicationId\": \"2018071800000021\",\n" +
//                "\t\"encData\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><BJCAROOT><Version>1<\\/Version><SignTID>1113<\\/SignTID><EncKey>QvKKVGOwqCJ/sCAqhUPfEspfm+Yn3kQYS2MW2yRfbQSw3x3FpEfSzdtgu9L+SX4Xiq9E0dCzeXxbHqMpK0Ks/I5ZcFyh+f6nw2ITK4CHk+ZzKKJZ4nJqcESHXflTRkgDgMrBICk2DPohC1rKrq1gnCUUC2rQLXNiIYuLxNRpv9s=<\\/EncKey><Cert><EncCertSN>1021020000067221ed<\\/EncCertSN><EncCertKeyID>N/A<\\/EncCertKeyID><EncAlg>1<\\/EncAlg><\\/Cert><EncData>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<\\/EncData><DeviceInfo><DeviceMF>ipad<\\/DeviceMF><DeviceOS/><DeviceSN/><\\/DeviceInfo><SignCertSN>NA<\\/SignCertSN><SignValue>NULL<\\/SignValue><\\/BJCAROOT>\",\n" +
//                "\t\"templetId\": \"5132\",\n" +
//                "\t\"fileId\": \"6\",\n" +
//                "\t\"userName\": \"娄彦胜\",\n" +
//                "\t\"currentDateHMS\": \"2018-7-18 16:9:function getSeconds() {\\n    [native code]\\n}\",\n" +
//                "\t\"signData\": {\n" +
//                "\t\t\"unitCode\": \"3110100\",\n" +
//                "\t\t\"ContractStatus\": \"待签约\",\n" +
//                "\t\t\"touBaoDanId\": \"ASUZ511Z2518P000035A\",\n" +
//                "\t\t\"applicationTime\": \"36\",\n" +
//                "\t\t\"approveAmount\": \"197000\",\n" +
//                "\t\t\"preRateMonth\": \"0.72%\",\n" +
//                "\t\t\"bankContactId\": \"ASUZ511Z2518Q000021C\",\n" +
//                "\t\t\"loanContractId\": \"ASUZ511Z2518Q000021C\",\n" +
//                "\t\t\"insurancePolicyId\": \"ASUZ511Z2518Q000021C\",\n" +
//                "\t\t\"insurantInfo\": [{\n" +
//                "\t\t\t\"insurantName\": \"上海银行上海自贸试验区分行TEST\",\n" +
//                "\t\t\t\"insurantPostcode\": \"444444\",\n" +
//                "\t\t\t\"insurantAddress\": \"上海市浦东新区银城中路488号TEST\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"insureInfo\": [{\n" +
//                "\t\t\t\"insureName\": \"娄彦胜\",\n" +
//                "\t\t\t\"idType\": \"身份证\",\n" +
//                "\t\t\t\"idNumber\": \"440305198601012055\",\n" +
//                "\t\t\t\"insureTel\": \"13722632489\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"insuranceAmount\": [{\n" +
//                "\t\t\t\"insuAmountLower\": \"234,233.00\",\n" +
//                "\t\t\t\"insuAmountUpper\": \"贰拾叁万肆仟贰佰叁拾叁元整\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"premium\": [{\n" +
//                "\t\t\t\"premiumRate\": \"0.72\",\n" +
//                "\t\t\t\"premiumUpper\": \"壹仟肆佰壹拾捌元肆角整\",\n" +
//                "\t\t\t\"premiumLower\": \"1,418.40\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"insureSign\": \"\",\n" +
//                "\t\t\"signCompanyInfo\": [{\n" +
//                "\t\t\t\"signCompanyName\": \"苏州分公司\",\n" +
//                "\t\t\t\"signCompanyAddress\": \"吴江区松陵镇云梨路2008号\",\n" +
//                "\t\t\t\"signCompanyPostcode\": \"\",\n" +
//                "\t\t\t\"signCompanyTel\": \"\",\n" +
//                "\t\t\t\"signCompanyFac\": \"\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"insureChecker\": \"\",\n" +
//                "\t\t\"policyCreater\": \"\",\n" +
//                "\t\t\"handler\": \"\",\n" +
//                "\t\t\"signPolicyDate\": \"2018年07月18日\",\n" +
//                "\t\t\"loanContractDate\": [{\n" +
//                "\t\t\t\"contractYear\": \"2018\",\n" +
//                "\t\t\t\"contractMonth\": \"07\",\n" +
//                "\t\t\t\"contractDate\": \"18\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"policyId\": \"ASUZ511Z2518Q000021C\",\n" +
//                "\t\t\"BankAccount\": \"623185009800034032\",\n" +
//                "\t\t\"dateOne\": \"2018-07-18\"\n" +
//                "\t},\n" +
//                "\t\"userId\": \"13289\"\n" +
//                "}";
//        JSONObject jsonO = JSONObject.parseObject(json);
//        String signData = String.valueOf(jsonO.get("signData"));
//        JSONObject jsonO2 = JSONObject.parseObject(signData);
//        String unCode = String.valueOf(jsonO2.get("unitCode"));
//        System.out.println(unCode);
    	
    	List<Object> list = new ArrayList<Object>();
    	for(int i=1;i<=3;i++){
    		Map<String,String> map = new HashMap<String, String>();
        	map.put("name", "12" + "期");
        	map.put("value", "12");
        	list.add(map);
    	}
    	System.out.println(list);
    	HashSet h = new HashSet(list);
    	list.clear();
    	list.addAll(h);
    	System.out.println(list);
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
