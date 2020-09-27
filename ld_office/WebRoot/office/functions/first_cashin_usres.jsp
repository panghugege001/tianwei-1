<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <s:head  theme="simple"/>
    <title>首存会员信息查询</title>

	
<style type="text/css" media="all">
   @import url("/css/maven-base.css"); 
   @import url("/css/maven-theme.css"); 
   @import url("/css/site.css"); 
   @import url("/css/screen.css");
</style>
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
<style type="text/css" >
table {
	border: 0px solid #666;
	font-size:11px;
}
</style>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
  </head>
  
  <body style="background:#b6d9e4;">

   
   <div style="top:2px;left:5px;font-size:11px;position:absolute;">
   		统计--&gt;首存会员统计
   </div>
    <div style="top:100px;left:200px;border:solid; border-width:1px;width:700px;height:200px;position:absolute;text-algin:center">
    	<s:form action="qeuryFirstCashinUsres" namespace="/office" theme="simple">
   
		   	 <table width="720px" align="center" cellpadding="0" cellspacing="0" border="0">
		    	
		    		<tr>
						<td style="width:100px">&nbsp;</td>
						<td style="text-align:center">检索类型:<s:select name="checktype" cssStyle="width:168px;" list="#{'0':'======全部======','1':'真钱账号','2':'代理账号','3':'代理网址'}" listKey="key" listValue="value"></s:select></td>
						<td>&nbsp;</td>
					</tr>
		    		<tr>
		    				<td style="width:100px">&nbsp;</td>
							<td style="text-align:center;width:240px">
								检索内容:<s:textfield size="25" name="checkcontent"></s:textfield>
							</td>
							<td align="left" valign="bottom" style="color:blue;"><span id="span_content" style="position:absolute;margin-top:5px"></span></td>
							
					</tr>
					<tr>
							<td >&nbsp;</td>
							<td style="text-align:center">
								起始时间:<s:textfield  size="25" name="oneDate" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield>
							</td>
							<td>&nbsp;</td>
					</tr>
					<tr>
							<td>&nbsp;</td>
							<td style="text-align:center">
								结束时间:<s:textfield  size="25" name="twoDate"  value="%{@dfh.utils.DateUtil@getNow()}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield>
							</td>
							<td>&nbsp;</td>
					</tr>
					<tr>
						<td height="30px" colspan="3">&nbsp;</td>
					</tr>
					<tr >
						<td>&nbsp;</td>
						<td style="text-align:center">
							<s:submit value="查    询" cssStyle="height:30px;width:80px" ></s:submit>
						</td>
						<td>&nbsp;</td>
					</tr>
		    </table>
		    
		   </s:form>
    </div>
   
  </body>
  <c:import url="/office/script.jsp" />
</html>
