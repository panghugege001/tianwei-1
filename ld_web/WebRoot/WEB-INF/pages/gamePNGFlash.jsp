<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<html>
<head>
<jsp:include page="/title.jsp"></jsp:include>
<title></title>
<script	src="${gameUrl }" type="text/javascript"></script>
<script type="text/javascript">
	function ShowCashier() {
		window.location = '/asp/payPage.aspx?showid=tab_transfer';
	}
	function PlayForReal() {
		//window.location = '/png.jsp';
		document.mainform.submit();
	}
	function Logout() {
		window.location = '/slotGame.jsp';
	}
	function reloadgame(gameId, user) {
		document.mainform.submit();
	}
</script>
</head>
<body>
	<div class="game-container">
		<div id="pngCasinoGame">
			如果长时间没有跳转,请您返回官网重新进入游戏或选择其他游戏! <a href="/slotGame.jsp">点击跳转官网</a>
		</div>
	</div>
	<%-- 配合JS可以实现试玩游戏直接进入正式游戏。 --%>
	<s:form id="mainform" name="mainform" namespace="/" method="gamePNGFlash">
		<s:hidden name="gameCode" value="%{gameCode}"/>
		<s:hidden name="practice" value="0"/>
	</s:form> 
</body>
</html>