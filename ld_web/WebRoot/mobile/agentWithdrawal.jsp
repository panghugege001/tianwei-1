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
			<div id="page-index"  data-page-index="" class="tab-panel active">
				<div class="main-wrap">
					<div class="header-margin"></div>
					<div class="content">
						<div class="ui-form">
							<div class="ui-input-row">
								<label class="ui-label">游戏账户密码：</label>
								<input id="withdrawal-password" class="ui-ipt" type="password" required>
							</div>
							<div class="ui-input-row zf-sele zf-sele1">
								<label class="ui-label">取款银行：</label>
								<div id="withdrawal-bankName"></div>
							</div>
							<div class="ui-input-row">
								<label class="ui-label">银行卡号：</label>
								<input id="withdrawal-cardno" class="ui-ipt" type="text" readonly>
								<div class="mui-btn mui-btn--raised mui-btn--danger small"  id="withdrawal-binding">绑定?</div>
							</div>
							<div class="ui-input-row">
								<label class="ui-label">取款金额：</label>
								<input id="withdrawal-money" class="ui-ipt" type="text" placeholder="0.00" required>
							</div>
							<div class="mui-textfield tkTip">
								<p id="tkTip" style="color: crimson;"></p>
							</div>

							<div class="ui-input-row zf-sele">
								<label class="ui-label">密保问题：</label>
								<div id="withdrawal-question"></div>
							</div>
							<div class="ui-input-row">
								<label class="ui-label">密保答案：</label>
								<input id="withdrawal-answer" class="ui-ipt" type="text" required>
								<div class="mui-btn mui-btn--raised mui-btn--danger small" ><a data-toggle="tab" href="#page-question" class="set" aria-expanded="false">设定密保</a></div>
							</div>
							<div class="ui-input-row zf-sele">
								<label class="ui-label">提款类型：</label>
								<div id="withdrawal-type"></div>
							</div>
							<!--<div class="yue-box">-->
								<!--<span class="slot-account active">余额：<i class="num-account">${session.slotAccount}</i> 元</span>-->
								<!--<span class="credit">余额：<i class="num-credit">${session.customer.credit}</i> 元</span>-->
							<!--</div>-->


							<div class="yue-box">
                                <span class="slot-account active">余额：
						        <i class="agent-slotAccount num-account"></i></span>
								<span class="credit">余额：<i class="num-credit">${session.customer.credit}</i> 元</span>
							</div>
							<div class="ui-button-row center">
								<div class="btn-login block" id="withdrawal-submit">确定提款</div>
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
		<input id="moneyAccountMoblie" type="hidden" value="${session.slotAccount}" data-liveall="${session.customer.credit}" />


		<jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			//滑动时 隐藏header
			headerBar.scrollHide(false);
			headerBar.setTitle("申请提款");
			footerBar.active("agentWithdrawal");
			new QuestionPage();
			new Withdrawal();
            getUserInfo();
			//申请提款
			function Withdrawal(){
				var that = this;

				//设定只能输入数字
				NumberInput('withdrawal-money');

				$('#withdrawal-binding').click(function(){
					that.bankNameCbo.setValue('');
					mobileManage.getModel().open('bankBind');
				});

				//银行下拉
				that.bankNameCbo = new MobileComboBox({
					appendId:'withdrawal-bankName',
					cls:'',
					valueName:'value',
					displayName:'name',
					datas:[
						{value:'',name:'请选择银行'},
						{value:'工商银行',name:'工商银行'},
						{value:'招商银行',name:'招商银行'},
						{value:'商业银行',name:'商业银行'},
						{value:'农业银行',name:'农业银行'},
						{value:'建设银行',name:'建设银行'},
						{value:'交通银行',name:'交通银行'},
						{value:'民生银行',name:'民生银行'},
						{value:'光大银行',name:'光大银行'},
						{value:'兴业银行',name:'兴业银行'},
						{value:'上海浦东银行',name:'上海浦东银行'},
						{value:'广东发展银行',name:'广东发展银行'},
						{value:'深圳发展银行',name:'深圳发展银行'},
						{value:'中国银行',name:'中国银行'},
						{value:'中信银行',name:'中信银行'},
						{value:'邮政银行',name:'邮政银行'}
					],
					onChange:function(e){
						if(!that.bankNameCbo.getValue())return;
						mobileManage.getLoader().open("载入中");
						mobileManage.getBankManage().getBankDataByName({bankName:that.bankNameCbo.getValue()},function(result){
							if(result.success){
								$('#withdrawal-cardno').val(result.data.number);
							}else{
								alert(result.message);
							}
							mobileManage.getLoader().close();
						});
					}
				});




				//提款类型下拉
                that.typeCbo = new MobileComboBox({
                    appendId: 'withdrawal-type',
                    cls: '',
                    valueName: 'value',
                    displayName: 'name',
                    datas: [
                        { value: 'slotmachine', name: '老虎机' },
                        { value: 'liveall', name: '其他' }
                    ],
                    onChange: function(e) {

                        var value = that.typeCbo.getValue()
                        if(!value) {
                            return
                        } else if(value === 'slotmachine') {
                            $('.yue-box span').removeClass('active')
                            $('.slot-account').addClass('active')
                        } else {
                            $('.yue-box span').removeClass('active')
                            $('.credit').addClass('active')
                        }

                    }
                });
                $('.yue-box i').each(function() {
                    var num = $(this).html();
                    if(num < 0) {
                        $(this).css('color', 'red')
                    } else {
                        $(this).css('color', 'green')
                    }
                });


                that.questionCbo = new MobileComboBox({
					appendId:'withdrawal-question',
					cls:'',
					valueName:'value',
					displayName:'name',
					datas:[
						{value:'1',name:'您最喜欢的明星名字？'},
		                {value:'2',name:'您最喜欢的职业？'},
		                {value:'3',name:'您最喜欢的城市名称？'},
		                {value:'4',name:'对您影响最大的人名字是？'},
		                {value:'5',name:'您就读的小学名称？'},
		                {value:'6',name:'您最熟悉的童年好友名字是？'}
		            ],
					onChange:function(e){

					}
				});

				//提交

                var maxTkMoney = 0; //最大可提款

				$('#withdrawal-submit').click(function(){
					var formData = {
						password:$('#withdrawal-password').val(),
						bankName:that.bankNameCbo.getValue(),
						cardNo:$('#withdrawal-cardno').val(),
						addr:'none',
						money:$('#withdrawal-money').val(),
						withdrawlType:that.typeCbo.getValue(),
						questionId:that.questionCbo.getValue(),
						answer:$('#withdrawal-answer').val()
					};

                    if(maxTkMoney < 100) {
                        alert("[提示]老虎机/其它类佣金综合余额不足100无法提款");
                        return false;
                    }
                    if(formData.withdrawlType == 'slotmachine') {
                        var slotMoney = Number($('.num-account').html()); //老虎机佣金余额
                        if(formData.money > slotMoney) {
                            alert("[提示]最大提款金额为" + slotMoney + "元");
                            return false;
                        }
                    }
                    if(formData.money - maxTkMoney > 0) {
                        alert("[提示]最大提款金额为" + maxTkMoney + "元");
                        return false;
                    }

                    //提款
					mobileManage.getLoader().open('处理中');
					mobileManage.getBankManage().withdrawal(formData, function(result){
						if(result.success){
							alert(result.message);
							$('#withdrawal-money').val('');
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});

				});


//                $('#withdrawal-money').focus(function() {
//                    var moneyAccount = $('#moneyAccountMoblie');
//                    var tkType = $("#m-withdrawal-type").val();
//                    var slotmachine = Number(moneyAccount.val());
//                    var liveall = Number(moneyAccount.attr('data-liveall'));
//                    maxTkMoney = (slotmachine + liveall).toFixed(2);
//                    $.get('/asp/agentWithdrawpz.aspx', {
//                        tkType: tkType,
//                        slotmachine: Number(slotmachine).toFixed(2),
//                        liveall: Number(liveall).toFixed(2)
//                    }, function(data) {
//                        $('#tkTip').html(data)
//                    })
//                });
                $('#withdrawal-money').focus(function () {
                    var moneyAccount = $('.agent-slotAccount');
                    var tkType = $("#m-withdrawal-type").val()
                    var slotmachine = Number(moneyAccount.val());
                    var liveall = Number(moneyAccount.attr('data-liveall'));

                    maxTkMoney = (slotmachine + liveall).toFixed(2);
                    $.get('/asp/agentWithdrawpz.aspx', {
                        tkType: tkType,
                        slotmachine: Number(slotmachine).toFixed(2),
                        liveall: Number(liveall).toFixed(2)
                    }, function (data) {
                        $('#tkTip').html(data)
                    })
                });

            }

            function getUserInfo(){
                //查询代理用户统计资料
                mobileManage.getUserManage().getAgentReport(function(result){
                    if(result.success){
                        $('.agent-slotAccount').html((result.data.slotAccount?result.data.slotAccount:0.0)+'元');
                    }else{
                        alert(result.message);
                    }
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