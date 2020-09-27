<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/account.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户设置" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="form-warp no-icon" id="pane-default-0">
			<label class="form-tips">用户名</label>
			<div class="form-group">
				<input type="text" class="form-control" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.loginname, 0.7,1) "/>" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">姓名</label>
			<div class="form-group">
				<input type="text" class="form-control" value="<s:property value=" @dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1) "/>" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">密码</label>
			<div class="form-group">
				<input type="text" class="form-control" value="********" disabled="" disabled="" readonly>
				<a href="/mobile/new/upPassword.jsp" class="form-code">修改密码</a>
			</div>
			<label class="form-tips">登陆次数</label>
			<div class="form-group">
				<input type="text" class="form-control" value="${customer.loginTimes}" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">上次登录</label>
			<div class="form-group">
				<input type="text" class="form-control" value="${customer.tempLastLoginTime}" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">上次IP</label>
			<div class="form-group">
				<input type="text" class="form-control" value="${customer.lastLoginIp}" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">登陆地点</label>
			<div class="form-group">
				<input type="text" class="form-control" value="${customer.lastarea}" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">联系电话</label>
			<div class="form-group">
				<input type="text" class="form-control" value="<s:property value=" @dfh.utils.StringUtil@formatStar(#session.customer.phone, 0.5,1) "/>" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">电子邮箱</label>
			<div class="form-group">
				<input type="text" class="form-control" value="${customer.email}" disabled="" disabled="" readonly>
			</div>
			<label class="form-tips">密保问题</label>
			<div class="form-group">
				<input id="question" class="form-control" type="text" disabled="" disabled="" readonly>
				<a class="form-code hidden" id='account-question-button' href="/mobile/new/secret.jsp">设定密保</a>
			</div>
			<label class="form-tips">QQ号码</label>
			<div class="form-group">
				<input id="QQ" data-pattern="\S+" class="form-control" type="text" placeholder="<s:property value=" @dfh.utils.StringUtil@formatStar(#session.customer.qq, 0.7,0) "/>">
				<div class="ipt-clear undone"></div>
			</div>
			<div class="btn-submit " id="account-submit">提交</div>
		</div>
		<br />
		<br />
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script>
			UserInfo();
			//账户资料
			function UserInfo() {
				var that = this;
				var _questionNames = {
					'1': true,
					'2': true,
					'3': true,
					'4': true,
					'5': true,
					'6': true,
				};
				that.$QQ = $('#QQ');
				//查询密保问题
				mobileManage.getUserManage().getQuestion(function(result) {
					if(result.success && _questionNames[result.data.questionId]) {
						$('#question').val('已设置');
					} else {
						$('#question').val('尚未设置');
						$('#account-question-button').show();
					}
				});
				$('#account-submit').click(function() {
					var qqs = $QQ.val().trim()
					if(qqs && !/^\d+$/.test(qqs)) {
						return alert('[提示]qq只能为数字！')
					}
					var formData = {
						qq: that.$QQ.val()
					};
					mobileManage.getLoader().open("修改中");
					$.post("/asp/updateQQ.aspx ", {qq: qqs},
						function() {
							mobileManage.getLoader().close();
							window.location.reload();
					})
				});
			}
		</script>
	</body>

</html>