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
<script type="text/javascript">
	function checkContent(){
		if(document.getElementById("agentFlag").value.length==0){
			alert("检索内容不能为空，请重新输入");
			return false;
		}else{

			return true;
		}
	}
</script>
  </head>
  
   <body style="background:#b6d9e4;font-size:11px">
     <br>
     <p>统计-->代理下线统计</p>
    <br/>
    
     <div align="center" style="width:100%">
	    <s:form action="agentReferralsStatistic" namespace="/statistics" method="post"  theme="simple" onsubmit="return checkContent()">
		   	<table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
		   	
		    	<tr align="left">
		    		<td>
		    			检索类型：<s:select list="#{'0':'代理账号','1':'代理网址','2':'代理编号'}" listKey="key" listValue="value" name="flagType"></s:select>
		    		</td>
		    		<td>
		    			检索内容：<s:textfield name="agentFlag" id="agentFlag"></s:textfield>
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
	    <s:if test="#request.personCount!=null">
	    	<table align="center" width="100%" border="1" style="font-size:12px" cellpadding="0" cellspacing="0">
	    		<tr>
	    			<td colspan="3"><font color="blue">指定时间范围内，该代理的下线会员存款人数及存款金额如下：</font></td>
	    		</tr>
				<tr>
					<td width="200px" style="text-align:right"><b>合计：</b></td>
					<td width="200px" style="text-align:right">下线人数：<s:property value="#request.personCount"/></td>
					<td width="300px" style="text-align:right">存款金额：<s:property value="@dfh.utils.NumericUtil@double2String(#request.amount)"/></td>
				</tr>
			</table>
		</s:if>
    </div>
  </body>
</html>
<c:import url="/office/script.jsp" />