<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<link rel="stylesheet" href="/activety/common/css/activety_base.css?v=2">
<div class="activety_header">
    <div class="container">
            <a  class="activety_logo" href="/index.jsp">

            </a>
        <div class="back_index">
            <a href="/index.jsp">返回首页</a>
        </div>
        <div class="activety_muee" id="activety_muee">
            <a href="/activety/qixi/index.jsp">七夕拥抱爱无限</a>
            <!-- <a href="/activety/sfyc/index.jsp">胜负预测</a> -->
            <a href="/activety/tjhy/index.jsp">推荐返利</a>
            <a href="/activety/tsyj/index.jsp">百家乐赢彩金</a>
           <!--  <a href="/activety/zzrw/index.jsp">任务周周送</a>
            <a href="/activety/football/index.jsp">世界杯竞猜</a> -->
            <a href="/activety/jzsjb/index.jsp">决战世界杯</a>
            <a href="/">返回首页</a>
        </div>
    </div>
</div>
<script>
    window.onload = function () {
        var psth = window.location.pathname;
        $('#activety_muee a').each(function (i, item) {
            if (psth.toString().indexOf(item.href) > -1 || item.href.toString().indexOf(psth) > -1) {
                $(item).addClass('active');
            }

        })
    }
</script>
