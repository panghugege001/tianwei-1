<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<title>龙都娱乐城--手机投注</title>
	<style>
		.fl{float:left;}
	</style>
</head>
<body>
<div class="pay-page">
	<jsp:include page="/tpl/header.jsp"></jsp:include>

	<div class="pay-online-wp">
		<%--<jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>--%>
		<form id="dinpayRedirect" action="" method=post name="dinpayRedirect" id="dinpayRedirect">
		
			<table class="account-info table-pay">
				<tr>
					<td width="168" height="40">账户 名: ${session.customer.loginname} </td>
					<td width="460" height="40">账户余额：${session.customer.credit}元</td>
					<td width="133" height="40"></td>
				</tr>
				<tr class="showImg" >
					<td colspan="5" align="center"><p id="depositResult" style="padding:9px 0;">
						<span class="c-red" style="font-size:18px;">请扫码后在备注栏填写您的游戏账号：(例如:longdu) 可立即到账,账号写错或不写则无法到账!!!<br/>重要：二维码实时更新，请务必每次刷新</span> </p>
					</td>
				</tr>
				<tr class="showImg" >
					<td align="right" width="130" >通用支付二维码：</td>
					<td  colspan="4" align="left" style="padding-left: 25px;">
						<div class="fl">
							<img src="${bank.zfbImgCode}" style="width:300px;"/>
						</div>
						<div class="fl">
							<img src="/images/gather-demo.jpg?v=1" style="width:400px;"/>
							<p class="c-red text-center"> 支持：微信，支付宝，QQ钱包扫码。</p>
						</div>
					</td>
				</tr>
				<tr>
					<td height="40" align="right"></td>
					<td height="90" colspan="4" class="wenxin"><p>温馨提示：</p>
						<p>1.二维码实时更新，请每次刷新。</p>
						<p>2.单笔最低存款20元，最高存款5000。</p>
						<p>3.通用支付通道需承担0.35%的手续费，手续费由第三方收取。</p>
						<p>4.如果您已支付 请等待系统处理此笔订单，如有疑问可以随时联系在线客服进行谘询。</p>
					</td>
				</tr>
			</table>
			
		</form>
	</div>
</div>

<jsp:include page="/tpl/footer.jsp"></jsp:include>


</body>
</html>