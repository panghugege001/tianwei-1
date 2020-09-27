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
<c:if test="${message.code != '10000'}">
    <body>
    <h3 id="massage" data-code="${message.code}">${message.desc}</h3>
    </body>
</c:if>
<c:if test="${message.code == '10000'}">
    <body onLoad="document.payForm.submit();">
    <form name="payForm" method="post" action="${message.data.url}">
        <c:forEach items="${message.data.params}" var="item">
            <input type="hidden" name="${item.key}" value="${item.value}">
        </c:forEach>
    </form>
    </body>
</c:if>
</html>