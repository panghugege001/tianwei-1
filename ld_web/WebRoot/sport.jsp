<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>

</head>
<body>

<div class="index-bg" style="background: #464646; height: 100%;" >
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
	<div class="centent" style="width: 100%; height: 100%; margin-top:30px ;">
		<iframe style="width: 100%; min-height: 1000px;" src="/asp/sbLogin.aspx"></iframe>
	</div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

</body>
</html>