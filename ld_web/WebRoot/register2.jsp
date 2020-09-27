<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.Constants" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<style>
			html, body {
				height: 100%;
			}
		</style>
		<link rel="stylesheet" href="css/new_user.css?v=13" />
	</head>
	<style>
		.reg-page .reg-form .col-6 {
			border: none;
		}
		.reg-page .form-ft {
			box-shadow: none;
		}
		.mull{
	    position: fixed;
	    top: 0px;
	    bottom: 0px;
	    background: rgba(0,0,0,.5);
	    width: 100%;
		height: 100%;
		z-index: 8;	
		display: none;	
		}
		.mas_show{
			width: 300px;
			height: 140px;
			background: #EEEEEE;
			position: absolute;
			top: 50%;
			left: 50%;
			margin-top: -70px;
			margin-left: -150px;
			z-index: 9;
			border-radius: 10px;
			display: none;
			color: #333;
		}
		.title_mag{
			height: 80px;
			line-height: 80px;
			text-align: center;
			font-size: 16px;
		}
		.msg_btn ul li{
			width: 50%;
			text-align: center;
			font-size: 16px;
			border-top: 1px solid #D1D1D1;
			float: left;
			height: 60px;
			line-height: 60px;
			cursor: pointer;
		}
	</style>

	<body class="user_new_body">
		<div class="reg-page">
			<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
			<s:url action="checkUserExsit" namespace="/asp" var="checkUserExsitUrl"></s:url>
			<s:url action="validateCodeForIndex" namespace="/asp" var="validateCodeForIndexUrl"></s:url>
			<s:url action="register" namespace="/asp" var="registerUrl"></s:url>
			<div class="user_new_top"></div>
			<div class="container">
				<div class="user_new_header">
					<div class="user_logo">
						<img src="images/register/new_user.png" />
					</div>
					<div class="w_1000">
						<div class="user_new_title">
							<span class="action">设置账户信息</span>
							<span></span>
							<span>恭喜！注册成功</span>
						</div>
						<div class="user_action">
							<ul>
								<li></li>
								<li></li>
							</ul>
							<div class="user_clover">
								<span class="action">1</span>
								<span></span>
								<span>2</span>
							</div>
						</div>
					</div>
				</div>
				<div class="h_978">
					<div class="user_new_table">
						<div class="user_new_table_box">
							<form id="registrationtable" name="registrationtable" method="post" class="reg-form ui-form">
								<div class="m-row">
									<div class="cell col-6 next_once">
										<%-- <div class="ui-form-item" style="display:none;">
					                <label for="" class="ui-label ">货币：</label>
					                <input class="ui-ipt" type="text" name="rmb" value="人民币" disabled="disabled"/>
					
					                <span class="ipt-tip">
					                  一旦注册货币将不能更换
					                </span>
					            </div>--%>
										<div class="ui-form-item">
											<label for="" class="ui-label ">用户名：</label>
											<input class="ui-ipt user_name" type="text" maxlength="10" name="loginname" id="loginnameId" data-rule-register-username="true" placeholder="6-10位小写字母与数字组合" />
											<span class="ipt-tip user_name_massage"><i class="yes_ok"></i>6-10位小写字母与数字组合</span>
										</div>
										<!--<div class="ui-form-item">
					                    <label for="" class="ui-label ">电子邮箱：</label>
					                    <input class="ui-ipt" type="text" name="email" id="email" maxlength="30" placeholder="请输入你的邮箱，建议@gmail.com" data-rule-email="true" required />
					
					                    <span class="ipt-tip email_massage"><i class="yes_ok"></i>输入正确邮箱，获取第一时间优惠通知一经注册，信息将无法进行修改</span>
					                </div>	-->
										<div class="ui-form-item">
											<label for="" class="ui-label ">手机号码：</label>
											<input class="ui-ipt" type="text" name="phone" id="phone" maxlength="11" placeholder="若申请优惠，我们须和本人进行电话核实" data-rule-register-phone="true" required/>

											<span class="ipt-tip phone_massage"><i class="yes_ok"></i>一经注册，信息将无法进行修改</span>
										</div>
										<div class="ui-form-item">
											<label for="" class="ui-label ">密码：</label>
											<input type="password" style="display: none;" />
											<input class="ui-ipt" type="password" maxlength="12" name="password" id="passwordRegister" placeholder="由8-12个数字或英文字母组成" data-rule-password2="true" data-rule-notequalto="true" required/>
											<span class="ipt-tip pass_word"><i class="yes_ok"></i>由8-12个数字或英文字母组成</span>
										</div>
										<div class="ui-form-item">
											<label for="" class="ui-label ">确认密码：</label>
											<input class="ui-ipt" type="password" name="confirm_password" maxlength="12" id="confirmPasswordRegister" placeholder="请再次填写密码" data-rule-equalTo="#passwordRegister" required />

											<span class="ipt-tip zacipass"><i class="yes_ok"></i>请再次填写密码</span>
										</div>

									</div>

									<div class="cell col-6">
										<!--<div class="ui-form-item">
					                    <label for="" class="ui-label ">姓名：</label>
					                    <input class="ui-ipt" type="text" name="accountName" id="accountName" maxlength="10" placeholder="与银行账户姓名相同，否则无法提款" data-rule-chinese-name="true" required/>
					
					                    <span class="ipt-tip xingming"><i class="yes_ok"></i>与银行账户姓名相同，否则无法提款</span>
					                </div>-->
										<%--  <div class="ui-form-item" style="display:none;">
					                    <label for="" class="ui-label ">昵称：</label>
					                    <input class="ui-ipt" type="text" name="aliasName" id="aliasName" maxlength="10" placeholder="请输入您的昵称"/>
					
					                    <span class="ipt-tip">
					                      10个以内的汉字、英文字母或数字
					                    </span>
					                </div>--%>

										<!--<div class="ui-form-item">
					                    <label for="" class="ui-label rq-label">生日：</label>
					                    <input class="ui-ipt" type="text" name="birthday" id="birthday" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" readonly placeholder="用于抽查是否年满18岁，发放生日礼金"/>
					
					                    <span class="ipt-tip">用于抽查是否年满18岁，发放生日礼金</span>
					                </div>
					                -->

										<div class="ui-form-item">
											<label for="" class="ui-label ">验证码：</label>
											<input style="width: 334px;" class="ui-ipt" type="text" name="validateCode" id="validateCode" placeholder="请输入验证码" required data-msg-required="请输入验证码" />
											<img id="validateCodeRegister" src="${validateCodeForIndexUrl}" title="如果看不清验证码，请点图片刷新" onclick="this.src='${validateCodeForIndexUrl}?r='+Math.random();" class="img_code" style="height: 38px;width: 92px; margin-top: -6px;" />
											<span class="ipt-tip">请输入验证码</span>
										</div>

										<%-- <div class="ui-form-item" style="display:none;">
					                    <label for="" class="ui-label">邀请码：</label>
					                    <input class="ui-ipt" type="text" name="intro" id="intro" value="" maxlength="50" placeholder="请输入邀请码"/>
					
					                    <span class="ipt-tip">
					                      可以不填
					                    </span>
					               </div>--%>
										<div class="form-ft">
											<div class="text">
												<p>温馨提醒：请确认您的注册信息，一经注册，信息将无法进行修改。</p>
												<input id="ipt-coupon-plant" type="checkbox" checked="checked" name="sms" required data-msg-required="请选择是否同意协议" />
												<label for="ipt-coupon-plant" class="check-box"></label> 我愿意接收
												<a target="_blank" href="/aboutus.jsp#tab-agreement" class="link c-blue">《用户协议》</a>和
												<a target="_blank" href="/aboutus.jsp#tab-privacy" class="link c-blue">《隐私条款》</a>

											</div>
											<div class="text-center mt20">
												<input type="submit" class="btn next_user j-next-submit" value="下一步" />
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>

						<div class="user_ok" style="display: none;">
							<img src="images/user/ok_user.jpg" />
							<div class="next_game">
								<a href="index.jsp">进入游戏</a>
							</div>
						</div>

					</div>
				</div>
			</div>	
		</div>
		<div class="mull">
			<div class="mas_show">
				<div class="title_mag">
					请先完善个人信息
				</div>
				<div class="msg_btn">
					<ul>
						<li style="border-right: 1px solid #D1D1D1;" id="quxiao">取消</li>
						<li><a href="/userManage.jsp?user-vip">确认</a></li>
					</ul>
				</div>
			</div>
		</div>
		
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		<script type='text/javascript' src="${ctx}/js/lib/jquery.validate.min.js"></script>
		<script type='text/javascript' src="${ctx}/js/lib/jquery.validate.config.js?v=1"></script>
		<!--<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>-->
		<script>
			$(function() {

				//验证密码，字母头同时为大小写字母和数字的密码
				$.validator.addMethod('notEqualTo', function(value, element) {
					var reg = $('#loginnameId').val();
					return this.optional(element) || value != reg;
				}, '密码不能和用户名相同');

				function chekObj(key, id) {
					return {
						url: '/asp/sjyz.aspx',
						type: 'post',
						data: {
							'sjType': key,
							'sjValue': function() {
								return $('#' + id).val();
							}
						},
						dataFilter: function(data) {
							if(data == '\"1\"') {
								return true;
							} else {
								return false;
							}
						}
					}
				}

				$('#registrationtable').validate({

					//          rules:{
					//              'loginname': {
					//                  required: true,
					//                  email   : false,
					//                  remote  : chekObj('yhm','loginnameId')
					//              },
					//
					//          },
					//          messages:{
					//              'loginname':{
					//                  remote: '用户名重复,请重新填写！'
					//              }
					//          },        

					onfocus: true,
					onkeyup: false,
					errorClass: "error-tip",
					errorPlacement: function(error, element) {
						error.appendTo(element.parent());
						element.siblings('.ipt-tip').hide();
					},
					submitHandler: function(form) {
						$(".easemobim-prompt-wrapper").show();
							$('.j-next-submit').attr('disabled','disabled');
						$.post('/asp/register.aspx', $(form).serialize(),
							function(data) {
								$(".easemobim-prompt-wrapper").hide();
								document.getElementById('validateCodeRegister').src = '${ctx}/asp/validateCodeForIndex.aspx?r=' + Math.random();
								if(data === "SUCCESS") {

									$(".user_action ul li").eq(1).addClass("action");
									$(".user_clover>span").eq(2).addClass("action");
									$(".user_new_table_box").hide();
									$(".user_ok").show();
									$(".mull").show();
									$(".mas_show").show();									
										return ;
									// window.location.href="${ctx}/";
									//                              window.parent.$('#modal-success').modal('show');
									//                              window.parent.$('#modal-reg').modal('hide');
								} else if(data == "帐号被禁用 :请联系客服!") {
									$(form).find('input').val('');
									alert(data);
								} else {

									$("#validateCodeRegister").val("");
									alert(data);
								}
								setTimeout(
								" $('.j-next-submit').removeAttr('disabled')  "
								,2500)
							})
					}
				})

				//      	if($("#loginnameId-error").is(":hidden") && $("#email-error").is(":hidden") && $("#phone-error").is(":hidden") && $("#passwordRegister-error").is(":hidden") &&$("#confirmPasswordRegister-error").is(":hidden")){
				//      		alert("OK")
				//      	}else{
				//      		alert("NO")
				//      	}

				$("#accountName").blur(function() {
					if($("#accountName").val() != "" || $("#birthday").val() != "") {
						$(".user_clover>span").eq(1).addClass("action");
						$(".user_action ul li").eq(0).addClass("action");
					} else {
						$(".user_clover>span").eq(1).removeClass("action");
						$(".user_action ul li").eq(0).removeClass("action");
					}

				})

				//					$(".user_name").focus(function(){
				//						if($(".user_name").siblings("#loginnameId-error").html()==undefined || $(".user_name").siblings("#loginnameId-error").html()==""){
				//							$(".user_name_massage").show()
				//						}else{
				//							$(".user_name_massage").hide()
				//						}
				//							
				//					})

				$(".user_name").blur(function() {
					if($(".user_name").siblings("#loginnameId-error").html() == undefined || $(".user_name").siblings("#loginnameId-error").html() == "") {
						if($(".user_name").val() != "") {
							$(".user_name_massage").show()
						} else {
							$(".user_name_massage").hide()
						}
					} else {
						$(".user_name_massage").hide()
					}
 
				})

				$("#email").blur(function() {
					if($("#email").siblings("#email-error").html() == undefined || $("#email").siblings("#email-error").html() == "") {
						if($("#email").val() != "") {
							$(".email_massage").show()
						} else {
							$(".email_massage").hide()
						}
					} else {
						$(".email_massage").hide()
					}

				})

				$("#phone").blur(function() {
					if($("#phone").siblings("#phone-error").html() == undefined || $("#phone").siblings("#phone-error").html() == "") {
						if($("#phone").val() != "") {
							$(".phone_massage").show()
						} else {
							$(".phone_massage").hide()
						}
					} else {
						$(".phone_massage").hide()
					}

				})

				$("#passwordRegister").blur(function() {
					if($("#passwordRegister").siblings("#passwordRegister-error").html() == undefined || $("#passwordRegister").siblings("#passwordRegister-error").html() == "") {
						if($("#passwordRegister").val() != "") {
							$(".pass_word").show()
						} else {
							$(".pass_word").hide()
						}
					} else {
						$(".pass_word").hide()
					}

				})

				$("#confirmPasswordRegister").blur(function() {
					if($("#confirmPasswordRegister").siblings("#confirmPasswordRegister-error").html() == undefined || $("#confirmPasswordRegister").siblings("#confirmPasswordRegister-error").html() == "") {
						if($("#confirmPasswordRegister").val() != "") {
							$(".zacipass").show()
						} else {
							$(".zacipass").hide()
						}
					} else {
						$(".zacipass").hide()
					}

				})

				$("#accountName").blur(function() {
					if($("#accountName").siblings("#accountName-error").html() == undefined || $("#accountName").siblings("#accountName-error").html() == "") {
						if($("#accountName").val() != "") {
							$(".xingming").show()
						} else {
							$(".xingming").hide()
						}
					} else {
						$(".xingming").hide()
					}

				})
				
				
				$(".mull").click(function(){
					$(".mull").hide();
					$(".mas_show").hide();
				})

				$("#quxiao").click(function(){
					$(".mull").hide();
					$(".mas_show").hide();
				})

			});
		</script>
	</body>

</html>