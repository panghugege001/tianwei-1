<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head></head>
	<body>
		<form action="/app/updateConstraintAddressConfig.do" name="mainform" id="mainform" method="post">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr align="left">
					<td>约束类型：</td>
					<td>${constraintAddressConfig.typeName}</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr align="left">
					<td>约束项值：</td>
					<td>
						<input type="text" id="value" name="constraintAddressConfig.value" value="${constraintAddressConfig.value}" />
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="button" value="确 定" onclick="submitConfig()" />
					</td>
				</tr>
			</table>
			<input type="hidden" name="constraintAddressConfig.id" value="${constraintAddressConfig.id}" />
			<input type="hidden" name="constraintAddressConfig.typeId" value="${constraintAddressConfig.typeId}" />
		</form>
	</body>
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript" src="/app/common/function.js"></script>
	<script type="text/javascript">
		
		function submitConfig() {
	
			var value = $("#value").val();
	
			if (isNull(value)) {
	
				alert('约束项值不能为空！');
				return;
			}
			
			$('#mainform').ajaxSubmit(function(data) {
	
				alert(data);
	
				if (data.indexOf('成功') != -1) {
	
					var _parentWin = window.opener;
					_parentWin.mainform.submit();
					window.close();
				}
			});
		};
	</script>
</html>