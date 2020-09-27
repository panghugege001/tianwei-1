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
			<form class="ui-form" id="j-login-form">
				<div class="ui-input-row rq-value">
					<input type="text" class="ui-ipt" id="login-account" placeholder="请输入您的账号" value="">
				</div>
				<div class="ui-input-row rq-value">
					<input type="password" class="ui-ipt" id="login-password" placeholder="请输入您的密码" value="">
					<a href="mobile/forgotPassword.jsp" class="c-strong a-link for">忘记密码?</a>
				</div>

				<%--<div class="ui-input-withbutton">
					<div class="ui-input-row">
						<input type="text" class="ui-ipt" id="login-code" placeholder="请输入您的验证码">
					</div>
					<img id="login-img" src="/mobi/mobileValidateCode.aspx" class="w btn" alt="">
				</div>--%>

				<div class="c-red center" id="error-message"></div>

				<div class="ui-button-row center ui-button-row1">
					<a href="mobile/register.jsp" class="btn btn-reg a-link" type="submit" >新用户注册</a>
				</div>
				<div class="ui-button-row center">
					<input class="btn-login" type="submit" value="登录">
				</div>
				 
			</form>
		</div>
		 
	</div>

	<jsp:include page="commons/footer1.jsp" />
	
	<%--<script>
		window.mobileManage = new MobileManage('/','/mobi/mobileValidateCode.aspx');
	</script>--%>
	<script>
		headerBar.setTitle('登录');
		$(function(){
			$('#j-login-form').submit(function() {
				var $account=$('#login-account'),
					$password=$('#login-password'),
					//$code=$('#login-code'),
					$submitBtn=$(this).find(':submit'),
					//$image=$('#login-img'),
					$errorMessage=$('#error-message');

				//$image.attr('src', mobileManage.getSecurityCodeUrl() + '?' + Math.random());

				/*$image.click(function(){
					$image.attr('src',mobileManage.getSecurityCodeUrl()+'?'+Math.random()+'');
				});*/

				var formData = {
					account: $account.val(),
					password: $password.val()
					/*imageCode: $code.val()*/
				};
				var text= mobileManage.disabledBtn($submitBtn);
				mobileManage.getUserManage().login(formData, function(result) {
					if (result.success) {
						mobileManage.redirect('index');
					} else {
						mobileManage.enabledBtn($submitBtn,text);
						/*$code.val('');
						$image.attr('src', mobileManage.getSecurityCodeUrl() + '?' + Math.random());*/
						$errorMessage.html(result.message);
					}
				});

				return false; // return false to cancel form action
			});
		});
	</script>
	 
	<!-- <script language="javascript" src="https://mpsnare.iesnare.com/snare.js"></script> -->
</body>
</html>