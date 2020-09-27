<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="app.util.EffectiveDataUtil" %>

<%
		
	String id = request.getParameter("id");
	String name = request.getParameter("name");
	String typeNo = request.getParameter("typeNo");
	String itemNo = request.getParameter("itemNo");
	String flag = request.getParameter("flag");
	String headerKey = request.getParameter("headerKey");
	String headerValue = request.getParameter("headerValue");
	String width = request.getParameter("width");
	String defaultValue = request.getParameter("defaultValue");
	String removeKey = request.getParameter("removeKey");
	
	if (StringUtils.isEmpty(id)) {
		
		id = "type";
	}
	
	if (StringUtils.isEmpty(name)) {
		
		name = "type";
	}
	
	if (StringUtils.isEmpty(width)) {
		
		width = "100";
	}

	if (StringUtils.isEmpty(typeNo)) {
		
		typeNo = "type008";
	}
	
	if (StringUtils.isEmpty(flag)) {
		
		flag = "否";
	}
	
	String result = EffectiveDataUtil.querySystemConfigList(typeNo, itemNo, flag);
	
%>

<select id="select_1a2b3c4d"></select>

<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	
	$("#select_1a2b3c4d").empty();
	
	var headerKey = "<%=headerKey%>";
	var headerValue = "<%=headerValue%>";
	var id = "<%=id%>";
	var name = "<%=name%>";
	var defaultValue = "<%=defaultValue%>";
	var removeKey = "<%=removeKey%>";
	
	if (!isNull(headerValue)) {
	
		$("#select_1a2b3c4d").append("<option value='" + headerKey + "'>" + headerValue + "</option>");
	}
	
	var result = '<%=result%>';
	var arr = $.parseJSON(result);
	
	$.each(arr, function (index, o) {
		
		if (removeKey != o.itemNo) {
		
			$("#select_1a2b3c4d").append("<option value='" + o.itemNo + "'>" + o.value + "</option>");
		}
	});
	
	if (!isNull(defaultValue)) {
	
		$("#select_1a2b3c4d").attr("value", defaultValue);
	}
	
	$("#select_1a2b3c4d").attr('id', id).attr('name', name).css("width", "<%=width%>" + "px");
	
	function isNull (v) {
		
		if (v == null || v == "" || v == "null" || v == undefined) {
		
			return true;
		}
		
		return false;
	};
	
</script>