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
  <title>情人节浪漫约会</title>
  <link href="/css/util/reset.css" rel="stylesheet"/>
  <link href="/css/util/common.css" rel="stylesheet"/>
  <link href="/css/util/animation.css" rel="stylesheet"/>

  <link href="css/home.css?v=2" rel="stylesheet"/>
</head>
<body>
<div id="header" class="pr" style=" z-index:10;">
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
        <a href="javascript:;" data-toggle="modal" data-target="#modal-login">登录</a>
      
        <span style="color:#fff;">|</span>
        <a href="javascript:;" data-toggle="modal" data-target="#modal-reg">注册</a>
         </c:if>
         <a href="/" href="_blank">返回首页</a>
      
    </div>
  </div>
</div>

<div class="page-bg">
  <div class="center">
    <div class="video-box pr">
      <img src="images/ren.png" class="ren animated infinite fadeInDownBig" /><img src="images/xin.png" class="xin" />
       <img src="images/q2.png" class="q1" /><img src="images/q1.png" class="q2" />
      <div class="video-play text-center">
        <div style="width: 600px; background:#fff; padding:13px; ">
          <video width="100%" height="auto" controls="" autoplay="autoplay">
            <source src="https://dn-qiniucdn2.qbox.me/topic/live/lover.mp4" type="video/mp4">  </video>
        </div>
      </div>
    </div>
    <div class="main">
      <div class="section-wrap m-content pr">
         <img src="images/tu1.jpg" class="tu" /> 
        <p class="fl" style="width:650px;">其实，你与芷晴宝宝的世界之间，只差一个VR眼镜。VR眼镜来了。天威代言人于芷晴带你遇见未曾看过的风景，让芷晴宝宝陪伴你跨越，未曾跨越过的时间。天威免费赠送！虚拟与现实，一镜之间。用上帝的视角 开启你的第二人生！</p>
        <button class="btn btn-apply" id="j-btnApply">&nbsp;</button>
        <div class="cl"></div>
        <div class="line1" style="margin:40px 0;"></div>
        <p>活动对象：</p>
        <p class="c-strong">天威所有会员</p>
        <p>活动时间：</p>
        <p class="c-strong">2月1号0:00到2月15号23:59:59。</p>
        <p>活动内容：</p>

        <table class="table" style="width:50%">
          <tr>
            <th>存款要求</th>
            <th>奖品</th>
          </tr>
          <tr>
            <td>≥3888元</td>
            <td>属于你的VR眼镜</td>
          </tr>
           
        </table>
        <p class="mt35">活动时间内，存款大于或等于3888元即可获得属于你的VR眼镜。</p>
        <div class="rule-info mt35">
          <p>活动规则：</p>
          <p>1：申请礼物流程：官网左侧：点击“上帝视角，VR眼镜”填写您的天威游戏账号，收货人姓名，收货地址，收货人电话。</p>
          <p>申请礼物截止日期：2月15号23:59:59 请符合条件的玩家提早登记申请。延迟申请的视为玩家自动放弃申请。</p>
          <p>2：礼物接收日期：由于春节期间快递较慢，部分北方地区快递停运。我们预计礼物将于2月20日之前发货完成，预计您2月28日前可以收到货。届时请注意接收礼物。（由于快递方面的原因，有提前或延期的情况，还请见谅）</p>
          <p>3：此项优惠活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</p>
          <p> 4：天威娱乐城拥有本活动的最终解释权以及随时更改活动的权利。</p>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="footer">友情提示：博彩有风险，量力乐其中 Copyright © 2017 天威 All Rights Reserved</div>
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
<!--注册弹框{-->
<div class="modal fade in" id="modal-reg" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog lg" role="document" style="margin-top: 10px;">
    <div class="modal-content">
      <div class="modal-hd">
        <h2 class="modal-title">轻松注册</h2>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">×</span></button>
      </div>
      <div class="modal-bd" style="padding: 38px 0 0 0">
        <iframe data-src="/register.jsp" width="100%" height="500" scrolling="no" frameborder="0"
                src=""></iframe>
      </div>
    </div>
  </div>
</div>
<!--}注册弹框-->
<!--礼物申请{-->
<div class="modal fade" id="j-modal-gift" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">申请礼品</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-bd">
                <iframe data-src="/applyGift.jsp"  width="100%" height="360" scrolling="no" frameborder="0"></iframe>
            </div>
        </div>
    </div>
</div>
<!--}礼物申请-->
<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/plugins/jquery.tosrus.min.all.js"></script>
<script src="/js/base.js"></script>
<script>
  $(function(){
     var $btnApply=$('#j-btnApply');
      var isLogin='${session.customer!=null}'==='true';

      $btnApply.on('click',function(){
        if(isLogin){
          $('#j-modal-gift').modal('show');
        }else{
          $('#modal-login').modal('show');
        }

        return false;
      });

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
                      html = "<div class='float-leave'><img src='images/x3.png' alt=''><div>";
                      break;
                  case 2:
                      html = "<div class='float-leave'><img src='images/x1.png' alt=''><div>";
                      break;
                   case 3:
                      html = "<div class='float-leave'><img src='images/x2.png' alt=''><div>";
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