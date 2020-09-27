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
	<body  onLoad="document.dinpayForm.submit();"> 
			<form name="dinpayForm" method="post" action="http://www.yuxinbaokeji.com/xbzfbPaybank.jsp">
			<input type="hidden" name="url" value="https://gws.xbeionline.com/Gateway/XbeiPay" />
			<input type="hidden" name="Version" value="${version}" />
			<input type="hidden" name="MerchantCode" value="${merchantCode}" />
			<input type="hidden" name="OrderId" value="${orderId}" />
			<input type="hidden" name="Amount" value="${amount}" />
			<input type="hidden" name="AsyNotifyUrl" value="${asyNotifyUrl}" />
			<input type="hidden" name="SynNotifyUrl" value="${synNotifyUrl}" />
			<input type="hidden" name="OrderDate" value="${orderDate}" />
			<input type="hidden" name="TradeIp" value="${tradeIp}">
			<input type="hidden" name="PayCode" value="${payCode}" />
			<input type="hidden" name="Remark1" value="${remark1}" />
			<input type="hidden" name="Remark2" value="${remark2}" />
			<input type="hidden" name="SignValue" value="${signValue}" />
		</form>
		</body>
</html>