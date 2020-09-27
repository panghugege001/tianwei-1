<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>洗码优惠提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>
<body style="background:#b6d9e4;font-size:12px">
<script type="text/javascript">
function computeTryCredit(){
var frm=document.getElementById("mainform");
if(!isNaN(frm.firstCash.value) && !isNaN(frm.rate.value)){
	frm.tryCredit.value=(parseFloat(frm.rate.value)*parseFloat(frm.firstCash.value)/100).toFixed(2);
}
}
var http_request;
var userName;
function queryLastXimaTime(nameInput,url) {
	nameInput=nameInput.replace(/(^\s*)/g, "").replace(/(\s*$)/g, "");
	if(nameInput==null || nameInput==""){
		alert("请输入帐号");
		return;
	}
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
		http_request.onreadystatechange = function(){
			if (http_request.readyState == 4) {
				var chkResult = http_request.responseText;
				document.getElementById("mainform").start.value=chkResult;
			}
		};
		http_request.open("POST", url,true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+userName);
}

function queryValidBetAmount(url) {
	var frm=document.getElementById("mainform");
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
		http_request.onreadystatechange = function(){
			if (http_request.readyState == 4) {
				var chkResult = http_request.responseText;
				frm.firstCash.value=chkResult;
			}
		};;
		http_request.open("POST", url,true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+userName+"&start="+frm.start.value+"&end="+frm.end.value);
}
</script>
<div id="excel_menu_left">
操作 --> 洗码优惠提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<s:set var="endTime" value="@dfh.utils.DateUtil@getOneHourAgoFormat()" />
<div id="excel_menu">

<p align="left"><s:fielderror/></p>
<s:form action="addXimaProposal" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>用户类别:</td><td>
<s:select name="title" list="%{#application.UserRoleExceptFree}" listKey="code" listValue="text" value="MONEY_CUSTOMER"/></td></tr>
<s:url var="queryLastXimaTime" value='/office/queryLastXimaTime.do' />
<s:url var="queryValidBetAmount" value='/office/queryValidBetAmount.do' />
<tr><td>会员帐号:</td><td><s:textfield name="loginname" size="30"/></td></tr>
<tr><td>结算开始时间:</td><td><s:textfield name="start" size="20"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td></tr>
<tr><td>结算截至时间:</td><td><s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" /></td></tr>
<tr><td>有效投注额:</td><td><!--<s:textfield name="firstCash" size="30" onfocus="queryValidBetAmount('%{queryValidBetAmount}');"/>--><s:textfield name="firstCash" size="30"/></td></tr>
<tr><td>洗码比率:</td><td><s:textfield name="rate" size="30" onkeyup="computeTryCredit();"/>%</td></tr>
<tr><td>申请优惠金额:</td><td><s:textfield name="tryCredit" size="30" disabled="true"/></td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td align="center"><s:submit value="提交" /></td><td></td></tr>

	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addNewPtBatchSixXimaProposal" namespace="/batchxima" method ="post" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(NEWPT)" />
        <s:hidden name="rate" value="0.05"></s:hidden>
        </s:form>
    	</td></tr> 
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

