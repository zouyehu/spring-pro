package com.zyh.hu.interceptor;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zyh.hu.comments.ResponseBaseBean;
import com.zyh.hu.repository.AppProperRepository;
import com.zyh.hu.service.JsonConvertProvider;


/**
 * 单点登录
 * @author HU
 * @date 2019-02-20
 */
public class AccessTokenInterceptor implements HandlerInterceptor{

private static final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);
private AntPathMatcher matcher = new AntPathMatcher();

	@Value("#{'${REQUEST.HU.CHECKTRANSCODE}'.split('#')}")
	private List<String> checkTransCodes;//需要拦截的接口号
	
	@Value("#{'${REQUEST.HU.CHECKPATTERURLS}'.split('#')}")
	private List<String> checkPatterUrls;//需要拦截的URL

	@Autowired
	private JsonConvertProvider jsonConvertProvider;
	
	@Autowired
	private AppProperRepository appProperRepository;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ request.getContextPath();
		String interSwitch = appProperRepository.getValueByKey("INTER.SWITCH", "CMPS");//1-开,0-关
		String reqUri = request.getRequestURI();
		if ("1".equals(interSwitch)) {
			for (String checkUrl:checkPatterUrls) {
				if (matcher.match(checkUrl, reqUri)) {
					response.sendRedirect(basePath+"/sysAccess/userLogin.do");
		            return false;
				}
			}
			
			String transCode = request.getParameter("transCode");
			for (String code:checkTransCodes) {
				if (matcher.match(code, transCode)) {
					response.sendRedirect(basePath+"/sysAccess/userLogin.do");
		            return false;
				}
			}
		} else {
			return true;
		}
		
		String appToken = request.getHeader("appToken");
		logger.info("---前端请求传过来的加密token为:" + appToken);
		if (StringUtils.isNotBlank(appToken)) {
			String token = "123123";//从数据库取出的或从redis中拿到的,根据实际业务需求来做
			if (appToken.equals(token)) {
				return true;
			}
			this.writeResponse(response, "2222", "会话超时,请重新登录");
			return false;
		}
		
		this.writeResponse(response, "3333", "您当前无权限访问此页面,请联系管理员授权。");
		return false;
	}
	
	/**
	 * return false时
	 * 返回相应错误信息
	 * @param response
	 * @param errorMsg
	 * @throws IOException
	 */
	private void writeResponse(HttpServletResponse response, String errorCode, String errorMsg)
			throws IOException {
		
		ResponseBaseBean baseBean = new ResponseBaseBean();
		baseBean.setResultCode(errorCode);
		baseBean.setResultMsg(errorMsg);
		String json = this.jsonConvertProvider.toJson(baseBean);
		response.setContentType("application/json");
		Writer writer = response.getWriter();
		writer.write(json);
		writer.close();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public JsonConvertProvider getJsonConvertProvider() {
		return jsonConvertProvider;
	}

	public void setJsonConvertProvider(JsonConvertProvider jsonConvertProvider) {
		this.jsonConvertProvider = jsonConvertProvider;
	}
	
	
}
