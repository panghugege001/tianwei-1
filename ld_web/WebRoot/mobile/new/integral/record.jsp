<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<!DOCTYPE >
<html>

<head>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
    <meta name="full-screen" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link rel="stylesheet" href="/mobile/css/common.css?v=10">
    <link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_226486_fqtbqjfka66iggb9.css" />
    <link href="css/index.min.css?v=9999" rel="stylesheet">
    <link rel="stylesheet" href="css/pagination.css">
</head>

<body class="record-bg">
<header class="common-header j-common-header" id="j-common-header">
<i class="back iconfont icon-arrow-right" onclick="history.back()"></i>
    <div class="header-title">积分商城</div>
</header>


<div class="containers">
    <!--记录-->
    <section>
        <div class="data-container">
        </div>
        <div id="pagination-lottery-record"></div>
    </section>

</div>

<!--登录状态-->
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<!--<script src="/js/jquery-tmpl.js"></script>-->
<script src="/js/jquery.lazyload-v1.9.1.min.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="js/integral-common.js?v=333"></script>
<script src="js/pagination.min.js"></script>
<script src="js/record.js?v=333"></script>

</body>

</html>