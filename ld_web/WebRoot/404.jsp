<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>你访问的页面不存在!</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="${ctx}/css/404.css?v=1"/>

  </head>
  
  <body>
  <div class="page">
  	<div class="logo"><a href="/"><img src="/images/logo.png?v=3"></a></div>
  	<div class="top404"><img src="/images/404_banner2.png"></div>
  	<div class="text">
  		<div class="first">“真的很抱歉，我们弄丢了页面.....” </div>
  		<div class="second"> 要不要去首页看看？</div>
  	</div>
  	<div class="button"><button><a href="/">去首页</a></button></div>
  </div>
  </body>
</html> 
 