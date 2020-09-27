<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>积分商城</title>

    <link rel="stylesheet" type="text/css" href="/css/default.css?v=888"/>
    <script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
    <style>

        .preserve {
            background: #f9f5ec;
            height: 408px;
            text-align: center;
            padding: 100px 0;
        }

        .preserve h2 {
            color: #666;
            font-size: 26px;
            margin: 60px 0 20px;
        }

        .preserve p {
            color: #666;
            font-size: 16px;
        } 
    </style>
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
<div class="preserve">
    <img src="img/preserve.jpg" alt="">
    <h2>
        积分商城临时维护中
    </h2>
    <p>
        请您耐心等待一下，精美礼品即将开启兑换
    </p>
</div>

<jsp:include page="/footer.jsp"></jsp:include>

</body>

</html>