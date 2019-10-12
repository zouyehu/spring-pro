package com.zyh.hu.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.io.File;

/**
 * 接口轮循请求,尝试几次每次隔几秒请求,最后如何处理
 * @author HU
 */
public class RoundUtil {
    private static final Logger logger = LoggerFactory.getLogger(RoundUtil.class);

    public static String ckeckFiles(String filePath){

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(4);                       //请求次数
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy ();
        backOffPolicy.setInitialInterval(1000L);        //间隔时间ms
        backOffPolicy.setMultiplier(2.5D);              //下次执行时间倍数
        template.setBackOffPolicy(backOffPolicy);
        template.setRetryPolicy(policy);
        String result = null;
        try {
            final String filePathFinal = filePath;
            result = template.execute(new RetryCallback<String, Exception>() {

                File pdf = new File(filePathFinal);
                public String doWithRetry(RetryContext context) throws Exception {
                    // 业务逻辑处理
                    if (pdf.exists()){
                        return "1";
                    }
                    throw new NullPointerException("nullPointerException");
                }
            }, new RecoveryCallback<String>() {
                public String recover(RetryContext context) throws Exception {
                    // 最后处理

                    return "0";
                }
            });
        } catch (Exception e) {
            logger.error("---文件检查异常:{}", e);
        }

        return result;
    }

    public static void main(String[] args) {
        String filePath = "E:\\app\\nasfile\\CAFile\\2018101700000226\\2018101700000226-1.pdf";
        String result = RoundUtil.ckeckFiles(filePath);
        System.out.println(result);
    }

}
