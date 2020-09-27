<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	if(session.getAttribute(Constants.SESSION_CUSTOMERID)!=null){
		response.sendRedirect(request.getContextPath()+"/mobile/");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="找回密码" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body class="forgotPassword-page">
		<div class="page-top">
			<img src="/mobile/img/logo.png" />
			<div id='page-msg'>
				<img src="/mobile/img/icon/safe.png" style="width: 0.28rem;height:0.333333rem;margin-top: -4px;" />
				<span class="hidden">请输入手机号，获取验证短信</span>
				<span class="hidden">请输入邮箱地址，获取更改密码邮件</span>
				<span>正在为您重置密码，请选择找回方式。</span>
			</div>
		</div>
		<div id="page-index">
			<div class="list-group">
				手机找回密码
			</div>
			<div class="list-group">
				邮箱找回密码
			</div>
			<a class="list-group"  href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank">
				联系在线客服
			</a>
		</div>
		<div class="tab-panel" id="find-sms-page">
			<form class="form-warp" id="form-sms">
				<div class="form-group icon-account">
					<input name="account" type="text" class="form-control" placeholder="请输入您的游戏账号">
				</div>
				<div class="form-group icon-phones">
					<input name="phone" type="text" class="form-control" placeholder="请输入您的注册电话号码">
				</div>

				<div class="form-group icon-code">
					<input id="forgot-code1" placeholder="验证码" type="text" name="imageCode" class="form-control">
					<img class="form-code" id="forgot-image1" title="如果看不清验证码，请点图片刷新" src="${imgCode}" alt="">
				</div>

				<input type="submit" class="btn-submit" value="找回密码">

			</form>
		</div>
		<div class="tab-panel" id="find-mail-page">
			<form class="form-warp" id="form-email">
				<div class="form-group  icon-account">
					<input name="account" type="text" class="form-control" placeholder="请输入您的游戏账号">
				</div>
				<div class="form-group  icon-email">
					<input type="text" name="email" class="form-control" placeholder="请输入您的邮箱">
				</div>

				<div class="form-group icon-code">
					<input id="forgot-code" placeholder="验证码" type="text" name="imageCode" class="form-control">
					<img class="form-code" id="forgot-image" title="如果看不清验证码，请点图片刷新" src="/mobi/mobileValidateCode.aspx" alt="">
				</div>
				<input type="submit" class="btn-submit" value="找回密码">
			</form>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/UserManage.js?v=1020"></script>
		<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1210"></script>
		<script type="text/javascript">
			$(function() {
				window.header.backFun = function() {
					if($('#page-index').is(":hidden")) {
						$('#page-index').show();
						$("#page-msg span").last().show().siblings('span').hide();
						$('.forgotPassword-page .tab-panel').hide();
					} else {
						history.back();
					}
				}
				$('#page-index div.list-group').click(function() {
					$(this).parent().hide();
					$('.forgotPassword-page .tab-panel').eq($(this).index()).show();
					$("#page-msg span").eq($(this).index()).show().siblings('span').hide();
				})
				new ForgotPasswordPage();
				$(document).on('click', '[data-live-action]', function() {
					mobileManage.redirect('online800');
				});
			});

			function ForgotPasswordPage() {
				var that = this;
				that.$code = $('#forgot-code');
				that.$code1 = $('#forgot-code1');
				that.$image = $('#forgot-image');
				that.$formSms = $('#form-sms');
				that.$formEmail = $('#form-email');
				that.$image1 = $('#forgot-image1');

				this.init = function() {
					that.$formSms.on('submit', function() {

						var formData = that.$formSms.serializeObject();
						mobileManage.getLoader().open("处理中");
						mobileManage.getUserManage().findPasswordByPhone(formData, function(result) {
							mobileManage.getLoader().close();
							if(result.success) {
								alert(result.message);
							} else {
								that.$code1.val('');
								that.$image1.attr('src', Global.config.imgCodeUrl + '?' + Math.random());
								alert(result.message);
							}
						});
						return false;

					});
					that.$formEmail.on('submit', function() {

						var formData = that.$formEmail.serializeObject();

						mobileManage.getLoader().open("处理中");
						mobileManage.getUserManage().findPasswordByEmail(formData, function(result) {
							if(result.success) {
								alert(result.message);
							} else {
								alert(result.message);
								that.$code.val('');
								that.$image.attr('src', Global.config.imgCodeUrl + '?' + Math.random());
							}
							mobileManage.getLoader().close();
						});
						return false;
					});
					that.$image.on('click', function() {
						that.$image.attr('src', Global.config.imgCodeUrl + '?random=' + Math.random());
					})
					that.$image1.on('click', function() {
						that.$image1.attr('src', Global.config.imgCodeUrl + '?random=' + Math.random());
					})
				};

				this.init();
			}
		</script>

	</body>

</html>