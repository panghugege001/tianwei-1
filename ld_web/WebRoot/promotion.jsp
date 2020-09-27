<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE>
<html>
<head lang="zh-cn">
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="css/promotion.css?v=16"/>
</head>
<style>
    #modal-content {
        height: 666px;
    }

    @media screen and (max-width: 750px) {
        #modal-content {
            height: auto;
        }
    }
</style>
<body>
<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
<div class="promotion_bannar"></div>
<div class="w_1200">
    <div class="muel_left_box">
        <div class="pro-nav mb20">
            <ul class="navlist" id="j-menu">
                <li class="action"><i><img src="images/promotion/quanbu.png"></i><a href="#j-promotion-list"
                                                                                    data-type="ALL">全部优惠</a></li>
                <!-- <li><i class="t_10"><img src="images/promotion/shouji.png"></i><a href="#j-promotion-list"
                                                                                  data-type="PHO">手机专属</a></li>
                <li><i class="t_10"><img src="images/promotion/changqi.png"></i><a href="#j-promotion-list"
                                                                                   data-type="LON">长期优惠</a></li> -->
                <li><i class="t_10"><img src="images/promotion/xianshi.png"></i><a href="#j-promotion-list"
                                                                                   data-type="LIM">限时优惠</a></li>
                <!-- <li><i class="t_10"><img src="images/promotion/mianfei.png"></i><a href="#j-promotion-list"
                                                                                   data-type="TOP">免费彩金</a></li> -->
                <li><i class="t_10"><img src="images/promotion/xianshi.png"></i><a href="#j-promotion-list"
                                                                                   data-type="OVER">往期优惠</a></li>
            </ul>
        </div>
    </div>
    <div class="promotion-list" id="j-promotion-list"></div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script src="/js/lib/jquery.lazyload-v1.9.1.min.js"></script>
<script src="/js/promotion.js?v=01041"></script>
<script>
    window.onload = function () {
        setTimeout(function(){
            var url=window.location.search.trim();
            if(url){
            $(url.replace("?","[data-id=")+"]").click()
            }
        })
        $('[data-id="vip2"]').parent("#j-promotion-list").find(".promotion-item").first().css("width", "600px");
        $(".promotion-item").hover(function () {
            $(this).find(".go_btn_box").show();
        }, function () {
            $(this).find(".go_btn_box").hide();
        })
    }
</script>
</body>
</html>