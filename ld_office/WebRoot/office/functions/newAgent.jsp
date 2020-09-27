<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新增代理</title>
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
操作 --> 新增代理 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<font color="red"></font><br/>
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="addNewAgent" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>代理帐号:</td><td><s:textfield name="loginname" id="loginname" size="30" maxlength="18"/><input type="button" id="checkBtn" value="检测" onclick="checkCusExist(document.getElementById('loginname').value,'<c:url value='/office/checkUserExsit.do' />')"/></td></tr>
<tr><td>代理ID:</td><td><s:password name="acode" size="30"/></td></tr>
<tr><td>真实姓名:</td><td><s:textfield name="accountName" size="30"/></td></tr>
<tr><td>代理网址:</td><td><s:textfield name="referWebsite" size="50"/></td></tr>
<tr><td>邮箱:</td><td><s:textfield name="email" size="50"/></td></tr>
<tr><td>电话号码:</td><td><s:textfield name="phone" size="30"/></td></tr>
<tr><td>QQ:</td><td><s:textfield name="qq" size="20"/></td></tr>
<tr><td>代理推荐码:</td><td><s:textfield name="partner" size="20"/></td></tr>
<tr><td>代理类型:</td><td><s:select name="agentType" list="#{1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="true"></s:select> </td></tr>
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

