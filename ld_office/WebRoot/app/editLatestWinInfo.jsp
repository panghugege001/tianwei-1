<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改中奖信息</title>
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<form action="/app/updateLatestWinInfo.do" name="mainform" id="mainform" method="post" enctype="multipart/form-data">
				<table border="0">
					<tbody>
						<tr>
							<td>会员账号：</td>
							<td>
								<input type="text" name="latestWinInfo.loginName" id="loginName" value="${latestWinInfo.loginName}"/>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>投注金额：</td>
							<td>
							    <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${latestWinInfo.betAmount}" var="betAmount"/>
								<input type="text" name="latestWinInfo.betAmount" id="betAmount" value="${betAmount}" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>中奖金额：</td>
							<td>
							    <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${latestWinInfo.winAmount}" var="winAmount"/>
								<input type="text" name="latestWinInfo.winAmount" id="winAmount" value="${winAmount}" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>游戏名称：</td>
							<td>
								<input type="text" name="latestWinInfo.gameTitle" id="gameTitle" value="${latestWinInfo.gameTitle}" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>游戏图标：</td>
							<td>
								<input type="file" name="gameIcon" id="gameIcon"/>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<img alt="" src="${latestWinInfo.gameIcon}" />
							</td>
						</tr>
						<tr>
							<td>论坛地址：</td>
							<td>
								<input type="text" name="latestWinInfo.forumUrl" id="forumUrl" value="${latestWinInfo.forumUrl}" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>状态：</td>
							<td>
								<s:select list="#{ '1': '启用', '0': '禁用' }" name="latestWinInfo.status" id="status" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<input type="button" value="提 交" id="updateBtn" />
							</td>
						</tr>
					</tbody>	
				</table>
				<input type="hidden" name="latestWinInfo.id" id="id" />
			</form>	
		</div>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
		<script type="text/javascript">
			
			$(document).ready(function () {
				
				$("#status").val("${latestWinInfo.status}");
				$("#id").val("${latestWinInfo.id}");
				
				$("#updateBtn").click(function () {
					
					var self = this;
					
					if (confirm("确定修改该记录吗？")) {
						
						if (isNull($("#loginName").val())) {
							
							alert("会员帐号不能为空！");
							return;
						}
						
						if (isNull($("#betAmount").val())) {
							
							alert("投注金额不能为空！");
							return;
						}
						
						if (isNaN($("#betAmount").val())) {

							alert("投注金额只能为有效数字！");
							return;
						}
						
						if (parseInt($("#betAmount").val())<=0) {

							alert("投注金额只能大于0！");
							return;
						}
						
						if (isNull($("#winAmount").val())) {
							
							alert("中奖金额不能为空！");
							return;
						}
						
						if (isNaN($("#winAmount").val())) {

							alert("中奖金额只能为有效数字！");
							return;
						}
						
						if (parseInt($("#winAmount").val())<=0) {

							alert("中奖金额只能大于0！");
							return;
						}
						
						if (isNull($("#gameTitle").val())) {
							
							alert("游戏名称不能为空！");
							return;
						}
						
						
						if (isNull($("#forumUrl").val())) {
							
							alert("论坛地址不能为空！");
							return;
						}
						
						$(self).attr("disabled", "disabled");

						$('#mainform').ajaxSubmit(function(data) {

							$(self).removeAttr("disabled");

							alert(data);

							if (data.indexOf('成功') != -1) {

								var _parentWin = window.opener;
								_parentWin.mainform.submit();
								window.close();
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