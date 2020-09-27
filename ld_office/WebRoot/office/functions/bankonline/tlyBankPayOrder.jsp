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
<script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>
  </head>
  
  <body>
<script type="text/javascript">
$(document).ready(function(){
	  $.post("/bankinfo/queryTlyBankinfo.do",{"bankname":"同略云銀行","type":7},function(data){
		  var json = eval(data);
		  $(json).each(function(index){
		  	$("#acceptName").append("<option value='"+json[index].username+"'>"+json[index].username+"</option>");
		  });
	  });
});
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
function excute(transfeId){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/bankonline/excutecmbtransfer.jsp?transfeId='+transfeId+"&r="+Math.random(),'','height=250, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}
function change(transfeId){
	if(confirm('你确定要把订单改为已处理吗?')){
		$.post("/office/changeCMBBank.do",{"transfeId":transfeId},function(data){
			alert(data);
		});
	}
}



function makeUp() {
	
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	
	if (startTime == '') {
		
		alert('补录开始时间不能为空！');
		return;
	}
	
	if (endTime == '') {
		
		alert('补录结束时间不能为空！');
		return;
	}
	
	$.post("/office/makeUp.do", { "activityBeginTime": startTime, 'activityEndTime': endTime }, function (data) {
		
		alert(data);
	});
}




</script>
<s:form action="queryCmbTransferTly" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
记录 --> 银行二维码存款订单记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
	招商银行账户:<select name="acceptName" id="acceptName" ><option></option></select>
	转账记录状态:<s:select name="status" list="#{'':'全部','1':'已处理','2':'未匹配','0':'未处理'}" listKey="key" listValue="value"/>
	开始时间: <s:textfield name="start" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  />
	结束时间:<s:textfield name="end" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
	是否超时:<s:select cssStyle="width:115px" list="#{'':'','0':'正常','1':'超时'}" name="overtime" listKey="key" listValue="value" emptyOption="false"></s:select>
	转账卡号:<s:textfield name="uaccountno" size="10"></s:textfield>
	用户名:<s:textfield name="loginname" size="10"></s:textfield>
	每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
	<s:submit value="查询"></s:submit>
	<s:hidden name="pageIndex"></s:hidden>
	<s:set name="by" value="'payDate'" />
	<s:set name="order" value="'desc'" />
	<s:hidden name="payType" value="5"/>
	<s:hidden name="order" value="%{order}"/>
	<s:hidden name="by" value="%{by}"/>
</div>


<%-- <c:if test="${sessionScope.operator.authority eq 'boss' or sessionScope.operator.authority eq 'admin' or sessionScope.operator.authority eq 'finance_manager'}">
	<div id="excel_menu">
		补录开始时间:<s:textfield id="startTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
		补录结束时间:<s:textfield id="endTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
		<input type='button' value='提交' onclick='makeUp()' />
	</div>
</c:if> --%>

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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >用户名</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amount');">转账金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amount');">转账卡号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">附言</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">姓名</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易类型</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">转入卡账户</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">转入卡卡号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('payDate');">网银转账时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('date');">系统添加时间</td>
               <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('timecha');">处理用时</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>
               <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">备注</td>

            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            
            <s:if test="#fc.amount>=500000"><c:set var="bgcolor" value="#FF9999"/></s:if>
            <s:elseif test="#fc.amount>=50000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
            <s:elseif test="#fc.amount>=5000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            
            <s:if test="#fc.overtime>0"><c:set var="duanbgcolor" value="#00ff00"/></s:if>
            <s:else><c:set var="duanbgcolor" value="${bgcolor}" /></s:else>
            
            <tr bgcolor="${bgcolor}">
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.uaccountno"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.notes"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.uaccountname"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.jylx"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.acceptName"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.StringUtil@subStrBefore(#fc.acceptCardnum, 4)"  /></td>
              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.payDate"/>   </td>
              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.date"/>   </td>
              <td  align="center"  bgcolor="${duanbgcolor}" style="font-size:13px;"><s:property value="#fc.timecha"/>   </td>
              <s:if test="#fc.status==1">
              		<td  align="center"  style="font-size:13px;">已处理</td>
              </s:if>
              <s:elseif test="#fc.status==2">
              		<td  align="center"  style="font-size:13px;">未匹配
              		<c:if test="${sessionScope.operator.authority eq 'boss' or sessionScope.operator.authority eq 'admin' or sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_manager' or  sessionScope.operator.authority eq 'finance_leader'}">
              			(<a onclick="excute('<s:property value="#fc.transfeId"/>')" style="cursor:pointer" title="处理">处理</a>)
              		</c:if>
              		<c:if test="${sessionScope.operator.authority eq 'boss' or sessionScope.operator.authority eq 'admin' or sessionScope.operator.authority eq 'finance_manager'}">
              			| (<a onclick="change('<s:property value="#fc.transfeId"/>')" style="cursor:pointer" title="改状态">改状态</a>)
              		</c:if>
              		</td>
              </s:elseif>
              <s:else>
              		<td  align="center"  style="font-size:13px;">未处理</td>
              </s:else>
              		<td  align="center"  bgcolor="${duanbgcolor}" style="font-size:13px;"><s:property value="#fc.remark"/>   </td>	
              		
            </tr>
            <s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2">当页小计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="9"></td>    
            </tr>
            <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' ||   #session.operator.authority=='finance_leader' ||#session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager'">
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics1}"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">合计：<s:property value="%{#request.page.statics1}"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="10"></td>
            </tr>
            </s:if>
            <tr>
              <td colspan="14" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
