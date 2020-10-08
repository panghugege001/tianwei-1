﻿<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="dfh.action.vo.ActivityCalendarVO" %>
<%@ page import="dfh.utils.AxisSecurityEncryptUtil" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
    List<ActivityCalendarVO> activityCalendars = AxisSecurityEncryptUtil.queryTopActivity(Integer.MAX_VALUE);
    application.setAttribute("activityCalendars", activityCalendars);
%>
<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<base href="<%=request.getRequestURL()%>"/>
<jsp:include page="${ctx}/tpl/linkResource.jsp?v=1"></jsp:include>
<link rel="stylesheet" href="css/new_index.css?v=53">
</head>
<style>
.guanbi_f1 { display: inline-block; width: 40px; position: absolute; height: 39px; right: -7px; top: -7px; cursor: pointer; }
.guanbi_f2 { display: inline-block; width: 40px; position: absolute; height: 39px; right: -7px; top: -7px; cursor: pointer; }
</style>

<body class="index_body">
<div class="index-bg" style="position: relative;">
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
  <div class="new_bannar_box">
    <!-- <div class="bd_center">
      <ul>
        <li> <a href="/activety/qixi/index.jsp"> <img src="images/banner/index/pc_qixi.jpg"/> </a> </li>
        <li> <a href="/promotion.jsp"> <img src="images/banner/index/cicun.jpg"/> </a> </li>
        <li> <a href="/slotGame.jsp?showtype=MGS"> <img src="images/banner/index/mghot.jpg"/> </a> </li>
        <li> <a href="/activety/jzsjb/index.jsp"> <img src="images/banner/index/jzsjb.jpg"/> </a> </li>
        <li> <a href="/activety/tjhy/index.jsp""> <img src="images/banner/index/tjhy.jpg"/> </a> </li>
      </ul>
    </div>
    <div class="hd_center">
      <ul class="w_1200">
        <li><img src="/images/banner/icon/b1.png" alt=""/><img class="active" src="/images/banner/icon/b1-0.png" alt=""/>合作乐虎</li>
          <li><img src="/images/banner/icon/icon_qixi1.png" alt=""/><img class="active" src="/images/banner/icon/icon_qixi2.png" alt=""/>七夕拥抱爱无限</li>
          <li><img src="/images/banner/icon/b10.png" alt=""/><img class="active" src="/images/banner/icon/b10-0.png" alt=""/>次次存笔笔送</li>
          <li><img src="/images/banner/icon/mg-icon.png" alt=""/><img class="active" src="/images/banner/icon/mg-hover.png" alt=""/>热门游戏</li>
          <li><img src="/images/banner/icon/icon01.png" alt=""/><img class="active" src="/images/banner/icon/icon01-hover.png" alt=""/>决战世界杯 </li>
        <li><img src="/images/banner/icon/b4.png" alt=""/><img class="active" src="/images/banner/icon/b4-0.png" alt=""/>推荐好友 </li>
        <li><img src="/images/banner/icon/b2.png" alt=""/><img class="active" src="/images/banner/icon/b2-0.png" alt=""/>久安钱包 </li>
      </ul>
    </div> -->
  </div>
  <div class="index_massage">
    <div class="w_1200 ">
      <!-- <div class="gb-news box-shadow"><span><img src="images/new_index/new_gonggao.png"></span> <a href="/newsList.jsp" target="_blank"><img src="images/new_index/gonggao_jia.png"></a> </i>
        <jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>
      </div> -->
      <div class="index_center">
          <div class="index_center_no1">
                    <ul>
                      <li style="height: 140px;"> <a href="/phone2.jsp" class="game_down"></a> </li>
                      <li style="height: 108px;"> <a href="/agent.jsp" class="index_bangzhu"> </a> </li>
                      <li style="height: 108px;"> <a href="/activety/vip2/index.jsp" class="index_vip"></a> </li>
                    </ul>
                  </div>
                  <div class="index_center_no2">
                    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
            <!-- Indicators -->
                      <ol class="carousel-indicators">
                        <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="3"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="4"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="5"></li>
                      </ol>

                      <!-- Wrapper for slides -->
                      <div class="carousel-inner" role="listbox">
                        <div class="item active">
                          <img src="images/new_index/znxb_pc.jpg" alt="...">
                          <div class="carousel-caption">
                          </div>
                        </div>
                        <div class="item">
                          <img src="images/new_index/nnyujackpot_pc.jpg" alt="...">
                          <div class="carousel-caption">
                          </div>
                        </div>
                        <div class="item">
                          <img src="images/new_index/bgshoucun.jpg" alt="...">
                          <div class="carousel-caption">
                          </div>
                        </div>
                        <div class="item">
                          <img src="images/new_index/vipzm_pc.jpg" alt="...">
                          <div class="carousel-caption">
                          </div>
                        </div>
                        <div class="item">
                          <img src="images/new_index/tjxn_pc.jpg" alt="...">
                          <div class="carousel-caption">
                          </div>
                        </div>
                        <div class="item">
                          <img src="images/new_index/yqhy_pc.jpg" alt="...">
                          <div class="carousel-caption">
                          </div>
                        </div>
                      </div>

            <!-- Controls -->
            <!-- <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
              <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
              <span class="sr-only"></span>
            </a>
            <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
              <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
              <span class="sr-only"></span>
            </a> -->
          </div>

        </div>
        <div class="index_center_no3">
          <div class="index_center_no3_title">
            <ul class="index_title">
              <li>公告区</li>
              <li>幸运榜单</li>
              <!-- <li>玩家论坛</li> -->
              <!-- <a # class="icon_jia"><img
                                    src="images/new_index/jia_icon.jpg"></a> -->
            </ul>
          </div>
          <div class="post-list-box title_post" style="display: none;">
            <h3>天威体验金8~88随机领取</h3>
            <p>游天威乐天威，彩金好礼送给你</p>
            <div class="post_ul">
              <ul class="calendar post-list" id="j-calendar" tyle="padding-left: 25px; padding-right: 22px;">
                <s:if test="#application.activityCalendars.size() <= 0"> 暂无公告...</s:if>
                <s:else>
                  <c:forEach items="${activityCalendars}" var="item" varStatus="loop">
                    <c:if test="${loop.index < 6}">
                      <li> <a href="${ctx}/getActivityCalendarDetail.aspx?id=${item.id}"
                                                   target="_blank">
                        <c:choose>
                          <c:when test="${fn:length(item.name) > 10}">
                            <c:out value="${fn:substring(item.name, 0, 20)}..."/>
                          </c:when>
                          <c:otherwise>
                            <c:out value="${item.name}"/>
                          </c:otherwise>
                        </c:choose>
                        </a> <em style="float:right">${item.activityDate}</em></li>
                    </c:if>
                  </c:forEach>
                </s:else>
              </ul>
            </div>
          </div>
          <div class="post-list-box aside-left winner-box box">
            <ul class="inner" id="j-winner-box">
            </ul>
          </div>
          <!-- <div class="post-list-box title_post" style="display: none;">
            <h3>DT赛亚烈战最近大爆发</h3>
            <p>恭喜da**玩家击中免费获得30167元</p>
            <div class="post_ul" style="overflow-y: auto;">
              <ul class="post-list" id="j-postList">
              </ul>
            </div>
          </div> -->
        </div>
      </div>
    </div>
  </div>
  <div class="h_788">
    <div class="banner-top mb20">
      <div class="jackpot-top fl pt_jack">
        <div class="jackpot_text"><img src="images/game/jackpot_name.png"></div>
        <div class="jackpot-div" id="j-jackpotCount"> <span class="num-item small">&yen;</span> </div>
      </div>
    </div>
    <div class="hotgame-box">
      <jsp:include page="${ctx}/hotgame.jsp"></jsp:include>
      <!--             <span class="left_btn_game"><img src="images/new_index/left_btn.png"></span>
