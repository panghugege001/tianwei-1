<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="java.net.URL"%>
<%@taglib uri="/struts-tags" prefix="s"%>
  <!-- ie 全国哀悼灰色 
  <style type="text/css">
	html { filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1); }
  </style>-->
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
String serverName=request.getServerName();
if(serverName.startsWith("www")){
	serverName=serverName.substring(4);
}

String title="";
if(Constants.titles.containsKey(serverName)){
	title=Constants.titles.get(serverName);
}else{
	title=Constants.titles.get("168.tl"); // old title
}

// 获取来源网址：
try{
	String reqURL=request.getRequestURL().toString();
	if(session.getAttribute("referURL")==null){
		String refer = request.getHeader("referer");
		if(refer==null||refer.equals(""))
			refer=reqURL;
		URL url=new URL(refer);
		session.setAttribute("referURL",url.getProtocol()+"://"+url.getHost());
		
	}
	
}catch(Exception e){
}

%>
<title><%=title %></title>
<meta http-equiv="keywords" content=""/>
