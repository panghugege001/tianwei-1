<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-5-13
  Time: 下午12:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<html>
	<head>
		<title>main</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/css/base.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/css/main.css" />
		<script type="text/javascript"
			src="${ctx}/js/jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/js/jquery/jquery-ui-1.8.21.custom.min.js"></script>
	</head>
	<script> 
	$(document).ready(function () { 
		//setInterval("startRequest()",60*1000); 
	}); 
	function startRequest() { 
		window.location.href="${ctx}/asp/leftBook.aspx";
	} 
	function timeTime(){
	    window.location.href="${ctx}/asp/leftBook.aspx";
	}
</script>
	<body leftmargin="8" topmargin='8'>
		<table width="98%" align="center" border="0" cellpadding="4"
			cellspacing="1" bgcolor="#CBD8AC" style="margin-bottom: 8px">
			<tr>
				<td colspan="2"
					bgcolor="#EEF4EA" class='title'>
					<div style='float: left'>
						<span>站内信管理</span>
					</div>
					<div style='float: right; padding-right: 10px;'></div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td height="30" colspan="2" valign="bottom">
					&nbsp;&nbsp;
					<a href="${ctx}/asp/saveBookPage.aspx" onclick="timeTime();" target="I1">发信箱</a>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td height="30" colspan="2" valign="bottom">
					&nbsp;&nbsp;
					<a href="${ctx}/asp/bookfindAll.aspx" onclick="timeTime();" target="I1">收发信箱:(${countAll})</a>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td height="30" colspan="2" valign="bottom">
					&nbsp;&nbsp;
					<a href="${ctx}/asp/bookfindNoReadAll.aspx" onclick="timeTime();" target="I1">未读信箱:(<font
						color="red">${unreadCount}</font>)</a>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td height="30" colspan="2" valign="bottom">
					&nbsp;&nbsp;
					<a href="${ctx}/asp/bookfindMessage.aspx" onclick="timeTime();" target="I1">公告信箱:(<font
						color="red">${countMessage}</font>)</a>
				</td>
			</tr>
		</table>
	</body>
</html>