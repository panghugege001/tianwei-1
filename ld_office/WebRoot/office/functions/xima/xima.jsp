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
    			<input type="button" value="1.检测未执行的洗码提案" onclick="requestServer('${checkXimaProposalUrl }', responseMessage);operatorLog(this)" style="display: none;">
    			<s:form action="execXimaProposal" namespace="/batchxima" method ="post" id="fromexecXimaProposal">
    				<input type="button" value="3.执行提案"  onclick="execBottun(this)">
    			</s:form>
    			<input type="button" value="4.通知客户" onclick="exec('${sendmessageUrl }', this)" style="display: none;">
    			<br/>
    			
    			
    		</th>
    	</tr>
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addBatchSixXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(ea)" />
<s:hidden name="rate" value="0.01"></s:hidden>
</s:form>
    	
    	</td></tr>
    	<!-- 
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addBatchTwoXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="2提交1.5%提案(ea)" />
 <s:hidden name="rate" value="0.015"></s:hidden>
</s:form>
    	
    	</td></tr>
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addBatchFourXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="2提交1.2%提案(ea)" />
  <s:hidden name="rate" value="0.012"></s:hidden>
</s:form>
    	
    	</td></tr> -->
    	
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addAGBatchSixXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(ag,agin,bbin)" />
  <s:hidden name="rate" value="0.02"></s:hidden>
</s:form>
    	
    	</td></tr>
    	
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addAGBatchSixXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(SB)" />
  <s:hidden name="rate" value="0.04"></s:hidden>
</s:form>
    	
    	</td></tr>
    	 
    	<%-- <tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addPtBatchSixXimaProposal" namespace="/batchxima" method ="post" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(PT)" />
        <s:hidden name="rate" value="0.05"></s:hidden>
        </s:form>
    	</td></tr>  --%>
    	
    	<%-- <tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addNewPtBatchSixXimaProposal" namespace="/batchxima" method ="post" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(NEWPT)" />
        </s:form>
    	</td></tr>  --%>
    	
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addNewPtBatchXimaProposalXXX" namespace="/batchxima" method ="post" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="提交提案(NEWPT最新)" />
        <s:hidden name="rate" value="0.05"></s:hidden>
        </s:form>
    	</td></tr> 
    	
    	<!-- 
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addAGBatchTwoXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="2提交1.5%提案(ag,bbin)" />
  <s:hidden name="rate" value="0.035"></s:hidden>
</s:form>
    	
    	</td></tr>
    	<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addAGBatchFourXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="2提交1.2%提案(ag,bbin)" />
  <s:hidden name="rate" value="0.032"></s:hidden>
