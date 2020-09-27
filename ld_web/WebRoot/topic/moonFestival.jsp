<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>中秋活动</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="/css/topic/moonFestival.css" rel="stylesheet" />
</head>
<body>
<div class="page-bg">
  <div id="stars"></div>
  <div id="stars2"></div>
  <div id="stars3"></div>
  <div class="main">
    <div class="main-inner" id="content">
      <div class="lantern"></div>
      <h1 class="title">
        <button class="btn fr" id="j-btnApply">立即领取</button>
      </h1>
      <div class="m-content">
        <h3>礼物类别：</h3>
        <p>好利来月饼礼盒</p>
        <h3>活动对象：</h3>
        <p>天威全体会员</p>
        <h3>活动时间：</h3>
        <p>8月15号 0:00 - 9月8号 23:59:59</p>
        <h3>活动内容：</h3>
        <p>中秋佳节来临之际，天威为各位玩家准备了精美月饼，祝愿各位中秋快乐。</p>

        <table class="table">
          <tbody>
          <tr>
            <th>会员等级</th>
            <th>累积存款</th>
          </tr>
          <tr>
            <td>新会员</td>
            <td rowspan="3">2888</td>
          </tr>
          <tr>
            <td>忠实vip</td>
          </tr>
          <tr>
            <td>星级vip</td>
          </tr>
          <tr>
            <td>黄金vip</td>
            <td rowspan="4">无需存款</td>
          </tr>
          <tr>
            <td>白金vip</td>
          </tr>
          <tr>
            <td>钻石vip</td>
          </tr>
          <tr>
            <td>至尊vip</td>
          </tr>
          </tbody>
        </table>
        <p>温馨提示:由于礼盒中包含苏式月饼,是酥脆外皮.快递路途颠簸,会有轻微的脱皮现象出现.</p>

        <h3>活动要求：</h3>
        <p>1.新会员,忠实，星级会员于8月15号0:00:00-9月8号23:59:59累计存款2888，即可申请一份中秋节礼品。金牌及以上会员无需存款即可申请。</p>
        <p>2.申请礼物流程：官网右侧——点击“中秋节礼品”填写您的天威游戏账号，收货人姓名，收货地址，收货人电话。</p>
        <p>3.申请礼物截止日期：9月8号23:59:59  请符合条件的玩家提早登记申请。延迟申请的视为玩家自动放弃申请。</p>
        <p>4.礼物接收日期：我们预计礼物将于中秋节（9月15日）当天送达，届时请注意接收礼物。（由于快递方面的原因，有提前或延期的情况，还请见谅）</p>
        <p>5.此项优惠活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</p>
        <p>6.天威拥有本活动的最终解释权以及随时更改活动的权利。</p>
      </div>

    </div>
    <div class="main-bottom text-center">
      <a href="#content">返回头部</a>
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

<!--礼物申请{-->
<div class="modal fade" id="j-modal-gift" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">中秋节礼物申请</h2>
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

  });

</script>

</body>
</html>