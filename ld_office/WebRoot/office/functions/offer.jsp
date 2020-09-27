<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>再存优惠提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery-1.4.2.js' />"></script>
<script type="text/javascript">
	$(function(){
		if($("#mainform_flag").val()==1){
			$("#trflag").hide();
			//$("#lastflag").show();
		}
		
		$("#mainform_flag").change(function(){
			if($("#mainform_flag").val()==1){
				//alert("等于0");
				$("#trflag").hide();
				//$("#lastflag").show();
			}else{
				$("#trflag").show();
			}
		});
	})
</script>
</head>
<body>
<div id="excel_menu_left">
操作 --> 再存优惠提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left"><s:fielderror/></p>
<s:form action="addOfferProposal" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>用户类别:</td><td>
<s:select name="title" list="%{#application.UserRoleExceptFree}" listKey="code" listValue="text" value="MONEY_CUSTOMER"/></td></tr>
<tr><td>会员帐号:</td><td><s:textfield name="loginname" size="30"/></td></tr>
<tr><td>再存金额:</td><td><s:textfield name="firstCash" size="30" value="%{#request.page.money}" /></td></tr>
<tr><td>申请再存优惠金额:</td><td><s:textfield name="tryCredit"  size="30"/></td></tr>
<tr><td>是否有投注要求:</td><td><s:select name="flag" list="#{'1':'否','0':'是'}" headerKey="1" listKey="key" listValue="value"></s:select></td></tr>
<tr id="trflag"><td>投注倍数:</td><td><s:textfield name="times"  size="30" value="30"/></td></tr>
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

