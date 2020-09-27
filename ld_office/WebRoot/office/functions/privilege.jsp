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
    <base href="<%=basePath%>">
    
    <title>优惠批量派发</title>
    
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
function remind(){
	return confirm("Hey，导入前务必检查您的Excel。\n导入有风险，确定请谨慎!");
}
</script>

<div id="excel_menu_left">
操作 --> 优惠批量派发 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<div style="margin-left:80px;font-weight:bold;float:left;">
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='sale_manager' || #session.operator.authority=='sale'">
	<s:form action="impPrivileges" namespace="/batchxima" method ="post" enctype="multipart/form-data" onsubmit="return remind();">
		文件:<input type="file" name="myFile" size="32" accept="application/vnd.ms-excel"/>
		<input type="submit" value="导入优惠" />
	</s:form>
</s:if>	
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance'">
	&nbsp;&nbsp;&nbsp;<input type="button" value="执行当日导入优惠" style="color: red; font-weight: bold;" id="exePrivilegesOfTodayBtn" onclick="exePrivilegesOfToday()"/>
	&nbsp;&nbsp;&nbsp;<input type="button" value="取消当日待执行优惠" style="color: red; font-weight: bold;" id="cancelPrivilegesOfTodayBtn" onclick="cancelPrivilegesOfToday()"/>
</s:if>
</div>
<s:form action="queryPrivileges" namespace="/office" name="mainform" id="mainform" theme="simple">
	状态:<s:select name="status" list="#{'':'全部','0':'待派发','1':'已派发','2':'已取消'}" listKey="key" listValue="value"/>
	会员帐号:<s:textfield name="loginname" size="15" />
	导入时间:<s:textfield size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" name="start" value="%{startTime}"/>
	每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
	<s:submit value="查询"></s:submit>
	<s:hidden name="pageIndex"></s:hidden>
	<s:set name="by" value="'createTime'" />
	<s:set name="order" value="'desc'" />
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
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >账号</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;">金额</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >导入时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >状态</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">开始时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">结束时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >最低存款</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" onclick="orderby('depositAmount');">实际存款</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >最低流水</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" onclick="orderby('betAmount');" >实际流水</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
	            </tr>
	            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><s:property value="#fc.loginName"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
	              <td align="center"  style="font-size:13px;">
					 <s:if test="#fc.status==1">已派发</s:if>
					 <s:elseif test="#fc.status==2">已取消</s:elseif>
					 <s:elseif test="#fc.status==0">待派发</s:elseif>
	              </td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTime"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTime"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.minDeposit)"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.depositAmount)"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.minBet)"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.betAmount)"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td> 
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
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	function exePrivilegesOfToday(){
		$("#exePrivilegesOfTodayBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/exePrivilegesOfToday.do", 
	          cache: false,  
	          success: function(data){alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){$("#exePrivilegesOfTodayBtn").attr("disabled", false);}
        });
	}
    function cancelPrivilegesOfToday(){
		$("#cancelPrivilegesOfTodayBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/cancelPrivilegesOfToday.do", 
	          cache: false,  
	          success: function(data){alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){$("#cancelPrivilegesOfTodayBtn").attr("disabled", false);}
        });
	}
</script>
</body>
</html>