<span class="right_btn_game"><img src="images/new_index/right_btn.png"></span> -->
    </div>
    <div class="w_1200">
      <div class="gallery-box" id="j-gallery">
        <div class="gallery-item active">
          <div class="pic">
            <div class="gallery_list1 gallery_list">
              <div class="gallery_list1_box">
                <ul>
                  <li> BG国际厅 <a href="javascript:;" onclick="bgLiveGame()">进入游戏</a> </li>
<!--                   <li> VR彩票 <a href="/gameNTwoRedirect.aspx" class="j-check">进入游戏</a> </li> -->
                </ul>
              </div>
            </div>
            <i class="gallery-icon gallery1"></i> </div>
        </div>
        <div class="gallery-item">
          <div class="pic">
            <div class="gallery_list2 gallery_list">
              <ul style="margin-left: 285px;">
                 <li>捕鱼达人<a href="javascript:;" onclick="bbinFishLogin('30599')" title="捕鱼游戏">进入游戏</a> </li>
                 <li>捕鱼大师<a href="javascript:;" onclick="bbinFishLogin('38001')" title="捕鱼游戏">进入游戏</a> </li>
              </ul>
            </div>
            <i class="gallery-icon gallery2"></i> </div>
        </div>
        <div class="gallery-item">
          <div class="pic">
            <div class="gallery_list3 gallery_list">
              <ul>
                <li>BB体育<a href="javascript:;" onclick="bbSportGame()" class="pbLogin"> 进入游戏</a> </li>
                <li>沙巴体育<a href="javascript:;" onclick="sportGame()" class="sbLogin">进入游戏</a> </li>
              </ul>
            </div>
            <i class="gallery-icon gallery3"></i> </div>
        </div>
      </div>
    </div>
  </div>
  <!-- <div class="foot_dao">
    <div class="w_1200 foot_bangzhu">
      <ul>
        <li> <a href=""> <i class="inco_list1" id="inco_list1"></i>
          <div class="inco_list1_div">
            <h3>25秒</h3>
            <p>存款到账时间</p>
          </div>
          </a> </li>
        <li> <a href=""> <i class="inco_list2"></i>
          <div class="inco_list1_div">
            <h3>2分57秒</h3>
            <p>取款到账时间</p>
          </div>
          </a> </li>
        <li> <a href="javascript:alert('敬请期待');"> <i class="inco_list3"></i> <span>新手帮助
          <p>一站式服务</p>
          </span> </a> </li>
        <li> <a href="/agent.jsp"> <i class="inco_list4"></i> <span>代理加盟
          <p>诚信服务</p>
          </span> </a> </li>
        <li> <a #> <i class="inco_list5"></i> <span>玩家论坛
          <p>互动交流</p>
          </span> </a> </li>
      </ul>
    </div>
  </div> -->
  <div id="asidebar">
    <div class="item"> 
      <a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank" class="chat-service">
        <i class="iconfont"><span class="icon_img1"></span></i>
      <div class="txt">在线客服</div>
      </a> 
    </div>
    <!--<div class="item"><a href="http://b.qq.com/webc.htm?new=0&sid=800157062&o=www.tianwei4.com/&q=7" target="_blank"> <i class="iconfont icon-qq"></i>
