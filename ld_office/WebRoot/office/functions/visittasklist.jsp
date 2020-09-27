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

function changeQyExten(num){
	var oEle = document.getElementsByName("_call");
	if(oEle.length){
	   for(var i = 0 ;i<oEle.length;i++){
	      var href = oEle[i].href;
	      var replaceStr = href.substring(0,href.length-3)+num;
		  oEle[i].href = replaceStr;
	  }
	}
}
function changeOtherExten(num){
	var oEle = document.getElementsByName("_call2");
	if(oEle.length){
		for(var i = 0 ;i<oEle.length;i++){
			var href = oEle[i].href;
			var replaceStr = href.substring(0,href.length-4)+num;
			oEle[i].href = replaceStr;
		}
	}
}
function getEncy(intro,num){
	var r = prompt("请输入推荐码"+intro+"的密匙:","");
	if(r){
		if("a"==intro){
			if(r==121322){
				alert(num-r-1111111111);
			}else{
				alert("密匙错误");
			}
		}else if("b"==intro){
			if(r==221433){
				alert(num-r-1222222221);
			}else{
				alert("密匙错误");
			}
		}else if("c"==intro){
			if(r==331543){
				alert(num-r-1333333331);
			}else{
				alert("密匙错误");
			}
		}else if("d"==intro){
			if(r==451654){
				alert(num-r-1444444441);
			}else{
				alert("密匙错误");
			}
		}else if("e"==intro){
			if(r==571725){
				alert(num-r-1555555551);
			}else{
				alert("密匙错误");
			}
		}
	}else{
		alert("请重新输入!");
	}
}
</script>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
     <br>
     <p>电话回访-->使用任务列表-->任务列表明细&nbsp;&nbsp;<a href="/office/functions/usetasklist.jsp"> <font color="red">上一步</font></a></p>
    <p>当前任务：<font color="blue"><s:property value="taskName"/></font>&nbsp;&nbsp;<a href="javascript:taskComplete(${taskid })"><font color="red">回访任务完成</font></a></p>
     <s:form action="searchVisits" namespace="/telvisit" method="post"  theme="simple">
     		<s:hidden name="taskid"></s:hidden>
     		<s:hidden name="taskName"></s:hidden>
		   	<table border="0" style="width:1005px" cellpadding="0" cellspacing="0" align="center">
		   		
		   		<tr align="left">
		   			<td width="60px" style="text-align:right">
		    			锁定与否:
		    		</td>
		    		<td width="60px"><s:select cssStyle="width:60px" list="#{'-1':'全部','0':'否','1':'是'}" name="islock" listKey="key" listValue="value" emptyOption="false"></s:select></td>
		    		<td width="70px" style="text-align:right">
		    			会员账号:
		    		</td>
		    		<td width="150px"><s:textfield name="loginname"></s:textfield></td>
		    		<td colspan="5" width="600px">
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
		    		<td width="60px" style="text-align:right">
		    			推荐码:
		    		</td>
		    		<td width="60px"><s:textfield name="intro"></s:textfield></td>
		    		<td width="130px;">
					座机号1：
					<select onchange="changeQyExten(this.value);">
						<c:if test="${sessionScope.operator.authority eq 'boss' }">
							<option value="801" selected="selected">
								801
							</option>
							<option value="802" selected="selected">
								802
							</option>
						</c:if>
						<option value="803" selected="selected">
							803
						</option>
						<option value="804">
							804
						</option>
						<option value="805">
							805
						</option>
						<option value="806">
							806
						</option>
						<option value="807">
							807
						</option>
						<option value="808">
							808
						</option>
						<option value="809">
							809
						</option>
						<option value="810">
							810
						</option>
						</select>
					</td>
					<td>
					座机号2：
					<select onchange="changeOtherExten(this.value);">
						<c:if test="${sessionScope.operator.authority eq 'boss' }">
							<option value="8001" selected="selected">
								8001
							</option>
							<option value="8002" selected="selected">
								8002
							</option>
						</c:if>
						<option value="8003" selected="selected">
							8003
						</option>
						<option value="8004">
							8004
						</option>
						<option value="8005">
							8005
						</option>
						<option value="8006">
							8006
						</option>
						<option value="8007">
							8007
						</option>
						<option value="8008">
							8008
						</option>
						<option value="8009">
							8009
						</option>
						<option value="8010">
							8010
						</option>
					</select>
					&nbsp;&nbsp;
				</td>
		    	</tr>
		 
		    </table>
	    </s:form>
    <div align="center" style="width:100%">
    	<s:if test="pageList!=null">
    		<display:table name="pageList" id="fc" requestURI="/telvisit/searchVisits.do" style="width:1050px" decorator="dfh.displaytag.util.TelvisitFormat">
				<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
		    	<display:column property="loginname" title="会员账号" style="text-align:left"></display:column>
		    	<display:column property="accountname" title="真实姓名" style="text-align:left"></display:column>
		    	<display:column property="phone" title="电话号码" style="text-align:left" ></display:column>
		    	<display:column property="qq" title="qq" style="text-align:left"></display:column>
		    	<display:column property="email" title="邮件地址" style="text-align:left"></display:column>
		    	<display:column property="intro" title="推荐码" style="text-align:left"></display:column>
		    	<display:column property="createtime" title="注册时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
		    	<display:column property="lastlogintime" title="最后登录时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
		    	<display:column property="intervaltime" title="登录间隔天数" style="text-align:left"></display:column>
		    	<display:column property="execstatus" title="访问结果" style="text-align:center"></display:column>
		    	<display:column property="islock" title="锁定" style="text-align:center"></display:column>
		    	<display:column property="cz" title="操  作" style="text-align:right"></display:column>
		    </display:table>
		</s:if>
    </div>
    <br/><br/><br/>
  </body>
</html>