</s:form>
    	
    	</td></tr> -->
    	
    	<tr align="center"><td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addAGBatchSixXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
		文件:<input type="file" id="file7" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(EBET)" />
		  <s:hidden name="rate" value="0.07"></s:hidden>
		</s:form>
		
		<tr align="center"><td align="center" height="40px">
    	<p align="left"><s:fielderror/></p>
    	<s:form action="addAGBatchSixXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
		文件:<input type="file" id="file7" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(TTG)" />
		<s:hidden name="rate" value="0.09"></s:hidden>
		</s:form>
    	
    	</td></tr>
    	
    	<tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addFishXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					文件:<input type="file" id="filefish" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(AGINFISH)" />
				</s:form>
    		</td>
    	</tr>

        <tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addAginSlotXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					文件:<input type="file" id="filefish" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(AGINSLOT)" />
				</s:form>
    		</td>
    	</tr>
    	
    	<tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addPNGBatchXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					文件:<input type="file" id="filepng" name="myFile"  size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(PNG)" />
				</s:form>
    		</td>
    	</tr>
    		    <%-- <tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addSBABatchXimaProposalExcel" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					文件:<input type="file" id="filepng" name="myFile"  size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(SBA)" />
				</s:form>
    		</td>
    	</tr> --%>
    	<tr align="center">
	    	<td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addSBABatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(SBA)" />
		  			<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
	    	</td>
	    </tr>
	    <tr align="center">
	    	<td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addMWGBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(MWG)" />
		  			<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
	    	</td>
	    </tr>
		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addBbinBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(bbin电子)" />
					<s:hidden name="gamekind" value="5"></s:hidden>
				</s:form>
			</td>
		</tr>
		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addBbinBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(bbin真人)" />
					<s:hidden name="gamekind" value="3"></s:hidden>
				</s:form>
			</td>
		</tr>
		<tr align="center">
	    	<td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addQTBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(QT)" />
		  			<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
	    	</td>
	    </tr>

		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addEBetAppBatchXimaProposal" namespace="/batchxima" method ="post" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(EBetApp)" />
					<s:hidden name="rate" value="0.12"></s:hidden>
				</s:form>
			</td>
		</tr>


	    <%-- <tr align="center"><td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addAGBatchSixXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
		文件:<input type="file" id="file7" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(QT)" />
		  <s:hidden name="rate" value="0.11"></s:hidden>
		</s:form></td></tr> --%>

	    <tr align="center">
    		<td align="center" height="40px">
    			<p align="left"><s:fielderror/></p>
    			<s:form action="addAutoAginSlotXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(Agin Slot)" />
				</s:form>
    		</td>
    	</tr>
    	
	    
	    <tr align="center">
	    	<td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addNTBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案( NT老虎机 )" />
		  			<s:hidden name="rate" value="0.1"></s:hidden>
				</s:form>
	    	</td>
	    </tr>
		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addFanyaBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(FANYA)" />
					<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
			</td>
		</tr>
	    
   		</tr>
	    	    <tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addDtBatchXimaProposal" namespace="/batchxima" method ="post" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(DT)" />
					<s:hidden name="rate" value="0.12"></s:hidden>
				</s:form>
			</td>
		</tr> 
	    
	    
	    <tr align="center">
	    	<td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="addNTwoBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案( N2Live )" />
		  			<s:hidden name="rate" value="0.1"></s:hidden>
				</s:form>
	    	</td>
	    </tr>
		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addKyqpBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(开元棋牌)" />
				</s:form>
			</td>
		</tr>

		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addVRBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(VR官方彩)" />
					<s:hidden name="gamekind" value="1"></s:hidden>
				</s:form>
			</td>
		</tr>

		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addVRBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(VR彩)" />
					<s:hidden name="gamekind" value="2"></s:hidden>
				</s:form>
			</td>
		</tr>
	    
	    <tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addMGSBatchXimaProposal" namespace="/batchxima" method ="post" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(MG)" />
					<s:hidden name="rate" value="0.12"></s:hidden>
				</s:form>
			</td>
		</tr>
		<tr align="center">
	    	<td align="center" height="40px">
		    	<p align="left"><s:fielderror/></p>
		    	<s:form action="add761BatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(761)" />
		  			<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
	    	</td>
	    </tr>
		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addPBBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(平博体育)" />
					<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
			</td>
		</tr>
		<tr align="center"><td align="center" height="40px">
			<p align="left"><s:fielderror/></p>
			<s:form action="addNTwoBatchBaobiaoXimaProposal" namespace="/batchxima" method ="post" enctype="multipart/form-data">
				文件:<input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(N2Live报表)" />
				<s:hidden name="rate" value="0.01"></s:hidden>
			</s:form>

		</td></tr>

		<tr align="center">
			<td align="center" height="40px">
				<p align="left"><s:fielderror/></p>
				<s:form action="addBITBatchXimaProposal" namespace="/batchxima" method ="post">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交提案(比特游戏)" />
					<s:hidden name="rate" value="0.11"></s:hidden>
				</s:form>
			</td>
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
    <c:import url="/office/script.jsp" />
  </body>
</html>
