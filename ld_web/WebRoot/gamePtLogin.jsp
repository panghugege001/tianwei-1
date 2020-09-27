<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
	String baseUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
%>
<html>
<head>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-1.11.2.min.js"></script>
	<style>
		* {
			margin: 0;
			padding: 0;
			list-style-type: none;
		}

		a,img {
			border: 0;
		}

		.demo {
			margin: 100px auto 0 auto;
			width: 400px;
			text-align: center;
			font-size: 18px;
		}

		.demo .action {
			color: #3366cc;
			text-decoration: none;
			font-family: "微软雅黑", "宋体";
		}

		.overlay {
			position: fixed;
			top: 0;
			right: 0;
			bottom: 0;
			left: 0;
			z-index: 99998;
			width: 100%;
			height: 100%;
			_padding: 0 20px 0 0;
			background: #f6f4f5;
			display: none;
		}

		.showbox {
			position: fixed;
			top: 0;
			left: 50%;
			z-index: 99999;
			opacity: 0;
			filter: alpha(opacity = 0);
			margin-left: -80px;
			margin-top: -60px;
		}

		* html,* html body {
			background-image: url(about:blank);
			background-attachment: fixed;
		}

		* html .showbox,* html .overlay {
			position: absolute;
			top: expression(eval(document . documentElement . scrollTop) );
		}

		#AjaxLoading {
			border: 1px solid #8CBEDA;
			color: #37a;
			font-size: 12px;
			font-weight: bold;
		}

		#AjaxLoading div.loadingWord {
			width: 180px;
			height: 50px;
			line-height: 50px;
			border: 2px solid #D6E7F2;
			background: #fff;
		}

		#AjaxLoading img {
			margin: 10px 15px;
			float: left;
			display: inline;
		}
	</style>
	<script type="text/javascript">
		//登录游戏
// 		function login(realMode) {
// 			openProgressBar();
// 			var gameCode="${gameCode}";
// 			if(gameCode==null || gameCode ==""){
// 				alert("游戏编码不能为空！");
// 				window.close();
// 				return;
// 			}
// 			var loginname="E${sessionScope.customer.loginname}".toUpperCase();
// 			var password="${sessionScope.ptPassword}";
// 			if(loginname==null || loginname == "" || password==null || password=="" ){
// 				alert("登录失效，请重新登录官网！");
// 				window.close();
// 				return;
// 			}
// 			var loginname="E${sessionScope.customer.loginname}".toUpperCase();
// 			var password="${sessionScope.ptPassword}";
<%-- 			var baseUrl='<%=request.getRequestURL().toString().replace(request.getServletPath(), "") %>'; --%>
// 			document.getElementById("pth5LoginameIn").value = loginname;
// 			document.getElementById("pth5passIn").value = password;
// 			document.getElementById("ptgamecodeIn").value = gameCode;
// 			document.getElementById("pth5LobbyIn").value = baseUrl;
// 			document.getElementById("ptH5Form").submit();
// 		}

		//打开进度条
		function openProgressBar(){
			var h = $(document).height();
			$(".showbox").css({"z-index": "99999" });
			$(".overlay").css({"height": h });
			$(".overlay").css({'display':'block','opacity':'0.8'});
			$(".showbox").stop(true).animate({'margin-top':'300px','opacity':'1'},200);
		}
		//关闭进度条
		function closeProgressBar(){
			$(".showbox").css({"z-index": "-99999" });
			$(".showbox").stop(true).animate({'margin-top':'250px','opacity':'0'},400);
			$(".overlay").css({'display':'none','opacity':'0'});
		}
	</script>
</head>
<body>
<div class="overlay"></div>
<div id="AjaxLoading" class="showbox">
	<div class="loadingWord">
		<img src="${ctx}/images/waiting.gif">
		登录中，请稍候...
	</div>
</div>
<div style="display: none;">
	<form action="http://i5g20f3as-31g4s.com/goldenrose/ptforward.jsp" method="post" id="ptH5Form">
			<input id="pth5LoginameIn" type="password" name="loginname" value="K${sessionScope.customer.loginname}">
			<input id="pth5passIn" type="password" name="password" value="${sessionScope.ptPassword}">
			<input id="ptgamecodeIn" type="password" name="ptgamecode" value="${gameCode}">
			<input type="password" name="language" value="ZH-CN">
			<input id="pth5LobbyIn" type="password" name="ptlobby" value="<%=baseUrl %>">
		</form>
</div>
<script type="text/javascript">
	openProgressBar();
	document.getElementById("ptH5Form").submit();
</script>
</body>
</html>