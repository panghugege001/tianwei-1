<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>首存会员信息查询</title>

	
<style type="text/css" media="all">
   @import url("/css/maven-base.css"); 
   @import url("/css/maven-theme.css"); 
   @import url("/css/site.css"); 
   @import url("/css/screen.css");
</style>
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
<style type="text/css" >


span.pagebanner {
	width: 850px;
}
span.pagelinks {
	width: 850px;
	
}

</style>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
  <p>统计-->首存会员统计-->首存会员信息</p>
  <div align="center">
  		<display:table name="pageList" id="fc" requestURI="/office/qeuryFirstCashinUsres.do" style="width:850px" defaultsort="4">
			<display:column title="序号" style="text-align:left">${fc_rowNum}</display:column>
	    	<display:column property="loginName" title="会员账号" style="text-align:left"></display:column>
	    	<display:column property="registerTime" title="开户日期" style="text-align:center"></display:column>
	    	<display:column property="firstTime" title="存款日期" style="text-align:center"></display:column>
	    	<display:column property="firstAmount" title="存款金额" style="text-align:right" format="{0,number,#,##0.00}"></display:column>
	    	<display:column property="concessionsAmount" title="首存优惠金额" style="text-align:right" format="{0,number,#,##0.00}"></display:column>
	    	<display:column property="referWebsite" title="代理网址" style="text-align:left"></display:column>
	    	<display:column property="howToKnow" title="来源网址" style="text-align:left"></display:column>
	    	<display:footer>
	    		<tr>
	    			<td colspan="7">
	    				<table align="right" width="100%" border="0" style="font-size:12px" cellpadding="0" cellspacing="0">
	    					<tr>
	    						<td width="200px" style="text-align:right"><b>合计：</b></td>
	    						<td width="200px" style="text-align:right">总人数：${request.total.personCount}</td>
	    						<td width="300px" style="text-align:right">总存款金额：<s:property value="@dfh.utils.NumericUtil@double2String(#request.total.firstAmount)"/></td>
	    						<td width="300px" style="text-align:right">总优惠金额：<s:property value="@dfh.utils.NumericUtil@double2String(#request.total.concessionsAmount)"/></td>
	    					</tr>
	    				</table>
	    			</td>
	    		</tr>
	    	</display:footer>
	    </display:table>
  </div>
    
  </body>
  <c:import url="/office/script.jsp" />
</html>
