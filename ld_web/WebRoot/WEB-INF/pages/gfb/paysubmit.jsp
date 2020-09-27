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
	<input type="hidden" name="url" value="https://gateway.gopay.com.cn/Trans/WebClientAction.do" />
  	<input type="hidden" name="version" value="${version}" />
	<input type="hidden" name="charset" value="${charset}" />
	<input type="hidden" name="language" value="${language}" />
	<input type="hidden" name="signType" value="${signType}" />
	<input type="hidden" name="tranCode" value="${tranCode}" />
	<input type="hidden" name="merchantID" value="${merchantID}" />
	<input type="hidden" name="merOrderNum" value="${merOrderNum}" />
	<input type="hidden" name="tranAmt" value="${tranAmt}" />
	<input type="hidden" name="feeAmt" value="${feeAmt}" />
	<input type="hidden" name="currencyType" value="${currencyType}" />
	<input type="hidden" name="frontMerUrl" value="${frontMerUrl}" />
	<input type="hidden" name="backgroundMerUrl" value="${backgroundMerUrl}" />
	<input type="hidden" name="tranDateTime" value="${tranDateTime}" />
	<input type="hidden" name="virCardNoIn" value="${virCardNoIn}" />
	<input type="hidden" name="tranIP" value="${tranIP}" />
	<input type="hidden" name="isRepeatSubmit" value="${isRepeatSubmit}" />
	<input type="hidden" name="goodsName" value="${goodsName}" />
	<input type="hidden" name="goodsDetail" value="${goodsDetail}" />
	<input type="hidden" name="buyerName" value="${buyerName}" />
	<input type="hidden" name="buyerContact" value="${buyerContact}" />
	<input type="hidden" name="merRemark1" value="${merRemark1}" />
	<input type="hidden" name="merRemark2" value="${merRemark2}" />
	<input type="hidden" name="bankCode" value="${bankCode}" />
	<input type="hidden" name="userType" value="${userType}" />
	<input type="hidden" name="signValue" value="${signValue}" />
	<input type="hidden" name="gopayServerTime" value="${gopayServerTime}" />
</form>
</body>
</html>
