<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>积分商城-确认兑换</title>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="/css/index-new.css?v=0939">
    <link href="css/index.min.css?v=555" rel="stylesheet">
    <link rel="stylesheet" href="css/pagination.css">
    <!-- <script type="text/javascript" src="${ctx}/js/jquery18.js"></script> -->
</head>

<body>
<jsp:include page="/tpl/header.jsp"></jsp:include>

<div class="address-wrapper wp">
    <div class="nav-bread">
        <a href="/newIntegralShop/integral.jsp">积分商城</a> &gt;
        <a href="javascript:;">确认兑换</a>
    </div>

    <div class="point-address">
        <div class="point-addr">

        </div>

        <div class="confirm-exchange">

        </div>

    </div>
</div>
<input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad" />
<script src="/js/jquery18.js"></script>
<jsp:include page="/tpl/footer.jsp"></jsp:include>
<script src="${ctx}/js/superslide.2.1.js"></script>
<script src="${ctx}/js/items.js"></script> 

<!--积分商城-->  
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="js/pagination.min.js"></script>
<script src="js/address.js"></script>
<script src="js/integral-common.js?v=21"></script>
<script src="js/exchange-address.js?v=666"></script> 
<script id="point-addr-tpl" type="text/x-jquery-tmpl">
     <h3>选择地址</h3>
            <div class="addr-list clearfix">

                {{each(i,item) data}}
                    <div data-id="{{= id}}" class="item {{if flag}}active{{/if}}">
                        <div class="tit">
                            <i class="iconfont icon-yonghu"></i>
                            <p>
                                <span>{{= name}}</span> 收
                            </p>
                            <!--<a class="{{if flag}}active{{/if}}" data-flag="{{= flag}}" href="javascript:;">{{= flag?'默认地址':'设置默认' }}</a>-->
                        </div>
                        <div>
                            <i class="iconfont icon-appchanpinyemian03"></i>
                            <p class="ellipsis-2">{{= area +' '+ address}}</p>
                        </div>
                        <div class="user-phone">
                            <i class="iconfont icon-shouji-copy"></i>
                            <p>
                                <span data-phone="{{= phone}}" class="exchange-phone">{{= formatStr(phone,3,4,4)}}</span>
                                <a class="change-addr" href="javascript:;">修改</a>
                            </p>
                        </div>
                        <i class="delete">&times;</i>
                    </div>
                {{/each}}

                <div class="add-addr">
                    <i class="iconfont icon-plus"></i> 新增收货地址
                </div>

            </div>

</script>

<script id="confirm-exchange-tpl" type="text/x-jquery-tmpl">
      <h3>确认兑换商品信息</h3>
          <div>
            <table>
              <thead>
              <td>兑换商品</td>
              <td>兑换积分</td>
              <td>优惠方式</td>
              </thead>
              <tbody>
              <td>
                <img class="thumb" src="{{= imageUrl}}" alt="" />
                <span>{{= name}}</span>
              </td>
              <td>{{= point}}</td>
              <td>
                {{= vipSave*10 == 10?'新会员暂无优惠（等级不够）':'享受'+vipSave*10+'折优惠'}}
                （<c:if test="${session.customer.level==0}">新会员</c:if>
                <c:if test="${session.customer.level==1}">忠实会员</c:if>
                <c:if test="${session.customer.level==2}">星级VIP</c:if>
                <c:if test="${session.customer.level==3}">金牌VIP</c:if>
                <c:if test="${session.customer.level==4}">白金VIP</c:if>
                <c:if test="${session.customer.level==5}">钻石VIP</c:if>
                <c:if test="${session.customer.level==6}">至尊VIP</c:if>）
                <div class="promotion-info">
                    <p><span class="left-label">商品积分：</span>{{= oldPoint}}分</p>
                    <p><span class="left-label">活动优惠：</span>0分</p>
                    <p><span class="left-label">VIP优惠：</span>-{{= promotion}}分</p>
                </div>
              </td>
              </tbody>
              <tfoot>
              <td colspan="2"><p class="tip">虎哥温馨提示：发货时以站内信通知您，将于一周内发货，收货信息经确认无法修改</p></td>
              <td>兑换积分合计：<span>{{= point}}</span> 分</td>
              </tfoot>
            </table>

            <div class="clearfix">
              <a class="sure-exchange btnClass" href="javascript:;">立即兑换</a>
            </div>
          </div>

</script>

</body>

</html>