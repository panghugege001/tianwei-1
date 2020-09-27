<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<s:head theme="xhtml" />
		<sx:head parseContent="true" />
		<title>短信平台</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<style type="text/css">
/*.search_margin{*/ /*margin-left:8px;*/ /*}*/
.label_search_td_play {
	font-family: Tahoma;
	font-size: 15px;
	/*font-size: 11px;*/
	line-height: 28px;
	font-weight: bold;
	/* text-align: center;*/
	text-transform: capitalize;
	color: #FFFFFF;
	text-decoration: none;
	padding-right: 1px;
}

.input {
	font-family: Tahoma;
	font-size: 18px;
	/*font-size: 11px;*/
	font-weight: normal;
	/*text-transform: capitalize;*/
	text-decoration: none;
	background-color: #FFFFFF;
	border: 1px solid #336699;
	line-height: 16px;
	height: 22px;
	float: left;
	margin-top: 2px;
}
</style>

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
			alert("选择群发用户，请慎重选择！！！");
		}
	}
	function showAndHidden(eid){
		if(eid=="a1"){
			document.getElementById("a1").style.display="block";
			document.getElementById("a2").style.display="none";
			document.getElementById("a3").style.display="none";
		}else if(eid=="a2"){
			document.getElementById("a1").style.display="none";
			document.getElementById("a2").style.display="block";
			document.getElementById("a3").style.display="none";
		}else{
			document.getElementById("a1").style.display="none";
			document.getElementById("a2").style.display="none";
			document.getElementById("a3").style.display="block";
		}
	}
