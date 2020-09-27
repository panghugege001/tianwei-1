<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>  
<!DOCTYPE html>
<html>
<head>
<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
<title>龙都娱乐城--手机投注</title>
</head>

<body>
	<div class="pay-page">
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>

		<div class="pay-online-wp">
			<form id="dinpayRedirect" action="https://shenghuo.alipay.com/send/payment/fill.htm" method=post name="dinpayRedirect" id="dinpayRedirect">
				<table class="account-info table-pay">

					<tbody>
					<tr>
						<th>账户信息：</th>
						<td><span class="c-red">${session.customer.loginname}</span>
							<input type="hidden" name="attach" id="attach"
								   value="${session.customer.loginname}" />
						</td>
					</tr>
					<tr>
						<th>账户余额：</th>
						<td><span class="c-red">${session.customer.credit}元</span>
						</td>
					</tr>
					<tr>
						<th>支付宝账号：</th>
						<td>
							<div class="ipt-group">
								<input  name="optEmail" readonly="readonly"
										value="${zfbBank.accountno}" type="text" maxlength="10" class="ipt-txt" />
							</div>
						</td>
					</tr>
					<tr>
						<th>附言：</th>
						<td>
							<div class="ipt-group">
								<input  name="title" readonly="readonly"
										value="${zfbBank.vpnpassword}" type="text" maxlength="10" class="ipt-txt"/>
								<span>* 必填</span>
							</div>
						</td>
					</tr>
					<!-- <tr>
                        <th>存款额度：</th>
                        <td>
                            <div class="ipt-group">
                                <input  name="payAmount" id="amount" type="text" maxlength="10" class="ipt-txt"/>
                                <span>* 必填 10元以上或者10元才能存款</span>
                            </div>
                        </td>
                    </tr> -->

					<tr>
						<th></th>
						<td>
							<div class="ipt-group">
								<!-- <input name="Submit" type="button" class="btn btn-danger"  onclick="return btnOK_zf_onclick();" value="点击跳转支付宝支付" style="height: 36px;line-height: 36px;"/> -->
								<a class="btn btn-danger" href="https://www.alipay.com/" target="_blank">跳转到支付宝</a>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<p class="c-red">温馨提示：龙都国际娱乐欢迎您，为了您的存款能够安全快速到账， 请勿修改收款人和付款说明，祝您游戏愉快，谢谢。</p>
						</td>
					</tr>
					</tbody>

				</table>
			</form>
		</div>
	</div>

	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
</body>
<script src="${ctx}/js/lib/jquery-1.11.2.min.js"></script>
<script>
	function btnOK_zf_onclick(){
		var amount = $("#amount").val();
		var attach = $("#attach").val();
		if(isNaN(amount)){
			alert("[提示]存款额度非有效数字！");
			return false;
		}
		if(amount<1){
			alert("[提示]1元以上或者1元才能存款！");
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
</html>