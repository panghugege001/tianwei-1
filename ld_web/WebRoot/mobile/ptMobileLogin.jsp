<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	boolean loginModel = true;
	if(null == user){loginModel=false;}
%>
<!DOCTYPE>
<html>
	<head>
		
	</head>
<body>
	
	<script type="text/javascript">
		if(!<%=loginModel%>){
			alert("需登录后才能进入游戏");
			window.location = '<%=request.getRequestURL().toString().replace(request.getServletPath(), "") %>' + "/mobile/index.jsp";
		}else{
			
			var loginname="K${sessionScope.customer.loginname}".toUpperCase();
			var password="${sessionScope.ptPassword}";
	    	var baseUrl='<%=request.getRequestURL().toString().replace(request.getServletPath(), "") %>';
			window.onload = function(){
				document.getElementById("pth5LoginameIn").value = loginname;
				document.getElementById("pth5passIn").value = password;
				document.getElementById("pth5calloutUrlIn").value = baseUrl;
				document.getElementById("pth5Lobby").value = baseUrl+"/mobile/ptlobby.jsp";
	            document.getElementById("ptH5Form").submit();
	        };
		}
	 	
	</script>
</body>
	<div style="display: none;">
		<form action="http://i5g20f3as-31g4s.com/goldenrose/pth5Login.jsp" method="post" id="ptH5Form">
			<input id="pth5LoginameIn" type="password" name="loginname">
			<input id="pth5passIn" type="password" name="pass">
			<input id="pth5calloutUrlIn" type="password" name="calloutUri">
			<input id="pth5Lobby" type="password" name="pth5Lobby">
		</form>
	</div>
</html>