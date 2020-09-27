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
		<form name="dinpayForm" method="post" action="https://pay.lefu8.com/gateway/trade.htm">
			<input type="hidden" name="apiCode" value="${apiCode}" />
			<input type="hidden" name="versionCode" value="${versionCode}" />
			<input type="hidden" name="inputCharset" value="${inputCharset}" />
			<input type="hidden" name="signType" value="${signType}" />
			<input type="hidden" name="redirectURL" value="${redirectURL}" />
			<input type="hidden" name="notifyURL" value="${notifyURL}" />
			<input type="hidden" name="sign" value="${sign}" />
			<input type="hidden" name="partner" value="${partner}">
			<input type="hidden" name="buyer" value="${buyer}" />
			<input type="hidden" name="buyerContactType" value="${buyerContactType}" />
			<input type="hidden" name="buyerContact" value="${buyerContact}" />
			<input type="hidden" name="outOrderId" value="${outOrderId}" />
			<input Type="hidden" Name="amount" value="${amount}" />
			<input Type="hidden" Name="paymentType" value="${paymentType}" />
			<input Type="hidden" Name="interfaceCode" value="${interfaceCode}" />
			<input Type="hidden" Name="retryFalg" value="${retryFalg}" />
			<input Type="hidden" Name="submitTime" value="${submitTime}" />
			<input Type="hidden" Name="timeout" value="${timeout}" />
			<input Type="hidden" Name="clientIP" value="${clientIP}" />
			<input Type="hidden" Name="returnParam" value="${returnParam}" />
		</form>
	</body>
</html>