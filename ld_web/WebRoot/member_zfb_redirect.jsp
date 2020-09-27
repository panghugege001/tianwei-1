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
	<body onLoad="document.alipayForm.submit();">
		<form name="alipayForm" method="post"
			action="https://mapi.alipay.com/gateway.do?_input_charset=${_input_charset}">
			<input type="hidden" name="_input_charset" value="${_input_charset}" />
			<input type="hidden" name="extra_common_param" value="${extra_common_param}" />
			<input type="hidden" name="notify_url" value="${notify_url}" />
			<input type="hidden" name="out_trade_no" value="${out_trade_no}" />
			<input type="hidden" name="partner" value="${partner}" />
			<input type="hidden" name="payment_type" value="${payment_type}" />
			<input type="hidden" name="paymethod" value="${paymethod}" />
			<input type="hidden" name="return_url" value="${return_url}" />
			<input type="hidden" name="seller_email" value="${seller_email}" />
			<input type="hidden" name="service" value="${service}" />
			<input type="hidden" name="subject" value="${subject}" />
			<input type="hidden" name="total_fee" value="${total_fee}" />
			<input type="hidden" name="sign" value="${sign}" />
			<input type="hidden" name="sign_type" value="${sign_type}" />
		</form>
	</body>
</html>