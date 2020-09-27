<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>重设后台密码</title>
	</head>
	<body>
		<div id="excel_menu_left">
			其他-->重置后台密码--><a href="javascript:history.back();"><font color="red">上一步</font></a>
		</div>
		<div id="excel_menu">
			<p align="left">
				后台账号：&nbsp;<s:textfield name="username" id="username" size="30" />
			</p>
			<p align="left" style="margin-left: 100px;">
				<input type="button" value="重 置" onclick="resetOperatorPassword()" />
			</p>
		</div>
	</body>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript">
	
		function resetOperatorPassword() {
			
			var username = document.getElementById("username").value;
			
			if (null == username || "" == username) {
			
				alert("请输入要重置的后台账号！");
				document.getElementById('username').focus();
				return;
			}
							
			var action = "/office/resetOperatorPassword.do";
			
			var data = "username=" + username;
			
			var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function (result) {
				
				var text = result.responseText;
				
				alert(text);
				document.getElementById('username').select();
				
				if (text.indexOf('成功') != -1) {
				
					document.getElementById("username").value = "";
					document.getElementById('username').focus();
				}
			}});
		};
	</script>
</html>