<div class="txt">QQ客服</div>
</a></div>-->
    <!-- <div class="item"> <a href="javascript:;" data-toggle="modal" data-target="#j-modal-tel"> <i class="iconfont"><span
                    class="icon_img2"></span></i>
      <div class="txt">电话回拨</div>
      </a> </div> -->
    <div class="item"> <a href="javascript:;"> <i class="iconfont"><span class="icon_img3"></span></i>
      <div class="txt">tianwei661@gmail.com</div>
      </a> </div>
    <div class="line"></div>
    <div class="line"></div>
  </div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<!-- <div style="position: fixed;top: 245px; z-index: 10;"> <a class="span_img_1" href="/IntegralShop/integral.jsp"><img src="images/new_index/jfsc.png"></a><span class="guanbi_f1"></span></div> -->
<!-- <div style="position: fixed;top: 445px; z-index: 10;"> <a class="span_img_1" href="/activety/sfyc/index.jsp"><img src="images/new_index/sfyc.png"></a><span class="guanbi_f1"></span></div> -->
<!-- <div style="position: fixed;top: 574px;z-index: 10;left: 16px;">
    <a class="span_img_2" href="https://wj.qq.com/s/1998690/7464"><img src="images/new_index/wenjuan.png"></a><span
        class="guanbi_f2"></span></div> -->
