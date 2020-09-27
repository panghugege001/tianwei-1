<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>支付页面</title>
</head>
<body onLoad="document.payForm.submit();">

<form name="payForm" method="post" action="<%=request.getParameter("apiUrl")%>">
    <%
        Map<String, String[]> pays = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : pays.entrySet()) {
            if (entry.getKey().equals("apiUrl")) {
                continue;
            }
    %>
    <input type="hidden" name="<%=entry.getKey()%>" value="<%=entry.getValue()[0]%>">
    <%
        }
    %>
</form>

</body>
</html>