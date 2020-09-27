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
                        <a href="javaScript:void(0);" target="_blank;"><s:property
                                value="title"/>
                        </a>
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
        <img src="${ctx}/images/ttg_banner.jpg" alt=""/>
    </div>
    <div class="account_btn wp" id='accountDiv'><a id="j_account_btn" href="" target="_blank">我的账户</a></div>
    <div class="video_tabs tabs">
        <ul class="tabs_handle tab_hd">
            <li class="active"><a data-toggle="tab" href="#tab_new_games">最新游戏</a></li>
            <li><a data-toggle="tab" href="#tab_hot_games">热门游戏</a></li>
            <li><a data-toggle="tab" href="#tab_slot_games_wp">老虎机</a></li>
            <li><a data-toggle="tab" href="#tab_card_games">纸牌游戏</a></li>
            <li><a data-toggle="tab" href="#tab_desktop_games">桌面游戏</a></li>
            <li><a data-toggle="tab" href="#tab_elect_porker_games">电子扑克</a></li>
        </ul>

        <div class="tab_bd">
            <div class="game-list tab_panel fade in active" id="tab_new_games"></div>
            <div class="game-list tab_panel fade" id="tab_hot_games"></div>
            <div class="game-list tab_panel fade" id="tab_slot_games_wp" style="overflow: hidden;padding-bottom: 80px;">
                <ul class="tab_hd tab_nav_slot">
                    <li class="active"><a href="#tab_slot_games" data-toggle="tab">全部</a></li>
                    <li><a href="#tab_onePay" data-toggle="tab">1条赔付线</a></li>
                    <li><a href="#tab_ninePay" data-toggle="tab">9条赔付线</a></li>
                    <li><a href="#tab_tenPay" data-toggle="tab">10或15条赔付线</a></li>
                    <li><a href="#tab_twentyPay" data-toggle="tab">20条赔付线</a></li>
                    <li><a href="#tab_twentyfivePay" data-toggle="tab">25条赔付线</a></li>
                    <li><a href="#tab_thirtyPay" data-toggle="tab">30条赔付线</a></li>
                    <li><a href="#tab_fourtyPay" data-toggle="tab">40或50条赔付线</a></li>
                   <!-- <li><a href="#tab_onehundrenPay" data-toggle="tab">100条赔付线</a></li>-->
                    <li><a href="#tab_onetwentyPay" data-toggle="tab">1024种方式游戏</a></li>
                </ul>
                <div class="tab_bd tab_bd_slot clearfix">
                    <div class="tab_panel fade in active" id="tab_slot_games"></div>
                    <div class="tab_panel fade" id="tab_onePay"></div>
                    <div class="tab_panel fade" id="tab_ninePay"></div>
                    <div class="tab_panel fade" id="tab_tenPay"></div>
                    <div class="tab_panel fade" id="tab_twentyPay"></div>
                    <div class="tab_panel fade" id="tab_twentyfivePay"></div>
                    <div class="tab_panel fade" id="tab_thirtyPay"></div>
                    <div class="tab_panel fade" id="tab_fourtyPay"></div>
                    <!--<div class="tab_panel fade" id="tab_onehundrenPay"></div>-->
                    <div class="tab_panel fade" id="tab_onetwentyPay"></div>
                </div>

            </div>
            <div class="game-list tab_panel fade" id="tab_card_games"></div>
            <div class="game-list tab_panel fade" id="tab_desktop_games"></div>
            <div class="game-list tab_panel fade" id="tab_elect_porker_games"></div>
        </div>
    </div>

</div>