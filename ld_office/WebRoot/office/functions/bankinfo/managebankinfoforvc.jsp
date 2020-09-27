<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<head>
<title>管理银行账户</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}

function submitForNewAction(btn,action,pno,useable){
	btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"jobPno="+pno+"&useable="+useable+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function responseMethod(data){

	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}
function searchforeditbankinfo(_aid){
	window.location="/bankinfo/searchforeditbankinfo.do?jobPno="+_aid;

}
</script>
</head>
<body>
<p>
账户 --&gt; 查询银行账户 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
<s:form action="managebankinfoforvc" namespace="/bankinfo" name="mainform" id="mainform" theme="simple">
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="1100px">
				<tr align="left">
					<td>账户名称:<s:textfield name="username" size="30" /></td>
					<td>账户性质:<s:select cssStyle="width:80px" list="#{'0':'','1':'存款账户','2':'支付账户','3':'存储账户','4':'在线账户','5':'事务账户(人民币)','6':'事务账户(比索)','7':'VIP存款账户'}" name="type" listKey="key" listValue="value" emptyOption="false"></s:select></td>
					<td>银行名称:<s:select name="bankname" list="%{#application.IssuingBankEnum}" emptyOption="true" listKey="issuingBank" listValue="issuingBankCode" /></td>
					<td>每页:<s:select cssStyle="width:120px" list="%{#application.PageSizes}" name="size"></s:select></td>
					<td ><s:submit  value="查询"></s:submit></td>
				
				</tr>
		</table>
	</td>
</tr>

</table>

<s:hidden name="pageIndex"/>
<s:set name="by" value="'createTime'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
<s:hidden name="jobPno"></s:hidden>
<s:hidden name="remark"></s:hidden>
</s:form>
</div>

<div id="middle" style="position:absolute; top:125px;left:0px">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="960px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">账户名称</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">账户性质</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">银行名称</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">账户余额</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">备注</td>
             
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="#e4f2ff">
              <td align="center" width="80px" ><s:property value="#fc.username"/></td>
              <td  align="center" width="70px" ><s:property value="@dfh.model.enums.Banktype@getText(#fc.type)"/> </td>
               <td  align="center" width="80px" ><s:property value="#fc.bankname"/></td>
               <td  align="center" width="80px" ><s:property value="#fc.amount"/></td>
              <td  align="center"  width="140px"><s:property value="#fc.remark"/></td>
              <s:set var="jobPno" value="#fc.id" scope="request"/>
 
              
             
             
             
                      
            </tr>
            <s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="3">当页小计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="3"></td>
            </tr>
            <tr>
              <td colspan="13" align="right" bgcolor="66b5ff" align="center" >
				${page.jsPageCode}
              </td>
            </tr>
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

