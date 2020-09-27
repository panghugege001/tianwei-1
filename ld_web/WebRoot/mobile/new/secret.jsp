<%@page import="dfh.utils.StringUtil"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	if(session.getAttribute(Constants.SESSION_CUSTOMERID)==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="设置密保问题" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<form id="form-question" class="form-warp no-icon">
			<div class="form-tips">密保问题</div>
			<div class="form-group zf-sele">
				<select name="" id="question-question" class="form-control">
					<option value="">请选择</option>
				</select>
			</div>
			<div class="form-tips">您的答案</div>
			<div class="form-group">

				<input name="answer" type="text" class="form-control" required/>
			</div>
			<div class="form-tips hidden">登录密码</div>
			<div class="form-group">
				<input name="password" type="password" value="" class="form-control" maxlength="20" required/>
			</div>
			<input class="btn-submit block" type="submit" id="question-submit" value="设置">
		</form>
		<script>
			QuestionPage();
			function QuestionPage() {

				var that = this;

				var $form = $('#form-question');
				var $submitBtn = $form.find(':submit');

				var questionData = [{
						value: '1',
						name: '您最喜欢的明星名字？'
					},
					{
						value: '2',
						name: '您最喜欢的职业？'
					},
					{
						value: '3',
						name: '您最喜欢的城市名称？'
					},
					{
						value: '4',
						name: '对您影响最大的人名字是？'
					},
					{
						value: '5',
						name: '您就读的小学名称？'
					},
					{
						value: '6',
						name: '您最熟悉的童年好友名字是？'
					}
				];

				that._init = function() {
					//来原账户下拉
					var question_input = $('#question-question')
					$.each(questionData, function(i, item) {
						question_input.append('<option value="' + item.value + '">' + item.name + '</option>')
					});
					$form.submit(function() {
						/*var formData = {
						password:_$questionModel.$password.val(),
						answer:_$questionModel.$answer.val(),
						questionId:_$questionModel.$question.val()
						};*/
						var formData = $form.serializeObject();
						formData['questionId'] = question_input.val();
						mobileManage.getLoader().open("设置中");
						mobileManage.getUserManage().saveQuestion(formData, function(result) {
							mobileManage.getLoader().close();
							if(result.success) {
								alert(result.message);
								window.header.backFun();
							} else {
								alert(result.message);
							}
						});
						return false;
					});
				};

				that._init();
			}
		</script>
	</body>

</html>