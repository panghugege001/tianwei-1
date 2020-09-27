<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=request.getRequestURL()%>" />
		<script type="text/javascript">
if(top.location != self.location)
{
	top.location = self.location;
}
</script>

		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>

	</head>

	<body>
	<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		<div class="container pt20" style="min-height: 600px">
			<div class="m-content">
				<h2 class="tit"><s:property value="#request.ann.title"/>  <em><s:property value="#request.ann.createtime"/></em></h2>
				<div>
					${ann.content}
				</div>
			</div>
		</div>

	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	</body>
</html>