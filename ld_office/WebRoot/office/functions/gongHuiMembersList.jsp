<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
		
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工会成员列表</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
</head>
<body>
<div id="excel_menu" style="text-align:center;margin-top:20px;">
<s:form action="/office/gongHuiMembersList.do" name="mainform" id="mainform" theme="simple">
	<div id="excel_menu" style="text-align:center;">
	账号:<s:textfield name="loginname" size="15" />
	每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
	<s:submit value="查询"></s:submit>
	<s:hidden name="pageIndex"></s:hidden>
	<s:hidden name="id" ></s:hidden>

	
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
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >用户名</td>
					<td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >所属公会</td>
					<td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >更新时间</td>
					<td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >总存款</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >游戏总流水</td>
					<td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >加入时间</td>
					<td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
	            </tr>

	            <s:iterator var="fc" value="%{#request.page.pageContents}">
		            <tr bgcolor="${bgcolor}">
		              <td align="center" style="font-size:13px;"><s:property value="#fc.username"/></td>
						<td align="center" style="font-size:13px;"><s:property value="#fc.name"/></td>
						<td align="center" style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updatetime"/></td>
						<td align="center" style="font-size:13px;"><s:property value="#fc.deposet"/></td>
		              <td align="center" style="font-size:13px;"><s:property value="#fc.gameAmount"/></td>
		              <td align="center" style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.joinTime"/></td>
						<td align="center" style="font-size:13px;"><s:property value="#fc.remark"/></td>
		            </tr>
		  	 	 </s:iterator>
	            <tr>
	              <td colspan="8" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
</body>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}
var modifyApplyInfo_window;
function modifyApplyInfo(id, name, address, cellphoneNo)
{
	var height = window.screen.height;
	var width = window.screen.width; 
	modifyApplyInfo_window=window.open("<%=basePath%>/office/functions/modifyGiftApplyInfo.jsp?id="+id+"&addressee="+encodeURI(name)+"&address="+encodeURI(address)+"&cellphoneNo="+cellphoneNo, 'applyInfo', 'height=350, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2);
}
</script>
</html>