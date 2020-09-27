<%@page import="dfh.utils.StringUtil"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	if(session.getAttribute(Constants.SESSION_CUSTOMERID)==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/login.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div id="deposit-fast-page-1">
			<!--极速转账-->

			<!--		<div class="space-2"></div>-->
			<div class="form-warp no-icon">
				<!--		<font color="">使用该通道，存款加赠0.5%！</font>-->
				<div class="form-tips">银行种类</div>
				<div class="two-col" id='deposit-fast-type'>
					<!--<div class="item">
						<div class="">支付宝转账</div>
						<div class="tips">fadfadf</div>
					</div>-->
				</div>
				<div class="form-tips">存款姓名</div>
				<div class="form-group">
					<input id="deposit-fast-name" type="text" placeholder="存款人姓名" class="form-control">
				</div>
				<div class="hidden">
					<div class="form-tips">存款银行</div>
					<div id="selectpay" class="form-group">
						<input id="deposit-fast-bank" type="text" class="form-control">
					</div>
					<div class="form-tips">存款卡号</div>
					<div id="card" class="form-group">
						<input id="deposit-fast-card" class="form-control" type="text" data-rule-digits="true" minlength="16" maxlength="20" placeholder="请输入您的存款卡号...">

					</div>
				</div>
				<div class="form-tips">存款金额</div>
				<div class="form-group">
					<input id="deposit-fast-money" class="form-control" type="text" data-rule-digits="true" maxlength="10" placeholder="存款金额额度为1元-300万">
				</div>
				<%--<span class="money_prompt">请您输入小数点，以确保实时到账！(如10.11)</span>--%>
				<!--<div class="testfueld">
    <div id="btn-history" class="mui-btn mui-btn--raised mui-btn--primary small">查看历史银行记录</div>
    </div>-->
				<div id="deposit-fast-submit" class="btn-submit">下一步</div>
				<div class="text-tips">
					<div class="h3"><strong>温馨提示：</strong></div>
					<p>
						<span class="c-ylow">使用秒存转账赠0.5%，满300再赠免费红包。</span><br/> 请务必按照系统提示消息进行存款，银行卡转账“附言”必须填写，支付宝转账无需附言完成之后请点击“我已成功存款”，否则您的款项将无法及时到账
						<br/> 如果您的款项10分钟未能到账，请联系24小时在线客服！
						<br/> 此存款方式无需手续费
					</p>
				</div>
			</div>
		</div>

		<script>
			var _htmls = [
				'<div class="item {{active}}" data-value="{{value}}">',
				'			<div class="">{{name}}</div>',
				'			<div class="tips">{{name}}</div>',
				'		</div>'
			].join('')
			$.each([{
					value: '2',
					name: '支付宝转账',
					bankType: '支付宝转账'
				},
				{
					value: '0',
					name: '手机银行转账',
					bankType: '手机银行转账'
				},
				{
					value: '1',
					name: '网上银行转账',
					bankType: '网上银行转账'
				},
				{
					value: '4',
					name: '微信转账',
					bankType: '微信转账'
				}
			], function(i, item) {
				$('#deposit-fast-type').append(_htmls.replace(/{{(\w+)}}/g, function(reg, $1) {
					if($1==='active'&&i==0){ //设置默认
						return "active";
					}
					return item[$1] || ''
				}))
			})
			$('#deposit-fast-type .item').click(function() {
				$(this).addClass("active").siblings().removeClass('active')
			})
		</script>
	</body>

</html>