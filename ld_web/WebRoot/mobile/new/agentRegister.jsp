<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<!DOCTYPE >
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="代理注册" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body>
		<title>天威</title>
		<div class="page-top">
			<div class="reg-step-num">
				<div class="progress-bar">
					<div class="progress-length"></div>
					<div class="step step0 active"><span>账号设置</span></div>
					<div class="step step1">资料填写</div>
					<div class="step step2"><span>注册成功</span></div>
				</div>
			</div>
		</div>
		<div class="form-warp reg-step-page">
			<div class="step-one">
				<div class="form-group icon-account">
					<input id="agent-account" data-pattern="^a\_[a-z0-9]{1,13}$" type="text" class="form-control" required placeholder="请输入代理账户">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">以a_开头，由3-15个数字或英文母<span class="c-red" id='check-account'></span></div>
				<div class="form-group icon-pwd">
					<input id="agent-password" data-pattern="^[a-zA-Z][a-zA-Z0-9]{5,15}$" type="password" class="form-control" required placeholder="请输入登入密码：">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">字母开头,6-16位数字或字母,且不能和账号相同</div>
				<div class="form-group icon-pwd">
					<input id="agent-confirmPassword"  type="password" class="form-control" required placeholder="请输入确认密码">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">再次填写登录密码</div>

				<div class="form-group icon-name">
					<input id="agent-name" data-pattern="^[\u4e00-\u9fa5,\·]+$" type="text" required class="form-control" placeholder="请输入中文真实姓名">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">中文姓名.必须与您的银行帐户名称相同，否则不能出款</div>
				<div class="form-group icon-phones">
					<input id="agent-phone" data-pattern="^1[3,4,5,7,8]\d{9}$" maxlength="11" type="text" placeholder="常用联系电话" required class="form-control">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">输入你的常用联系电话</div>
				<button disabled="" id='next-btn' class="btn-submit">下一步</button>
				<br />
			</div>

			<div class="step-two hidden">
				<div class="form-group icon-yumao iconfont">
					<input id="agent-url" type="text" data-pattern="^[0-9a-zA-Z]+$" required placeholder="推广网址" class="form-control">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">填写2-6个数字或者是字母,您用来推广的网址</div>
				<div class="form-group icon-email">
					<input id="agent-email" type="text" data-pattern="^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$" placeholder="常用联系邮箱" required class="form-control">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">请输入你的邮箱，建议@google.com邮箱</div>

				<div class="form-group  icon-iconqq iconfont">
					<input id="agent-qq" placeholder="QQ" data-pattern="\S+" type="text" required class="form-control">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">玩家注册时必须填写QQ或SWpe帐号</div>
				<div class="form-group icon-wechats">
					<input id="agent-wechat" data-pattern="^\w{0,19}$" placeholder="微信号" type="text" required class="form-control">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">玩家注册时必须填写微信</div>

				<div class="form-group icon-agent iconfont">
					<input id="agent-partner" placeholder="代理推荐码" type="text" class="form-control">
					<div class="ipt-clear undone"></div>
				</div>
				<div class="form-group icon-code">
					<input id="agent-code" class="form-control" type="text" maxlength="4" placeholder="请输入验证码">
					<img id="agent-image" class="form-code" title="如果看不清验证码，请点图片刷新" src="${imgCode}" />
					<div class="ipt-clear undone"></div>
				</div>
				<div class="control-msg">如果看不清验证码，请点图片刷新</div>
				<div id="agent-submit" class="btn-submit">提交</div>
				<div class="agree">
					<input id="agent-check1" type="checkbox">同意接受
					<a href="${ctx}/down/AgentAgreement-2017.doc">
						<font color="red">《天威娱乐城合作协议》</font>
					</a>。
				</div>
				<br /><br />
			</div>
			<div class="regiseter-success reg-step-page step-three">
				<div class="reg-info">
					<div class="page-top" style="background: none;">
						<img src="/mobile/img/icon/success.png" alt="" /><br /> 恭喜您，注册成功！
					</div>
					<div class="title">
						<div class="item fl"><img src="/mobile/img/icon/leftnav1.png" alt="">账户名：<span class="c-ylow" id='done-account'>longfu123</span></div>
						<div class="item fr"><img src="/mobile/img/icon/history2.png" alt="">余额：<span class="c-ylow">0.00</span></div>
					</div>
					<div class="tips">感谢您申请天威代理账户，我们的市场专员会24小时内 与您联系激活账户。
					</div>
					<div id="" class="btn-warp-x" style="overflow: hidden;">
						<div class="item fl">
							<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19">联系代理专员</a>
						</div>
						<div class="item fr">
							<a href="/mobile/new/index.jsp" class="other">我知道了</a>
						</div>
					</div>
				</div>
			</div>

		</div>

		<!--<script type="text/javascript" src="/mobile/js/lib/mui-0.2.1/mui.min.js?v=1210"></script>
		<script src="/mobile/js/MUIModel.js"></script>-->
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
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
			$image.click(function() {
				$image.attr('src', '${imgCode}?' + Math.random() + '');
			});
			$(".step-one input").blur(function() {
				setTimeout(function() {
					if($('.step-one .control-msg:visible').length == 0) {
						$('#next-btn').removeAttr("disabled")
					}
				})
			})
			$confirmPassword.blur(function(){
				if($password.val()==$confirmPassword.val()) {
					$(this).parent().next('.control-msg').hide()
					$(this).next('.ipt-clear').show().addClass("done").removeClass('undone')
				}else{
					$(this).parent().next('.control-msg').show();
					$(this).next('.ipt-clear').show().addClass("undone").removeClass('done')
				}
			})
			$('#next-btn').click(function() {
				if($('.step-one .control-msg:visible').length > 0) {
					return;
				}
				mobileManage.getLoader().open('验证中');
				//检查帐号是否已经注册
				$.post('/asp/sjyz.aspx', {
					sjType: 'yhm',
					sjValue: $account.val()
				}, function(data) {
					mobileManage.getLoader().close();
					if(data != '1') {
						alert(data)
					} else {
						$("#done-account").text($account.val())
						$('.step-one,.step-two').toggle();
						$(".step1").addClass('active');
						$('.progress-length').css('width', '50%')
					}
				})
			})

			//注册
			$('#agent-submit').click(function() {
				if(!$check1.is(":checked")){
					return alert("请先同意天威协议");
				}
				var formData = {
					account: $account.val(),
					password: $password.val(),
					confirmPassword: $confirmPassword.val(),
					name: $name.val(),
					email: $email.val(),
					url: $url.val(),
					qq: $qq.val(),
					phone: $phone.val(),
					wechat: $wechat.val(),
					partner: $partner.val(),
					check1: $check1.prop('checked') ? 1 : 0,
					imageCode: $imageCode.val()
				};
				mobileManage.getLoader().open('注册中');
				mobileManage.getUserManage().registerAgent(formData, function(result) {
					mobileManage.getLoader().close();
					if(result.success || result.message.indexOf('代理帐号注册成功') > -1) {
						$('.step-two,.step-three').toggle();
						$(".step2").addClass('active');
						$('.progress-length').css('width', '100%');
					} else {
						$imageCode.val('');
						$image.attr('src', '${imgCode}?' + Math.random());
						alert(result.message);
					}
				});
			});
		</script>

	</body>

</html>