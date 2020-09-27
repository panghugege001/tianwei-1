<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<ul>
	<s:iterator var="fcg" value="%{#request.list}"
				status="st">
		<li class="item"><a href="javascript:;"><s:property value="#fcg.title" /> <span class="fr"><s:property value="#fcg.createtime"/></span>
			<div style="display: none;" class="content"><s:property value="#fcg.content" escapeHtml="false"/></div>
		</a></li>

	</s:iterator>
</ul>


						
					
					