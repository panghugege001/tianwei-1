<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>天天首存</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="/css/topic/deposit.css?v=1112" rel="stylesheet" />
</head>
<body>
<div id="header">
  <div class="container">
    <div class="fl">
      <a href="/" class="fl">返回首页</a>
      <a href="/topic/deposit.jsp" class="fl">天天首存</a>
      <!-- <a href="/asp/bbsIndex.aspx">官方论坛</a> -->
      <a href="/topic/live.jsp" class="fl">天威宝贝秀</a>

    </div>
    <div class="fr">
      <a data-target="#modal-login" data-toggle="modal" href="javascript:;">登录</a>
      <a class="i-btn"  href="/register2.jsp">注册</a>
    </div>
  </div>
</div>
<div class="page-bg">
  <div class="top-banner container">
    <div class="light right"></div>
    <div class="light left"></div>
    <h1 class="tit text-center"><span class="sp sp-tit"></span></h1>
    <div class="text-center"><a class="btn" id="j-btnApply" href="javascript:;">点击申请</a></div>
    <p class="desc text-center">还在为错过限时存送而后悔不已吗？还在抱怨次次存送优惠太低吗？加入天威，以后都无需担忧！ <br>
      这个1月天天都有60%首存优惠！一定不要错过了！最高2888等着你！</p>
    <ul class="box-list">
      <li class="fl">
        <i class="arrow"></i>
        <i class="sp sp-ele-1"></i>
        <h2>对象</h2>
        <p>天威全体会员</p>
      </li>
      <li class="fr">
        <i class="arrow"></i>
        <i class="sp sp-ele-3"></i>
        <h2>内容</h2>
        <p>每日存款10元以上即
          可参与天天首存</p>
      </li>
      <li>
        <i class="arrow"></i>
        <i class="sp sp-ele-2"></i>
        <h2>时间</h2>
        <p>1月1日-1月31日</p>
      </li>
    </ul>
  </div>

  <div class="container">
    <div class="m-content section-one">
      <table class="table">
        <tr>
          <th>游戏平台</th>
          <th>会员等级</th>
          <th>天天首存比例</th>
          <th>最低存款</th>
          <th>最高彩金</th>
          <th>流水倍数</th>
        </tr>
        <tr>
          <td>PT,MG,DT,TTG,QT,NT</td>
          <td>初学者，忠实</td>
          <td>120%</td>
          <td>10</td>
          <td>60</td>
          <td>20</td>
        </tr>
        <tr>
          <td>PT,MG,DT,TTG,QT,NT</td>
          <td>星级及以上</td>
          <td>60%</td>
          <td>10</td>
          <td>2888</td>
          <td>25</td>
        </tr>

      </table>
      <p class="f18">活动规则</p>

      <p>1.每位会员每天仅限参与一次天天首存。</p>
      <p>2.申请流程：无需联系在线客服。存款到账后，点击账户管理——自助优惠——每日任务——天天首存自助申请。</p>
      <p>3.天天首存需达到对应的流水倍数才可提款。 <br>例如：玩家A，存款2000申请天天首存，获得1200彩金，则需达到（2000+1200）*25的流水即可提款。</p>
      <p>4.天天首存最低存款10元，才可申请。</p>
      <p>5.天天首存需全程在天威PT娱乐场-"经典老虎机"和"吃角子老虎"以及MG、DT、NT、TTG、QT内投注。其中百家乐游戏，21点游戏，轮盘游戏，骰宝游戏，视频扑克游戏，Pontoon游戏，Craps游戏，赌场战争游戏，娱乐场Hold'em游戏，牌九扑克游戏，多旋转老虎机(地妖之穴、海洋公主、三倍利润、热带滚筒和部落生活)和老虎机奖金翻倍投注将不计算在内。MG、DT、NT、TTG、QT内所有的老虎机奖金翻倍游戏的投注都不计算在该优惠内；天天首存活动禁止游戏PT《古怪猴子》以及多旋转老虎机(地妖之穴、海洋公主、三倍利润、热带滚筒和部落生活)，发现违规投注天威有权收回红利和非法盈利额。</p>
      <p>6.本次优惠和返水共享，其他需要流水倍数的红利不能参加次次存，若发现取消其红利。</p>
      <p>7.凡是只靠天天首存套取优惠者，我们有权发至游戏平台方审核，严重者扣除盈利及本金并且禁用会员账号。</p>
      <p>8.天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>

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
</body>

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/base.js"></script>
<script>

  $(function(){
    var $btnApply=$('#j-btnApply');
    var isLogin='${session.customer!=null}'==='true';

    $btnApply.on('click',function(){
      if(isLogin){
        window.open('/member_everyday.jsp?showid=tab-daily-tasks','_blank');
      }else{
        $('#modal-login').modal('show');
      }

      return false;
    });

  });

</script>

</html>