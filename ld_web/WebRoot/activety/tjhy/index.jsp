<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>推荐返利</title>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" href="/css/base.css?v=2" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>

	<body>
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
		<style>
			.activety_header {
				background-color: #2c8ba3;
			}
			.activety_logo {
				left:-140px;
			}
		</style>
		<div class="banner-wp">
			<div class="container-box">
				<div class="banner-text">
					<p><span class="lable">活动对象</span>&nbsp;&nbsp;天威所有会员</p>
					<p><span class="lable">活动时间</span>&nbsp;&nbsp;2019年1月1日起</p>
					<p><span class="lable">活动内容</span>&nbsp;&nbsp;呼朋引伴，只要你的朋友在你的链结下注册，并且存款达到指定要求，<p/>
					<p><span style="margin-right:111px"></span>&nbsp;&nbsp;推荐人即可获得相应彩金，最高无上限!</p>
				</div>
					<img src="img/boy.png" alt="" class="boy">
			</div>
		</div>
		<div class="mobile-banner">
			<img src="img/mobile-banner.jpg" alt="">
		</div>
		<div class="content-wp">
			<div class="container-box">
				<div class="text-row"><img src="img/text1.png" alt=""></div>
				<div class="link-box clearfix">
					<a href="/asp/payPage.aspx?tab-friends" target="_blank" class="link-item zhgl ss active">
						<i></i>
						<div class="link-text">
							<h3>帐户管理</h3>
							<p>推广好友</p>
						</div>
					</a>
					<a href="/mobile/new/friends.jsp" target="_blank" class="link-item zhgl-mobile ss active">
						<i></i>
						<div class="link-text">
							<h3>帐户管理</h3>
							<p>推广好友</p>
						</div>
					</a>					
					<a href="/asp/payPage.aspx?tab-friends" target="_blank" class="link-item bj zhlj">
						<i></i>
						<div class="link-text">
							<h3>推荐链接</h3>
							<p>邀请注册</p>
						</div>
					</a>
					<a href="/mobile/new/friends.jsp" target="_blank" class="link-item bj zhlj-mobile">
						<i></i>
						<div class="link-text">
							<h3>推荐链接</h3>
							<p>邀请注册</p>
						</div>
					</a>					
					<a href="/asp/payPage.aspx?tj-link" target="_blank" class="link-item wk btjr">
						<i></i>
						<div class="link-text">
							<h3>被推荐人</h3>
							<p>完成存款并游戏</p>
						</div>
					</a>
					<a href="/mobile/new/friends.jsp?tj-link" target="_blank" class="link-item wk btjr-mobile">
						<i></i>
						<div class="link-text">
							<h3>被推荐人</h3>
							<p>完成存款并游戏</p>
						</div>
					</a>					
					<a href="javascript:;" class="link-item ts fsyj last-item">
						<i></i>
						<div class="link-text">
							<h3>发送邮件</h3>
							<p>tianwei661@gmail.com</p>
						</div>
					</a>
				</div>				
				<div class="tab-wp" style="margin-top: 50px">
					<div class="tab-box">
						<span class="tab-item caijin active"></span>
						<span class="tab-item ruler"></span>
					</div>
					<div class="tab-content">
						<div class="tab-content1 tab-item-con active">
							<table class="table">
								<thead>
									<tr>
										<td>被推荐人存款</td>
										<td>彩金</td>
									</tr>
								</thead>
								<tbody>								
									<tr>
										<td>100</td>
										<td>18</td>
									</tr>									
									<tr>
										<td>500</td>
										<td>38</td>
									</tr>									
									<tr>
										<td>2000</td>
										<td>68</td>
									</tr>									
									<tr>
										<td>5000</td>
										<td>188</td>
									</tr>
									<tr>
										<td>10000</td>
										<td>388</td>
									</tr>									
									<tr>
										<td>50000</td>
										<td>1888</td>
									</tr>									
								</tbody>
							</table>							
						</div>
						<div class="tab-content2 tab-item-con">
							<p>1、邀请的每位玩家独立计算，每个存款阶段皆可申请一次彩金，最高无上限！</p>
							<p>举例：B在A的注册链结成功注册，B存款100元进行游戏，A可获得18元彩金。B再次存款500元，A可再获得38元彩金。</p>
							<p>2、达到要求后即可联系在线客服申请，彩金以红包形式派发，20倍流水即可提款。</p>
							<p>3、此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名、同一邮件地址、同一相似IP地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑等其他任何不正常行为，一经发现，我们将保留冻结您的账户盈利及余额的权利。</p>
							<p>5、本活动天威娱乐具有最终解释权。</p>
						</div>
					</div>
				</div>
				
			</div>
		</div>

		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		<script>
			$('.link-box a').on('click',function () {
				$(this).addClass('active').siblings('.active').removeClass('active');
			})
			$('.tab-box .tab-item').on('click',function () {
				var index = $(this).index();
				$(this).addClass('active').siblings().removeClass('active');
				$('.tab-content .tab-item-con').eq(index).show().siblings().hide();
			})
		</script>

	</body>

</html>