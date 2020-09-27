<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Redis缓存管理页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <jsp:include page="/pages/common.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/pages/navbar.jsp"></jsp:include>
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">Redis缓存管理</h3>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-body">
                <button type="button" class="btn btn-default" onclick="clear()">清除所有Redis缓存</button>
                <button type="button" class="btn btn-default" onclick="clearByKey('dictionary')">清除字典项缓存</button>
                <button type="button" class="btn btn-default" onclick="clearByKey('merchantpay')">清除支付平台配置缓存</button>
            </div>
        </div>
    </div>
</div>
<div id="message" class="alert alert-success" role="alert"></div>

<script>
    function clear() {
        $.get("/redis/clear", function (data) {
            $("#message").html("清除成功...");
        });
    }

    function clearByKey(key) {
        $.ajax({
            type: "POST",
            url: '/redis/clearByKey',
            data: {key: key},
            success: function (data) {
                $("#message").html("key:" + key + ",清除成功...");
            }
        });
    }

</script>
</body>
</html>
