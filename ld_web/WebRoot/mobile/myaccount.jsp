<%@page import="dfh.utils.StringUtil"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	if(session.getAttribute(Constants.SESSION_CUSTOMERID)==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}
%>
<!DOCTYPE >
<html>
	<head>
		<base href="${ctx}/"/>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=9" />
	</head>
	<body>
		<div class="tab-bd">
			<div id="page-index" data-page-index="" class="tab-panel active">
				<div class="main-wrap">
					<div class="header-margin"></div>
					<div class="content">
							<div class="account-info">
								<div class="user-photo sp-level sp-level-${session.customer.level}"></div> 
								<div class="info">
									<strong>账号:</strong><span class="value cde">${session.customer.loginname}</span> <br />
									<strong>等级:</strong><span class="value cde"><s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)"/></span><br /> 
									
									<c:choose>
										<c:when test="${session.customer!=null&&session.customer.role!='AGENT'}">
											<div class="banlce"><strong>余额:</strong><span class="value cde" id="credit">${session.customer.credit} 元</span><span class="refre iconfont icon-shuaxin" onclick="refresh()" title="刷新余额"></span></div><br />
										</c:when>
										<c:otherwise>
											<strong>老虎机佣金余额:</strong><span class="value cde">${session.slotAccount} 元</span><br />
											<strong>其他佣金余额:</strong><span class="value cde">${session.customer.credit} 元</span><br />
										</c:otherwise>
									</c:choose>
								</div>
								 
							</div>
							<div class="ui-form">
								<div class="ui-input-row">
									<label class="ui-label">用户名：</label>	
									<input type="text" class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1)"/>" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">密码：</label>	
									<input type="text"  class="ui-ipt" value="********" readonly>
									<a id="account-modifyPassword-button" href="#page-chagepassword" data-toggle="tab"  aria-expanded="false" class="mui-btn mui-btn--raised mui-btn--danger" >修改密码</a> 
								</div>
								<div class="ui-input-row">
									<label class="ui-label">登陆次数：</label>	
									<input type="text"  class="ui-ipt" value="${customer.loginTimes}" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">上次登录：</label>	
									<input type="text"  class="ui-ipt" value="${customer.tempLastLoginTime}" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">上次IP：</label>	
									<input type="text"  class="ui-ipt" value="${customer.lastLoginIp}" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">登陆地点：</label>	
									<input type="text"  class="ui-ipt" value="${customer.lastarea}" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">联系电话：</label>	
									<input type="text"  class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.phone, 0.5,1)"/>" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">电子邮箱：</label>	
									<input type="text" class="ui-ipt" value='<s:property value="@dfh.utils.StringUtil@emailFormat(#session.customer.email)"/>' readonly>
								</div>
								<div class="ui-input-row date-select">
									<label class="ui-label">出生日期：</label>	
									<input id="birthday" class="ui-ipt" type="text" value='<fmt:formatDate value="${session.customer.birthday}" pattern="yyyy-MM-dd"/>'  readonly> 
								 
								</div>
								<div class="ui-input-row">
									<label class="ui-label">密保问题：</label>	
									<input id="account-question" class="ui-ipt" type="text" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">绑定银行卡：</label>	
									<input id="account-bankcard" class="ui-ipt" type="text" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">QQ号码：</label>	
									<input id="QQ" type="text" class="ui-ipt" placeholder="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.qq, 0.7,0)"/>" >
								</div>
								<div class="ui-input-row">
									<label class="ui-label">微信号：</label>	
									<input id="wx" type="text" class="ui-ipt"  value='<s:property value="'*******'+@dfh.utils.StringUtil@subStrLast(#session.customer.microchannel, 3)" />' >
	
								</div>
								<div class="ui-button-row center">  
									<div class="btn-login block" id="account-submit">提交</div>
								</div> 
							</div>
						
					</div>
					<div class="footer-margin"></div>
				</div>
			</div>
			<div id="page-chagepassword" class="tab-panel mt5">
				<form id="form-chagepassword" class="ui-form">
					<div class="ui-input-row">
						<label for="" class="ui-label">旧密码：</label>
						<input name="password" type="password" class="ui-ipt" placeholder="" required="">
					</div>

					<div class="ui-input-row">
						<label for="" class="ui-label">新密码：</label>
						<input name="newPassword" type="password" class="ui-ipt" placeholder="密码为6-16位数字或英文字母，英文字母开头" required="">
					</div>

					<div class="ui-input-row">
						<label for="" class="ui-label">确认密码：</label>
						<input name="confirmPassword" type="password" class="ui-ipt" maxlength="20" placeholder="再次输入密码，确认新密码无误" required="">
					</div>
					<div class="ui-button-row center">
						<input class="btn-login block" type="submit" value="确认修改">
					</div>

				</form>
			</div>
			<div id="page-question" class="tab-panel">
				<form id="form-question" class="ui-form mt5">
					<div class="ui-input-row zf-sele">
						<label for="" class="ui-label">密保问题：</label>
						<div id="question-question"></div>
					</div>

					<div class="ui-input-row">
						<label for="" class="ui-label">您的答案：</label>
						<input name="answer" type="text" class="ui-ipt"  required/>
					</div>

					<div class="ui-input-row">
						<label for="" class="ui-label">登录密码：</label>
						<input name="password" type="password" class="ui-ipt" maxlength="20" required/>
					</div>


					<div class="ui-button-row center">
						<input class="btn-login block" type="submit"  id="question-submit" value="绑定">
					</div>

				</form>
	

			</div>
		</div>
		<jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
		headerBar.setTitle('账户设置');
			footerBar.active("account");
			$(function () {
				  new ChagepasswordPage();
				  new QuestionPage();
			})
			
			if($("#birthday").val()==''){
                $('.date-select').click(function(){
                    mui.datepicker.open($(this).find('input'));
                });
                $('.date-select').append('<span class="date-icon flaticon-weekly3"></span>');
            }
			 if($('#wx').val()=='*******'){
                $('#wx').val(''); 
            }  
            if($("#email").val()!=''){
                $('#email').attr("readonly","readonly");
            }
			var questionNames = {
				'1':true,
	            '2':true,
	            '3':true,
	            '4':true,
	            '5':true,
	            '6':true,
			};

			var $QQ = $('#QQ');
			var $EMAIL=$('#email');
			var $BIRTHDAY=$('#birthday');
			var $WX=$('#wx');
			//查询密保问题
			mobileManage.getUserManage().getQuestion(function(result){
				if(result.success&&questionNames[result.data.questionId]){
					$('#account-question').val('已设置');
					$('#account-question-button').hide();
				}else{
					$('#account-question').val('尚未设置');
					$('#account-question').parent().append('<div id="account-question-button" class="mui-btn mui-btn--raised mui-btn--danger" ><a data-toggle="tab" href="#page-question" class="set"  aria-expanded="false">设定密保</a></div>');
					/*$('#account-question-button').click(function(){
						mobileManage.getModel().open('question');
					});*/
				}
			});

			//查询绑定银行卡数量
			mobileManage.getBankManage().isBindBankNo(function(result){
				if(result.success){
					$('#account-bankcard').val('已绑定'+result.data.count+'张银行卡');
					if(result.data.count<3){
						$('#account-bankcard').parent().append('<div id="account-bankcard-button" class="mui-btn mui-btn--raised mui-btn--danger" >绑定</div>');
					}
				}else{
					$('#account-bankcard').val('尚未绑定');
					$('#account-bankcard').parent().append('<div id="account-bankcard-button" class="mui-btn mui-btn--raised mui-btn--danger" >绑定</div>');
				}
				$('#account-bankcard-button').click(function(){
					mobileManage.getModel().open('bankBind');
				});
			});
			
			/*$('#account-modifyPassword-button').click(function(){
				mobileManage.getModel().open('modifyPassword');
			});*/
			
			$('#account-submit').click(function(){
					/*if(!$QQ.val()){
					alert('资料没有异动！');
					return;
				}*/
				
				var formData = {
					qq:$QQ.val(),
					email:$EMAIL.val(),
					birthday:$BIRTHDAY.val(),
                    microchannel:$WX.val()
				};
				mobileManage.getLoader().open("修改中");
				mobileManage.getUserManage().modifyInfo(formData, function(result){
					mobileManage.getLoader().close();
					if(result.success){
						$QQ.val('');
						$QQ.attr('placeholder',result.data.QQ);
						alert(result.message);
						window.location.reload();
					}else{
						alert(result.message);
					}
				});
			});
			 function QuestionPage(){
			  
				var that=this;

				var $form=$('#form-question');
				var $submitBtn=$form.find(':submit');

				var questionData = [
					{value:'1',name:'您最喜欢的明星名字？'},
					{value:'2',name:'您最喜欢的职业？'},
					{value:'3',name:'您最喜欢的城市名称？'},
					{value:'4',name:'对您影响最大的人名字是？'},
					{value:'5',name:'您就读的小学名称？'},
					{value:'6',name:'您最熟悉的童年好友名字是？'}
				];

				that._init=function(){

					//来原账户下拉
					var questionSelect = new MobileComboBox({
						appendId:'question-question',
						cls:'ui-select',
						valueName:'value',
						displayName:'name',
						datas:questionData,
						onChange:function(e){
						}
					});

					$form.submit(function(){ 
						/*var formData = {
						password:_$questionModel.$password.val(),
						answer:_$questionModel.$answer.val(),
						questionId:_$questionModel.$question.val()
						};*/
						var formData = $form.serializeObject();

						formData['questionId']=questionSelect.getValue();

						mobileManage.getLoader().open("设置中");
						mobileManage.getUserManage().saveQuestion(formData, function(result){
							mobileManage.getLoader().close();
							if(result.success){
								alert(result.message);
								window.location.reload();
							}else{
								alert(result.message);
							}
						});


						return false;
					});
				};


				that._init();
			}
			function ChagepasswordPage(){

				var that=this;

				var $form=$('#form-chagepassword');

				that._init=function(){

					$form.submit(function(){
						/*var formData = {
						password:_$questionModel.$password.val(),
						answer:_$questionModel.$answer.val(),
						questionId:_$questionModel.$question.val()
						};*/
						var formData = $form.serializeObject();


						mobileManage.getLoader().open("设置中");
						mobileManage.getUserManage().changePassword(formData, function(result){
							mobileManage.getLoader().close();
							if(result.success){
								alert(result.message);
							}else{
								alert(result.message);
							}
						});


						return false;
					});
				};


				that._init();
			}
			
			
			//重新整理余额
			function refresh(){
				$('.refre').addClass('credit-query');
				
				//先查询优发平台余额
				mobileManage.getUserManage().getCredit(
					function(result){
			    		if(result.success){
							$("#credit").html(result.message+" 元");
						}else{
							$("#credit").html('系统繁忙中');
							alert(result.message);
						}
						$('.refre').removeClass('credit-query');
					}
			    );
			}
		</script>
	</body>
</html>