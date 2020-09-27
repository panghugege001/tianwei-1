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
		<jsp:include page="commons/back.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/forgotPassword.css?v=9" />
	
		<script type="text/javascript" src="mobile/js/ModeControl.js"></script>
		
	</head>
	<body>
		<div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">
				 <div class="pt40"></div>
				 <div class="tab-bd">
					<div id="page-index" class="tab-panel active" data-page-index="">
						<div class="list-group">
							<div class="list-item"><a data-toggle="tab" href="#find-sms-page">手机找回</a></div>
						</div>
						<div class="list-group">
							<div class="list-item"><a data-toggle="tab" href="#find-mail-page">邮箱找回</a></div>
						</div>
						<div class="list-group">
							<div class="list-item"><a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank" data-live-action="">联系客服</a></div>
						</div>
						<div class="list-group c-strong">注册手机和邮箱无法使用，请点击联系客服</div>	
					</div>
					<div class="tab-panel" id="find-sms-page">
						<form class="ui-form" id="form-sms">
							<div class="ui-input-row">
								<input name="account" type="text" class="ui-ipt" placeholder="请输入您的游戏账号" >
							</div>
							<div class="ui-input-row">
								<input name="phone" type="text" class="ui-ipt" placeholder="请输入您的注册电话号码">
							</div>
							<div class="ui-input-withbutton">
								<div class="ui-input-row">
									<input id="forgot-code1" placeholder="验证码" type="text" name="imageCode" class="ui-ipt">
								</div>
								<img class="btn w" id="forgot-image1" title="如果看不清验证码，请点图片刷新" src="${imgCode}" alt="">
							</div>
							<div class="ui-button-row center">
								<input type="submit" class="btn btn-block btn-login" value="找回密码">
							</div>
						</form>
					</div>
					<div class="tab-panel" id="find-mail-page">
						<form class="ui-form" id="form-email">
							<div class="ui-input-row">
								<input name="account" type="text" class="ui-ipt" placeholder="请输入您的游戏账号">
							</div>
							<div class="ui-input-row">
								<input type="text" name="email" class="ui-ipt" placeholder="请输入您的邮箱">
							</div>
							<div class="ui-input-withbutton">
								<div class="ui-input-row">
									<input id="forgot-code" placeholder="验证码" type="text" name="imageCode" class="ui-ipt">
								</div>
								<img class="btn w" id="forgot-image" title="如果看不清验证码，请点图片刷新" src="/mobi/mobileValidateCode.aspx" alt="">
							</div>
							<div class="ui-button-row center">
								<input type="submit" class="btn btn-block btn-login" value="找回密码">
							</div>				
						</form>
					</div>
				</div>
			</div>
			<div class="footer-margin"></div>
		</div>
		<jsp:include page="commons/footer1.jsp" />
		  
		<script type="text/javascript">
			headerBar.setTitle('忘记密码');
			   $(function () {
					new ForgotPasswordPage();
					 $(document).on('click','[data-live-action]',function () {
						mobileManage.redirect('online800');
					});
				});

    function ForgotPasswordPage(){
       var that=this;

       this.$code=$('#forgot-code');
        this.$code1=$('#forgot-code1');
       this.$image=$('#forgot-image');
       this.$formSms=$('#form-sms');
       this.$formEmail=$('#form-email');
        this.$image1=$('#forgot-image1');

       this.init=function(){
           that.$formSms.on('submit',function () {
               var formData=that.$formSms.serializeObject();

               mobileManage.getLoader().open("处理中");
               mobileManage.getUserManage().findPasswordByPhone(formData, function(result){
                   mobileManage.getLoader().close();
                   if(result.success){
                       alert(result.message);
                   }else{
                       that.$code1.val('');
                       that.$image1.attr('src',Global.config.imgCodeUrl +'?'+Math.random());
                       alert(result.message);
                   }
               });

			   return false;
           });

           that.$formEmail.on('submit',function () {
               var formData=that.$formEmail.serializeObject();

               mobileManage.getLoader().open("处理中");
               mobileManage.getUserManage().findPasswordByEmail(formData, function(result){
                   if(result.success){
                       alert(result.message);
                   }else{
                       alert(result.message);
                       that.$code.val('');
                       that.$image.attr('src',Global.config.imgCodeUrl +'?'+Math.random());
                   }
                   mobileManage.getLoader().close();
               });
			   return false;
           });

           that.$image.on('click',function () {
               that.$image.attr('src',Global.config.imgCodeUrl +'?'+Math.random());
           })
           that.$image1.on('click',function () {
               that.$image1.attr('src',Global.config.imgCodeUrl +'?'+Math.random());
           })
	   };

	   this.init();
	}
		</script>
		 
	</body>
</html>