<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<s:head theme="xhtml" />
<title>短信平台</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery-1.7.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/kindeditor/kindeditor-all.js' />"></script>



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




</head>
<body style="background: #b6d9e4; font-size: 12px">
	<div id="excel_menu_left">
		其它 -->外部名单群发邮件平台 <a href="javascript:history.back();"><font
			color="red">上一步</font> </a>
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



	<div
		style="position: absolute; top: 50px; height: 20px; left: 456px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a4');"><b>按代理账户群发邮件</b> </a>
		</div>
	</div>
	<div
		style="position: absolute; top: 50px; height: 20px; left: 608px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a5');"><b>代理推荐码</b> </a>
		</div>
	</div>
	<div
		style="position: absolute; top: 50px; height: 20px; left: 760px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a6');"><b>客服推荐码</b> </a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 910px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a7');"><b>按存款情况群发邮件</b> </a>
		</div>
	</div>



	<div id="a1"
		style="border: solid; border-width: 1px; position: absolute; display: block; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">用户账号：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="loginname" maxlength="20" id="assignUser" onblur="loadInfo()" cssClass="input" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">输入登陆账号系统将自动查询出其Email</td>
			</tr>
			<tr>
				<td width="70px" valign="top" align="right">收件人：</td>
				<td align="left" valign="top">
					<table border="0">
						<tr>
							<td><s:textarea name="email" cssClass="input" cols="50" id="usersMail" cssStyle="height:120px;" rows="6" /></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px">收件人(多个以,或者 ;隔开选择了用户类型此处可为空)如：A@qq.com,B@qq.com</td>
			</tr>

			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="subject" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>
							<td>
								<div id="div1">
									<textarea  name="content" id="kindEditorEmail" style="width: 600px; height: 150px;"></textarea>
								</div>
							</td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容：如果选择模板邮件，模板中的内容如需修改，则只用修改文字部分，代码部分不需要修改，如果是自己编写邮件则没有任何限制！
				</td>
			</tr>
			<tr><s:submit value="发 送" id="sendMail" onclick="sendEmails()" cssClass="button_orange" />
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>


	<div id="a2" style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">用户类别：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="%{#application.UserRole}" name="roleCode" id='roleCode_id' listKey="code" listValue="text" cssClass="input" cssStyle="width:273px;height:28px;"
									onchange="alertMsg(this.value);"></s:select>
						    </td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">选择用户类型，系统将给所有此类用户发送邮件</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="#{'-1':'',0:'有存款',1:'没存款'}" name="isMoney" id='isMoneyRole' cssClass="input" cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！ (没选择 为 发送  有和没存款都发送)</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="startTime" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTime" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="subject1" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>
							<td><s:textarea value="" id="kindEditorRole"   style="width: 600px; height: 150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
			   <s:submit value="发 送" onclick="sendYiYeEmailRole()" cssClass="button_orange" />
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>

	<div id="a3"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">用户级别：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="%{#application.VipLevel}" name="level" id='leavel_id' listKey="code" listValue="text" cssClass="input"
									cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">选择用户级别，系统将给所有此类用户发送邮件</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="#{'-1':'',0:'有存款',1:'没存款'}" name="isMoney" id='isMoneyLevel'  cssClass="input" cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！ (没选择 为 发送  有和没存款都发送)</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="startTimeLevel" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTimeLevel" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="titleLevel" size="60" cssClass="input" id='subjectLevel' cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>
							<td><s:textarea  value="" id="kindEditorLevel"  style="width: 600px; height: 150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
			     <s:submit value=" 发 送" onclick="sendYiYeEmailsLevel()" cssClass="button_orange" />
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>



	<div id="a4"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">代理账号：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="agent" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">录入代理账号，系统将给该代理下的所有玩家都发送短信。</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="#{'-1':'',0:'有存款',1:'没存款'}" name="isMoney" id='isMoneyAgent'  cssClass="input" cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！ (没选择 为 发送  有和没存款都发送)</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="startTimeAgent" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTimeAgent" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="subjectAgent" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>
							<td><s:textarea value=""   id='kindEditorAgent' style="width: 600px; height: 150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
			    <s:submit value=" 发 送" onclick="sendYiYeEmailAgent()" cssClass="button_orange" />
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>



	<div id="a5"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">推荐码：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="partner" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">录入代理推荐，将给该推荐码下的所有玩家发送邮件！</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="#{'-1':'',0:'有存款',1:'没存款'}" name="isMoney" id='isMoneyPartner'  cssClass="input" cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！ (没选择 为 发送  有和没存款都发送)</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="startTimePartner" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTimePartner" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="subjectaPartner" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top" style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>
						    <td><s:textarea value=""   id='kindEditorPartner' style="width: 600px; height: 150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
			    <s:submit value="发 送" onclick="sendEmailPartner()" cssClass="button_orange" /> 
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>
	
	<div id="a6"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">推荐码：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="intro" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top" style="line-height: 20px;">录入客服推荐码，将给该推荐码下的所有玩家发送邮件！</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="#{'-1':'',0:'有存款',1:'没存款'}" name="isMoney" id='isMoneyIntro' cssClass="input" cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！ (没选择 为 发送  有和没存款都发送)</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="startTimeIntro" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTimeIntro" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id="subjectaIntro" cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>
							<td><s:textarea value=""   id='kindEditorIntro' style="width: 600px; height: 150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
			    <s:submit value="发 送" onclick="sendEmailIntro()" cssClass="button_orange" />
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>



	<div id="a7"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 71px; left: 50px; width: 880px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="#{'-1':'',0:'有存款',1:'没存款'}" name="isMoney" id='isMoney'  cssClass="input" cssStyle="width:273px;height:28px;" onchange="alertMsg(this.value);"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！ (没选择 为 发送  有和没存款都发送)</td>
			</tr>

			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="startTimeSaving" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTimeSaving" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input" id='subjectSaving' cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;"><font color="#FF0000">*</font>邮件标题!
				</td>
			</tr>
			<tr>
				<td valign="top" align="center">邮件内容：</td>
				<td valign="top">
					<table border="0">
						<tr>				
						    <td><s:textarea value=""   id='kindEditorSaving' style="width: 600px; height: 150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
			    <s:submit value="发 送" onclick="sendEmailSaving()" cssClass="button_orange" />
				<td height="44" colspan="2" align="center"></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>

	<c:import url="/office/script.jsp" />

	<script type="text/javascript">
		function changeValue() {
			var a = $("#sltList").val();
			if (a == 0) {
				$('#input').val("请输入邮件标题");
				$('#kindEditor').val("请输入邮件内容！");
				return;
			}
			$.ajax({
				url : "/office/getEmailDetilById.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型
				data : "newSletterId=" + a,
				async : false, // 异步
				success : function(msg) {
					var vars = msg.split("#######");
					$('#input').val(vars[1]);
					$('#kindEditor').val(vars[0]);
				},
			});
		}
		
		function loadInfo() {
			var username = $("#assignUser").val();

			if (username == null || username == "") {
				return;
			}

			$.ajax({
				url : "/office/getUserInfoForMail.do",
				type : "post", // 请求方式
				data : {
					"loginname" : username
				},
				dataType : "text", // 响应的数据类型
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "0") {
						$("#usersMail").val(json.obj);
					} else {
						alert(json.errormsg);
					}
				},
			});
		}

		//发送邮件
		function sendEmails() {
			var usersmail = $("#usersMail").val();//收件人
			var subject = $("#subject").val();
			var content = $("#kindEditorEmail").val();//邮件内容

			if (usersmail == ""||usersmail==null) {
				alert('请输入用户账号或者收件人！');
				return;
			}

			usersmail = usersmail.replace(/\ +/g, "");//去掉空格
			usersmail = usersmail.replace(/[ ]/g, ""); //去掉空格
			usersmail = usersmail.replace(/[\r\n]/g, "");//去掉回车换行
			usersmail = usersmail.replace(/，/ig, ',');

			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型
				data : "email=" + usersmail + "&content=" + content + "&subject=" + subject+"&id=1",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
						$("#assignUser").val("");
					    $("#usersMail").val(""); //清空
// 						$("#subject").val("");
// 						$("#kindEditorEmail").val("");
// 						KindEditor.instances[0].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});
		}

		function checkdate(start, end) {
			if (null == start || null == end) {
				alert("时间选项不能为空");
				return 0;
			}
			return 1;
			if (end <= start) {
				alert("结束时间需要大于开始时间");
				return 0;
			}
			var newStart = new Date(start);
			var newEnd = new Date(end);
			if ((newEnd.getTime() - newStart.getTime() > 15 * 24 * 60 * 60
					* 1000)) {
				alert("结束时间和开始时间相差不能超过15天");
				return 0;
			}
			return 1;
		}

		//发送邮件0 类型
		function sendYiYeEmailRole() {
			var subject = $("#subject1").val();
			var isMoney = $("#isMoneyRole").val();
			var content = $("#kindEditorRole").val();//邮件内容
			var roleCode = $("#roleCode_id").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var timeOk = checkdate(startTime, endTime);
			
			if (timeOk == 0) {
				return;
			}

			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型
				data : "subject=" + subject + "&content=" + content + "&roleCode=" + roleCode+ "&startTime=" + startTime + "&endTime=" + endTime+ "&isMoney=" + isMoney+"&id=2",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
// 						$("#subject1").val("");
// 						$("#kindEditor1").val("");
// 						KindEditor.instances[1].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});

		}

		function sendYiYeEmailsLevel() {
			
			var subject = $("#subjectLevel").val();
			var isMoney = $("#isMoneyLevel").val();
			var content = $("#kindEditorLevel").val();//邮件内容
			var level = $("#leavel_id").val();
			var startTime = $("#startTimeLevel").val();
			var endTime = $("#endTimeLevel").val();
			var timeOk = checkdate(startTime, endTime);
			if (timeOk == 0) {
				return;
			}

			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
            
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型
				data : "subject=" + subject + "&content=" + content + "&level=" + level+ "&startTime=" + startTime + "&endTime=" + endTime+ "&isMoney=" + isMoney+"&id=3",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
// 						$("#subjectLevel").val("");
// 						$("#kindEditorLevel").val("");
// 						KindEditor.instances[2].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});


		}

		//
		function sendYiYeEmailAgent() {
			var agentValue = $("#agent").val();
			var isMoney = $("#isMoneyAgent").val();
			var content = $("#kindEditorAgent").val();//邮件内容
			var subject = $("#subjectAgent").val();
			var startTime = $("#startTimeAgent").val();
			var endTime = $("#endTimeAgent").val();
			var timeOk = checkdate(startTime, endTime);
			if (timeOk == 0) {
				return;
			}
		
			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
			
			if (null == agentValue || agentValue == ""|| agentValue == "请输入代理账号") {
				alert('请输入代理账号');
				return;
			}
		
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型 
				data : "subject=" + subject + "&content=" + content + "&agentValue=" + agentValue+ "&startTime=" + startTime + "&endTime=" + endTime+ "&isMoney=" + isMoney +"&id=4",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
						$("#agent").val("");
// 						 $("#kindEditorAgent").val("");//邮件内容
// 						$("#subjectAgent").val("");
// 						KindEditor.instances[3].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});

		}

		//发送根据代理推荐码
		function sendEmailPartner() {
			
			var partner = $("#partner").val();
			var isMoney = $("#isMoneyPartner").val();
			var content = $("#kindEditorPartner").val();//邮件内容
			var subject = $("#subjectaPartner").val();
			var startTime = $("#startTimePartner").val();
			var endTime = $("#endTimePartner").val();
			var timeOk = checkdate(startTime, endTime);
			if (timeOk == 0) {
				return;
			}
		
			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
			
			if (partner == null || partner == ""|| partner == "请输入推荐码") {
				alert('请输入代理账号');
				return;
			}
		
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型 
				data : "subject=" + subject + "&content=" + content + "&partner=" + partner+ "&startTime=" + startTime + "&endTime=" + endTime+ "&isMoney=" + isMoney +"&id=5",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
						$("#intro").val("");
// 						 $("#kindEditorPartner").val("");//邮件内容
// 						$("#subjectaPartner").val("");
// 						KindEditor.instances[4].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});

		}
		
		//发送根据推荐码
		function sendEmailIntro() {
			
			var intro = $("#intro").val();
			var isMoney = $("#isMoneyIntro").val();
			var content = $("#kindEditorIntro").val();//邮件内容
			var subject = $("#subjectaIntro").val();
			var startTime = $("#startTimeIntro").val();
			var endTime = $("#endTimeIntro").val();
			var timeOk = checkdate(startTime, endTime);
			if (timeOk == 0) {
				return;
			}
		
			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
			
			if (null == intro || intro == ""|| intro == "请输入推荐码") {
				alert('请输入代理账号');
				return;
			}
		
			
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型 
				data : "subject=" + subject + "&content=" + content + "&introValue=" + intro+ "&startTime=" + startTime + "&endTime=" + endTime+ "&isMoney=" + isMoney +"&id=6",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
						$("#intro").val("");
// 						 $("#kindEditorIntro").val("");//邮件内容
// 						$("#subjectIntro").val("");
// 						KindEditor.instances[5].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});

		}

		//发送邮件 按存款情况群发邮件
		function sendEmailSaving() {

			var isMoney = $("#isMoney").val();
			var content = $("#kindEditorSaving").val();//邮件内容
			var subject = $("#subjectSaving").val();
			var startTime = $("#startTimeSaving").val();
			var endTime = $("#endTimeSaving").val();
			var timeOk = checkdate(startTime, endTime);
			
			if (timeOk == 0) {
				return;
			}
		
			if (subject == null || subject == "") {
				alert('请输入邮件主题');
				return;
			}
			if (content == null || content == "") {
				alert('请输入邮件内容！');
				return;
			}
		
			content = content.replace(/%/g, "%25");
			content = content.replace(/\&/g, "%26");
			content = content.replace(/\+/g, "%2B");
			
			$.ajax({
				url : "/office/sendYiYeEmailExternal.do",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型 
				data : "subject=" + subject + "&content=" + content + "&isMoney=" + isMoney+ "&startTime=" + startTime + "&endTime=" + endTime +"&id=7",
				async : false, // 异步
				success : function(data) {
					var json=JSON.parse(data);
					if (json.success == "200") {
// 						 $("#kindEditorSaving").val("");//邮件内容
// 						$("#subjectSaving").val("");
// 						KindEditor.instances[6].html("");
						 alert(json.msg1);
					} else {
						alert(json.errormsg);
					}
				},
			});

		}

		function showAndHidden(eid) {
			if (eid == "a1") {
				document.getElementById("a1").style.display = "block";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "none";
				document.getElementById("a5").style.display = "none";
				document.getElementById("a6").style.display = "none";
				document.getElementById("a7").style.display = "none";
			} else if (eid == "a2") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "block";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "none";
				document.getElementById("a5").style.display = "none";
				document.getElementById("a6").style.display = "none";
				document.getElementById("a7").style.display = "none";
			} else if (eid == "a3") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "block";
				document.getElementById("a4").style.display = "none";
				document.getElementById("a5").style.display = "none";
				document.getElementById("a6").style.display = "none";
				document.getElementById("a7").style.display = "none";
			} else if (eid == "a4") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "block";
				document.getElementById("a5").style.display = "none";
				document.getElementById("a6").style.display = "none";
				document.getElementById("a7").style.display = "none";
			} else if (eid == "a5") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "none";
				document.getElementById("a5").style.display = "block";
				document.getElementById("a6").style.display = "none";
				document.getElementById("a7").style.display = "none";
			} else if (eid == "a6") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "none";
				document.getElementById("a5").style.display = "none";
				document.getElementById("a6").style.display = "block";
				document.getElementById("a7").style.display = "none";
			}else if (eid == "a7") {
			   document.getElementById("a1").style.display = "none";
			   document.getElementById("a2").style.display = "none";
			   document.getElementById("a3").style.display = "none";
			   document.getElementById("a4").style.display = "none";
			   document.getElementById("a5").style.display = "none";
			   document.getElementById("a6").style.display = "none";
			   document.getElementById("a7").style.display = "block";
		  }
		}
		
		
		

		function alertMsg(a) {
			if (a) {
				alert("选择群发用户，请慎重选择！！！");
			}
		}
		
		
	</script>
	
	<script type="text/javascript">  
        
	  KindEditor.ready(function(K) { 
     	 K.create('#kindEditorEmail', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } });  
     	 K.create('#kindEditorRole', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } });
     	 K.create('#kindEditorLevel', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } });  
     	 K.create('#kindEditorAgent', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } }); 
     	 K.create('#kindEditorPartner', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } });  
     	 K.create('#kindEditorIntro', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } });  
     	 K.create('#kindEditorSaving', {  afterCreate : function() { this.sync(); },  afterBlur:function(){ this.sync();  } });  

     });  
       
   </script>  
	

</body>

</html>

