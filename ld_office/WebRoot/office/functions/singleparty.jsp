<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>单身派对</title>
    
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
<s:form action="queryNineDaysBets" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 单身派对<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">

玩家账号:<s:textfield name="loginname" size="15" />
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>

<input type="button" value="查询" id="sub" onclick="disableBut();">
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'ranking'" />
<s:set name="order" value="'asc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>

</div>
<br/>
<br/>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('loginname');" >玩家账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('ranking');">排名</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">统计日期</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('bettotal');">总投注额</td>
            </tr> 
            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.loginname"/>"><s:property value="#fc.loginname"/></a></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.ranking"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.rankdate"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.bettotal"/></td>
	            </tr>
	            <s:set var="amountValue" value="#fc.bettotal" scope="request"></s:set>
            	<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
	  	 	 </s:iterator>
	  	 	 <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="3">当页小计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ></td>
             </tr>
  	 	     <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="3">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ><s:property value="%{#request.page.statics1}"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ></td>
             </tr>
             <tr>
              <td colspan="13" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	function disableBut(){
		window.mainform.submit();
		$("#sub").attr("disabled","true");
		
	}
</script>
</body>
</html>