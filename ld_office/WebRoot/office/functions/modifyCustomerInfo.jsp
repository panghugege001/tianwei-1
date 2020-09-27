<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改用户基本信息</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
			<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript">
	//得到用户信息
	function loadInfo() {
		var frm=document.getElementById("mainform");
		frm.action="<c:url value='/office/getUseremailphone.do' />";
		frm.submit();
	}
</script>
<style type="text/css">
		body{
			font-size:12px;
			background:#b6d9e4;
		}
</style>
	</head>
	<body>
		<div id="excel_menu_left">
			操作 --&gt; 修改Email联系电话
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<s:fielderror></s:fielderror>
		<s:form action="modifyUserRemark" onsubmit="submitonce(this);" namespace="/office" id="mainform" theme="simple">
			<table align="left" border="0">
				<tr>
					<td>
						<span style="color:red">*</span>会员账号:
					</td>
					<td>
						<s:textfield name="loginname" size="30" onblur="loadInfo();" />
					</td>
				</tr>
				<tr>
					<td>
						昵称:
					</td>
					<td>
						<s:textfield name="aliasName" size="30" value="%{#request.user.aliasName}" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>真实姓名:
					</td>
					<td>
						<s:textfield name="accountName" size="30" value="%{#request.user.accountName}" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>生日:
					</td>
					<td>
						<s:textfield name="birthday" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{#request.birthday}"  cssClass="Wdate"/>
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>Email:
					</td>
					<td>
						<s:textfield name="email" size="30" value="%{#request.user.email}" />
					</td>
				</tr>

				<tr>
					<td>
						<span style="color:red">*</span>联系电话:
					</td>
					<td>
						<s:textfield name="phone" size="30" value="%{#request.user.phone}" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>QQ:
					</td>
					<td>
						<s:textfield name="qq" size="30" value="%{#request.user.qq}" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						 <s:if test="#request.user.sms==0">
	                       			<input type="checkbox" checked="checked" name="sms" value="male" class="modifycheck" /> 
	                       	</s:if>
	                       	<s:else>
	                       		<input type="checkbox" name="sms" value="male" class="modifycheck" /> 
	                       	</s:else>
	                       	我要接收会员通讯及最新优惠计划
                        
				
					</td>
				</tr>
				<tr>
					<td>
						备注:
					</td>
					<td>
					 <s:textarea cols="50" rows="10" name="remark" value="%{#request.user.remark}"></s:textarea>
					</td>
				</tr>
				<tr>
					<td>

					</td>
					<td>
						<s:submit value="更  新"></s:submit>
						<s:reset value="重 置"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
