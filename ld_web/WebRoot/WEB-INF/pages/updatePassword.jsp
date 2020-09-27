<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>

<body>	
	<jsp:include page="/tpl/header.jsp"></jsp:include>
	<div class="pay-online-wp">
		<form>
			<table class="account-info table-pay">
				<tr>
					<td class="first" style="text-align: right;">原密码：</td>
					<td height="50"><input style="width: 230px;" class="text"
						type="password" id="updatePassword" maxlength="12" /></td>
					<td></td>
				</tr>
				<tr>
					<td class="first" style="text-align: right;">新密码：</td>
					<td height="50"><input style="width: 230px;" class="text"
						type="password" id="updateNew_password" maxlength="12" /></td>
					<td></td>
				</tr>
				<tr>
					<td class="first" style="text-align: right;">密码确认：</td>
					<td height="50"><input style="width: 230px;" class="text"
						type="password" id="updateSpass2" maxlength="12" /></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="reset" onclick="return updateDatePassword();"
						class="btn btn-danger" value="提交" /></td>
					<td></td>
				</tr>
				<tr>
					<td height="40" align="right"></td>
					<td height="90" colspan="2">温馨提示：
						您的密码过于简单，为了您的资金安全请修改密码，以免账户信息泄露！感谢配合！！！</td>
				</tr>

			</table>
		</form>
	</div>

	
	<jsp:include page="/tpl/footer.jsp"></jsp:include>

	<script type="text/javascript">
	//更新密码
	function updateDatePassword(){
	    var updatePassword=$("#updatePassword").val();
	    var updateNew_password=$("#updateNew_password").val();
	    var updateSpass2=$("#updateSpass2").val();
	    if (updatePassword==''){
		    	alert("[提示]用户旧密码不可为空！");
		    	return false;
		   }
		   if (updateNew_password==''){
	 		alert("[提示]用户新密码不可为空！");
	 		return false;
		   }
	    if (updateSpass2==''){
	 		alert("[提示]用户确认新密码不可为空！");
	 		return false;
		   }
		   if (updateSpass2 != "" && (updateSpass2 < 8 || updateSpass2 >12)){
	         alert("[提示]密码的长度请介于8-12字符之间！")
	         return false;
	    }
	    if (updateNew_password!=updateSpass2){
	         alert("[提示]两次输入的密码不一致，请核对后重新输入！");
	         return false;
	    }
	    openProgressBar();
	    $.post("${ctx}/asp/change_pwsAjax.aspx", {
	      "password":updatePassword,
		     "new_password":updateNew_password,
		     "sPass2":updateSpass2
	    }, function (returnedData, status) {
	       	if ("success" == status) {
	          	closeProgressBar();
	          	alert(returnedData);
	          	if(returnedData=="修改成功"){
	       			window.location.href="${ctx}/";
	       		}
	       	}
	    });
	    return false;
	}
	</script>
</body>
</html>