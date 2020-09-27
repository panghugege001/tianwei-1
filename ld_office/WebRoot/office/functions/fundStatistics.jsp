<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
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
    <br/>
    <p>统计-->银行进出帐统计</p>
    <br/>
    <div align="center" style="width:100%">
	    <s:form action="searchFundDetail" namespace="/statistics" method="post"  theme="simple">
	    	<table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
		    	<tr align="left">
		    		<td>
		    			检索类型：<s:select name="searchType" list="#{'1':'存款','2':'提款'}" listKey="key" listValue="value" />
		    		</td>
		    		<td>
		    			银行名称：<s:select name="bankName" list="%{#application.IssuingBankEnum}" emptyOption="true" listKey="issuingBank" listValue="issuingBankCode" />
		    		</td>
		    		<td>
		    			起始时间:<s:textfield  size="25" name="start" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield>
		    		</td>
		    		<td>
		    			结束时间:<s:textfield  size="25" name="end" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield>
		    		</td>
		    		<td>
		    			<s:submit value="查  询"></s:submit>
		    		</td>
		    	</tr>
		    </table>
	    </s:form>
     </div>
    <div align="center" style="width:100%">
    	<s:if test="pageList!=null">
    		<s:if test="searchType==1">
	 			<display:table name="pageList" id="fc" requestURI="/statistics/searchFundDetail.do" style="width:850px">
					<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
			    	<display:column property="bankName" title="银行名称" style="text-align:left"></display:column>
			    	<display:column property="totalCount" title="存款次数" style="text-align:center"></display:column>
			    	<display:column property="amount" title="存款金额" style="text-align:right" format="{0,number,#,##0.00}">></display:column>
			    	<display:footer>
			    		<tr>
			    			<td colspan="4">
			    				<table align="right" width="100%" border="0" style="font-size:12px" cellpadding="0" cellspacing="0">
			    					<tr>
			    						<td width="200px" style="text-align:right"><b>合计：</b></td>
			    						<td width="200px" style="text-align:right">总存款次数：<s:property value="#request.totalRecord.totalCount"/></td>
			    						<td width="300px" style="text-align:right">总存款金额：<s:property value="@dfh.utils.NumericUtil@double2String(#request.totalRecord.amount)"/></td>
			    					</tr>
			    				</table>
			    			</td>
			    		</tr>
			    	</display:footer>
			    </display:table>
		    </s:if>
		    <s:else>
		    	<display:table name="pageList" id="fc" requestURI="/statistics/searchFundDetail.do" style="width:850px">
					<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
			    	<display:column property="bankName" title="银行名称" style="text-align:left"></display:column>
			    	<display:column property="totalCount" title="提款次数" style="text-align:center"></display:column>
			    	<display:column property="amount" title="提款金额" style="text-align:right" format="{0,number,#,##0.00}">></display:column>
			    	<display:footer>
			    		<tr>
			    			<td colspan="4">
			    				<table align="right" width="100%" border="0" style="font-size:12px" cellpadding="0" cellspacing="0">
			    					<tr>
			    						<td width="200px" style="text-align:right"><b>合计：</b></td>
			    						<td width="200px" style="text-align:right">总提款次数：<s:property value="#request.totalRecord.totalCount"/></td>
			    						<td width="300px" style="text-align:right">总提款金额：<s:property value="@dfh.utils.NumericUtil@double2String(#request.totalRecord.amount)"/></td>
			    					</tr>
			    				</table>
			    			</td>
			    		</tr>
			    	</display:footer>
			    </display:table>
		    </s:else>
		</s:if>
    </div>
  </body>
</html>
