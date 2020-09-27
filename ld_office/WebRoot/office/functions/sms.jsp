<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
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
<script type="text/javascript"
	src="<c:url value='/js/jquery-1.7.2.min.js' />"></script>
<script type="text/javascript" language="javascript">
	var APILength = new Array();
	APILength[1] = 62;
	APILength[2] = 56;
	APILength[3] = 50;
	APILength[4] = 56;

	var maxlen = 63; // 短信内容最大长度
	function smsCount(frm, maxlimit) {

		var value = frm.Msg.value;
		var msgSize = 0; // 当前短信内容长度
		var newMsg = "";
		var i = 0;
		for (; i < value.length; i++) {
			var code = value.charCodeAt(i);
			if (msgSize >= maxlen) {
				break;
			} else {
					msgSize++;
			}

		}

		frm.chrLen.value = msgSize;
		if (i < value.length) {
			frm.Msg.value = value.substring(0, i);
			alert("短信内容不能超过63个字符");
		}

	}

	function changeAPIType(type) {
		maxlen = APILength[type];
		document.getElementById("MaxLen").innerHTML = maxlen;
	}

	function loadInfo() {
		var frm = document.getElementById("sendForm");
		frm.action = "<c:url value='/office/getUserPhone.do' />";
		frm.submit();
	}

	function getstencilvalue() {
		var obj = document.getElementById('contentstencil');
		var index = obj.selectedIndex; //序号，取当前选中选项的序号
		var val = obj.options[index].value;
		document.getElementById("Msg").value = val;
	}

	function showAndHidden(eid) {
		if (eid == "a1") {
			document.getElementById("a1").style.display = "block";
			document.getElementById("a2").style.display = "none";
			document.getElementById("a3").style.display = "none";
			document.getElementById("a4").style.display = "none";
			document.getElementById("a5").style.display = "none";
			document.getElementById("a6").style.display = "none";
		} else if (eid == "a2") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "block";
			document.getElementById("a3").style.display = "none";
			document.getElementById("a4").style.display = "none";
			document.getElementById("a5").style.display = "none";
			document.getElementById("a6").style.display = "none";
		} else if (eid == "a3") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "none";
			document.getElementById("a3").style.display = "block";
			document.getElementById("a4").style.display = "none";
			document.getElementById("a5").style.display = "none";
			document.getElementById("a6").style.display = "none";
		} else if (eid == "a4") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "none";
			document.getElementById("a3").style.display = "none";
			document.getElementById("a4").style.display = "block";
			document.getElementById("a5").style.display = "none";
			document.getElementById("a6").style.display = "none";
		} else if (eid == "a5") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "none";
			document.getElementById("a3").style.display = "none";
			document.getElementById("a4").style.display = "none";
			document.getElementById("a5").style.display = "block";
			document.getElementById("a6").style.display = "none";
		}else if (eid == "a6") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "none";
			document.getElementById("a3").style.display = "none";
			document.getElementById("a4").style.display = "none";
			document.getElementById("a5").style.display = "none";
			document.getElementById("a6").style.display = "block";
		}

	}

	function showAndHiddenTip() {
		var s = "请输入短信内容，字符长度63";
		// sendListForm
		var frm = document.getElementById('sendListForm');
		if (frm.Msg.value == s) {
			frm.Msg.value = "";
		} else if (frm.Msg.value.length == 0) {
			frm.Msg.value = s;
		}
	}

	function showAndHiddenTipByLevel() {
		var s = "请输入短信内容，字符长度63";
		// sendListForm
		var frm = document.getElementById('sendListFormByLevel');
		if (frm.Msg.value == s) {
			frm.Msg.value = "";
		} else if (frm.Msg.value.length == 0) {
			frm.Msg.value = s;
		}
	}

	function calculateCount() {
		var leng = sendSms.PhoneNum.value.split(',').length;
		sendSms.smsLen.value = leng + "";
	}

	function sendMsg() {
		var frm = document.getElementById("sendListForm");
		var sdate = dojo.widget.byId("startdate").getValue();
		var edate = dojo.widget.byId("enddate").getValue();

		frm.action = "<c:url value='/office/sendSmsList.do' />?start=" + sdate
				+ "&end=" + edate;
		frm.submit();
	}
	function sendMsgByLevel() {
		var frm = document.getElementById("sendListFormByLevel");
		var sdate = dojo.widget.byId("startdate").getValue();
		var edate = dojo.widget.byId("enddate").getValue();

		frm.action = "<c:url value='/office/sendSmsListByLevel.do' />?start="
				+ sdate + "&end=" + edate;
		frm.submit();
	}

	//发送邮件
	function sendsms6() {
		var isMoney = $("#isMoney").val();//是否有存款
		var start6 = $("#start6").val();//开始时间
		var end6 = $("#end6").val();//结束时间
		var message6 = $("#message6").val();//短信内容
		if (null == message6 || message6 == "") {
			alert('请输入短信内容！');
			return;
		}
		message6 = message6.replace(/%/g, "%25");
		message6 = message6.replace(/\&/g, "%26");
		message6 = message6.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendSmsList45.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			data : [ {
				"name" : "isMoney",
				"value" : isMoney
			}, {
				"name" : "start",
				"value" : start6
			}, {
				"name" : "end",
				"value" : end6
			},{
				"name" : "fslb",
				"value" : '6'
			}, {
				"name" : "Msg",
				"value" : message6
			} ],
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}
	
	//发送邮件
	function sendsms5() {
		var agentValue = $("#input5").val();//是否有存款
		var start6 = $("#start5").val();//开始时间
		var end6 = $("#end5").val();//结束时间
		var message6 = $("#message5").val();//短信内容
		if (null == message6 || message6 == "") {
			alert('请输入短信内容！');
			return;
		}
		message6 = message6.replace(/%/g, "%25");
		message6 = message6.replace(/\&/g, "%26");
		message6 = message6.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendSmsList45.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			data : [ {
				"name" : "agentValue",
				"value" : agentValue
			}, {
				"name" : "start",
				"value" : start6
			}, {
				"name" : "end",
				"value" : end6
			},{
				"name" : "fslb",
				"value" : '5'
			}, {
				"name" : "Msg",
				"value" : message6
			} ],
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}
	
	//发送邮件
	function sendsms4() {
		var introValue = $("#input4").val();//是否有存款
		var start6 = $("#start4").val();//开始时间
		var end6 = $("#end4").val();//结束时间
		var message6 = $("#message4").val();//短信内容
		if (null == message6 || message6 == "") {
			alert('请输入短信内容！');
			return;
		}
		message6 = message6.replace(/%/g, "%25");
		message6 = message6.replace(/\&/g, "%26");
		message6 = message6.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendSmsList45.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			data : [ {
				"name" : "introValue",
				"value" : introValue
			}, {
				"name" : "start",
				"value" : start6
			}, {
				"name" : "end",
				"value" : end6
			},{
				"name" : "fslb",
				"value" : '4'
			}, {
				"name" : "Msg",
				"value" : message6
			} ],
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}
</script>

