<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.Constants" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <style>html,body{height: 100%;} body{ background:#eae9e8;} .c-huangse{color: #dfa85a;} </style>
</head>
<body>
	<div class="w314 fl">
		<img src="https://support.qnappat01.com/Web/license.jpg" width="315" height="420" /> 
		
	</div>
    <div class="w380 fl">
		<p><span class="c-huangse">天威</span>持有菲律宾（PAGCOR）合法博彩执照。</p>
		<p>天威执照由菲律宾娱乐及博彩公司（PAGCOR）所核发和监管，以便提供网上投注服务。</p>
		<p>菲律宾娱乐及博彩公司（PAGCOR）是菲律宾共和国总统办公室100%由政府拥有和控制的公司，</p>
		<p class="mt20">菲律宾娱乐及博彩公司（PAGCOR）是一家拥有合法经营权的公司。</p>
		<p class="mt20">关于（PAGCOR）：</p>
		<p>地址:1330 PAGCOR HouseRoxas Boulevard Ermita, Manila, Philippines 1000</p>
		<p>联系电话： (63 2) 521-1542</p> 
		<p>邮箱： GLDD@pagcor.ph</p>
		<p>如欲查阅更多关于菲律宾娱乐及博彩公司（PAGCOR）的资讯，进入菲律宾（PAGCOR）官网：<a class="c-huangse" href="http://www.pagcor.ph/index.php">www.pagcor.ph</a></p>
	</div> 
</body>
</html>
