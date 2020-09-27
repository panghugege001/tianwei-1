<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
	<base href="<%=basePath%>">
		<link rel="stylesheet" rev="stylesheet"
			href="<c:url value='/css/old_css/css.css' />" type="text/css" media="all" />
			<link type="text/css" rel="stylesheet" href="yilufa.css" />
			<style type="text/css">
<!--
body {
	background-color: #0d0213;
}
-->
</style>
		<SCRIPT LANGUAGE=javascript>
	function CheckInput() {
		if (frmUser.loginname.value == '') {
			alert("[提示]用户名不可为空！");
			frmUser.UserName.focus();
			return;
		}
		if (frmUser.password.value == '') {
			alert("[提示]密码不可为空！");
			frmUser.password.focus();
			return;
		}
		if (frmUser.validateCode.value == '') {
			alert("[提示]验证码不可为空！");
			frmUser.validateCode.focus();
			return;
		}
		document.frmUser.sAction.value = "LOGON";
		frmUser.action = "/asp/loginTransfer.aspx";
		frmUser.submit();
	}
	//-->
</SCRIPT>
	</head>

	<body >
		<div id="memberlogincontent_s" style="width:710px; margin:0 auto;">
			<form name="frmUser" method="post">
				<input type=hidden name="sAction">
				<div class="codecon memberuser">
				<label class="codenumtxt">帐号：</label>
				<input name="loginname" type="text" id="name"
					class="memberinput" />
				</div>
				
				<div class="codecon memberuser">
				<label class="codenumtxt">密码：</label>
				<input name="password" type="password" id="password"
				class="memberinput"  />
				<br />
				</div>
				<div class="codecon">
				<label class="codenumtxt">验证：</label>
				<input name="validateCode" type="text" id="password2"
					class="codenuminput" />
				<c:url value="/asp/validateCodeForIndex.aspx" scope="request"
					var="imgCode" />
				<img id="imgCode" src="${imgCode}" title="如果看不清验证码，请点图片刷新"
					onclick="document.getElementById('imgCode').src='${imgCode}?'+Math.random();" />
				<br />
				</div>
				<br />
				<a href="javascript:CheckInput();"><img
						src="<c:url value='/images/log_01.gif' />" width="64" height="29" />
				</a>
				<img src="<c:url value='/images/log_02.gif' />" width="66" height="29" />

			</form>
		</div>
	</body>
	<s:include value="/outmessage.jsp"></s:include>
</html>