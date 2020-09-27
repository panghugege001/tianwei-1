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
	<body  onLoad="document.dinpayForm.submit();"> 
		<form name="dinpayForm" method="post" action="${return_url}/yfzf.jsp">
			<input type="hidden" name="VERSION" value="${version}" />
			<input type="hidden" name="INPUT_CHARSET" value="${input_charset}" />
			<input type="hidden" name="RETURN_URL" value="${return_url}" />
			<input type="hidden" name="NOTIFY_URL" value="${notify_url}" />
			<input type="hidden" name="BANK_CODE" value="${bank_code}" />
			<input type="hidden" name="MER_NO" value="${mer_no}">
			<input type="hidden" name="ORDER_NO" value="${order_no}" />
			<input type="hidden" name="ORDER_AMOUNT" value="${order_amount}" />
			<input type="hidden" name="PRODUCT_NAME" value="${product_name}" />
			<input type="hidden" name="PRODUCT_NUM" value="${product_num}" />
			<input type="hidden" name="REFERER" value="${referer}" />
			<input type="hidden" name="CUSTOMER_IP" value="${customer_ip}" />
			<input type="hidden" name="CUSTOMER_PHONE" value="${customer_phone}" />
			<input type="hidden" name="RECEIVE_ADDRESS" value="${receive_address}" />
			<input type="hidden" name="RETURN_PARAMS" value="${return_Params}" />
			<input type="hidden" name="SIGN" value="${sign}" />
			<input type="hidden" name="apiUrl" value="${apiUrl}" />
		</form>
	</body>
</html>