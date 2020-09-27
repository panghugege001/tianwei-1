<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	//禁止访问
// 	response.sendRedirect(request.getContextPath()+"/mobile/");

// 	if(session.getAttribute(Constants.mobileDeviceID)==null){
// 		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
// 	}
String cpuid=(String) request.getSession(true).getValue("cpuid");
%>
<!DOCTYPE >
<html>
<head>
	<jsp:include page="commons/back.jsp" />
	<link rel="stylesheet" type="text/css" href="mobile/css/register.css?v=9" />
	
	 
	
</head>
<body>
	<div class="main-wrap">
		<div class="header-margin"></div>
		 <div class="content">
			<div class="pt40"></div> 
			<div class="ui-form	 agent-reg">
				<div class="ui-input-row">
					<label class="ui-label">代理账户：</label>	
					<input id="agent-account" type="text" class="ui-ipt"  required  placeholder="请输入代理账户">
				</div>
				<div class="message">以a_开头，由3-15个数字或英文母</div>
				<div class="ui-input-row">
					<label class="ui-label">登入密码：</label>	
					<input id="agent-password" type="password" class="ui-ipt"  required placeholder="请输入登入密码：">
				</div>
				<div class="message">密码为6-16位数字或英文字母，英文字母开头且不能和账号相同</div>
				<div class="ui-input-row">
					<label class="ui-label">确认密码：</label>	
					<input id="agent-confirmPassword" type="password" class="ui-ipt"  required placeholder="请输入确认密码">
				</div>
				<div class="message">再次填写登录密码</div>
				<div class="ui-input-row">
					<label class="ui-label">真实姓名：</label>	
					<input id="agent-name" type="text" required class="ui-ipt" placeholder="请输入代理账户">
				</div>
				<div class="message">必须与您的银行帐户名称相同，否则不能出款</div>
				<div class="ui-input-row">
					<label class="ui-label">代理网址：</label>	
					<input id="agent-url" type="text" required class="ui-ipt" >
				</div>
				<div class="message">填写2-6个数字或者是字母,您用来推广的网址</div>
				<div class="ui-input-row">
					<label class="ui-label">电子邮箱：</label>	
					<input id="agent-email" type="text" required class="ui-ipt" >
				</div>
				<div class="message">请输入你的邮箱，建议@google.com邮箱</div>
				<div class="ui-input-row">
					<label class="ui-label">联系电话：</label>	
					<input id="agent-phone" type="text" required class="ui-ipt" >
				</div>
				<div class="message">输入你的常用联系电话</div>
				<div class="ui-input-row">
					<label class="ui-label">QQ/SWpe：</label>	
					<input id="agent-qq" type="text" required class="ui-ipt" >	
				</div>
				<div class="message">玩家注册时必须填写QQ或SWpe帐号</div>
				<div class="ui-input-row">
					<label class="ui-label">微信号：</label>	
					<input id="agent-wechat" type="text" required class="ui-ipt" >
				</div>
				<div class="ui-input-row">
					<label class="ui-label">代理推荐码：</label>	
					<input id="agent-partner" type="text" class="ui-ipt" >
				</div>
					<div class="ui-input-row ui-input-row1"> 
					<label class="ui-label ui-label1">验证码：</label>	
					<div class="ui-input-row" style="margin:0;padding:0">
						<input id="agent-code" class="ui-ipt" type="text" maxlength="4" placeholder="请输入验证码">
					</div>
					<img id="agent-image" title="如果看不清验证码，请点图片刷新" class="security-img w btn"  src="${imgCode}"/>
				</div>
				<div class="message">如果看不清验证码，请点图片刷新</div>
				<div class="agree"> <input id="agent-check1" type="checkbox"> 本人已经超过合法年龄以及本人在此网站的所有活动并没有抵触本人所身在的国家所管辖的法律，
						同时接受 <a href="${ctx}/down/AgentAgreement-2017.doc"><font color="red">《天威娱乐城合作协议》</font></a>。

				</div>
				<div class="ui-button-row center">
				<div id="agent-submit" class="block btn-login">注册</div>
				</div>
			</div>
		 </div>	
	 
		<div class="footer-margin"></div>
	</div>

	<jsp:include page="commons/footer1.jsp" />
	<script type="text/javascript">

		headerBar.setTitle('会员注册');
		var $account = $('#agent-account');
		var $password = $('#agent-password');
		var $confirmPassword = $('#agent-confirmPassword');
		var $name = $('#agent-name');
		var $url = $('#agent-url');
		var $email = $('#agent-email');
		var $phone = $('#agent-phone');
		var $qq = $('#agent-qq');
		var $wechat = $('#agent-wechat');
		var $partner = $('#agent-partner');
		var $imageCode = $('#agent-code');
		var $check1 = $('#agent-check1');
		var $image = $('#agent-image');
		 
		 //刷新验证码
		$image.click(function(){
			$image.attr('src','${imgCode}?'+Math.random()+'');
		});
		
		//注册
		$('#agent-submit').click(function(){
			mobileManage.getModel().open('confirm',[{
				title:'确定注册代理帐户？',
				message:['<span class="h3">温馨提示：</span><br>',
				         '<p style="text-indent: 2em;">此账号为合营账号，主要用于佣金提款及代理线相应数据查询，不可进行游戏、存款及领取体验金等相关操作！如有任何疑问请联系市场部咨询,谢谢！</p>'].join(''),
				callback:function(confirm){
					if(!confirm)return;
					
					var formData = {
						account:$account.val(),
						password:$password.val(),
						confirmPassword:$confirmPassword.val(),
						name:$name.val(),
						email:$email.val(),
						url:$url.val(),
						qq:$qq.val(),
						phone:$phone.val(),
						wechat:$wechat.val(),
						partner:$partner.val(),
						check1:$check1.prop('checked')?1:0,
						imageCode:$imageCode.val()
					};
					mobileManage.getLoader().open('注册中');
					mobileManage.getUserManage().registerAgent(formData, function(result){
						if(result.success){
							alert(result.message);
							mobileManage.redirect('index');
						}else{
							mobileManage.getLoader().close();
							$imageCode.val('');
							$image.attr('src','${imgCode}?'+Math.random());
							alert(result.message);
						}
					});
				}
			}]);
		});
	</script>
	 
</body>
</html>