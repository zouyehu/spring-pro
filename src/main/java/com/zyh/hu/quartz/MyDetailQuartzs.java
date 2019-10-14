package com.zyh.hu.quartz;

import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zyh.hu.utils.ApplicationContextUtil;

/**
 * quartz集群批处理任务总管理层
 * @author zyh
 *
 */
public class MyDetailQuartzs extends QuartzJobBean {

	private Logger logger = LoggerFactory.getLogger(MyDetailQuartzs.class);
	private String targetObject;
    private String targetMethod;
     
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			logger.info("开始执行>>>>>> [" + targetObject+":"+ targetMethod + "] ");	
			Object otargetObject = ApplicationContextUtil.getBean(targetObject);
			Method m = null;
			try {
				m = otargetObject.getClass().getMethod(targetMethod, new Class[] {});
				m.invoke(otargetObject, new Object[] {});
				} catch (SecurityException e) {
					logger.error("---处理异常-1:", e);
					} catch (NoSuchMethodException e) {
						logger.error("---处理异常-2:", e);
					}		
			} catch (Exception e) {
				logger.error("---处理异常-3:", e);
				throw new JobExecutionException(e);
			}
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	
}
