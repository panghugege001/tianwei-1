<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>开户提案</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<script type="text/javascript">
var http_request;
var userName;
function checkCusExist(nameInput,url) {
	nameInput=nameInput.replace(/(^\s*)/g, "").replace(/(\s*$)/g, "");
	if(nameInput==null || nameInput==""){
		alert("请输入帐号");
		return;
	}
	document.getElementById("checkBtn").disabled = true;
	userName=nameInput;
	if (window.XMLHttpRequest) { // if Mozilla, Safari etc
			http_request = new XMLHttpRequest();
			if (http_request.overrideMimeType) {
				http_request.overrideMimeType("text/xml");
			}
		} else {
			if (window.ActiveXObject) { // if IE
				try {
					http_request = new ActiveXObject("Msxml2.XMLHTTP");
				}
				catch (e) {
					try {
						http_request = new ActiveXObject("Microsoft.XMLHTTP");
					}
					catch (e) {
					}
				}
			}
		}
		http_request.onreadystatechange = process;
		http_request.open("POST", url,true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+userName);
}

function process() {
	if (http_request.readyState == 4) {
		var chkResult = http_request.responseText;
		if (chkResult && chkResult == 'true') {    
			alert(userName + "该帐号已经被使用,请另选一个!");
		} else if(chkResult && chkResult == 'false') {
			alert(userName + "该帐号可以使用");
		} else{
			alert("对不起，出现网络故障，请联系客服！"+chkResult);
		}
		document.getElementById("checkBtn").disabled = false;
	}
}
</script>
<div id="excel_menu_left">
操作 --> 开户提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<font color="red">[系统会自动根据用户类别检测帐号字头]</font><br/>
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="addNewAccountProposal" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>用户类别:</td><td>
<s:select name="title" list="%{#application.UserRoleExceptFree}" listKey="code" listValue="text"/></td></tr>
<tr><td>帐号:</td><td><s:textfield name="loginname" id="loginname" size="30" maxlength="10"/><input type="button" id="checkBtn" value="检测" onclick="checkCusExist(document.getElementById('loginname').value,'<c:url value='/office/checkUserExsit.do' />')"/></td></tr>
<tr><td>密码:</td><td><s:password name="password" size="30"/></td></tr>
<tr><td>真实姓名:</td><td><s:textfield name="aliasName" size="30"/></td></tr>
<tr><td>电话号码:</td><td><s:textfield name="phone" size="30"/></td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td align="center"><s:submit value="提交" /></td><td></td></tr>
</table>
</s:form>
</div>

<br/>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

