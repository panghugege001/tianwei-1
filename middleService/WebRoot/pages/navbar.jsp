<%@ page language="java" pageEncoding="UTF-8" %>
<nav class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#example-navbar-collapse">
                <span class="sr-only">切换导航</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">中间件</a>
        </div>
        <div class="collapse navbar-collapse" id="example-navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="/pay/main">支付平台</a></li>
                <li><a href="#">优惠活动</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        系统<b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#">基础设置</a></li>
                        <li><a href="/dictionary/main">字典项</a></li>
                        <li><a href="/merchantPay/main">支付平台配置-管理员</a></li>
                        <li><a href="/merchantPay/finance">支付平台配置-专员</a></li>
                        <li class="divider"></li>
                        <li><a href="/redis/main">缓存管理</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>