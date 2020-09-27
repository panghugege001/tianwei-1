<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/common.inc"%>
<s:if test="#request.errormsg==null">
	<s:set var="errormsg" value="#parameters.errormsg" scope="request"></s:set>
</s:if>
<s:if test="#request.errormsg!=null">
		<script type="text/javascript">
		alert("<s:property value='#request.errormsg'/>");
	</script>
</s:if>
