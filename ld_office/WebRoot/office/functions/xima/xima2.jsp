<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/office/include.jsp" %>
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
	<script type="text/javascript" src="/scripts/someUserfulFunc.js"></script>
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
		  	eleObject.disabled=true;
	  		requestServer(url,responseMessage);
	  		getRuntimeMeesage();
	  	}

		function execBottun(eleObject){
	  	    $('fromexecXimaProposal').submit();
		  	eleObject.disabled=true;
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
   操作  --> 系统洗码
    
    <s:url action="getXimaMessage" namespace="/xima" var="getXimaMessageUrl"></s:url>
    <s:url action="addXimaProposal" namespace="/xima" var="addXimaProposalUrl"></s:url>
    <s:url action="execXimaProposal" namespace="/batchxima" var="execXimaProposalUrl"></s:url>
    <s:url action="sendmessage" namespace="/batchxima" var="sendmessageUrl"></s:url>
    <s:url action="updateXimaProposal" namespace="/xima" var="updateXimaProposalUrl"></s:url>
    <s:url action="checkXimaProposal" namespace="/batchxima" var="checkXimaProposalUrl"></s:url>
    <s:url action="recoverStatus" namespace="/xima" var="recoverStatuslUrl"></s:url>
    
    <table border="1" cellpadding="0" cellspacing="0" width="80%" align="center">
    	<tr>
    		<td height="20px">&nbsp;</td>
    	</tr>
    	<tr align="center">
    		<th>
    			<input type="button" value="1.检测未执行的洗码提案" onclick="requestServer('${checkXimaProposalUrl }', responseMessage);operatorLog(this)" style="display:none;">
    			<s:form action="execXimaProposal" namespace="/batchxima" method ="post" id="fromexecXimaProposal" >
    				<input type="button" value="3.执行提案" onclick="execBottun(this)">
    			</s:form>
    			<input type="button" value="4.通知客户" onclick="exec('${sendmessageUrl }', this)" style="display:none;">
    			<br/>
    			
    		</th>
    	</tr>
   
    	
    	<tr align="center"><td align="center" height="40px">
    		
    	<s:form action="addNewPtSkyBatchXimaProposal" namespace="/batchxima" method ="post" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="提交提案(PTSKY)" />
        </s:form>
    		
    	</td>
    	</tr> 
    	
    	<tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addSwFishBatchXimaProposalExcel" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					文件:<input type="file" id="filefish" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(SWFISH报表)" />
				</s:form>
    		</td>
    	</tr>
		<tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addDTFishBatchXimaProposalExcel" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					文件:<input type="file" id="filefish" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(HYG报表)" />
				</s:form>
    		</td>
    	</tr>
    	
		<tr align="center"><td align="center" height="40px">

			<s:form action="addNewDTBatchXimaProposal" namespace="/batchxima" method ="post" >
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" value="提交提案(SWFISH)" />
			</s:form>

		</td>
		</tr>

		<tr align="center">
    		<td align="center" height="40px">
	    		<s:form action="addNewDTBatchXimaProposal" namespace="/batchxima" method ="post" >
	            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            <input type="submit" value="提交提案(DT)" />
	       	 	</s:form>
    		</td>
    	</tr> 
    	
    	<tr align="center">
			<td align="center" height="40px"><s:form
					action="addNewNTBatchXimaProposal" namespace="/batchxima"
					method="post">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="提交提案(NT)" />
				</s:form></td>
		</tr>

		<tr align="center">
			<td align="center" height="40px"><s:form
					action="addNewMGSBatchXimaProposal" namespace="/batchxima"
					method="post">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="提交提案(MG)" />
				</s:form></td>
		</tr>

		<tr align="center">
			<td align="center" height="40px">
				<s:form action="addBbinBatchXimaBaobiaoProposal" namespace="/batchxima" method ="post"  enctype="multipart/form-data">
					文件:<input type="file" id="filefish" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;
					<select name="gamekind">
						<option value="3">真人</option>
						<option value="5">老虎机</option>
					</select>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="submit" value="提交提案(bbin)" />
				</s:form>
			</td>
		</tr>


		<tr align="center">
			<td align="center" height="40px"><s:form
					action="addPNGBatchXimaProposal_V1" namespace="/batchxima"
					method="post">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="提交提案(PNG)-12月22日开始使用" />
				</s:form></td>
		</tr>
		
		<tr align="center">
			<td align="center" height="40px"><s:form
					action="addQTBatchXimaProposal_V1" namespace="/batchxima"
					method="post">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="提交提案(QT)-12月22日开始使用" />
				</s:form></td>
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
    <c:import url="/office/script.jsp" />
  </body>
</html>
