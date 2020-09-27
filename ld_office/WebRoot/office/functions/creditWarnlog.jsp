<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>警告记录</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
</head>
<body>
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
$(document).ready(function () {
	var authority = $("#operatorAuthority").val();
	if(authority == 1){
		$("#_creditLogTypeId option[value='CHANGE_BANKMANUAL']").remove();
	}
});
</script>
<s:form action="queryCreditWarnlogs" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
记录 --> 警告记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
额度记录类型:<s:select name="creditLogType" list="%{#application.CreditChangeType}" listKey="code" listValue="text" emptyOption="true" id="_creditLogTypeId"/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
帐号:<s:textfield name="loginname" size="20"></s:textfield>
下限额度:<s:textfield name="downLmit" size="10"></s:textfield>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'createtime'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
<c:choose>
	<c:when test="${sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'sale' }">
		<s:hidden id="operatorAuthority" value="1"/>
	</c:when>
	<c:otherwise>
		<s:hidden id="operatorAuthority" value="0"/>
	</c:otherwise>
</c:choose>
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
		  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">帐号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('type');">额度记录类型</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('remit');">额度变量</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">改变前额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">改变后额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('createtime');">加入时间</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">备注</td>

            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.CreditChangeType@getText(#fc.type)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.remit)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.credit)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.newCredit)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/>   </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
            </tr>
            <s:set var="amountValue" value="#fc.remit" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2">当页小计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"></td>
            </tr>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics1}"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"></td>
            </tr>
            <tr>
              <td colspan="9" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
				${page.jsPageCode} 
              </td>
            </tr>
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
</s:form>
<c:import url="/office/script.jsp" />
</body>
</html>

