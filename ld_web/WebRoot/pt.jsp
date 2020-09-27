﻿<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <s:include value="/title.jsp"></s:include>
  	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/game.css?v=11"/>
</head>
<body>
<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
<div class="banner">
  <img src="${ctx}/images/slot-banner.jpg?v=1" alt=""/>
</div>
<div class="game-wp wp cfx">

  <div class="game-top cfx">
      <div class="winner-msg-outer" id="j-marquee-winner-list">
          <ul class="winner-msg cfx" >
              <jsp:include page="/tpl/winnerList.jsp"></jsp:include>
          </ul>
      </div>

      <div class="winner">
          <div id="j-count-num" class="count-num"></div>
      </div>
  </div>
  <ul class="game-btn-goup">
      <li class="active"><a href="/pt.asp"><i class="icon icon-game-pt"></i>PT老虎机</a></li>
      <li><a href="/asp/ttLogin.aspx"><i class="icon icon-game-ttg"></i>MG/TTG老虎机</a></li>
      <li><a href="/gameNt.jsp"><i class="icon icon-game-nt"></i>NT老虎机</a></li>
      <li><a href="/gameQt.jsp"><i class="icon icon-game-qt"></i>QT老虎机</a></li>
    </ul>
  <aside class="game-aside">
      <h2>PT老虎机</h2>
      <ul class="tab-hd tab-game-catetory">
          <li class="active">
              <a data-toggle="tab" href="#tab-hotGames">热门游戏</a>
              <i class="iconfont icon-hot"></i>
          </li>
           <li><a data-toggle="tab" href="#tab-newGames">新游戏</a></li>
          <li><a data-toggle="tab" href="#tab-slotGames">经典老虎机</a></li>
          <li class="j-sub-nav dropdown-toggle"><a data-toggle="tab" href="#tab-eleSlotGames">电动吃角子老虎</a>
              <div class="sub-nav">
                  <a data-toggle="tab" href="#tab-miracle">【奇迹大奖】</a>
                  <a data-toggle="tab" href="#tab-5line">【5-10条线】</a>
                  <a data-toggle="tab" href="#tab-15line">【15-20条线】</a>
                  <a data-toggle="tab" href="#tab-25line">【25+条线】</a>
                  <a data-toggle="tab" href="#tab-otherLine">【多旋转游戏】</a>
              </div>
          </li>
          <li><a data-toggle="tab" href="#tab-pokerGames">视频扑克</a></li>
          <li><a data-toggle="tab" href="#tab-streetGames">街机游戏</a></li>
          <li><a data-toggle="tab" href="#tab-accumulateGames">累积游戏</a></li>
          <li><a data-toggle="tab" href="#tab-demoGames">试玩游戏</a></li>
          <li><a data-toggle="tab" href="#tab-rescueGames">救援金及幸运转游戏</a></li>
      </ul>
      <div class="slot-btn-wp cfx">
          <a href="http://cdn.sunrise99.im/sunrise/setupglx.exe" class="btn-game"><i class="iconfont icon-download-2"></i>下载地址</a>
          <a href="javascript:void(0);" class="btn-game"><img class="img-qr" src="/images/qr/wechat.png?v=1112" alt="" width="120" height="120"/> 微信客服</a>
      </div>
  </aside>
  <div class="tab-bd gamelist-wp">
      <div id="j-gamelist-tpl"></div>
    <script id="tpl" type="text/x-handlebars-template">
        {{#games}}
            <ul id="tab-{{type}}" class="tab-panel gamelist fade">
                {{#if demo}}
                    {{#each gameList}}
                    <li class="game-info">
                        <div class="game-pic">
                            <img src="${ctx}/images/ptgames/{{this.pic}}" width="156" height="180" alt=""/>
                        </div>
                        <div class="name"><h4>{{this.name}}</h4></div>
                        <div class="ename">{{this.eName}}</div>
                        <%--<div class="ename">{{game-type ../type}}</div>--%>
                        <div class="game-play">
                            <a href="/loginGame.aspx?gameCode={{this.code}}" target="_blank" class="btn-game play">进入游戏</a>
                            <a href="{{game-type ../type}}/launchcasino.html?mode=offline&affiliates=1&language=zh-cn&game={{this.code}}" target="_blank" class="btn-game demo">免费试玩</a>
                        </div>
                    </li>
                    {{/each}}
                {{else}}
                    {{#each gameList}}
                    <li class="game-info">
                        <div class="game-pic">
                            <img src="${ctx}/images/ptgames/{{this.pic}}" width="156" height="180" alt=""/>
                        </div>
                        <div class="name"><h4>{{this.name}}</h4></div>
                        <div class="ename">{{this.eName}}</div>

                        <div class="game-play">
                            <a href="/loginGame.aspx?gameCode={{this.code}}" target="_blank" class="btn-game play">进入游戏</a>
                        </div>
                        <%--<div class="game-bonus">
                            <iframe src="" frameborder="0" width="100%"></iframe>
                        </div>--%>
                    </li>
                    {{/each}}
                {{/if}}
            </ul>
        {{/games}}
      </script>
  </div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script src="${ctx}/js/lib/handlebars-v3.0.3.js"></script>
<script src="${ctx}/js/lib/countUp.js"></script>
<script>
    var cookie_path = "/";
    $(function(){
        var options = {
            useEasing : true,
            useGrouping : true,
            separator : ',',
            decimal : '.',
            prefix : '',
            suffix : ''
        };
        var demo = new CountUp("j-count-num", 11200000, 11216110, 2, 3000, options);
        demo.start();


        //判断游戏类型
        Handlebars.registerHelper('game-type', function(value, options) {
            //console.log('fn(this):', options.fn(this)); // fn(this): 2是偶数
            if(value  ==='newGames') {
                return 'http://cache.download.banner.redcoral88.com/flash/54';

            } else {
                return 'http://cache.download.banner.redcoral88.com/flash/54';
            }
        });
        //用jquery获取模板
        var tpl   =  $("#tpl").html();
        //预编译模板
        var template = Handlebars.compile(tpl);

        $.getJSON('${ctx}/js/json/pt.json?v=5',function(data){
            //模版填充数据
            var html=template({games:data});
            $('#j-gamelist-tpl').html(html);
            $('#j-gamelist-tpl>ul').eq(0).addClass('active in');
            
        });
        
    });


   /* function countNum(){
        $('.game-bonus').find('iframe').attr('src','/ptTicker.html');
    }*/
</script>
</body>
</html>