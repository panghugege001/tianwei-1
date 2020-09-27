<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.model.HdImage"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	response.setHeader("pragma", "no-cache");
	response.setHeader("cache-control", "no-cache");
	response.setDateHeader("expires", 0);

	List<HdImage> listImage = AxisSecurityEncryptUtil.queryImage();
	if (listImage == null) {
		listImage = new ArrayList();
	}
	request.setAttribute("listImage", listImage);
%>
<s:iterator value="#request.listImage" var="img">
	<c:if test="${img.image001!=null && img.image001!=''}">
		<H4 class=demo6>
			<img src="${img.image002}" />
		</H4>
		<DIV class=panel>
			<img src="${img.image003}" />
		</DIV>
		<br />
	</c:if>
</s:iterator>


