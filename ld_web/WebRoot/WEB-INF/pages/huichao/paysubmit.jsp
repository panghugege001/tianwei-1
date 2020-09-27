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
<title>支付</title>

<body onLoad="document.E_FORM.submit();">
<form action="http://www.yuanhuohuakj.com/hcpay.jsp" method="post" name="E_FORM">
  <table align="center">
    <tr>
      <td></td>
      <td><input type="hidden" name="MerNo" value="${HC_MerNo}"></td>
    </tr>
    <tr>
      <td></td>
      <td><input type="hidden" name="BillNo" value="${BillNo}"></td>
    </tr>
    <tr>
      <td></td>
      <td><input type="hidden" name="Amount" value="${Amount}"></td>
    </tr>

    <tr>
      <td></td>
      <td><input type="hidden" name="ReturnURL" value="${ReturnURL}" ></td>
    </tr>
    
	 <tr>
      <td></td>
      <td><input type="hidden" name="AdviceURL" value="${AdviceURL}" ></td>
    </tr>

    <tr>
      <td></td>
      <td><input type="hidden" name="SignInfo" value="${SignInfo}"></td>
    </tr>
    <tr>
      <td></td>
      <td><input type="hidden" name="Remark" value="${Remark}"></td>
    </tr>
	 <tr>
      <td></td>
      <td><input type="hidden" name="defaultBankNumber" value="${defaultBankNumber}"></td>
    </tr>
	 <tr>
      <td></td>
      <td><input type="hidden" name="orderTime" value="${orderTime}"></td>
    </tr>
    <tr>
      <td></td>
      <td><input type="hidden" name="products" value="${products}"></td>
    </tr>
  </table>
  <p align="center">
    <!-- <input type="submit" name="b1" value="Payment"> -->
  </p>
</form>
</body>
</html>
