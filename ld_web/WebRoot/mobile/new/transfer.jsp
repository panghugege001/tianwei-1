<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE >

<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="转账" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body style="background: #eee;">

		<div class="email-nav">
			<div class="item active">户内转账</div>
			<div class="item ">红包转账</div>
		</div>
		<div id="tab-warp">
			<div id="funds-transfer-page">
				<div class="form-warp no-icon">
					<div class="form-tips">来源账户
						<small>(余额 <span id='transfer-source-credit' class="c-red">0.00</span>
						<img  style="width:1rem;" class="hidden" src="/mobile/img/loading.gif" alt="" />元)</small>
					</div>
					<div class="form-group">
						<select class="form-control" id="transfer-source">
							<option value="">请选择</option>
						</select>
					</div>
					<div class="form-tips">
						目标账户<small>(余额 <span id="transfer-target-credit" class="c-red">0.00</span>
						<img style="width:1rem;" class="hidden" src="/mobile/img/loading.gif" alt="" /> 元)</small>
					</div>
					<div class="form-group">
						<select class="form-control" id="transfer-target">
							<option value="">请选择</option>
						</select>
					</div>
					<div class="form-tips">转账金额</div>
					<div class="form-group ">
						<input type="text" id="transfer-money" class="form-control" placeholder="0.00" />
					</div>

					<div class="btn-submit" id="transfer-submit">确认</div>
					<div class="text-tips">
						<div class="h3"><strong>温馨提示：</strong></div>
						<ol>
							<li>老虎机账户"支持SW、DT、MG、PNG、QT、NT，若要游玩以上平台请将额度转入"老虎机账户"即可。</li>
							<li>请在户内转账前进行平台激活方可转账成功。</li>
							<li>户内转账只支持整数转账。</li>
							<li>进行户内转账时，请先关闭正在进行的游戏页面，避免出现错误。</li>
						</ol>
					</div>
				</div>
			</div>
			<div class="form-warp no-icon hidden">

				<div class="form-tips">来源账户</div>
				<div class="form-group">
					<select id="redRainOut" onchange="redRainMoneryOut(this.value);" class="form-control">
						<option value="redrain" selected="selected"> 红包账户</option>
						<option value="self">天威账户</option>
					</select>
				</div>
				<label class="form-tips">来源余额：</label>
				<div class="form-group">

					<input id="redRainOutDiv" type="text" class="form-control" disabled readonly value="0.00" />

				</div>
				<!--<div class="change-button flaticon-exchange1" id="transfer-change"></div>-->
				<label class="form-tips">目标账户：</label>
				<div class="form-group  ">
					<select id="redRainIn" onchange="redRainMoneryIn(this.value);" class="form-control">
					</select>
				</div>
				<label class="form-tips">目标余额：</label>
				<div class="form-group ">
					<input id="redRainInDiv" type="text" class="form-control" disabled readonly value="0.00" />
				</div>
				<label class="form-tips">转账金额：</label>
				<div class="form-group ">
					<input id="redRainMoney" class="form-control" type="text" placeholder="请输入需要转账的金额" />
				</div>
				<div class="btn-submit " onclick="return redRainMonery();">确认</div>
				<div class="text-tips">
					<div class="h3"><strong>温馨提示：</strong></div>
					<ol>
						<li>存款红包一天可领取一次。</li>
						<li>满10元可以转到任意游戏平台，1倍流水即可提款。</li>
					</ol>
				</div>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			$('.email-nav .item').click(function() {
				$(this).addClass('active').siblings().removeClass('active')
				$('#tab-warp>div').eq($(this).index()).show().siblings().hide();
			})

			function TransferPage() {
				var that = this;
				var _sourceAjax;
				var _targetAjax;
				//设定只能输入数字
				NumberInput('transfer-money');
				//下拉资料来源
				that.accountData = [{
						value: 'self',
						name: '天威账户'
					},
					{
						value: 'mg',
						name: 'MG老虎机账户'
					},
					{
						value: 'dt',
						name: 'DT老虎机账户'
					},
					{
						value: 'cq9',
						name: 'CQ9老虎机账户'
					},
					{
						value: 'pg',
						name: 'PG老虎机账户'
					},
					/* {
						value: 'agin',
						name: 'AG国际厅账户'
					},
					{
						value: 'newpt',
						name: 'PT账户'
					},
					{
						value: 'ttg',
						name: 'TTG账户'
					}, */
					// {
					// 	value: 'nt',
					// 	name: 'NT账户'
					// },
					// {
					// 	value: 'qt',
					// 	name: 'QT账户'
					// },
					// {
					// 	value: 'dt',
					// 	name: 'DT账户'
					// },
					 /* {
					 	value: 'mg',
					 	name: 'MG账户'
					 }, */
					// {
					// 	value: 'png',
					// 	name: 'PNG账户'
					// },
					{
						value: 'qd',
						name: '签到账户'
					}
					/* {
						value: 'sba',
						name: '沙巴体育账户'
					},
					{
						value: 'n2live',
						name: 'N2Live账户'
					},
					{
						value: 'mwg',
						name: 'MWG大满贯帐户'
					},
					{
						value: 'chess',
						name: '761棋牌'
					},
					{
						value: 'kyqp',
						name: '开元棋牌'
					},
					{
						value: 'vr',
						name: 'VR彩票'
					},
					{
						value: 'fish',
						name: '捕鱼帐户'
					},
					{
						value: 'pb',
						name: '平博体育'
					},
					{
						value: 'bbin',
						name: 'BBIN帐户'
					},
					{
						value: 'slot',
						name: '老虎机账户(SW,MG,DT,PNG,QT,NT)'
					},
					{
						value: 'fanya',
						name: '泛亚电竞'
					} */

				];

				//目标下拉资料来源
				that.xialaData = [{
						value: 'self',
						name: '天威账户'
					},
					/* {
						value: 'agin',
						name: 'AG账户'
					},
					{
						value: 'newpt',
						name: 'PT账户'
					},
					{
						value: 'ttg',
						name: 'TTG账户'
					}, */
					{
					 	value: 'mg',
					 	name: 'MG老虎机账户'
					},
					{
					 	value: 'cq9',
					 	name: 'CQ9老虎机账户'
					},
					{
					 	value: 'pg',
					 	name: 'PG老虎机账户'
					},
					{
						value: 'qd',
						name: '签到账户'
					},
					/* {
						value: 'sba',
						name: '沙巴体育账户'
					},
					{
						value: 'n2live',
						name: 'N2Live账户'
					},
					{
						value: 'mwg',
						name: 'MWG大满贯帐户'
					},
					{
						value: 'chess',
						name: '761棋牌'
					},
					{
						value: 'kyqp',
						name: '开元棋牌'
					},
					{
						value: 'vr',
						name: 'VR彩票'
					},
					{
						value: 'fish',
						name: '捕鱼帐户'
					},
					{
						value: 'pb',
						name: '平博体育'
					},
					{
						value: 'bbin',
						name: 'BBIN帐户'
					},
					{
						value: 'slot',
						name: '老虎机账户(SW,MG,DT,PNG,QT,NT)'
					},
					{
						value: 'mgSlot',
						name: 'MG老虎机账户'
					}, */
					/* {
						value: 'dtSlot',
						name: 'DT老虎机账户'
					}, */
					{
						value: 'dt',
						name: 'DT老虎机账户'
					}/* ,
					{
						value: 'fanya',
						name: '泛亚电竞'
					} */

				];
				var $transferSource = $("#transfer-source"),
					$transferTarget = $("#transfer-target");
				//来原账户下拉
				$.each(that.accountData, function(i, item) {
					$transferSource.append('<option value="' + item.value + '">' + item.name + '</option>')
				})
				$transferSource.change(function() {
					var notNeedShowList = $('#m-transfer-target option:not([value="newpt"],[value="ttg"],[value="slot"])');
					if($transferSource.val() == 'self') {
						$transferTarget.find('option[value="self"]').hide();
						$transferTarget.find('option[value="qd"]').hide();
					} else {
						$transferTarget.find('option[value="self"]').show();
					}
					$transferTarget.val('')
					if($transferSource.val() == 'qd') {
						notNeedShowList.hide()
					} else {
						notNeedShowList.show()
					}
					querySourceAmout($transferSource.val());
				})

				//目标账户下拉
				$.each(that.xialaData, function(i, item) {
					$transferTarget.append('<option value="' + item.value + '">' + item.name + '</option>')
				})

				$transferTarget.change(function() {
					queryTargetAmout($transferTarget.val());
				})
				that.transferMoney = $('#transfer-money');
				that.transferSubmit = $('#transfer-submit');
				that.transferSubmit.click(doTransger);

				//转账
				function doTransger() {
					var formData = {
						transferGameIn: $transferTarget.val(),
						transferGameOut: $transferSource.val(),
						money: that.transferMoney.val()
					};
					mobileManage.getLoader().open('转账中');
					mobileManage.getBankManage().transfer(formData, function(result) {
						if(result.success) {
							alert(result.message);
							//更新游戏平台余额
							querySourceAmout($transferSource.val());
							queryTargetAmout($transferTarget.val());
							//更新账户余额
							queryCredit();
							that.transferMoney.val('');
						} else {
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}

				//查询平台余额
				function queryCredit() {
					//先查询天威平台余额
					mobileManage.getUserManage().getCredit(
						function(result) {
							if(result.success) {
								$("#credit").html(result.message + " 元");
							} else {
								$("#credit").html('系统繁忙中');
								alert(result.message);
							}
							$('.refre').removeClass('credit-query');
						}
					);
				}

				//查詢来源余额
				function querySourceAmout(code) {
					if(_sourceAjax && _sourceAjax.readyState != 0) {
						_sourceAjax.abort();
					}
					var $credit = $('#transfer-source-credit');
					$credit.html('');
					$credit.next().css('display', 'inline-block');

					_sourceAjax = $.post('${ctx}/mobi/gameAmount.aspx', {
						"gameCode": code
					}, function(result) {
						$credit.next().css('display', 'none');
						$credit.html(result.message);
						$credit = null;
					}).fail(function(result) {
						if(result.statusText != 'abort') {
							$credit.next().css('display', 'none');
							$credit.html('系统繁忙中...');
						}
						$credit = null;
					});
				}

				//查詢目标余额
				function queryTargetAmout(code) {
					if(_targetAjax && _targetAjax.readyState != 0) {
						_targetAjax.abort();
					}
					var $credit = $('#transfer-target-credit');
					$credit.html('');
					$credit.next().css('display', 'inline-block');
					_targetAjax = $.post('${ctx}/mobi/gameAmount.aspx', {
						"gameCode": code
					}, function(result) {
						$credit.next().css('display', 'none');
						$credit.html(result.message);
						$credit = null;
					}).fail(function(result) {
						if(result.statusText != 'abort') {
							$credit.next().css('display', 'none');
							$credit.html('系统繁忙中...');
						}
						$credit = null;
					});
				}

			}
			new TransferPage();
		</script>

		<script type="text/javascript">
			/****/
			//显示游戏金额
			$(function() {
				var redRainOut = $("#redRainOut").val();
				var redRainIn = $("#redRainIn").val();
				redRainMoneryOut(redRainOut);
				redRainMoneryIn(redRainIn);
			})

			//获取游戏金额
			function redRainMoneryOut(gameCode) {
				$("#redRainOutDiv").val('');
				$("#redRainInDiv").val('0.00');
				if(gameCode == "self") {
					$.post("/asp/getGameMoney.aspx", {
						"gameCode": gameCode
					}, function(returnedData, status) {
						if("success" == status) {
							$("#redRainOutDiv").val(returnedData);
							$("#redRainIn").html('<option value="">请选择目标帐户</option>' + '<option value="redrain">红包账户</option>')
						}
					});
				} else if(gameCode == "redrain") {
					$.post("/asp/getGameMoney.aspx", {
						"gameCode": gameCode
					}, function(returnedData, status) {
						if("success" == status) {
							$("#redRainOutDiv").val(returnedData);
							$("#redRainIn").html(
								'<option value="">请选择目标帐户</option>' +
								//'<option value="newpt"> PT账户</option>' +
								//'<option value="ttg"> TTG账户</option>' +
								'<option value="mg"> MG账户</option>' +
								'<option value="dt"> DT账户</option>'+
								'<option value="cq9"> CQ9账户</option>'+
								'<option value="pg"> PG账户</option>' 
								//'<option value="slot"> 老虎机钱包(SW,MG,DT,PNG,QT,NT)</option>'
							)
						}
					});
				}
			}

			//获取游戏金额
			function redRainMoneryIn(gameCode) {
				$("#redRainInDiv").val('');
				if(gameCode == "redrain") {
					$.post("/asp/getGameMoney.aspx", {
						"gameCode": gameCode
					}, function(returnedData, status) {
						if("success" == status) {
							$("#redRainInDiv").val(returnedData);
						}
					});
				} else {
					$.post("/asp/getGameMoney.aspx", {
						"gameCode": gameCode
					}, function(returnedData, status) {
						if("success" == status) {
							$("#redRainInDiv").val(returnedData);
						}
					});
				}
			}

			//游戏转账
			function redRainMonery() {
				var redRainOut = $("#redRainOut").val();
				var redRainIn = $("#redRainIn").val();
				var redRainMoney = $("#redRainMoney").val();
				var rex = /^[0-9]+$/;
				if(redRainIn == "") {
					return alert("请选择目标账户!")
				}
				if(redRainOut && redRainIn && redRainMoney) {
					if(redRainMoney < 10) {
						alert("转帐金额不能少于10元!")
					} else if(!rex.test(redRainMoney)) {
						alert("转账金额只能是整数哦。");
					} else {
						mobileManage.getLoader().open('转账中');
						if(redRainOut == 'redrain') {
							$.post("/redrain/transferInforRedRain.aspx", {
								"signType": redRainIn,
								"redRainRemit": redRainMoney
							}, function(returnedData, status) {
								mobileManage.getLoader().close();
								if("success" == status) {
									redRainMoneryOut(redRainOut);
									redRainMoneryIn(redRainIn);
									alert(returnedData);
								}
							})
						} else {
							//主账户转红包雨账户
							$.post("/asp/updateGameMoney.aspx", {
								"transferGameIn": 'redrain',
								"transferGameMoney": redRainMoney
							}, function(returnedData, status) {
								mobileManage.getLoader().close();
								if("success" == status) {
									redRainMoneryOut(redRainOut);
									redRainMoneryIn(redRainIn);
									alert(returnedData);
								}
							})
						}
					}
					//红包账户转游戏平台

				} else {
					alert("请输入正确金额!")
				}
			}
		</script>
	</body>

</html>