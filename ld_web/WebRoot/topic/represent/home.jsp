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
  <title>代言</title>
  <link href="/css/util/reset.css" rel="stylesheet"/>
  <link href="/css/util/common.css" rel="stylesheet"/>
  <link href="/css/util/animation.css" rel="stylesheet"/>

  <link type="text/css" media="all" rel="stylesheet" href="/css/topic/jquery.tosrus.all.css">
  <link href="css/home.css?v=1229" rel="stylesheet"/>
  <style>
  .tos-slide:before{ height:auto;}
  </style>
</head>
<body>
<div id="header">
  <div class="container">
    <a href="/" class="fl logo"><img src="/images/logo.png" width="180" alt=""></a>

    <div class="fl top-link">
       
      <a href="/topic/represent/home.jsp">新年双庆</a>
      <a href="/topic/lover/home.jsp">情人节约会</a>
      <a href="/topic/newyear/home.jsp">恭贺新禧</a>
      <a href="/topic/newyear/week.jsp">金鸡报喜</a>
    </div>

    <div class="fr top-link" id="myScrollspy">
      <ul class="nav nav-tabs" >
        <li class="active"><a href="#section-top">签约视频</a></li>
        <li><a href="#section-one">代言花絮</a></li>
        <li><a href="#section-two">于芷晴简介</a></li>
      </ul>
    </div>

  </div>
</div>

