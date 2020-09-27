<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@taglib uri="/struts-tags" prefix="s"%>

<div class="m-content">
	<%--<h3>您的推广网址为： <a class="c-red" href="${session.customer.referWebsite}">${session.customer.referWebsite}</a></h3>--%>
	<ul>
		<s:iterator var="fc" value="%{#request.addrList}" status="st">
			<li>推广网址<s:property value='#st.index+1'/>：
				<a href="<s:property value="#fc"/>" class="c-blue"><s:property value="#fc"/></a> </li>
		</s:iterator>
	</ul>
</div>
