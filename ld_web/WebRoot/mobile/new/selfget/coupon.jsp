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
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="优惠券专区" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>
	<body>
		<div class="form-warp txt-form">
			<div class="form-group zf-sele zf-sele1">
				<label class="form-label">从天威帐户转帐到：</label>
				<select id="coupon-platform" class="form-control"></select>
			</div>
			<div class="form-group">
				<label class="form-label">存款金额：</label>
				<input id="coupon-money" class="form-control" type="text">
			</div>
			<div class="form-group">
				<label class="form-label">优惠代码：</label>
				<input id="coupon-code" class="form-control" type="text">
			</div>
			<div class="ui-button-row center">
				<div class="btn-submit block" id="coupon-submit">领取</div>
			</div>
			<div class="text-tips">
				<div class="h3">100%存送优惠券、88%存送优惠券、68%存送优惠券</div>
				<ol>
					<li>只限老虎机平台使用，需游戏账户低于5元才能使用存送优惠券，填写红利代码，确认游戏平台，提交后在相关的游戏里面会自动得到优惠礼金。</li>
					<li>达到流水倍数或游戏账户低于5元即可进行转入转出。</li>
					<li>优惠券为30天有效期限，逾时未使用恕不进行补发。</li>
				</ol>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			CouponPage()
			function CouponPage() {
				var that = this;
				that.platform = $('#coupon-platform');
				that.code = $('#coupon-code');
				that.money = $('#coupon-money');
				that.submit = $('#coupon-submit');
				that.submit.click(_submit);
				if($('#coupon-platform').get(0)) {
					that.platform = new MobileComboBox({
						appendId: 'coupon-platform',
						valueName: 'value',
						displayName: 'name',
						datas: [{
								value: 'pt',
								name: 'PT'
							},
							{
								value: 'ttg',
								name: 'TTG'
							},
							{
								value: 'slot',
								name: '老虎机账户(SW,MG,DT,PNG,QT,NT)'
							}
						]
					});
				}

				//优惠卷转帐
				function _submit() {

					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().transferInforCoupon({
						platform: that.platform.getValue(),
						couponCode: that.code.val(),
						couponRemit: that.money.val()
					}, function(result) {
						mobileManage.getLoader().close();
						if(result.success) {
							alert(result.message);
						} else {
							alert(result.message);
						}
					});
				}
			}
		</script>
	</body>

</html>