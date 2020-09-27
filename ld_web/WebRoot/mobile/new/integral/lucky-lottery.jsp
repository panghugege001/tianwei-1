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
</head>

<body class="lottoery-bg">
<header class="common-header j-common-header" id="j-common-header">
<i class="back iconfont icon-arrow-right" onclick="history.back()"></i>
    <div class="header-title">积分抽奖</div>
</header>


<div class="containers">
    <div class="l-tit">
        <img src="img/font.png?V" alt="" />
    </div>

    <div class="lucky-wrapper">
        <div class="lucky-lottery clearfix">

            <div class="lotto-box">


            </div>

        </div>
    </div>
    <div class="l-rule">
        <c:if test="${session.customer!=null}">
            <p class="rule">抽奖规则：每次抽奖使用<span class="point-expend">1000</span>，每天抽奖次数不限。</p>
        </c:if>
    </div>
    <div class="user-record">
        <div>
            <div class="record-title">
                <div>
                    <i class="iconfont icon-jiangbei"></i>
                    <span>会员中奖信息</span>
                </div>
                <a href="javascript:;" class="lottery-record">我的抽奖记录<span class="iconfont icon-jiantou"></span></a>
            </div>
            <div class="list">
                <div class="bd">
                    <ul></ul>
                </div>
            </div>
        </div>
    </div>

</div>

<!--登录状态-->
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="/js/superslide.2.1.js"></script>
<script src="/js/jquery.lazyload-v1.9.1.min.js"></script>
<script src="js/integral-common.js?v=333"></script>
<script src="js/lucky-lottery.js?v=888"></script>
<script type="text/javascript">

</script>


</body>

</html>