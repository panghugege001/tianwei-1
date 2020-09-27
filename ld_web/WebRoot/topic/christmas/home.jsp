<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>天威圣诞活动</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/animation.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="./css/home.css" rel="stylesheet"/>
</head>
<body>
<div class="snow-wrap">
  <div class="snow"></div>
  <div class="snow"></div>
  <div class="snow"></div>
</div>
<div class="page-bg">
  <div  class="header" id="header">
    <div class="container">
      <a href="/" class="fl">返回首页</a>

      <div class="fr">
        <c:if test="${session.customer==null}">
        <a href="javascript:;" data-toggle="modal" data-target="#modal-login">登录</a>
          </c:if>
        <!-- <a href="/asp/bbsIndex.aspx">官方论坛</a> -->
        <a href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=">联系客服</a>
      </div>
    </div>
  </div>

  <div class="container main">
    <div class="person-bg a-bounceinB"></div>
    <button class="btn btn-apply" id="j-btnApply">&nbsp;</button>
    <div class="top-h"></div>

    <div class="pro-info cfx">
      <div class="pro-inner">
        <div class="pic"><img src="./images/product.jpg" width="243" height="243" alt=""></div>

        <div class="pro-summary">
          <h2 class="f26 c-red">奖品：机灵迷你暖风机【冷暖两用】</h2>
          <p><span class="lb">品牌:</span> 机灵</p>
          <p><span class="lb">取暖器加热方式:</span> 陶瓷加热</p>
          <p><span class="lb">适用面积:</span> 11m^2 (含)-20m^2 (含)</p>
          <p><span class="lb">采购地:</span> 中国大陆</p>
          <p><span class="lb">颜色分类:</span> 橙色 白色</p>
        </div>
      </div>

    </div>
    <div class="m-content section">
      <h3><i class="sp sp-i-1"></i>活动时间：</h3>
      <p>2016年12月1日00:00:00 ~ 2016年12月15日23:59:59</p>

      <h3><i class="sp sp-i-2"></i>活动对象：</h3>
      <p>天威全体会员</p>

      <h3><i class="sp sp-i-3"></i>活动内容：</h3>

      <table class="table">
        <tr>
          <th>累计存款</th>
          <th>温情礼物</th>
        </tr>
        <tr>
          <td>3888</td>
          <td>暖风加湿小精灵</td>
        </tr>
      </table>
      <h3><i class="sp sp-i-4"></i>申请方式：</h3>
      <p>活动期间内累计存款达到指定额度，即可在官网活动页面点选申请的小图标进行申请对应温情礼物!</p>

      <h3><i class="sp sp-i-5"></i>活动规则：</h3>
      <ol>
        <li>每个会员只能拥有一个账号参加此活动，包括电话、邮箱、IP、地址、支付方式（相同借 记卡 /信用卡/银行账户号码），使用多个账户我们将有权取消会员申领资格。</li>
        <li>有效存款计算时间为2016年12月1日00:00:00 —— 2016年12月15日23:59:59，活动结束将会以站内信及公告告知.</li>
        <li>此活动奖励预计将于圣诞节前统一派发至玩家手中。</li>
        <li>此活动请务必填写正确可以联系到的电话，将会有专人致电与您联系。</li>
        <li>会员因填写错地址，要修改地址，请会员发送邮件到邮箱vip@lehu.ph(修改地址只限活动时间内) 邮件内容：游戏账号、原始收件讯息以及欲更改讯息。</li>
        <li>达到要求的玩家请在2016年12月16日00:00之前进行申请。</li>
        <li>若因会员收件讯息填写错误，造成速递公司无法寄送或退件，恕不负责。</li>
        <li>天威享有最终解释权。</li>
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