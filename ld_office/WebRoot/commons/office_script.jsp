<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/common.inc" %>
<c:if test="${not empty errormsg}">
	<script type="text/javascript">
		alert("<c:out value="${errormsg}"/>");
	</script>
</c:if>
