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
<i class="back iconfont icon-arrow-right" onclick="history.back()"></i>
    <div class="header-title">商品兑换详情</div>
</header>


<div class="containers">

    <div class="detail-wrapper"></div>

    <a class="btnClass fixed exchange" href="javascript:;">确认兑换</a>

    <!--返回顶部-->
    <div class="go-top">
        <i class="iconfont icon-shaixuan"></i>
    </div>
</div>

<!--登录状态-->
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="js/integral-common.js?v=333"></script>
<script>
    // 等级
    levelNum = parseInt('${session.customer.level}');
</script>
<script src="js/detail.js?v=888"></script>
<!--模板-->
<script id="detail-tpl" type="text/x-jquery-tmpl">
        <div class="pro-image">
            <img src="{{= imageUrl}}" alt=""/>
        </div>

        <div class="d-info">
            <h3>{{= name}}</h3>
            <p>
                {{= summary}}
            </p>
            <p>
                <span class="curr-point new-point">{{= property[0] && property[0].point*vipSave}}分 </span>
                <span class="old-point">{{= property[0].point}}分</span>
                 <span style="opacity:.6;">
                   <c:if test="${session.customer!=null}">
                     {{= vipSave*10 == 10?'暂无优惠（等级不够）':'享受'+vipSave*10+'折优惠'}}
                   </c:if>
                  </span>
            </p>

            <p class="user-info">
                <c:if test="${session.customer!=null}">
                     <span class="level">
                    <c:if test="${session.customer.level==0}">新会员</c:if>
                    <c:if test="${session.customer.level==1}">忠实VIP</c:if>
                    <c:if test="${session.customer.level==2}">星级VIP</c:if>
                    <c:if test="${session.customer.level==3}">金牌VIP</c:if>
                    <c:if test="${session.customer.level==4}">白金VIP</c:if>
                    <c:if test="${session.customer.level==5}">钻石VIP</c:if>
                    <c:if test="${session.customer.level==6}">至尊VIP</c:if>
                 </span>
                </c:if>
                <c:if test="${session.customer!=null}">
                     <span class="d-badge">当前可以用<span class="user-points">0</span>积分</span>
                </c:if>
                <c:if test="${session.customer==null}">
                     <span class="d-badge">请先登录</span>
                </c:if>
            </p>
        </div>

        <div class="promotion">
            <span>会员优惠查询</span>
            <i class="iconfont icon-jiantou"></i>
        </div>

        <div class="time-validity">
            <span>兑换有效期</span>
            <span>{{= startTime.split(' ')[0]}} 至 {{= endTime.split(' ')[0]}}</span>
        </div>

        <div class="property properties">
            <h4>类型</h4>
            <div class="clearfix">
                {{each(i,item) property}}
                  <span data-oldPoint="{{= item.point}}" data-point="{{= item.point*vipSave}}" data-property="{{= item.property}}">{{= filterAttrVal(type,item.property)}}</span>
                {{/each}}
            </div>
        </div>

</script>

</body>

</html>