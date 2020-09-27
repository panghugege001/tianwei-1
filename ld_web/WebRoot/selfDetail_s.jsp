<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="dfh.model.Users"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
Users users=(Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
String loginname=users.getLoginname();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <base href="<%=request.getRequestURL()%>"/>
    <s:include value="title.jsp"></s:include>
    <meta http-equiv="description" content="自助返水-明细"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
<link type="text/css" rel="stylesheet" href="yilufa.css" />
<style type="text/css">
<!--
body {
	background-color: #0d0213;
}
-->
</style>
<script type="text/javascript" language="javascript">
	
	
	function $(ele){
		return document.getElementById(ele);
	}

	function checkselfform(){
		if(document.salfFrom.startTime.value==""){
			alert("起始时间不能为空");
			return false;
		}else if(document.salfFrom.endTime.value==""){
			alert("截止时间不能为空");
			return false;
		}
		return true;
			
	}

	
</script>
</head>

<body>
 			<div id="requstcontent_s" style="width:710px;">
 				 <s:include value="cwtop.jsp"></s:include>
 				 <h2>反水明细</h2>
                    		<s:url action="searchXimacw" namespace="/asp" var="selfDetailUrl"></s:url>
                        <table border="0" cellpadding="0" cellspacing="0" width="760px" class="mytable" align="left" >
                        	<tr align="left">
                        		<td valign="bottom" colspan="8">
                        		<form name="salfFrom" action="${selfDetailUrl }" method="post" onsubmit="return checkselfform()">
                        			<table align="center" border="0" cellpadding="0" cellspacing="0">
                        				<tr>
	                        				<td><label style="margin-left:12px;">起点时间：</label></td>
	                        				<td><input type="text" class="searchDedail_input" name="startTime" id="startTime" value="${requestScope.startTime }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/></td>
	                        				<td><label style="margin-left:32px;">截止时间：</label></td>
	                        				<td><input type="text" class="searchDedail_input" name="endTime" id="endTime" value="${requestScope.endTime }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/></td>
	                        				<td width="10px">&nbsp;</td>
	                        				<td><input type="submit" class="searchDedail_button" value="" /> <input type="button" class="goback" value="" onclick="window.location='/self_s.asp'"/></td>
	                        			</tr>
                        			</table>
                        			
			                     </form>
                        		</td>
                        	</tr>
                        	<tr><td colspan="8" height="5px">&nbsp;</td></tr>
                        	<tr align="center">
                        		<td width="30px">序号</td>
                        		<td width="100px">编号</td>
                        		<td>统计时间段</td>
                        		<td width="110px">有效投注额</td>
                        		<td width="80px">结算金额</td>
                        		<td width="80px">洗码类型</td>
                        		<td width="50px">洗码率</td>
                        		<td width="50px">状  态</td>
                        	</tr>
                       	<s:if test="ximaList!=null&&ximaList.size()>0">
                       		<s:iterator value="ximaList" var="x" status="st">
                        		<tr>
                        			<td align="center"><s:property value="#st.index+1"/></td>
                        			<td align="center"><s:property value="pno"/></td>
                        			<td align="center"><s:property value="statisticsTimeRange"/></td>
                        			<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.validAmount)"/></td>
                        			<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.ximaAmount)"/></td>
                        			<td align="center"><s:property value="ximaType"/></td>
                        			<td align="right"><s:property value="rate"/></td>
                        			<td align="center"><s:property value="ximaStatus"/></td>
                        		</tr>
                        	</s:iterator>
                        	<tr>
                        		<td colspan="3" align="right">总计:</td>
                        		<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.totalValidAmount)"/></td>
                        		<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.totalXimaAmount)"/></td>
                        		<td colspan="3">&nbsp;</td>
                        	</tr>
                        	<tr><td colspan="8" height="5px">&nbsp;</td></tr>
                        	<tr align="right">
                       			<td colspan="8">
									共&nbsp;<s:property value="totalRowsno"/>&nbsp;条记录&nbsp;&nbsp;
									每页&nbsp;<s:property value="maxRowsno"/>&nbsp;条记录&nbsp;&nbsp;
									共&nbsp;<s:property value="totalPageno"/>&nbsp;页&nbsp;&nbsp;
									第&nbsp;<s:property value="pageno"/>/<s:property value="totalPageno"/>&nbsp;页&nbsp;&nbsp;&nbsp;
									<s:if test="totalPageno==1">
										首页&nbsp;&nbsp;上一页&nbsp;&nbsp;下一页&nbsp;&nbsp;尾页&nbsp;
									</s:if>
									<s:elseif test="pageno==1">
										首页&nbsp;&nbsp;上一页&nbsp;&nbsp;
										<a href="${selfDetailUrl }?pageno=${pageno+1}&startTime=${startTime}&endTime=${endTime}">下一页</a>&nbsp;&nbsp;
										<a href="${selfDetailUrl }?pageno=${totalPageno }&startTime=${startTime}&endTime=${endTime}">尾页</a>&nbsp;
									</s:elseif>
									<s:elseif test="pageno==totalPageno">
										<a href="${selfDetailUrl }?pageno=1&startTime=${startTime}&endTime=${endTime}">首页</a>&nbsp;
										<a href="${selfDetailUrl }?pageno=${pageno-1 }&startTime=${startTime}&endTime=${endTime}">上一页</a>&nbsp;&nbsp;
										上一页&nbsp;&nbsp;尾页&nbsp;
									</s:elseif>
									<s:else>
										<a href="${selfDetailUrl }?pageno=1&startTime=${startTime}&endTime=${endTime}">首页</a>&nbsp;&nbsp;
										<a href="${selfDetailUrl }?pageno=${pageno-1 }&startTime=${startTime}&endTime=${endTime}">上一页</a>&nbsp;&nbsp;
										<a href="${selfDetailUrl }?pageno=${pageno+1}&startTime=${startTime}&endTime=${endTime}">下一页</a>&nbsp;&nbsp;
										<a href="${selfDetailUrl }?pageno=${totalPageno }&startTime=${startTime}&endTime=${endTime}">尾页</a>&nbsp;
									</s:else>
								</td>
                       		</tr>
                       	</s:if>
                       		
                        </table>
                   
             </div>
<!--containercon-->
<s:url value="/scripts/My97DatePicker/WdatePicker.js" var="WdatePickerUrl"></s:url>
<script type="text/javascript" src="${WdatePickerUrl}"></script>
</body>
</html>
