<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增版本信息</title>
</head>
<body>
	<div style="position: absolute; top: 25px; left: 0px;">
		<form action="/appPak/addAppPackageVersion.do" name="mainform"
			id="mainform" method="post" enctype="multipart/form-data">
			<table border="0">
				<tbody>
					<tr>
						<td>版本标题：</td>
						<td><input type="text" name="appPackageVersion.versionTitle"
							id="versionTitle" /> <span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>版本号：</td>
						<td><input type="text" name="appPackageVersion.versionCode"
							id="versionCode" /> <span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>App平台类型：</td>
						<td><s:select list="#{'android': '安卓', 'iOS': 'iOS' }"
								name="appPackageVersion.plat" id="plat" listKey="key"
								listValue="value" cssStyle="width: 100px;" theme="simple"
								value="0"></s:select></td>
					</tr>
					<tr>
						<td>状态：</td>
						<td><s:select list="#{ '1': '启用', '0': '禁用' }"
								name="appPackageVersion.status" id="status" listKey="key"
								listValue="value" cssStyle="width: 100px;" theme="simple"
								value="0"></s:select></td>
					</tr>
					<tr>
						<td>版本描述：</td>
						<td><textarea name="appPackageVersion.upgradeLog"
							id="upgradeLog" cols="22" rows="5"></textarea><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="button"
							value="提 交" id="addBtn" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
			
			$(document).ready(function () {
				$("#addBtn").click(function () {
					var self = this;
					if (confirm("确定新增该记录吗？")) {
						if (isNull($("#versionTitle").val())) {
							alert("版本标题不能为空！");
							return;
						}
						
						if (isNull($("#versionCode").val())) {
							alert("版本号不能为空！");
							return;
						}
						
						$(self).attr("disabled", "disabled");
						$('#mainform').ajaxSubmit(function(data) {
							$(self).removeAttr("disabled");
							if (data.indexOf('成功') != -1) {
								var _parentWin = window.opener;
								_parentWin.mainform.submit();
								window.close();
							}
							else{
								alert(data);
							}
						});

						return false;
					}
				});
			});

			function isNull(v) {
				if (v == null || v == "" || v == "null" || v == undefined) {
					return true;
				}
				
				return false;
			};
		</script>
</body>
</html>
