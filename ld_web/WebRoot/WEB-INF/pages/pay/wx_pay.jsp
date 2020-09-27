<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/title.jsp"></jsp:include>
	<script type="text/javascript">
		var loginname="${session.customer.loginname}";
		if(loginname==null || loginname==""){
			window.location.href="${ctx}/"
		}
	</script>
</head>
<body>
<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>

<div class="container pay-wp">
	<form id="dinpayRedirect" action="${ctx}/asp/pay_api.php" method=post>
		<input type="hidden" name="platformId" id="platformId" value="${platformId}"/>
		<input type="hidden" name="loginName" id="loginName" value="${session.customer.loginname}"/>
		<input type="hidden" name="payUrl" id="payUrl" value="${payUrl}"/>
		<input type="hidden" name="usetype" value="2"/>
		<table class="tb-pay">
			<tr>
				<td align="center">账户信息：</td>
				<td>账户 名: ${session.customer.loginname} </td>
			</tr>
			<tr>
				<td>账户余额：</td>
				<td>${session.customer.credit}元</td>
			</tr>
			<tr>
				<td> 存款额度：</td>
				<td><input name="orderAmount" id="orderAmount" type="text" maxlength="10" class="ui-ipt" />
					<span class="c-red">* 必填</span>
				</td>
			</tr>
			<tr>
				<td> 支付方式：</td>
				<td>
					<select name="payCode" id="bankco" class="userFriendSel">
 						<option value="ZF_WX">微信支付</option>
					</select>
					<span class="c-red">* 必填</span>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input name="Submit" type="button" class="btn" onclick="return btnOK_zf_onclick();" value="确定支付"></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<div class="prompt-info">
						<h3 class="tit">温馨提示：</h3>
						<p>1.最低存款额度为10元，最高500元；</p>
						<p>2.若支付成功未及时到账，请立即联系我们的在线客服。</p>
                        <p>3.如微信支付被限额或无法支付，请使用秒存转账</p>
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script type="text/javascript">
	function btnOK_zf_onclick(){
		var amount = $("#orderAmount").val();
		var attach = $("#loginName").val();
		var bankco = $("#bankco").val();
		if(isNaN(amount)){
			alert("[提示]存款额度非有效数字！");
			return false;
		}
		if(attach==null ||attach==""){
			alert("[提示]系统检测你已经掉线,请重新登录！");
			return false;
		}
		$("#dinpayRedirect").submit();
		return true;
	}
</script>
</body>
</html>