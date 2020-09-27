<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户余额" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body style="background: #fff;">
		<div class="balance-top"><img src="/mobile/img/icon/money.png" alt="" />主账户(元)：
			<span class="c-ylow credit" id="credit">${session.customer.credit}</span>
			<!--			<i class="fr c-ylow iconfont icon-icon-refresh " onclick="refresh()"></i>-->
		</div>
		<h3 class="section-title"><span><img src="/mobile/img/icon/winrar.png" alt="" /><em class="c-ylow">各平台余额</em><small style="color:#999;">(￥) </small></span></h3>
		<div class="balance-group">
			<div class="balance-list">
				<div class="balance-item">老虎机钱包<i class="iconfont icon-info c-ylow"></i>
					<div class=" slot">0.00</div>
				</div>
				<!-- <div class="balance-item">PT老虎机
					<div class=" newpt">0.00</div>
				</div>
				<div class="balance-item">AG老虎机
					<div class=" agin">0.00</div>
				</div> -->
				<div class="balance-item">DT老虎机
					<div class=" dt">0.00</div>
				</div>
				<div class="balance-item">MG老虎机
					<div class=" mg">0.00</div>
				</div>

			</div>
		</div>
		<div class="balance-list">
			<div class="balance-item">TTG老虎机
				<div class="ttg">0.00</div>
			</div>
			<div class="balance-item">AG捕鱼
				<div class=" agin">0.00</div>
			</div>
			<div class="balance-item">GG捕鱼
				<div class="ttg">0.00</div>
			</div>

		</div>
		<div class="balance-list">
			<div class="balance-item">MW捕鱼
				<div class="mwg">0.00</div>
			</div>
			<div class="balance-item">AG真人
				<div class="agin">0.00</div>
			</div>
			<div class="balance-item">N2真人
				<div class=" n2live">0.00</div>
			</div>
		</div>
		<!--<div class="balance-group">
			<h3 class="section-title"><span><img src="/mobile/img/icon/balance4.png" alt="" />棋牌游戏</span></h3>
			<div class="balance-list">
				<div class="balance-item">斗地主 <span class="fr">0.00</span></div>
				<div class="balance-item">好运5扑克 <span class="fr">0.00</span></div>
				<div class="balance-item">百乐斗牛<span class="fr">0.00</span></div>
			</div>
		</div>-->
		<div class="balance-list">
			<div class="balance-item">沙巴体育
				<div class="sba">0.00</div>
			</div>
			<div class="balance-item">捕鱼帐户
				<div class="fish">0.00</div>
			</div>
		</div>
		<div class="overlay hidden">
			<dic class="overlay-content slot-bll">
				<div class="title">老虎机钱包包括
					<div class="close iconfont icon-close"></div>
				</div>
				<div class="content slot-bll">
					<div class="slot-include">DT老虎机</div>
					<div class="slot-include bdnone">MG老虎机</div>
					<div class="slot-include">QT老虎机</div>
					<div class="slot-include bdnone">PNG老虎机</div>
					<div class="slot-include">SW老虎机</div>
					<div class="slot-include bdnone">NT老虎机</div>
				</div>
			</dic>
		</div>
		<br /><br />
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			//重新整理余额
			$.each(["slot", /* "newpt", 'agin', */'dt','mg', 'ttg', 'mwg', 'n2live', 'sba'], function(i, item) {
				$('.' + item).html('<i class="iconfont icon-icon-refresh c-ylow"></i>')
				$("." + item).click(function() {
					$(this).addClass('active')
					$.post("/asp/getGameMoney.aspx", {
						"gameCode": item
					}, function(returnedData, status) {
						if("success" == status) {
							$("." + item).text(returnedData);
						}
					});
				})
			});
			$(".icon-info").click(function() {
				$(".overlay").fadeIn(500)
			})
			$('.overlay').click(function(e) {
				if(!$(e.target).is(".overlay-content *:not(.close)")) {
					$(this).fadeOut(500);
				}
			})
			function refresh() {
				$('.refre').addClass('credit-query');
				//先查询优发平台余额
				mobileManage.getUserManage().getCredit(
					function(result) {
						if(result.success) {
							$("#credit").html(result.message + " 元");
						} else {
							$("#credit").html('系统繁忙中');
							alert(result.message);
						}
						$('.refre').removeClass('credit-query');
					}
				);
			}
			refresh()
		</script>
	</body>

</html>