﻿<%@ page import="dfh.model.Users"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.util.Calendar"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
    HttpSession chksession = request.getSession(true);
    Users user = (Users) chksession.getValue("customer");
    if (user == null) {
%>
<script language="javascript">
    alert("你的登录已过期，请从首页重新登录");
    window.location.href="/index.jsp";
</script> 
<%
        //request.getRequestDispatcher("/index.asp").forward(request, response);
        //response.sendRedirect("/index.asp");
    }
%>
<div id="winner-slide" class="user-banner-top carousel slide mb20 mt10" data-ride="carousel" data-interval="false">
    <!-- 轮播（Carousel）项目 -->
    <!--<div class="carousel-inner">
        <div class="item active"> <a href="/promotion.jsp?fuqin" target="_blank"> <img src="/images/user/banner/banner-top.png?v=12" width="1170" height="155" class="a-rotateinLT"></a> </div>
    </div>-->
</div>
<div class="user-profile clearfix">
    <!--<div class="fr aside-box">
        <div class="meet-box">
            <h3>快来体验"公会"系统吧</h3>
            <div class="mt10">
                <a href="javascript:alert('敬请期待')" class="btn-meet btn-gh1">加入公会</a><a href="javascript:alert('敬请期待')" class="btn-meet btn-gh2">创建公会</a>
            </div>
            <img src="${ctx}/images/user/pic.png" class="pic a-bounceinL" />
        </div>
        <a href="/promotion.jsp?showid=mail" target="_blank" class="item">
            <i class="order"></i>
            <p class="text-center">订阅获优惠</p> 
        </a>
        <a href="/topic/cunsong.html" target="_blank" class="item">
            <i class="app"></i>
            <p class="text-center">高额存送</p> 
        </a>
    </div>-->
    <div  class="u-info">
        <div class="user_leftbox">
            <div class="user_img">
                <span class="c-strong">新会员</span>
            </div>
            <div class="user_cz" style=" list-style: none; position: relative; top: -130px; left: 185px; line-height: 40px;">
                <a class="check_name" href="/userManage.jsp?tab_deposit">存款</a><br>
                <a href="/userManage.jsp?tab_withdraw">提款</a><br>
                <a href="/userManage.jsp?tab_transfer">转账</a>
            </div>
            <div class="user_caozuo">
                <ul style="">
                    <li class="active">
                        <label>账户余额：</label>
                        <span class="u_money">0</span>
                        <label>元</label>
                        <i class="iconfont icon-shuaxin" onclick="refreshBalance();"><img src="../images/new_index/shuaxin_icon.png"></i>
                    </li>
                    <li class="active" style="    padding-left: 20px;">
                        <label>签到余额：</label>
                        <span class="u_money">0</span>
                        <label>元</label>
                        <i class="iconfont icon-shuaxin" onclick="refreshBalance();"><img src="../images/new_index/shuaxin_icon.png"></i>
                    </li>
                    <li class="active" style="    padding-left: 20px;">
                        <label>红包余额：</label>
                        <span class="u_money">0</span>
                        <label>元</label>
                        <i class="iconfont icon-shuaxin" onclick="refreshBalance();"><img src="../images/new_index/shuaxin_icon.png"></i>
                    </li>

                    <li style="margin-top: 10px; font-size: 14px;">
                        <label class="datatype">中午好，</label>
                        <span class="span_name">windtest</span>
                        <label>欢迎回来</label>
                    </li>
                    <li style="width: 100%;">
                        <p style="font-size: 13px;">
                            上次登入时间：<span class="user_out_time">2018-12-10 13:31:37</span>
                        </p>
                    </li>

                </ul>
            </div>
        </div>
        <div class="cfx info-hd">
          <%-- <div class="level-pic"><i class="sp-level sp-level-${session.customer.level}"></i></div>--%>
            <div class="level-list" id="j-levelList">
                <div class="level-line"></div>
                <div class="active-line"></div>
                <div class="level-item">
                    <c:choose>  
                       <c:when test="${session.customer.level==0}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">天兵</div>
                </div>
                <div class="level-item">
                     <c:choose>  
                       <c:when test="${session.customer.level==1}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose> 
                    <div class="dot"></div>
                    <div class="level-name">天将</div> 
                    <div class="level-desc">单月总投注额<br>大于等于10W<div class="icon_sanjiao"></div></div>
                </div>
                <div class="level-item">
                    <c:choose>  
                       <c:when test="${session.customer.level==2}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">天王</div> 
                     <div class="level-desc">单月总投注额<br>大于等于30W<div class="icon_sanjiao"></div></div>
                </div>
                <div class="level-item">
                     <c:choose>  
                       <c:when test="${session.customer.level==3}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">星君</div>  
                    <div class="level-desc">单月总投注额<br>大于等于80W<div class="icon_sanjiao"></div></div>
                </div>
                <div class="level-item">
                    <c:choose>  
                       <c:when test="${session.customer.level==4}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">真君</div> 
                    <div class="level-desc">单月总投注额<br>大于等于180W<div class="icon_sanjiao"></div></div>
                </div>
                <div class="level-item">
                   <c:choose>  
                       <c:when test="${session.customer.level==5}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>   
                    <div class="dot"></div>
                    <div class="level-name">仙君</div>
                    <div class="level-desc">单月总投注额<br>大于等于400W<div class="icon_sanjiao"></div></div> 
                </div>
                <div class="level-item">
                   <c:choose>  
                       <c:when test="${session.customer.level==6}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">帝君</div> 
                      <div class="level-desc" style="left: -80%;">单月总投注额<br>大于等于800W<div class="icon_sanjiao"></div></div>
                </div>
                <div class="level-item">
                   <c:choose>  
                       <c:when test="${session.customer.level==6}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">天尊</div> 
                      <div class="level-desc" style="left: -80%;">单月总投注额<br>大于等于1500W<div class="icon_sanjiao"></div></div>
                </div>
                <div class="level-item">
                   <c:choose>  
                       <c:when test="${session.customer.level==6}"><div class="level-pic"></div></c:when>  
                       <c:otherwise>    
                        <div class="level-pic"></div>
                       </c:otherwise>  
                    </c:choose>  
                    <div class="dot"></div>
                    <div class="level-name">天帝</div> 
                      <div class="level-desc" style="left: -80%;">无需投注额<br>仅限特邀玩家<div class="icon_sanjiao"></div></div>
                </div>

            </div>

        <!-- <div class="huodong">
            <ul>
                <li class="list_1">
                    <a href="/promotion.jsp?qiandao">
                        <div class="hd_w50">
                            签到余额<br />
                            <span id="qiandao_money"></span><label></label>
                        </div>                  
                    </a>
                </li>
                <li class="list_2">
                    <a href="/promotion.jsp?zfb">
                        <div class="hd_w50">
                            红包余额<br />
                            <span id="hongbao_money"></span><label></label>
                        </div>                  
                    </a>
                </li>
                <li class="list_3">
                    <a href="javascript:;">
                        <div class="hd_w50">
                            微信群发
                        </div>                  
                    </a>
                </li>
                <li class="list_4">
                    <a href="http://ui.easeye.com.cn/Eventmail/UserRegisterPage.aspx?type=reg&guid=xSE1KVklJSE5O">
                        <div class="hd_w50">
                            邮件<br />
                            <span>订阅</span>
                        </div>                  
                    </a>
                </li>
            </ul>
        </div> -->
            
        </div>
        <div class="get_money">
            <div class="btn_div">
                <span  class="left_btn prev">
                    <img  src="../images/user/left_btn.png"/>
                </span>
                <span class="rigth_btn next">
                    <img src="../images/user/rigth_btn.png" />
                </span>
            </div>
            <div class="get_money_box">
                <ul class="ul">
                    <li>
                        <span class="span_gamename">PT账户</span>
                        <div class="show_moneyget">
                            <span>PT账户</span>
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                            <input type="hidden" value="newpt" />
                        </div>
                        <i class="fenge_rigth"></i>
                    </li>
                    <li>
                        <span class="span_gamename">TTG账户</span>
                        <div class="show_moneyget">
                            <span>TTG账户</span>
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                            <input type="hidden" value="ttg" />
                        </div>
                        <i class="fenge_rigth"></i>
                    </li>                   
                    <li>
                        <span class="span_gamename">老虎机账户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>老虎机账户</span>
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                            <input type="hidden" value="slot" />
                        </div>                      
                    </li>
                    <li>
                        <span class="span_gamename">AG账户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>AG账户</span>
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                            <input type="hidden" value="agin" />
                        </div>                      
                    </li>                                       
                    <li>
                        <span class="span_gamename">沙巴体育账户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>沙巴体育账户</span>
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                            
                            <input type="hidden" value="sba" />
                        </div>                      
                    </li>                   
                    <li>
                        <span class="span_gamename">N2账户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>N2账户</span>
                            <input type="hidden" value="n2live" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>
                    <li>
                        <span class="span_gamename">MW账户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>MW账户</span>
                            <input type="hidden" value="mwg" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>                   
                    <li>
                        <span class="span_gamename">761账户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>761账户</span>
                            <input type="hidden" value="chess" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>  
                     <li>
                        <span class="span_gamename">开元棋牌</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>开元棋牌</span>
                            <input type="hidden" value="kyqp" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>  
                     <li>
                        <span class="span_gamename">VR彩票</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>VR彩票</span>
                            <input type="hidden" value="vr" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>                 
                    <li>
                        <span class="span_gamename">捕鱼帐户</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>捕鱼帐户</span>
                            <input type="hidden" value="fish" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li> 
                    <li>
                        <span class="span_gamename">平博体育</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>平博体育</span>
                            <input type="hidden" value="pb" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>  
                    <li>
                        <span class="span_gamename">BBIN</span>
                        <i class="fenge_rigth"></i>
                        <div class="show_moneyget">
                            <span>BBIN</span>
                            <input type="hidden" value="bbin" />
                            <span class="get_user"><i class="shuaxin_i"></i>元</span>
                        </div>                      
                    </li>                 
                </ul>
            </div>
        </div>
        <div class="user_rigyh_bj"></div>
        <!--<div class="info-bd">
            <div class="item">欢迎您：<span class="cee6">${customer.loginname}</span></div>
            <div class="item">账号余额：<span class="j-balance-total cee6">${customer.credit}</span></div>
            <div class="item u-balance">
                <span>平台：</span>
                <select name="" id="j-topBalanceSelect" class="select">
                    <option value="newpt" selected>PT账户</option>
                    <option value="mg">MG账户</option>
                    <option value="png">PNG账户</option>
                    <option value="agin">
                        AG账户
                    </option>
                    <option value="dt">
                        DT账户
                    </option>
                    <option value="ttg">
                        TTG账户
                    </option>
                    <option value="qt">
                        QT账户
                    </option>  
                    <option value="nt">NT账户</option>
                    <option value="sba">沙巴体育账户</option> 
                </select>
                <span>&nbsp;&nbsp;余额：</span>
                <span id="j-topBalance" class="balance-txt cee6">加载中...</span>
            </div>
            <div class="item">签到余额：<span id="todayGet11" class="cee6">0.00</span>元</div>
            <%--<div class="item">可用积分：<span id="j-friendPoint" class="cee6"></span></div>--%>
        </div>-->
    </div>
