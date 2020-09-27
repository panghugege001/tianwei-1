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
		<form id="dinpayRedirect" action="${ctx}/asp/dinpayRedirectDk.aspx" method=post name="dinpayRedirect" id="dinpayRedirect">
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
							<input  name="order_amount" id="amount" type="text" maxlength="10" class="ipt-txt" />
							<span class="c-red">* 必填</span>
						</div>
					</td>

				</tr>
				<tr>
					<th>支付点卡：</th>
					<td>
						<div class="ipt-group">
							<select name="bank_code" id="bankco" class="ipt-txt">
								<option value="">
									－ 请选择点卡 －
								</option>
								<option value="YDSZX">
									移动神州行
								</option>
								<option value="DXGK">
									电信国卡
								</option>
								<option value="LTYKT">
									联通一卡通
								</option>
								<option value="QBCZK">
									QQ币充值卡
								</option>
								<option value="JWYKT">
									骏网一卡通
								</option>
								<option value="SDYKT">
									盛大一卡通
								</option>
								<option value="WMYKT">
									完美一卡通
								</option>
								<option value="ZTYKT">
									征途一卡通
								</option>
								<option value="WYYKT">
									网易一卡通
								</option>
								<option value="SFYKT">
									盛付通一卡通
								</option>
								<option value="SHYKT">
									搜狐一卡通
								</option>
								<option value="JYYKT">
									九游一卡通
								</option>
								<option value="THYKT">
									天宏一卡通
								</option>
								<!-- <option value="GYYKT">
                                    光宇一卡通
                                </option> -->
								<option value="TXYKT">
									天下一卡通
								</option>
								<option value="ZYYKT">
									纵游一卡通
								</option>
								<option value="TXYKTZX">
									天下一卡通专项
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
			<p class="mb20 c-red">温馨提示：建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的。到账金额比实际存款金额少。到账比例 请查看费率表。</p>

			<table class="table">
				<tbody>
				<tr>
					<th colspan="4">点卡支付业务费率</th>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 移动神州行</td>
					<td>费率：</td>
					<td>0.05</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td>电信国卡</td>
					<td>费率：</td>
					<td>0.05</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td> QQ币充值卡</td>
					<td>费率：</td>
					<td>0.14</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 联通一卡通</td>
					<td>费率：</td>
					<td>0.05</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 骏网一卡通</td>
					<td>费率：</td>
					<td>0.16</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 盛大一卡通</td>
					<td>费率：</td>
					<td>0.13</td>

				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 完美一卡通</td>
					<td>费率：</td>
					<td>0.14</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 征途一卡通</td>
					<td>费率：</td>
					<td>0.13</td>

				</tr>
				<tr>
					<td>卡类型：</td>
					<td> 网易一卡通</td>
					<td>费率：</td>
					<td>0.14</td>
				</tr>

				<tr>
					<td>卡类型：</td>
					<td>搜狐一卡通</td>
					<td>费率：</td>
					<td>0.16</td>

				</tr>
				<tr>
					<td>卡类型：</td>
					<td>久游一卡通</td>
					<td>费率：</td>
					<td>0.2</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td>天宏一卡通</td>
					<td>费率：</td>
					<td>0.17</td>
				</tr>

				<tr>
					<td>卡类型：</td>
					<td>天下一卡通</td>
					<td>费率：</td>
					<td>0.18</td>
				</tr>
				<%--<tr>
					<td>卡类型：</td>
					<td> 光宇一卡通</td>
					<td>费率：</td>
					<td>0.17</td>
				</tr>--%>
				<tr>
					<td>卡类型：</td>
					<td>纵游一卡通</td>
					<td>费率：</td>
					<td>0.16</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td>天下一卡通专项</td>
					<td>费率：</td>
					<td>0.19</td>
				</tr>
				<tr>
					<td>卡类型：</td>
					<td>盛付通一卡通</td>
					<td>费率：</td>
					<td>0.14</td>

				</tr>

				</tbody></table>
		</form>
	</div>

	</div><jsp:include page="/tpl/footer.jsp"></jsp:include>
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