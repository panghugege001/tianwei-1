<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<html>
<head>
	<jsp:include page="/title.jsp"></jsp:include>
	<script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
	  var userId = '${session.customer.loginname}';
	  var accessToken = '${session.accessToken}';
	  var channelId = '${channelId}';

	  if(userId == '' || accessToken == '' || channelId == ''){
		  alert('请您从平台登录！');
		  window.location.href="/";
		  return;
	  }
	  window.location.href="http://lh.ebet2022.com/h5/odogif?username=" + userId + "&accessToken=" + accessToken;

  });
</script>
</head>
<body>
<div>系统加载中，请稍候</div>
</body>
</html>