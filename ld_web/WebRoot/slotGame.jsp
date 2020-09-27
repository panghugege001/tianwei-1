<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/game.css?v=0153"/>
</head>
<body>

<div class="index-bg">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
<div class="slotgame_box">
    <div class="slotgame_bannar">
        <div class="h_300"></div>
        <div class="winner_box">
          <div class="bd">
            <ul>
             <li>
                <div class="li_left">
                    <div class="game_imgbox">
                        <span class="box1"></span>
                    </div>
                     <div class="p_10">
                          <a href="" target="_blank"> 恭喜湖北玩家<span class="cfe">zdd****"</span> 在 PT游戏<br><span class="cfe">海滨嘉年华</span> 中喜中 <b class="r_red">375000</b> 元大奖<br><span class="cfe">2017-01-27</span> </a>
                     </div>                 
                </div>
                <div class="li_rigth"> 
                    <div class="game_imgbox">
                        <span class="box2"></span>
                    </div>
                     <div class="p_10">
                          <a href="" target="_blank"> 恭喜山西玩家<span class="cfe">hyf****</span> 在 PT游戏<br><span class="cfe">年年有余</span> 中喜中 <b class="r_red">800000</b> 元大奖<br><span class="cfe">2016-12-27</span> </a>
                     </div>                 
                </div>               
              </li>
              <li>
                <div class="li_left">
                    <div class="game_imgbox">
                        <span class="box2"></span>
                    </div>
                     <div class="p_10">
                          <a href="" target="_blank"> 恭喜四川玩家<span class="cfe">damia××××</span> 在 PT游戏<br><span class="cfe">年年有余</span> 中喜中 <b class="r_red">626429</b> 元大奖<br><span class="cfe">2016-12-23</span> </a>
                     </div>                 
                </div>
                
                <div class="li_rigth">
                    <div class="game_imgbox">
                        <span class="box3"></span>
                    </div>
                     <div class="p_10">
                          <a href="" target="_blank"> 恭喜广西玩家<span class="cfe">zuo××××</span> 在 DT游戏<br><span class="cfe">火影忍者，拳皇等</span> 中喜中 <b class="r_red">1100000</b> 元大奖<br><span class="cfe">2016-12-3</span> </a>
                     </div>                 
                </div>
                
              </li>            
              <li>
                    <div class="li_left">
                        <div class="game_imgbox">
                            <span class="box3"></span>
                        </div>
                         <div class="p_10">
                              <a href="" target="_blank"> 恭喜广西玩家<span class="cfe">gq1××××</span> 在 DT游戏<br><span class="cfe">酷炫水果</span> 中喜中 <b class="r_red">724553</b> 元大奖<br><span class="cfe">2016-11-24</span> </a>
                         </div>                     
                    </div>
                    
                    <div class="li_rigth">
                        <div class="game_imgbox">
                            <span class="box1"></span>
                        </div>
                         <div class="p_10">
                              <a href="" target="_blank"> 恭喜湖北玩家<span class="cfe">zdd****"</span> 在 PT游戏<br><span class="cfe">海滨嘉年华</span> 中喜中 <b class="r_red">375000</b> 元大奖<br><span class="cfe">2017-01-27</span> </a>
                         </div>                     
                    </div>
              </li>
              <li>
                    <div class="li_left">
                        <div class="game_imgbox">
                            <span class="box2"></span>
                        </div>
                         <div class="p_10">
                              <a href="" target="_blank"> 恭喜山西玩家<span class="cfe">hyf****</span> 在 PT游戏<br><span class="cfe">年年有余</span> 中喜中 <b class="r_red">800000</b> 元大奖<br><span class="cfe">2016-12-27</span> </a>
                         </div>                     
                    </div>
                    
                    <div class="li_rigth">
                        <div class="game_imgbox">
                            <span class="box2"></span>
                        </div>
                         <div class="p_10">
                              <a href="" target="_blank"> 恭喜四川玩家<span class="cfe">damia××××</span> 在 PT游戏<br><span class="cfe">年年有余</span> 中喜中 <b class="r_red">626429</b> 元大奖<br><span class="cfe">2016-12-23</span> </a>
                         </div>                     
                    </div>
              </li>   
              <li>
                    <div class="li_left">
                        <div class="game_imgbox">
                            <span class="box3"></span>
                        </div>
                         <div class="p_10">
                              <a href="" target="_blank"> 恭喜广西玩家<span class="cfe">zuo××××</span> 在 DT游戏<br><span class="cfe">火影忍者，拳皇等</span> 中喜中 <b class="r_red">1100000</b> 元大奖<br><span class="cfe">2016-12-3</span> </a>
                         </div>                     
                    </div>
                    <div class="li_rigth">
                        <div class="game_imgbox">
                            <span class="box3"></span>
                        </div>
                         <div class="p_10">
                              <a href="" target="_blank"> 恭喜广西玩家<span class="cfe">gq1××××</span> 在 DT游戏<br><span class="cfe">酷炫水果</span> 中喜中 <b class="r_red">724553</b> 元大奖<br><span class="cfe">2016-11-24</span> </a>
                         </div>                     
                    </div>
              </li>                   
            </ul>
          </div>
        </div>
    </div>
    <div class="container">
        <div class="banner-top mb20">
            <div class="jackpot-top fl pt_jack">
                <div class="jackpot_text"><img src="images/game/jackpot_name.png"></div>
                <div class="jackpot-div" id="j-jackpotCount1">
                    <span class="num-item small">&yen;</span>
                </div>
            </div>
            <div class="jackpot-top fl dt_jack" style="display: none;">
                <div class="jackpot_text"><img src="images/game/jackpot_name.png"></div>
                <div class="jackpot-div" id="j-jackpotCount">
                    <span class="num-item small">&yen;</span>
                </div>
            </div>              
        </div> 
        <div class="cl"></div>
        <ul id="j-gameMenu" class="tab-menu cfx">
            <li><a data-tab="PT" data-value='{"category":"PT"}' href="javascript:;"><span class="s1">PT 老虎机</span></a></li>
            <li><a data-tab="DT" data-value='{"category":"DT"}' href="javascript:;"></i><span class="s1">DT 老虎机</span></a></li>
            <li><a data-tab="PTSW" data-value='{"category":"PTSW"}' href="javascript:;"><span class="s1">SW老虎机</span></a></li>    
            <li><a data-tab="MGS" data-value='{"category":"MGS"}' href="javascript:;"><span class="s1">MG 老虎机</span></a></li>
            <li><a data-tab="PNG" data-value='{"category":"PNG"}' href="javascript:;"><span class="s1">PNG 老虎机</span></a></li>
            <li><a data-tab="TTG" data-value='{"category":"TTG"}' href="javascript:;"><span class="s1">TTG 老虎机</span></a></li>
            <li><a data-tab="NT" data-value='{"category":"NT"}' href="javascript:;"><span class="s1">NT 老虎机</span></a></li>
            <li><a data-tab="QT" data-value='{"category":"QT"}' href="javascript:;"><span class="s1">QT 老虎机</span></a></li>
            <li><a data-tab="AG" data-value='{"category":"AG"}' href="javascript:;"><span class="s1">AG 老虎机</span></a></li>
            <li style="width: 149px;"><a data-tab="BBIN" data-value='{"category":"BBIN"}' href="javascript:;"><span class="s1">BBIN 老虎机</span></a></li>
        </ul>
        <div class="game_1200">
        <div class="cfx">
            <div class="search-info fl">
                <div class="search-bd no-border cfx">
                    <div>
                        <form id="j-filter" class="form">
                            <dl class="tab-hd search-row show_gametype">
                                <dt>游戏平台:</dt>
                                <dd><%--<a class="active" data-type="category" data-value="" href="#">全选</a>--%>
                                    <a data-toggle="game-tab" data-value='{"category":"PT"}' href="#tab-pt">PT</a>
                                    <a data-toggle="game-tab" data-value='{"category":"PTSW"}' href="#tab-ptsw">SW</a>
                                    <a data-toggle="game-tab" data-value='{"category":"DT"}' href="#tab-dt">DT</a>                                    
                                    <a data-toggle="game-tab" data-value='{"category":"MGS"}' href="#tab-mg">MG</a>
                                    <a data-toggle="game-tab" data-value='{"category":"PNG"}' href="#tab-png">PNG</a>
                                    <a data-toggle="game-tab" data-value='{"category":"TTG"}' href="#tab-ttg">TTG</a>

                                    <%--<a data-value='TTG-MG" href="#tab-TTG小游戏">TTG小游戏</a>--%>
                                    <a data-toggle="game-tab" data-value='{"category":"NT"}' href="#tab-nt">NT</a>                                    
                                    <a data-toggle="game-tab" data-value='{"category":"QT"}' href="#tab-qt">QT</a>
                                    <a data-toggle="game-tab" data-value='{"category":"AG"}' href="#tab-ag">AG</a>
                                    <a data-toggle="game-tab" data-value='{"category":"BBIN"}' href="#tab-bbin">BBIN</a>
                                </dd>

                            </dl>
                            <div class="tab-bd">
                                <div id="tab-pt" class="tab-panel active">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <%-- PT 老虎机和其他不一样，经典和电动的类型为 {"type":"CLA"}，{"type":"SLO"}--%>
                                            <a data-value='{"type":"CLA"}' href="javascript:;">经典</a>
                                            <a data-value='{"type":"SLO"}' href="javascript:;">电动</a>
                                            <a data-value='{"type":"POK"}' href="javascript:;">视频扑克</a>
                                            <a data-value='{"type":"STR"}' href="javascript:;">街机</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row j-line-pt">
                                        <dt>赔钱线数:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"line":"1-10"}' href="javascript:;">1-10线</a>
                                            <a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>
                                            <a data-value='{"line":"25"}' href="javascript:;">25线</a>
                                            <a data-value='{"line":"30"}' href="javascript:;">30线</a>
                                            <a data-value='{"line":"40-60"}' href="javascript:;">40-60线</a>
                                            <a data-value='{"line":"100-1024"}' href="javascript:;">100-1024线</a>
                                            <a data-value='{"line":"MUL"}' href="javascript:;">多旋转</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"AMA"}' href="javascript:;">奖池游戏</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-type="tag" data-value='{"tag":"HEL"}' href="javascript:;">幸运转游戏</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-ptsw" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <%-- PT 老虎机和其他不一样，经典和电动的类型为 {"type":"CLA"}，{"type":"SLO"}--%>
                                            <a data-value='{"tag":"AMA"}' href="javascript:;">经典</a>
                                        </dd>
                                    </dl>
                                    <%--<dl class="search-row j-line-pt">--%>
                                        <%--<dt>赔钱线数:</dt>--%>
                                        <%--<dd><a class="active" data-value="" href="javascript:;">全选</a>--%>
                                            <%--<a data-value='{"line":"1-10"}' href="javascript:;">1-10线</a>--%>
                                            <%--&lt;%&ndash;<a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<a data-value='{"line":"25"}' href="javascript:;">25线</a>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<a data-value='{"line":"30"}' href="javascript:;">30线</a>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<a data-value='{"line":"40-60"}' href="javascript:;">40-60线</a>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<a data-value='{"line":"100-1024"}' href="javascript:;">100-1024线</a>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<a data-value='{"line":"MUL"}' href="javascript:;">多旋转</a>&ndash;%&gt;--%>
                                        <%--</dd>--%>
                                    <%--</dl>--%>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"JPT"}' href="javascript:;">奖池游戏</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <%--<a data-type="tag" data-value='{"tag":"HEL"}' href="javascript:;">幸运转游戏</a>--%>
                                        </dd>
                                    </dl>
                                </div>                            
                                <div id="tab-ttg" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value='{"type":"SLO"}' href="javascript:;">全选</a>
                                            <a data-value='{"type":"SLO"}' href="javascript:;">电动</a>
                                            <a data-value='{"type":"CLA"}' href="javascript:;">经典</a>
                                            <a data-value='{"subType":"PMT"}' href="javascript:;">Pragmatic游戏</a>
                                            <a data-value='{"subType":"BMG"}' href="javascript:;">Booming游戏</a>
                                            <a data-value='{"subType":"PSG"}' href="javascript:;">Playson游戏</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row j-line-pt">
                                        <dt>赔钱线数:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"line":"1-10"}' href="javascript:;">1-10线</a>
                                            <a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>
                                            <a data-value='{"line":"25"}' href="javascript:;">25线</a>
                                            <a data-value='{"line":"30"}' href="javascript:;">30线</a>
                                            <a data-value='{"line":"40-60"}' href="javascript:;">40-60线</a>
                                            <a data-value='{"line":"100-1024"}' href="javascript:;">100-1024线</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-type="tag" data-value='{"tag":"AMA"}' href="javascript:;">奖池游戏</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-mg" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"CLA"}' href="javascript:;">经典</a>
                                            <a data-value='{"tag":"ELE"}' href="javascript:;">电动</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>赔钱线数:</dt>
                                        <dd><a class="active" data-type="" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"line":"1-15"}' href="javascript:;">1-15线</a>
                                            <a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>
                                            <a data-value='{"line":"25"}' href="javascript:;">25线</a>
                                            <a data-value='{"line":"30"}' href="javascript:;">30线</a>
                                            <a data-value='{"line":"MUL"}' href="javascript:;">多旋转</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"AMA"}' href="javascript:;">奖池游戏</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-dt" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"DEM"}' href="javascript:;">试玩</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                            <a data-type="tag" data-value='{"tag":"AMA"}' href="javascript:;">奖池游戏</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-qt" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"CLA"}' href="javascript:;">经典</a>
                                            <a data-value='{"tag":"ELE"}' href="javascript:;">电动</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                            <a data-type="tag" data-value='{"tag":"AMA"}' href="javascript:;">奖池游戏</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-nt" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"CLA"}' href="javascript:;">经典</a>
                                            <a data-value='{"tag":"ELE"}' href="javascript:;">电动</a>
                                            <a data-value='{"tag":"MIN"}' href="javascript:;">迷你</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>游戏风格:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-type="tag" data-value='{"tag":"AMA"}' href="javascript:;">奖池游戏</a>
                                            <a data-type="tag" data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-type="tag" data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-png" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"NEW"}' href="javascript:;">最新</a>
                                            <a data-value='{"tag":"HOT"}' href="javascript:;">热门</a>
                                            <a data-value='{"tag":"CLA"}' href="javascript:;">经典</a>
                                            <a data-value='{"tag":"AMA"}' href="javascript:;">奖池</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>赔钱线数:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"line":"1"}' href="javascript:;">单线</a>
                                            <a data-value='{"line":"1-10"}' href="javascript:;">1-10线</a>
                                            <a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>
                                            <a data-value='{"line":"25"}' href="javascript:;">25线</a>
                                            <a data-value='{"line":"243"}' href="javascript:;">243线</a>
                                            <a data-value='{"line":"other"}' href="javascript:;">其他</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-ag" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"RQ"}' href="javascript:;">热门</a>
                                            <a data-value='{"tag":"CF"}' href="javascript:;">中奖率排行</a>
                                            <a data-value='{"tag":"LS"}' href="javascript:;">流水排行</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>赔钱线数:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"line":"5-10"}' href="javascript:;">5-10线</a>
                                            <a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>
                                            <a data-value='{"line":"25-30"}' href="javascript:;">25-30线</a>
                                            <a data-value='{"line":"other"}' href="javascript:;">其他</a>
                                        </dd>
                                    </dl>
                                </div>
                                <div id="tab-bbin" class="tab-panel">
                                    <dl class="search-row">
                                        <dt>游戏类型:</dt>
                                        <dd> <a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"tag":"RQ"}' href="javascript:;">热门</a>
                                            <a data-value='{"tag":"CF"}' href="javascript:;">中奖率排行</a>
                                            <a data-value='{"tag":"LS"}' href="javascript:;">流水排行</a>
                                        </dd>
                                    </dl>
                                    <dl class="search-row">
                                        <dt>赔钱线数:</dt>
                                        <dd><a class="active" data-value="" href="javascript:;">全选</a>
                                            <a data-value='{"line":"5-10"}' href="javascript:;">5-10线</a>
                                            <a data-value='{"line":"15-20"}' href="javascript:;">15-20线</a>
                                            <a data-value='{"line":"25-30"}' href="javascript:;">25-30线</a>
                                            <a data-value='{"line":"other"}' href="javascript:;">其他</a>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="winner-box fr search_info">
                <div class="search-hd" id="j-searchForm">
                    <div class="ipt-group g_left">
                        <label for="" class="label g_left">搜索游戏:</label> 
                        <div class="g_left">
                            <input type="text" class="ipt j-ipt" placeholder="请输入游戏名称"> <i class="iconfont icon-search btn-search j-btnSearch"></i>
                        </div>
                        <div class="select_list j-select">
                        </div>
                    </div>
                    <div>
                         <a href="javascript:;" id="j-resetBtn" class="btn mb10">重置条件</a>
                    </div>
                </div>                
            </div>
   </div>

        <div class="game-list cfx">
            <ul class="aside-menu  no-padding">
                <li class="user_sc"><a id="j-favoriteAction" class="j-login" href="javascript:;"><span></span></a></li>
                <li class="user_wg"><a id="j-historyAction" href="javascript:;"><span></span></a></li>
            </ul>
            <div id="j-gameContainer"></div>
        </div>
    </div>
