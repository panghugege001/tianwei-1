<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="设置" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="list-warp">
			<div class="list-group">
				<a class="list-item" href="/mobile/new/myaccount.jsp"><img src="/mobile/img/icon/history8.png" alt="" />基本信息<i class="iconfont fr icon-downjiantou"></i></a>
			</div>
			<div class="list-group">
				<a class="list-item" href="/mobile/new/upPassword.jsp"><img src="/mobile/img/icon/history1.png" alt="" />修改密码<i class="iconfont fr icon-downjiantou"></i></a>
			</div>
			<div class="list-group">
				<a class="list-item" href="/mobile/new/upPayPassword.jsp"><img src="/mobile/img/icon/history3.png" alt="" />设置提款密码<i class="iconfont fr icon-downjiantou"></i></a>
			</div>
			<div class="list-group">
				<a class="list-item" href="/mobile/new/mybank.jsp"><img src="/mobile/img/icon/bank.png" alt="" />银行卡信息<i class="iconfont fr icon-downjiantou"></i></a>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script> 
			//查询密保问题
			var questionNames = {
				'1': true,
				'2': true,
				'3': true,
				'4': true,
				'5': true,
				'6': true,
			};

			mobileManage.getUserManage().getQuestion(function(result) {
				if(result.success && questionNames[result.data.questionId]) {
					$('#account-question').val('已设置');
					$('#account-question-button').hide();
				} else {
					$('#account-question').val('尚未设置');
					$('#account-question-button').show();
				}
			});
		</script>
	</body>

</html>