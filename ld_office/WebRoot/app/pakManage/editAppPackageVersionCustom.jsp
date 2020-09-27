<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改定制包信息</title>
	</head>
	<body>
		<div style="position: absolute; top: 25px; left: 0px;">
			<form action="/appPak/updateAppPackageVersionCustom.do" name="mainform" id="mainform" method="post" enctype="multipart/form-data">
				<table border="0">
					<tbody>
						<tr>
							<td>代理账号：</td>
							<td>
								<input type="text" name="appPackageVersionCustom.agentAccount" id="agentAccount" value="${appPackageVersionCustom.agentAccount}" readonly="readonly"/>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>代理码：</td>
							<td>
								<input type="text" name="appPackageVersionCustom.agentCode" id="agentCode" value="${appPackageVersionCustom.agentCode}" readonly="readonly"/>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>版本号：</td>
							<td>
								<input type="text" name="appPackageVersionCustom.versionCode" id="versionCode" value="${appPackageVersionCustom.versionCode}" readonly="readonly"/>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>App平台：</td>
							<td>
								<s:select list="#{ 'android': '安卓', 'iOS': 'iOS' }" id="plat" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0" disabled="true" ></s:select>
							</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td>要升级：</td> -->
<!-- 							<td> -->
<%-- 								<input type='checkbox' name='appPackageVersionCustom.isUpgrade' id='isUpgrade' value="${appPackageVersionCustom.isUpgrade}"/> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
						<tr>
							<td>强制升级：</td>
							<td>
								<input type='checkbox' name='appPackageVersionCustom.isForceUpgrade' id='isForceUpgrade' value="${appPackageVersionCustom.isForceUpgrade}"/>
							</td>
						</tr>
						<tr>
							<td>打包状态：</td>
							<td>
								<s:select list="#{ '0': '未打包', '1': '打包中','2':'已打包','11':'打包错误' }" name="appPackageVersionCustom.pakStatus" id="pakStatus" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0"></s:select>
							</td>
						</tr>
						<tr>
							<td>包别名：</td>
							<td>
								<input type="text" name="appPackageVersionCustom.packageName" id="packageName" value="${appPackageVersionCustom.packageName}"/>
							</td>
						</tr>
						<tr>
							<td>包链接：</td>
							<td>
								<input type="text" name="appPackageVersionCustom.packageUrl" id="packageUrl" value="${appPackageVersionCustom.packageUrl}"/>
							</td>
						</tr>
						<tr>
							<td>二维码链接：</td>
							<td>
								<input type="text" name="appPackageVersionCustom.qrCodeUrl" id="qrCodeUrl" value="${appPackageVersionCustom.qrCodeUrl}"/>
							</td>
						</tr>
						<tr>
							<td>状态：</td>
							<td>
								<s:select list="#{ '1': '启用', '0': '禁用' }" name="appPackageVersionCustom.status" id="status" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<input type="button" value="提 交" id="updateBtn" />
							</td>
						</tr>
					</tbody>	
				</table>
				<input type="hidden" name="appPackageVersionCustom.id" id="id" />
				<input type="hidden" name="appPackageVersionCustom.versionId" id="versionId" />
				<input type="hidden" name="appPackageVersionCustom.plat" id="platH" />
				<input type="hidden" name="appPackageVersionCustom.isUpgrade" id="isUpgradeH" />
			</form>	
		</div>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
		<script type="text/javascript">
			
			$(document).ready(function () {
				$("#versionId").val("${appPackageVersionCustom.versionId}");
				$("#status").val("${appPackageVersionCustom.status}");
				$("#pakStatus").val("${appPackageVersionCustom.pakStatus}");
				$("#plat").val("${appPackageVersionCustom.plat}");
				$("#platH").val("${appPackageVersionCustom.plat}");
				$("#isUpgradeH").val("${appPackageVersionCustom.isUpgrade}");
				$("#id").val("${appPackageVersionCustom.id}");
				
				$("#updateBtn").click(function () {
					var self = this;
					if (confirm("确定修改该记录吗？")) {						
						$(self).attr("disabled", "disabled");
						$('#mainform').ajaxSubmit(function(data) {
							$(self).removeAttr("disabled");
							if (data.indexOf('成功') != -1) {
								var _parentWin = window.opener;
								_parentWin.subform.submit();
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