<!--首页公告弹框{-->
<div class="modal fade" id="modal-index" tabindex="-1" role="dialog" data-modal-load="" aria-labelledby="myModalLabel" style="display: none;">
  <%--
<canvas id="canvas"></canvas>
--%>
  <div class="modal-dialog modal-dialog_sy" id="index_tan" role="document">
    <img src="/images/index/modal/girl.png" alt="" class="girl-gugu">
    <h1 class="notice-tit"></h1>

    <div class="notice-wp">
      <div class="notice-cnt"></div>
      <!-- <div class="erweima_box">
      <div class="erweima_50 erweima_50_left"> <img src="images/appxiazai/pt-ewm.png" style="height: 110px; margin-top: 7px;">
        <div class="erweima_text"> <span>PT客户端</span>
        <p>立即下载PT客户端</p>
        </div>
      </div>
      <div class="erweima_50 erweima_50_rigth"> <img src="images/appxiazai/dt-ios.png">
        <div class="erweima_text"> <span>关注天威</span>
        <p>立即下载DT客户端 </p>
        </div>
        </div>
      </div> -->

    </div>
    <button type="button" class="closebtn" data-dismiss="modal" aria-label="Close"></button>
    <div class="know"><input type="checkbox" id="ts-know">明白了，姑姑别提示啦！</div>
    <div class="ui-scrollnews easing" id="breakingnews">
            <ul>
                <li><a href="javascript:;" target="_blank">恭喜玩家zdd****在&nbsp;&nbsp;PT海滨嘉年华下注7.5元&nbsp;&nbsp;喜赢375000元</a></li>
                <li><a href="javascript:;" target="_blank">恭喜玩家hyf****在&nbsp;&nbsp;PT年年有余下注0.45元&nbsp;&nbsp;喜赢800000元</a></li>
                <li><a href="javascript:;" target="_blank">恭喜玩家damia××××在&nbsp;&nbsp;PT年年有余下注2.25元&nbsp;&nbsp;喜赢626429元</a></li>
                <li><a href="javascript:;" target="_blank">恭喜玩家zuo××××在&nbsp;&nbsp;DT火影忍者下注300元&nbsp;&nbsp;喜赢1100000元</a></li>
                <li><a href="javascript:;" target="_blank">恭喜玩家gq1××××在&nbsp;&nbsp;PT酷炫水果下注8元&nbsp;&nbsp;喜赢724553元</a></li>
            </ul>
        </div>
  </div>
</div>
<!--}首页公告弹框-->

<!--首页活动弹框{-->
<div class="modal fade" id="modal-activity" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  </div>
</div>
<!--}首页活动弹框-->

<!--礼物申请{-->
<%--
<div class="modal fade" id="j-modal-gift" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
style="display: none;">
<div class="modal-dialog" role="document">
<div class="modal-content">
    <div class="modal-hd">
        <h2 class="modal-title">中秋节礼物申请</h2>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
    </div>
    <div class="modal-bd">
        <iframe data-src="/applyGift.jsp" src="" width="100%" height="360" scrolling="no"
                frameborder="0"></iframe>
    </div>
</div>
</div>
</div>
--%>
<!--}礼物申请-->

<!-- <a href="https://wj.qq.com/s/1998690/7464" target="_blank" style="position: fixed;left: 26px;bottom: 17%;z-index: 99">
<img src="/images/aside/qustion.png" width="151" height="141" alt="">
</a> -->
<input type="hidden" id="j-isLogin" value="${session.customer!=null}">
<script id="tpl" type="text/x-handlebars-template">
    <li data-marquee-item="" class="item cfx">
        <a href="javascript:;">
            <div class="game-pic">
                <img src="{{img}}" width="64" height="64"></div>
            <p>恭喜{{location}}玩家<em class="c-lanse">{{winner}}</em></p>
            <p>在{{type}}游戏<em class="c-huangse">{{gamename}}</em>中</p>
            <p>喜提<em class="f18 c-red">{{win}}</em>元</p>
        </a>
    </li>

</script>
<script id="j-tplPost" type="text/x-handlebars-template">
    <li class="item">
        <a target="_blank" href="{{link}}">{{title}} <span class="fr">{{createDate}}</span></a>
    </li>
