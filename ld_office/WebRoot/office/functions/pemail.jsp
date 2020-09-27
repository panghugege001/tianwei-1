<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp"%>
<%@ page import="dfh.model.Users" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Email平台</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript">
	function loadInfo() {
		var frm = document.getElementById("sendEmailForm");
		frm.action = "<c:url value='/office/getUserEmail.do' />";
		frm.submit();
	}
	
	function loademails() {
		var frm = document.getElementById("sendEmailForm");
		frm.action = "<c:url value='/office/getUsersEmail.do' />";
		frm.submit();
	}
	function alertMsg(a){
		if(a){
			alert("选择【用户类别】将群发给所有用户，请慎重选择！！！");
		}
	}
</script>
	</head>
	<body>
	
		<div id="excel_menu_left">
			其它 -->邮件平台
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<table width="90%" cellpadding="0" cellspacing="0" border="0">
			<span style="color: red"><s:fielderror></s:fielderror> </span>
			<s:form action="sendPEmail" namespace="/office" id="sendEmailForm"
				theme="simple" onsubmit="submitonce(this);">
				<tr>
					<td style="width: 20%; height: 40px;" align="right">
						主 题:
					</td>
					<td style="width: 50%">
						<s:textfield name="title" size="60"></s:textfield>
					</td>
					<td style="width: 20%">
						<font color="#FF0000">*</font>邮件标题!
					</td>
				</tr>
				
				<tr>
					<td style="width: 20%; height: 40px;" align="right">
						用户账户:
					</td>
					<td style="width: 50%">
						
						<s:textfield name="loginname" maxlength="20" readonly="true"  value="%{#parameters.loginname}"></s:textfield>
					</td>
					<td style="width: 20%" style="color:red;">
						输入用户的登陆账号系统将自动查询出其Email
					</td>
				</tr>
				
				<tr>
					<td style="width: 20%" align="right">
						邮件内容:
					</td>
					<td style="width: 50%">
						<s:textarea name="msg" rows="8" cols="60"></s:textarea>
					</td>
					<td style="width: 20%">
						<font color="#FF0000">*</font>邮件内容(如抄送网址不为空的话此可为空)
					</td>
				</tr>


				<tr>
					<td style="width: 20%; height: 40px;">

					</td>
					<td style="width: 50%">
						<s:submit name="submitEmail" value="确定发送"></s:submit>
					</td>
					<td style="width: 20%">
					</td>
				</tr>


			</s:form>
		</table>

<!--  
		<br />
		<br />
		<div id="excel_menu_left">
			其它邮件平台模版发送
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		-->
<!--  
		<s:form action="stencilSendMail" namespace="/office"
			id="sendEmailForm" theme="simple" onsubmit="submitonce(this);">
			<table width="90%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td style="width: 20%; height: 40px;" align="right">
						主 题:
					</td>
					<td style="width: 50%">
						<s:textfield name="title" size="60"></s:textfield>
					</td>
					<td style="width: 20%">
						<font color="#FF0000">*</font>邮件标题!
					</td>
				</tr>
				<tr>
					<td style="width: 20%; height: 40px;" align="right">
						用户类别：
					</td>
					<td style="width: 50%">
						<s:select list="%{#application.UserRole}" name="roleCode"
							listKey="code" listValue="text" emptyOption="true"></s:select>
					</td>
					<td style="width: 20%" style="color:red;">
						选择用户类型系统将自动查询出此类的所有会员email
					</td>
				</tr>
				<tr>
					<td style="width: 20%; height: 40px;" align="right">
						收件人:
					</td>
					<td style="width: 50%">
						<s:textarea name="email" rows="6" cols="60"></s:textarea>
					</td>
					<td style="width: 20%">
						收件人(多个以,隔开 选择了用户类型此处可为空)
					</td>
				</tr>



				<tr>
					<td style="width: 20%; height: 40px;" align="right">
						选择模版:
					</td>
					<td style="width: 50%">
						<s:radio name="stencil" list="#{'1':'模版一'}"></s:radio>
					</td>
					<td style="width: 20%">

					</td>
				</tr>

				<tr>
					<td style="width: 20%; height: 40px;">
					</td>
					<td style="width: 50%">
						<s:submit name="submitEmail" value="确定发送"></s:submit>
					</td>
					<td style="width: 20%">
					</td>
				</tr>

			</table>
		</s:form>

-->



























		<c:import url="/office/script.jsp" />
	</body>
</html>

