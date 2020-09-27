<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<jsp:include page="/title.jsp"></jsp:include>
<script type="text/javascript">
	window.onload=function(){
		document.getElementById("iframegpi").src="${gameUrl}";
		//location.href = "${gameUrl}";
	}
</script>
<body style="padding:0;margin:0;">
	<iframe id="iframegpi" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
</body>
</html>