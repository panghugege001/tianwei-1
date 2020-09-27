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
    <div class="lottery-landing">
        <div class="landing-center w_1200">
            <img class="text"  src="../images/lottery/text.png" />
            <div class="landing-z">
                <div class="right-box">
                    <h2 class="title">VR彩票</h2>
                    <p class="tip">多样彩票 刺激享乐</p>
                    <div class="game-lsit-text">
                        <h3>游戏种类：</h3>
                        <p>北京赛车、幸运飞艇、时时彩、快8、六合彩等</p>
                    </div>
                    <ul class="landing-btn">
                        <li class="web"><a href="javascript:;"><b></b>网页版</a></li>
                        <!-- <li class="app"><a href=""><b></b>客户端</a></li> -->
                        <li class="phone show_ewm"><a href="javascript:;"><b></b>手机版</a></li>
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
        $('.web').click(function(){
            if ('${session.customer==null}' == 'true') {
                alert('请登入后再进入游戏！');
                return;
            } 
            // location.href = '/game/fanyaLogin.aspx'
            window.open('/game/vrLogin.aspx')
        })   
    </script>
</body>
</html>