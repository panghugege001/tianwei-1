<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新增中奖信息</title>
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<form action="/app/addLatestWinInfo.do" name="mainform" id="mainform" method="post" enctype="multipart/form-data">
				<table border="0">
					<tbody>
						<tr>
							<td>会员账号：</td>
							<td>
								<input type="text" name="latestWinInfo.loginName" id="loginName" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>投注金额：</td>
							<td>
								<input type="text" name="latestWinInfo.betAmount" id="betAmount" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>中奖金额：</td>
							<td>
								<input type="text" name="latestWinInfo.winAmount" id="winAmount" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>游戏名称：</td>
							<td>
								<input type="text" name="latestWinInfo.gameTitle" id="gameTitle" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>游戏图标：</td>
							<td>
								<input type="file" name="gameIcon" id="gameIcon" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>论坛地址：</td>
							<td>
								<input type="text" name="latestWinInfo.forumUrl" id="forumUrl" />
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
								<input type="button" value="提 交" id="addBtn" />
							</td>
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
					
					if (confirm("确定创建该记录吗？")) {
						
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
						if($("#gameIcon").attr("value")=="" || $("#gameIcon").attr("value")==undefined ||$("#gameIcon").attr("value")==null){
								
							alert("游戏图标不能为空！");
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