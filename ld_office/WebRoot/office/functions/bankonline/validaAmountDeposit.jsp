<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>bank online transfer</title>
    
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
function excute(transfeId, amount){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/bankonline/supplementPayOrder.jsp?transfeId='+transfeId+"&amount="+amount+"&r="+Math.random(),'','height=250, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}
</script>
<s:form action="queryValidateAmountDeposit" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
记录 --> 额度验证存款记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
转账记录状态:<s:select name="status" list="#{'':'全部','1':'已处理','2':'未匹配'}" listKey="key" listValue="value"/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
是否超时:<s:select cssStyle="width:115px" list="#{'':'','0':'正常','1':'超时'}" name="overtime" listKey="key" listValue="value" emptyOption="false"></s:select>
额度:<s:textfield name="amount" size="10"></s:textfield>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'payTime'" />
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
              <td style="display: none;">ID</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >转出卡账户</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >转出卡卡号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amount');">转账金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >手续费</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">附言</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">转入卡账户</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('payTime');">网银转账时间</td>
               <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('readTime');">系统添加时间</td>
               <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('timecha');">处理用时</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>

            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <c:set var="feeSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            
             <s:if test="#fc.amount>=500000"><c:set var="bgcolor" value="#FF9999"/></s:if>
            <s:elseif test="#fc.amount>=50000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
            <s:elseif test="#fc.amount>=5000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            
            <s:if test="#fc.overtime>0"><c:set var="duanbgcolor" value="#00ff00"/></s:if>
            <s:else><c:set var="duanbgcolor" value="${bgcolor}" /></s:else>
            
            <tr bgcolor="${bgcolor}">
              <td style="display: none;"><s:property value="#fc.transID"/></td>
              <td align="center"  style="font-size:13px;"><s:property value="#fc.depositor"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.cardNo"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.fee)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.note"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.acceptName"/></td>
              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.payTime"/>   </td>
              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.readTime"/>   </td>
              <td  align="center"  bgcolor="${duanbgcolor}" style="font-size:13px;"><s:property value="#fc.timecha"/>   </td>
              <s:if test="#fc.status==1">
              		<td  align="center"  style="font-size:13px;">已处理</td>
              </s:if>
              <s:elseif test="#fc.status==2">
              		<td  align="center"  style="font-size:13px;">未匹配
              		<s:if test="@dfh.security.Authorities@canAccess(request,'supplement')">
              			(<a onclick="excute('<s:property value="#fc.transID"/>', '<s:property value="#fc.amount"/>')" style="cursor:pointer" title="处理">处理</a>)</td>
                   </s:if> 			
              </s:elseif>
              <s:else>
              		<td  align="center"  style="font-size:13px;">未处理</td>
              </s:else>
              		
            </tr>
            <s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            <s:set var="feeValue" value="#fc.fee" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
            <c:set var="feeSum" value="${feeSum+feeValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2">当页小计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.feeSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"></td>
            </tr>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics1}"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics2}"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"></td>
            </tr>
            <tr>
              <td colspan="10" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
