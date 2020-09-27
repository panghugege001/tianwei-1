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
		<form id="dinpayRedirect" action="${ctx}/asp/hfRedirect.aspx" method=post name="dinpayRedirect">
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
							<input  name="OrdAmt" id="amount" type="text" maxlength="10" class="text" />
							<span class="c-red">* 必填</span>
						</div>
					</td>
				</tr>
				<tr>
					<th> 支付银行：</th>
					<td><select name="GateId" id="bankco" class="ipt-txt">
						<option value="">
							－ 请选择银行 －
						</option>
						<option value="45">
							中国银行
						</option>
						<option value="25">
							工商银行
						</option>
						<option value="27">
							建设银行
						</option>
						<!-- <option value="28">
                            招商银行
                        </option> -->
						<option value="29">
							农业银行
						</option>
						<option value="33">
							中信银行
						</option>
						<!--<option value="36">
							光大银行
						</option>-->
						<option value="46">
							邮储银行
						</option>
						<option value="12">
							民生银行
						</option>
						<!-- <option value="50">
                            平安银行
                        </option> -->
						<option value="13">
							华夏银行
						</option>
						<!-- <option value="16">
                            浦发银行
                        </option> -->
						<!-- <option value="19">
                            广发银行
                        </option> -->
						<option value="15">
							北京银行
						</option>
						<option value="09">
							兴业银行
						</option>
						<option value="49">
							南京银行
						</option>
						<option value="51">
							杭州银行
						</option>
						<option value="54">
							上海银行
						</option>
						<option value="55">
							渤海银行
						</option>
					</select>
						<span class="c-red">* 必填</span>
					</td>
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
			<input type="hidden" name="attach" id="attach" value="${session.customer.loginname}" />
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
			$("#dinpayRedirect").submit();
			return true;
		}
	</script>
</body>
</html>