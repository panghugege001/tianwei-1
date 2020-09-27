<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<title>VIP中心</title>
		<link rel="stylesheet" href="css/bootstrap.min.css?v=2" />
		<link rel="stylesheet" href="css/index.css?v=3" />
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>
	<style>
		#header .link-info {
			margin-left: 55px;
		}
		
		.gb-nav>li {
			width: 128px;
		}
	</style>

	<body class="vip_body">
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
		<style>
			.activety_header {
				background-color: #14192c;
			}
		</style>
		<div class="vip_bannar">
			<div class="bannar_text">
				<img src="img/text_banar.png" />
			</div>
			<div class="container">
				<div class="col-md-12 vip_listbox">
					<ul>
						<li>
							<a href="javascript:scroll_vip('section-1');"><img src="img/mui_list1.png"></a>
						</li>
						<li>
							<a href="javascript:scroll_vip('section-2');"><img src="img/mui_list2.png"></a>
						</li>
						<li>
							<a href="javascript:scroll_vip('section-3');"><img src="img/mui_list3.png"></a>
						</li>
					</ul>
				</div>

				<div class="section" id="section-1">
					<div class="section_title1">
						<span class="title1_img"><img src="img/vip_title1.png" /></span>
						<div class="section_list col-md-12 col-md-offset-1">
							<div class="row">
								<ul>
									<li class="col-md-2">
										<span><img src="img/title1_list1.png"></span>
										<label>生日礼物</label>
									</li>
									<li class="col-md-2">
										<span><img src="img/title1_list2.png"></span>
										<label>免费筹码</label>
									</li>
									<li class="col-md-2" id="clear">
										<span><img src="img/title1_list3.png"></span>
										<label>晋级礼金</label>
									</li>
									<li class="col-md-2">
										<span><img src="img/title1_list4.png"></span>
										<label>高额反水</label>
									</li>
									<li class="col-md-2">
										<span><img src="img/title1_list5.png"></span>
										<label>高额存送</label>
									</li>
								</ul>
							</div>
						</div>
						<div class="section_table1">
							<div class="vip_table1">
								<table cellpadding="0" cellspacing="0">
									<tr>
										<th>等级</th>
										<th>天兵</th>
										<th>天将</th>
										<th>天王</th>
										<th>星君</th>
										<th>真君</th>
										<th>仙君</th>
										<th>帝君</th>
										<th>天尊</th>
										<th>天帝</th>
									</tr>
									<tr>
										<td>晋级礼金</td>
										<td>X</td>
										<td>18元</td>
										<td>88元</td>
										<td>188元</td>
										<td>388元</td>
										<td>888元</td>
										<td>1888元</td>
										<td>2888元</td>
										<td>3888元</td>
									</tr>
									<tr>
										<td>生日礼金</td>
										<td>X</td>
										<td>18元</td>
										<td>88元</td>
										<td>188元</td>
										<td>388元</td>
										<td>888元</td>
										<td>1888元</td>
										<td>2888元</td>
										<td>3888元</td>
									</tr>
									<tr>
										<td>每月回馈</td>
										<td>X</td>
										<td>8元</td>
										<td>38元</td>
										<td>88元</td>
										<td>188元</td>
										<td>288元</td>
										<td>388元</td>
										<td>588元</td>
										<td>888元</td>
									</tr>
									<tr>
										<td>秒提功能</td>
										<td>X</td>
										<td>√</td>
										<td>√</td>
										<td>√</td>
										<td>√</td>
										<td>√</td>
										<td>√</td>
										<td>√</td>
										<td>√</td>
									</tr>
									<tr>
										<td>次存优惠</td>
										<td>存送15%,5次/天</td>
										<td>存送16%,5次/天</td>
										<td>存送17%,5次/天</td>
										<td>存送18%,10次/天</td>
										<td>存送19%,10次/天</td>
										<td>存送20%,10次/天</td>
										<td>存送22%,10次/天</td>
										<td>存送25%,10次/天</td>
										<td>存送30%,无限次/天</td>
									</tr>
									<tr>
										<td>老虎机返水比例</td>
										<td>0.5%</td>
										<td>0.6%</td>
										<td>0.7%</td>
										<td>0.8%</td>
										<td>0.9%</td>
										<td>1.0%</td>
										<td>1.1%</td>
										<td>1.2%</td>
										<td>1.5%</td>										
									</tr>
									<tr>
										<td>AG真人返水比例</td>
										<td>0.5%</td>
										<td>0.6%</td>
										<td>0.7%</td>
										<td>0.8%</td>
										<td>0.9%</td>
										<td>1.0%</td>
										<td>1.1%</td>
										<td>1.2%</td>
										<td>1.5%</td>										
									</tr>
									<tr>
										<td>电竞体育返水比例</td>
										<td>0.5%</td>
										<td>0.6%</td>
										<td>0.7%</td>
										<td>0.8%</td>
										<td>0.9%</td>
										<td>1.0%</td>
										<td>1.1%</td>
										<td>1.2%</td>
										<td>1.5%</td>										
									</tr>
									<tr>
										<td>彩票返水比例</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>
										<td>0.4%</td>										
									</tr>
									<tr>
										<td>返水上限</td>
										<td>8888</td>
										<td>18888</td>
										<td>18888</td>
										<td>28888</td>
										<td>28888</td>
										<td>28888</td>
										<td>28888</td>
										<td>28888</td>
										<td>88888</td>																			
									</tr>
									<tr>
										<td>救援金比例</td>
										<td>4%</td>
										<td>5%</td>
										<td>6%</td>
										<td>7%</td>
										<td>8%</td>
										<td>9%</td>
										<td>10%</td>
										<td>12%</td>
										<td>15%</td>																			
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="section2" id="section-2">
					<div class="section_title1">
						<span class="title1_img"><img src="img/vip_title2.png" /></span>
					</div>					
					<div class="vip_table2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>等级</th>
								<th>晋级条件</th>
								<th>保级要求</th>
							</tr>
							<tr>
								<td>天将</td>
								<td>单月全部平台累计投注额10万</td>
								<td>当月投注额2万</td>					
							</tr>
							<tr>
								<td>天王</td>
								<td>单月全部平台累计投注额30万</td>
								<td>当月投注额6万</td>					
							</tr>
							<tr>
								<td>星君</td>
								<td>单月全部平台累计投注额80万</td>
								<td>当月投注额16万</td>					
							</tr>
							<tr>
								<td>真君</td>
								<td>单月全部平台累计投注额180万</td>
								<td>当月投注额36万</td>					
							</tr>
							<tr>
								<td>仙君</td>
								<td>单月全部平台累计投注额400万</td>
								<td>当月投注额80万</td>					
							</tr>
							<tr>
								<td>帝君</td>
								<td>单月全部平台累计投注额800万</td>
								<td>当月投注额160万</td>					
							</tr>
							<tr>
								<td>天尊</td>
								<td>单月全部平台累计投注额1500万</td>
								<td>当月投注额300万</td>					
							</tr>
							<tr>
								<td>天帝</td>
								<td>特邀玩家</td>
								<td>当月投注额500万</td>					
							</tr>
						</table>
					</div>
				<div class="section3" id="section-3">
					<div class="section_title2">
						<span class="title1_img">
							<img src="img/vip_title3.png" />
						</span>
					</div>					
					<div class="guize">						
						<p>
							【说明】<br /> 
							1、如何晋级？<br />
						    VIP晋级分自助晋级和系统升降级，每月的投注只能提升一次会员等级，请根据自身投注额情况决定是否提前自助晋级。<br />
							自助晋级：个人中心 > 自助优惠 > 自助晋级，流水的更新可能存在延迟，建议您投注后次日操作晋级。<br />
							系统升降级：每个月初1-2号系统自动升降级，计算每位会员上个月的投注额。如会员在上个月已经提前自助晋级，则系统升降级时等级不变。<br /> 
							2、是否允许跳级？跳级能拿几次晋级礼金？ <br /> 
							可以跳级，跳级后只能领取最终等级的晋级礼金。<br /> 
							3、生日礼金请与生日当天联系客服申请，逾期作废且无法补偿，无需流水即可提款。<br />
							4、每月回馈礼金需上月存款满100元，在月初系统升降级完成之后，系统自动派发，无需流水即可提款。
							 
						</p>
					</div>
				</div>
			</div>
			<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		</div>
	</body>
	<script>
		$(".vip_table2").eq(0).show();
		$(".vip_box ul li").click(function() {
			$(".vip_box ul li").removeClass("active");
			$(this).addClass("active");
			$(".vip_table2").hide();
			$(".vip_table2").eq($(this).index()).show();
		})
	</script>
	<script>
		jQuery.getPos = function(e) {
			var l = 0;
			var t = 0;
			var w = jQuery.intval(jQuery.css(e, 'width'));
			var h = jQuery.intval(jQuery.css(e, 'height'));
			var wb = e.offsetWidth;
			var hb = e.offsetHeight;
			while(e.offsetParent) {
				l += e.offsetLeft + (e.currentStyle ? jQuery.intval(e.currentStyle.borderLeftWidth) : 0);
				t += e.offsetTop + (e.currentStyle ? jQuery.intval(e.currentStyle.borderTopWidth) : 0);
				e = e.offsetParent;
			}
			l += e.offsetLeft + (e.currentStyle ? jQuery.intval(e.currentStyle.borderLeftWidth) : 0);
			t += e.offsetTop + (e.currentStyle ? jQuery.intval(e.currentStyle.borderTopWidth) : 0);
			return { x: l, y: t, w: w, h: h, wb: wb, hb: hb };
		};
		jQuery.getClient = function(e) {
			if(e) {
				w = e.clientWidth;
				h = e.clientHeight;
			} else {
				w = (window.innerWidth) ? window.innerWidth : (document.documentElement && document.documentElement.clientWidth) ? document.documentElement.clientWidth : document.body.offsetWidth;
				h = (window.innerHeight) ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.offsetHeight;
			}
			return { w: w, h: h };
		};
		jQuery.getScroll = function(e) {
			if(e) {
				t = e.scrollTop;
				l = e.scrollLeft;
				w = e.scrollWidth;
				h = e.scrollHeight;
			} else {
				if(document.documentElement && document.documentElement.scrollTop) {
					t = document.documentElement.scrollTop;
					l = document.documentElement.scrollLeft;
					w = document.documentElement.scrollWidth;
					h = document.documentElement.scrollHeight;
				} else if(document.body) {
					t = document.body.scrollTop;
					l = document.body.scrollLeft;
					w = document.body.scrollWidth;
					h = document.body.scrollHeight;
				}
			}
			return { t: t, l: l, w: w, h: h };
		};

		jQuery.intval = function(v) {
			v = parseInt(v);
			return isNaN(v) ? 0 : v;
		};

		jQuery.fn.HoverTreeScrollvip = function(s) {
			o = jQuery.speed(s);
			return this.each(function() {
				new jQuery.fx.HoverTreeScrollvip(this, o);
			});
		};

		jQuery.fx.HoverTreeScrollvip = function(e, o) {
			var z = this;
			z.o = o;
			z.e = e;
			z.p = jQuery.getPos(e);
			z.s = jQuery.getScroll();
			z.clear = function() {
				clearInterval(z.timer);
				z.timer = null
			};
			z.t = (new Date).getTime();
			z.step = function() {
				var t = (new Date).getTime();
				var p = (t - z.t) / z.o.duration;
				if(t >= z.o.duration + z.t) {
					z.clear();
					setTimeout(function() { z.scroll(z.p.y, z.p.x) }, 13);
				} else {
					st = ((-Math.cos(p * Math.PI) / 2) + 0.5) * (z.p.y - z.s.t) + z.s.t;
					sl = ((-Math.cos(p * Math.PI) / 2) + 0.5) * (z.p.x - z.s.l) + z.s.l;
					z.scroll(st, sl);
				}
			};
			z.scroll = function(t, l) { window.scrollTo(l, t) };
			z.timer = setInterval(function() { z.step(); }, 13);
		};

		function scroll_vip(id) {
			$("#" + id).HoverTreeScrollvip(1000);
		}
	</script>

</html>