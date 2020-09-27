<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/account.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="balance-top">
			<img src="/mobile/img/icon/money.png" alt="" /> 账户余额：
			<span class="c-ylow" id="credit">${session.slotAccount==null?0:session.slotAccount}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			其他余额：<span class="c-ylow" id="creditother">${session.customer.credit}</span>
			<i class="fr c-ylow iconfont icon-icon-refresh " onclick="refresh()"></i>
			
		</div>

		<div class="form-warp no-icon">
			<label class="form-tips">提款类型</label>
			<div class="form-group">
				<select id="withdrawal-type" class="form-control">
					<option value="">请选择提款类型</option>
				</select>
			</div>
			<label class="form-tips">取款银行<small>（每个账户可绑定三张银行卡）</small></label>
			<div class="form-group">
				<select id="withdrawal-bankName" class="form-control">
					<option value="">请选择取款银行</option>
				</select>
			</div>
			<label class="form-tips">银行卡号</label>
			<div class="form-group">
				<input id="withdrawal-cardno" class="form-control" type="text" disabled="" readonly>
				<a href="/mobile/new/mybank.jsp" class="form-code" id="withdrawal-binding">绑定卡号</a>
			</div>
			<label class="form-tips">取款金额<small>（提款金额不能低于100元）</small></label>
			<div class="form-group">
				<input id="withdrawal-money" data-pattern="\S+" class="form-control" type="text" placeholder="0.00" required>
				<div class="ipt-clear undone"></div>
			</div>
			<div class="mui-textfield tkTip">
				<p id="tkTip" style="color: crimson;"></p>
			</div>
			<label class="form-tips">密保问题</label>
			<div class="form-group">
				<select id="withdrawal-question" class="form-control">
					<option value="">请选密保问题</option>
				</select>
			</div>
			<label class="form-tips">密保答案</label>
			<div class="form-group">
				<input id="withdrawal-answer" placeholder="请输入密保答案" class="form-control" type="text" required>
				<a class="form-code hidden" id='nosecret' href="/mobile/new/secret.jsp">设定密保</a>
			</div>
			
			<label class="form-tips">代理帐号密码</label>
			<div class="form-group">
				<input id="withdrawal-password" class="form-control" type="password" placeholder="请输入密码" required>
			</div>
			<div class="btn-submit block" id="withdrawal-submit">确定提款</div>
		</div>
		<br />
		<input id="moneyAccountMoblie" type="hidden" value="${session.slotAccount}" data-liveall="${session.customer.credit}" />
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			//查询代理用户统计资料
			function refresh() {
				mobileManage.getUserManage().getAgentReport(function(result) {
					if(result.success) {
						$('#credit').html((result.data.slotAccount ? result.data.slotAccount : 0.0));
					} else {
						alert(result.message);
					}
				});
			}
			refresh();
			//查询是否绑定问题
			mobileManage.getUserManage().getQuestion(function(result) {
				var questionNames = {
					'1': true,
					'2': true,
					'3': true,
					'4': true,
					'5': true,
					'6': true,
				};
				if(result.success) {
					$("#nosecret").hide()
				} else {
					$("#withdrawal-question").attr("disabled", 'disabled')
					$("#withdrawal-answer").val("请先设定密保")
					$("#nosecret").show()
				}
			});
			new Withdrawal();
			//申请提款
			function Withdrawal() {
				var that = this;
				//设定只能输入数字
				NumberInput('withdrawal-money');
				//银行下拉
				var banks = $('#withdrawal-bankName');
				$.get('/asp/queryBankAll.aspx', function(data) {
					if(data.length > 0) {
						for(var i = 0; i < data.length; i++) {
							var $html = $("<option value='"+data[i].bankname+"' data-json='" + JSON.stringify(data[i]) + "'>" + data[i].bankname + "</option>")
							banks.append($html)
						}
						$('#withdrawal-cardno').val('已绑定' + data.length + '张银行卡');
						if(data.length >= 3) {
							$("#withdrawal-binding").hide()
						}
					} else {
						$('#withdrawal-bankName').attr("disabled", 'disabled')
						$('#withdrawal-cardno').val("请先绑定银行卡");
					}
				})
				banks.change(function() {
					var jston =$('option:selected',this).data('json');
					if(!jston) return;
					$('#withdrawal-cardno').val(jston.bankno);
				})
				//提款类型下拉
				var typeCbo = $('#withdrawal-type')
				$.each([{
						value: 'slotmachine',
						name: '老虎机'
					},
					{
						value: 'liveall',
						name: '其他'
					}
				], function(i, item) {
					typeCbo.append('<option value="' + item.value + '">' + item.name + '</option>')
				});
				//				typeCbo.change(function() {
				//					var value =typeCbo.val()
				//					if(!value) {
				//						return
				//					}
				//				})
				var questionCbo = $("#withdrawal-question")
				$.each([{
						value: '1',
						name: '您最喜欢的明星名字？'
					},
					{
						value: '2',
						name: '您最喜欢的职业？'
					},
					{
						value: '3',
						name: '您最喜欢的城市名称？'
					},
					{
						value: '4',
						name: '对您影响最大的人名字是？'
					},
					{
						value: '5',
						name: '您就读的小学名称？'
					},
					{
						value: '6',
						name: '您最熟悉的童年好友名字是？'
					}
				], function(i, item) {
					questionCbo.append('<option value="' + item.value + '">' + item.name + '</option>')
				});

				//提交
				var maxTkMoney = 0; //最大可提款
				$('#withdrawal-submit').click(function() {
					var formData = {
						password: $('#withdrawal-password').val(),
						bankName: banks.val(),
						cardNo: $('#withdrawal-cardno').val(),
						addr: 'none',
						money: $('#withdrawal-money').val(),
						withdrawlType: typeCbo.val(),
						questionId: questionCbo.val(),
						answer: $('#withdrawal-answer').val()
					};
					if(typeCbo.val()==''){
						return alert("请选择提款类型")
					}
					if(formData.money==""){
						return alert("请输入提款金额")
					}
//					if(formData.money < 100) {
//						alert("[提示]老虎机/其它类佣金综合余额不足100无法提款");
//						return false;
//					}
//					if(formData.withdrawlType == 'slotmachine') {
//						var slotMoney = Number($('.num-account').html()); //老虎机佣金余额
//						if(formData.money > slotMoney) {
//							alert("[提示]最大提款金额为" + slotMoney + "元");
//							return false;
//						}
//					}
//					if(formData.money - maxTkMoney > 0) {
//						alert("[提示]最大提款金额为" + maxTkMoney + "元");
//						return false;
//					}

					//提款
					mobileManage.getLoader().open('处理中');
					mobileManage.getBankManage().withdrawal(formData, function(result) {
						if(result.success) {
							alert(result.message);
							$('#withdrawal-money').val('');
						} else {
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				});
//				$('#withdrawal-money').focus(function() {
//					var moneyAccount = $('.agent-slotAccount');
//					var tkType = $("#withdrawal-type").val()
//					var slotmachine = Number(moneyAccount.val());
//					var liveall = Number(moneyAccount.attr('data-liveall'));
//
//					maxTkMoney = (slotmachine + liveall).toFixed(2);
//					$.get('/asp/agentWithdrawpz.aspx', {
//						tkType: tkType,
//						slotmachine: Number(slotmachine).toFixed(2),
//						liveall: Number(liveall).toFixed(2)
//					}, function(data) {
//						$('#tkTip').html(data)
//					})
//				});
			}
		</script>

	</body>

</html>