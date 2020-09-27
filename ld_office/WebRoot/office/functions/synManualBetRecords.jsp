<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>手动同步注单</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
	</head>
	<body>
		<div id="excel_menu_left">
			其它 -->邮件平台
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
			<s:form action="synBetRecords" namespace="/office" theme="simple" onsubmit="submitonce(this);">
				<s:submit value="手动同步注单"></s:submit>
			</s:form>
	</body>
</html>

