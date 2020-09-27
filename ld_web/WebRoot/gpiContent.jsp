<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.action.vo.AnnouncementVO" %>
<%@page import="dfh.utils.AxisSecurityEncryptUtil" %>
<div class="clear"></div>
<div class="video_banner">
    <div class="bottom_shadow"></div>
</div>
<div class="video_page container">
    <div class="public">
        <div class="pub_left">
            <strong>NEWS</strong>
            <span>新闻公告：</span>
        </div>
        <div class="pub_marquee">
            <ul>
                <%
                    response.setHeader("pragma", "no-cache");
                    response.setHeader("cache-control", "no-cache");
                    response.setDateHeader("expires", 0);

                    List<AnnouncementVO> list = AxisSecurityEncryptUtil.query();
                    if (list == null) {
                        list = new ArrayList();
                    }
                    request.setAttribute("list", list);
                %>
                <s:iterator value="#request.list" var="ann">
                    <li>
                        <a href="javaScript:void(0);" target="_blank;"><s:property value="title"/></a>
                    </li>
                </s:iterator>
            </ul>
        </div>
        <div class="c_btn">
            <a href="javascript:void(0);" class="pre"></a>
            <a href="javascript:void(0);" class="next"></a>

        </div>

    </div>
    <div class="video_online">
        <img src="${ctx}/images/gpi_banner.jpg" alt=""/>
    </div>
    <div class="video_tabs">
        <ul class="tabs_handle tab_hd">
        	<li class="active"><a data-toggle="tab" href="#tab_gpi_jackpotgames">累计奖池</a></li>
            <li><a data-toggle="tab" href="#tab_gpi_games">3D老虎机</a></li>
        </ul>
        <div class="tab_bd">
        	<ul class="game-list tab_panel fade in active" id="tab_gpi_jackpotgames"></ul>
            <ul class="game-list tab_panel fade" id="tab_gpi_games"></ul>
        </div>
    </div>

</div>