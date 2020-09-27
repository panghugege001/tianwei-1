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
    <style>
        body{
            background:#f9f5ec;
        }
        .preserve{
            text-align: center;
            padding-top: 150px;
        }
        .preserve img{
            width:90%;
        }

        .preserve h2 {
            color: #666;
            font-size: 20px;
            margin: 60px 0 20px;
        }

        .preserve p {
            color: #666;
            font-size: 16px;
        }

    </style>
</head>

<body>
<header class="common-header j-common-header" id="j-common-header">
<i class="back iconfont icon-arrow-right" onclick="history.back()"></i>
    <div class="header-title">积分商城</div>
</header>
<div class="preserve">
    <img src="img/preserve.jpg" alt="">
    <h2>
        积分商城临时维护中
    </h2>
    <p>
        请您耐心等待一下，精美礼品即将开启兑换
    </p>
</div>

<script>
    // 返回处理
    var mobileorApp = window.location.href;
    var $back = $('#j-common-header a');
    if (mobileorApp.indexOf('openMobile') === -1) {//app
        window.localStorage.__platform__ = 'app';
        $back.attr('href', 'javascript:goBackApp();')
    }else{//webapp
        window.localStorage.__platform__ = 'webapp';
    }

    // 跳app
    function goBackApp() {
        if (getMobileKind() == 'Android') {
            window.location.href = 'lehuwebapp://Home'
        } else {
            window.location.href = 'lehuwebapp://Home'
        }
    }
    // 判断设备
    function getMobileKind() {
        if (navigator.userAgent.match(/Android/i))
            return 'Android';
        if (navigator.userAgent.match(/iPhone/i) ||
            navigator.userAgent.match(/iPad/i) ||
            navigator.userAgent.match(/iPod/i))
            return 'IOS';
        if (navigator.userAgent.match(/Windows Phone/i))
            return 'Windows Phone';
        return 'other';
    }
</script>
</body>

</html>