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
     <p>电话回访-->统计回访结果</p>
    <br/>
    <div align="center" style="width:100%">
	    <s:form action="getTelTaskCount" namespace="/telvisit" method="post" id="mainform" theme="simple">
		   	<table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
		   	
		    	<tr align="left">
		    		<td>
		    			任务名称：<s:textfield name="taskname" size="30"></s:textfield>
		    		</td>
		    		<td>
		    			任务状态：<s:select name="taskstatus" list="#{'-1':'全部','0':'未完成','1':'完成','2':'废弃'}" emptyOption="false" listKey="key" listValue="value"></s:select>
		    		</td>
		    		<td>
		    			起始时间:<s:textfield  size="25" name="start" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield>
		    		</td>
		    		
		    		<td rowspan="2">
		    			<s:submit value="查  询"></s:submit>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
		    			操作员工：<s:textfield name="operator" size="30"></s:textfield>
		    		</td>
		    		<td>
		    			回访结果：<s:select name="execstatus" list="#{'-1':'全部','0':'失败','1':'成功','2':'未访问','3':'已访问'}" emptyOption="false" listKey="key" listValue="value"></s:select>
		    		</td>
					<td>
		    			结束时间:<s:textfield  size="25" name="end" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield>
		    		</td>
		    	</tr>
		   	
		    </table>
	    </s:form>
    </div>
    <div align="center" style="width:100%">
    	<s:if test="pageList!=null">
    		<display:table name="pageList" id="fc" requestURI="/telvisit/getTelTaskCount.do" style="width:1000px" decorator="dfh.displaytag.util.TelvisitThreeFormat">
					<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
			    	<display:column property="taskname" title="任务名称" style="text-align:left" group="1"></display:column>
			    	<display:column property="createtime" title="任务添加时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
			    	<display:column property="operator" title="操作员工" style="text-align:center" ></display:column>
			    	<display:column property="sum" title="总拨打数" style="text-align:center" ></display:column>
			    	<display:column property="success" title="成功数" style="text-align:center"  ></display:column>
			    	<display:column property="fail" title="失败数" style="text-align:center"></display:column>
			    	<display:column property="taskstatus" title="任务状态" style="text-align:center"></display:column>
			    	
			    </display:table>
			    
		</s:if>
    </div>
  </body>
</html>