</script>

	</head>
	<body style="background: #b6d9e4; font-size: 12px">
		<div id="excel_menu_left">
			其它 -->邮件平台
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<div
			style="position: absolute; top: 50px; left: 50px; height: 20px; width: 100px; background: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a1');"><b>指定用户发送</b> </a>
			</div>
		</div>

		<div
			style="position: absolute; top: 50px; height: 20px; left: 152px; width: 150px; background-color: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a2');"><b>按会员类型群发邮件</b> </a>
			</div>
		</div>

		<div
			style="position: absolute; top: 50px; height: 20px; left: 304px; width: 150px; background-color: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a3');"><b>按会员级别群发邮件</b> </a>
			</div>
		</div>
		<div id="a1"
			style="border: solid; border-width: 1px; position: absolute; display: block; height: 475px; top: 71px; left: 50px; width: 830px; background-color: #CCC">
			<table width="100%" border="0" align="center" cellpadding="2">
				<s:fielderror></s:fielderror>
				<s:form action="sendEmail" namespace="/office" id="sendEmailForm"
					theme="simple" onsubmit="submitonce(this);">
					<tr>
						<td width="70px" valign="middle" align="right">
							主 题：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="title" size="60" cssClass="input"
											cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="top"
							style="line-height: 20px;">
							<font color="#FF0000">*</font>邮件标题!
						</td>
					</tr>
					<tr>
						<td width="70px" valign="middle" align="right">
							用户账号：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="loginname" maxlength="20"
											onblur="loadInfo()" cssClass="input" cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="top"
							style="line-height: 20px;">
							输入登陆账号系统将自动查询出其Email
						</td>
					</tr>

					<s:if test="%{#session.operator.authority == 'boss'}">
						<tr>
							<td width="70px" valign="top" align="right">
								收件人：
							</td>
							<td align="left" valign="top">
								<table border="0">
									<tr>
										<td>
											<s:textarea name="email" cssClass="input" cols="50"
												cssStyle="height:120px;" rows="6"
												value="%{#request.user.email}" />
										</td>
									</tr>
								</table>
							</td>
							<td width="270px" align="left" valign="top"
								style="line-height: 20px">
								收件人(多个以,隔开 选择了用户类型此处可为空)如：A@qq.com,B@qq.com
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td width="70px" valign="top" align="right">
								收件人：
							</td>
							<td align="left" valign="top">
								<table border="0">
									<tr>
										<td>
											<s:textarea name="email" value="" cssClass="input" cols="50"
												cssStyle="height:120px;" rows="6" />
										</td>
									</tr>
								</table>
							</td>
							<td width="270px" align="left" valign="top"
								style="line-height: 20px">
								收件人(多个以,隔开 选择了用户类型此处可为空)如：A@qq.com,B@qq.com
							</td>
						</tr>
					</s:else>
					<tr>
						<td width="70px" valign="middle" align="right">
							抄送网址：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="urladdress" maxlength="20" cssClass="input"
											cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="middle"
							style="line-height: 20px;">
							要发送的网络地址系统会自动得到内容(可为空)
						</td>
					</tr>
					<tr>
						<td valign="top" align="center">
							邮件内容：
						</td>
						<td valign="top">
							<table border="0">
								<tr>
									<td>
										<s:textarea name="Msg" cssClass="input" cols="50" rows="6"
											cssStyle="height:120px;" />
									</td>
								</tr>
							</table>
						</td>
						<td align="left" valign="top" style="line-height: 20px">
							邮件内容(如抄送网址不为空的话此可为空)
						</td>
					</tr>
					<tr>
						<td height="44" colspan="2" align="center">
							<s:submit value=" 发 送" cssClass="button_orange" />
						</td>
						<td height="44" align="center">
							&nbsp;&nbsp;
						</td>
					</tr>
				</s:form>
			</table>
		</div>

		<div id="a2"
			style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
			<table width="100%" border="0" align="center" cellpadding="2">
				<s:fielderror></s:fielderror>
				<s:form action="sendEmailTwo" namespace="/office" id="sendEmailForm"
					theme="simple">
					<tr>
						<td width="70px" valign="middle" align="right">
							主 题：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="title" size="60" cssClass="input"
											cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="top"
							style="line-height: 20px;">
							<font color="#FF0000">*</font>邮件标题!
						</td>
					</tr>
					<tr>
						<td width="70px" valign="middle" align="right">
							用户类别：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:select list="%{#application.UserRole}" name="roleCode"
											listKey="code" listValue="text" cssClass="input"
											cssStyle="width:273px;height:28px;"
											onchange="alertMsg(this.value);"></s:select>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="top"
							style="line-height: 20px;">
							选择用户类型，系统将给所有此类用户发送邮件
						</td>
					</tr>
					<tr>
						<td width="70px" valign="middle" align="right">
							抄送网址：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="urladdress" maxlength="20" cssClass="input"
											cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="middle"
							style="line-height: 20px;">
							要发送的网络地址系统会自动得到内容(可为空)
						</td>
					</tr>
					<tr>
						<td valign="top" align="center">
							邮件内容：
						</td>
						<td valign="top">
							<table border="0">
								<tr>
									<td>
										<s:textarea name="Msg" cssClass="input" cols="50" rows="6"
											cssStyle="height:150px;" />
									</td>
								</tr>
							</table>
						</td>
						<td align="left" valign="top" style="line-height: 20px">
							邮件内容(如抄送网址不为空的话此可为空)
						</td>
					</tr>
					<tr>
						<td height="44" colspan="2" align="center">
							<%-- <s:submit value=" 发 送" cssClass="button_orange" /> --%>
						</td>
						<td height="44" align="center">
							&nbsp;&nbsp;
						</td>
					</tr>
				</s:form>
			</table>
		</div>

		<div id="a3"
			style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
			<table width="100%" border="0" align="center" cellpadding="2">
				<s:fielderror></s:fielderror>
				<s:form action="sendEmailTwo" namespace="/office" id="sendEmailForm"
					theme="simple">
					<tr>
						<td width="70px" valign="middle" align="right">
							主 题：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="title" size="60" cssClass="input"
											cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="top"
							style="line-height: 20px;">
							<font color="#FF0000">*</font>邮件标题!
						</td>
					</tr>
					<tr>
						<td width="70px" valign="middle" align="right">
							用户级别：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:select list="%{#application.VipLevel}" name="level"
											listKey="code" listValue="text" cssClass="input"
											cssStyle="width:273px;height:28px;"
											onchange="alertMsg(this.value);"></s:select>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="top"
							style="line-height: 20px;">
							选择用户级别，系统将给所有此类用户发送邮件
						</td>
					</tr>
					<tr>
						<td width="70px" valign="middle" align="right">
							抄送网址：
						</td>
						<td align="left" valign="middle">
							<table border="0">
								<tr>
									<td>
										<s:textfield name="urladdress" maxlength="20" cssClass="input"
											cssStyle="width:270px;"></s:textfield>
									</td>
								</tr>
							</table>
						</td>
						<td width="270px" align="left" valign="middle"
							style="line-height: 20px;">
							要发送的网络地址系统会自动得到内容(可为空)
						</td>
					</tr>
					<tr>
						<td valign="top" align="center">
							邮件内容：
						</td>
						<td valign="top">
							<table border="0">
								<tr>
									<td>
										<s:textarea name="Msg" cssClass="input" cols="50" rows="6"
											cssStyle="height:150px;" />
									</td>
								</tr>
							</table>
						</td>
						<td align="left" valign="top" style="line-height: 20px">
							邮件内容(如抄送网址不为空的话此可为空)
						</td>
					</tr>
					<tr>
						<td height="44" colspan="2" align="center">
							<%-- <s:submit value=" 发 送" cssClass="button_orange" /> --%>
						</td>
						<td height="44" align="center">
							&nbsp;&nbsp;
						</td>
					</tr>
				</s:form>
			</table>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>

