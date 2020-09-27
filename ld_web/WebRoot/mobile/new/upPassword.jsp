<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="修改密码" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div id="page-chagepassword">
			<form id="form-chagepassword" class="form-warp no-icon">
				<div class="form-tips">旧密码</div>
				<div class="form-group">
					<input name="password" data-pattern="^[a-zA-Z0-9]{6,10}$" type="password" class="form-control" placeholder="请输入旧密码" required="">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="form-tips">新密码</div>
				<div class="form-group">
					<input name="newPassword" data-pattern="^[a-zA-Z0-9]{6,10}$" type="password" class="form-control" placeholder="密码为6-16位数字或英文字母，英文字母开头" required="">
				</div>
				<div class="form-tips">确认密码</div>
				<div class="form-group">
					<input name="confirmPassword" data-pattern="^[a-zA-Z0-9]{6,10}$" type="password" class="form-control" maxlength="20" placeholder="再次输入密码，确认新密码无误" required="">
					<div class="ipt-clear undone"></div>
				</div>

				<input class="btn-submit block" type="submit" value="确认修改">

			</form>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script>
			function ChagepasswordPage() {
				var that = this;
				var $form = $('#form-chagepassword');
				that._init = function() {
					$form.submit(function() {
						var formData = $form.serializeObject();
						mobileManage.getLoader().open("设置中");
						mobileManage.getUserManage().changePassword(formData, function(result) {
							mobileManage.getLoader().close();
							if(result.success) {
								alert(result.message);
							} else {
								alert(result.message);
							}
						});
						return false;
					});
				};
				that._init();
			}
			ChagepasswordPage()
		</script>
	</body>

</html>