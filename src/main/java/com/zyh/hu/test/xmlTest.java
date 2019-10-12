package com.zyh.hu.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.zyh.hu.domin.Student;
import com.zyh.hu.domin.User;
import com.zyh.hu.utils.FtlUtil;

/**
 *XML工具类
 *@author zyh
 */
public class xmlTest {
private static final String templatePath = "/ftl/xml.ftl";
private static final String resultFilePath = "D:\\download\\testxml.xml";


    //测试根据ftl模板生成对应的xml文件
    @Test
    public void xmltest(){
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

    //解析复杂xml
    @SuppressWarnings({ "rawtypes", "unused" })
	@Test
    public void xmljx(){
        String resultStr = null;
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<root>\n" +
                "<result>\n" +
                "0\n" +
                "</result>\n" +
                "<msg>\n" +
                "返回统计成功!\n" +
                "</msg>\n" +
                "<date>\n" +
                "2018-07-25 14:38:23\n" +
                "</date>\n" +
                "<page>\n" +
                "<pageid  pathid = \"0\"  url = \"http://127.0.0.0:8080/hupro/servlet/getUrls?ZGF0ZT0yMDE4MDcyNSZmaWxlX25hbWU9L3RybWRhdGEvVVdfR1JYWS8yMDE4LzA3LzE5LzIwLzM2LzE1OTM4N2NhZDg5NjNhYTM4YjE2MjU2ZDZlNGM4OTNjXzUvNjY3N2NjYjItYWI2ZC00NTg3LWEzMDctYzM4NTcyY2FjMTNjLnBkZiZvcmlnaW5hbE5hbWU9MjAxODA3MDMwMDAwMDAyMy01MTI4LnBkZg==\" thum_url = \"null\" original_name = \"2012070300000023-5128.pdf\" />\n" +
                "</page>\n" +
                "<path>\n" +
                "<node pathid = \"0\" pathtext=\"个人资料/个人信用资料/保单资料/\" pathtextid=\"HUGEGE/POLICY/H0011/\"  size=\"1\" />\n" +
                "</path>\n" +
                "</root>";
        try {
            Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            String result = rootElt.elementTextTrim("result"); // 拿到result节点值
            if ("0".equals(result)){
                String url = "";
                String originalName = "";
                Element recordEle = rootElt.element("page");
                Iterator iter = recordEle.elementIterator("pageid"); // 遍历节点
                while(iter.hasNext()){
                    Element pageEl = (Element) iter.next();
                    String retCode = pageEl.elementTextTrim("节点名称"); // 拿到pageid节点下的子节点title值
                    Attribute att = pageEl.attribute("url");// 拿到pageid节点里的属性名称值
                    url = att.getValue();
                    Attribute original_name = pageEl.attribute("original_name");
                    originalName = original_name.getValue();
                }
                /*
                 * 影像支持同一目录下可以存放多个同名的PDF文件
                 * 返回的URL下载链接是按照上传时间正序排列的
                 * 故遍历之后返回最后一个
                 */
                String lastPdf = originalName.substring(originalName.lastIndexOf("."));
                if ((".pdf").equalsIgnoreCase(lastPdf)){
                    resultStr = url;
                }else{
                    System.out.println("返回为空！");
                }
            }
            System.out.println("返回下载URL：" + resultStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
