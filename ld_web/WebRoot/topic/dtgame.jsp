<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.DateUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>DT老虎机</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="/css/topic/dtgame.css" rel="stylesheet" />
</head>
<body>
<div class="header">
  <div class="header-inner">
    <a href="/"><img class="fl" src="/images/topic/match/logo.png" alt=""></a>
    <div class="fr toplink">
      <!-- <a target="_blank" href="/asp/bbsIndex.aspx">官方论坛</a> -->
      <a target="_blank" href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=">在线客服</a>
      <a href="/">回到首页</a>
      <c:if test="${session.customer==null}">
        <a class="i-btn two"  href="javascript:;" data-toggle="modal" data-target="#j-modal-login">登录</a>
        <a class="i-btn" href="/register.jsp" target="_blank">免费开户</a>
      </c:if>

    </div>
  </div>
</div>
<div class="index-bg">
  <div class="container">
    <div class="btnlist">
      <a href="#j-gameList">DT公测游戏</a>
      <a target="_blank" href="http://www.lehubbs.com/thread-4712-1-1.html">优惠活动</a>
      <a href="https://www.wenjuan.com/s/6niiauY/" target="_blank">问卷</a>
    </div>
    <div class="text-center mb40">
      <img src="/images/topic/dtgame/all-game.png" width="100%" alt="">
    </div>
    <ul class="game-list" id="j-gameList">
      <li class="game-info">
        <div class="game-pic">
          <img src="/images/topic/dtgame/slamdunk.jpg"  width="270" height="155" alt="">
        </div>
        <div class="name">
          <h4 class="fl">灌篮高手 <sub>SLAMDUNK</sub></h4>
          <div class="fr scroe c-orange">9.8</div>
        </div>Z

        <div class="game-brief">
          <div class="fl">
            <p>人气:<span class="c-orange">68859</span></p>
            <p class="like">★★★★☆</p>
          </div>
          <a target="_blank" href="http://www.dreantech.net/publish/sd/sd.php?uname=${session.customer.loginname}&pname=e68&code=${session.trycode}" class="j-check btn-game play">进入游戏</a>
        </div>
      </li>
      <li class="game-info">
        <div class="game-pic">
          <img src="/images/topic/dtgame/kingoffighters.jpg"  width="270" height="155" alt="">
        </div>
        <div class="name">
          <h4 class="fl">拳皇 <sub>THE KING OF FIGHTERS</sub></h4>
          <div class="fr scroe c-orange">9.6</div>
        </div>

        <div class="game-brief">
          <div class="fl">
            <p>人气:<span class="c-orange">12859</span></p>
            <p class="like">★★★★☆</p>
          </div>
          <a target="_blank" href="http://www.dreantech.net/publish/kof/kof.php?uname=${session.customer.loginname}&pname=e68&code=${session.trycode}" class="j-check  btn-game play">进入游戏</a>
        </div>
      </li>
      <li class="game-info">
        <div class="game-pic">
          <img src="/images/topic/dtgame/games/onepiece.jpg"  width="270" height="155" alt="">
        </div>
        <div class="name">
          <h4 class="fl">海贼王 <sub>ONE PIECE</sub></h4>
          <div class="fr scroe c-orange">0.0</div>
        </div>

        <div class="game-brief">
          <div class="fl">
            <p>人气:<span class="c-orange">0</span></p>
            <p class="like">☆☆☆☆☆</p>
          </div>
          <a target="_blank" class="j-check btn-game forward">敬请期待</a>
        </div>
      </li>
      <li class="game-info">
        <div class="game-pic">
          <img src="/images/topic/dtgame/games/naruto.jpg"  width="270" height="155" alt="">
        </div>
        <div class="name">
          <h4 class="fl">火影忍者 <sub>naruto</sub></h4>
          <div class="fr scroe c-orange">0.0</div>
        </div>

        <div class="game-brief">
          <div class="fl">
            <p>人气:<span class="c-orange">0</span></p>
            <p class="like">☆☆☆☆☆</p>
          </div>
          <a target="_blank" class="j-check btn-game forward">敬请期待</a>
        </div>
      </li>
    </ul>
  </div>
</div>

<!--登录{-->
<div class="modal fade" id="j-modal-login" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">登录</h4>
      </div>
      <div class="modal-bd">
        <form action="" method="post" class="ui-form">
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">用户名：</label>
            <input type="text" name="address" class="ui-ipt"  id="j-username" maxlength="11"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">密码：</label>
            <input type="password" class="ui-ipt" name="addressee" id="j-pwd"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">验证码：</label>
            <input type="text" class="ui-ipt" name="cellphoneNo"  id="j-code" maxlength="8"/>
            <img id="j-codeimg" src="/asp/validateCodeForIndex.aspx" onclick="this.src='/asp/validateCodeForIndex.aspx?'+Math.random();" alt="">
          </div>
          <div class="ui-form-item">
            <input type="submit" class="btn btn-danger" id="j-login" value="确定"/>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<!--}登录-->

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/base.js"></script>
<c:if test="${session.customer==null}">
  <script>
    $(function () {
      $(document).on('click','.j-check',function(){
        var $loginOpen=$('#j-modal-login');
        $loginOpen.modal('show');
        return false;
      })
    });
  </script>
</c:if>
<script>
  !function($){
    var $btnLogin=$('#j-login'); //登录按钮

    var match=match||{};
    /**
     * 登录
     * @returns {boolean}
     */
    match.login=function(){
      var loginname=$('#j-username').val(),
              password=$('#j-pwd').val(),
              code=$('#j-code').val();

      $btnLogin.prop('disabled',true);

      if(loginname==""||loginname=="帐 号"){
        alert("账号不能为空！");
        return false;
      }
      if(password==""||password=="密 码"){
        alert("密码不能为空！");
        return false;
      }
      if(code==""||code=="验证码"){
        alert("验证码不能为空！");
        return false;
      }
      $.post("/asp/login.aspx", {
        "loginname":loginname, "password":password,"imageCode":code
      }, function (returnedData) {
        $btnLogin.prop('disabled',false);
        if(returnedData=="SUCCESS"){
          window.location.reload();
        }else{
          $('#j-codeimg').attr('src','/asp/validateCodeForIndex.aspx?r='+Math.random());
          alert(returnedData);
          var str2='已被锁';
          if(returnedData.indexOf(str2)>-1){
            window.location="/forgotPassword.jsp";
          }
        }
      });
    };
    match.init=function(){
      $btnLogin.on('click',match.login);
    };

    $(function(){
      match.init();
    });
  }(jQuery);

</script>
</body>
</html>