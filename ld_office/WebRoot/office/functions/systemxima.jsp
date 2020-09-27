<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'systemxima.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<style type="text/css">
		body{
			font-size:12px;
		}
		table,input{
			font-size:12px;
		}
		
		.runtimeMessageStyle {
			height: 300px;
			width: 100%;
			overflow:auto;
		}
	</style>
	<script type="text/javascript" >

		// 请求反水控制器的唯一入口
		function requestServer(url,responseMethod){
			//alert(url);
	  		var xmlhttp = new Ajax.Request(    
		        url,
		        {    
		            method: 'post', 
		            parameters: 'r='+Math.random(),    
		            onComplete: responseMethod  
		        }
	    	);
	    }

	    // 反水命令的执行状态
		function responseMessage(resdata){
	  		if(resdata.readyState==4&&resdata.status>=200){
	  			$("message1").innerHTML=resdata.responseText;
	  		}
	  	}

		// 系统消息回调函数
		function responseRunTimeMessage(resdata){
	  		if(resdata.readyState==4&&resdata.status>=200){
	  			var msg=resdata.responseText;
		  		if(msg.indexOf("end")!=-1){
		  			window.clearTimeout(timeid);
		  			$("message2").innerHTML="任务完成\ 、系统消息传输完成";
		  			$('getmessage_btn').disabled=false;
		  		}else{
		  			$("runtimeMessage").innerHTML=msg;
		  		}
	  			
	  		}
	  	}

		// 获取系统消息
		var timeid;
		var i=1;
		var getXimaMessageURL="/xima/getXimaMessage.do?";
	  	function getRuntimeMeesage(){
	  		$('getmessage_btn').disabled=true;
	  		$("message2").innerHTML=i++;
	  		requestServer(getXimaMessageURL,responseRunTimeMessage);
	  		timeid=window.setTimeout(getRuntimeMeesage,1000);
	  	}

		// 执行反水相关命令
		function exec(url,eleObject){
			i=1;
		  	$('operatorLog').innerHTML=$('operatorLog').innerHTML+eleObject.value+"-->";
	  		requestServer(url,responseMessage);
	  		getRuntimeMeesage();
	  	}

		// 记录操作日志
	  	function operatorLog(eleObject){
	  		$('operatorLog').innerHTML=$('operatorLog').innerHTML+eleObject.value+"-->";
	  	}


		// 停止传输系统消息
	  	function stopMessage(){
	  		window.clearTimeout(timeid);
	  		$('getmessage_btn').disabled=false;
	  	}
	  	

	    // 清除页面消息
	  function clearMessage(){
		  	i=1;
			$("message1").innerHTML="";
			$("message2").innerHTML="";
			$("operatorLog").innerHTML="";
			$("runtimeMessage").innerHTML="";
	  }

	  
		
	</script>
  </head>
  
  <body style="background:#b6d9e4;">
    结算  --> 系统洗码
    
    <s:url action="getXimaMessage" namespace="/xima" var="getXimaMessageUrl"></s:url>
    <s:url action="addXimaProposal" namespace="/xima" var="addXimaProposalUrl"></s:url>
    <s:url action="execXimaProposal" namespace="/xima" var="execXimaProposalUrl"></s:url>
    <s:url action="updateXimaProposal" namespace="/xima" var="updateXimaProposalUrl"></s:url>
    <s:url action="checkXimaProposal" namespace="/xima" var="checkXimaProposalUrl"></s:url>
    <s:url action="recoverStatus" namespace="/xima" var="recoverStatuslUrl"></s:url>
    
    <table border="1" cellpadding="0" cellspacing="0" width="80%" align="center">
    	<tr>
    		<td height="20px">&nbsp;</td>
    	</tr>
    	<tr align="center">
    		<th>
    			<input type="button" value="1.检测未执行的洗码提案" onclick="requestServer('${checkXimaProposalUrl }', responseMessage);operatorLog(this)">
    			<input type="button" value="2.提交提案" onclick="exec('${addXimaProposalUrl }', this)">
    			<input type="button" value="3.执行提案" onclick="exec('${execXimaProposalUrl }', this)">
    			<input type="button" value="4.更新投注记录状态" onclick="exec('${updateXimaProposalUrl }', this)">
    			<br/>
    			<input type="button" value="5.获取洗码过程中产生的消息" onclick="getRuntimeMeesage()" id="getmessage_btn">
    			<input type="button" value="6.清除页面消息" onclick="clearMessage()">
    			<input type="button" value="7.停止获取消息" onclick="stopMessage()">
    			<input type="button" value="8.将控制器恢复到初始状态" onclick="exec('${recoverStatuslUrl }', this)">
    			
    		</th>
    	</tr>
    	<tr>
    		<td height="20px">&nbsp;</td>
    	</tr>
    	<tr>
    		<td><font color="blue">基本状态：</font><div id="message1"></div></td>
    	</tr>
    	<tr>
    		<td><font color="blue">消息状态：</font><div id="message2"></div></td>
    	</tr>
    	<tr>
    		<td><font color="blue">操作记录：</font><div id="operatorLog"></div></td>
    	</tr>
    	
    	<tr>
    		<td >
    			<font color="blue">系统消息：</font>
    			<div id="runtimeMessage" class="runtimeMessageStyle">
    				
    			</div>
    		</td>
    	</tr>
    </table>
  </body>
</html>
