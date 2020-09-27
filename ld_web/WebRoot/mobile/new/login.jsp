<%@page import="dfh.utils.Constants"%>
<%@page import="dfh.model.Users"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user!=null){
		if("AGENT".equals(user.getRole())){
			response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
		}else{
			response.sendRedirect(request.getContextPath()+"/mobile/new/account.jsp");
		}
	} 
%>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="用户登录" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body class="login-page">
		<div class="page-top">
			<img src="/mobile/img/logo.png" />
			<div id="error-message">欢迎登录天威</div>
		</div>
		<form id="j-login-form" class="form-warp">
			<div class="form-group icon-account rq-value">
				<input data-pattern="\S+" class="form-control" type="text" id="login-account" placeholder="请输入登录账号" value="">
				<div class="ipt-clear undone"></div>
			</div>
			<div class="form-group icon-pwd rq-value">
				<input data-pattern="\S+" class="form-control" type="password" id="login-password" placeholder="请输入登录密码" value="">
				<div class="ipt-clear undone"></div>
			</div>
			<!--<div class="form-group icon-code rq-value">
				<input class="form-control" type="text" id="login-code" placeholder="请输入您的验证码">
				<img class="form-code" id="login-img" src="/mobi/mobileValidateCode.aspx" alt="">
			</div>-->
			<input class="btn-submit" type="submit" value="登录" />
			<div class="help-user">
				<a href="/mobile/new/register.jsp" class="a-link fl" type="submit">新用户注册</a>
				<a href="/mobile/new/forgotPassword.jsp" class="a-link fr">忘记密码?</a>
			</div>
		</form>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			var fp_bbout_element_id = 'fpBB';
			var io_bbout_element_id = 'ioBB';
			var io_install_stm = false;
			var io_exclude_stm = 12;
			var io_install_flash = false;
			var io_enable_rip = true;
			$(document).ready(function() {
				var cpuVal = $('#cpuid').val();
				if(typeof(cpuVal) == "undefined" || cpuVal == null || '' == cpuVal || cpuVal == 'null' || !cpuVal || cpuVal.length < 1) {
					var ioBBVal = $("#ioBB").val();
					$.post("${ctx}/asp/addcpuid.aspx", {
						"cpuid": ioBBVal
					}, function(returnedData, status) {
						if("success" == status) {}
					});
				}
			});
		</script>
		<script>
			$(function() {
				var $image = $('#login-img');
				$image.click(function() {
					$image.attr('src', mobileManage.getSecurityCodeUrl() + '?random=' + Math.random());
				});
				$('#j-login-form').submit(function() {
					var $account = $('#login-account'),
						$password = $('#login-password'),
						$code = $('#login-code'),
						$submitBtn = $(this).find(':submit'),
						$errorMessage = $('#error-message');
					var formData = {
						account: $account.val(),
						password: $password.val()
						//						,imageCode: $code.val()
					};
					var text = mobileManage.disabledBtn($submitBtn);
					mobileManage.getUserManage().login(formData, function(result) {
						if(result.success) {
							window.location.href = '/mobile/new/index.jsp';
						} else {
							$image.click()
							mobileManage.enabledBtn($submitBtn, text);
							$code.val('');
							alert(result.message)
//							$errorMessage.html(result.message);
						}
					});
					return false;
				});
			});
		</script>
		<script>
			var url = window.location.href;
			if(url.indexOf('agent') != -1){
				$('#h-title').html('代理登录');
			}
		</script>
	</body>

</html>