<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>积分商城-领取商品</title>
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
        <a href="javascript:;">积分商城</a> &gt;
        <a href="javascript:;">领取商品</a>
    </div>

    <div class="point-address">
        <div class="point-addr">

        </div>

        <div class="confirm-exchange">

        </div>


    </div>
</div>

<script src="/js/jquery18.js"></script>
<jsp:include page="/tpl/footer.jsp"></jsp:include>
<script src="${ctx}/js/superslide.2.1.js"></script>  
<script src="${ctx}/js/items.js"></script>

<!--积分商城-->
<script src="/js/jquery-tmpl.js"></script>
<script src="/js/layer/layer.js"></script>
<script src="js/pagination.min.js"></script> 
<script src="js/address.js"></script>
<script src="js/integral-common.js?v=20"></script>
<script src="js/exchange-address.js?v=80"></script>

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
                                <span class="exchange-phone" data-phone="{{= phone}}">{{= formatStr(phone,3,4,4)}}</span>
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
      <h3>确认领取商品信息</h3>
          <div>
            <table>
              <thead>
              <td>领取商品</td>
              <td>获得方式</td>
              </thead>
              <tbody>
              <td>
                <img class="thumb" src="{{= imageUrl}}" alt="" />
                <span>{{= name}}</span>
              </td>
              <td>抽奖获得</td>
              </tbody>
              <tfoot>
              <td colspan="2"><p class="tip">虎哥温馨提示：发货时以站内信通知您，将于一周内发货，收货信息经确认无法修改</p></td>
              </tfoot>
            </table>


            <div class="clearfix">
              <a class="sure-exchange btnClass" href="javascript:;">立即领取</a>
            </div>
          </div>






</script>

</body>

</html>