<div class="page-bg">
  <div id="section-top" class="section section-top">
    <div class="section-inner">
      <div class="person"></div>
      <div class="caption a-bounceinT">
         <div class="video-play text-center">
          <div style="width:600px;margin-left:20px; background:#000;padding:10px;">
            <video width="100%" height="auto" controls="" autoplay="autoplay">
              <source src="https://dn-qiniucdn2.qbox.me/topic/live/yzq-0124.mp4" type="video/mp4">  </video>
          </div>
        </div>
      </div>

    </div>
  </div>
  <div id="section-one" class="section section-one bg active">
    <div class="section-inner">
      <div class="tit tit-one"></div>
        <div class=" clearfix"></div>
       <div class="show-picbox">
          <div class="banner-slides">
              <div class="thumbs show-pic">
                  <a class="li" href="images/slide/pic1.jpg"> <img
                          src="images/slide/pic1.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic2.jpg"> <img
                          src="images/slide/pic2.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic3.jpg"> <img
                          src="images/slide/pic3.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic4.jpg"> <img
                          src="images/slide/pic4.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic5.jpg"> <img
                          src="images/slide/pic5.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic6.jpg"> <img
                          src="images/slide/pic6.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic7.jpg"> <img
                          src="images/slide/pic7.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic8.jpg"> <img
                          src="images/slide/pic8.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic9.jpg"> <img
                          src="images/slide/pic9.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic10.jpg"> <img
                          src="images/slide/pic10.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic11.jpg"> <img
                          src="images/slide/pic11.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic12.jpg"> <img
                          src="images/slide/pic12.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic13.jpg"> <img
                          src="images/slide/pic13.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic14.jpg"> <img
                          src="images/slide/pic14.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic15.jpg"> <img
                          src="images/slide/pic15.jpg"/> <span class="click-big"></span></a>
              </div>
              <div class="thumbs show-pic">
                  <a class="li" href="images/slide/pic16.jpg"> <img
                          src="images/slide/pic16.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic17.jpg"> <img
                          src="images/slide/pic17.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic18.jpg"> <img
                          src="images/slide/pic18.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic19.jpg"> <img
                          src="images/slide/pic19.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic20.jpg"> <img
                          src="images/slide/pic20.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic21.jpg"> <img
                          src="images/slide/pic21.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic22.jpg"> <img
                          src="images/slide/pic22.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic23.jpg"> <img
                          src="images/slide/pic23.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic24.jpg"> <img
                          src="images/slide/pic24.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic25.jpg"> <img
                          src="images/slide/pic25.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic26.jpg"> <img
                          src="images/slide/pic26.jpg"/> <span class="click-big"></span></a>
                  <a class="li" href="images/slide/pic27.jpg"> <img
                          src="images/slide/pic27.jpg"/> <span class="click-big"></span></a>
      
              </div>
         
          </div>
          <span class="prev"></span>
          <span class="next"></span>
      </div>

    </div>
  </div>
  <div id="section-two" class="section section-two active">
    <div class="section-inner">
      <div class="tit tit-two"></div>
      <div class="cfx">
        <div class="m-content">
          <p>于芷晴，90后知名网络嫩模 。</p>
          <p>北京人，1992年1月27日生，现职业为美术编辑。</p>
          <p>曾是2011上海车展车模，2011青岛车展，2011深圳车展车模，路虎、捷豹、奥迪车模，并获得全国环保形象大使最优人气奖、CCTV6《爱秀电影》获得‘爱秀之星’称号，拥有甜美相貌和性感身形，内地新生代宅男女神。</p>
          <table class="info">
            <tr>
              <td>中文名：于芷晴</td>
              <td>身  高：178CM</td>
            </tr>
            <tr>
              <td>外文名：MINI</td>
              <td>出生地：中国北京</td>
            </tr>
            <tr>
              <td>别   名：小鱼</td>
              <td>出生日期：1992.1.27</td>
            </tr>
            <tr>
              <td>国   籍：中国</td>
              <td>职   业：模特演员</td>
            </tr>
            <tr>
              <td>星   座：水瓶座</td>
              <td>三   围：93/62/91</td>
            </tr>
            <tr>
              <td>血   型：A型</td>
              <td>出道时间：2012年</td>
            </tr>
          </table>
        </div>
        <div class="slide">
          <div class="slide-item"><img src="images/slide/1.jpg" alt=""></div>
          <div class="slide-item"><img src="images/slide/2.jpg" alt=""></div>
          <div class="slide-item"><img src="images/slide/3.jpg" alt=""></div>
          <div class="slide-item"><img src="images/slide/4.jpg" alt=""></div>
          <div class="slide-item active"><img src="images/slide/5.jpg" alt=""></div>
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
        function banner(){


          /*  $('.show-pic a.li').tosrus({
                buttons   : 'inline',
                pagination: {
                    add : true,
                    type: 'thumbnails'
                }
            });

            //	Add a custom filter to recognize images from lorempixel (that don't end with ".jpg" or something similar)
            $.tosrus.defaults.media.image = {
                filterAnchors: function ($anchor) {
                   /!* return $anchor.attr('href').indexOf('pic') > -1;*!/
                    return false;
                }
            };*/

            $(".banner-slides").tosrus({
                infinite: true,
                slides  : {
                    visible: 1
                }
            });
        }

        function playVideo(){
            var $play=$('#j-play'),
                $btn=$('#j-play-btn').find('li'),
                tpl='<video width="624" height=346"" height="auto"  controls>'+
                    '<source src="{{src}}" type="video/mp4">  </video>';

            $btn.on('click',function(){
                var url=$(this).attr('data-url');
                $play.html(tpl.replace(/\{\{src\}\}/g,url));
                $(this).addClass('active').siblings('li').removeClass('active');

            });
        }

        function openAffix(){
            $("#header").affix({offset: {top: 100}});
            $('body').scrollspy({ target: '#myScrollspy',offset:600 });
        }

//        $('#myScrollspy').on('activate.bs.scrollspy', function (e) {
//            var href= $(e.target).find('a').attr('href');
//            $(href).addClass('active').siblings('.section').removeClass('active');
//        });

        openAffix();
        playVideo();
        banner();
    });

        jQuery(function ($) {
        //	Add a custom filter to recognize images from lorempixel (that don't end with ".jpg" or something similar)
        $.tosrus.defaults.media.image = {
            filterAnchors: function ($anchor) {
                return $anchor.attr('href').indexOf('pic') > -1;
            }
        };

        $('.show-pic a').tosrus({
            buttons   : 'inline',
            pagination: {
                add : true,
                type: 'thumbnails'
            }
        });
        <%--banner-slides--%>
        $(".banner-slides").tosrus({
            infinite: true,
            slides  : {
                visible: 1
            }
        });

     

       
    });
</script>
</body>
</html>