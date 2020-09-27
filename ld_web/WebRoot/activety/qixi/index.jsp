<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>七夕牵红线</title>
		<link rel="stylesheet" href="/css/base.css?v=3" />
		<link rel="stylesheet" type="text/css" href="css/tsyj.css?v=3" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>

	<body id="pageQixi">
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
			<div class="page-bg">
				<div class="banner">
					<img class="bg12" src="./img/qhx.png">
					<div class="w-1200 banner-box"  id="scene">
						<!-- <div class="inner cloud-left" data-depth="0.5"></div>
						<div class="inner qixi-bg" data-depth="0.9"></div>
						<div class="inner xawx" data-depth="0.7"></div>
						<div class="inner cloud-right" data-depth="0.5"></div> -->
					</div>
				</div>
				<div class="page2">
					
				</div>
				<div class="page3">
					<div class="w-1200 page3-box">
						<div class="xin-box">
							<span class="xin"></span>
							<span class="xin"></span>
							<span class="xin"></span>
							<div class="xin"></div>
							<div class="xin"></div>
							<!-- <ul class="line">
								<li class="line1"></li>
								<li class="line2"></li>
								<li class="line3"></li>
								<li class="line4"></li>
								<li class="line5"></li>
								<li class="line6"></li>
							</ul> -->
						</div>
						<div class="btn-nbox">
							<span class="qixi-btn"></span>
							<a  class="transfor transfor_pc" href="/userManage.jsp?tab_mrrw">点我去转账>>></a>
							<a  class="transfor transfor_moble" href="/mobile/new/everyday/hongbao.jsp">点我去转账>>></a>
						</div>
					</div>
				</div>
				<div class="w-1200 page4">
					<table class="text-table">
						<tr>
							<td class="title"  valign="top">活动对象：</td>
							<td>天威所有会员。</td>
						</tr>
						<tr>
							<td class="title"  valign="top">活动时间：</td>
							<td>2018年8月1日 — 2018年8月31日。</td>
						</tr>
						<tr>
							<td class="title"  valign="top">活动内容：</td>
							<td>在活动时间内系红线，累积缘分值，达到指定缘分值可获得七夕彩金奖励。</td>
						</tr>
					</table>
					<table class="table">
						<tr>
							<th width="33%">存款要求</th>
							<th width="33%">红线缘分值</th>
							<th width="33%">彩金</th>
						</tr>
						<tr>
							<td>当天累计存款100</td>
							<td>+2</td>
							<td>8</td>
						</tr>
						<tr>
							<td>当天累计存款500</td>
							<td>+5</td>
							<td>18</td>
						</tr>
						<tr>
							<td>当天累计存款3000</td>
							<td>+8</td>
							<td>28</td>
						</tr>
						<tr>
							<td>当天累计存款5000</td>
							<td>+15</td>
							<td>38</td>
						</tr>
						<tr>
							<td>当天累计存款10000</td>
							<td>+20</td>
							<td>88</td>
						</tr>
					</table>
					<table class="text-table">
						<tr>
							<td class="title"  valign="top">温馨提示：</td>
							<td>存款越多，获取的红包越多。</td>
						</tr>
						<tr>
							<td  valign="top">例如：</td>
							<td>当天存款500元则可领取8元，18元，(1+1)=2个红包次数。</td>
						</tr>
					</table>
					<table class="text-table">
						<tr>
							<td class="title"  valign="top">活动规则：</td>
							<td></td>
						</tr>
					</table>
					<table class="text-table">
						<tr>
							<td valign="top">1、</td>
							<td>当日达到累计存款额度即可领取对应彩金。</td>
						</tr>
						<tr>
							<td valign="top">2、</td>
							<td>情人节红包需金额满10元才可转出PT/TTG/老虎机钱包（DT、MG、QT、NT、PNG、SW），
      						任一平台进行游戏。完成5倍流水即可提款。</td>
						</tr>
						<tr>
							<td valign="top">3、</td>
							<td>如当日存款达到，请即时领取彩金，如超过时间领取将视为自动放弃领取。</td>
						</tr>
						<tr>
							<td valign="top">4、</td>
							<td>若会员有任何违反规定行为天威将视情况扣除您盈利与本金的权利。</td>
						</tr>
						<tr>
							<td valign="top">5、</td>
							<td>优惠活动只针对娱乐性质，如发现用户拥有超过一个账户，包括同一姓名，同一邮件地址，同一相似IP地址，同一住址，同一借记卡信用卡，同一银行账户，同一计算机等其他任何不正常投注行为，一经发现，天威娱乐将保留冻结您的账户盈利及余额的权利。
							</td>
						</tr>
						<tr>
							<td valign="top">6、</td>
							<td>天威保留对本次活动的修改，修订和最终解释权，以及在无通知的情况下修改本次活动的权利。</td>
						</tr>
					</table>
					
				</div>	
			</div>
			<div class="modal-qixi-selecty">
				<h4 class="modal-title text-center">存款红包 <b class="close">×</b></h4>
				<div class="modal-qixi-content" >
					<div class="item"><label>红包余额：</label><span id="hbMoney" class="moeny">12</span></div>
					<div class="item"><label>红包类型：</label><select id="select-qixi"></select> <a id="lqu-btn" class="btn">领取红包</a></div>	
				</div>
			</div>
			<div class="back-bg"></div>
		<jsp:include page="${ctx}/activety/common/activety_footer.jsp"></jsp:include> 
		<!-- <script type="text/javascript" src="./js/parallax.js"></script> -->
		<script>
			
			$(function(){
				// $('.bg12').attr('src', './img/qhx.png').on('load', function() {
				//    $(this).remove(); 
				// 	$('.banner-box').addClass('banner-box-cur');
				// });
				//查询玩家红包余额
				function getHBMoney(type) {
					$("#hbMoney").html("<img src='../images/waiting.gif'>");
					$.post("/asp/getHBMoney.aspx", { "type": 0 }, function(returnedData, status) {
						if("success" == status && returnedData != '') {
							$("#hbMoney").html("" + returnedData + "元");
						}
					});
					queryHBSelect();
				}
				getHBMoney()
				//查询选项 
				function queryHBSelect() {
					$.post("/asp/queryHBSelect.aspx", function(returnedData, status) {
						$("#select-qixi").html(returnedData);
					});
				}

				//查询今日充值金额
				function queryMoeny(){
					$.post("/asp/getdepositAmout.aspx", function(returnedData, status) {
						line(returnedData)
					});
				}
				queryMoeny()

				// 当天领取金额达到一定数额 亮红心
				function line(nuber){
					if(nuber >= 100){
						$('.xin').eq(0).addClass('cur')
					}
					if(nuber >= 500){
						$('.xin').eq(1).addClass('cur')
					}
					if(nuber >= 3000){
						$('.xin').eq(2).addClass('cur')
					}
					if(nuber >= 5000){
						$('.xin').eq(3).addClass('cur')
					}
					if(nuber >= 10000){
						$('.xin').eq(4).addClass('cur')
					}
				}
				//点击领取金额
				if ('${session.customer==null}' == 'true') {
		                $('.transfor').hide();
		            } 
				function pullMoeny(sid){
		            $.post("/asp/doHB.aspx",{'sid':sid} ,function(returnedData, status) {
						alert(returnedData)
					});
				}
				$('.qixi-btn').click(function(){
					if ('${session.customer==null}' == 'true') {
					    $('#loginBtn').trigger('click'); 
		                return;
		            } 
					$('.modal-qixi-selecty').show();
					$('.back-bg').show();
				})

				$('#lqu-btn').click(function(){
					var  sid =  $("#select-qixi").val();
					if(sid==''){
						alert('请选择') 
						return;
					}
					pullMoeny(sid)
				})

				$('.modal-qixi-selecty .close').click(function(){
					$('.modal-qixi-selecty').hide();
					$('.back-bg').hide();
				})
			})
		</script>
	</body>

</html>