</div>    
</div>
</div>
<!--DT客户端下载{-->
<div class="modal fade" id="j-tip" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">温馨提示：</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-bd">
                <h3 class="f18 mb20 c-red">温馨提示：您的浏览器暂不支持该游戏， 请您到官网下载手机客户端或者选择其他浏览器进行游戏，推荐浏览器：</h3>
                <div class="m-row text-center">
                    <div class="col-6">
                        <a class="c-black" href="https://www.google.com/chrome/browser/desktop/index.html" target="_blank"><img src="/images/icon/chrome.png" width="50" height="50"  alt="">
                            <p class="text-center">谷歌浏览器</p></a>
                    </div>
                    <div class="col-6">
                        <a class="c-black" href="http://www.firefox.com.cn/" target="_blank"><img src="/images/icon/firefox.png" width="50" height="50" alt="">
                            <p class="text-center">火狐浏览器</p></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--}DT客户端下载-->
<script id="tpl" type="text/x-handlebars-template">
    <li data-marquee-item="" class="item cfx"> <a href="javascript:;">
        <div class="game-pic2">
            <img src="{{img}}" width="64" height="64"></div>
         <div>
         <div class="text_box"> 
        <p>恭喜{{location}}玩家<em class="c-white">{{winner}}</em>在{{type}}游戏</p>
        <p><em class="c-white">{{gamename}}</em>赢得<em class="f18 c-strong">{{win}}</em>元</p>
        <p>{{date}}</p>
        </div>
     </a></li>
    <li data-marquee-item="" class="item cfx"> <a href="javascript:;">
        <div class="game-pic2">
            <img src="{{img}}" width="64" height="64"></div>
         <div>
         <div class="text_box"> 
        <p>恭喜{{location}}玩家<em class="c-white">{{winner}}</em>在{{type}}游戏</p>
        <p><em class="c-white">{{gamename}}</em>赢得<em class="f18 c-strong">{{win}}</em>元</p>
        <p>{{date}}</p>
        </div>
     </a></li>     

