<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/lotterylanding.css?v=12215"/>
    <script src="${ctx}/js/area.js"></script>
    <script src="${ctx}/js/superslide.2.1.js"></script>
</head>
<body class="daili_body">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
    <div class="lottery-landing esprots">
        <div class="landing-center w_1200">
            <!-- <img class="text"  src="../images/lottery/conenct.png" /> -->
            <div class="text"></div>
            <div class="landing-z">
                <div class="right-box">
                    <h2 class="title">泛亚电竞</h2>
                    <p class="tip">电竞联赛投注</p>
                    <div class="game-lsit-text">
                        <h3>游戏种类：</h3>
                        <p>英雄联盟、王者荣耀、绝地求生、DOTA2等</p>
                    </div>
                    <ul class="landing-btn">
                        <li class="phone"><a><b></b>网页版</a></li>
                        <li class="web show_ewm"><a href="javascript:;"><b></b>手机版</a></li>
                        <!-- <li class="app"><a href=""><b></b>客户端</a></li> -->
                    </ul>
                </div>
                 <div class="app_code">
                        <img src="/images/appxiazai/longduapp.png" />
                        <p>扫码下载手机APP</p>
                </div>
            </div>
        </div>        
    </div>
    <jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
    <script src="${ctx}/js/plugins/jquery.validate.min.js"></script>
    <script src="${ctx}/js/plugins/jquery.validate.config.js"></script>
    <script>
        $('.show_ewm').on('click',function () {
            $(this).parents().find('.app_code').slideToggle(400);
        })   
        $('.phone').click(function(){
            if ('${session.customer==null}' == 'true') {
                alert('请登入后再进入游戏！');
                return;
            } 
            // location.href = '/game/fanyaLogin.aspx'
            window.open('/game/fanyaLogin.aspx')
        })     
       
    </script>
</body>
</html>