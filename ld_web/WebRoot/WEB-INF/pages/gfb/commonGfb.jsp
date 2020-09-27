<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<title>龙都娱乐城--手机投注</title>
</head>

<body>
<div class="pay-page">
	<jsp:include page="/tpl/header.jsp"></jsp:include>
	<div class="pay-online-wp">
		<form id="gfbRedirect" action="${ctx}/gfb/commitPay.aspx" method=post name="gfbRedirect">
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
							<input  name="tranAmt" id="tranAmt" type="text" maxlength="10" class="ipt-txt" />
							<span class="c-red">* 必填</span>
						</div>
					</td>

				</tr>
				<tr>
					<th>支付银行：</th>
					<td>
						<div class="ipt-group">
							<select name="bankCode" id="bankCode" class="ipt-txt">
								<option value=""> － 请选择银行 － </option>
			                    <option value="CCB"> 中国建设银行 </option>
			                    <option value="CMB"> 招商银行 </option>
			                    <option value="ICBC"> 中国工商银行 </option>
			                    <option value="BOC"> 中国银行 </option>
			                    <option value="ABC"> 中国农业银行 </option>
			                    <option value="BOCOM"> 交通银行 </option>
			                    <option value="CMBC"> 中国民生银行 </option>
			                    <option value="HXBC"> 华夏银行 </option>
			                    <option value="CIB"> 兴业银行 </option>
			                    <option value="SPDB"> 上海浦东发展银行 </option>
			                    <option value="GDB"> 广东发展银行 </option>
			                    <option value="CITIC"> 中信银行 </option>
			                    <option value="CEB"> 光大银行 </option>
			                    <option value="PSBC"> 中国邮政储蓄银行 </option>
			                    <option value="BOBJ"> 北京银行 </option>
			                    <option value="TCCB"> 天津银行 </option>
			                    <option value="BOS"> 上海银行 </option>
			                    <option value="PAB"> 平安银行 </option>
			                    <option value="NBCB"> 宁波银行 </option>
			                    <option value="NJCB"> 南京银行 </option>
							</select>
							<span class="c-red">* 必填</span>
						</div></td>
				</tr>
				<tr>
					<th>&nbsp;</th>
					<td>
						<div class="ipt-group">
							<input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_gfb_onclick();" value="确定支付" />
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

	
	</div><jsp:include page="/tpl/footer.jsp"></jsp:include>
	<script type="text/javascript">
		function btnOK_gfb_onclick(){
			var amount = $("#tranAmt").val();
			var attach = $("#attach").val();
			var bankco =  $("#bankCode").val();
			if(isNaN(amount)){
				alert("[提示]存款额度非有效数字！");
				return false;
			}
			if(amount<1){
				alert("[提示]1元以上或者1元才能存款！");
				return false;
			}
			if(amount>50000){
				alert("[提示]存款金额不能超过50000！");
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
			$("#gfbRedirect").submit();
			return true;
		}
	</script>
	
</body>
</html>