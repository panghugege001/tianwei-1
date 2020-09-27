<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<title>龙都娱乐城--手机投注</title>
</head>

<body>	
	<jsp:include page="/tpl/header.jsp"></jsp:include>
	<div class="pay-online-wp">
		<form id="bfbRedirect" action="${ctx}/bfb/commitPay.aspx" method=post name="bfbRedirect">
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
							<input  name="p6_amount" id="amount" type="text" maxlength="10" class="ipt-txt" />
							<span class="c-red">* 必填</span>
						</div>
					</td>

				</tr>
				<tr>
					<th>支付银行：</th>
					<td>
						<div class="ipt-group">
							<select name="p4_pd" id="bankco" class="ipt-txt">
								<option value="">
									－ 请选择银行 －
								</option>
								<option value="10001">招商银行</option>
								<option value="10002">兴业银行</option>
								<option value="10003">中信银行</option>
								<option value="10004">民生银行</option>
								<option value="10005">光大银行</option>
								<option value="10006">华夏银行</option>
								<option value="10007">北京农村商业银行</option>
								<option value="10008">深圳发展银行</option>
								<option value="10009">中国银行</option>
								<option value="10010">北京银行</option>
								<option value="10011">邮政储蓄银行</option>
								<option value="10012">上海浦发银行</option>
								<option value="10013">东亚银行</option>
								<option value="10014">广东发展银行</option>
								<option value="10015">南京银行</option>
								<option value="10016">上海交通银行</option>
								<option value="10017">平安银行</option>
								<option value="10018">中国工商银行</option>
								<option value="10019">杭州银行</option>
								<option value="10020">中国建设银行</option>
								<option value="10021">宁波银行</option>
								<option value="10022">中国农业银行</option>
								<option value="10023">浙商银行</option>
							</select>
							<span class="c-red">* 必填</span>
						</div></td>
				</tr>
				<tr>
					<th>&nbsp;</th>
					<td>
						<div class="ipt-group">
							<input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_bfb_onclick();" value="确定支付" />
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
		function btnOK_bfb_onclick(){
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
			if(amount>5000){
				alert("[提示]存款金额不能超过5000！");
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
			$("#bfbRedirect").submit();
			return true;
		}
	</script>
	
</body>
</html>