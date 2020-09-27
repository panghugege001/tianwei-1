﻿<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>


<body>	
	<div class="pay-page">
		<jsp:include page="/tpl/header.jsp"></jsp:include>

		<div class="pay-online-wp">
			
			 <jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>
			<form id="dinpayRedirect" action="${ctx}/asp/ZfWxRedirect.aspx" method=post name="dinpayRedirect"  id="dinpayRedirect">
				<input type="hidden" name="attach" id="attach" value="${session.customer.loginname}" />
				<input type="hidden" name="payType" id="payType" value="${payType}" />
				<table class="account-info table-pay">
					<tr>
						<th>账户名:</th>
						<td>${session.customer.loginname}</td>
					</tr>
					<tr>
						<th>账户余额：</th>
						<td class="c-red">${session.customer.credit}元</td>
					</tr>
					<tr>
						<th> 存款额度：</th>
						<td>
							<div class="ipt-group">
								<input  name="order_amount" id="amount" type="text" maxlength="10" class="ipt-txt" />
								<span class="c-red">* 必填</span>
							</div>
						</td>

					</tr>
					<tr>
						<th>支付银行：</th>
						<td>
							<div class="ipt-group">
								<select name="bank_code" id="bankco" class="ipt-txt">
									<option value="ZF_WX">
										微信支付
									</option>
								</select>
								<span class="c-red">* 必填</span>
							</div></td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>
							<div class="ipt-group">
								<input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_zf_onclick();" value="确定支付" />
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							温馨提示：为了避免掉单情况的发生，请您在支付完成后，需等“支付成功”页面跳转出来，点
							<input name="Submit22" type="button" class="pay-tip warning" value="商城取货" />
							或
							<input name="Submit2" type="button" class="pay-tip danger" value="通知客户" />
							，再关闭页面,以免掉单！感谢配合！！！
						</td>
					</tr>
				</table>
			</form>
		</div>

	</div>

	<jsp:include page="/tpl/footer.jsp"></jsp:include>
	<script type="text/javascript">
		function btnOK_zf_onclick(){
			var amount = $("#amount").val();
			var attach = $("#attach").val();
			var bankco =  $("#bankco").val();
			if(isNaN(amount)){
				alert("[提示]存款额度非有效数字！");
				return false;
			}
			if(amount<1){
				alert("[提示]1元以上或者1元才能存款！");
				return false;
			}
			if(amount>3000){
				alert("[提示]存款金额不能超过3000！");
				return false;
			}
			if(bankco==null ||bankco==""){
				alert("[提示]支付银行不能为空！");
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