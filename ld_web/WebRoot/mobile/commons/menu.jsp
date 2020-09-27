<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<style>
	body {
		padding-bottom: 1.306666rem;
	}
</style>
<div class="menu-nav">
	<a href="/mobile/new/index.jsp" class="active">
		<div class="img-warp"><span></span><img src="/mobile/img/icon/nav1.png" alt="" /><img class="active" src="/mobile/img/icon/nav11.png" alt="" /></div>
		<div>首页</div>
	</a>
	<a href="/mobile/app/slotGame.jsp">
		<div class="img-warp"><span></span> <img src="/mobile/img/icon/nav2.png" alt="" /><img class="active" src="/mobile/img/icon/nav21.png" alt="" /></div>
		<div>游戏</div>
	</a>
	<!-- <a href="${(session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER')?'/mobile/new/everyday.jsp':'/mobile/new/agent.jsp'}">
		<div class="img-warp"><span></span><img src="/mobile/img/icon/everyday.png" alt="" /><img class="active" src="/mobile/img/icon/everyday2.png" alt="" /></div>
		<div>任务</div>
	</a> -->	
	<a href="/mobile/new/preferential.jsp">
		<div class="img-warp"><span></span><img src="/mobile/img/icon/nav3.png" alt="" /><img class="active" src="/mobile/img/icon/nav31.png" alt="" /></div>
		<div>优惠</div>
	</a>
	<a href="${(session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER')?'/mobile/new/account.jsp':'/mobile/new/agent.jsp'}">
		<div class="img-warp"><span></span><img src="/mobile/img/icon/nav4.png" alt="" style="width: 0.466666rem;height: 0.466666rem;"/><img class="active" src="/mobile/img/icon/nav41.png" alt="" style="width: 0.466666rem;height: 0.466666rem;" /></div>
		<div>我的</div>
	</a>
</div>
<script>

	$('.menu-nav a').each(function(i, item) {
		if(item.href.indexOf(window.location.pathname) > -1) {
			$(item).addClass('active')
		} else {
			$(item).removeClass('active')
		}
	})
</script>