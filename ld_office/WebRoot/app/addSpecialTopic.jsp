<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新增活动专题</title>
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<form action="/app/addSpecialTopic.do" name="mainform" id="mainform" method="post" enctype="multipart/form-data">
				<table border="0">
					<tbody>
						<tr>
							<td>专题名称：</td>
							<td>
								<input type="text" name="specialTopic.title" id="title" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>专题图片：</td>
							<td>
								<input type="file" name="topicImage" />
							</td>
						</tr>
						<tr>
							<td>专题地址：</td>
							<td>
								<input type="text" name="specialTopic.topicUrl" id="topicUrl" />
							</td>
						</tr>
						<tr>
							<td>APP应用内链接地址：</td>
							<td>
								<input type="text" name="specialTopic.actionUrl" id="actionUrl" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>状态：</td>
							<td>
								<s:select list="#{ '1': '启用', '0': '禁用' }" name="specialTopic.status" id="status" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0"></s:select>
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
						
						if (isNull($("#title").val())) {
						
							alert("专题名称不能为空！");
							return;
						}
						
						/*if (isNull($("#topicUrl").val())) {
							
							alert("专题地址不能为空！");
							return;
						}*/
						
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