</script>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<input type="hidden" id="j-gameurl" value="${session.gameurl}">
<input type="hidden" id="j-slotKey" value="${session.slotKey}">
<input type="hidden" id="j-referWebsite" value="${session.referWebsite}">
<input type="hidden" id="j-isLogin" value="${session.customer!=null}">
<script src="/js/swfobject.js"></script>
<script src="/js/lib/countUp.js"></script>
<script src="/js/lib/jquery.lazyload-v1.9.1.min.js"></script>
<script src="/js/lib/webglhelper.js"></script>
<script src="/js/game.js?90000008"></script> 
<script src="/js/superslide.2.1.js"></script>
<script>
    $(function(){
        //setJackPot();
    var url=window.location.search;
        
        if(url==="?showtype=DT"){
            $(".jackpot_text").find("img").attr("src","images/game/jackpot_name.png");
            $(".dt_jack").next(".fr").addClass("fr_dt");
            $(".pt_jack").hide();
            $(".dt_jack").show(); 
        }
        
        $("#j-gameMenu").find("li").click(function(){
            if($("#j-gameMenu").find("li").eq(1).hasClass("active")){
            $(".jackpot_text").find("img").attr("src","images/game/jackpot_name.png");               
            $(".pt_jack").hide();
            $(".dt_jack").show();   
            $(".dt_jack").next(".fr").addClass("fr_dt");            
            }else{
            $(".jackpot_text").find("img").attr("src","images/game/jackpot_name.png");  
            $(".pt_jack").show();
            $(".dt_jack").hide();
            $(".pt_jack").next(".fr").removeClass("fr_dt");
            }
        })
        
        $("#j-filter").find("dd").children("a").click(function(){
            if($("#j-filter").find("dd").children("a").eq(1).hasClass("active")){
            $(".jackpot_text").find("img").attr("src","images/game/jackpot_name.png");       
            $(".pt_jack").hide();
            $(".dt_jack").show();   
            $(".dt_jack").next(".fr").addClass("fr_dt");
            
            $(this).eq($(this).index())
            
            }else{
            $(".jackpot_text").find("img").attr("src","images/game/jackpot_name.png");  
            $(".pt_jack").show();
            $(".dt_jack").hide();   
            $(".fr").removeClass("fr_dt");
            }
        })      
        
            // function setJackPot(){

            //     $.post("/asp/dtJackpot.aspx", function (response) {
            //         console.log(response.pot);
            //         if(CountUp){
            //             var demo = new CountUp("j-jackpotCount", response.pot, 9194572, 2, 3000000000, {
            //                 useEasing : true,
            //                 useGrouping : true,
            //                 separator : ',',
            //                 decimal : '.',
            //                 formate : true,
            //                 suffix : ''
            //             });
            //             demo.start();
            //         }

            //     });
            // }
            
//      $("#j-gameContainer>.game-info").hover(function(){
//              $(this).find("h4").css("background-color","#0056AD");
//      },function(){
//          
//      })

  //    中奖喜讯
    jQuery(".winner_box").slide({ titCell: ".hd ul", mainCell: ".bd ul", autoPage: true, effect: "top", autoPlay: true });
    
    })
</script>

</body>
</html>