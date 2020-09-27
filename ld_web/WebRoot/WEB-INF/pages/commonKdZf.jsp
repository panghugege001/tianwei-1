<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%-- <jsp:include page="/title.jsp"></jsp:include> --%>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<title>龙都娱乐城--手机投注</title>
		<script type="text/javascript">
		   $(document).ready(function () {
		  		var loginname="${session.customer.loginname}";
		  		if(loginname==null || loginname==""){
		      		window.location.href="${ctx}/"
		        }
	   	   });
		  function btnOK_zf_onclick(){
			var amount = $("#amount").val();
			var attach = $("#attach").val();
			var bankco =  $("#bankco").val();
		  	if(isNaN(amount)){
		   		alert("[提示]存款额度非有效数字！");
		   		return false;
		   	}
		  	var reg = /^[1-9]\d*$/;
		  	if(!reg.test(amount)){
		  		alert("[提示]请输入整数！");
				return;
		  	}
		   	if(amount<1){
			   alert("[提示]1元以上或者1元才能存款！"); 
		   	   return false;
		   	}
		   	if(amount>1000){
			   alert("[提示]存款金额不能超过1000！");
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
	</head>
	<body>
	<div class="pay-page">
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		
		<div class="pay-online-wp"  id="pay-online-wp">
			<jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>	
			<form id="dinpayRedirect" action="${ctx}/asp/KdWxZfRedirects.aspx" method=post
				name="dinpayRedirect" id="dinpayRedirect">
				<input type="hidden" name="attach" id="attach"
					value="${session.customer.loginname}" />
				<input type="hidden" name="payType" id="payType" value="${payType}" />
				<table class="account-info table-pay">
					<tbody>
					<tr>
						<th>
							账户信息：
						</th>
						<td>
							账户名：${session.customer.loginname}    龙都账户余额：${session.customer.credit}元
						</td>
					</tr>
					<tr>
						<th>
							存款额度：
						</th>
						<td>
							<input class="input" name="amount" id="amount" type="text" maxlength="10"/>
							*必选
						</td>
					</tr>
					<tr>
						<th>
							支付银行：
						</th>
						<td>
							<select name="payCode" id="bankco" class="input">
								<option value="100040">
									支付宝支付
								</option>
							</select>
							*必选
						</td>
					</tr>
					
					<tr>
						<td></td>
						<td colspan="2" align="left">
							<input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_zf_onclick();" value="确定支付" />
							<%-- <img src="${ctx}/images/submit.png" style="cursor: pointer"
								onclick="return btnOK_zf_onclick();" /> --%>
							&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
					<td colspan="2">
						温馨提示：为了避免掉单情况的发生，请您在支付完成后，需等“支付成功”页面跳转出来，点击
						<input name="Submit22" type="button" class="pay-tip warning" value="商城取货" />
						或
						<input name="Submit2" type="button" class="pay-tip danger" value="通知客户" />，再关闭页面,以免掉单！感谢配合！！！
					</td>
				</tr> 
				</tbody>
				</table>
			</form>
		</div>
	</div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	</body>
</html>