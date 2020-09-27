<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="dfh.model.Users" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>积分商城</title>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link href="css/index.min.css?v=666" rel="stylesheet">
    <link rel="stylesheet" href="css/pagination.css">
    <link rel="stylesheet" href="/css/index-new.css?v=0938">
    <!-- <script type="text/javascript" src="${ctx}/js/jquery18.js"></script> -->
</head>

<body>
 <jsp:include page="/tpl/header.jsp"></jsp:include>
<input type="hidden" id="getLv" val="${session.customer.level}">
<!--详情页-->
<div class="detail-wrapper wp">

</div>

<!--登录状态-->
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad"/>
<script src="/js/jquery18.js"></script>
<jsp:include page="/tpl/footer.jsp"></jsp:include>
<script src="${ctx}/js/superslide.2.1.js"></script>
<script src="${ctx}/js/items.js"></script>

<!--积分商城-->
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script> 
<script src="js/pagination.min.js"></script>
<script src="js/integral-common.js?v=666"></script> 
<script>
    // 等级
    levelNum = parseInt('${session.customer.level}');
</script>
<script src="js/detail.js?v=888"></script> 
<!--模板-->
<script id="detail-tpl" type="text/x-jquery-tmpl">
      <div class="nav-bread">
        <a href="/IntegralShop/integral.jsp">积分商城</a> &gt;
        <a href="javascript:;">{{= name}}</a>
      </div>
      <div class="detail clearfix">
        <div class="img-content">
          <img src="{{= imageUrl}}" alt="" />
        </div>
        <div class="detail-descrip">
          <div class="level-info">
            <c:if test="${session.customer!=null}">
              帐号：<span>${session.customer.loginname}</span>
              等级：<span class="level-text-${session.customer.level}"></span>
            </c:if>
            <span class="point-show" style="border:none;color: #2e2e2e;">积分:<span class="user-points" style="color: #fca825">0</span></span>
            <h3 class="b-tit">{{= name}}（<span class="info">{{= summary}}</span>）</h3>
          </div>
          <div class="points-info">
            <p class="curr-point-info">
              <span>所需积分：</span>
              <span class="curr-point">{{= property[0] && property[0].point*vipSave}}分 </span>
              <span class="old-point">{{= property[0] && property[0].point}}分 </span>
            </p>
            <div class="level-df">
              <p class="promotion-level">
               <c:if test="${session.customer!=null}">
                 {{= vipSave*10 == 10?'暂无优惠（等级不够）':'享受'+vipSave*10+'折优惠'}}
               </c:if>
              </p>
              <div class="integral-promotion">
                <a class="collapse" href="javacript:;">
                  不同等级会员享不同优惠 <i class="iconfont icon-down-trangle-copy-copy"></i>
                </a>
                <ul> 
                </ul>
              </div>
            </div>
          </div>

          <div class="options">
            <p>
              <span>有效期</span>
              <span class="timer">{{= startTime.split(' ')[0]}} 至 {{= endTime.split(' ')[0]}}</span>
            </p>
            <div class="type clearfix">
              <span>选择类型</span>
              <div class="properties">
                {{each(i,item) property}}
                  <span data-oldPoint="{{= item.point}}" data-point="{{= item.point*vipSave}}" data-property="{{= item.property}}">{{= filterAttrVal(type,item.property)}}</span>
                {{/each}}
              </div>
            </div>
            <p>
              <span></span>
              <a class="spanBtnStyle exchange" href="javascript:;">立即兑换</a>
            </p>
          </div>
        </div>
      </div>

</script>

</body>

</html>