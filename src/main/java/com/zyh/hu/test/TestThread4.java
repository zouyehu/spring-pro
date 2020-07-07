package com.zyh.hu.test;


import java.util.concurrent.*;

import org.junit.Test;

import com.zyh.hu.domin.User;

/**
 * 主线程等待子线程返回信息
 */
public class TestThread4 {
    @Test
    public void testXC(){
        String result = testRun();
        System.out.println(result);
    }
    public String testRun(){
        final User vo = new User();
        ExecutorService  taskExecutor = Executors.newFixedThreadPool(2);
        Future f =taskExecutor .submit(new  Callable<User>(){
            public User call() throws Exception {
                System.out.print("start################");
                return vo;
            }
//            public UserVo testHello(UserVo vo){
//                System.out.print("hello##################");
//                UserVo userVo = new UserVo();
//                userVo.setAddress("nihao");
//                return userVo;
//            }
        });
        try {
            Object s = f.get();
            if(f.get().equals(vo)){
                return "123";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
