<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>民生银行开始处理订单</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	 <style type="text/css">
	<!--
	.STYLE1 {
		font-size: xx-large;
		font-weight: bold;
	}
	.STYLE2 {font-size: 16px}
	.STYLE3 {
		color: #FF0000;
		font-size: 14px;
	}
	-->
	</style>
    <script type="text/javascript" src="js/msBank.js"></script>
    <script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript">
    
    function send(){
   		 $.ajax({
			url:'process.do',
			type:'post',
			data:{"num":1},
			success:function(reData){
				if(reData){
					if(reData.again =="true"){
					 	send();
					 	return;
					}
					if(reData.netError =="true"){
						$("#info").html("由于网络原因，您现在无法执行该交易！请重新登录！");
						return;
					}
					var txcode = $("input[name='txcode']:hidden",$(reData.infos)).val();
					var txSerNo =$("input[name='txSerNo']:hidden",$(reData.infos)).val();
					var CommDN = $("input[name='CommDN']:hidden",$(reData.infos)).val();
					var CommObject = $("input[name='CommObject']:hidden",$(reData.infos)).val();
					var CommValue = $("input[name='CommValue']:hidden",$(reData.infos)).val();
					var ran = $("input[name='ran']:hidden",$(reData.infos)).val();
					var space= $("input[name='space']:hidden",$(reData.infos)).val();
					$("input[name='txcode']:hidden",$("body")).val(txcode);
					$("input[name='txSerNo']:hidden",$("body")).val(txSerNo);
					$("input[name='CommDN']:hidden",$("body")).val(CommDN);
					$("input[name='CommObject']:hidden",$("body")).val(CommObject);
					$("input[name='CommValue']:hidden",$("body")).val(CommValue);
					$("input[name='ran']:hidden",$("body")).val(ran);
					$("input[name='space']:hidden",$("body")).val(space);
					DigitalSigMsg();
					var DSData = $("input[name='DSData']:hidden",$("body")).val();
						$.ajax({
							url:'process.do',
							data:{'num':2,'txcode':txcode,'txSerNo':txSerNo,'CommDN':CommDN,'CommObject':CommObject,
							 'CommValue':$("input[name='CommValue']:hidden",$("body")).val(),'ran':ran,'space':space,
							 'DSData':DSData,'password':$("input[name='password']:hidden",$("body")).val()
							},
							success:function(data){
								if ("true" == data.success) {
									$("#info").html("订单处理中....");
									send();
								}else if(data.length>0){
									$("#faliedNum").html(""+data[0].faliedNum);
									$("#info").html(""+data[1].errorInfo);
									send();
								}
							},
							error:function(){
								 send();
							},
							type:'post',
							dataType:'json'
						});
					}
			},
//			async:false,
			dataType:'json'
		})
	}
	$(function(){
		send();
	})
	//window.setInterval("send();",2000);
	
  </script>

  </head>
  
 <body>
  	<p align="center" class="STYLE1">民生银行基本信息</p>
	<table width="513" height="135" border="1" align="center" cellpadding="0" cellspacing="0">
	  <tr>
	    <td width="171"><div align="right" class="STYLE2">姓名：</div></td>
	    <td width="336" align="center">${sessionScope.MsSession.username}</td>
	  </tr>
	  <tr>
	    <td><div align="right" class="STYLE2">卡号：</div></td>
	    <td align="center">${sessionScope.MsSession.userAccount}</td>
	  </tr>
	  <tr>
	    <td><div align="right" class="STYLE2">失败次数：</div></td>
	    <td id="faliedNum" align="center">0</td>
	  </tr>
	</table>
	<p align="center"><span class="STYLE3"  id="info"><strong>订单处理中....</strong></span></p><br/>
	<p align="center"><input type="button" value="返回" onclick="javasript:window.location.href='index.html'"></p>
 	<input type="hidden" name="txcode" value=""/>
	<input type="hidden" name="txSerNo" value=""/>
    <input type="hidden" name="space" value="&nbsp;"/>
	<input type="hidden" name="CommDN" value=""/>
	<input type="hidden" name="CommObject" value=""/>
	<input type="hidden" name="CommValue" value=""/>
	<input name="DSData" type="hidden" value=""/>
	<input name="ran" type="hidden" value=""/>
	<object id="CryptoAgency1" classid="clsid:E110BC2B-C768-4c9c-87EB-3A2228F7C4BF" style="visibility: hidden;" VIEWASTEXT=""/>
  </body>
</html>
