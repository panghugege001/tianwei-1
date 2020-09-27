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
    <body>
        <input id="payUrl" type="hidden" value="${message.data.url}">
    </body>
<script src="/js/jquery18.js" type="text/javascript"></script>
<script>
    $(function() {
        var url = $("#payUrl").val();
        window.location.href = url;
    });

</script>
</html>