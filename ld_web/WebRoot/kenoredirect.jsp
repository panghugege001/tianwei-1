<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.utils.KenoUtil"%>
<%@page import="dfh.remote.bean.KenoResponseBean"%>
<%@page import="dfh.remote.DocumentParser"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'kenoredirect.jsp' starting page</title>
    
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
    	//用户名
		HttpSession chksession=request.getSession(true);
		Users user =(Users)chksession.getValue("customer");
		String loginname="";
		String url="";
		String loginid = "";
		if(user != null){
			loginname = user.getLoginname();
			loginid =  AxisSecurityEncryptUtil.generateLoginKenoID(loginname);
			String xml=KenoUtil.login(loginname,user.getAliasName(),request.getRemoteAddr(),loginid,"");
			KenoResponseBean bean= DocumentParser.parseKenologinResponseRequest(xml);
			System.out.print(bean);
			if(bean!=null){
				if(bean.getName()!=null && bean.getName().equals("Link")){
					url="http://"+bean.getValue();
				}else{
					url="kenoSystemMaintenance.asp";
				}
			}
		}
		System.out.println(url);
     %>
     
      <form action="<%=url%>" method="post" id="frm1">
     	 <input type="hidden" name="loginname" id="loginname" value="<%=loginname%>" />
     </form>
     <script language="javascript">
      var loginname = document.getElementById("loginname").value;
	  if(loginname==null || loginname == ""){ 
		alert("请先登录e68帐号再登录游戏");
		window.location.href="index.asp";
	  }else{
	  		window.location.href="<%=url%>";
	  }
	  window.location.href="<%=url%>";
    </script>
    
  </body>
</html>
