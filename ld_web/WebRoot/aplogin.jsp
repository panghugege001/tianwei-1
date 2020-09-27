<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="dfh.utils.APUtils"%>
<%@page import="dfh.security.EncryptionUtil"%>
<%@page import="dfh.security.DESEncrypt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>" />
    
    <title>My JSP 'aplogin.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <%
    	String params="cagent=B18_AG"+"/\\\\/loginname=woody"+"/\\\\/dm="+"www.e68.ph"+"/\\\\/actype=0/\\\\/password=1223456"+"/\\\\/sid=B18_AG123456789012345";
			System.out.println(params);
			String  encrypt_key="jsn72ksm";
			String  md5encrypt_key="jsn72ksmm";
			DESEncrypt  des=new DESEncrypt(encrypt_key);
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url="http://220.90.206.25:81/forwardGame.do?params="+targetParams+"&key="+key;
			System.out.println(url);
     %>
     <form action="<%=url %>" method="post" onsubmit="return checkLoginForm()">
     	<button type="submit" value="登录游戏"></button>
     </form>
  </body>
</html>
