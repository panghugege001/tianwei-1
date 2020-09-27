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
  <title>天威红包来了</title>
  <link href="/css/util/reset.css" rel="stylesheet"/>
  <link href="/css/util/common.css" rel="stylesheet"/>
  <link href="/css/util/animation.css" rel="stylesheet"/>

  <link href="css/home.css?v=2" rel="stylesheet"/>
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

  <div class="top-h"></div>
  <div class="video-box">
    <div class="video-play text-center">
      <div style="width: 620px;margin: 0 auto; background: #333;">
        <video width="100%" height="auto" controls="" autoplay="autoplay">
          <source src="https://dn-qiniucdn2.qbox.me/topic/live/girl.mp4" type="video/mp4">  </video>
      </div>
    </div>
  </div>
  <div class="main">
    <div class="text-center">
      <div class="red-package a-swing"></div>
      <h1 class="caption"></h1>
    </div>

    <div class="section-wrap m-content">
      <p>金鸡报喜，天威送礼.红包拿来，通通发财！愿所有天威玩家在新的一年里万事称心如意；福星高照；财运亨通！</p>
      <p class="c-strong">活动时间：</p>
      <p>1月27日，1月28日，1月29日。</p>
      <p class="c-strong">活动要求：</p>
      <p>连续三天均可参加存款送红包活动，例如1月27日达到存款要求则次日12点前会直接派发到帐。该玩家1月28日，1月29日依然可以参加该活动哦！连续三天每天达到要求，每天都可以派发红包呢！</p>

      <table class="table">
        <tr>
          <th>会员等级</th>
          <th>存款要求</th>
          <th>红包金额</th>
        </tr>
        <tr>
          <td rowspan="4">不限等级</td>
          <td>500</td>
          <td>8-58随机</td>
        </tr>
        <tr>
          <td>2000</td>
          <td>68</td>
        </tr>
        <tr>
          <td>5000</td>
          <td>88</td>
        </tr>
        <tr>
          <td>10000</td>
          <td>128</td>
        </tr>
      </table>

      <div class="rule-info">
        <p>活动规则：</p>
        <p>1.此活动不与“周末欢乐存，一周欢乐送”活动共享。</p>
        <p>2.此活动不区分等级，无需申请。达到存款要求后次日12点前会直接派发到帐。</p>
        <p>3.此次活动红包无流水要求，每天仅限领取一次。</p>
        <p>4.天威享有本活动的最终解释权及在不通知情况下随时修改本次活动的权力。</p>
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