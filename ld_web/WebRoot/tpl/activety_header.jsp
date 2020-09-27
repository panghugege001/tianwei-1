<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<style>
    body {
        font-size: 16px;
    }

    .activety_logo {
        position: absolute;
        top: 2px;
        left: 0;
        display: block !important;
    }

    .activety_muee {
        float: right;
    }

    .activety_muee li {
        float: left;
        height: 80px;
        line-height: 80px;
        width: 155px;
        text-align: center;
        font-size: 18px;
        color: #fff;
        background-color: #2c8ba3;
        border-right: 1px solid #171c3a;
    }

    .activety_muee li a {
        display: block;
        cursor: pointer;
    }

    .activety_muee li.active {
        color: #dfa85a;
    }

    #asidebar {
        display: none !important;
    }

    .back_index {
        display: none;
    }

    .back_index a {
        height: 40px;
        display: inline-block;
        width: 110px;
        background: #dfa85a;
        border-radius: 10px;
        color: #FFFFFF;
        line-height: 40px;
        font-size: 16px;
    }

    .activety_header .container {
        height: 80px;
        width: 1200px;
        position: relative;
        margin: 0 auto;
        z-index: 10;
    }

    @media screen and (max-width: 750px) {
        .activety_muee, .gb-footer, .activety_logo {
            display: none !important;
        }

        .activety_header .container {
            width: 100% !important;
            height: 80px;
            line-height: 80px;
            padding: 0 !important;
        }
        .back_index {
            display: inline-block;
            vertical-align: middle;
            margin-left: 10%;
            margin-top: 20px;
        }
    }

    .activety_header {
        background-color: #14192c;
        position: relative;
        border-bottom: 5px solid #2c8ba3;
        z-index: 10;
    }


</style>
<div class="activety_header">
    <div class="container">
        <div class="activety_logo">
            <a href="/index.jsp">
                <img src="img/new_logo.png">
            </a>
        </div>
        <div class="back_index">
            <a href="/index.jsp">返回首页</a>
        </div>
        <ul class="activety_muee" id="activety_muee">
            <li>
                <a href="/activety/redpacket/index.jsp">百万红包雨</a>
            </li>
            <li>
                <a href="/activety/tjhy/index.jsp">推荐返利</a>
            </li>
            <li>
                <a href="/activety/meinv/index.jsp">海派甜心</a>
            </li>
            <li>
                <a href="/activety/topic/index.jsp">每日存送</a>
            </li>
            <!--<li>-->
            <!--<a href="/activety/DTjackpot/index.jsp">DT百万奖池</a>-->
            <!--</li>-->
            <!--<li>-->
            <!--<a href="/activety/shengdang/index.jsp">庆圣诞迎元旦</a>-->
            <!--</li>-->
            <li>
                <a href="/activety/zhoumo/index.jsp">周末狂欢</a>
            </li>
            <li>
                <a href="/">返回首页</a>
            </li>
        </ul>
    </div>
</div>
<script>
    window.onload = function () {
        var psth = window.location.pathname;
        $('#activety_muee a').each(function (i, item) {
            if (psth.toString().indexOf(item.href) > -1 || item.href.toString().indexOf(psth) > -1) {
                $(item).parent().addClass('active');
            }

        })
    }

</script>