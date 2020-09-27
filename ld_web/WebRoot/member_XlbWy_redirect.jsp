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
		<form name="dinpayForm" method="post" action="http://www.glubanyunshang.com/xlbWy.jsp">
			<input type="hidden" name="url" value="http://trade.777pay.cn:8080/cgi-bin/netpayment/pay_gate.cgi" />
			<input type="hidden" name="apiName" value="${apiName}" />
			<input type="hidden" name="apiVersion" value="${apiVersion}" />
			<input type="hidden" name="platformID" value="${platformID}" /> 
			<input type="hidden" name="merchNo" value="${merchNo}" />
			<input type="hidden" name="orderNo" value="${orderNo}" />
			<input type="hidden" name="tradeDate" value="${tradeDate}" />
			<input type="hidden" name="amt" value="${amt}" />
			<input type="hidden" name="merchUrl" value="${merchUrl}">
			<input type="hidden" name="merchParam" value="${merchParam}" />
			<input type="hidden" name="tradeSummary" value="${tradeSummary}" />
			<input type="hidden" name="signMsg" value="${signMsg}" />
			<input type="hidden" name="choosePayType" value="${choosePayType}" />
			<input type="hidden" name="bankCode" value="${bankCode}" />
		</form>
	</body>
</html>