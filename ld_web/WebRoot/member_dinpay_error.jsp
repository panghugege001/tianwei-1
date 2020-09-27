<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>支付错误页面</title>
	</head>
	<script>
 function error(){
    alert("${error_info}");
    window.close();
 }
</script>
	<body onload="error();">
	</body>
</html>