</head>
<body style="background: #b6d9e4; font-size: 12px">
	<div id="excel_menu_left">
		其他--&gt; 短信平台 <a href="javascript:history.back();"><font
			color="red">上一步</font> </a>
	</div>



	<div
		style="position: absolute; top: 50px; left: 50px; height: 20px; width: 100px; background: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a1');"><b>指定用户发送</b></a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 152px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a2');"><b>按会员类型群发短信</b></a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 304px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a3');"><b>按会员级别群发短信</b></a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 456px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a4');"><b>按推荐码/代码群发短信</b></a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 610px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a5');"><b>按代理账户群发短信</b></a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 763px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a6');"><b>按有无存款群发短信</b></a>
		</div>
	</div>

	<div id="a1"
		style="border: solid; border-width: 1px; position: absolute; display: block; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<s:form action="sendSms" onsubmit="submitonce(this);"
				namespace="/office" id="sendForm" theme="simple"> -->

					<tr>
					<td width="70px" valign="middle" align="center">用户账号：</td>
					<td align="left" width="500px" valign="top">
						<table border="0">
							<tr>
								<%-- <td valign="middle"><s:textfield name="loginname"
										maxlength="20" onblur="loadInfo()"></s:textfield></td> --%>
										<s:textarea id="usernames" name="usernames" 
												value="" cssClass="formstyle" cols="55"
												rows="6" />
							</tr>
						</table>
					</td>
					<td width="230px" align="left" valign="middle"
						style="line-height: 20px; color: red;">输入用户的登陆账号系统将查询出其电话号码</td>
				</tr>
				<tr>
					<td width="70px" valign="top" align="center">接收号码：</td>
					<td align="left" width="500px" valign="top">
						<table border="0">
							<tr>
								<td valign="middle"><s:if
										test="%{#session.operator.authority == 'boss'}">
										<s:textarea id="textphone" name="PhoneNum"
											value="%{#request.user.phone}" cssClass="formstyle" cols="55"
											rows="6" onblur="calculateCount()" />
									</s:if> <s:else>
										<s:textarea id="textphone" name="PhoneNum" value=""
											cssClass="formstyle" cols="55" rows="6"
											onblur="calculateCount()" />
									</s:else></td>
							</tr>
						</table>
					</td>
					<td width="230px" align="left" valign="middle"
						style="line-height: 20px">
						发送多个手机号码请用逗号“,”分开，如：13700000000,13900000000(选择了用户类型此处可为空)</td>
				</tr>
				<tr>
					<td valign="top" align="center"><br /> <br /> 短信内容：</td>
					<td valign="middle">
						<table border="0">
							<tr>
								<td valign="middle">
									<div>
										您已写了 <input readonly value="0" type="text" name="chrLen"
											id="chrLen" class="formstyle" size=3 maxlength=3 /> 个字符，共 <input
											readonly value="0" type="text" name="smsLen"
											class="formstyle" size=3 maxlength=3 /> 条短信
									</div>
									<div>
										<s:textarea name="Msg" id="Msg" wrap="physical"
											cssClass="formstyle" cols="55" rows="6"
											onKeyUp="smsCount(this.form,1);this.pos=document.selection.createRange();"
											onChange="smsCount(this.form,1);"
											onSelect="this.pos=document.selection.createRange();"
											onClick="this.pos=document.selection.createRange();" />
									</div>
								</td>
							</tr>
						</table>
					</td>
					<td align="left" valign="middle" style="line-height: 20px">
						&nbsp; <font color="#FF0000">*</font>每条短信字数与所选通道有关，一次输入累计不能超过3条。
					</td>
				</tr>
				<tr>
					<td height="44" colspan="2" align="center"><s:submit
							value=" 发 送" cssClass="button_orange" /></td>
					<td height="44" align="center">&nbsp;&nbsp;</td>
				</tr>

			</s:form>
		</table>
	</div>

	<div id="a2"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">

		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<s:form action="sendSmsList" id="sendListForm" namespace="/office"
				theme="simple">
				<tr>
					<td height="15px">&nbsp;</td>
				</tr>
				<tr>
					<td width="80px">会员类型:</td>
					<td width="160px"><s:select name="type" cssStyle="width:150px"
							list="#application.UserRole" listKey="code" listValue="text"></s:select>
					</td>
					<td width="60px">时间范围:</td>
					<td colspan="3"><s:textfield name="start" size="18"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							value="%{startTime}" cssClass="Wdate" /> &nbsp;&nbsp;至&nbsp; <s:textfield
							name="end" size="18"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							value="%{endTime}" cssClass="Wdate" /></td>
				</tr>
				<tr>
					<td colspan="4"><s:textarea onchange="smsCount(this.form,1)"
							onkeyup="smsCount(this.form,1)" onblur="showAndHiddenTip()"
							onfocus="showAndHiddenTip()" name="Msg" id="Msg" cols="85"
							rows="16"></s:textarea></td>
				</tr>
				<tr>
					<td colspan="4" height="10px"></td>
				</tr>
				<tr>
					<td colspan="2">您已写了<input readonly value="0" type="text"
						name="chrLen" id="chrLen" class="formstyle" size=3 maxlength=3 />个字符
					</td>
					<td colspan="2" align="left"><s:submit value="发送短信" /></td>
				</tr>
			</s:form>
		</table>

	</div>

	<div id="a3"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">

		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<s:form action="sendSmsListByLevel" id="sendListFormByLevel"
				namespace="/office" theme="simple">
				<tr>
					<td height="15px">&nbsp;</td>
				</tr>

				<td width="80px">会员级别:</td>
				<td width="160px"><s:select name="level" cssStyle="width:150px"
						list="#application.VipLevel" listKey="code" listValue="text"></s:select>
				</td>
				<td width="60px">时间范围:</td>
				<td colspan="3"><s:textfield name="start" size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{startTime}" cssClass="Wdate" /> &nbsp;&nbsp;至&nbsp; <s:textfield
						name="end" size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{endTime}" cssClass="Wdate" /></td>
				</tr>
				<tr>
					<td colspan="4"><s:textarea onchange="smsCount(this.form,1)"
							onkeyup="smsCount(this.form,1)"  name="Msg" id="Msg" cols="85"
							rows="16"></s:textarea></td>
				</tr>
				<tr>
					<td colspan="4" height="10px"></td>
				</tr>
				<tr>
					<td colspan="2">您已写了<input readonly value="0" type="text"
						name="chrLen" id="chrLen" class="formstyle" size=3 maxlength=3 />个字符
					</td>
					<td colspan="2" align="left"><s:submit value="发送短信" /></td>
				</tr>
			</s:form>
		</table>

	</div>


	<div id="a4"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">

		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<s:form 
				theme="simple">
				<tr>
					<td height="15px">&nbsp;</td>
				</tr>

				<td width="80px">推荐码/代码:</td>
				<td width="160px"><s:textfield name="introValue" size="60"
						cssClass="input" id='input4' cssStyle="width:140px;"></s:textfield>
					<input name="fslb" style="display: none;" value="4" /></td>
				<td width="60px">时间范围:</td>
				<td colspan="3"><s:textfield name="start" id='start4' size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{startTime}" cssClass="Wdate" /> &nbsp;&nbsp;至&nbsp; <s:textfield
						name="end" size="18" id='end4'
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{endTime}" cssClass="Wdate" /></td>
				</tr>
				<tr>
					<td colspan="4"><s:textarea onchange="smsCount(this.form,1)"
							onkeyup="smsCount(this.form,1)" onblur="showAndHiddenTip()"
							onfocus="showAndHiddenTip()" name="Msg" id="message4" cols="85"
							rows="16"></s:textarea></td>
				</tr>
				<tr>
					<td colspan="4" height="10px"></td>
				</tr>
				<tr>
					<td colspan="2">您已写了<input readonly value="0" type="text"
						name="chrLen" id="chrLen4" class="formstyle" size=3 maxlength=3 />个字符
					</td>
			</s:form>
			<td colspan="2" align="left"><button 
							onclick="sendsms4()">发送</button></td>
				</tr>
		</table>

	</div>


	<div id="a5"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">

		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<s:form 
				theme="simple">
				<tr>
					<td height="15px">&nbsp;</td>
				</tr>

				<td width="80px">代理账户:</td>
				<td width="160px"><s:textfield name="agentValue" size="60"
						cssClass="input" id='input5' cssStyle="width:140px;"></s:textfield>
					<input name="fslb" style="display: none;" value="5" /></td>
				<td width="60px">时间范围:</td>
				<td colspan="3"><s:textfield name="start" id='start5' size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{startTime}" cssClass="Wdate" /> &nbsp;&nbsp;至&nbsp; <s:textfield
						name="end" size="18" id='end5'
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{endTime}" cssClass="Wdate" /></td>
				</tr>
				<tr>
					<td colspan="4"><s:textarea onchange="smsCount(this.form,1)"
							onkeyup="smsCount(this.form,1)"  name="Msg" id="message5" cols="85"
							rows="16"></s:textarea></td>
				</tr>
				<tr>
					<td colspan="4" height="10px"></td>
				</tr>
				<tr>
					<td colspan="2">您已写了<input readonly value="0" type="text"
						name="chrLen" id="chrLen5" class="formstyle" size=3 maxlength=3 />个字符
					</td>
			</s:form>
			<td colspan="2" align="left"><button 
							onclick="sendsms5()">发送</button></td>
				</tr>
		</table>

	</div>


	<div id="a6"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">

		<table border="0" cellpadding="0" cellspacing="0" align="center">
			<s:form 	theme="simple">
				<tr>
					<td height="15px">&nbsp;</td>
				</tr>

				<td width="80px">有无存款:</td>
				<td width="100px"><s:select list="{'有存款','无存款'}" name="level"
						id='isMoney' cssClass="input" cssStyle="width:180px;height:28px;"></s:select>
				</td>
				<td width="60px">时间范围:</td>
				<td colspan="3"><s:textfield name="start" id='start6' size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{startTime}" cssClass="Wdate" /> &nbsp;&nbsp;至&nbsp; <s:textfield
						name="end" id='end6' size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						value="%{endTime}" cssClass="Wdate" /></td>
				</tr>
				<tr>
					<td colspan="4"><s:textarea id='message6' onkeyup="smsCount(this.form,1)" name="Msg" 
				onchange="smsCount(this.form,1)"  cols="85" rows="16"></s:textarea></td>
				</tr>
				<tr>
					<td colspan="4" height="10px"></td>
				</tr>
				<tr>
					<td colspan="2">您已写了<input readonly value="0" type="text" 
						name="chrLen" id="chrLen6" class="formstyle" size=3 maxlength=3 />个字符
					</td>
				</s:form>
					<td colspan="2" align="left"><button 
							onclick="sendsms6()">发送</button></td>
				</tr>
				
		</table>
	</div>
	

	<c:import url="/office/script.jsp" />
</body>
</html>

