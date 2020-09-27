<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>其他</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript">
	//得到用户信息
	function loadInfo() {
		var frm = document.getElementById("mainform");
		frm.action = "<c:url value='/office/getUseremailphone.do' />";
		frm.submit();
	}
</script>
	</head>
	<body>
		<div id="excel_menu_left">
			其他 --&gt; 修改远程接口
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<s:fielderror></s:fielderror>
		<table align="left" border="0">
			<s:iterator var="url" value="%{#request.urls}">
				<s:form action="modifyRemoteUrl" onsubmit="submitonce(this);"
					namespace="/office" theme="simple">
					<s:hidden name="key" value="%{#url.id}"></s:hidden>
					<tr>
						<td>
							<s:property
								value="@dfh.model.enums.RemoteUrlType@getText(#url.id)" />
						</td>
						<td>
							<s:textfield name="value" value="%{#url.value}" size="30" />
						</td>
						<td>
							<s:submit value="修 改"></s:submit>
						</td>
					</tr>
				</s:form>
			</s:iterator>
		</table>
		<br />
		<br />
		<br />
		<br />
		<br />
		<br />
		<br />
		<br />
		<br />
		<table>
			<tr>
				<Td>
					<s:form action="modifyUserRemoteUrl" onsubmit="submitonce(this);"
						namespace="/office" theme="simple">
				账户：
						<s:textfield name="loginname"></s:textfield>
					远程接口地址：
						<s:textfield name="remoteUrl"></s:textfield>
						<s:submit value="修改"></s:submit>
					</s:form>
				</Td>
			</tr>
		</table>







		<c:import url="/office/script.jsp" />
	</body>
</html>
