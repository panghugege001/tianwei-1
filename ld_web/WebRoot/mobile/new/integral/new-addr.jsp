<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<!DOCTYPE >
<html>

<head>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
    <meta name="full-screen" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <!--<link rel="stylesheet" href="/mobile/css/iconfont/iconfont.css?v=4">-->
    <link rel="stylesheet" href="/mobile/css/common.css?v=10">
    <link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_226486_fqtbqjfka66iggb9.css" />
    <link href="css/index.min.css?v=9999" rel="stylesheet">
    <link rel="stylesheet" href="css/LArea.css">
</head>

<body class="new-addr-bg">
<header class="common-header j-common-header" id="j-common-header">
<i class="back iconfont icon-arrow-right" onclick="history.back()"></i>
    <div class="header-title">编辑地址</div>
    <a class="delete-tit" href="javascript:;">删除</a>
</header>

<div class="containers">
    <div class="new-addr">

        <div class="input-group">
            <label for="name">收货人：</label>
            <input type="text" id="name"/>
        </div>

        <div class="input-group">
            <label for="phone">手机号码：</label>
            <input maxlength="11" type="text" id="phone"/>
        </div>

        <div class="input-group">
            <label for="area">所在地区：</label>
            <input type="text" id="area"/>
            <input id="area-value" type="hidden"/>
            <i class="iconfont icon-jiantou"></i>
        </div>

        <div class="input-group">
            <label for="address">详细地址：</label>
            <input type="text" id="address" placeholder="街道、楼牌号等"/>
        </div>

        <div class="input-group">
            <label for="flag">设为默认地址：</label>
            <input type="checkbox" id="flag"/>
        </div>

        <a class="btnClass fixed save-addr-btn" href="javascript:;">保存</a>
    </div>
</div>

<!--登录状态-->
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="js/integral-common.js?v=333"></script>
<script src="js/LAreaData2.js"></script>
<script src="js/LArea.js"></script>
<script src="js/new-addr.js?v=333"></script>


</body>

</html>