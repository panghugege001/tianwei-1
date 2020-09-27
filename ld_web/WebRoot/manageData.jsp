<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.utils.Constants"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/user.css?v=1116" />
	</head>

	<body class="user_body">

		<div class="index-bg about-bj">
			<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
			<div class="user_center"></div>
			<div class="container w_357">
				<jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
				<div class="cfx about-main">
					<div class="gb-sidenav">
						<jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>
					</div>

					<div class="gb-main-r user-main tab-bd p_73">
						<div class="ul_sidebar">
							<ul>
								<li class="active">
									<a data-toggle="tab" href="#tab-personal">个人资料</a>
								</li>
								<li>
									<a data-toggle="tab" href="#tab-repass">修改密码</a>
								</li>
								<li>
									<a data-toggle="tab" href="#tab-card-binding">银行卡绑定</a>
								</li>
								<!--<li>
									<a data-toggle="tab" href="#tab-sms" aria-expanded="true">短信通知</a>
								</li>-->
							</ul>
						</div>
						<div id="tab-personal" class="user-tab-box tab-panel active">
							<div class="user_ziliao">
							<s:url action="change_info" namespace="/asp" var="modifyUserInfoUrl"></s:url>
							<form id="j-modify-form" action="${modifyUserInfoUrl}" method="post" class="ui-form">
								<table class="tb-form">
									<tr>
										<td>
											<div class="">
												<label class="ui-label">账户：</label>
												<span class="ipt-value">${session.customer.loginname}</span>
											</div>
										</td>

									</tr>
									<tr>
										<td>
											<div class="">
												<label class="ui-label">注册货币：</label><span class="ipt-value">人民币</span>
											</div>
										</td>
									</tr>

									<tr>
										<td>
											<div class="">
												<label class="ui-label">账户余额：</label><span class="ipt-value money_18">${session.customer.credit}<label>元</label></span>
											</div>
										</td>

									</tr>
									<tr>
										<td>
											<div class="">
												<label class="ui-label">出生年月：</label>
												<input class="ui-ipt" type="text" name="birthday" id="birthday" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" readonly placeholder="请输入正确的生日日期" value="${session.birthday}" />
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div class="">
												<label class="ui-label">联络电话：</label><input class="ui-ipt" type="text" value='<s:property value="@dfh.utils.StringUtil@mobilePhoneFormat(#session.customer.phone)"/>' disabled>
											</div>
										</td>

									</tr>
									<tr>
										<td>
											<div class="">
												<label class="ui-label">邮箱：</label><input class="ui-ipt" type="text" value='<s:property value="@dfh.utils.StringUtil@emailFormat(#session.customer.email)"/>' disabled>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div class="">
												<label class="ui-label">QQ：</label><input name="qq" class="ui-ipt" type="text" value='<s:property value="@dfh.utils.StringUtil@qqFormat(#session.customer.qq)" />' data-rule-digits="true" placeholder="请输入QQ" maxlength="20">
											</div>
										</td>

									</tr>
									<tr>
										<td>
											<div class="">
												<label class="ui-label">微信号：</label><input name="microchannel" class="ui-ipt" type="text" value='<s:property value="' ******* '+@dfh.utils.StringUtil@subStrLast(#session.customer.microchannel, 3)" />' placeholder="请输入微信号" maxlength="20">
											</div>
										</td>
									</tr>
									<tr>
										<!--<td>
											<div style=" margin-left:130px;">
												<label>
										<s:if test="#session.customer.sms==0">
											<input type="checkbox" name="sms" class="chk" checked="checked"/>
										</s:if>
										<s:else>
											<input type="checkbox" name="sms" class="chk"/>
										</s:else>
										我要接收会员通讯及最新优惠计划
									</label>
											</div>
										</td>-->
									</tr>
									<tr>
										<td>
											<div><input class="btn tijiao" type="submit" value="确认提交" id="J_sub_info"></div>
										</td>
									</tr>
								</table>

								<!--  <div class="ui-form-item">
                <label class="ui-label rq-value">用户昵称：</label><input name="aliasName" class="ui-ipt" value="${session.customer.aliasName}" type="text" required maxlength="12" placeholder="请输入用户昵称">
              </div>-->

								<!--  <div class="ui-form-item">
                <label class="ui-label">邮寄地址：</label><input name="mailaddress" class="ui-ipt" type="text"  value="<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.mailaddress, 3)+'******'" />"  maxlength="50" placeholder="请输入邮寄地址">
              </div>-->

							</form>
							</div>
						</div>
						<div id="tab-repass" class="user-tab-box tab-panel">
							<div class="update_pass">
							<h1 class="tab-tit">会员信息 ><span class="cee6"> 修改密码</span></h1>
							<s:url action="change_pws" namespace="/asp" var="changePwdUrl"></s:url>
							<form id="passwordform" class="ui-form" name="frmAction" action="${ctx}/asp/change_pwsAjax.aspx" method="post">
								<div class="ui-form-item">
									<label class="ui-label">原密码：</label>
									<input type="password" class="ui-ipt" name="password" placeholder="请输入原密码" autocomplete="off" required/>
								</div>

								<div class="ui-form-item">
									<label class="ui-label">新密码：</label>
									<input type="password" id="ipt_pwd" class="ui-ipt" autocomplete="off" name="new_password" placeholder="请输入新密码" maxlength="12" required data-rule-rangelength="8,12" data-rule-password1="true" />
								</div>

								<div class="ui-form-item">
									<label class="ui-label">确认密码：</label>
									<input type="password" class="ui-ipt" autocomplete="off" name="sPass2" maxlength="12" placeholder="请输入确认密码" required data-rule-equalto="#ipt_pwd" />
								</div>

								<div class="tijiao_box">
									<input type="submit" class="btn" value="确认提交" id="J_sub_repass">
								</div>

							</form>
						</div>
						</div>
						<div id="tab-card-binding" class="user-tab-box tab-panel">
							<div class="yinhangka_box">
							<h1 class="tab-tit">会员信息 ><span class="cee6"> 银行卡绑定</span></h1>
							<form id="blindCardForm" class="userform ui-form" action="${ctx}/asp/bandingBankno.aspx" method="post">
								<div class="ui-form-item">
									<label class="ui-label rq-value">银行/支付宝账户：</label>
									<select id="bdbank" class="ui-ipt" onchange="showyzmDiv(this.value)">
										<option value="">请选择</option>
										<s:iterator value="%{#application.IssuingBankEnum}" var="bk">
											<option value=<s:property value="#bk.issuingBankCode" />>
											<s:property value="#bk.issuingBank" />
											</option>
										</s:iterator>
									</select>
								</div>
								<div class="ui-form-item">
									<label class="ui-label rq-value" for="bankno">卡/折号：</label>
									<input type="text" name="bdbankno" id="bdbankno" class="ui-ipt" maxlength="100" autocomplete="off" />
								</div>
								<div class="ui-form-item">
									<label class="ui-label rq-value" for="password_login">登录密码：</label>
									<%--防止自动填充--%> <input type="password" style="display: none;" />
									<input type="password" class="ui-ipt" id="bdpassword" maxlength="15" name="bdpassword" autocomplete="off" />
								</div>
								<div class="ui-form-item" id="zfbyzmDiv" style="display:none;">
									<label class="ui-label" for="password_login">验证码：</label>
									<input type="text" class="ui-ipt" id="bindingCode" maxlength="6" />
									<span class="checkcode1">
								<a href="javascript:void(0)" class="disFlag link" id="sendAlipayPhoneVoiceCodeBtn" >语音验证</a>
								<a href="javascript:void(0)" class="disFlag link" id="sendAlipayPhoneCodeBtn" >短信验证</a>
							</span>
								</div>
								<div class="tijiao_box">
									<input class="btn" type="button" value="确认提交" id="J_sub_bindCard" onclick="return checkbandingform();" />
								</div>
							</form>
							<div class="prompt-info p_280">
								<h3 class="tit c-huangse">温馨提示:</h3>
								<p>1.绑定银行卡/折号，可以免去您重复输入卡/折号的繁琐步骤 </p>
								<p class="c-red">2.只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。银行卡号绑定位数15-20位。</p>
							</div>
						</div>
						</div>

						<div id="tab-sms" class="user-tab-box tab-panel">
							<h1 class="tab-tit">会员信息 ><span class="cee6"> 短信通知</span></h1>

							<%
						Users customer = (Users) session
								.getAttribute(Constants.SESSION_CUSTOMERID);
						String service = customer.getAddress();
						if (service == null) {
							service = "";
						}
					%>
							<form class="ui-form" method="post">
								<div class="ui-form-item">
									<label class="ui-label">修改密码通知：</label> 短信 <input type="checkbox" name="service" value="3" <%=service.indexOf( "3")==- 1 ? "" : "checked"%> />
								</div>

								<div class="ui-form-item">
									<label class="ui-label">提款申请通知：</label> 短信 <input type="checkbox" name="service" value="5" <%=service.indexOf( "5")==- 1 ? "" : "checked"%> />
								</div>

								<div class="ui-form-item">
									<label class="ui-label">存款执行通知：</label> 短信 <input type="checkbox" name="service" value="9" <%=service.indexOf( "9")==- 1 ? "" : "checked"%> />
								</div>
								<div class="ui-form-item">
									<label class="ui-label">自助晋级礼金通知：</label> 短信 <input type="checkbox" name="service" value="2" <%=service.indexOf( "2")==- 1 ? "" : "checked"%> />
								</div>
								<%--<div class="ui-form-item">
							<label class="ui-label rq-value">系统反水通知：</label>
							短信 <input type="checkbox" name="service"
									  value="7" <%=service.indexOf("7") == -1 ? "" : "checked"%> />
						</div>--%>

						<div class="ui-form-item">
							<input type="button" class="btn btn-pay" value="提交" onclick="return smsServie();" />
						</div>

						</form>
						<div class="prompt-info">
							<h3 class="tit">温馨提示:</h3>
							<p>1.请根据实际情况选择相应的服务，以免造成不必要的打扰。 </p>
							<p class="c-red">2.“提款申请通知”和“存款执行通知”均为100元以上才能生效。</p>
						</div>

					</div>

				</div>
			</div>
		</div>

		</div>

		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		<script src="${ctx}/js/plugins/jquery.validate.min.js"></script>
		<script src="${ctx}/js/plugins/jquery.validate.config.js"></script>
		<script type='text/javascript' charset='utf-8' src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js' async></script>
		<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>

		<script type="text/javascript">
			$(function() {

				if($("#birthday").val() != '') {
					$("#birthday").removeAttrs('onfocus');
				}
				//改变菜单
				

				$('#j-modify-form').validate({
					submitHandler: function(form) {
						$.post('${ctx}/asp/change_infoAjax.aspx', $(form).serialize(), function(data) {
							alert(data);
							window.location.reload();
						});
					}
				});

				$('#passwordform').validate({
					submitHandler: function(form) {
						$.post('${ctx}/asp/change_pwsAjax.aspx', $(form).serialize(), function(data) {
							alert(data);
							window.location.reload();
						});
					}
				});

				//支付宝绑定手机号验证
				$("#sendAlipayPhoneVoiceCodeBtn").on("click", function() {
					/*********触点**********/
					var is_checked = false;
					window.TouClick.Start({
						website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
						position_code: 0,
						args: { 'this_form': $("#blindCardForm")[0] },
						captcha_style: { 'left': '50%', 'top': '60%' },
						onSuccess: function(args, check_obj) {
							//console.log(args);
							//console.log(check_obj);
							is_checked = true;
							var this_form = args.this_form;
							var hidden_input_key = document.createElement('input');
							hidden_input_key.name = 'check_key1';
							hidden_input_key.value = check_obj.check_key;
							hidden_input_key.type = 'hidden';
							//将二次验证口令赋值到隐藏域
							this_form.appendChild(hidden_input_key);
							var hidden_input_address = document.createElement('input');
							hidden_input_address.name = 'check_address1';
							hidden_input_address.value = check_obj.check_address;
							hidden_input_address.type = 'hidden';
							//将二次验证地址赋值到隐藏域
							this_form.appendChild(hidden_input_address);

							openProgressBar();
							var check_address = $("input[name='check_address1']").eq(0).val();
							var check_key = $("input[name='check_key1']").eq(0).val();

							$.post("${ctx}/asp/sendAlipayPhoneVoiceCode.aspx", { "check_address": check_address, "check_key": check_key }, function(data) {
								alert(data);
								closeProgressBar();
							});
						},
						onError: function(args) {
							//启用备用方案
						}
					});
					/*********触点**********/
				});

				$("#sendAlipayPhoneCodeBtn").on("click", function() {
					/*********触点**********/
					var is_checked = false;
					window.TouClick.Start({
						website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
						position_code: 0,
						args: { 'this_form': $("#blindCardForm")[0] },
						captcha_style: { 'left': '50%', 'top': '60%' },
						onSuccess: function(args, check_obj) {
							//console.log(args);
							//console.log(check_obj);
							is_checked = true;
							var this_form = args.this_form;
							var hidden_input_key = document.createElement('input');
							hidden_input_key.name = 'check_key1';
							hidden_input_key.value = check_obj.check_key;
							hidden_input_key.type = 'hidden';
							//将二次验证口令赋值到隐藏域
							this_form.appendChild(hidden_input_key);
							var hidden_input_address = document.createElement('input');
							hidden_input_address.name = 'check_address1';
							hidden_input_address.value = check_obj.check_address;
							hidden_input_address.type = 'hidden';
							//将二次验证地址赋值到隐藏域
							this_form.appendChild(hidden_input_address);

							openProgressBar();
							var check_address = $("input[name='check_address1']").eq(0).val();
							var check_key = $("input[name='check_key1']").eq(0).val();
							$.post("${ctx}/asp/sendAlipayPhoneSmsCode.aspx", { "check_address": check_address, "check_key": check_key }, function(data) {
								alert(data);
								closeProgressBar();
							});
						},
						onError: function(args) {
							//启用备用方案
						}
					});
					/*********触点**********/
				});

				clearBandingform();
			});

			//绑定银行卡
			function checkbandingform() {
				if(!window.confirm("确定吗？")) {
					return false;
				}
				var bdbankno = $("#bdbankno").val();
				if(bdbankno == "") {
					alert("[提示]卡/折号不可为空！");
					return false;
				}
//				if(bdbankno.length > 30 || bdbankno.length < 10) {
//					alert("[提示]卡/折号长度只能在10-30位之间");
//					return false;
//				}
				var bdbank = $("#bdbank").val();
				if(bdbank == "") {
					alert("[提示]银行不能为空！");
					return false;
				}
				var bdpassword = $("#bdpassword").val();
				if(bdpassword == "") {
					alert("[提示]登录密码不可以为空");
					return false;
				}
				var bindingCode = $("#bindingCode").val();
				openProgressBar();
				$.post("${ctx}/asp/bandingBankno.aspx", {
					"password": bdpassword,
					"bankname": bdbank,
					"bankno": bdbankno,
					"bankaddress": "none",
					"bindingCode": bindingCode
				}, function(returnedData, status) {
					if("success" == status) {
						if(returnedData == "SUCCESS") {
							alert("绑定成功！");
						} else {
							closeProgressBar();
							alert(returnedData);
						}
					}
				});
			}

			//重置绑定
			function clearBandingform() {
				$("#bdbankno").val("");
				$("#bdpassword").val("");
			}

			function showyzmDiv(bank) {
				if(bank == "支付宝") {
					$("#zfbyzmDiv").attr("style", "display:block;");
				} else {
					$("#zfbyzmDiv").attr("style", "display:none;");
				}
			}

			//短信服务
			function smsServie() {
				var str = "";
				$("input[name='service']:checkbox").each(function() {
					if($(this).prop("checked")) {
						str += $(this).val() + ","
					}
				})
				/* if (str.length <= 0) {
				    alert("未选中数据！");
				    return false;
				} */
				openProgressBar();
				$.post("/asp/chooseservice.aspx", {
					"service": str
				}, function(returnedData, status) {
					if("success" == status) {
						closeProgressBar();
						alert(returnedData);
						//window.location.reload();
					}
				});
				return false;
			}
		</script>
		
		<script>
			window.onload=function(){
				var url=window.location.search;
				if(url=="?card_binding"){
					$(".ul_sidebar ul li").eq(2).find("a").click()
				}				
			}
		</script>
	</body>

</html>