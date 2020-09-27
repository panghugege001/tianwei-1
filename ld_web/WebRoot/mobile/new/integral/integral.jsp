<%@page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.Constants" %>
<%@page import="org.apache.axis2.AxisFault"%>
<%@page import="dfh.utils.AxisUtil"%>
<%@page import="app.bean.TokenBean"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="app.util.AESUtil"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="dfh.model.Users"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.google.gson.Gson"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String tokenForTopic = request.getParameter("tokenForTopic");

if(StringUtils.isNotEmpty(tokenForTopic)){

	String params = "";

	try {

		tokenForTopic = tokenForTopic.replaceAll("%5R", "\r").replaceAll("%5N", "\n");

		params = AESUtil.getInstance().decrypt(tokenForTopic);


		JSONObject json = JSONObject.fromObject(params);

		String loginName = String.valueOf(json.get("loginName"));
		String timestamp = String.valueOf(json.get("timestamp"));
		long timestampSub = (new Date().getTime() - Long.parseLong(timestamp));

		System.out.println(loginName);

		if(timestampSub < 1000*60*3){

			Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { loginName }, Users.class);
	
			System.out.println(user.getLoginname());

			session.setAttribute("customer", user);

		}

	} catch (Exception e) {

	}
}

%>
<!DOCTYPE >
<html>

<head>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
    <meta name="full-screen" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link rel="stylesheet" href="/mobile/css/common.css?v=10">
    <link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_226486_fqtbqjfka66iggb9.css" />
    <link href="css/index.min.css?v=99992" rel="stylesheet">
</head>

<body>
<header class="common-header j-common-header integral-index" id="j-common-header">
        <i class="back iconfont icon-arrow-right" onclick="history.back()"></i>
    <div class="header-title">积分商城</div>
</header>

<div class="containers integral-index">
    <header class="p-header new-title">
        <c:if test="${session.customer!=null}">
            <div class="user-center">
                <div>
                    <i class="sp-level sp-lv-${session.customer.level}"></i>
                    <div style="padding-left: 20px;">
                        <p class="user-name">${session.customer.loginname}</p>
                        <p>我的积分：<span class="user-point">0</span>分</p>
                    </div>
                </div>
                <a class="record" href="./record.jsp?flag=2">
                    兑换记录<i class="iconfont icon-jiantou"></i>
                </a>
            </div>
        </c:if>
        <c:if test="${session.customer==null}">
            <div class="user-center user-center-loginOut">
                <div>
                    <img class="user-face" src="img/user.png" alt=""/>
                    <div>
                        <a href="/mobile/new/login.jsp"><p class="user-name">请先登录</p></a> 
                    </div>
                </div>
            </div>
        </c:if>

<!--         <div class="p-welfare">
    <a href="javascript:;">
        <div>
            <i class="iconfont icon-exchange"></i>
            <span>兑一送一</span>
        </div>
        <p class="tip">青龙及以上VIP会员，指定日期开启兑换</p>
    </a>
    <a href="javascript:;">
        <div>
            <i class="iconfont icon-jifen"></i>
            <span>双倍积分</span>
        </div>
        <p class="tip">全员可享受，指定日期开启存款送双倍积分</p>
    </a>
    <a href="javascript:;">
        <div>
            <i class="iconfont icon-zhekou"></i>
            <span>兑换折扣</span>
        </div>
        <p class="tip">根据会员等级，享受不同比例折扣，等级越高折扣越高</p>
    </a>
</div> -->
    </header>

    <div class="p-notice">
        <div class="tit">
            <span>中奖喜讯</span>
        </div>
        <div class="p-marquee list">
            <div class="bd">
                <ul></ul>
            </div>
        </div>

    </div>

    <div class="p-lottery">
        <a href="lucky-lottery.jsp">
            <img src="img/lucky2.png" alt=""/>
        </a>
    </div>

    <div class="nav-tab">

        <div class="wr goods-title">
            <div class="p-tab goods-nav">
                <div data-type="other" class="active">彩金福利兑换</div>
                <div data-type="physical">礼品兑换</div>
            </div>
            <div class="p-search">
                <span href="javascript:;">筛选</span>
                <i class="iconfont icon-shaixuan"></i>
            </div>
        </div>

        <div class="sub">
            <p>会员积分兑换值</p>
            <div class="items clearfix goods-tab">
                <span class="active">全部</span>
                <span data-price="1w">10000以上</span>
                <span data-price="5w">50000以上</span>
                <span data-price="10w">100000以上</span>
            </div>
        </div>

    </div>

    <div class="goods-list">
        <ul id="goods-list">

        </ul>
    </div>

    <div class="go-top">
        <span>Top</span>
    </div>
</div>

<!--登录状态-->
<input type="hidden" id="j-level" value="${session.customer.level}">
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script src="/js/superslide.2.1.js"></script>
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="/js/jquery.lazyload-v1.9.1.min.js"></script>
<script src="js/integral-common.js?v=555"></script>
<script src="js/newIntegral.js?v=555"></script>
<script type="text/javascript">
    // 等级
    levelNum = parseInt('${session.customer.level}');
</script>
<script id="goods-list-tpl" type="text/x-jquery-tmpl">
      <li
        data-type="{{= type}}"
        data-1w="{{= !(parseInt(vipSaveRange)<10000)}}"
        data-5w="{{= !(parseInt(vipSaveRange)<50000)}}"
        data-10w="{{= !(parseInt(vipSaveRange)<100000)}}"
      >
          <div class="pic">
            <img class="lazy" data-original="{{= imageUrl}}" alt="" />
          </div>
          <div class="descrip">
            <h3 class="ellipsis">{{= name}}</h3>
            <p class="point ellipsis">{{if range!=vipSaveRange}}现{{/if}}积分：<span>{{= vipSaveRange}}</span></p>
            {{if range!=vipSaveRange}}<p class="old-point ellipsis">原积分：<span>{{= range}}</span></p>{{/if}}
            <a href="javascript:;">立即兑换</a>
          </div>
      </li>
</script>


</body>

</html>