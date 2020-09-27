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
		<form name="dinpayForm" method="post" action="https://pay.heepay.com/Payment/Index.aspx">
			<input type="hidden" name="version" value="${version}" />
			<input type="hidden" name="pay_type" value="${pay_type}" />
			<input type="hidden" name="pay_code" value="${pay_code}" />
			<input type="hidden" name="agent_id" value="${agent_id}" />
			<input type="hidden" name="agent_bill_id" value="${agent_bill_id}" />
			<input type="hidden" name="pay_amt" value="${pay_amt}" />
			<input type="hidden" name="notify_url" value="${notify_url}" />
			<input type="hidden" name="return_url" value="${return_url}" />
			<input type="hidden" name="user_ip" value="${user_ip}" />
			<input type="hidden" name="agent_bill_time" value="${agent_bill_time}" />
			<input type="hidden" name="goods_name" value="${goods_name}" />
			<input type="hidden" name="goods_num" value="${goods_num}" />
			<input type="hidden" name="remark" value="${remark}" />
			<input type="hidden" name="goods_note" value="${goods_note}" />
			<input type="hidden" name="sign" value="${sign}" />
		</form>
	</body>
</html>