<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/pages/taglibs.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>支付页面</title>
</head>
	<script type="text/javascript">
		window.onload=function(){
			document.getElementById("iframegpi").src="${message.data.url}";
			location.href = "${message.data.url}";
		}
	</script>
<body style="padding:0;margin:0;">
	<iframe id="iframegpi" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
</body>
</html>