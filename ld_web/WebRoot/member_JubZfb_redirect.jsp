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
 			document.oncontextmenu = new Function("return false;") 
 			//跳转聚宝收银平台
 			window.location.href="http://www.juedaifengh.com/jubaopay.htm?signature=${jubaoPay.signature}&message=${jubaoPay.message}&payMethod=${payMethod}";
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
	<%-- <body onLoad="document.dinpayForm.submit();">
		<form name="dinpayForm" method="get" action="http://www.disufei.com/jubaopay.htm?signature=${jubaoPay.signature}&message=${jubaoPay.message}&payMethod=${payMethod}">
			<input type="hidden" name="message" value="${jubaoPay.message}" />
			<input type="hidden" name="signature" value="${jubaoPay.signature}" />
		</form>
	</body> --%>
</html>