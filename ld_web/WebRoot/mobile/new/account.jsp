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
<!DOCTYPE >
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body class="account-page">
		<div class="account-top">
			<div class="top-left">
				<div class="level"><s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)" /></div>
				<div>${session.customer.loginname}</div>
			</div>
			<div class="top-right">

				<c:choose>
					<c:when test="${session.customer!=null&&session.customer.role!='AGENT'}">
						<div><img src="/mobile/img/icon/account1.png" alt="" />红包余额：<span id='hbMoney' class="c-ylow">0.00</span></div>
						<div><img src="/mobile/img/icon/account2.png" alt="" />签到余额：<span id='todayGet' class="c-ylow">0.00</span></div>
						<div><img src="/mobile/img/icon/account3.png" alt="" />账户余额：<span class="c-ylow">${session.customer.credit}</span>
							<a href="/mobile/new/mybalance.jsp"><i class="iconfont icon-downjiantou"></i></a>
						</div>
						<!--<div class="banlce"><strong>余额:</strong><span class="value cde" id="credit">${session.customer.credit} 元</span><span class="refre iconfont icon-shuaxin" onClick="refresh()" title="刷新余额"></span></div><br />-->
						<!--<a href="javascript:void(0)" class="btn-member center" onclick="onekeyMonery();" style="position: static;">一键额度回归</a>-->
					</c:when>
					<c:otherwise>
						<div>老虎机佣金余额:<span class="c-ylow">${session.slotAccount} 元</span></div>
						<div>其他佣金余额:<span class="c-ylow">${session.customer.credit} 元</span></div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="money-nav">
			<a class="limit" href="javascript:;" onclick="alert('存款通道已关闭！')">我要存款</a>
			<a class="cash limit" href="/mobile/new/withdrawal.jsp">我要取款</a>
			<a class="transfer" href="/mobile/new/transfer.jsp">户内转账</a>
		</div>
		<div class="list-group">
			<a class="list-item limit" href="/mobile/new/selfget.jsp"><img src="/mobile/img/icon/auto.png" alt="" />自助优惠<i class="iconfont fr icon-downjiantou"></i></a>
			<a class="list-item limit" href="mobile/new/everyday.jsp"><img src="/mobile/img/icon/everyday2.png" alt="" />每日任务<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">

			<a class="list-item" href="/mobile/new/accountHistory.jsp"><img src="/mobile/img/icon/list.png" alt="" />账户清单<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/setting.jsp"><img src="/mobile/img/icon/setting.png" alt="" />账户设置<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/friends.jsp"><img src="/mobile/img/icon/leftnav2.png" alt="" />好友推荐<i class="iconfont fr icon-downjiantou"></i></a>
			<a class="list-item" href="/activety/vip2/index.jsp"><img src="/mobile/img/icon/vip.png" alt="" />VIP中心<i class="iconfont fr icon-downjiantou"></i></a>
			<a class="list-item" href="/mobile/new/email.jsp"><img src="/mobile/img/icon/email.png" alt="" />站内信<i class="iconfont fr icon-downjiantou"></i><span class="msg j-msgcount hidden">你有未读信件<i class="iconfont icon-dian   c-ylow"></i></span></a>
			<!-- <a class="list-item" href="/mobile/new/integral/integral.jsp"><img src="/mobile/img/icon/auto.png" alt="" />积分商城<i class="iconfont fr icon-downjiantou"></i></a> -->
			<a style="display: none" id="isShowService" class="list-item" href="/mobile/new/vip-service.jsp"><img src="/mobile/img/icon/vip.png" alt="" />VIP专属客服<i class="iconfont fr icon-downjiantou"></i></a>

		</div>
		<div class="list-group">
			<div class="list-item" id="logout-button"><img src="/mobile/img/icon/loginout.png" alt="" />安全退出<i class="iconfont fr icon-downjiantou"></i></div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script>
      $.get('/asp/queryQRcode.aspx',function(data){
        if((data.length && data[0].address)){
          $('#isShowService').show()
        }else{
          $('#isShowService').hide()
        }
      },'json')
		</script>
		<script type="text/javascript">
			$(function() {
				// loginout
				$('#logout-button').on('click', function() {
					mobileManage.getLoader().open('登出中');
					mobileManage.getUserManage().logout(function(data) {
						mobileManage.getLoader().close();
						if(data.success) {
							sessionStorage.setItem('permission', 0);
							sessionStorage.setItem('notice', 1);
							mobileManage.getLoader().close();
							window.location = '/mobile/new/index.jsp'
						} else {
							alert(data.message);
						}
					});
				});
			})

			//查询玩家红包余额
			function getHBMoney(type) {
				$("#hbMoney").html("<img src='/images/waiting.gif'>");
				$.post("/asp/getHBMoney.aspx", {
					"type": 0
				}, function(returnedData, status) {
					if("success" == status && returnedData != '') {
						$("#hbMoney").html(returnedData);
					}
				});
			}
			getHBMoney()

			function checkInSignAmount() {
				$.ajax({
					url: "/asp/querySignAmount.aspx",
					type: "post", // 请求方式
					dataType: "text", // 响应的数据类型
					data: "",
					async: true, // 异步
					success: function(msg) {
						$("#todayGet").html(Math.floor(msg));
					},
				});
			}
			checkInSignAmount()
			//重新整理余额
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
			$('.limit').click(function() {
				if('${session.customer.accountName}' == '' || '${session.customer.email}' == '') {
					layer.open({
						content: '请先完善个人信息!',
						btn: ['<span class="bule">确认</span>', '<span class="orange">取消</span>'],
						yes: function(index) {
							window.location.href = '/mobile/new/myaccount.jsp'
							layer.close(index);
						}
					});
					return false;
				}
				return true
			})
		</script>
				<script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>
	</body>

</html>