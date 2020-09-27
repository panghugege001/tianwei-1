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
<script type="text/javascript" src="<c:url value='/js/jquery-1.4.2.js' />"></script>
<script type="text/javascript">
	$(function(){
		if($("#generateTask_flag").val()==0){
			$("#trflag ~ tr").hide();
			$("#lastflag").show();
		}
		
		$("#generateTask_flag").change(function(){
			if($("#generateTask_flag").val()==0){
				//alert("等于0");
				$("#trflag ~ tr").hide();
				$("#lastflag").show();
			}else{
				$("#trflag ~ tr").show();
			}
		});
	})
</script>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
    <br>
     <p>电话回访-->生成任务列表</p>
    <br/>
    <div align="center" style="width:100%">
	    <s:form action="generateTask" namespace="/telvisit" method="post"  theme="simple">
		   	<table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
		   		<tr>
		   			<td style="text-align:right">任务名称：</td><td align="left"><s:textfield size="55" name="taskName"/></td>
		   		</tr>
		   		<tr>
		   			<td style="text-align:right">任务周期：</td><td align="left"><s:textfield  size="25" name="taskStartTime" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield> 至 <s:textfield  size="25" name="taskEndTime" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield></td>
		   		</tr>
		   		<tr id="trflag">
		   			<td style="text-align:right">是否系统洗码：</td><td align="left"><s:select name="flag" list="#{'1':'否','0':'是'}" headerKey="1" listKey="key" listValue="value"></s:select></td>
		   		</tr>
		   		
		   		<tr>
		   			<td style="text-align:right">代理网址：</td><td align="left"><s:textfield size="50" name="agentURL"/></td>
		   		</tr>
		   		<tr>
		   			<td style="text-align:right">登录间隔天数：</td><td align="left"><s:textfield size="7" name="loginInterval"/></td>
		   		</tr>
		   		<tr>
		   			<td style="text-align:right">存款状态：</td><td align="left"><s:select name="isCashin" list="#{'0':'已存款','1':'未存款'}" emptyOption="true" listKey="key" listValue="value"></s:select></td>
		   		</tr>
		   		<tr>
		   			<td style="text-align:right">用户等级：</td><td align="left"><s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/></td>
		   		</tr>
		   		<tr>
		   			<td style="text-align:right">会员注册时间范围：</td><td align="left"><s:textfield  size="25" name="start" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield> 至 <s:textfield  size="25" name="end" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield></td>
		   		</tr>
		   		
		   		<tr id="lastflag">
		   			<td style="text-align:center" colspan="2"><s:submit value="生成任务"></s:submit></td>
		   		</tr>
		    	
		   	
		    </table>
	    </s:form>
    </div>
  </body>
</html>
