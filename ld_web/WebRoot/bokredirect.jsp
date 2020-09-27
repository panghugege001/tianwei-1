<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.utils.BBinUtils"%>
<%@page import="dfh.remote.bean.DspResponseBean"%>
<%@page import="dfh.remote.DocumentParser"%>
<%@page import="dfh.utils.StringUtil"%>
<%@page import="dfh.utils.BokUtils"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>" />
    
    <title>My JSP 'agredirect.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />   
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
	<meta http-equiv="description" content="This is my page" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <%
   		//用户名
		HttpSession chksession=request.getSession(true);
		Users user =(Users)chksession.getValue("customer");
		user.setCheckcode(StringUtil.getRandomString(6));
		chksession.setAttribute("customer",user);
		
		String loginname="";
		String result="";
	 	String url="";
	 	if(user != null){
	 		 loginname = user.getLoginname();
		 	 if(user.getRole().equals("MONEY_CUSTOMER")){
		 		String [] results = BokUtils.CheckOrCreateGameAccount(loginname);
		 		if(null!=results[0]){
		 			if("1".equals(results[0])|| "-1".equals(results[0]) && "uid exists".equals(results[1])){
		 				//注册成功或者已注册
		 				url = "http://www.bok88.com/api.aspx?src=abc&op=login"+
		 				"&uid="+loginname+"&checkcode="+user.getCheckcode()+"&gid="+BokUtils.FORESTDANCE;
		 			}else{
		 				result = results[1];
		 			}
		 		}
		 	}else{
		 		result="登录异常";
		 	}
	 	}
	 	if(loginname==null || "".equals(loginname)){
	 		out.print("<script>alert('请先登录天威帐号再登录游戏');window.close();</script>");
	 		out.flush();
	 	}else if(result!=null && !"".equals(result)){
	 		out.print("<script>alert('登录游戏过程中出现问题**"+result+"**,请联系在线客服');window.close();</script>");
	 		out.flush();
	 	}else{
	 		response.sendRedirect(url);
	 	}
    %>
  </body>
</html>
