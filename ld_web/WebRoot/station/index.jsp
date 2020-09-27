<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-5-13
  Time: 下午12:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" scope="request" value="${pageContext.request.contextPath}"/><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>e68站内信</title>
</head>
<frameset rows="52,*,24" cols="*" framespacing="0" frameborder="no" border="0" name="main">
    <frame src="${ctx}/station/top.jsp" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" />
    <frame src="${ctx}/station/center.jsp" name="mainFrame" id="mainFrame" />
    <frame src="${ctx}/station/down.jsp" name="bottomFrame" scrolling="No" noresize="noresize" id="bottomFrame" />
</frameset>
<noframes><body>
</body>
</noframes></html>