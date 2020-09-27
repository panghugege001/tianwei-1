<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
	</head>
	<link rel="stylesheet" href="../../css/user.css?v=2" />
	<script src="../../js/lib/jquery-1.11.2.min.js"></script>
	<script src="../../js/user/usercheckin.js?v=3"></script>
	<body>
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

				<div class="tab-bd">
					<div class="tab-bd-box">
						<!--签到中心-->
						<div id="tab-checkInCenter" class="tab-panel active">

							<div class="qiandao-con clearfix">

								<div class="qiandao-left">
									<div class="qiandao-top">
										<div class="just-qiandao qiandao-sprits" id="j-qdA">立即签到</div>
									</div>
									<p>今日已领<em id="todayGet" class="c-red">0</em>元</p>
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
							<div id="tab-checkInInfo" class="tab-box tab-panel index">
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
											<td>5888</td>
											<td>8888</td>
											<td>8888</td>
											<td>12888</td>
											<td>21888</td>
											<td>38888</td>
										</tr> 
									</tbody>
								</table>
								<h3 class="c-blue">温馨提示：</h3>
								<p>1、转账要求是根据您当日存款，达到存款要求后签到奖金自动开启转账。</p>
								<p>2、所有签到奖金流水需要10倍。</p>
								<p>3、提款金额不得超过最大提款的额度。</p>
								<p>4、签到奖金每日的23:29:59秒自动清理，请及时进行游戏。</p>
								<p>5、禁止套利行为，如有发现立即关闭账户。</p>
								<p>6、签到彩金需转入PT、DT、MG、TTG、NT、QT任一平台进行游戏，达到流水即可转出。</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>

</html>