package com.zyh.hu.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyh.hu.domin.Student;
import com.zyh.hu.domin.User;

/**
 * FTL模板工具类
 *
 * @author HU
 */
public class FtlUtil {

    public static boolean createFile(String templatePath, String resultFilePath, Object... obj) {
        boolean flag = false;
        try {
            Map<String, Object> map = BeanToMap(obj[0]);
            for (int i = 1; i < obj.length; i++) {
                map = BeanToMap(obj[i], map);
            }
            flag = createFile(templatePath, resultFilePath, map);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 根据FTL模板生成需要的文件
     *
     * @param templatePath   模板路径
     * @param resultFilePath 生成文件路径
     * @param map            需要放入map的bean
     * @return
     */
    public static boolean createFile(String templatePath, String resultFilePath, Map<String, Object> map) {
        boolean flag = false;
        try {
            String xmlStr = FreemarkerUtil.getString(templatePath, map);
            File xmlFile = new File(resultFilePath);
            if (xmlFile.exists()) {
                xmlFile.delete();
            }
            //检查当前文件夹是否存在，否：创建文件夹
            if (!xmlFile.getParentFile().exists()) {
                xmlFile.getParentFile().mkdirs();
            }
            //创建文件
            flag = xmlFile.createNewFile();
            if (flag) {
                FileOutputStream fos = null;
                OutputStreamWriter outputStreamWriter = null;
                try {
                    fos = new FileOutputStream(xmlFile);
                    outputStreamWriter = new OutputStreamWriter(fos);
                    outputStreamWriter.append(xmlStr);
                    outputStreamWriter.flush();
                } catch (IOException e) {
                    flag = false;
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    //把bean转化为map
    public static Map<String, Object> BeanToMap(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        return BeanToMap(obj, map);
    }

    //把bean加入已有的map
    public static Map<String, Object> BeanToMap(Object obj, Map<String, Object> map) throws Exception {
        Class<? extends Object> clz = obj.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            Method rm = clz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            Object value = rm.invoke(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public static void main(String[] args) {
         String templatePath = "/ftl/xml.ftl";
         String resultFilePath = "D:\\test\\spring1\\testxml.xml";
        User userData = new User();
        userData.setAddr("上海");
        userData.setAge("26");
        userData.setUserName("huge");
        userData.setPassWord("123123");
        userData.setRealName("虎");
        List<Student> list = new ArrayList<Student>();
        Student s = new Student();
        s.setId(1);
        s.setName("111");
        s.setGender("男");
        s.setAddress("光速");
        Student s1 = new Student();
        s1.setId(2);
        s1.setName("222");
        s1.setGender("女");
        s1.setAddress("还好");
        list.add(s);
        list.add(s1);
        userData.setStudents(list);
        boolean createXML = FtlUtil.createFile(templatePath,resultFilePath,userData);
        System.out.println(createXML);
    }
}
