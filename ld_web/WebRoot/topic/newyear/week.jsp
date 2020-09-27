<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
  String infoValue=(String)request.getSession(true).getValue("infoValue4Live800");
  if(infoValue==null)infoValue="";
%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>金鸡报喜 天天有惊喜</title>
  <link href="/css/util/reset.css" rel="stylesheet"/>
  <link href="/css/util/common.css" rel="stylesheet"/>
  <link href="/css/util/animation.css" rel="stylesheet"/>

  <link href="css/week.css?v=8" rel="stylesheet"/>
</head>
<body>
<div id="header">
  <div class="container">
    <a href="/" class="fl"><img src="/images/logo.png" width="156" alt=""></a>
    <div class="fl at-link">
      <a href="/topic/represent/home.jsp">新年双庆</a>
      <a href="/topic/lover/home.jsp">情人节约会</a>
      <a href="/topic/newyear/home.jsp">恭贺新禧</a>
      <a href="/topic/newyear/week.jsp">金鸡报喜</a>
    </div>

    <div class="fr top-link">
      <c:if test="${session.customer==null}">
      <a data-target="#modal-login" data-toggle="modal" href="javascript:;">登录</a>
      </c:if>

      <a target="_blank" href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&amp;configID=23&amp;lan=zh&amp;jid=&amp;info=">联系客服</a>
    </div>
  </div>
</div>
<div class="page-bg">
  <div class="container">
    <div class="top-h">
      <h2 class="caption"></h2>
      <div class="tip-box">
        <p>▪ 活动对象： 天威全体会员</p>
        <p>▪ 活动内容： PT、MG、DT、TTG、NT、QT</p>
      </div>
      <p>金鸡报喜贺新春，财源滚滚好运来。天威又来送喜啦！“七喜”献上， <br>
        好运难挡！天威给您拜年啦！</p>
    </div>
  </div>


  <div class="main">
    <div class="main-bg"></div>
    <div class="container main-inner">
      <ul class="nav-list cfx">
        <li class="active"><a data-toggle="tab" href="#tab-mon">
          <i class="sp circle sp-btn-1"></i>
          <div class="tit">周一</div>
          <div class="sub-tit">Monday</div>
        </a></li>
        <li><a data-toggle="tab" href="#tab-tue">
          <i class="sp circle sp-btn-2"></i>
          <div class="tit">周二</div>
          <div class="sub-tit">Tuesday</div>
        </a></li>
        <li><a data-toggle="tab" href="#tab-wed">
          <i class="sp circle sp-btn-3"></i>
          <div class="tit">周三</div>
          <div class="sub-tit">Wednesday</div>
        </a></li>
        <li><a data-toggle="tab" href="#tab-thu">
          <i class="sp circle sp-btn-4"></i>
          <div class="tit">周四</div>
          <div class="sub-tit">Thursday</div>
        </a></li>
        <li><a data-toggle="tab" href="#tab-fri">
          <i class="sp circle sp-btn-5"></i>
          <div class="tit">周五</div>
          <div class="sub-tit">Friday</div>
        </a></li>
       <!-- <li><a target="_blank" href="/topic/weekend/home.jsp">
          <i class="sp circle sp-btn-6"></i>
          <div class="tit">周末</div>
          <div class="sub-tit">Weekend</div>
        </a></li>-->
      </ul>

      <div class="tab-bd">
        <div id="tab-mon" class="tab-panel section active">
          <h1 class="tit">一喜 喜气洋洋</h1>
          <div class="m-content">
            <table class="table">
              <tr>
                <th>游戏平台</th>
                <th>优惠活动</th>
                <th>最高彩金</th>
                <th>流水倍数</th>
              </tr>
              <tr><td>DT,QT</td>
                <td>存100送50</td>
                <td>1888</td>
                <td>20</td></tr>
              <tr><td>PT</td>
                <td>存50送58</td>
                <td>58</td>
                <td>25</td></tr>
            </table>


          </div>

        </div>
        <div id="tab-tue" class="tab-panel section">
          <h1 class="tit">二喜 喜上眉梢</h1>
          <div class="m-content">
            <table class="table">
              <tr>
                <th>游戏平台</th>
                <th>优惠活动</th>
                <th>最高彩金</th>
                <th>流水倍数</th>
              </tr>
              <tr><td>MG,NT</td>
                <td>存100送50</td>
                <td>1888</td>
                <td>20</td></tr>
              <tr><td>TTG</td>
                <td>存50送58</td>
                <td>58</td>
                <td>25</td></tr>
            </table>
          </div>

        </div>
        <div id="tab-wed" class="tab-panel section">
          <h1 class="tit">三喜 喜出望外</h1>
          <div class="m-content">
            <table class="table">
              <tr>
                <th>游戏平台</th>
                <th>优惠活动</th>
                <th>最高彩金</th>
                <th>流水倍数</th>
              </tr>
              <tr><td>PT,QT</td>
                <td>存100送50</td>
                <td>1888</td>
                <td>20</td></tr>
              <tr><td>DT</td>
                <td>存50送58</td>
                <td>58</td>
                <td>25</td></tr>
            </table>
          </div>

        </div>
        <div id="tab-thu" class="tab-panel section">
          <h1 class="tit">四喜 喜笑颜开</h1>
          <div class="m-content">
            <table class="table">
              <tr>
                <th>游戏平台</th>
                <th>优惠活动</th>
                <th>最高彩金</th>
                <th>流水倍数</th>
              </tr>
              <tr><td>TTG,NT</td>
                <td>存100送50</td>
                <td>1888</td>
                <td>20</td></tr>
              <tr><td>MG</td>
                <td>存50送58</td>
                <td>58</td>
                <td>25</td></tr>
            </table>
          </div>

        </div>
        <div id="tab-fri" class="tab-panel section">
          <h1 class="tit">五喜 喜眉笑眼</h1>
          <div class="m-content">
            <table class="table">
              <tr>
                <th>游戏平台</th>
                <th>优惠活动</th>
                <th>最高彩金</th>
                <th>流水倍数</th>
              </tr>
              <tr><td>PT,DT</td>
                <td>存100送50</td>
                <td>1888</td>
                <td>20</td></tr>
              <tr><td>QT</td>
                <td>存50送58</td>
                <td>58</td>
                <td>25</td></tr>
            </table>
          </div>

        </div>

      </div>

      <div class="section m-content">
        <p class="c-strong">申请方式</p>
        <p>第一步：账户管理-第二步：每日任务-第三步：天天有惊喜</p>
        <p class="c-strong">活动规则</p>
        <ol>
          <li>每位会员每天每个平台仅限参与一次</li>
          <li>申请流程：无需联系在线客服。存款到账后，点击“账户管理”——“每日任务”——“天天有惊喜”即可自助申请。</li>
          <li>“存100送50”的优惠活动，新会员及忠实会员最高彩金888。星级及星级以上会员最高彩金1888.</li>
          <li>此优惠需达到对应的流水倍数即可提款。 </li>
          <li>此优惠最低存款10元，即可申请。</li>
          <li>此优惠需全程在天威PT娱乐场-"经典老虎机"和"吃角子老虎"以及MG、DT、NT、TTG、QT内投注。其中百家乐游戏，21点游戏，轮盘游戏，骰宝游戏，视频扑克游戏，Pontoon游戏，Craps游戏，赌场战争游戏，娱乐场Hold'em游戏，牌九扑克游戏，多旋转老虎机(地妖之穴、海洋公主、三倍利润、热带滚筒和部落生活)和老虎机奖金翻倍投注将不计算在内。MG、DT、NT、TTG、QT内所有的老虎机奖金翻倍游戏的投注都不计算在该优惠内；存送活动禁止游戏PT《古怪猴子》《疯狂七》；多旋转老虎机(地妖之穴、海洋公主、三倍利润、热带滚筒和部落生活)；以及捕鱼类游戏。发现违规投注天威有权收回红利和非法盈利额。本次优惠和返水共享，其他需要流水倍数的红利不能参加次次存，若发现取消其红利。</li>
          <li>本次优惠和返水共享，其他需要流水倍数的红利不能参加次次存，若发现取消其红利。</li>
          <li>凡是只靠此优惠套取优惠者，我们有权发至游戏平台方审核，严重者扣除盈利及本金并且禁用会员账号。</li>
          <li>天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</li>
        </ol>
      </div>
    </div>

  </div>

