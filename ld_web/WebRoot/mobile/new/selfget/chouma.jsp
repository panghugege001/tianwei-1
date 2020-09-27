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
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="免费筹码" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<style>
			.btn-submit[disabled] {
				background: #2c8ba3;
			}
		</style>
	</head>

	<body>
		<div class="free-top">
			<div class="level"><s:property value=" @dfh.model.enums.VipLevel@getText(#session.customer.level) " /></div>
			<div class="txt"> 本月筹码金额（元）</div>
			<div class="c-ylow" id="moneyVal"></div>
		</div>
		<div class="form-warp txt-form ">
			<div class="form-group hidden">
				<label class="form-label">会员等级：</label>
				<input style="color: #ed6325;" type="text" class="form-control" readonly value="<s:property value=" @dfh.model.enums.VipLevel@getText(#session.customer.level) "/>">
			</div>
			<div class="form-group hidden">
				<label class="form-label">转账金额：</label>
				<label class="form-label">目标帐户：</label>
				<select id="j-chipPaltform" class="form-control">
					<!-- <option value="" selected="">请选择游戏平台</option>
						<option value="PT">PT</option>
						<option value="TTG">TTG</option>
						<option value="QT">QT</option>
						<option value="NT">NT</option>
						<option value="MG">MG</option>
						<option value="DT">DT</option>
						<option value="PNG">PNG</option> -->
				</select>
			</div>
			<div class="form-group hidden">
				<label class="form-label">筹码金额：</label>
				<input class="form-control" type="text" disabled="" readonly>
			</div>
			<div class="form-group hidden" style="display: none;">
				<label class="form-label">流水倍数：</label>
				<input id="theMultiple" class="form-control" type="text" disabled="" readonly>
			</div>
			<input id="youhui-id" type="hidden">
			<input type="button" class="btn-submit" id="mfcm-submit" value="立即领取">
			<div class="text-tips">
				<div class="h3">温馨提示：</div>
				<ol>
					<li>忠实会员及以上会员，每月可领取一次VIP免费筹码。</li>
					<li>免费筹码以您领取时的VIP等级来进行派发。</li>
					<li>免费筹码领取后，直接添加到天威主帐户。</li>
					<li>免费筹码无需流水即可提款。</li>
					<li>领取过程中有任何疑问，请咨询24小时在线客服。</li>
				</ol>
			</div>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
	</body>
	<script>
		// 领取免费筹码
		function chouma() {
			$.ajax({
				type: "post",
				url: "/asp/checkActivityInfo.aspx",
				cache: false,
				beforeSend: function() {
					$('#theMultiple').val('加载中');
					$('#moneyVal').text("加载中");
				},
				data: {
					titleId: 'vipmonthfree'
				},
				success: function(data) {

					if(!data.amount) {
						$('#moneyVal').text(0 + '元');
					} else {
						$('#moneyVal').text(data.amount + '元');
					}
					$('#theMultiple').val(data.multiple + '倍');
					var htmlarr = ['<option value="" selected="">请选择游戏平台</option>'],
						platformarr;
					if(!data.platform) {
						$('.need-hide').hide();
					} else {
						platformarr = data.platform.split(',');
						for(var i = 0; i < platformarr.length; i++) {
							htmlarr.push('<option value="' + platformarr[i] + '">' + platformarr[i] + '</option>');
						}
					}
					$('#j-chipPaltform').html(htmlarr.join(''));
				},
				error: function() {
					("系统错误");
				},
				complete: function() {}
			});
		}
		chouma()
		$('#mfcm-submit').on('click', function() {
			var that = this;
			$(that).attr('disabled', '').val("领取中...")
			$.post('/asp/applyActivity.aspx', {
				titleId: 'vipmonthfree',
				platform: $('#j-chipPaltform').val(),
				entrance: 'pc'
			}, function(data) {
				alert(data);
				setTimeout(function() {
					$(that).removeAttr('disabled').val("立即领取")
				},500)
			})
		})
	</script>

</html>