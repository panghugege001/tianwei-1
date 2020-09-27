<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/user.css?v=1117" />
		<script src="js/lib/jquery-1.11.2.min.js"></script>
		<script src="js/user/usercheckin.js?v=5"></script>
	</head>
 
	<body>

		<div class="index-bg about-bj">
			<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>

			<div class="container">
				<jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
				<div class="cfx about-main">
					<div class="gb-sidenav">
						<jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>
					</div>
					<div class="gb-main-r tab-bd user-main">
						<div id="tab-user" class="tab-panel active letter-c">
							<div class="tab-box" id="tab-checkIn">
								<div class="clearfix tab-menu2">
									<ul class="tab-muen-item clearfix">
										<li class="active" style="margin-left: -2px;">
											<a href="#tab-checkInCenter" data-toggle="tab">签到中心</a>
										</li>
										<li>
											<a href="#tab-checkInInfo" data-toggle="tab">签到说明</a>
										</li>
									</ul>
								</div>
								<p class="c-red" style="padding: 0 28px;">连续签到7日，并达到每日存款要求，即可领取红包/存送优惠券</p>
								<div class="tab-bd">
									<div class="tab-bd-box">
										<!--签到中心-->
										<div id="tab-checkInCenter" class="tab-panel active">

											<div class="qiandao-con clearfix">

												<div class="qiandao-left">
													<div class="qiandao-top">
														<div class="just-qiandao qiandao-sprits" id="j-qdA">立即签到</div>
													</div>
													<p>签到余额<span id="todayGet" class="c-red todayGet">0.00</span></p>
													<p>请点击<span class="btn_qiandao">立刻签到</span></p>
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
											<table>
												<thead>
													<tr>
														<th></th>
														<th>新会员</th>
														<th>忠实</th>
														<th>星级</th>
														<th>黄金</th>
														<th>白金</th>
														<th>钻石</th>
														<th>至尊</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>每日签到</td>
														<td>3元</td>
														<td>6元</td>
														<td>8元</td>
														<td>10元</td>
														<td>15元</td>
														<td>30元</td>
														<td>50元</td>
													</tr>
													<tr>
														<td>日存款要求</td>
														<td>50元</td>
														<td>100元</td>
														<td>300元</td>
														<td>500元</td>
														<td>700元</td>
														<td>无存款要求</td>
														<td>无存款要求</td>
													</tr>
													<tr>
														<td>最大提款</td>
														<td>888</td>
														<td>3888</td>
														<td>5888</td>
														<td>8888</td>
														<td>12888</td>
														<td>21888</td>
														<td>38888</td>
													</tr>
													<tr>
														<td>累计连续签到7日</td>
														<td>18元红包</td>
														<td>28元红包</td>
														<td>38元红包</td>
														<td>68%存送优惠卷</td>
														<td>88%存送优惠卷</td>
														<td>100%存送优惠卷</td>
														<td>120%存送优惠卷</td>
													</tr>		 											
												</tbody> 
											</table> 
											<h3 class="c-blue">温馨提示：</h3>
											<p>1.转帐要求是根据您当日存款，达到存款要求后签到奖金自动开启户内转帐。</p>
											<p>2.所有签到奖金流水需要达到10流水即可提款，提款金额不得超过最大提款的额度。</p>
											<p>3.签到奖金每月最后一天的23:59:59自动清0，请及时进行游戏。</p>
											<p>4.签到彩金需转入PT/DT/MG/TTG/NT/QT，任一平台进行游戏。</p>
											<p>5.连续签到7日，并达每日的存款要求，即可向在线客服领取红包/存送优惠券（请在三天内联系在线客服，在线客服会为您派发红包/存送优惠券！)逾期则视为自动放弃。</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>

		</div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	</body>

</html> 