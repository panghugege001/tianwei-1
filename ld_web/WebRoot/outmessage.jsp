<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty errormsg}">
	<script type="text/javascript">
		alert("<c:out value="${errormsg}"/>");
		 
	</script>
</c:if>


