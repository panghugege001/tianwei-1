<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="关于我们" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body>
		<title>天威</title>
		<div class="aboutus-nav">
			<div class="item active" href="">关于天威</div>
			<div class="item">牌照展示</div>
			<div class="item">联系我们</div>
		</div>
		<div class="aboutus-content">
			<div class="content">
				<h3>关于天威</h3> 天威（www.tianwei2018.com)执照由菲律宾娱乐及博彩公司 （PAGCOR）所核发和监管，以便提供网上投注服务。<br /><br /> 天威致力于打造世界一流的网络博彩公司，采用目前亚洲市 场最具权威、公正性的娱乐平台AG、PT、MG、DT、TTG、 NT、QT、PNG平台，为您提供全方位的在线博彩游戏服务。
				<h3>秒到存、提款</h3> 天威以提供全方位全天候博彩服务为主，提供全球首家首推 【秒存】、【秒提】快速到帐的【秒】服务，坚持快速便捷的买彩 提彩机制。<br /><br /> 200000以下提款最快1秒到账，200000以上资金5分钟提款， 当天提款额度无上限，客户提彩无须再等待一天或更多时间！
				<br /><br />2015年9月，秒存提系统再次升级，支援支付宝【秒存】、 【秒提】的【秒】服务，实现完全无须使用银行卡的存提款服务， 传统银行需配合现金流调查，支付宝上线，更加保障玩家资金流 动的安全性及隐蔽性，提供玩家更便利、更优质的服务。
				<h3>信息安全</h3> 天威采用了128位SSL国际标准加密技术，其安全级别已经超 越国内大多数银行，并配合专业技术人员精密设计的安全管理系 统，所有客户资料都在极其安全机密的情况下处理，任何情况， 天威不会向任何第三方透露客户资料。
				<h3>优质服务</h3> 天威延揽业内最优秀的客服团队，以便捷与亲切的服务为玩家解 答所有问题。天威衷心感谢玩家的反馈，不管是任何疑问、意见、 赞扬或投诉，特设客服热线、在线客服、QQ，微信等多种联系 方式，天威客服中心随时欢迎您的光临。
			</div>
			<div class="content hidden">
				<h3>牌照展示</h3> 天威 持有菲律宾（PAGCOR）合法博彩执照。 天威执照由菲律宾娱乐及博彩公司（PAGCOR）所核发和 监管，以便提供网上投注服务。
				<div class="txt-center">
					<img src="mobile/img/pagcor.jpg" alt="" />
					<a class="more" href="">点击查阅更多</a>
				</div>

			</div>
			<div class="content hidden">

				<h3>联系我们</h3> 天威客服中心，为您提供 7/24 小时的在线咨询服务。
				<br /><br /> 欢迎您通过以下的联系方式随时联系我们，我们会全力于第一时间提供您最优质的服务。
				<h3>备用网址：</h3> http://www.longdu11.com
				<br /> http://www.longdu16.com
				<br /> http://www.longdu166.com
				<br /> http://www.longdu188.com
				<br /> http://www.tianwei2018.com
				<br /> http://www.longdu10.com
				<br /> http://www.longdu68.com
				<br />
				<br /> 在线客服：登入备用网址，点击在线客服。
				<br /><br /> 电话客服：登入备用网址，点击电话回拨，输入电话号码，客服会于收到讯息的第一时间回拨。
				<br /><br /> 客服邮箱：tianwei661@gmail.com
				<br /><br /> 投诉邮箱：tianwei991@gmail.com（问题最终处理渠道）。
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script>
			var aboutus_content = $('.aboutus-content>.content')
			$('.aboutus-nav .item').click(function() {
				$(this).addClass('active').siblings().removeClass('active')
				aboutus_content.eq($(this).index()).show().siblings().hide()
			})
			
			var i=Util.getQueryString("i");
			if(!isNaN(i)){
				$('.aboutus-nav .item').eq(i).click()
			}
		</script>
	</body>

</html>