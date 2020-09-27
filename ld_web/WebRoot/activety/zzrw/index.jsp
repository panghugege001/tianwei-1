<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>周周送金</title>
		<link rel="stylesheet" href="/css/base.css?v=3" />
		<link rel="stylesheet" type="text/css" href="css/index.css?v=1" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>

	<body id="pageZzrw">
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
		<div class="banner-wp">
			<div class="container">
				<div class="text-box">
					<img src="img/title.png" alt="">
					<div class="txt-p">
						<p><span class="c-yellow">活动对象：</span>天威全体会员</p>
						<p><span class="c-yellow">活动时间：</span>7月2号至7月31号</p>
						<p><span class="c-yellow">活动内容：</span>当周达到周存款活动或周流水活动任务，在未来七天<p>
						<p class="text-ind6">（下周）即可领取周周送彩金活动，每日领取最高</p>
						<p class="text-ind6">688彩金，一周有七天，天天领取688，简直暴力。</p>
					</div>
				</div>
			</div>
			<img src="img/girl.png" alt="" class="girl">
		</div>
		<div class="content-wp">
			<div class="container">
				<div class="center-box">
					<div class="tb-btn-box">
					<a href="javascript:;" class="tab-btn tab1 active">周存款</a>
					<a href="javascript:;" class="tab-btn tab2">周流水</a>
				</div>
				<div class="tab-con">
					<div class="tab-item active">
						<p class="c-yellow">7月2日00:00 - 7月8日23:59:59 举行周存款活动</p>
						<p class="c-yellow">7月16日00:00 - 7月22日23:59:59 举行周存款活动</p>
						<table class="table">
							<thead>
								<tr><th>活动</th><th>范围</th><th>彩金</th><th>流水</th></tr>
							</thead>
							<tbody>
								<tr>
									<td rowspan="5">周存款</td><td>5001-20000</td><td>18</td><td rowspan="5">10</td>
								</tr>
								<tr>
									<td>20001-50000</td><td>28</td>
								</tr>
								<tr>
									<td>50001以上</td><td>38</td>
								</tr>						
								<tr>
									<td>100000以上</td><td>58</td>
								</tr>						
								<tr>
									<td>800000以上</td><td>688</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="tab-item">
						<p class="c-yellow">7月9日00:00 - 7月15日23:59:59 举行周流水活动</p>
						<p class="c-yellow">7月23日00:00 - 7月29日23:59:59 举行周流水活动</p>
						<table class="table">
							<thead>
								<tr><th>活动</th><th>范围</th><th>彩金</th><th>流水</th></tr>
							</thead>
							<tbody>
								<tr>
									<td rowspan="5">周流水</td><td>50001-100000</td><td>18</td><td rowspan="5">10</td>
								</tr>
								<tr>
									<td>100001-500000</td><td>28</td>
								</tr>
								<tr>
									<td>500001以上</td><td>38</td>
								</tr>						
								<tr>
									<td>1000000以上</td><td>58</td>
								</tr>						
								<tr>
									<td>5000000以上</td><td>688</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<p><span class="c-yellow">例如：</span>天威二哥在6/11参加周流水活动，6/11 - 6/17 天威二哥周流水达到880,000，第二周结束后，</p>
				<p class="text-ind4">在未来七天（第三周）每天皆可领取58元红包。</p>
				<h3 class="ruler-title">
					活动规则：
				</h3>
				<div class="ruler-box">
					<p>1、存款活动：天威所有游戏会员皆可参与。</p>
					<p>2､流水活动：仅限于PT / TTG / SLOT老虎机游戏平台。</p>
					<p>3､计算一周活动时间，彩金于活动结束次日18点后，联系在线客服领取，达到10倍流水即可提款。</p>
					<p>4､此活动无需报名、达到活动要求的玩家，在当周活动结束后，次日18点后，联系在线客服领取，派发彩金给您。（每日彩金仅限当天联系在线客服，不能补发）</p>
					<p>5､此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名、同一邮件地址、同一相似IP地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑等其他任何不正常投注行为，一经发现，天威将保留冻结您的账户盈利及余额的权利。</p>
					<p>6、此活动天威具有最终解释权。</p>
				</div>
				</div>

			</div>
		</div>
		<jsp:include page="${ctx}/activety/common/activety_footer.jsp"></jsp:include>
		<script>
			$('.tab-btn').on('click',function () {
				var $this = $(this),index = $this.index();
				$(this).addClass('active').siblings('.active').removeClass('active');
				$('.tab-con').find('.tab-item').eq(index).show().siblings().hide();
			})
		</script>
		<script>
			$(document).ready(function($) {
				$('.girl').animate({
					right: 140,
					opacity: 1},
					800, function() {
					/* stuff to do after animation is complete */
				});
			});
		</script>
	</body>

</html>