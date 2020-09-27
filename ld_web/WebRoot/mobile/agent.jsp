<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/account.jsp");
	}
%>
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" /> 
		
		<link rel="stylesheet" type="text/css" href="mobile/css/agent.css?v=9" />
	</head>
	<body>
		<div class="tab-bd">
			<div id="page-index" data-page-index class="tab-panel active">

				<div class="main-wrap">
					<div class="header-margin"></div>
					<div class="content">
						<div class="account-info">
							<div class="iconfont icon-peoplefill user-photo"></div>
							<div class="info"> 
								<strong>欢迎您：</strong><span class="value cde">${session.customer.loginname}</span><br />
								<strong>等级：</strong> 
								<span class="value cde">
									<c:if test="${session.AGENTVIP ==null || session.AGENTVIP eq '0'}">
										代理
									</c:if>
									<c:if test="${session.AGENTVIP!=null && session.AGENTVIP eq '1'}">
										VIP代理
									</c:if>
								</span><br />
								<strong>老虎机佣金余额:</strong><span id="agent-slotAccount" class="value cde">${session.slotAccount}元</span><br />
								<strong>其他佣金余额：</strong><span class="value cde">${session.customer.credit} 元</span>
							</div>
						</div>
						<div class="profile-data">
							<div class="item">
								<p>本月输赢（元）</p>
								<h1 id="agent-profitall" class="report-text"></h1>
								<p>会员总人数</p>
								<h1 id="agent-reg" class="report-text"></h1>
							</div>
							<div class="item">
								<p>本月总返水</p>
								<h1 id="agent-ximafee" class="report-text"></h1>
								<p>本月注册量</p> 
								<h1 id="agent-monthly_reg" class="report-text"></h1>
							</div>
							<div class="item">
								<p>本月总优惠</p>
								<h1 id="agent-couponfee" class="report-text"></h1>
								<p>本月投注额</p>
								<h1 id="agent-betall" class="report-text"></h1>
							</div>
						</div>
						<div class="ui-form" id="pane-default-0">
								<div class="ui-input-row">
									<label class="ui-label">用户名：</label>
									<input type="text"  class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1)"/>" readonly>
								</div>
								<div class="ui-input-row">
									<label class="ui-label">密码：</label>
									<input type="text"  class="ui-ipt" value="********" readonly>
									<div class="mui-btn mui-btn--raised mui-btn--danger small" ><a href="#page-chagepassword" data-toggle="tab" class="set">修改密码</a></div>
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
									<input type="text" class="ui-ipt" value="${customer.lastarea}" readonly>
									
								</div>
								<div class="ui-input-row">
									<label class="ui-label">联系电话：</label>
									<input type="text" class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.phone, 0.5,1)"/>" readonly>
								
								</div>
								<div class="ui-input-row">
									<label class="ui-label">电子邮箱：</label>
									<input type="text" class="ui-ipt" value="${customer.email}" readonly>
									
								</div>
								<div class="ui-input-row">
									<label class="ui-label">密保问题：</label>
									<input id="question" class="ui-ipt" type="text" readonly>
								
									<div id="account-question-button" class="mui-btn mui-btn--raised mui-btn--danger small" ><a href="#page-question" data-toggle="tab" class="set">设定密保</a></div>
								</div>
								<div class="ui-input-row"> 
									<label class="ui-label">QQ号码：</label>
									<input id="QQ" class="ui-ipt" type="text" placeholder="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.qq, 0.7,0)"/>" >
								
								</div>
								<div class="ui-button-row center">  
									<div class="btn-login block" id="account-submit">提交</div>
								</div>  
						
						</div>
					</div>
					<div class="footer-margin"></div>
				</div>
			</div>
			<div id="page-question" class="tab-panel mt5">
				<form id="form-question" class="ui-form">
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
						<input class="btn btn-login" type="submit"  id="question-submit" value="绑定">
					</div>

				</form>
			</div>
			<div id="page-chagepassword" class="tab-panel mt5">
				<form id="form-chagepassword" class="ui-form">
					<div class="ui-input-row">
						<label for="" class="ui-label">旧密码：</label>
						<input name="password" type="password" class="ui-ipt" placeholder=""  required/>
					</div>

					<div class="ui-input-row">
						<label for="" class="ui-label">新密码：</label>
						<input name="newPassword" type="password" class="ui-ipt" placeholder="密码为6-16位数字或英文字母，英文字母开头"  required/>
					</div>

					<div class="ui-input-row">
						<label for="" class="ui-label">确认密码：</label>
						<input name="confirmPassword" type="password" class="ui-ipt" maxlength="20" placeholder="再次输入密码，确认新密码无误" required/>
					</div>
					<div class="ui-button-row center">
						<input class="btn btn-login" type="submit"  value="保存">
					</div>

				</form>
			</div>

		</div>		
		<jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			//滑动时 隐藏header
			headerBar.scrollHide(false);
			headerBar.setTitle("个人中心");
			footerBar.active("agent");
			$(function () {
				  new ChagepasswordPage();
				  new QuestionPage();
			})
			//查询代理用户统计资料
			mobileManage.getUserManage().getAgentReport(function(result){
				if(result.success){
					$('#agent-slotAccount').html((result.data.slotAccount?result.data.slotAccount:0.0)+'元');
					$('#agent-profitall').html((result.data.profitall?result.data.profitall:0));
					$('#agent-ximafee').html((result.data.ximafee?result.data.ximafee:0));
					$('#agent-couponfee').html((result.data.couponfee?result.data.couponfee:0));
					$('#agent-reg').html(result.data.reg?result.data.reg:0);
					$('#agent-monthly_reg').html(result.data.monthly_reg?result.data.monthly_reg:0);
					$('#agent-betall').html(result.data.betall?result.data.betall:0);
				}else{
					alert(result.message);
				}
			});
			UserInfo();
			//账户资料
			function UserInfo(){
				var that = this;
				var _questionNames = {
					'1':true,
		            '2':true,
		            '3':true,
		            '4':true,
		            '5':true,
		            '6':true,
				};

				that.$QQ = $('#QQ');
				
				//查询密保问题
				mobileManage.getUserManage().getQuestion(function(result){
					if(result.success&&_questionNames[result.data.questionId]){
						$('#question').val('已设置');
						$('#account-question-button').hide();
					}else{
						$('#question').val('尚未设置');
					}
				});
				
				/*$('#account-modifyPassword-button').click(function(){
					mobileManage.getModel().open('modifyPassword');
				});*/
				/*$('#account-question-button').click(function(){
					mobileManage.getModel().open('question');
				});*/
				
				
				$('#account-submit').click(function(){
					/*if(!that.$QQ.val()){
						alert('资料没有异动！');
						return;
					}*/
					
					var formData = {
						qq:that.$QQ.val()
					};
					mobileManage.getLoader().open("修改中");
					mobileManage.getUserManage().modifyInfo(formData, function(result){
						mobileManage.getLoader().close();
						if(result.success){
							that.$QQ.val('');
							that.$QQ.attr('placeholder',result.data.QQ?result.data.QQ:'');
							alert(result.message);
						}else{
							alert(result.message);
						}
					});
				});
			}
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
								mobileManage.getLoader().open('登出中');
								mobileManage.getUserManage().logout( function(data){
									mobileManage.getLoader().close();
									if(data.success){
										sessionStorage.setItem('permission',0);
										sessionStorage.setItem('notice',1);
										mobileManage.getLoader().close();
										window.location = '/mobile/login.jsp'

									}else{
										alert(data.message);
									}
								});
							}else{
								alert(result.message);
							}
						});


						return false;
					});
				};


				that._init();
			}
		</script>
	</body>
</html>