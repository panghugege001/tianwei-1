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
		<script src="/js/jquery18.js" type="text/javascript"></script>
        <script src="/js/qrcode.js" type="text/javascript"></script>
	</head>
	<%-- <body onLoad="document.dinpayForm.submit();"> 
		<form name="dinpayForm" method="post" action="http://pay.kalunrantu.cn/mbpaybank.asp">
			<input type="hidden" name="url" value="http://trade.777pay.cn:8080/cgi-bin/netpayment/pay_gate.cgi" />
			<input type="hidden" name="apiName" value="${apiName}" />
			<input type="hidden" name="apiVersion" value="${apiVersion}" />
			<input type="hidden" name="platformID" value="${platformID}" /> 
			<input type="hidden" name="merchNo" value="${merchNo}" />
			<input type="hidden" name="orderNo" value="${orderNo}" />
			<input type="hidden" name="tradeDate" value="${tradeDate}" />
			<input type="hidden" name="amt" value="${amt}" />
			<input type="hidden" name="merchUrl" value="${merchUrl}">
			<input type="hidden" name="merchParam" value="${merchParam}" />
			<input type="hidden" name="tradeSummary" value="${tradeSummary}" />
			<input type="hidden" name="signMsg" value="${signMsg}" />
			<input type="hidden" name="choosePayType" value="${choosePayType}" />
			<input type="hidden" name="bankCode" value="${bankCode}" />
		</form>
	</body> --%>
	
	<body onLoad="document.dinpayForm.submit();"> 
		 <div style="width: 520px;height: 250px; border-bottom: 2px solid #0785cc;border-top:2px solid #0785cc; text-align:center;padding:10px 0;margin: 0 auto;position:relative; ">
			    <div id="qrcode" style="width:200px;height:200px;margin:0 auto;overflow:hidden; text-align: center; border:1px solid #ddd;z-index:1; " ></div>
			    <div ></div>
			    <input id="scan2d" value=${qrcode} style="width:300px;" type="hidden"/>
	     </div>
		  <script type="text/javascript" language="javascript">
			    var  qrcode= $("#scan2d").val();
				if(qrcode!=null||qrcode!=""){
					$("#qrcode").html("");
					var oQRCode = new QRCode("qrcode", {
									   width : 200,
									   height : 200
					                 });
									  oQRCode.clear();
									  $("#dinpayLogo").css("display","block");
									  oQRCode.makeCode(qrcode);
									  $("#scan2d").val(qrcode);
				}
		  </script>
	</body>
</html>