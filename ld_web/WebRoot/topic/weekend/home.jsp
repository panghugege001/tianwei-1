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
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
  <title>周末欢乐存</title>
  <script>
      !function(){
          document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.5 + 'px';
      }();
  </script>
  <link href="/css/util/reset.css" rel="stylesheet"/>
  <link href="/css/util/common.css" rel="stylesheet"/>
  <link href="/css/util/animation.css" rel="stylesheet"/>
  <link href="./css/home.css?v=13" rel="stylesheet"/>
</head>
<body>
<div id="header">
  <div class="container">
    <a href="javascript:;" class="fl"><img src="/images/logo.png" width="156" alt=""></a>
    <div class="fl at-link">
      <a href="/topic/represent/home.jsp">新年双庆</a>
      <a href="/topic/weekend/home.jsp">周末欢乐存</a>
      <a href="/topic/newyear/home.jsp">恭贺新禧</a>
      <a href="/topic/newyear/week.jsp">金鸡报喜</a>
    </div>

    <div class="fr top-link">
      <c:if test="${session.customer==null}">
      <a data-target="#modal-login" data-toggle="modal" href="javascript:;">登录</a>
      </c:if>
      <a target="_blank" href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=<%=infoValue %>">联系客服</a>
    </div>
  </div>
</div>
<div class="page-bg">
  <div class="container main">
    <%--<div class="person-1 a-fadeinT"></div>--%>
    <div class="person-2 a-fadeinB"></div>
    <div class="top-h">
      <p class="mb10">芷晴宝宝陪您欢度周末时光，周末闲在家里，来天威！</p>
      <span class="text">活动时间：活动结束</span>
      <div><a href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=<%=infoValue %>"
         class="btn btn-apply a-flipinX" id="j-btnApply">&nbsp;</a></div>
    </div>

    <!--<div class="person-bg"></div>-->

    <div class="section">
      <div class="tit tit-one">欢乐随机双倍送</div>
      <div class="m-content">
        <table class="table">
          <tr><th>周末累计存款</th>
            <th>周一红包</th>
            <th>周二红包</th>
            <th>周三红包</th>
            <th>周四红包</th>
            <th>周五红包</th>
            <th>投注要求</th>
            <th>申请次数</th></tr>
          <tr><td>3000</td>
            <td>60</td>
            <td>60</td>
            <td>60</td>
            <td>60</td>
            <td>60</td>
            <td>8倍流水</td>
            <td>每天1次</td></tr>
          <tr><td>1000</td>
            <td>30</td>
            <td>30</td>
            <td>30</td>
            <td>30</td>
            <td>30</td>
            <td>8倍流水</td>
            <td>每天1次</td></tr>
          <tr><td>500</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
            <td>10</td>
            <td>8倍流水</td>
            <td>每天1次</td></tr>
        </table>
        <p class="text-center">每周周末两天累计存款500以上</p>
        <p class="text-center">下周连续5天登录即可联系客服免费申请红包！并有机会参加随机日期的双倍红包活动！</p>
      </div>
    </div>
    <div class="section section-qa">
      <div class="tit tit-two">活动问答</div>
      <div class="m-content">
        <div class="qa-item">
          <p class="c-strong">Q:什么是双倍红包?</p>
          <p>A : 您好，双倍红包是天威针对新年开展的加码回馈活动，双倍也就是您之前本应获得的红包金额翻倍！（如周一开展该活动，您本应领取60元红包，则双倍活动当天您可以领取120元红包） </p>
        </div>
        <div class="qa-item">
          <p class="c-strong">Q：红包是以什么形式派发的？</p>
          <p>A：您好，红包是以红包优惠券代码的形式进行派发的。（红包优惠券代码只能通过手机登录进行使用）</p>
        </div>
        <div class="qa-item">
          <p class="c-strong">Q：周末存款达到了，什么时候可以开始申请红包？</p>
          <p>A：您好，周一0：00即可申请周一当天的红包了哦。</p>
        </div>
        <div class="qa-item">
          <p class="c-strong">Q：红包可以游戏哪些平台？打多少流水可以提款？</p>
          <p>A：您好，红包可转账至PT,MG,DT,TTG,NT,QT老虎机平台进行游戏，您需要完成红包金额×8的投注流水，即可提款。</p>
        </div>
      </div>
    </div>
    <div class="section">
      <div class="tit tit-three">活动规则</div>
      <div class="m-content">
        <p>1.周末累计存款计算时间：周六0：00-周日23：59：59</p>
        <p>2.红包申请流程：达到周末存款的玩家，需下周一至周五当天联系在线客服申请红包，客服审核后，将立即派发红包。若当天未联系客服申请，则视为玩家放弃领取，不予补发！</p>
        <p>3.红包申请开始时间：周一0：00开始——周五23：59：59截止。当天红包必须当天申请。如：申请周一红包，需周一0：00-23：59：59申请周一红包。</p>
        <p>4.红包可转账至PT,MG,DT,TTG,NT,QT老虎机平台进行游戏。</p>
        <p>5.红包完成8倍流水即可提款。如：红包金额60需完成480的投注即可提款。</p>
        <p>6.PT游戏只限经典老虎机、电动吃角子游戏。所有21点游戏，所有轮盘游戏，所有百家乐游戏，所有骰宝游戏，所有视频扑克游戏及刮刮乐游戏等，多旋转老虎机和老虎机奖金翻倍投注将不计算在内。MG、NT、TTG、QT、DT游戏只限老虎机游戏，其他纸牌游戏，桌面游戏，电子扑克都不计算在内。次存活动禁止游戏多旋转老虎机(地妖之穴、海洋公主、三倍利润、热带滚筒和部落生活)，发现违规投注天威有权收回红利和非法盈利额。</p>
        <p>7.此项优惠活动只针对娱乐性质的会员，若系统检测同一人有多个帐号或不正当的投注，天威有权毋需通知玩家，直接收回红利和非法盈利额，禁用所有游戏帐号。</p>
        <p>8.天威娱乐城保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
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
            <input type="text" name="address" class="ui-ipt"  id="j-name" maxlength="20"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">密码：</label>
            <input type="password" class="ui-ipt" name="addressee" id="j-pwd"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">验证码：</label>
            <input type="text" class="ui-ipt" name="cellphoneNo" style="width: 168px;"  id="j-code" maxlength="8"/>
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
</body>

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/base.js"></script>


</html>