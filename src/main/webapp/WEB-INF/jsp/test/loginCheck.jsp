<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录验证</title>
<script
	src="<c:url value='/commons/js/jquery-2.1.3.min.js'/>"
	type="text/javascript"></script>
<script>

//点击更换验证码
$("#img").on("click", function(e) {
	_change();
});

//验证码获取
function _change() {
	document.getElementById("img").src = "${ctx}/img?"
			+ new Date().toLocaleString();
	$('#validatecode').val("");
}

</script>
</head>
<body>
<form action="/spring-pro/sysAccess/interface.do" method="post">
用户名：<input id="userName" name="userName" type="text">
密码：<input id="password" name="password" type="password">
<input type="text" name="validatecode" id="validatecode" placeholder="请输入正确的验证码" maxlength="5">
<img id="img" alt="点击获取验证码" width="100" height="36" onclick="_change()">
<input type="submit" value="提交">
</form>
</body>
</html>