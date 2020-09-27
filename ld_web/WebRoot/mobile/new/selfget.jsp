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
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="自助优惠" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
<!-- 		<div class="list-group">
			<a class="list-item" href="https  ://wj.qq.com/s/1998690/7464" target="_blank"><img src="/mobile/img/icon/everyday.png" alt="" />调查问卷领彩金<i class="iconfont fr icon-downjiantou"></i></a>
		</div> -->		
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/youhui.jsp"><img src="/mobile/img/icon/cash.png" alt="" />自助存送<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/experience.jsp"><img src="/mobile/img/icon/win.png" alt="" />体验金<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/xima.jsp"><img src="/mobile/img/icon/history3.png" alt="" />自助返水<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/help.jsp"><img src="/mobile/img/icon/history5.png" alt="" />救援金<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/upgrade.jsp"><img src="/mobile/img/icon/history4.png" alt="" />自助晋级<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/coupon.jsp"><img src="/mobile/img/icon/auto.png" alt="" />优惠卷专区<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/redCoupon.jsp"><img src="/mobile/img/icon/red.png" alt="" />红包优惠卷<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/chouma.jsp"><img src="/mobile/img/icon/free.png" alt="" />免费筹码<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/birthday.jsp"><img src="/mobile/img/icon/red.png" alt="" />生日礼金<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>
		<script>
			//信息框- 例2
			if('${session.customer.accountName}' == ''){
				layer.open({
					content: '请先完善个人信息!',
					btn: ['确认', '取消'],
					yes: function(index) {
						window.location.href = '/mobile/new/myaccount.jsp'
//						layer.close(index);
					},
					no:function(){
						window.history.go(-1)
					}
				});
			}
		</script>
	</body>

</html>