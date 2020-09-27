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
<title>支付</title></head>
<body onLoad="document.E_FORM.submit();">
<form action="${jumpUrl}" method="post" name="E_FORM">
    <input type="hidden" name="url" value="https://mag.kjtpay.com/mag/gateway/receiveOrder.do" />
  	<input type="hidden" name="service" value="${service}" />
	<input type="hidden" name="version" value="${version}" />
	<input type="hidden" name="partner_id" value="${partner_id}" />
	<input type="hidden" name="_input_charset" value="${_input_charset}" />
	<input type="hidden" name="return_url" value="${return_url}" />
	<input type="hidden" name="request_no" value="${request_no}" />
	<input type="hidden" name="trade_list" value="${trade_list}" />
	<input type="hidden" name="buyer_id" value="${buyer_id}" />
	<input type="hidden" name="buyer_id_type" value="${buyer_id_type}" />
	<input type="hidden" name="pay_method" value="${pay_method}" />
	<input type="hidden" name="go_cashier" value="${go_cashier}" />
	<input type="hidden" name="memo" value="${memo}" />
	<input type="hidden" name="sign" value="${sign}" />
	<input type="hidden" name="sign_type" value="${sign_type}" />
</form>
</body>
</html>
