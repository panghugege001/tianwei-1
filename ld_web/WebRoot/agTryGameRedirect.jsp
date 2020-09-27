<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.APTRYUtils"%>
<%@page import="dfh.remote.bean.DspResponseBean"%>
<%@page import="dfh.remote.DocumentParser"%>
<%@page import="dfh.security.EncryptionUtil"%>
<%@page import="dfh.security.DESEncrypt"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.model.AgTryGame"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>天威娱乐城AG试玩娱乐厅</title>
  </head>
  <body>
   <%
  
        HttpSession chksession=request.getSession(true);
        AgTryGame agTryGame  =(AgTryGame)chksession.getValue(Constants.SESSION_AGTRYUSER.toString());
	    String gameType=request.getParameter("gameType");
	    StringBuffer requestUrl = request.getRequestURL();  
	    String domain = requestUrl.delete(requestUrl.length() - request.getRequestURI().length(), requestUrl.length()).append("/").toString(); 
	    
	    String loginname=agTryGame.getAgName();
   		String password=agTryGame.getAgPassword();
	 	 if(null!=loginname && !"".equals(loginname.trim()) && null!=password && !"".equals(password)){
 			String url = AxisSecurityEncryptUtil.agTryLogin(loginname,password,gameType,domain);
 			if(url.contains("http://")){
 			     response.sendRedirect(url);
 			}else{
 			     out.print("<script>alert('"+url+"');window.close();</script>");
 			     out.flush();
 			}
 		}else{
 			out.print("<script>alert('登录异常!');window.close();</script>");
 			out.flush();
 		}
    %>
  </body>
</html>
