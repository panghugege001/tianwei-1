<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>peso事务提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getAccountInfoForCashout.do' />";
frm.submit();
}
</script>
</head>
<body>
<div id="excel_menu_left">
操作 --> peso事务提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<p align="left"><s:fielderror/><s:actionerror/></p>

<s:form action="addpesoAttachment" namespace="/batchxima" method ="post" enctype="multipart/form-data" theme="simple">
<table>
<tr><td>收款人:</td><td><s:textfield name="depositname" size="30" /></td></tr>
<tr><td>收款银行:</td><td><s:select name="depositbank" headerValue="" headerKey="" list="%{#application.IssuingBankEnum}" listKey="issuingBank" listValue="issuingBankCode" /></td></tr>
<tr><td>收款帐号:</td><td><s:textfield name="depositaccount" size="30" /></td></tr>
<tr><td>事务类型(不可为空):</td><td><s:select cssStyle="width:115px" name="businessProposalType" list="%{#application.BusinessProposalType}" listKey="code" listValue="text" emptyOption="true"/></td></tr>
<tr><td>金额:</td><td><s:textfield name="amount" size="30" /></td></tr>
<!-- 
<tr><td>当属月份(不可为空):</td><td><select name="belong" id="belong"  style="width:50px">  
                <script language="javascript" type="text/javascript">  
                		document.write("<option value=''></option>");  
  			 			for(var i=1;i<=12;i++){  
                			document.write("<option value="+i+">"+i+"</option>");  
          				}
          				var date= new Date();
          				var monthvalue=date.getMonth();
          				document.getElementById("belong").value=monthvalue+1;
				</script>
		<option value="不属于">不属于</option>  
        </select></td></tr> -->
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td>附件:</td><td><input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交事务" />
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