</script>
<script src="/js/lib/countUp.js"></script>
<script src="/js/index.js?v=0123"></script>
<script src="/js/superslide.2.1.js"></script>
<c:if test="${session.customer==null}">
  <script>
        $(function () {
            $(document).on('click', '.j-play,.j-check', '.enter-game', function () {
                alert('请先登录！！！');
                return false;
            });
        });
    </script>
</c:if>
<script>
    jQuery(".new_bannar_box").slide({
        titCell: ".hd_center ul li",
        mainCell: ".bd_center ul",
        effect: "fold",
        autoPlay: true,
        interTime: 5000,
        delayTime:1000
    });
    $(function () {
        $("#ck_app").click(function () {
            $.ajax({
                url: "/asp/checkAgentURLogin.aspx",
                type: "post",
                dataType: "text",
                success: function (data) {
                    if ('false' == data) {
                        alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
                        $("#modal-reg").modal('show');
                        $("#modal-app").modal('hide');
                        $(".modal-dialog").hide();

                    } else {
                        $("#modal-app").modal('show');
                        $(".modal-dialog").show();
                        $("#modal-reg").modal('hide');
                    }
                }
            });
        })

        $(".close").click(function () {
            var $that = $(this).parent();
            $that.hide();
        })

        $(".hide-btn").find(".enter-game").click(function () {
            if ($("#massage").val() == "true") {
                alert("请先登入账号...");
                return false;
            }
        })

        $(".j-check").click(function () {
            if ($("#massage").val() == "true") {
                alert("请先登入账号...");
                return false;
            }
        })

        $(".span_img_1").click(function () {
            if ($("#massage").val() == "true") {
                alert("请先登入账号...");
                return false;
            } else {
                window.location.href = "/userManage.jsp"
            }
        })

        $(".guanbi_f1").click(function () {
            $(".span_img_1").hide();
        })
        $(".guanbi_f2").click(function () {
            $(".span_img_2").hide();
        })

        $(".guanbi").click(function () {
            $(".wenjuan").hide();
        })

    })
</script>
<script>
    function jackPot() {
        if (CountUp) {
            var options = {
                useEasing: true,
                useGrouping: true,
                separator: ',',
                decimal: '.',
                prefix: '',
                formate: true,
                suffix: ''
            };
            var demo = new CountUp("j-jackpotCount", 219457295, 919457295, 2, 50000000000, options);
            demo.start();
        }
    }

    jackPot();

    function mwGame(type) {
        if ($("#j-isLogin").val()) {
            openProgressBar();
            $.post("/asp/mwgLogin.aspx", {
                "gameCode": type
            }, function (response) {
                closeProgressBar();
                if (response.code == 0) {
                    window.location.href = response.data;
                } else {
                    alert(response.msg);
                }
            })
        } else {
            alert('您好，请先登录！');
        }
    }
    
    function sportGame(type) {
		if ($("#j-isLogin").val()) {
			openProgressBar();
			$.post("/game/sbLogin.aspx", function(response) {
				closeProgressBar();
				if (response.code == 0) {
					window.location.href = response.data;
				} else {
					alert(response.msg);
				}
			})
		} else {
			alert('您好，请先登录！');
		}
	}

	
	function bbSportGame(type) {
		if ($("#j-isLogin").val()) {
			openProgressBar();
			$.post("/game/bbinPcLogin.aspx", function(response) {
				closeProgressBar();
				if (response.code == 0) {
					window.location.href = response.data;
				} else {
					alert(response.msg);
				}
			})
		} else {
			alert('您好，请先登录！');
		}
	}
	
	function bgLiveGame(type) {
		if ($("#j-isLogin").val()) {
			openProgressBar();
			$.post("/game/bgLogin.aspx", function(response) {
				closeProgressBar();
				if (response.code == 0) {
					window.location.href = response.data;
				} else {
					alert(response.msg);
				}
			})
		} else {
			alert('您好，请先登录！');
		}
	}
	
	function bbinFishLogin(type) {
		if ($("#j-isLogin").val()) {
			openProgressBar();
		$.post("/game/bbinFishLogin.aspx", {
				"gameCode" : type
			},  function(response) {
				closeProgressBar();
				if (response.code == 0) {
					window.location.href = response.data;
				} else {
					alert(response.msg);
				}
			})
		} else {
			alert('您好，请先登录！');
		}
	}
</script>

</body>
</html>