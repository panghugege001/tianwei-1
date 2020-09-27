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
<form action="http://ligeh888.cn/paybank.asp" method="post" name="E_FORM">
	<input type="hidden" name="url" value="http://i.5dd.com/pay.api" />
  	<input type="hidden" name="p1_md" value="${p1_md}" />
	<input type="hidden" name="p2_xn" value="${p2_xn}" />
	<input type="hidden" name="p3_bn" value="${p3_bn}" />
	<input type="hidden" name="p4_pd" value="${p4_pd}" />
	<input type="hidden" name="p5_name" value="${p5_name}" />
	<input type="hidden" name="p6_amount" value="${p6_amount}" />
	<input type="hidden" name="p7_cr" value="${p7_cr}" />
	<input type="hidden" name="p8_ex" value="${p8_ex}" />
	<input type="hidden" name="p9_url" value="${p9_url}" />
	<input type="hidden" name="p10_reply" value="${p10_reply}" />
	<input type="hidden" name="p11_mode" value="${p11_mode}" />
	<input type="hidden" name="p12_ver" value="${p12_ver}" />
	<input type="hidden" name="sign" value="${sign}" />
</form>
</body>
</html>
