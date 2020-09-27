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
	    $(document).ready(function () {
	    	var cpuVal = '<%=cpuid %>';
		    if(typeof(cpuVal)=="undefined"||cpuVal==null||''==cpuVal||cpuVal=='null'||!cpuVal||cpuVal.length<1){
		        var ioBBVal = $("#ioBB").val();
		        $.post("${ctx}/asp/addcpuid.aspx", {"cpuid":ioBBVal
		        }, function (returnedData, status) {
		            if ("success" == status) {}
		        });
		    }
	    });
	</script>
	
</head>
<body>
	<div class="main-wrap">
		<div class="header-margin"></div>
		<div class="content">
			<div class="pt40"></div> 		
			<div id="register-model" class="ui-form">
				<div class="ui-row center c-red"style="background:none">【*为必填项目】 </div>
				<div class="ui-input-row">
					<label class="ui-label"><span class="c-red">*&nbsp;</span>账号：</label>	
     		    	<input id="register-account" type="text" maxlength="10" class="ui-ipt" required  placeholder="由4-10个数字或英文字母组成"> 
     		 	</div>
     		  	<div class="ui-input-row">
					<label class="ui-label"><span class="c-red">*&nbsp;</span>密码：</label>	   	
     		  	  	<input id="register-password" type="password" class="ui-ipt" maxlength="16" required placeholder="密码为6-12位数字或英文字母，英文字母开头">
     		  	</div>
     		  	<div class="ui-input-row">
					<label class="ui-label"><span class="c-red">*&nbsp;</span>确认密码：</label>	   	   	
     		  	  	<input id="register-confirmPassword" type="password" class="ui-ipt" maxlength="16" required  placeholder="密码为6-12位数字或英文字母，英文字母开头">
     		  	</div>
				<div class="ui-input-row">
					<label class="ui-label"><span class="c-red">*&nbsp;</span>姓名：</label>	   	   	
     		  	  	<input id="register-name" class="ui-ipt" type="text" required placeholder="必须与银行账户姓名相同，否则不能提款成功">
     		  	</div> 
				<div class="ui-input-row">
					<label class="ui-label"><span class="c-red">*&nbsp;</span>电话号码：</label>	 	
     		    	<input id="register-phone" type="text" maxlength="11" class="ui-ipt" required placeholder="若申请优惠，我们须和你本人进行电话核实">
					<div class="ui-tip"></div>
     		 	</div> 
				<div class="ui-input-row">
					<label class="ui-label"><span class="c-red">*&nbsp;</span>电子邮箱：</label>		
     		    	<input id="register-email" type="text" class="ui-ipt" required placeholder="建议@gmail.com邮箱">
     		 	</div>
				<div class="ui-input-row date-select">  
					<label class="ui-label">生日：</label>
					<input id="register-birthday"  class="ui-ipt" type="text" readonly >	   	
     		  	</div>
				<div class="ui-input-row">
					<label class="ui-label">微信：</label>	 	
     		    	<input id="register-wechat" type="text" maxlength="30"  class="ui-ipt" required >
     		 	</div>
				<div class="ui-input-row">
					<label class="ui-label">QQ：</label>	 	
     		    	<input id="register-qq" type="text" class="ui-ipt" required maxlength="20">
     		 	</div>
				
     		  	<div class="ui-input-row ui-input-row1"> 
					<label class="ui-label ui-label1"><span class="c-red">*&nbsp;</span>验证码：</label>
					<div class="ui-input-row" style="margin:0;padding:0">
     		  	  		<input id="register-code" type="text" maxlength="4" class="ui-ipt" required placeholder="请输入您的验证码">
     		    	</div>
     		    	<img id="register-img" src="${imgCode}" class="w btn"/>
     		    	 
     		  	</div>
     		  	<div class="agree"><input id="register-check1" type="checkbox"> 我要接收会员通讯及最新优惠计划 (同一台电脑或同一姓名重复注册的账户将不享有首存及体验金等活动)		 
				   <p class="center c-strong">注册后资料无法修改（真实姓名、邮箱、电话）</p>
				</div>
				<div class="ui-button-row center">
					<div class="block btn-login" id="register-submit">立即注册</div>
				</div>
			</div>
		</div>
		<div style="display:none;">
	        <input class="text medium" tabindex="2" TYPE="text" NAME="ioBB" id="ioBB" width="100" readonly>
	        <input class="text medium" TYPE="text" NAME="browsertype" id="browsertype" readonly>
	    </div>
		<div class="footer-margin"></div>
	</div>

	<jsp:include page="commons/footer1.jsp" />
	<script type="text/javascript">

		headerBar.setTitle('会员注册');
	
		$('.date-select').click(function(){
	    	mui.datepicker.open($(this).find('input'));
		});
		$('.date-select').append('<span class="date-icon flaticon-weekly3"></span>');
		
		var $account = $('#register-account');
		var $password = $('#register-password');
		var $confirmPassword = $('#register-confirmPassword');
		var $name = $('#register-name');
		var $email = $('#register-email');
		var $phone = $('#register-phone');
		var $qq = $('#register-qq');
		var $wechat = $('#register-wechat');
		var $imageCode = $('#register-code');
		var $image = $('#register-img');
		var $birthDate =  $('#register-birthday');

		//设定只能输入数字
		NumberInput('register-phone');
		
		
		//刷新验证码
		$image.click(function(){
			$image.attr('src','${imgCode}?'+Math.random()+'');
		});
		
		//注册
		$('#register-submit').click(function(){
			if(!$('#register-check1').prop('checked')){
				alert('[提示]请勾选同意计划！');
				return;
			}
			
			var formData = {
				account:$account.val(),
				password:$password.val(),
				confirmPassword:$confirmPassword.val(),
				name:$name.val(),
				email:$email.val(),
				birthDate:$birthDate.val(),
				phone:$phone.val(),
				qq:$qq.val(),
				wechat:$wechat.val(),
				imageCode:$imageCode.val()
			};
			mobileManage.getLoader().open('资料验证中');
			mobileManage.getUserManage().register(formData, function(result){
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
		});
		
		//Enter 提交
		$("#register-model").bind("keyup",function(e){  
            if(e.which=='13'&&$('input').is(":focus")){
            	$('#register-submit').click();
            }
        }); 
		
	 
		
	</script>
	<!-- <script language="javascript" src="https://mpsnare.iesnare.com/snare.js"></script> -->
</body>
</html>