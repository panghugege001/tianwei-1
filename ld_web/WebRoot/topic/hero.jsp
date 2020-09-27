<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.DateUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>夜战</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="/css/topic/hero.css?v=33" rel="stylesheet" />
</head>
<body>
<div class="header">
  <div class="header-inner">
    <a href="/"><img class="fl" src="/images/topic/match/logo.png" alt=""></a>
    <div class="fr toplink">
      <!-- <a target="_blank" href="/asp/bbsIndex.aspx">官方论坛</a> -->
      <a target="_blank" href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=">在线客服</a>
      <a href="/">回到首页</a>
     <%-- <c:if test="${session.customer==null}">
        <a class="i-btn two"  href="javascript:;" data-toggle="modal" data-target="#j-modal-login">登录</a>
        <a class="i-btn" href="/register.jsp">免费开户</a>
      </c:if>--%>

    </div>
  </div>
</div>
<div class="bg-top">
  <div class="bg-center">
    <div class="bg-gradient"></div>
    <div class="main-box cfx">
      <ul class="menu fl">
        <li class="active"><a class="sp-menu-1" href="#tab-one" data-toggle="tab">夜擒双虎</a></li>
        <%--<li><a href="#tab-two" class="sp-menu-2" data-toggle="tab">夜擒双虎</a></li>--%>
        <li><a href="#tab-three" class="sp-menu-3" data-toggle="tab">夜擒双虎</a></li>
      </ul>
      <div class="tab-bd fl box-content" style="min-height: 684px;">
        <div class="box-left"></div>
        <div class="box-right"></div>

        <div id="tab-one" class="m-content tab-panel active">
          <div class="sp sp-tit-one tit"></div>
          <h3>时来运转，助您一臂之力</h3>
          <h3>活动时间:</h3>
          <p>每周六 18:00～21:00疯狂幸运转（<span class="c-red">负盈利</span>） <br>
            每周六 01:00～03:00疯狂幸运转（<span class="c-red">正盈利</span>）
          </p>
          <h3>活动内容:</h3>
          <p>活动时间内，您在天威PT经典，电动吃角子老虎机（不包含奖池）正盈利大于<span class="c-red">10</span>元or负盈利大于<span class="c-red">10</span>元（扣除当天红利），活动结束后，正盈利or负盈利将全额返还！
          </p>
          <h3>正盈利:</h3>
          <table class="table">
            <tbody><tr>
              <th>会员等级</th>
              <th>喜上加喜比例</th>
              <th>最高红利</th>
              <th>最低正盈利</th>
              <th>流水倍数</th>
            </tr>
            <tr>
              <td>新会员</td>
              <td rowspan="7">100%</td>
              <td>168元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            <tr>
              <td>忠实会员</td>
              <td>168元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            <tr>
              <td>星级会员</td>
              <td>288元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            <tr>
              <td>金牌会员</td>
              <td>588元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            <tr>
              <td>白金会员</td>
              <td>888元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            <tr>
              <td>钻石会员</td>
              <td>1888元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            <tr>
              <td>至尊会员</td>
              <td>5888元</td>
              <td>10元</td>
              <td>30倍</td>
            </tr>
            </tbody></table>
          <h3>负盈利:</h3>
          <table class="table">
            <tbody><tr>
              <th>会员等级</th>
              <th>救援反赠比例</th>
              <th>最高红利</th>
              <th>最低负盈利</th>
              <th>流水倍数</th>
            </tr>
            <tr>
              <td>新会员</td>
              <td rowspan="7">100%</td>
              <td>168元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            <tr>
              <td>忠实会员</td>
              <td>168元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            <tr>
              <td>星级会员</td>
              <td>288元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            <tr>
              <td>金牌会员</td>
              <td>588元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            <tr>
              <td>白金会员</td>
              <td>888元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            <tr>
              <td>钻石会员</td>
              <td>1888元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            <tr>
              <td>至尊会员</td>
              <td>5888元</td>
              <td>10元</td>
              <td>20倍</td>
            </tr>
            </tbody></table>
          <h3>活动规则:</h3>
          <ol>
            <li>此活动只限PT经典老虎机老虎机和电动吃角子老虎机，（累计奖池不计算在内）每天只限参加一次。</li>

            <li>指定活动时间内的输赢值为有效值，其它以外时间均不计算在内。例如：10月3号中午12点到下午5点钟举行PT幸运转负盈利返赠活动，在上午9点存款了1000元，上午领取了昨日的救援金300元，存款的1000元等到活动时间内参加活动并输了1000元，活动结束后我们的负盈利奖金=1000-300=700元，扣除当天所有红利。</li>

            <li>此活动与其他活动共享</li>

            <li class="c-red">此次优惠每位玩家﹑每户﹑每一住址 、每一电子邮箱地址﹑每一电话号码﹑相同支付方式(相同借记卡/信用卡/银行帐户姓名及号码) 及IP地址只能享有一次优惠,一旦确认为套利玩家，立即没收盈利和本金。</li>

            <li>天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利 <br>
              温馨提示：净输值=活动时间段内的PT后台输值扣除当天所有红利（幸运抽奖、返水等）` （净输值定义：天威会员在PT老虎机平台的负盈利，其它游戏平台不计算在内） <br>
              净盈利=PT后台盈利扣除当天所有红利（幸运抽奖、返水等）（净盈利定义：天威会员在PT老虎机平台的盈利，其它游戏平台不计算在内）</li>

          </ol>
        </div>
        <div id="tab-two" class="m-content tab-panel">
          <div class="sp sp-tit-two tit"></div>
          <h3>活动对象:</h3>
          <p>天威娱乐全体会员</p>
          <h3>活动时间:</h3>
          <p>2016年8月 每周四 </p>
          <h3>活动内容:</h3>
          <p>8月每周四天威所有会员返水1.5%，积分双倍</p>
          <h3>活动规则:</h3>
          <ol>
            <li>高额返水只计算PT平台“经典老虎机”和“电动吃角子老虎”、TTG老虎机、NT老虎机、QT老虎机游戏中进行投注。所有21点游戏，所有轮盘游戏，所有百家乐游戏，所有骰宝游戏，所有视频扑克游戏，所有Pontoon游戏，各种Craps游戏，赌场战争游戏，娱乐场Hold'em游戏，牌九游戏，多旋转老虎机(地妖之穴、海洋公主、三倍利润、热带滚筒和部落生活)和老虎机奖金翻倍投注将不计算在内，如投注此类游戏将不计算高额返水。</li>
            <li>keno平台和188体育平台投注不计算在双倍积分内。</li>
            <li>高返水期间将不能使用自助返水。</li>
            <li>每位玩家、每户、每一住址、每一电子邮箱地址及共享电脑环境只能拥有一个帐户。</li>
            <li>一旦天威娱乐发现客户有滥用此促销的行为，我们保留撤回此奖金及由此赢得的任何彩金的权力。一旦此种情况发生，天威娱乐城将终止玩家享受此优惠的资格。</li>
            <li>天威娱乐拥有最终解释权。</li>
          </ol>
        </div>
        <div id="tab-three" class="m-content tab-panel">
          <div class="sp sp-tit-three tit"></div>
          <h3>活动对象:</h3>
          <p>天威所有会员</p>
          <h3>活动时间:</h3>
          <p>请关注天威公布活动时间与平台</p>
          <table class="table">
            <tr>
              <th>活动对象</th>
              <th>存送比例</th>
              <th>最低存款</th>
              <th>最高红利</th>
              <th>流水倍数</th>
              <th>游戏平台</th>
            </tr>
            <tr>
              <td>不限等级</td>
              <td>100%</td>
              <td>10元</td>
              <td>588</td>
              <td>25倍</td>
              <td>PT、TTG、NT、QT</td>
            </tr>
            <tr>
              <td>不限等级</td>
              <td>150%</td>
              <td>10元</td>
              <td>588</td>
              <td>25倍</td>
              <td>PT、TTG、NT、QT</td>
            </tr>
          </table>
          <h3>活动规则:</h3>
          <ol>
            <li>凡是在公告限时优惠时间内进行存款并申请限时100%、150%存送优惠，平台分为PT、TTG、NT、QT，即可享受限时100%、150%存送优惠优惠红利。</li>
            <li>优惠必须全数转入老虎机平台，需达到流水限制或是平台内小于5元才能进行转入操作。</li>
            <li>天威会员均可享受限时存送优惠，此优惠无需和在线客服或者QQ客服申请，在官网登陆账号后，个人中心——自助优惠——自助存送——选择<span class="c-red">自助限时存送</span>优惠平台，填写完毕即可游戏。</li>
            <li>提款要求：（本金+红利）*流水倍数。例如：（10+10）*25即可提款</li>
            <li>此优惠只计算PT、TTG、NT、QT所有老虎机游戏，所有奖金翻倍游戏、多旋转游戏、牌类、真人视频、轮盘、刮刮乐游戏投注都不计算在内，若发现不正当投注天威有权收回红利和非法盈利额。</li>
            <li>本次优惠和返水共享，其他需要流水倍数的红利不能参加次次存，若发现取消其红利。</li>
            <li>此项优惠活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</li>
            <li>此优惠活动也不能参与“古怪猴子”和“疯狂之7”这两款游戏</li>
            <li>本活动天威具有最终解释权。</li>
          </ol>
        </div>
      </div>
    </div>
  </div>
  <div class="bg-bottom"></div>
</div>
<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/main.js"></script>

</body>
</html>