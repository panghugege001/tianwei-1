<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="${ctx}/js/lib/jquery18.js"></script>
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
			String mer_no = request.getAttribute("wxP_UserId").toString();
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
			<%
	             if(mer_no.equals("1004048") || mer_no.equals("1004058")){
	         %>
	            	<form name="dinpayForm" method="post" action="${wxP_Notify_URL}/kdzf.jsp">
	      			<input type="hidden" name="P_UserId" value="${wxP_UserId}" />
	      			<input type="hidden" name="P_OrderId" value="${wxP_OrderId}" />
	      			<input type="hidden" name="P_FaceValue" value="${amount}" />
	      			<input type="hidden" name="P_ChannelId" value="${wxP_ChannelId}" />
	      			<input type="hidden" name="P_Price" value="${amount}" />
	      			<input type="hidden" name="P_Quantity" value="${wxP_Quantity}" />
	      			<input type="hidden" name="P_Description" value="${wxP_Description}" />
	      			<input type="hidden" name="P_Notic" value="${wxP_Notic}" />
	      			<input type="hidden" name="P_Result_URL" value="${wxP_Result_URL}" />
	      			<input type="hidden" name="P_Notify_URL" value="${wxP_Notify_URL}" />
	      			<input type="hidden" name="P_PostKey" value="${wxP_PostKey}" />
	      			<input type="hidden" name="apiUrl" value="${apiUrl}" />
	      		</form>
	            	 
	            <% }
	             else {
	            %>
	            	 <!--  <form name="dinpayForm"  method="post" action="http://Api.Duqee.Com/pay/Bank.aspx"> -->
	            	  <form name="dinpayForm"  method="post" action="https://api.duqee.com/pay/KDBank.aspx">
						<input type="hidden" name="P_UserId"  value="${wxP_UserId}" />
						<input type="hidden" name="P_OrderId" value="${wxP_OrderId}" />
						<input type="hidden" name="P_FaceValue" value="${amount}" />
						<input type="hidden" name="P_ChannelId" value="${wxP_ChannelId}" />
						<input type="hidden" name="P_Price" value="${amount}" />
						<input type="hidden" name="P_Quantity" value="${wxP_Quantity}" />
						<input type="hidden" name="P_Description" value="${wxP_Description}" />
						<input type="hidden" name="P_Notic" value="${wxP_Notic}" />
						<input type="hidden" name="P_Result_URL" value="${wxP_Result_URL}" />
						<input type="hidden" name="P_Notify_URL" value="${wxP_Notify_URL}" />
						<input type="hidden" name="P_PostKey" value="${wxP_PostKey}" />
						<input type="hidden" name="apiUrl" value="${apiUrl}" />
						</form>
	            <%
	             }
	            %> 
	</body>
</html>