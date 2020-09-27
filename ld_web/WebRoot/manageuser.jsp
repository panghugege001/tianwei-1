<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/user.css?v=1125" />
		<script src="js/lib/jquery-1.11.2.min.js"></script>
		<script src="js/user/usercheckin.js?v=5"></script>
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
					<div class="gb-main-r tab-bd user-main">
						<ul class="tab-muen-item clearfix user_mull" id="user_mull">
							<li class="active">
								<a href="#tab-user" data-toggle="tab">每日签到</a>
							</li>
							<li>
								<a href="#tab-money" data-toggle="tab" aria-expanded="false">存款红包</a>
							</li>
							<!-- <li>
								<a href="#tab-cunsong" data-toggle="tab" aria-expanded="false">每日存送</a>
							</li> -->
						</ul>
						<div id="tab-user" class="tab-panel active letter-c tab_box">
							<div class="tab-box" id="tab-checkIn">
								<div class="tab-bd">
									<div class="tab-bd-box">
										<!--签到中心-->
										<div id="tab-checkInCenter" class="tab-box">

											<div class="qiandao-con clearfix">

												<div class="qiandao-left">
													<div class="qiandao_top">
														<p>签到余额:<span id="todayGet" class="todayGet">0.00</span><span class="btn_qiandao">立刻签到</span></p>
														<div class="qiandao-top">
															<div class="just-qiandao qiandao-sprits" id="j-qdA">立即签到</div>
														</div>
														<span class="xian_no1"></span>
													</div>
													<div class="qiandao-bottom">
														<h3>温馨提示</h3>
														<p>
															连续签到7日，并达到每日存款要求， 即可领取红包存送优惠券。
														</p>
														<span class="show_qiandao">签到说明</span>
													</div>
												</div>

												<div class="qiandao-right">
													<div class="qiandao-left-top clearfix">
														<div class="current-date" style="text-align: center;font-size: 20px;"></div>
													</div>
													<ol class="cycle">
														<li>周日</li>
														<li>周一</li>
														<li>周二</li>
														<li>周三</li>
														<li>周四</li>
														<li>周五</li>
														<li>周六</li>
													</ol>
													<div class="qiandao-main" id="js-qiandao-main">
														<ul class="qiandao-list clearfix" id="js-qiandao-list">
														</ul>
													</div>
												</div>
											</div>
										</div>
										<!--签到说明-->
										<div id="tab-checkInInfo" class="tab-box tab-panel">
											<h3 class="qiandaotetle">签到说明</h3>
												<div class="modal-content" role="document">
													<div class="modal-hd">
														<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
														<h4 class="modal-title text-center">每日签到、得好礼</h4>  
													</div>
													<div class="pro-text">
														<h2>每日签到:每日登入活跃获彩金</h2>
														<h3>活动对象：</h3>
														<p>天威所有会员</p>
														<h3>活动时间:</h3>
														<p>长期有效</p> 
														<h3>活动内容：</h3>
														<p>当月存款10元以上，每天即可在个人中心签到，每次签到彩金0.5元会自动转入『签到余额』，当累计超过10元以上，即可在个人中心-转账，选择签到余额转入老虎机平台游戏。</p>
														<h3>活动规则：</h3>
														<p>1.此活动彩金需完成10倍流水即可提款。</p>
														<p>2.礼金账户累计大于10元，即可在『自助优惠』→『每日签到』选择转至老虎机游戏。</p>
														<p>3.本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一/相似IP地址，同一银行卡，同一电脑等其他任何不正常行为，一经发现，我们将保留冻结帐户盈利及余额的权利。</p>
														<p>4.本活动天威娱乐享有最终解释权。</p>
													</div>
												</div>	
										</div>
									</div>
								</div>
							</div>
						</div>
						<div id="tab-money" class="tab-panel letter-h tab_box">
							<div class="form_box meirhb">
								<form method="post" name="form2">
									<table border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td>红包余额：</td>
											<td><span id="hbMoney"></span></td>
										</tr>
										<tr>
											<td>红包类型：</td>
											<td>
												<select id="hbSelect" name="hbSelect" class="input">

												</select>
											</td>
											<td class="sub" id="lingqu">
												<input type="button" value="领取红包" onclick="return doHB();" />
											</td>
										</tr>
										<tr>
											<td>红包使用：</td>
											<td>
												<select id="hbType" name="hbType" class="input">
													<option value="0" selected="selected">请选择</option>
													<option value="pt"> PT账户</option>
													<option value="ttg"> TTG账户</option>
													<option value="nt"> NT账户</option>
													<option value="qt"> QT账户</option>
													<option value="mg"> MG账户</option>
													<option value="dt"> DT账户</option>
												</select>
											</td>
										</tr>
										<tr>
											<td>转账金额：</td>
											<td><input type="text" class="text" id="hbRemit"></td>
										</tr>
										<tr>
											<td colspan="2" class="sub" id="tijiao" style="padding-top: 30px;">
												<input type="button" onclick="return submitHBRemit();" class="mt10" value="提交" style="margin-left:180px; padding: 0 50px;">
											</td>
										</tr>
									</table>
								</form>
								<p style="">
									<span class="c-huangse">温馨提示:</span><br /> 1.同时段红包只能领取一类红包
									<br /> 2.满10元可以转到任意的平台，需5倍流水
								</p>
							</div>
						</div>
						<div id="tab-cunsong" class="tab-panel letter-h tab_box">
							<form class="ui-form" style="font-size: 14px;">
								<div class="ui-form-item">
									<label class="ui-label">存送优惠类型：</label>
									<select name="youhuiType" id="youhuiType1" class="ui-ipt" onchange="youHuiTypeChange1(this.value);">
										<option value="">---请选择存送类型---</option>
									</select>
								</div>

								<div class="ui-form-item">
									<label class="ui-label">转账金额：</label>
									<input class="ui-ipt" type="text" name="transferMoney" id="transferMoney1" onblur="getSelfYouhuiAmount1(this.value);" />
								</div>

								<div class="ui-form-item">
									<label class="ui-label">红利金额：</label>
									<input class="ui-ipt" readonly type="text" name="giftMoney1" id="giftMoney1" />
								</div>

								<div class="ui-form-item">
									<label class="ui-label">流水倍数：</label>
									<input class="ui-ipt" type="text" name="waterTimes" id="waterTimes1" readonly="readonly" />
								</div>
								<div class="ui-form-item" id="tijiao2">
									<input type="button" class="btn" value="提交" onclick="return checkSelfYouHuiSubmit1();" />
								</div>
							</form>
							<div class="prompt-info" style=" padding-left: 150px; color: #727272; padding-bottom: 20px;">
								<h3 class="c-huangse">温馨提示</h3>
								<p>1.每天 00:00 - 01:00 系统结算时间,暂无法使用每日存送。</p>
								<p>2.每日存送最低存款10元即可申请。</p>
								<p>3.每日存送申请成功后系统会自动派发您相应的游戏平台账户，请登录查看并游戏。</p>
							</div>
						</div>
					</div>

				</div>

			</div>

		</div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		<script type="text/javascript" src="${ctx}/js/self.js?v=26"></script>
		<script>
			//---存提款红包

			//查询玩家红包余额
			function getHBMoney(type) {
				$("#hbMoney").html("<img src='/images/waiting.gif'>");
				$.post("/asp/getHBMoney.aspx", { "type": 0 }, function(returnedData, status) {
					if("success" == status && returnedData != '') {
						$("#hbMoney").html("" + returnedData + "元");
					}
				});
				queryHBSelect();
			}
			// 获取玩家可领红包

			function queryHBSelect() {
				$.post("/asp/queryHBSelect.aspx", function(returnedData, status) {
					console.log(returnedData);
					if("success" == status) {
						$("#hbSelect").html("");
						$("#hbSelect").html(returnedData);
					}
				});
			}

			function doHB() {
				var hbSelect = $("#hbSelect").val();
				if(hbSelect == null || hbSelect == '') {
					alert("请选择红包类型！");
					return;
				}
				openProgressBar();
				console.log(hbSelect)
				$.post("/asp/doHB.aspx", {
					"sid": hbSelect
				}, function(returnedData, status) {
					if("success" == status) {
						closeProgressBar();
						getHBMoney();
						alert(returnedData);
					}
				});
				return false;

			}

			function submitHBRemit() {
				var hbType = $("#hbType").val();
				var hbRemit = $("#hbRemit").val();
				if(hbType == "") {
					alert("请选择平台！");
					return false;
				}
				if(hbMoney != "") {
					if(isNaN(hbRemit)) {
						alert("转账金额非有效数字！");
						return false;
					}
					if(hbRemit < 10) {
						alert("转账金额必须大于10！");
						return false;
					}
				}
				openProgressBar();
				$.post("/asp/submitHBRemit.aspx", {
					"type": hbType,
					"transferGameIn": hbRemit
				}, function(returnedData, status) {
					if("success" == status) {
						closeProgressBar();
						getHBMoney();
						alert(returnedData);
					}
				});
				return false;
			}

			getHBMoney();
			// ---存提款红包结束
		</script>
		<script>
			$(function() {
				$("#tab-money").removeClass("active");
				
				$(".show_qiandao").click(function(){
					$("#tab-checkInCenter").hide();
					$("#tab-checkInInfo").show();
				})
				$("#user_mull li").eq(0).click(function(){
					$("#tab-checkInCenter").show();
					$("#tab-checkInInfo").hide();					
				})
			})
		</script>
	</body>

</html>