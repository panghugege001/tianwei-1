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
<sx:head parseContent="true" />
<title>最新邮件平台</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery-1.7.2.min.js' />"></script>
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
	/* $(document).ready(function() {
		$.ajax({
			url : "/office/getEmailList.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			async : false, // 异步
			success : function(msg) {
				if(msg=='wrong'){
					$(msg).append("从邮件服务器获取邮件列表失败，如需列表，请刷新页面！");
					alert(msg);
					return;
				}
				$(msg).appendTo("#sltList");
			},
		});
	}); */
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

	function alertMsg(a) {
		if (a) {
			alert("选择群发用户，请慎重选择！！！");
		}
	}
	//发送邮件
	function sendEmails() {
		var sjrs = '';
		var sjr = $("#sjr").val();//收件人
		var sjr1 = $("#sjr1").val();//收件人1
		var yhzh = $("#yhzh").val();//用户账号
		var ycemail = $("#ycemail").val();
		if (null == sjr || sjr == "") {
			if (null == sjr1 || sjr1 == "") {
				if (null == ycemail || ycemail == "") {
					alert('请输入用户账号或者收件人！');
					return;
				} else {
					sjrs = ycemail;
				}
			} else {
				sjrs = sjr1;
			}
		} else {
			sjrs = sjr;
		}
		sjrs = sjrs.replace(/\ +/g, "");//去掉空格
		sjrs = sjrs.replace(/[ ]/g, ""); //去掉空格
		sjrs = sjrs.replace(/[\r\n]/g, "");//去掉回车换行
		sjrs = sjrs.replace(/，/ig, ',');
		//var mbID=$("#sltList").val();//模板ID
		var mbID = '0';
		var zt = $("#input").val();//主题
		if (null == zt || zt == "" || zt == "请输入邮件标题") {
			alert('请输入邮件主题');
			return;
		}
		var yjnr = $("#kindEditor").val();//邮件内容
		if (null == yjnr || yjnr == "" || yjnr == "请输入邮件内容！") {
			alert('请输入邮件内容！');
			return;
		}
		if (((yjnr.indexOf("<html>") > -1) || (yjnr.indexOf("<head>") > -1) || (yjnr
				.indexOf("<body>" > -1))) == 'false') {
			yjnr = yjnr.replace(/[\r\n]/g, "");
		}
		yjnr = yjnr.replace(/%/g, "%25");
		yjnr = yjnr.replace(/\&/g, "%26");
		yjnr = yjnr.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendEmails.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			data : "yjnr=" + yjnr + "&newSletterId=" + mbID + "&sjr=" + sjrs
					+ "&zt=" + zt,
			async : false, // 异步
			success : function(msg) {
				alert(msg);
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
		if ((newEnd.getTime() - newStart.getTime() > 15 * 24 * 60 * 60 * 1000)) {
			alert("结束时间和开始时间相差不能超过15天");
			return 0;
		}
		return 1;
	}

	//发送邮件0 类型
	function sendEmails1() {
		var mbID = '0';
		var zt = $("#input1").val();//主题
		var fslb = "0";
		var roleCode = $("#roleCode_id").val();
		var beginTime = $("#beginTime2").val();
		var endTime = $("#endTime2").val();
		var timeOk = checkdate(beginTime, endTime);
		if (timeOk == 0) {
			return;
		}
		if (null == zt || zt == "" || zt == "请输入邮件标题") {
			alert('请输入邮件主题');
			return;
		}
		var yjnr = $("#kindEditor1").val();//邮件内容
		if (null == yjnr || yjnr == "" || yjnr == "请输入邮件内容！") {
			alert('请输入邮件内容！');
			return;
		}
		if (((yjnr.indexOf("<html>") > -1) || (yjnr.indexOf("<head>") > -1) || (yjnr
				.indexOf("<body>" > -1))) == 'false') {
			yjnr = yjnr.replace(/[\r\n]/g, "");
		}
		yjnr = yjnr.replace(/%/g, "%25");
		yjnr = yjnr.replace(/\&/g, "%26");

		$.ajax({
			url : "/office/sendEmails.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			data : "yjnr=" + yjnr + "&newSletterId=" + mbID + "&zt=" + zt
					+ "&fslb=" + fslb + "&roleCode=" + roleCode + "&beginTime="
					+ beginTime + "&endTime=" + endTime,
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}

	//发送邮件1 级别
	function sendEmails2() {
		var fslb = '1';
		var mbID = '0';
		var zt = $("#input2").val();//主题
		var level = $("#leavel_id").val();
		var beginTime = $("#beginTime3").val();
		var endTime = $("#endTime3").val();
		var timeOk = checkdate(beginTime, endTime);
		if (timeOk == 0) {
			return;
		}
		if (null == zt || zt == "" || zt == "请输入邮件标题") {
			alert('请输入邮件主题');
			return;
		}
		var yjnr = $("#kindEditor2").val();//邮件内容
		if (null == yjnr || yjnr == "" || yjnr == "请输入邮件内容！") {
			alert('请输入邮件内容！');
			return;
		}
		if (((yjnr.indexOf("<html>") > -1) || (yjnr.indexOf("<head>") > -1) || (yjnr
				.indexOf("<body>" > -1))) == 'false') {
			yjnr = yjnr.replace(/[\r\n]/g, "");
		}
		yjnr = yjnr.replace(/%/g, "%25");
		yjnr = yjnr.replace(/\&/g, "%26");
		yjnr = yjnr.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendEmails.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型 
			data : "yjnr=" + yjnr + "&newSletterId=" + mbID + "&zt=" + zt
					+ "&fslb=" + fslb + "&level=" + level + "&beginTime="
					+ beginTime + "&endTime=" + endTime,
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}

	//发送邮件4级别
	function sendEmails4() {
		var fslb = '4';
		var mbID = '0';
		var zt = $("#input4").val();//主题
		var agentValue = $("#agent").val();
		var beginTime = $("#beginTime4").val();
		var endTime = $("#endTime4").val();
		var timeOk = checkdate(beginTime, endTime);
		if (timeOk == 0) {
			return;
		}
		if (null == zt || zt == "" || zt == "请输入邮件标题") {
			alert('请输入邮件主题');
			return;
		}
		if (null == agentValue || agentValue == "" || agentValue == "请输入代理账号") {
			alert('请输入代理账号');
			return;
		}
		var yjnr = $("#kindEditor4").val();//邮件内容
		if (null == yjnr || yjnr == "" || yjnr == "请输入邮件内容！") {
			alert('请输入邮件内容！');
			return;
		}
		if (((yjnr.indexOf("<html>") > -1) || (yjnr.indexOf("<head>") > -1) || (yjnr
				.indexOf("<body>" > -1))) == 'false') {
			yjnr = yjnr.replace(/[\r\n]/g, "");
		}
		yjnr = yjnr.replace(/%/g, "%25");
		yjnr = yjnr.replace(/\&/g, "%26");
		yjnr = yjnr.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendEmails.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型 
			data : "yjnr=" + yjnr + "&newSletterId=" + mbID + "&zt=" + zt
					+ "&fslb=" + fslb + "&agentValue=" + agentValue
					+ "&beginTime=" + beginTime + "&endTime=" + endTime,
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}

	//发送邮件5 级别
	function sendEmails5() {
		var fslb = '5';
		var mbID = '0';
		var zt = $("#input5").val();//主题
		var intro = $("#intro").val();
		var beginTime = $("#beginTime5").val();
		var endTime = $("#endTime5").val();
		var timeOk = checkdate(beginTime, endTime);
		if (timeOk == 0) {
			return;
		}
		if (null == intro || intro == "" || zt == "请输入推荐码") {
			alert('请输入推荐码');
			return;
		}
		if (null == zt || zt == "" || zt == "请输入邮件标题") {
			alert('请输入邮件主题');
			return;
		}
		var yjnr = $("#kindEditor5").val();//邮件内容
		if (null == yjnr || yjnr == "" || yjnr == "请输入邮件内容！") {
			alert('请输入邮件内容！');
			return;
		}
		if (((yjnr.indexOf("<html>") > -1) || (yjnr.indexOf("<head>") > -1) || (yjnr
				.indexOf("<body>" > -1))) == 'false') {
			yjnr = yjnr.replace(/[\r\n]/g, "");
		}
		yjnr = yjnr.replace(/%/g, "%25");
		yjnr = yjnr.replace(/\&/g, "%26");
		yjnr = yjnr.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendEmails.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型 
			data : "yjnr=" + yjnr + "&newSletterId=" + mbID + "&zt=" + zt
					+ "&fslb=" + fslb + "&introValue=" + intro + "&beginTime="
					+ beginTime + "&endTime=" + endTime,
			async : false, // 异步
			success : function(msg) {
				alert(msg);
			},
		});

	}

	//发送邮件6 级别
	function sendEmails6() {
		var fslb = '6';
		var mbID = '0';
		var zt = $("#input6").val();//主题
		var isMoney = $("#isMoney").val();
		var beginTime = $("#beginTime6").val();
		var endTime = $("#endTime6").val();
		var timeOk = checkdate(beginTime, endTime);
		if (timeOk == 0) {
			return;
		}
		if (null == zt || zt == "" || zt == "请输入邮件标题") {
			alert('请输入邮件主题');
			return;
		}
		var yjnr = $("#kindEditor6").val();//邮件内容
		if (null == yjnr || yjnr == "" || yjnr == "请输入邮件内容！") {
			alert('请输入邮件内容！');
			return;
		}
		if (((yjnr.indexOf("<html>") > -1) || (yjnr.indexOf("<head>") > -1) || (yjnr
				.indexOf("<body>" > -1))) == 'false') {
			yjnr = yjnr.replace(/[\r\n]/g, "");
		}
		yjnr = yjnr.replace(/%/g, "%25");
		yjnr = yjnr.replace(/\&/g, "%26");
		yjnr = yjnr.replace(/\+/g, "%2B");
		$.ajax({
			url : "/office/sendEmails.do",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型 
			//data : "yjnr="+yjnr+"&zt="+zt+"&fslb="+fslb+"&isMoney="+isMoney+"&beginTime="+beginTime+"&endTime="+endTime,
			//data : "{yjnr:"+yjnr+",zt:"+zt+",fslb:"+fslb+",isMoney:"+isMoney+",beginTime:"+beginTime+",endTime:"+endTime+"}",
			data : [ {
				"name" : "yjnr",
				"value" : yjnr
			}, {
				"name" : "zt",
				"value" : zt
			}, {
				"name" : "fslb",
				"value" : fslb
			}, {
				"name" : "isMoney",
				"value" : isMoney
			}, {
				"name" : "beginTime",
				"value" : beginTime
			}, {
				"name" : "endTime",
				"value" : endTime
			} ],
			async : false, // 异步
			success : function(msg) {
				alert(msg);
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
		} else if (eid == "a6") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "none";
			document.getElementById("a3").style.display = "none";
			document.getElementById("a4").style.display = "none";
			document.getElementById("a5").style.display = "none";
			document.getElementById("a6").style.display = "block";
		}
	}

	function loadInfo() {
		var frm = document.getElementById("sendEmailForm");
		frm.action = "<c:url value='/office/getUserEmail4.do' />";
		frm.submit();
	}

	function addDh() {
		var sjr = $("#sjr").val();//收件人
		var sjr1 = $("#sjr1").val();//收件人1
		if (null != sjr && sjr != "") {
			$("#sjr").val('');
			if (sjr.charAt(sjr.length - 1) != ','
					&& sjr.charAt(sjr.length - 1) != '，') {
				sjr = sjr + ',';
			}
			$("#sjr").val(sjr);
		} else if (null != sjr1 && sjr1 != "") {
			$("#sjr1").val('');
			if (sjr1.charAt(sjr1.length - 1) != ','
					&& sjr1.charAt(sjr1.length - 1) != '，') {
				sjr1 = sjr1 + ',';
			}
			$("#sjr1").val(sjr1);
		}

	}
</script>

</head>
<body style="background: #b6d9e4; font-size: 12px">
	<div id="excel_menu_left">
		其它 -->最新邮件平台 <a href="javascript:history.back();"><font
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
				href="javascript:showAndHidden('a5');"><b>按推荐码/代码群发邮件</b> </a>
		</div>
	</div>

	<div
		style="position: absolute; top: 50px; height: 20px; left: 760px; width: 150px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a6');"><b>按存款情况群发邮件</b> </a>
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
						<s:form action="sendEmail" namespace="/office" id="sendEmailForm"
							theme="simple" onsubmit="submitonce(this);">
							<tr>
								<td><s:textfield name="loginname" maxlength="20" id="yhzh"
										onblur="loadInfo()" cssClass="input" cssStyle="width:270px;"></s:textfield>
								</td>
							</tr>
						</s:form>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">输入登陆账号系统将自动查询出其Email</td>
			</tr>
			<s:if test="%{#session.operator.authority == 'boss'}">
				<tr>
					<td width="70px" valign="top" align="right">收件人：</td>
					<td align="left" valign="top">
						<table border="0">
							<tr>
								<td><s:textarea name="email" cssClass="input" cols="50"
										id="sjr" onfocus="addDh()" cssStyle="height:120px;" rows="6"
										value="%{#request.user.email}" /></td>
							</tr>
						</table>
					</td>
					<td width="270px" align="left" valign="top"
						style="line-height: 20px">收件人(多个以,隔开
						选择了用户类型此处可为空)如：A@qq.com,B@qq.com</td>
				</tr>
			</s:if>
			<s:else>
				<tr>
					<td>
						<div style="display: none;">
							<s:textarea id="ycemail" name="email"
								value="%{#request.user.email}" style="display: none;" />
						</div>
					</td>
				</tr>
				<tr>
					<td width="70px" valign="top" align="right">收件人：</td>
					<td align="left" valign="top">
						<table border="0">
							<tr>
								<td><s:textarea name="email" cssClass="input" id="sjr1"
										onfocus="addDh()" cols="50" cssStyle="height:120px;" rows="6" /></td>
							</tr>
						</table>
					</td>
					<td width="270px" align="left" valign="top"
						style="line-height: 20px">收件人(多个以,隔开
						选择了用户类型此处可为空)如：A@qq.com,B@qq.com</td>
				</tr>
			</s:else>
			<!-- 	<tr>
					<td width="70px" valign="middle" align="right">邮件模板：</td>
					<td align="left" valign="middle">
						<table border="0">
							<tr>
								<select id="sltList" name="list" onchange="changeValue()"
									style="width: 300px">
									<option value='0'>请选择</option>
								</select>
							</tr>
						</table>
						<td width="270px" align="left" valign="middle"
						style="line-height: 20px;">选择要发送的邮件模板（可为空）</td>
				</tr> -->
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id="input" cssStyle="width:270px;"></s:textfield></td>
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
									<textarea id="kindEditor" name="content"
										style="width: 600px; height: 150px;"></textarea>
								</div>
							</td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容：如果选择模板邮件，模板中的内容如需修改，则只用修改文字部分，代码部分不需要修改，如果是自己编写邮件则没有任何限制！
				</td>
			</tr>
			<tr>
				<td height="44" colspan="2" align="center"><s:submit
						value="发 送" id="fs" onclick="sendEmails()"
						cssClass="button_orange" /></td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>

	<div id="a2"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">用户类别：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="%{#application.UserRole}"
									name="roleCode" id='roleCode_id' listKey="code"
									listValue="text" cssClass="input"
									cssStyle="width:273px;height:28px;"
									onchange="alertMsg(this.value);"></s:select></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">选择用户类型，系统将给所有此类用户发送邮件</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="beginTime2" value="%{startTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTime2" value="%{endTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id="input1" cssStyle="width:270px;"></s:textfield></td>
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
							<td><s:textarea name="Msg" cssClass="input" cols="50"
									rows="6" id="kindEditor1" cssStyle="height:150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
				<td height="44" colspan="2" align="center"><s:submit
						value=" 发 送" onclick="sendEmails1()" cssClass="button_orange" />
				</td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>

	<div id="a3"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">用户级别：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="%{#application.VipLevel}" name="level"
									id='leavel_id' listKey="code" listValue="text" cssClass="input"
									cssStyle="width:273px;height:28px;"
									onchange="alertMsg(this.value);"></s:select></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">选择用户级别，系统将给所有此类用户发送邮件</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="beginTime3" value="%{startTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTime3" value="%{endTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id='input2' cssStyle="width:270px;"></s:textfield></td>
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
							<td><s:textarea name="Msg" cssClass="input" cols="50"
									rows="6" id='kindEditor2' cssStyle="height:150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
				<td height="44" colspan="2" align="center"><s:submit
						value=" 发 送" onclick="sendEmails2()" cssClass="button_orange" />
				</td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>



	<div id="a4"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">代理账号：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id='agent' cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">录入代理账号，系统将给该代理下的所有玩家都发送短信。</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="beginTime4" value="%{startTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTime4" value="%{endTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id='input4' cssStyle="width:270px;"></s:textfield></td>
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
							<td><s:textarea name="Msg" cssClass="input" cols="50"
									rows="6" id='kindEditor4' cssStyle="height:150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
				<td height="44" colspan="2" align="center"><s:submit
						value=" 发 送" onclick="sendEmails4()" cssClass="button_orange" />
				</td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>



	<div id="a5"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">推荐码：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id='intro' cssStyle="width:270px;"></s:textfield></td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">录入推荐码，将给该推荐码下的所有玩家发送邮件！</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="beginTime5" value="%{startTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTime5" value="%{endTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id='input5' cssStyle="width:270px;"></s:textfield></td>
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
							<td><s:textarea name="Msg" cssClass="input" cols="50"
									rows="6" id='kindEditor5' cssStyle="height:150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
				<td height="44" colspan="2" align="center"><s:submit
						value=" 发 送" onclick="sendEmails5()" cssClass="button_orange" />
				</td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>



	<div id="a6"
		style="border: solid; border-width: 1px; position: absolute; display: none; height: 400px; top: 71px; left: 50px; width: 800px; background-color: #CCC">
		<table width="100%" border="0" align="center" cellpadding="2">
			<s:fielderror></s:fielderror>
			<tr>
				<td width="70px" valign="middle" align="right">是否有存款：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:select list="{'有存款','无存款'}" name="level" id='isMoney'
									cssClass="input" cssStyle="width:273px;height:28px;"></s:select>
							</td>
						</tr>
					</table>
				</td>
				<td width="270px" align="left" valign="top"
					style="line-height: 20px;">根据选择的类型，发送邮件！</td>
			</tr>

			<tr>
				<td width="70px" valign="middle" align="right">注册时间(开始)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="beginTime6" value="%{startTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="70px" valign="middle" align="right">注册时间(截止)：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="endTime6" value="%{endTime}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									My97Mark="false" /></td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td width="70px" valign="middle" align="right">主 题：</td>
				<td align="left" valign="middle">
					<table border="0">
						<tr>
							<td><s:textfield name="title" size="60" cssClass="input"
									id='input6' cssStyle="width:270px;"></s:textfield></td>
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
							<td><s:textarea name="Msg" cssClass="input" cols="50"
									rows="6" id='kindEditor6' cssStyle="height:150px;" /></td>
						</tr>
					</table>
				</td>
				<td align="left" valign="top" style="line-height: 20px">邮件内容</td>
			</tr>
			<tr>
				<td height="44" colspan="2" align="center"><s:submit
						value=" 发 送" onclick="sendEmails6()" cssClass="button_orange" />
				</td>
				<td height="44" align="center">&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>




	<c:import url="/office/script.jsp" />
</body>

</html>

