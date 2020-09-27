<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE >
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户设置" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body class="myaccount-page">
		<div id="page-index" class="form-warp txt-form no-icon">
			<div class="form-group">
				<div class="form-label">用户名</div>
				<input type="text" class="form-control" value="<s:property value=" @dfh.utils.StringUtil@formatStar(#session.customer.loginname,0.7,1) "/>" disabled="" readonly>
			</div>
			<div class="form-group">
				<div class="form-label">真实姓名</div>
				<input type="text" id='accountName' class="form-control" 
					placeholder="请输入中文姓名"
				<s:property value='(#session.customer.accountName.trim()!=""?"disabled":"")' /> 
				value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1) " />">
			</div>
			<div class="form-group">
				<div class="form-label">联系电话</div>
				<input type="text" class="form-control" 
					placeholder="请输入联系电话"
					value="<s:property value=" @dfh.utils.StringUtil@formatStar(#session.customer.phone, 0.5,1) "/>" disabled="" readonly>
			</div>
			<div class="form-group">
				<div class="form-label">电子邮箱</div>
				<input type="text" id='email' class="form-control" 
					placeholder="请输入电子邮箱"
				<s:property value='(#session.customer.email.trim()!=""?"disabled":"")' /> 
				value='<s:property value="@dfh.utils.StringUtil@emailFormat(#session.customer.email)" />' />
			</div>
			<div class="form-group date-select">
				<div class="form-label">出生日期</div>
				<input id="birthday"  class="form-control" type="text"  readonly=""
					placeholder="请选择生日"
					<s:property value='(#session.customer.birthday.trim()!=""?"disabled":"")' />
					value='<fmt:formatDate value="${session.customer.birthday}" pattern="yyyy-MM-dd" />' />
			</div>
			<div class="form-group">
				<div class="form-label">QQ号码</div>
				<input id="qqx" type="text" placeholder="QQ号码" class="form-control" value="<s:property value=" @dfh.utils.StringUtil@formatStar(#session.customer.qq, 0.7,0) "/>"/>
				<div id='upqq' class="form-code">修改</div>
			</div>
			<div class="form-group">
				<div class="form-label">微信号</div>
				<input id="wx" type="text" placeholder="微信号" class="form-control" value='<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.microchannel,0.7,0)" />'>
				<div id='upwx' class="form-code">修改</div>
			</div>
			<c:if test="${session.customer.accountName==null || session.customer.email==null || session.customer.birthday ==null}">
				<div class="btn-submit block" id="account-submit">提交</div>
			</c:if>
		</div>
		<br />
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/lib/Mdate/Mdate.js"></script>
		<script type="text/javascript">
			if($("#birthday").val().trim() == '') {
                new LazyPicker("#birthday");
			}
			if('${session.customer.accountName}' == '') {
				$(".form-code").addClass('hidden')
			}
			var $QQ = $('#qqx');
			var $EMAIL = $('#email');
			var $BIRTHDAY = $('#birthday');
			var $WX = $('#wx');
			var $accountName = $("#accountName");

			var  QQValue = $QQ.val();
			var  WXValue = $WX.val();

			$('#account-submit').click(function() {
				var formData = {
					accountName: $accountName.val(),
					qq: $QQ.val(),
					email: $EMAIL.val(),
					birthday: $BIRTHDAY.val(),
					microchannel: $WX.val()
				};
				// if($QQ.val()==QQValue || (QQValue == '' && $QQ.val() =='')){
				// 	delete formData.qq
				// }
				// if($WX.val()==WXValue || (WXValue == '' && $WX.val() =='')){
				// 	delete formData.microchannel
				// }
				if($EMAIL.attr('disabled')=='disabled'){
					delete formData.email
				}
				if($BIRTHDAY.attr('disabled')=='disabled'){
					delete formData.birthday
				}
				if($accountName.attr('disabled')=='disabled'){
					delete formData.accountName
				}
				mobileManage.getLoader().open("修改中");
				mobileManage.getUserManage().modifyInfo(formData, function(result) {
					mobileManage.getLoader().close();
					if(result.success) {
						alert("提交成功！")
					} else {
						if(result.message){
							alert(result.message);
							return
						}else{
						alert("提交成功！");
						}
					}
					window.location.reload();
				});
			});
			$(document).ajaxError(function() {
				alert("请求错误,请稍后重试！")
				mobileManage.getLoader().close();
			})
			$("#upqq").click(function() {
				var qqs=$QQ.val().trim()
				if(qqs&&!/^\d+$/.test(qqs)){
					return alert('[提示]qq只能为数字！') 
				}
				mobileManage.getLoader().open("修改中");
				$.post("/asp/updateQQ.aspx ", {
						qq: qqs
					},
					function() {
						mobileManage.getLoader().close();
						window.location.reload();
					})
			})
			$("#upwx").click(function() {
				var wxs=$WX.val()
				if(!wxs){
					return alert('[提示]请输入微信号')
				}
				mobileManage.getLoader().open("修改中");
				$.post("/asp/updateWeiXin.aspx ", {
						microchannel:wxs
					},
					function(data) {
						mobileManage.getLoader().close();
						window.location.reload();
					}
				)
			})
		</script>
	</body>

</html>