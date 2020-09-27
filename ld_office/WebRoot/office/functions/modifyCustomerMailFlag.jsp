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
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
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

	$(document).ready(function(){
		$(".mtTouzhuFlag").click(function(){
			var _this = $(this);
			var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
			if(remark || remark==""){
				var action = _this.attr("url");
				var xmlhttp = new Ajax.Request(    
						action,
				        {
				            method: 'post',
				            parameters:"remark="+remark+"&r="+Math.random(),
				            onComplete: function(){
				            	$("#mainform").submit();
				            }
				        }
			    	);
			}
		});
	});
</script>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
    <br>
     <p>账户-->修改邮件标识</p>
    <br/>
    	<s:form action="changeUserMailFlag" namespace="/userstatus" method="post" id="mainform" theme="simple">
     			<table border="0" style="width:800px" cellpadding="0" cellspacing="0" align="center">
     				<tr align="left">
		   				<td width="60px" style="text-align:right">
		    				开通与否:
		    			</td>
		    			<td width="60px"><s:select cssStyle="width:60px" list="#{'-1':'全部','0':'是'}" name="mailflag" listKey="key" listValue="value" emptyOption="false"></s:select></td>
		    			<td width="70px" style="text-align:right">
		    				会员账号:
		    			</td>
		    			<td width="70px"><s:textfield name="loginname"></s:textfield></td>
		    			<td width="70px" style="text-align:right">
		    				起始时间:
		    			</td>
		    			<td>
		    				<s:textfield  size="25" name="start" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield> 
		    			</td>
		    			<td rowspan="2" width="60px">
		    			<s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit>
		    		</td>
		   			</tr>
		    		<tr align="left">
		    			<c:if test="${sessionScope.operator.authority eq 'boss'}">
			    			<td width="60px" style="text-align:right">电话号码:</td>
			    			<td width="60px"><s:textfield name="phone"></s:textfield></td>
			    			<td width="60px" style="text-align:right">邮件地址:</td>
			    			<td width="150px" style="text-align:left"><s:textfield name="email" size="20"></s:textfield></td>
		    			</c:if>
		    			<c:if test="${sessionScope.operator.authority ne 'boss'}">
			    			<td width="60px" style="text-align:right"></td>
			    			<td width="60px"></td>
			    			<td width="60px" style="text-align:right"></td>
			    			<td width="150px" style="text-align:left"></td>
		    			</c:if>
		    			<td width="70px" style="text-align:right">结束时间:</td>
		    			<td>
		    				 <s:textfield  size="25" name="end" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield>
		    			</td>
		    		</tr>
     			</table>
		 </s:form>
		  <div align="center" style="width:100%">
		  	<s:if test="pageList!=null">
		  		<display:table name="pageList" id="fc" requestURI="/userstatus/changeUserMailFlag.do" style="width:800px" decorator="dfh.displaytag.util.UserStatusFormat">
		  			<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
		  			<display:column property="loginname" title="会员账号" style="text-align:left"></display:column>
		  			<c:if test="${sessionScope.operator.authority eq 'boss'}">
		  			<display:column property="phone" title="电话号码" style="text-align:left"></display:column>
		  			<display:column property="email" title="邮箱" style="text-align:left"></display:column>
		  			</c:if>
		  			<display:column property="createtime" title="开户时间" style="text-align:left"></display:column>
		  			<display:column property="remark" title="备注" style="text-align:left"></display:column>
		  			<display:column property="cz" title="操作" style="text-align:left"></display:column>
		  			<display:column property="touzhu" title="投注要求" style="text-align:left"></display:column>
		  		</display:table>
		  	</s:if>
		  </div>
	    
  </body>
</html>
