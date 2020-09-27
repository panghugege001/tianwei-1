<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>天威周年盛典</title>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" href="/css/base.css?v=2" />
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>

	<body>
		<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>
		<div id="app" class="full-warp">
			<div class="section page1">
				<img src="./img/pg1.jpg" alt="" />
			</div>
			<div class="section">
				<div class="title">申请方式
					<small>Application method</small>
				</div>
				<div class="main-warp">
					<p>活动时间：2018年4月1日-2018年4月30日</p>
					<p>活动对象：天威所有会员</p>
					<p>活动要求：排名争霸赛，依照周流水进行排名，流水霸主获得名次相对应礼品与 奖金， 存款游戏，奖品领不完，赶紧充值来赢大奖吧!
					</p>
					<table style="margin-top:40px;">
						<tr>
							<th>排名</th>
							<th>礼品、礼金</th>
						</tr>
						<tr>
							<td>第1名</td>
							<td>Mac Book</td>
						</tr>
						<tr>
							<td>第2名</td>
							<td>IPhone X</td>
						</tr>
						<tr>
							<td>第3名</td>
							<td>IPad Pro</td>
						</tr>
						<tr>
							<td>第4名</td>
							<td>IPhone 8 Plus</td>
						</tr>
						<tr>
							<td>第5名</td>
							<td>iPad</td>
						</tr>
						<tr>
							<td>第6名</td>
							<td>3888</td>
						</tr>
						<tr>
							<td>第7名</td>
							<td>2888</td>
						</tr>
						<tr>
							<td>第8名</td>
							<td>1888</td>
						</tr>
						<tr>
							<td>第9名</td>
							<td>888</td>
						</tr>
						<tr>
							<td>第10-30名</td>
							<td>188</td>
						</tr>
						<tr>
							<td>第31-50名</td>
							<td>68</td>
						</tr>
						<tr>
							<td>第51-100名</td>
							<td>28</td>
						</tr>
					</table>
				</div>
			</div><br />
			<div class="section">
				<div class="title">流水排名
					<small>Flow ranking</small>
				</div>
				<div class="main-warp">
					<table>
						<tr>
							<th>排名</th>
							<th>玩家账号</th>
							<th>总流水</th>
							<th>更新日期</th>
						</tr>
						<tbody id='ranking'>
							<tr>
								<td colspan="4">暂无数据</td>
							</tr>
						</tbody>
					</table>
					<div id='betSort' class="page-by">
						<span>每页 10 条记录</span>
						<span>第 <em class="color number">0</em> / <em class="color count">0</em> 页 </span>
						<span class="color begin" style="cursor: pointer;">首页</span>
						<span class="color prev" style="cursor: pointer;">上一页</span>
						<span class="color next" style="cursor: pointer;">下一页</span>
						<span class="color end" style="cursor: pointer;">尾页</span>
					</div>
				</div>
			</div>
			<!--<div class="section">
				<div class="title">获奖记录
					<small>Award record</small>
				</div>
				<div class="week-by">
					<div class="item margin active">第一周</div>
					<div class="item ">第二周</div>
					<div class="item">第三周</div>
					<div class="item">第四周</div>
				</div>
				<div class="main-warp">
					<table>
						<tr>
							<td>暂无记录</td>
						</tr>
					</table>
					<div class="page-by">
						<span>每页 10 条记录</span>
						<span>第 <em class="color">1</em> / <em class="color">3</em> 页 </span>
						<span class="color">首页</span>
						<span class="color">上一页</span>
						<span class="color">下一页</span>
						<span class="color">尾页</span>
					</div>
				</div>
			</div>-->
			<br />
			<div class="section">
				<div class="title">活动内容
					<small>Activities</small>
				</div>

				<div class="main-warp">
					<ol>
						<li>活动以每七天为一个周期，依照此周期内玩家老虎机平台的投注额计算排名，当日投注额将于次日18:00之前更新至官网。
							<br /> 流水计算时间:<br />
							<div style="text-indent: 2em;">4月02号 00:00 — 4月08号 23:59:59 </div>
							<div style="text-indent: 2em;">4月09号 00:00 — 4月15号 23:59:59</div>
							<div style="text-indent: 2em;">4月16号 00:00 — 4月22号 23:59:59</div>
							<div style="text-indent: 2em;">4月23号 00:00 — 4月29号 23:59:59</div>
						</li>
						<li>此活动每周的投注额无法累积，将自动于该周期结束后清零，并重新计算。</li>
						<li>此活动无需报名。申请礼品会员发送邮件到邮箱：tianwei661@gmail.com <br />
							<div style="text-indent: 2em;"></div> 邮件标题:申请周年盛典周流水礼物信息 <br />
							<div style="text-indent: 2em;"> </div>邮件内容：您的账号+收件姓名+收件地址+收件电话。需要修改的资料<br />
							<div style="text-indent: 2em;"></div>(修改地址只限活动时间内进行修改)。
						</li>
						<li>实物奖品如需折现，将以官方价格的70%兑换，兑换彩金无需流水即可提款。</li>
						<li>达到条件的玩家，礼金将在隔日下午18点前派发，礼品将在15个工作日内送达到您的手中。</li>
						<li>此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账号，包括同一姓名，同一邮件地址，同一注册电话，同一/相似的IP地址，同一借记卡/信用卡，天威保有不派发任何奖励的权利！
						</li>
						<li>天威享有对本活动的修订权及最终解释权。</li>
					</ol>
				</div>
				<br />
			</div>
		</div>
		<br />
		<br />
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		<div class="menu-nav">
			<!--<div class="item ">立即<br />报名</div>-->
			<div class="item active">申请<br />方式</div>
			<div class="item">流水<br />排名</div>
			<!--<div class="item">获奖<br />记录</div>-->
			<div class="item">活动<br />内容</div>
		</div>
		<link rel="stylesheet" href="/css/base.css?v=2" />
		<script>
			$('.menu-nav .item').click(function() {
				$(this).addClass('active').siblings().removeClass("active");
				$('html,body').animate({
					scrollTop: $('#app .section').eq($(this).index() + 1).offset().top + 'px'
				}, 400)
			})
			var betSort = $("#betSort")

			function getBet(num) {
				$(".easemobim-prompt-wrapper").show();
				var obj = num || 1;
				$.get('/asp/queryConcertBetNew.aspx', {
					type: 5,
					size: 10,
					pageIndex: obj
				}, function(data) {
					$(".easemobim-prompt-wrapper").hide();
					if(data.success && data.page.pageContents) {
						var $html = [];
						$.each(data.page.pageContents, function(i, item) {
							var x = new Date(item.lastTime);
							var time = x.getFullYear() + '-' + (x.getMonth() + 1) + '-' + x.getDate()
							$html.push('<tr><td>' + (item.ranking||0) + '</td><td>' + item.loginname + '</td><td>' + item.bet + '</td><td>' + time + '</td></tr>');
						})
		

						if($html.length > 0) {
							$("#ranking").html($html.join(''))
							$(".number", betSort).text(obj)
							$(".count", betSort).text(data.page.totalPages)
						} else {
							$("#ranking").html('<tr><td colspan="4">暂无记录</td></tr>')
						}
					} else {
						$("#ranking").html('<tr><td colspan="4">暂无记录</td></tr>')
					}
				})
			}
			getBet()
			$(document).ajaxError(function() {
				$(".easemobim-prompt-wrapper").hide();
			})
			$('.begin', betSort).click(function() {
				getBet(1)
			})
			$('.prev', betSort).click(function() {
				if(+$(".number", betSort).text() > 1) {
					getBet((+$(".number", betSort).text()) - 1 || 1)
				}
			})
			$('.next', betSort).click(function() {
				if((+$(".number", betSort).text()) < ($(".count", betSort).text())) {
					getBet((+$(".number", betSort).text()) + 1)
				}

			})
			$('.end', betSort).click(function() {
				getBet($(".count", betSort).text())
			})
			var query = {
				"0": {
					guildId: 0,
					type: 1,
					pageIndex: 0
				},
				"1": {
					guildId: 0,
					type: 2,
					pageIndex: 0
				},
				"2": {
					guildId: 0,
					type: 3,
					pageIndex: 0
				},
				"3": {
					guildId: 0,
					type: 4,
					pageIndex: 0
				},
			}
			$(".week-by .item").click(function() {
				$(this).addClass('active').siblings().removeClass("active");
				$.get('/asp/queryGuildDate.aspx', query[$(this).index()], function() {})
			})
		</script>

	</body>

</html>