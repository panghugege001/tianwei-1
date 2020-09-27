<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>本地转账记录</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
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

</script>
<s:form action="queryTransfer" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
账户 --> 本地转账记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
转入/转出:<s:select name="isTransferIn" list="#{0:'转入',1:'转出'}"  emptyOption="true"/>
提案状态:<s:select name="transferFalg" list="#{0:'成功',1:'失败',-1:'废弃'}"  emptyOption="true"/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
帐号:<s:textfield name="loginname"></s:textfield>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex" value="1"></s:hidden>
<s:set name="by" value="'createtime'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('id');">转账编号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">帐号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">记录状态</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">来源</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">目标</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('remit');">额度变量</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('credit');">改变前额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('newCredit');">改变后额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('createtime');">加入时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">备注</td>
            </tr>
            
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.id"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:if test="#fc.flag==0">成功</s:if><s:if test="#fc.flag==1">失败</s:if><s:if test="#fc.flag==-1">无效</s:if></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.source"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.target"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.remit)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.credit)"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.newCredit)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
            </tr>
  	 	 </s:iterator>
            <tr>
              <td colspan="11" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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

