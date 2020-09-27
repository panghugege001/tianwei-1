<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
    <title>同步论坛信息</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
	function synMemberInfo(){
		if(!window.confirm("确定吗？"))
			return;
		var loginname=$("loginname").value;
		if(loginname.length==0||loginname==""){
			alert("会员账号不能为空");
			return;
		}
		jQuery.post("/office/synchBbsUserInfo.do",{"loginname":loginname},function(data){
			alert(data);
		});
	}

	function callbackMethod(data){
		alert(data);
	}

	function $(ele){
		return document.getElementById(ele);
	}
</script>
<style type="text/css">
		body{
			font-size:12px;
			background:#b6d9e4;
		}
</style>
  </head>
  
  <body>
    <br/>
    <p>账户 --> 同步会员信息</p>
    <table border="0" align="center">
    	<tr>
    		<th>同步会员基本信息到一路发社区</th>
    	</tr>
    	<tr><td><hr/></td></tr>
    	<tr><td>会员账号：<input name="loginname" id="loginname" value=""/></td></tr>
    	<tr align="center"><td><input type="button" value="提交" onclick="synMemberInfo()"/></td></tr>
    </table>
  </body>
</html>
