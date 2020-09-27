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

<body>
<header class="common-header j-common-header" id="j-common-header">
    <a href="javascript:history.back()" id="j-come-back" class="left-button j-back-url">
        <i class="back iconfont icon-jiantou"></i>
        返回
    </a>
    <div class="header-title">确认兑换信息</div>
</header>


<div class="containers">
    <!--所有地址，显示当前地址-->
    <div class="point-addr"></div>
    <!--兑换商品信息    -->
    <div class="confirm-exchange"></div>

</div>

<!--地址列表-->
<div class="addr-list-wrapper">
    <header class="common-header j-common-header">
        <a href="javascript:" class="left-button j-back-url close-addr-list">
            <i class="back iconfont icon-jiantou"></i>
        </a>
        <div class="header-title">选择地址</div>
    </header>
    <div class="addr-list addr-list-padding">
        <ul>
        </ul>
    </div>
    <a class="btnClass fixed new-addr-btn" href="./new-addr.jsp">
        <i class="iconfont icon-plus"></i>
        新建地址
    </a>
</div>



<!--登录状态-->
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="/js/jquery.lazyload-v1.9.1.min.js"></script>
<script src="js/integral-common.js?v=333"></script>
<script src="js/exchange-address.js?v=333"></script>
<!--商品信息模板-->
<script id="confirm-exchange-tpl" type="text/x-jquery-tmpl">
    <div class="goods-list record-list confirm-info">
        <ul>
            <li>
                <div class="pic">
                    <img src="{{= imageUrl}}" alt="" />
                </div>
                <div class="descrip">
                    <h3 class="ellipsis">{{= name}}</h3>
                    <p class="timer ellipsis">{{= property}}</p>
                </div>
                <div class="con-number">
                    <p>x1</p>
                    <p class="point">{{= point}}分</p>
                </div>
            </li>
        </ul>
        <div class="con-promotion">
            <span>优惠活动</span>
            <span>0分</span>
        </div>
        <div class="con-promotion">
            <span>VIP优惠</span>
            <span>-{{= promotion}}分</span>
        </div>
    </div>

    <div class="confirm-exchange-bar fixed">
        <p>共一件，合计<span class="red">{{= point}}分</span></p>
        <a class="exchange-btn sure-exchange btnClass" href="javascript:;">确认兑换</a>
    </div>
</script>
<!--当前地址-->
<script id="point-addr-tpl" type="text/x-jquery-tmpl">
    <div data-id={{= id}} class="addr-item {{= flag==1?'active':''}}">
        <div class="address">
            <i class="iconfont icon-appchanpinyemian03"></i>
                <div>
                    <p class="name">
                        {{= name}} <span class="exchange-phone" data-phone="{{= phone}}">{{= formatStr(phone,3,4,4)}}</span>
                    </p>
                    <p class="area">
                        {{= area}}{{= address}}
                    </p>
                    <p class="tip">
                        (发货时以站内信通知您，将于一周内发货，收获信息经确认无法修改)
                    </p>
                </div>
            <i class="iconfont icon-jiantou"></i>
        </div>
    </div>
</script>
<!--地址列表-->
<script id="point-addr-list-tpl" type="text/x-jquery-tmpl">
    <li class="{{= flag==1?'active':''}}">
        <i class="choose iconfont icon-duigou"></i>
        <div class="area">
            <p class="name">
                <span>{{= name}}</span>
                <span>{{= formatStr(phone,3,4,4)}}</span>
            </p>
            <p>{{= area}}{{= address}}</p>
        </div>
        <div data-id="{{= id}}" class="edit">
            <i class="iconfont icon-bianji"></i>编辑
        </div>
    </li>
</script>

</body>

</html>