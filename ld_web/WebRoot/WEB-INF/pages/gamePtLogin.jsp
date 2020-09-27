<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
	String baseUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
%>
<html>
	<head>
		<script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
		<title>pt游戏登录</title>
	</head>
	<body >
		<div class="overlay"></div>
		<div id="AjaxLoading" class="showbox">
			<div class="loadingWord">
				<img src="${ctx}/images/waiting.gif">
				登录中，请稍候...
			</div>
		</div>
		<div style="display: none;">
			<form action="http://tianweiyule.com/ptforwardpc.jsp" method="post" id="ptH5Form">
				<input id="pth5LoginameIn" type="password" name="loginname" value="TWYLU${sessionScope.customer.loginname}">
				<input id="pth5passIn" type="password" name="password" value="${sessionScope.ptPassword}">
				<input id="ptgamecodeIn" type="password" name="ptgamecode" value="${gameCode}">
				<input type="password" name="language" value="ZH-CN">
				<input id="pth5LobbyIn" type="password" name="ptlobby" value="<%=baseUrl %>">
			</form>
		</div>
		<script type="text/javascript">
			document.getElementById("ptH5Form").submit();
		</script>
	</body>
</html>