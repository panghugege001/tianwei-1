<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="会员注册" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>
	<body>
		<div id="register-model" class="form-warp">
			<div class="form-group icon-account">
				<input id="register-account" data-pattern="^[a-z][a-z0-9_]{6,10}" type="text" maxlength="10" class="form-control" required placeholder="由6-10个数字或英文字母组成">
				<div class="ipt-clear undone"></div>
			</div>
			<div class="form-group icon-pwd">
				<input id="register-password" type="password" data-pattern="^[a-zA-Z0-9]{6,16}$" class="form-control" maxlength="16" required placeholder="密码为6-16位数字或英文字母，英文字母开头">
				<div class="ipt-clear undone"></div>
			</div>
			<div class="form-group  icon-pwd">
				<input id="register-confirmPassword"  type="password" class="form-control" maxlength="16"
					 required placeholder="确认密码">
				<div class="ipt-clear undone"></div>
			</div>
			<!--<div class="form-group icon-name">
				<input id="register-name" class="form-control" data-pattern="^[\u4e00-\u9fa5]+$" type="text" required placeholder="必须与银行账户姓名相同，否则不能提款成功">
				<div class="ipt-clear undone"></div>
			</div>-->
			<div class="form-group icon-phones">
				<input id="register-phone" type="text" data-pattern="\S+" maxlength="11" class="form-control" required placeholder="若申请优惠，我们须和你本人进行电话核实">
				<div class="ipt-clear undone"></div>
				<div class="ui-tip"></div>
			</div>
			<!--<div class="form-group icon-email">
				<input id="register-email" type="text" data-pattern="^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$" class="form-control" required placeholder="建议@gmail.com邮箱">
				<div class="ipt-clear undone"></div>
			</div>-->
			<!--<div class="form-group date-select icon-rili iconfont">
				<input id="register-birthday" class="form-control" type="text" readonly placeholder="生日">
				<div class="ipt-clear"></div>
			</div>-->
			<!--<div class="form-group icon-wechats">
				<input id="register-wechat" type="text" maxlength="30" class="form-control" placeholder="微信号码" required>
				<div class="ipt-clear"></div>
			</div>
			<div class="form-group  icon-iconqq iconfont">
				<input id="register-qq" type="text" class="form-control" required maxlength="20" placeholder="QQ号码">
				<div class="ipt-clear"></div>
			</div>-->
			<div class="form-group icon-code">
				<input id="register-code" data-pattern="\S+" type="text" maxlength="4" class="form-control" required placeholder="请输入您的验证码">
				<img id="register-img" src="${imgCode}" class="form-code" />
			</div>

			<div class="msg-tips">
				*手机号一经注册将无法更改。
			</div>
			<div class=" btn-submit" id="register-submit">立即注册</div>
			<div class="agree">
				<input id="register-check1" type="checkbox"> 我要接收会员通讯及最新优惠计划 (同一台电脑或同一姓名重复注册的账户将不享有首存及体验金等活动)
			</div>
		</div>

		<div style="display:none;">
			<input class="text medium" tabindex="2" TYPE="text" NAME="ioBB" id="ioBB" width="100" disabled="" readonly>
			<input class="text medium" TYPE="text" NAME="browsertype" id="browsertype" disabled="" readonly>
		</div>

		<div class="regiseter-success">
			<div class="page-top">
				<img src="/mobile/img/icon/success.png" alt="" /><br /> 恭喜您，注册成功！
			</div>
			<div class="reg-info">
				<div class="title">
					<div class="item fl"><img src="/mobile/img/icon/leftnav1.png" alt="" />账户名：<span class="c-ylow" id='xxx-account'>longfu123</span></div>
					<div class="item fr"><img src="/mobile/img/icon/history2.png" alt="" />余额：<span class="c-ylow">0.00</span></div>
				</div>
				<div class="btn-warp-x">
					<div class="item fl">
						<a href="/mobile/new/myaccount.jsp">完善资料</a>
					</div>
					<div class="item fr">
						<a  href="javascript:void(0)" class="other" id='deposiet'>立即存款</a>
					</div>
				</div>
			</div>
			<a href="/mobile/new/download.jsp" class="go">
				<img src="/mobile/img/go.png" alt="" />
			</a>
		</div>

		<br />
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/UserManage.js?v=1021"></script>
		<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1210"></script>
		<script type="text/javascript">
			//io_bbout_element_id should refer to the hidden field in your form that contains the blackbox
			var fp_bbout_element_id = 'fpBB';
			var io_bbout_element_id = 'ioBB';
			// io_install_stm indicates whether the ActiveX object should be downloaded. The io_stm_cab_url
			// should reference your signed local copy of the ActiveX object
			var io_install_stm = false;
			var io_exclude_stm = 12; // don't run ActiveX on any platform if it is already installed (to avoid security warnings)
			// make sure you change the cab URL to the location of your signed copy before releasing
			//var io_stm_cab_url               ="http://www.reallybigcasino.com/downloads/StmOCX.cab";
			// uncomment any of the below to signal an error when ActiveX or Flash is not present
			//var io_install_stm_error_handler = "";
			var io_install_flash = false;
			//var io_flash_needs_update_handler = "";
			var io_enable_rip = true; // enable Real IP collection
			$(document).ready(function() {
				var cpuVal = $('#cpuid').val()
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
		<script type="text/javascript">

			$('.date-select').append('<span class="date-icon flaticon-weekly3"></span>');
			var $account = $('#register-account');
			var $password = $('#register-password');
			var $confirmPassword = $('#register-confirmPassword');
//			var $name = $('#register-name');
//			var $email = $('#register-email');
			var $phone = $('#register-phone');
//			var $qq = $('#register-qq');
//			var $wechat = $('#register-wechat');
			var $imageCode = $('#register-code');
			var $image = $('#register-img');
//			var $birthDate = $('#register-birthday');
			//设定只能输入数字
			NumberInput('register-phone');
			
			
			$confirmPassword.blur(function(){
				if($confirmPassword.val()!=''&&$password.val()==$confirmPassword.val()) {
					$(this).next('.ipt-clear').show().addClass("done").removeClass('undone')
				}else{
					$(this).next('.ipt-clear').show().addClass("undone").removeClass('done')
				}
			})
			//刷新验证码
			$image.click(function() {
				$image.attr('src', '${imgCode}?' + Math.random() + '');
			});
			//注册
			$('#register-submit').click(function() {
				if(!$('#register-check1').prop('checked')) {
					alert('[提示]请勾选同意计划！');
					return;
				}

				var formData = {
					account: $account.val(),
					password: $password.val(),
					confirmPassword: $confirmPassword.val(),
//					name: $name.val(),
//					email: $email.val(),
//					birthDate: $birthDate.val(),
					phone: $phone.val(),
//					qq: $qq.val(),
//					wechat: $wechat.val(),
					imageCode: $imageCode.val()
				};
				mobileManage.getLoader().open('资料验证中');
				mobileManage.getUserManage().register(formData, function(result) {
					if(result.success) {
						$("#xxx-account").text(formData.account);
						mobileManage.getLoader().close()
						//alert(result.message);
						$("#register-model,.regiseter-success").toggle();
						//mobileManage.redirect('index');
					} else {
						mobileManage.getLoader().close();
						$imageCode.val('');
						$image.attr('src', '${imgCode}?' + Math.random());
						alert(result.message);
					}
				});
			});

			//Enter 提交
			$("#register-model").bind("keyup", function(e) {
				if(e.which == '13' && $('input').is(":focus")) {
					$('#register-submit').click();
				}
			});
		</script>
		<script>
			$('#deposiet').click(function() {
					layer.open({
						content: '请先完善个人信息!',
						btn: ['<span class="bule">确认</span>', '<span class="orange">取消</span>'],
						yes: function(index) {
							window.location.href = '/mobile/new/myaccount.jsp'
							layer.close(index);
						}
					});
					return false;
			})
		</script>
				<script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>

	</body>

</html>