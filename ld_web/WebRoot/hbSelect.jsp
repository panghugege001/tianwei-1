<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
	<option value="" selected="selected"> 请选择 </option>
<s:iterator var="fc" value="%{#request.list}" status="st">
	<option value="<s:property value='#fc.id'/>"><s:property value='#fc.title'/></option>
</s:iterator>
