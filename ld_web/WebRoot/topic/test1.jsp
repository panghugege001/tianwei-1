<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>女主播</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/common.css" rel="stylesheet" />
  <link href="/js/lib/swiper/swiper.min.css"  rel="stylesheet">
  <link href="/css/topic/live.css?v=2" rel="stylesheet" />
  <style>
    .comment-box .comment-list {
      height: 480px;
      background: #fff;
    }
  </style>
</head>
<body>
<div class="header">
  <div class="header-inner cfx">
    <a href="/"><img class="fl" width="150" src="/images/logo.png" alt=""></a>
    <div class="fr toplink">
      <!-- <a target="_blank" href="http://blog.lehu111.com/">博客 <span class="t">BLOG</span></a>
      <a target="_blank" href="/asp/bbsIndex.aspx">论坛 <span class="t">BBS</span></a> -->
      <a href="/">官网 <span class="t">OFFICIAL</span></a>
      <c:if test="${session.customer==null}">
      <a class="i-btn two" href="javascript:;" data-toggle="modal" data-target="#modal-login">登录 <span class="t">LOGIN</span></a>
      <a class="i-btn" href="/">注册 <span class="t">REGISTER</span></a>
      </c:if>
    </div>
  </div>
</div>
<div class="page-bg">
  <div class="page-tit s-2"></div>
  <div id="main" style="display: none;">
    <div class="main container">
      <div class="cfx">
        <div class="aside">
          <div id="wowza_player" class="video-box"></div>
          <div class="comment-box">
            <div class="comment-list">
             <%-- <table class="tb-comment">
                <thead>
                <tr>
                  <th>时间</th>
                  <th>昵称</th>
                  <th>留言</th>
                </tr>
                </thead>
                <tbody>
                </tbody>

              </table>--%>
              <div id="rt-35673c28b877f628ef55d75257e2d285"></div>
            </div>

           <%-- <div class="ipt-group">
              <form action="">
                <input class="fr btn" type="button" value="发送" onclick="alert('敬请期待！！')">
                <div>
                  <input class="ipt-txt" type="text" value="">
                </div>
              </form>
            </div>--%>
          </div>
        </div>
      </div>
    </div>
    <div class="profile-box bg">
      <div class="container">
        <div class="item">
          <div class="pic"><img src="/images/topic/live/g-1.jpg" alt=""></div>
          <div>
            <h2>亭瑋</h2>
            <p class="mb20">身高：168公分 体重：49公斤 三围：<span class="c-strong">34D / 23 / 36</span></p>
            <p>大家好~我是亭瑋，平常擔任Dancer、车模的工作，还有內衣走秀，很多人都说我胸部很漂亮喔！（羞）我是ＳＢＬ台啤啦啦隊的一員，也是2015瓊斯盃國際籃球邀請賽啦啦隊以及2015台中車展車模票選比賽的第二名。個性認真活潑，好相處，姣好的身材和性感陽光兼具是我的招牌，希望你會喜歡^^
            </p>
          </div>
        </div>
        <div class="item">
          <div class="pic"><img src="/images/topic/live/g-2.jpg" alt=""></div>
          <div>
            <h2>尤里</h2>
            <p class="mb20">身高：166公分 體重：45公斤 三圍：<span class="c-strong">32E / 24 / 32</span></p>
            <p>我是尤里，很高興與大家見面喔～直播經驗非常豐富，許多大陸平台都看的到我！參加過很多展場的演出，也有參加火辣性感的成人展喔！很多人說我像蕭亞軒，你覺得像嗎？記得常常關注我來看我直播喔～保證超多福利大放送，香豔刺激火辣辣，不讓你睡！
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="container gallery-wrap">
      <!-- Swiper -->
      <div class="swiper-container gallery-top">
        <div class="swiper-wrapper">
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/1.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/2.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/3.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/4.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/5.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/6.jpg)"></div>
        </div>
        <!-- Add Arrows -->
        <div class="swiper-button-next swiper-button-white"></div>
        <div class="swiper-button-prev swiper-button-white"></div>
      </div>
      <div class="swiper-container gallery-thumbs">
        <div class="swiper-wrapper">
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/1.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/2.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/3.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/4.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/5.jpg)"></div>
          <div class="swiper-slide" style="background-image:url(https://dn-qiniucdn2.qbox.me/topic/live/6.jpg)"></div>
        </div>
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

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/base.js"></script>
<script src="/js/lib/swiper/swiper.jquery.min.js"></script>
<script>
  $(function(){
    var $page=$('.page-bg'),
            $pageTit=$page.find('.page-tit'),
            $main=$('#main'),
            transitionEvent='transitionend webkitTransitionEnd oTransitionEnd otransitionend MSTransitionEnd';


    function transitionEnd() {
      return false;
      var el = document.createElement('div')//what the hack is bootstrap

      var transEndEventNames = {
        WebkitTransition : 'webkitTransitionEnd',
        MozTransition    : 'transitionend',
        OTransition      : 'oTransitionEnd otransitionend',
        transition       : 'transitionend'
      };

      for (var name in transEndEventNames) {
        if (el.style[name] !== undefined) {
          return transEndEventNames[name];
        }
      }

      return false // explicit for ie8 (  ._.)
    }

    function swipeInit(){
      var galleryTop = new Swiper('.gallery-top', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        spaceBetween: 10,
      });
      var galleryThumbs = new Swiper('.gallery-thumbs', {
        spaceBetween: 10,
        centeredSlides: true,
        slidesPerView: 'auto',
        touchRatio: 0.2,
        slideToClickedSlide: true
      });
      galleryTop.params.control = galleryThumbs;
      galleryThumbs.params.control = galleryTop;

      galleryTop.slideTo(2);
    }

    function init(){
      var tran=transitionEnd();
      if(tran){
        setTimeout(function(){
          $page.addClass('active');
        },500);

        $page.one(tran,function() {
          $pageTit.addClass('s-2');
          $page.removeClass('active');
        });
        $pageTit.one(tran,function(){
          $main.slideDown('slow',function(){
            swipeInit();
          });
        });
        swipeInit();
      }else{
        $pageTit.addClass('s-2');
        $page.removeClass('active');
        $main.slideDown('slow',function(){
          swipeInit();
        });

      }
    }

    init();
  });
</script>

<script id='player_embed' src='//player.cloud.wowza.com/hosted/ljzftkpz/wowza.js' type='text/javascript'></script>

<script src="https://www.rumbletalk.com/client/?HXxCp-3-"></script>
</body>
</html>