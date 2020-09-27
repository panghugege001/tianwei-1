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
	width: 1000px;
}
span.pagelinks {
	width: 1000px;
	
}

</style>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
	
function taskComplete(_taskid){ 
	if(!isNaN(_taskid)){
		var url="/telvisit/visitComplete.do";
		 var xmlhttp = new Ajax.Request(    
				 url,
		        {    
		            method: 'post',
		            parameters:"taskid="+_taskid+"&r="+Math.random(),
		            onComplete: function(data){alert(data.responseText);}  
		        }
	    	);

	}else{
		alert("请不要篡改数据");
	}	
}

function submitForNewAction(action,pno){
			
			//var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
			if(confirm("你确认要解锁么？")){
		 		var xmlhttp = new Ajax.Request(    
					action,
		        	{    
		            	method: 'post',
		            	parameters:"id="+pno+"&r="+Math.random(),
		            	onComplete: responseMethod  
		        	}
	    		);

			}	
	}
	
	function responseMethod(data){

			alert(data.responseText);
			var frm=document.getElementById("mainform");
			frm.submit();
	}


</script>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
     <br>
     <p>电话回访-->管理任务列表-->任务列表明细&nbsp;&nbsp;<a href="/office/functions/managetask.jsp"> <font color="red">上一步</font></a></p>
    <p>当前任务：<font color="blue"><s:property value="taskName"/></font>&nbsp;&nbsp;</p>
     <s:form action="getVisitTaskForNolockByCondition" namespace="/telvisit" method="post" id="mainform" theme="simple">
     		<s:hidden name="taskid"></s:hidden>
     		<s:hidden name="taskName"></s:hidden>
		   	<table border="0" style="width:1000px" cellpadding="0" cellspacing="0" align="center">
		   		
		   		<tr align="left">
		   			<td width="60px" style="text-align:right">
		    			锁定与否:
		    		</td>
		    		<td width="60px"><s:select cssStyle="width:60px" list="#{'-1':'全部','0':'否','1':'是'}" name="islock" listKey="key" listValue="value" emptyOption="false"></s:select></td>
		    		<td width="70px" style="text-align:right">
		    			会员账号:
		    		</td>
		    		<td width="150px"><s:textfield name="loginname"></s:textfield></td>
		    		<td colspan="2" width="500px">
		    			会员注册时间范围:<s:textfield  size="25" name="start" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield> 至 <s:textfield  size="25" name="end" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield>
		    		</td>
		    		<td rowspan="2" width="60px">
		    			<s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit>
		    		</td>
		   		</tr>
		    	<tr align="left">
		    		
		    		<td width="60px" style="text-align:right">
		    			访问结果:
		    		</td>
		    		<td width="60px"><s:select list="#{'-1':'全部','0':'失败','1':'成功','2':'未访问','3':'已访问'}" name="execstatus" emptyOption="false" listKey="key" listValue="value"></s:select></td>
		    		<c:if test="${sessionScope.operator.authority eq 'boss'}">
		    		<td width="70px" style="text-align:right">
		    			电话号码:
		    		</td>
		    		<td width="150px"><s:textfield name="phone"></s:textfield></td>
		    		<td width="60px" style="text-align:left">
		    			邮件地址:
		    		</td>
		    		<td width="440px" style="text-align:left"><s:textfield name="email" size="30"></s:textfield></td>
		    		</c:if>
		    		
		    	</tr>
		 
		    </table>
	    </s:form>
    <div align="center" style="width:100%">
    	<s:if test="pageList!=null">
    		<display:table name="pageList" id="fc" requestURI="/telvisit/nextpageForCountNoUnlock.do" style="width:1000px" decorator="dfh.displaytag.util.TelvisitFormat">
				<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
		    	<display:column property="loginnameforsee" title="会员账号" style="text-align:left"></display:column>
		    	<display:column property="accountname" title="真实姓名" style="text-align:left"></display:column>
		    	<c:if test="${sessionScope.operator.authority eq 'boss'}">
		    	<display:column property="phone" title="电话号码" style="text-align:left"></display:column>
		    	<display:column property="email" title="邮件地址" style="text-align:left"></display:column>
		    	</c:if>
		    	<display:column property="createtime" title="注册时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
		    	<display:column property="lastlogintime" title="最后登录时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
		    	<display:column property="intervaltime" title="登录间隔天数" style="text-align:left"></display:column>
		    	<display:column property="execstatus" title="访问结果" style="text-align:center"></display:column>
		    	<display:column property="islock" title="锁定" style="text-align:center"></display:column>
		    	
		    </display:table>
		</s:if>
    </div>
    <br/><br/><br/>
  </body>
</html>
