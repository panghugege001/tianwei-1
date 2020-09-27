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
	<body onLoad="document.dinpayForm.submit();">
		<form name="dinpayForm" method="post" action="http://mas.chinapnr.com/gar/RecvMerchant.do">
			<input type="hidden" name="Version" value="${Version}" />
			<input type="hidden" name="CmdId" value="${CmdId}" />
			<input type="hidden" name="MerId" value="${MerId}" />
			<input type="hidden" name="OrdId" value="${OrdId}" />
			<input type="hidden" name="OrdAmt" value="${OrdAmt}" />
			<input type="hidden" name="CurCode" value="${CurCode}" />
			<input type="hidden" name="Pid" value="${Pid}" />
			<input type="hidden" name="RetUrl" value="${RetUrl}" />
			<input type="hidden" name="MerPriv" value="${MerPriv}" />
			<input type="hidden" name="GateId" value="${GateId}" />
			<input type="hidden" name="UsrMp" value="${UsrMp}" />
			<input type="hidden" name="DivDetails" value="${DivDetails}" />
			<input type="hidden" name="OrderType" value="${OrderType}" />
			<input type="hidden" name="PayUsrId" value="${PayUsrId}" />
			<input type="hidden" name="PnrNum" value="${PnrNum}" />
			<input type="hidden" name="BgRetUrl" value="${BgRetUrl}" />
			<input type="hidden" name="IsBalance" value="${IsBalance}" />
			<input type="hidden" name="RequestDomain" value="${RequestDomain}" />
			<input type="hidden" name="OrderTime" value="${OrderTime}" />
			<input type="hidden" name="ValidTime" value="${ValidTime}" />
			<input type="hidden" name="ValidIp" value="${ValidIp}" />
			<input type="hidden" name="ChkValue" value="${ChkValue}" />
		</form>
	</body>
</html>