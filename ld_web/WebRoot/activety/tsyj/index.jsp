<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>百家乐赢彩金</title>
		<link rel="stylesheet" href="/css/base.css?v=3" />
		<link rel="stylesheet" type="text/css" href="css/tsyj.css?v=3" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>

	<body id="pageTsyj">
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
			<div class="page-bg">
				<div class="banner-wp"></div>
				<div class="mobile-banner"><img src="img/mb-banner.jpg" alt=""></div>
				<div class="content-wp">
					<div class="content-box-bg">
						<div class="content-item content-item-1">
							<p><span class="c-yellow">活动时间：</span>2019年1月1日起</p>
							<p><span class="c-yellow">活动对象：</span>天威所有会员。</p>
							<p><span class="c-yellow">活动內容：</span>在真人百家乐进行投注，若在同一牌局中（庄家+闲家）出现3张8或3张9以上，即可享有百家乐奖金。</p>
						</div>
						<div class="content-item content-item-2">
							<div class="hover-item active">
								<i class="icon icon1"></i>
								<p>游戏ag</p>
								<p>真人百家乐</p>
							</div>						
							<div class="hover-item">
								<i class="icon icon2"></i>
								<p>该局出现</p>
								<p>3个8或9</p>
							</div>						
							<div class="hover-item">
								<i class="icon icon3"></i>
								<p>提供帐号</p>
								<p>联系客服申请</p>
							</div>						
							<div class="hover-item nomg">
								<i class="icon icon4"></i>
								<p>站内信查收</p>
								<p>红包代码</p>
							</div>
						</div>
						<div class="content-item content-item-3">
							<table class="table">
								<thead>
									<tr><th>同一局号出现</th><th>彩金</th><th>流水倍数</th></tr>
								</thead>
								<tbody>
									<tr><td>3张8</td><td>88</td><td rowspan="4">10</td></tr>
									<tr><td>3张9</td><td>88</td></tr>
									<tr><td>4张8</td><td>1288</td></tr>
									<tr><td>4张9</td><td>1288</td></tr>
								</tbody>
							</table>
						</div>
						<div class="content-item content-item-4">
							<p>1、本活动仅限于在AG,百家乐中投注，每笔投注额最低为50元或以上有效投注。</p>
							<p>2、天生赢家每天可申请1次，彩金以红包优惠卷方式派发，达到流水即可提款。</p>
							<p>3、申请方式：最迟于次日联系在线客服，提供会员账号和出现3张8或3张9以上局号，申请相应奖励。</p>
							<p>4、本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一/相似IP地址，同一银行卡，同一电脑等其他任何不正常行为，一经发现，我们将保留冻结帐户盈利及余额的权利。</p>
							<p>5、本活动天威娱乐享有最终解释权。</p>
						</div>
						<div class="icons-box">
							<i class="icon-cm1"></i>
							<i class="icon-cm2"></i>
							<i class="icon-cm3"></i>
							<i class="icon-pk1"></i>
							<i class="icon-pk2"></i>
						</div>
					</div>
				</div>
			</div>
		<jsp:include page="${ctx}/activety/common/activety_footer.jsp"></jsp:include> 
		<script>
			$('.hover-item').on('mouseover',function () {
				$(this).addClass('active').siblings('.active').removeClass('active');
			})
		</script>
	</body>

</html>