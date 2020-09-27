<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script language="JavaScript"> 
 			function click(e) { 
  				if (document.all) { 
    				if (event.button==2||event.button==3) { 
    					oncontextmenu='return false'; 
   					} 
  				} 
 			}
			document.onmousedown=click; 
 			document.oncontextmenu = new Function("return false;");
 			
		</script>
		<%
			HttpSession chksession = request.getSession(true);
			Users user = (Users) chksession.getValue("customer");
			if (user == null) {
				out.print("<script type=text/javascript>alert('你的登录已过期，请从首页重新登录');window.location.href='index.html';</script>");
				out.flush();
				return;
			} 
			if (!"MONEY_CUSTOMER".equals(user.getRole())) {
				out.print("<script type=text/javascript>window.location.href='index.html';</script>");
				out.flush();
				return;
			}
		%>
	</head>
	<body onLoad="document.dinpayForm.submit();">
		<form name="dinpayForm" method="GET" action="${api_url}">
			<input type="hidden" name="parter" value="${parter}" />
			<input type="hidden" name="type" value="${type}" />
			<input type="hidden" name="orderid" value="${orderid}" />
			<input type="hidden" name="callbackurl" value="${callbackurl}" />
			<input type="hidden" name="hrefbackurl" value="${hrefbackurl}" />
			<input type="hidden" name="value" value="${value}" />
			<input type="hidden" name="attach" value="${attach}" />
			<input type="hidden" name="payerIp" value="${payerIp}" />
			<input type="hidden" name="sign" value="${sign}" />
	    </form>	
   </body>
</html>