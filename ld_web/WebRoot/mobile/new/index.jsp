<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
String infoValue=(String)session.getAttribute("infoValue4Live800");
if(infoValue==null)infoValue="";
%>
<!DOCTYPE>
<html>
	<title>天威</title>
	<head>
		<!-- <div style="position: relative;" id="top_demo">
			<span id="colse" style="position: absolute;width: 50px; height: 100%; display: inline-block;"></span>
			<img src="../img/icon/top_domw.png" style="max-width: 100%;">
			<a onclick="appDownload()" style="position: absolute;right: 0px; width: 30%; height: 100%; display: inline-block;top: 0px;"></a>
		</div> -->
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="天威" />
			<jsp:param name="menu" value="1" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/lib/swiper/swiper.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body>
		        <!-- <a href="/activety/sfyc/index.jsp" style="position: fixed;right: 0;z-index: 100;top: 50%;width: 100px;"><img src="/mobile/new/fudong-1.png" style="width:100%;" alt=""></a> -->
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide">
					<a href="#"><img src="/mobile/img/banner/znxb_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#"><img src="/mobile/img/banner/nnyujackpot_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#"><img src="/mobile/img/banner/mobileshoucun.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#"><img src="/mobile/img/banner/vipzm_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#"><img src="/mobile/img/banner/tjxn_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#"><img src="/mobile/img/banner/yqhy_mobile.jpg" style="width:100%" /></a>
				</div>
				<!-- <div class="swiper-slide">
					<a href="/mobile/new/preferential.jsp" target="_self"><img src="/mobile/img/banner/cicun.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="/mobile/app/slotGame.jsp" target="_self"><img src="/mobile/img/banner/mghot.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="/activety/jzsjb/index.jsp" target="_self"><img src="/mobile/img/banner/jzsjb.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="/activety/tjhy/index.jsp" target="_self"><img src="/mobile/img/banner/tjhy.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="http://www.longdobbs.com/forum.php?mod=viewthread&tid=2732&page=1&extra=#pid9864" target="_self"><img src="/mobile/img/banner/jiuan.jpg" style="width:100%" /></a>
				</div> -->
			</div>
			<div class="swiper-pagination">
			</div>
		</div>
		<a href="/mobile/new/news.jsp" class="common-marquee">
			<div id="index-news-marquee" class="marquee-content">

			</div>
		</a>
		<div class="index-promo">
			<img src="/mobile/img/newplayers.png" alt="" />
			<div class="promo-content">
				<div>注册存款后领28-88元</div>
				<a href="/mobile/new/register.jsp" class="register">立即注册</a>
				<a href="/mobile/new/login.jsp" class="login">登录</a>
			</div>
		</div>
		<section class="section game-section">
			<div class="section-title">
				<span><img src="/mobile/img/icon/game.png" alt=""/>游戏平台</span>
			</div>
			<div class="section-content">
				<a class="item" href="/mobile/app/slotGame.jsp">
					<img src="/mobile/img/game1.png" alt="" />
					<div class="txt">老虎机游戏</div>
				</a>
				<a class="item" href="/mobile/app/slotGame.jsp?type=live">
					<img src="/mobile/img/game2.png" alt="" />
					<div class="txt">真人游戏</div>
				</a>
				<a class="item" href="/mobile/app/slotGame.jsp?type=sport">
					<img src="/mobile/img/game3.png" alt="" />
					<div class="txt">体育游戏</div>
				</a>
				<a class="item" href="/mobile/app/slotGame.jsp?type=fish">
					<img src="/mobile/img/game4.png" alt="" />
					<div class="txt">捕鱼游戏</div>
				</a>
				<a class="item" href="/mobile/app/slotGame.jsp?type=chess">
					<img src="/mobile/img/game_5.png" alt="" />
					<div class="txt">棋牌游戏</div>
				</a>
				<a class="item" href="/mobile/app/slotGame.jsp?type=fanya">
					<img src="/mobile/img/game_6.png" alt="" />
					<div class="txt">泛亚电竞</div>
				</a>
				<a class="item" href="/mobile/app/slotGame.jsp?type=lottery">
					<img src="/mobile/img/game_7.png" alt="" />
					<div class="txt">彩票游戏</div>
				</a>
			</div>
		</section>
		<section class="section promo-section">
			<div class="section-title">
				<span><img src="/mobile/img/icon/promo.png" alt=""/>最新优惠</span>
				<a class="fr" href="/mobile/new/preferential.jsp"><i class="iconfont icon-msnui-more"></i></a>
			</div>
			<div class="section-content" id="j-youhui">
				<!--<a class="item" href="">-->
				<!--<img src="/mobile/img/game_5.jpg" alt=""/>-->
				<!--<div class="txt">-->
				<!--优惠时间&nbsp;&nbsp;&nbsp;12-01至12-31-->
				<!--</div>-->
				<!--</a>-->
			</div>
		</section>
		<section class="section luck-section">
			<div class="section-title">
				<span><img src="/mobile/img/icon/luck.png" alt=""/>幸运玩家</span>
			</div>
			<div class="section-content">
				<div id="j-winner-box" style="position: relative;">
					<!--<a class="item" href="">
					<img src="/mobile/img/game_5.jpg" alt="" />
					<div class="rig-info">
						<div>恭喜玩家 <span class="color1">a8***818</span> 在 PT游戏</div>
						<div><span class="color2">公路王国</span> 赢得 <span class="color3">8058</span>元</div>
						<div class="color1">2017/12/31</div>
					</div>
				</a>-->
				</div>
			</div>
		</section>

		<jsp:include page="/mobile/commons/footer_1.jsp" />
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/lib/swiper/swiper.js"></script>
		<script type="text/javascript" src="/mobile/js/new/index.js?x=3"></script>
        <script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>
		<script>
			$(function(){
				$("#colse").click(function(){
					$("#top_demo").hide();
				})
			})
		</script>
		<script>
			//奇乐游
			function loginQlyGame() {
				if('${session.customer==null}' == 'true') {
					layer.open({
						content: '请登入后再进入游戏！',
						btn: '我知道了'
					});
					return;
				}
				layer.open({
					content: '即将跳转棋乐游棋牌',
					skin: 'msg',
					time: 2 //2秒后自动关闭
				});
				setTimeout("window.location.href='/game/cg761Login.aspx'", 1000)
			}
		</script>

		<style type="text/css">
			.layui-m-layer{z-index: 3!important;}
		</style>
	</body>

</html>