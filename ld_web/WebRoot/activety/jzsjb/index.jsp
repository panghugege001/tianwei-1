<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>决战世界杯</title>
		<link rel="stylesheet" type="text/css" href="css/index.css?v=1" />
		<link rel="stylesheet" href="/css/base.css?v=2" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>

	<body id="ztid">
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
		<div class="bg-wp">
			<div class="banner-wp">
				<img src="img/text.png" class="textPng" alt="">
			</div>
			<div class="mobile-banner">
				<img src="img/mobile-banner.jpg" alt="">
			</div>
			<div class="container-wp">
				<img src="img/nme.png" alt="" class="nme">
				<a href="javascript:;" onclick="runDtGame();" class="apply-btn">
					<img src="img/ball.png" class="football" alt="">
					<img src="img/btn.png" class="btnimg" alt="">
				</a>
				<div class="title-box">
					<p><span class="c-yellow">活动时间：</span>2018年8月1日00:00 — 8月31日23:59。</p>
					<p><span class="c-yellow">活动对象：</span>天威所有会员。</p>
					<p><span class="c-yellow">活动內容：</span>跟上世界杯的节奏，踢中奖池高兴之余，天威也替您喝彩在额外赠送高达8888元彩金。</p>
				</div>
				<div class="step-box clearfix">
					<a href="javascript:;" class="step-item"><img src="img/01.png" alt=""><i class="arrow-right"></i></a>
					<a href="javascript:;" class="step-item md"><img src="img/02.png" alt=""><i class="arrow-right"></i></a>
					<a href="javascript:;" class="step-item"><img src="img/03.png" alt=""></a>
				</div>
				<div class="table-box">
					<table class="table">
						<thead>
							<tr>
								<th>奖池中奖金额</th><th>额外赠送彩金</th><th>流水倍数</th>
							</tr>
						</thead>
						<tbody>
							<tr><td>300-600</td><td>88</td><td>5</td></tr>
							<tr><td>600-1000</td><td>128</td><td>5</td></tr>
							<tr><td>1000-1500</td><td>888</td><td>10</td></tr>
							<tr><td>5000-20000</td><td>1288</td><td>10</td></tr>
							<tr><td>20000以上</td><td>8888</td><td>20</td></tr>
						</tbody>
					</table>
				</div>
				<div class="ruler-box">
					<h3>活动规则</h3>
						<p>1、选择DT-决战世界杯游戏，踢中奖池，额外再送您最高8888彩金。</p>
						<p>2、申请方式：联系官网24小时在线客服，提供游戏帐号，游戏时间，客服审核后专员即可派发，彩金以红包卷方式派发到您站内信。</p>
						<p>3、注意事项：一个会员每天仅可申请一次，以0:00-24:00计数，当天中奖当天申请，超过24点申请无效，若使用同一局游戏反覆申请，天威会取消您的资格。</p>
						<p>4、活动彩金，仅限于在PT / TTG / SLOT老虎机游戏平台进行使用。</p>
						<p>5、本次优惠和返水共享。</p>
						<p>6、凡是只靠此优惠套取优惠者，我们有权发至游戏平台方审核，严重者扣除盈利及本金并且禁用会员账号。</p>
						<p>7、天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
				</div>

			</div>
		</div>
		<input type="hidden" id="j-isLogin" value="${session.customer!=null}">
		<jsp:include page="${ctx}/activety/common/activety_footer.jsp"></jsp:include>
	</body>
	<script>
		$(document).ready(function($) {
		$('.nme').animate({
			opacity: 1},
			1500, function() {
		});
		});

		function runDtGame () {
			if($('#j-isLogin').val() == 'true'){
				window.location.href = "/game/gameLoginDT.aspx?&gameCode=worldCup5x25&isfun=0&language=zh_CN&clientType=0"
			}else {
				alert("请您登录后再尝试！")
			}
		}
	</script>
</html>