</div>
<script type="text/javascript" src="../js/superslide.2.1.js"></script> 
<script>
        jQuery(".get_money").slide({titCell: ".hd ul", mainCell: ".get_money_box ul", autoPage: true, effect: "left", autoPlay: false, vis: 6, trigger: "click" });
</script>
<script>
    !function(){
        // 设置等级进度条
        function getLevel(){
            var level= parseInt('${session.customer.level}'),
                $levelList=$('#j-levelList'),
                item=$levelList.find('.level-item').eq(level),
                    line=$levelList.find('.active-line');
            line.animate({'width':(level/7*100).toString()+'%'},1800,function(){
                item.addClass('current active');
                item.prevAll('.level-item').addClass('active');
            });
        }
        //获取积分
        function getPoint(){
            $.post("/asp/queryPoints.aspx",
                    function (returnedData, status) {
                        if ("success" == status) {
                            var strs=returnedData.split("#");
                            $("#j-friendPoint").html(strs[0]);
                        }
            });
        }
 
        function tabLoad(){
            $('#j-userNav').find('a[data-toggle="tab"]').on('click', function (e) {
                // 获取已激活的标签页的名称
                var $target = $($(e.target).attr('href')),
                        $src=$target.find('[data-src]'),
                        src=$src.data('src');
                $src.length&&$src.attr('src',src);
            });

        }
        // 显示标签页
        function tabShow(){
            var target=Util.getQueryString('showid');
            chageNav(target);
        }

        function chageNav(target){
            var $nav=$('#j-userNav');
            if(target){
                $nav.find('a[href="#'+target+'"]').trigger('click');
            }
        }
        //获取游戏金额
//      function updateTopMonery(gameCode){
//          if(gameCode!=""){
//              $('#j-topBalance').html("加载中..");
//              $.post("/asp/getGameMoney.aspx", {
//                  "gameCode":gameCode
//              }, function (returnedData, status) {
//                  if ("success" == status) {
//                      $('#j-topBalance').html(returnedData);
//                  }
//              });
//          }
//      }
        function init(){
            $(function(){
                getLevel();
                //getPoint();
                tabLoad();
                tabShow();

                $(".get_money_box ul li").mouseenter(function(){
                    var gameCode=$(this).find("input").val();
                    var that=$(this);
                    if(gameCode!=""){
                         that.find(".show_moneyget").children(".get_user").html("加载中..");
                        $.post("/asp/getGameMoney.aspx", {
                            "gameCode":gameCode
                        }, function (returnedData, status) {
                            if ("success" == status) {
                                console.log($(this).find(".get_user").html())
                             that.find(".show_moneyget").children(".get_user").text(returnedData);
                            that.find(".get_user").children("i").remove();
                            }
                        });
                    }
                })
            });
        }
        
        function datatype(){
            var now=new Date();
            var hour=now.getHours();
            if(hour < 6){$('.datatype').text("凌晨好，")} 
            else if (hour < 9){$('.datatype').text("早上好，")} 
            else if (hour < 12){$('.datatype').text("上午好，")} 
            else if (hour < 14){$('.datatype').text("中午好，")} 
            else if (hour < 17){$('.datatype').text("下午好，")} 
            else if (hour < 19){$('.datatype').text("傍晚好，")} 
            else if (hour < 22){$('.datatype').text("晚上好，")} 
            else {$('.datatype').text("夜间好，")} 
        
        }
    //查询签到总余额
        function checkInSignAmount(){

            $.ajax({
                url : "/asp/querySignAmount.aspx",
                type : "post", // 请求方式
                dataType : "text", // 响应的数据类型
                data :"",
                async : true, // 异步
                success : function(msg) {
                    console.log(parseInt(msg))
                    
                    $("#todayGet11").text(msg)
//                  $("#j-check_in2").html("");
//                  $("#j-check_in2").html(msg+"元");
                },
            });
        } 
        
    function user_time(){
        $.get('/asp/queryLastLoginDate.aspx',function(data){
            $(".user_out_time").text(data)
        })
    }       
        user_time()
    checkInSignAmount();
        init();
        
        datatype();
    }();
    
    function user_time(){
        $.get('/asp/queryLastLoginDate.aspx',function(data){
            $(".user_out_time").text(data)
        })
    }
    user_time()
</script>