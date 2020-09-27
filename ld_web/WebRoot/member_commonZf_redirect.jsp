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
		<form name="dinpayForm" method="post" action="${return_url}/zfpay.jsp">
			<input type="hidden" name="url" value="https://pay.dinpay.com/gateway?input_charset=UTF-8" />
			<input type="hidden" name="sign" value="${sign}" />
			<input type="hidden" name="merchant_code" value="${merchant_code}" />
			<input type="hidden" name="bank_code" value="${bank_code}" />
			<input type="hidden" name="order_no" value="${order_no}" />
			<input type="hidden" name="order_amount" value="${order_amount}" />
			<input type="hidden" name="service_type" value="${service_type}" />
			<input type="hidden" name="input_charset" value="${input_charset}" />
			<input type="hidden" name="notify_url" value="${notify_url}">
			<input type="hidden" name="interface_version" value="${interface_version}" />
			<input type="hidden" name="sign_type" value="${sign_type}" />
			<input type="hidden" name="order_time" value="${order_time}" />
			<input type="hidden" name="product_name" value="${product_name}" />
			<input Type="hidden" Name="client_ip" value="${client_ip}" />
			<input Type="hidden" Name="extend_param" value="${extend_param}" />
			<input Type="hidden" Name="extra_return_param" value="${extra_return_param}" />
			<input Type="hidden" Name="product_code" value="${product_code}" />
			<input Type="hidden" Name="product_desc" value="${product_desc}" />
			<input Type="hidden" Name="product_num" value="${product_num}" />
			<input Type="hidden" Name="return_url" value="${return_url}" />
			<input Type="hidden" Name="show_url" value="${show_url}" />
		</form>
	</body>
</html>