<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<html>
<head>
	<jsp:include page="/title.jsp"></jsp:include>
	<script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
	  var eaLoginUrl="${ebetLoginUrl}";
	  if(eaLoginUrl!=""){
	      window.location.href=eaLoginUrl;
	  }
  });
</script>
<body>
</body>
</html>