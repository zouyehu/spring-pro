package com.zyh.hu.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
  *	在服务器端对 Cookie 设置了HttpOnly 属性，
  * 那么 JS 脚本就不能读取到 cookie,
  * 但是浏览器还是能够正常使用 cookie,
  * 禁用 JS 脚步读取用户浏览器中的Cookie
 * @author HU
 *
 */
public class XssFilter implements Filter {
	 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 解决动态脚本获取网页 cookie，将 cookie 设置成 HttpOnly
        String sessionId = req.getSession().getId();
        resp.setHeader("SET-COOKIE", "JSESSIONID=" + sessionId + "; HttpOnly");
        resp.setHeader("x-frame-options", "SAMEORIGIN");

        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {
    	
    }
    
}
