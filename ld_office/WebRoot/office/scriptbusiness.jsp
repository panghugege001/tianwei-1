<%@page errorPage="500.jsp" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<!-- 用于统计脚本,800脚本等等 -->
<c:if test="${not empty errormsg}">
	<script type="text/javascript">
		alert("<c:out value="${errormsg}"/>");
		window.close();
	</script>
</c:if>
