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
     <br>
     <p>统计-->来源网址统计</p>
    <br/>
    
    <p style="color:red">
     		2010年12月26日以前的会员来源网址为空（该功能尚未开通）。这些会员将不会纳入该统计之内。
   	</p>
     <div align="center" style="width:100%">
	    <s:form action="search" namespace="/statistics" method="post"  theme="simple">
		   	<table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
		   	
		    	<tr align="left">
		    		<td>
		    			来源网址：<s:textfield name="howToKnowURL" size="50"></s:textfield>
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
    		<display:table name="pageList" id="fc" requestURI="/statistics/search.do" style="width:850px">
					<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
			    	<display:column property="howToKnow" title="来源网址" style="text-align:left"></display:column>
			    	<display:column property="count" title="注册数量" style="text-align:right" format="{0,number,#,##0}"></display:column>
			    </display:table>
		</s:if>
    </div>
  </body>
</html>
