<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/login.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/account.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="我的账户" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="account-top">
			<div class="top-left">
				<div class="level"><img src="/mobile/img/logo.png" alt="" /></div>
				<div>${session.customer.loginname}</div>
			</div>
			<div class="top-right">
				<div><img src="/mobile/img/icon/luck.png" alt="" />代理等级：<span class="c-ylow">
					<c:if test="${session.AGENTVIP ==null || session.AGENTVIP eq '0'}">代理</c:if>
					<c:if test="${session.AGENTVIP!=null && session.AGENTVIP eq '1'}">VIP代理</c:if></span>
				</div>
				<div><img src="/mobile/img/icon/balance1.png" alt="" />老虎机佣金余额：<span class="c-ylow" id="agent-slotAccount">${session.slotAccount==null?0:session.slotAccount} 元</span></div>
				<div><img src="/mobile/img/icon/money1.png" alt="" />其他佣金余额：<span class="c-ylow">${session.customer.credit} 元</span></div>
			</div>
		</div>

		<div class="agent-money-list">
			<div class="item">
				<img src="/mobile/img/icon/win.png" alt="" />本月总输赢：
				<span id="agent-profitall" class="c-ylow"></span> 元
			</div>
			<div class="item">
				<img src="/mobile/img/icon/history2.png" alt="" />本月总返水：
				<span id="agent-ximafee" class="c-ylow"></span> 元
			</div>
			<div class="item">
				<img src="/mobile/img/icon/leftnav2.png" alt="" />本月注册量：
				<span id="agent-monthly_reg" class="c-ylow"></span> 人
			</div>
			<div class="item">
				<img src="/mobile/img/icon/many.png" alt="" />会员总人数：
				<span id="agent-reg" class="c-ylow"></span> 人
			</div>
			<div class="item">
				<img src="/mobile/img/icon/auto.png" alt="" />本月总优惠：
				<span id="agent-couponfee" class="c-ylow"></span> 元
			</div>
			<div class="item">
				<img src="/mobile/img/icon/money2.png" alt="" />本月投注额：
				<span id="agent-betall" class="c-ylow"></span> 元
			</div>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/agent/promotionSite.jsp"><img src="/mobile/img/icon/websiet.png" alt="" />推广网址<i class="iconfont fr icon-downjiantou"></i></a>
			<a class="list-item" href="mobile/new/agent/withdraw.jsp"><img src="/mobile/img/icon/cash.png" alt="" />申请提款<i class="iconfont fr icon-downjiantou"></i></a>
		</div>

		<div class="list-group">
			<a class="list-item" href="/mobile/new/agent/accountHistory.jsp"><img src="/mobile/img/icon/list.png" alt="" />账户清单<i class="iconfont fr icon-downjiantou"></i></a>
			<a class="list-item" href="/mobile/new/agent/myaccount.jsp"><img src="/mobile/img/icon/setting.png" alt="" />账户设置<i class="iconfont fr icon-downjiantou"></i></a>
			<a class="list-item" href="/mobile/new/email.jsp"><img src="/mobile/img/icon/email.png" alt="" />站内信
				<i class="iconfont fr icon-downjiantou"></i>
				<span class="msg j-msgcount hidden">你有未读信件<i class="iconfont icon-dian   c-ylow"></i></span>
			</a>
		</div>
		<div class="list-group">
			<div class="list-item" onclick="window.header.logout()"><img src="/mobile/img/icon/loginout.png" alt="" />安全退出<i class="iconfont fr icon-downjiantou"></i></div>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			//查询代理用户统计资料
			mobileManage.getUserManage().getAgentReport(function(result) {
				if(result.success) {
					$('#agent-slotAccount').html((result.data.slotAccount ? result.data.slotAccount : 0.0) + '元');
					$('#agent-profitall').html((result.data.profitall ? result.data.profitall : 0));
					$('#agent-ximafee').html((result.data.ximafee ? result.data.ximafee : 0));
					$('#agent-couponfee').html((result.data.couponfee ? result.data.couponfee : 0));
					$('#agent-reg').html(result.data.reg ? result.data.reg : 0);
					$('#agent-monthly_reg').html(result.data.monthly_reg ? result.data.monthly_reg : 0);
					$('#agent-betall').html(result.data.betall ? result.data.betall : 0);
				} else {
					alert(result.message);
				}
			});
		</script>
	</body>

</html>