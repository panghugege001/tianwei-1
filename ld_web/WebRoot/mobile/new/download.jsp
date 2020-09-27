<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="客户端下载" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/lib/swiper/swiper.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
		<!--<link rel="stylesheet" type="text/css" href="/mobile/css/lib/mui-0.2.1/mui.min.css" />-->
		<!--<link rel="stylesheet" type="text/css" href="/mobile/css/common.mui.css" />-->
	</head>

	<body>
		
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide">
					<a href="#" target="_self"><img src="/mobile/img/banner/znxb_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#" target="_self"><img src="/mobile/img/banner/nnyujackpot_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#" target="_self"><img src="/mobile/img/banner/mobileshoucun.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#" target="_self"><img src="/mobile/img/banner/vipzm_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#" target="_self"><img src="/mobile/img/banner/tjxn_mobile.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="#" target="_self"><img src="/mobile/img/banner/yqhy_mobile.jpg" style="width:100%" /></a>
				</div>
				<!-- <div class="swiper-slide">
					<a href="http://www.longdobbs.com/forum.php?mod=viewthread&tid=2732&page=1&extra=#pid9864" target="_self"><img src="/mobile/img/banner/jiuan.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="/activety/meinv/index.jsp" target="_self"><img src="/mobile/img/banner/tianxin_mb.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="/mobile/new/preferential.jsp?lottery" target="_self"><img src="/mobile/img/banner/lottery.jpg" style="width:100%" /></a>
				</div>
				<div class="swiper-slide">
					<a href="/activety/zhoumo/index.jsp" target="_self"><img src="/mobile/img/banner/zhoumo_web.jpg" style="width:100%" /></a>
				</div> -->
			</div>
			<div class="swiper-pagination">
			</div>
		</div>
		<div class="section download-section">
			<div class="section-title download">
				<span><img src="/mobile/img/icon/download.png" alt="">天威客户端</span>
			</div>
			<div class="section-content">
				<!-- <a class="item" onclick="appDownload()"  >
					<img src="/mobile/img/down6.jpg" alt="" />
					<div class="txt">手机APP</div>
				</a> -->
				<div class="item ptgame" >
					<img src="/mobile/img/down5.jpg" alt="" />
					<div class="txt">PT客户端</div>
				</div>
				<!-- <div class="item dtgame" >
					<img src="/mobile/img/down2.jpg" alt="" />
					<div class="txt">DT客户端</div>
				</div> -->
				<!--<div class="item aggame" >
					<img src="/mobile/img/down1.jpg" alt="" />
					<div class="txt">AG客户端</div>
				</div>-->
				<div class="item aggame2" >
					<img src="/mobile/img/down1.jpg" alt="" />
					<div class="txt">AG真人客户端</div>
				</div>
				<!--<div class="item aggameFish" >
					<img src="/mobile/img/game_1.jpg" alt="" />
					<div class="txt">AG捕鱼客户端</div>
				</div>-->
				<a class="item " target="_blank" href="https://www.666wins.com/as-lobby/mob/qrcode1_cn.html" >
					<img src="/mobile/img/down3.jpg" alt="" />
					<div   class="txt">MW客户端</div>
				</a>
				<!-- <a class="item " target="_blank" href="http://n2-live.com/mobile_platform_download/index_gb.html" >
					<img src="/mobile/img/down4.jpg" alt="" />
					<div   class="txt">N2真人客户端</div>
				</a> -->
			</div>
		</div>
				<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/lib/mui-0.2.1/mui.min.js"></script>
		<script type="text/javascript" src="/mobile/js/MUIModel.js"></script>
		<script type="text/javascript" src="/mobile/js/lib/swiper/swiper.js"></script>
		<script type="text/javascript" src="/mobile/js/WebApp.js"></script>
		<script type="text/javascript">
			new Swiper('.swiper-container', {
				pagination: '.swiper-pagination'
			});
			window.webapp = new WebApp();
			//苹果手机不显示
			if(getMobileKind() == 'IOS') {
				//$('.ptgame').remove();
				$('.ptgame').click(function() {
					if('${session.customer==null}' == 'true') {
						alert('[提示]请登录游戏账号之后，进行游戏！');
						return;
					}
					webapp.redirect(window.location.origin + '/mobile/ptMobileLogin.jsp');
				});
			} else {
				//PT老虎机下载弹窗
				$('.ptgame').click(function() {
					webapp.redirect('https://o46aaoh6w.qnssl.com/v3/new/newClient.apk')
				});
			}
			//AG下载弹窗
			$('.aggame,.aggame2').click(function() {
				webapp.redirect('http://agin.cc/');
			});
			//TTG老虎机
			$('#ttggame').click(function() {
				var url = 'https://ams-games.ttms.co/casino/longfa_lh/lobby/index.html?playerHandle={0}&account={1}&lang=zh-cn&platformUrl={2}';
				mobileManage.getModel().open('goGameOrFunGame', [{
					title: 'TTG老虎机',
					message: ('${session.customer==null}' == 'true' ? '登录您的账号，才可进入游戏。' : '您已登录，可直接进行游戏。') + '<br><div class="space-1"></div><span style="color:red;">更换游戏时，请返回官网在进入！</span>',
					goFun: function() {
						window.location.href = String.format(url, '999999', 'FunAcct', window.location.href);
					},
					goGame: function() {
						window.TTplayerhandle = '${session.TTplayerhandle}';
						if(TTplayerhandle) {
							window.location.href = String.format(url, TTplayerhandle, 'CNY', window.location.href);
						} else {
							mobileManage.getLoader().open("进入游戏中");
							mobileManage.ajax({
								url: 'mobi/loginTTG.aspx',
								callback: function(result) {
									mobileManage.getLoader().close();
									if(result.success) {
										window.TTplayerhandle = result.message;
										window.location.href = String.format(url, TTplayerhandle, 'CNY', window.location.href);
									} else {
										alert(result.message);
									}
								}
							});
						}
					}
				}]);
			});
			//DT
			$('.dtgame').click(function() {
				var downloadUrl = getMobileKind() == 'Android' ? 'http://down.dreamtech.asia/LONGDU/android.html' : 'http://down.dreamtech.asia/LONGDU/ios.html';
				webapp.redirect(downloadUrl);
			});
			$('.aggameFish').click(function() {
				if('${session.customer==null}' == 'true') {
					alert('登录您的账号，才可进入游戏')
				} else {
					mobileManage.getLoader().open('进入中');
					mobileManage.ajax({
						url: 'mobi/gameAginRedirect.aspx?agFish=1',
						callback: function(result) {
							if(result.success) {
								window.location.href = result.data.url;
							} else {
								alert(result.message);
							}
							mobileManage.getLoader().close();
						}
					});
				}
			});
		</script>
	</body>

</html>