</div>

<!--登录{-->
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-logo"><img src="/images/logo.png" alt=""></div>
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">会员登录</h4>
      </div>
      <div class="modal-bd">
        <form action="" method="post" class="ui-form">
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">用户名：</label>
            <input type="text" name="address" class="ui-ipt"  id="j-name" placeholder="用户名" maxlength="20"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">密码：</label>
            <input type="password" class="ui-ipt" name="addressee" placeholder="密码" id="j-pwd"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">验证码：</label>
            <input type="text" class="ui-ipt" name="cellphoneNo" style="width: 168px;"  id="j-code" maxlength="8" placeholder="验证码"/>
            <img width="78" height="34" id="j-codeimg" src="/asp/validateCodeForIndex.aspx" onclick="this.src='/asp/validateCodeForIndex.aspx?'+Math.random();" alt="">
          </div>
          <div class="ui-form-item">
            <input type="submit" class="btn btn-danger btn-block" id="j-login" value="确定" onclick="Page.login(this);"/>
          </div>
        </form>
      </div>
      <div class="modal-ft">登录时遇到任何问题，请及时联系在线客服获取帮助。 <a href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=" class="c-red link">24小时在线客服</a></div>
    </div>
  </div>
</div>
<!--}登录-->

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/plugins/jquery.tosrus.min.all.js"></script>
<script src="/js/base.js"></script>
<script>
    $(function(){
        function drift() {
            var wh = $(window).height();
            setInterval(function () {
                var f = $(document).width();
                var e = Math.random() * f - 100;//雪花的定位left值
                var o = 0.3 + Math.random();//雪花的透明度
                var fon = 10 + Math.random() * 30;//雪花大小
                var l = e - 100 + 200 * Math.random();//雪花的横向位移
                var k = 2000 + 5000 * Math.random();
                var html;
                switch (Math.floor(Math.random() * 3 + 1)) {
                    case 1:
                        html = "<div class='float-leave'><img src='images/lv-1.png' alt=''><div>";
                        break;
                    case 2:
                        html = "<div class='float-leave'><img src='images/lv-2.png' alt=''><div>";
                        break;
                }
                $(html).clone().appendTo("body").css({
                    left       : e + "px",
                    opacity    : o,
                    "font-size": fon,
                }).animate({
                    top    : (wh * 0.8) + "px",
                    left   : l + "px",
                    opacity: 0.7,
                }, k, "linear", function () {
                    $(this).remove()
                })
            }, 1000)
        }

        drift();
    });
</script>
</body>
</html>