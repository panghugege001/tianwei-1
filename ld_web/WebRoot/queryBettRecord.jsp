<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<body onLoad="document.queryRecord.submit();">
		<form name="queryRecord" method="post"  target="my-iframe" 
			action="http://123.1.186.208:8086/reports.aspx">
			<input type="hidden" id="account" name="account" value="${account}" />
			<input type="hidden" id="producttype" name="producttype" value="${producttype}" />
			<input type="hidden" id="platformtype" name="platformtype" value="" />
			<input type="hidden" id="keyword" name="keyword" value="${keyword}" />
			</form>
		<iframe scr="http://123.1.186.208:8086/reports.aspx"  name="my-iframe" width="100%" height="95%">
		</iframe>
	</body>
</html>