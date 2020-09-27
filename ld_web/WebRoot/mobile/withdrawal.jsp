<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/agent.jsp");
	}
%>
<!DOCTYPE >

<html>
	<head>
		
	</head>
	<body>
		<jsp:include page="commons/back.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=9" />
		
		<script type="text/javascript" src="mobile/js/MobileGrid.js"></script>
		<script type="text/javascript" src="mobile/js/ModeControl.js"></script>
		<div class="tab-bd">
			<div id="page-index"  data-page-index="" class="tab-panel active">
				<div class="main-wrap">
					<div class="header-margin"></div>
					<div class="content">
						<div class="page-block">
							<div id="funds-withdrawal-page" style="display: none;">
								<jsp:include page="funds/withdrawal.jsp"/>
							</div>
						</div>
					</div>
					
					<div class="footer-margin"></div>
				</div>
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
			$(function () {
			 	 QuestionPage();
			 
			});
			headerBar.setTitle('提款');
			footerBar.active('withdrawal');
			
			headerBar.scrollHide(true);
			headerBar.bind('hide',function(){
				$('.main-wrap .tab-block').css('top',0);
			});
			headerBar.bind('show',function(){
				$('.main-wrap .tab-block').css('top',headerBar.defHeight);
			});
			
			var mode = mobileManage.getSessionStorage('fundsManage').mode||0;
			//切换存款、提款、转账和交易明细
			var modeControl = new ModeControl();
			modeControl.putMode(0,{id:'funds-withdrawal',button:$('#funds-withdrawal-button'),page:$('#funds-withdrawal-page'),manage:undefined,getManage:WithdrawalPage});
 
			modeControl.on('beforChange',function(){
				var nextMode = modeControl.getNextMode();
				var activeMode = modeControl.getActiveMode();
				if(nextMode){
					nextMode.page.css('display','block');
					nextMode.button.addClass('active');
					if(!nextMode.manage&&nextMode.getManage){
						if(nextMode.getManage){
							//切換到該頁面才執行js 
							nextMode.manage = new nextMode.getManage();
						}
					}

				 
				}
				
				if(activeMode){
					activeMode.page.css('display','none');
					activeMode.button.removeClass('active');
				}
			});
			
			modeControl.on('change', function(){
				mode = modeControl.getActiveKey();
				mobileManage.setSessionStorage('fundsManage',{mode:mode});
			});
			
			
			//显示对应的Page
			modeControl.activeMode(mode);
			
			$('#funds-deposit-button').click(function(){
				modeControl.activeMode(0);
			});
			$('#funds-withdrawal-button').click(function(){
				modeControl.activeMode(1);
			});
			$('#funds-transfer-button').click(function(){
				modeControl.activeMode(2);
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