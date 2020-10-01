<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<title>龙都娱乐城--手机投注</title>

</head>

<body>	
	<jsp:include page="/tpl/header.jsp"></jsp:include> <div class="wp pt20"><table class="account-info table-pay"></table></div>

	<div class="pay-online-wp">
		<s:url action="hfRedirect" namespace="/asp" var="dinpayRedirectUrl"></s:url>
		<form id="dinpayRedirect" action="${dinpayRedirectUrl}" method=post name="dinpayRedirect" id="dinpayRedirect">
			<input type="hidden" name="attach" id="attach" value="${session.customer.loginname}" />

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
							<input  name="amount" id="amount" type="text" maxlength="10" class="ipt-txt" />
							<span class="c-red">* 必填</span>
						</div>
					</td>

				</tr>
				<tr>
					<th>支付银行：</th>
					<td>
						<div class="ipt-group">
							<select name="Bankco" id="bankco" class="ipt-txt" >
								<option value="">－ 请选择银行 －</option>
								<option value="00004">工商银行</option>
								<option value="00015">建设银行</option>
								<option value="00017">农业银行</option>
								<option value="00083">中国银行</option>
								<option value="00021">招商银行</option>
								<option value="00005">交通银行</option>
								<option value="00087">平安银行</option>
								<option value="00023">深圳发展银行</option>
								<option value="00016">兴业银行</option>
								<option value="00041">华夏银行</option>
								<option value="00054">中信银行</option>
								<option value="00056">北京农村商业银行</option>
								<option value="00050">北京银行</option>
								<option value="00095">渤海银行</option>
								<option value="00096">东亚银行</option>
								<option value="00057">光大银行</option>
								<option value="00011">广州市农村信用社</option>
								<option value="00081">杭州银行</option>
								<option value="00013">民生银行</option>
								<option value="00055">南京银行</option>
								<option value="00051">邮政储蓄</option>
							</select>
							<span class="c-red">* 必填</span>
						</div></td>
				</tr>
				<tr>
					<th>&nbsp;</th>
					<td>
						<div class="ipt-group">
							<input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_onclick();" value="确定支付" />
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

	<jsp:include page="/tpl/footer.jsp"></jsp:include>
	<script type="text/javascript">
		function btnOK_onclick(){
			var amount = $("#amount").val();
			var attach = $("#attach").val();
			var bankco =  $("#bankco").val();
			if(isNaN(amount)){
				alert("[提示]存款额度非有效数字！");
				return false;
			}
			if(amount<1){
				alert("[提示]1元以上才能存款！");
				return false;
			}
			if(amount>2000){
				alert("[提示]存款金额不能超过2000！");
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
			$.post("${ctx}/asp/getPayAccounts.aspx", {
			}, function (returnedData, status) {
				if ("success" == status) {
					var result=returnedData;
					if (result == 1) {
					} else if (result == 0) {
						alert("在线支付正在维护！");
					}else if (result == 2) {
						alert("在线支付不存在！");
					}else{
						alert("网络繁忙！请稍后再试！");
					}
				}
			});
			return false;
		}
	</script>

	
</body>
</html>