<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>抢黄金</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="/css/topic/gold.css" rel="stylesheet" />
</head>
<body>
<div class="header">
  <div class="header-inner cfx">
    <a href="/"><img class="fl" width="150" src="/images/logo.png" alt=""></a>
    <div class="fr toplink">
      <!-- <a target="_blank" href="/asp/bbsIndex.aspx">官方论坛</a> -->
      <a target="_blank" href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=">在线客服</a>
      <a href="/">回到首页</a>
      <c:if test="${session.customer==null}">
        <a class="i-btn two"  href="javascript:;" data-toggle="modal" data-target="#modal-login">登录</a>
        <a class="i-btn" href="/">免费开户</a>
      </c:if>

    </div>
  </div>
</div>
<div class="page-bg">
  <div class="page-tit"><p>2016年 9月 19日 00:00:00 - 2016年 9月 25日 23:59:59</p></div>
  <div class="main container">
    <div class="text-center mb60">
      <button class="btn btn-apply" id="j-btnApply">立即抢金</button>
    </div>
    <div class="pro-info mb60 cfx">
      <div class="gallery-box">
        <div class="pic"><img src="/images/topic/gold/product/1.jpg" alt=""></div>
        <div class="thumb-list">
          <a class="active item" href="javascript:;"><img src="/images/topic/gold/product/1.jpg" alt=""></a>
          <a class="item" href="javascript:;"><img src="/images/topic/gold/product/2.jpg" alt=""></a>
          <a class="item" href="javascript:;"><img src="/images/topic/gold/product/3.jpg" alt=""></a>
        </div>
      </div>
      <div class="pro-summary text-left">
        <h2>礼轻情义重，天威 抢金时代开始 <br>
          周生生黄金转运珠 <strong class="c-strong">限量200份</strong>，先到先得，拼手速时间到</h2>
        <p class="summary-text f16">黄金幸运珠以不同的花语作设计蓝本，为佩戴者送上无限的祝福。 <br>
          款号 # 83208P-GD <br>
          材质：足金 <br>
          重量：约 1.810 克 <br>
          项链：不包含 </p>
      </div>
    </div>
    <div class="m-content">
      <%--<table class="table mb60">
        <tbody>
        <tr>
          <th>排名</th>
          <th>游戏账号</th>
          <th>抢金时间</th>
        </tr>
        <tr>
          <td>1</td>
          <td>benny</td>
          <td>2016-08-22</td>
        </tr>
        <tr>
          <td>1</td>
          <td>benny</td>
          <td>2016-08-22</td>
        </tr>
        <tr>
          <td>1</td>
          <td>benny</td>
          <td>2016-08-22</td>
        </tr>
        </tbody>
      </table>--%>

      <iframe class="mb60" src="/asp/queryGiftData.aspx" scrolling="no"  width="100%" onload='resizeIframe(this)' frameborder="0"></iframe>

      <table class="f16 mb60">
        <tr>
          <td>礼物类别</td>
          <td>活动对象</td>
          <td>活动时间</td>
        </tr>
        <tr>
          <td><span class="c-strong">周生生黄金转运珠</span></td>
          <td><span class="c-strong">天威全体会员</span></td>
          <td><span class="c-strong">9月19日 00:00-9月25日 23:59:59</span></td>
        </tr>
      </table>

      <p class="f16">活动内容：</p>
      <ol>
        <li>全体会员于9月19日 00:00:00 - 9月25日 23:59:59 累计存款5888即可进入抢金时代</li>
        <li>申请黄金流程：官网右侧——点击“黄金饰品”进入专题页面，点击限时抢金——填写您的天威游戏账号，收货人姓名，收货地址，收货人电话。（由于是黄金礼品所以请填写正确的地址和联系电话如致电多次无人接听我们将会进行退回操作）</li>
        <li>申请黄金截止日期：9月25号23:59:59  请符合条件的玩家及时登记。黄金饰品仅限前200名会员。</li>
        <li>黄金饰品寄送日期：我们将于9月26日当天统一寄送黄金饰品</li>
        <li>此项优惠活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</li>
        <li>天威拥有本活动的最终解释权以及随时更改活动的权利。</li>
      </ol>
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
                <h2 class="modal-title">立即抢金</h2>
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

  var gallery= (function(){
    var $box=$('.gallery-box'),
            $pic=$box.find('.pic img'),
            $btnThumb=$box.find('.thumb-list .item');

    $btnThumb.on('click',function(){
      var $this=$(this);
      $this.addClass('active').siblings().removeClass('active');
      $pic.attr('src',$this.find('img').attr('src'));
    });

  })();

  function resizeIframe(obj){
    obj.style.height = 0;
    obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
  }

</script>

</body>
</html>