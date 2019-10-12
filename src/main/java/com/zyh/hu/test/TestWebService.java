package com.zyh.hu.test;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * 测试调度webservice接口
 * 客户端
 */
public class TestWebService {
    public String invokeRemoteFuc() {
        String endpoint = "http://localhost:3080/webservice/internal/syncVehicle/importVehicleInfo";
        String result = "no result!";
        Service service = new Service();
        Call call;
        Object[] object = new Object[2];
        object[0] = "123";//Object是用来存储方法的参数
        object[1] = "333";
        try {
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint);// 远程调用路径
            call.setOperationName("importVehicleInfo");// 调用的方法名

            // 设置参数名:
            call.addParameter("appkey", // 参数名
                    XMLType.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("sign", // 参数名
                    XMLType.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'

            // 设置返回值类型：
            call.setReturnType(XMLType.XSD_STRING);// 返回值类型：String

            result = (String) call.invoke(object);// 远程调用
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        TestWebService t = new TestWebService();
        String result = t.invokeRemoteFuc();
        System.out.println(result);
    }
}
