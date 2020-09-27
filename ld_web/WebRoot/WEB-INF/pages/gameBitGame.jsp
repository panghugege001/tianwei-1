<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<html>
<head>
<script type="text/javascript" src="${ctx}/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
$(function(){
	window.location.href = '<s:property value="gameUrl" escapeHtml="false"/>'; 
});
</script>
</head>
<body>
</body>
</html>