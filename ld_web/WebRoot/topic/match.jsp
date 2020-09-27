<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.DateUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@page import="dfh.action.vo.QueryDataEuroVO" %>
<%@page import="dfh.utils.AxisSecurityEncryptUtil" %>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>比赛活动</title>
  <link rel="stylesheet" href="/css/util/reset.css" />
  <link rel="stylesheet" href="/css/util/iconfont.css?v=333" />
  <link rel="stylesheet" href="/css/util/common.css" />
  <link rel="stylesheet" href="/css/topic/match.css">
  <link rel="stylesheet" href="/css/topic/filpclock.css">
  <link rel="stylesheet" href="/css/magnific-popup.css">
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
        <a class="i-btn" href="/">免费开户</a>
      </c:if>

    </div>
  </div>
</div>
<div class="w1920">
  <div id="banner" class="carousel slide" data-ride="carousel" data-interval="false">
    <div class="carousel-inner">
      <div class="item active" style="background-image:url(/images/topic/match/banner-02.jpg)">
        <div class="container">
          <div class="team-logo-wrapper">
            <div class="left">
              <img src="/images/topic/match/hd-logo.png" class="pic" width="120" alt="">
              <h2 class="title">广州恒大 <sub>Guangzhou Evergrande taobao</sub></h2>
            </div>
            <div class="right">
              <img src="/images/topic/match/sek-logo.png" class="pic" width="120" alt="">
              <h2 class="title">德甲沙尔克 04 <sub>The bundesliga schalke 04</sub></h2>
            </div>
          </div>
        </div>
      </div>
      <div class="item" style="background-image:url(/images/topic/match/banner-01.jpg)">
        <div class="container">
          <div class="team-logo-wrapper">
            <div class="left">
              <img src="/images/topic/match/fl-logo.png" class="pic" width="120" alt="">
              <h2 class="title">广州富力 <sub>Guangzhou R&F</sub></h2>
            </div>
            <div class="right">
              <img src="/images/topic/match/sek-logo.png" class="pic" width="120" alt="">
              <h2 class="title">德甲沙尔克 04 <sub>The bundesliga schalke 04</sub></h2>
            </div>
          </div>

        </div>
      </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="carousel-control left" href="#banner"
       data-slide="prev">&nbsp;</a>
    <a class="carousel-control right" href="#banner"
       data-slide="next">&nbsp;</a>
  </div>
</div>
<div class="main">
  <ul class="menu cfx" id="j-menu">
    <li class="active"><a href="#tab-tickets" data-toggle="tab">限时抢票<p>Time limit rob tickets</p></a></li>
    <li><a href="#tab-profile" data-toggle="tab">赛事简介<p>Event profile</p></a></li>
    <li><a href="#tab-activities" data-toggle="tab">赛事活动<p>Event activities</p></a></li>
    <li><a href="#tab-bidbits" data-toggle="tab">精彩花絮<i class="iconfont icon-hot3"></i><p>Brilliant tidbits</p></a></li>
    <li><a href="#tab-coverage" data-toggle="tab">媒体报道<p>Media coverage</p></a></li>
  </ul>
  <div class="tab-bd">
    <div id="tab-profile" class="tab-panel">
      <div id="j-team1">
        <div class="cfx">
          <ul class="team-list fl">
            <li class="tit-wrapper"><h2 class="title">广州恒大 <sub>Guangzhou Evergrande taobao</sub></h2> </li>
            <li class="box">
              <img src="/images/topic/match/h-1.jpg" width="123" height="123" alt="" class="fl">
              <p><em>郑智</em> 身高180cm，体重75kg。现效力广州恒大足球俱乐部，是现
                任中国家队队长和广州恒大队长。郑智身体素质好，技术全面，经验丰富，擅长远射。郑智效力深圳健力宝以及山东鲁能期间先后两次获得中超冠军。2007年转会英超查尔顿足球俱乐部。2009年9月签约苏超凯尔特人。2010年6月底亮相广州恒大。郑智于2002和2006年度两获中国足球先生。</p>
            </li>
            <li class="box">
              <img src="/images/topic/match/h-2.jpg" width="123" height="123" alt="" class="fl">
              <p><em>马丁内斯</em> 在效力波尔图期间连续三个赛季夺得葡超金靴，平
                均每个赛季葡超联赛进球数约为23个，帮助波尔图夺得过1次葡超联赛冠军和2次葡萄牙超级杯赛冠军。后转会马德里竞技。2016年2月3日，广州恒大足球俱乐部官方宣布以4200万欧元将其引进。</p>
            </li>
            <li class="box">
              <img src="/images/topic/match/h-3.jpg" width="123" height="123" alt="" class="fl">
              <p><em>保利尼奥</em> 巴西足球运动员，被球迷昵称为“暴力鸟”，也有
                “巴西兰帕德”的称号。1988年07月25日出生于巴西（Sao Paulo）,出道于维尔纽斯足球俱乐部的青训营，2013-14赛季从科林蒂安转会至托特纳姆热刺；场上司职中前卫，还可出任后腰。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/h-4.jpg" width="123" height="123" alt="" class="fl">
              <p><em>高拉特</em> 2015年1月13日上午（巴西当地时间1月12日晚），广州恒
                大足球俱乐部官方宣布与巴甲卫冕冠军克鲁塞罗队主力双子星、现役巴西国脚里卡多·高拉特·佩雷拉(全名Ricardo Goulart Pereira)正式签约4年，转会费1500万欧元。体检合格后，高拉特1月20日左右直接飞赴西班牙同球队汇合冬训，身披11号战袍。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/h-5.jpg" width="123" height="123" alt="" class="fl">
              <p><em>郜林</em> 2013年，郜林帮助广州恒大队获得亚冠联赛冠军，并时隔156天之
                后，在亚冠决赛中取得了中国国内球员首球。2014年12月27日，入选阿兰·佩兰执教的国家队，并随队在出征在澳大利亚举行的2015年亚洲杯。2016年3月12日，郜林已经累计为广州恒大俱乐部打入77球，与穆里奇并列成为球队历史最佳射手。
              </p>
            </li>
          </ul>
          <ul class="team-list two fr">
            <li class="tit-wrapper"><h2 class="title">德甲沙尔克 04 <sub>The bundesliga schalke 04</sub></h2> </li>
            <li class="box">
              <img src="/images/topic/match/s-1.jpg" width="123" height="123" alt="" class="fl">
              <p><em>拉尔夫·费尔曼</em> 2006年，他迎来了自己职业生涯中至
                今为止最美妙的一刻：和队友们一起在决赛里2:1战胜拜仁慕尼黑，赢得了当年U19的德国冠军。1年之后，他入选了德国U19队，并随队参加了U19欧洲杯。拉尔夫·费尔曼现在效力于沙尔克04足球俱乐部。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-2.jpg" width="123" height="123" alt="" class="fl">
              <p><em>内田笃人</em> 内田笃人（Atsuto Uchida，1988年3月27日－），
                出生于日本静冈县函南町，是一名足球运动员，现在效力于德国足球甲级联赛球队沙尔克04足球俱乐部，他是日本国家队的成员。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-3.jpg" width="123" height="123" alt="" class="fl">
              <p><em>博阿滕</em> 现效力AC米兰足球俱乐部，曾效力于沙尔克04足球俱乐
                部。凯文-普林斯·博阿滕是柏林赫塔队培养的年轻中场球员，1987年3月6日出生。博阿滕身体强壮，技术全面，加盟AC米兰队首个赛季就展现出了惊人的实力，并稳稳占据了主力中场的位置。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-4.jpg" width="123" height="123" alt="" class="fl">
              <p><em>德拉克斯勒</em> 德拉克斯勒1993年9月20日出生在德国，8岁
                进入沙尔克的青训体系，2010年底被马加特列入赛季冬训名单，并且很快就迎来了在德甲的首秀。朱利安·德拉克斯勒 （Julian Draxler，1993.09.20 － ），德国足球运动员，是一名中场球员。现效力于德甲沃尔夫斯堡足球俱乐部，曾效力于沙尔克04足球俱乐部。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-5.jpg" width="123" height="123" alt="" class="fl">
              <p><em>杰弗森·法尔范</em> 杰弗森·奥古斯丁·法尔范·瓜达鲁
                目前是秘鲁现役国脚，此前曾在秘鲁国内著名球队利马阿里安萨队效力，是名优秀的锋线新星，在2003年赛季他帮助该队获得了秘鲁联赛冠军，并在比赛中攻入13球。2004年6月25日法尔范与荷兰埃因霍温队签约四年 。
              </p>
            </li>
          </ul>
        </div>
        <div class="line"></div>
        <h2 class="title">球队简介 <sub>The Team Introduction</sub></h2>
        <div class="m-row">
          <div class="col-6 pr20 team-desc">
            <img src="/images/topic/match/hd-logo.png" alt="" width="58" class="fl pic">
            <p class="cnt">广州恒大足球俱乐部（Guangzhou Evergrande Taobao Football Club）是中国广州的一所
              职业足球俱乐部，现参加中国足球超级联赛。2011赛季启用广州天河体育场作为主场至今。 <br> <br>
              广州恒大足球俱乐部前身是成立于1954年的广州市足球队。1993年1月，广州市足球队通过和太阳神集团合作，成为中国第一家政府与企业合股的职业足球俱乐部。2010年3月1日，恒大集团买断球队全部股权，俱乐部更名为广州恒大足球俱乐部。2012年俱乐部首次参加亚洲足球俱乐部冠军联赛并进入八强，2013年获得亚洲足球俱乐部冠军联赛冠军，这也是中国俱乐部第一次问鼎该项赛事的冠军，同年获亚足联最佳俱乐部奖。2014年6月5日，阿里巴巴入股恒大俱乐部50%的股权；同年7月4日俱乐部更名为广州恒大足球俱乐部。2015年2月26日，里皮辞去恒大主教练职务，卡纳瓦罗接任；同年6月4日恒大官方宣布卡纳瓦罗下课，巴西名帅路易斯·菲利佩·斯科拉里上任。
              <br> <br>
              截至2016年2月，广州恒大足球俱乐部已连续五次获得中超联赛冠军，也是中超联赛中夺冠次数最多的球队；并获得两次中国足球超级杯冠军和一次中国足协杯冠军；两次亚冠联赛冠军 。2015中超新赛季广州恒大主场球衣以红色为主色调，虎斑条纹显现王者之气。</p>
          </div>
          <div class="col-6 pl20 team-desc">
            <img src="/images/topic/match/sek-logo.png" alt="" width="58" class="fl pic">
            <p class="cnt">盖尔森基兴-沙尔克04足球俱乐部（德文：Futball-Club Gelsenkirchen-Schalke 04，简
              称：FC Schalke 04），位于德国西部北莱茵-威斯特法伦州盖尔森基兴市沙尔克地区，是
              德国和欧洲足坛的传统劲旅。 <br> <br>
              沙尔克04原来的花园体育场（Parkstadion）在90年代开始展现老旧，当球队在1997年历史性首夺欧洲联盟杯及2004年的百年会庆，球队决定兴建一座全新的多功能运动场，于1998年兴德国的建筑公司HBM签定价值1,860万欧元的承建合约。
              <br> <br>
              百年老店沙尔克04在1877年成立时使用左上角队徽，款式与上世纪90年代的柏林赫塔如出一辙；1924-1929年使用右上角队徽，显得非常随意；左下角为球队1945-1958年使用队徽；1959年至今沙尔克04一直使用现有队徽，德甲走入职业化后与顶级联赛锦标再无亲密接触。
              <br> <br>
              沙尔克04是一只非常有个性的球队，在德国全国范围内有着众多球迷，甚至包括联邦总理。沙尔克是一个有着悠久历史却散发着年轻魅力的球队。沙尔克也出德国历史上第一个黑人国脚:阿萨莫阿。</p>
          </div>
        </div>
      </div>

      <div id="j-team2" style="display: none;">
        <div class="cfx">
          <ul class="team-list fl">
            <li class="tit-wrapper"><h2 class="title">广州富力 <sub>Guangzhou R&F</sub></h2> </li>
            <li class="box">
              <img src="/images/topic/match/f-1.jpg" width="123" height="123" alt="" class="fl">
              <p><em>卢琳</em> 卢琳，生于广州，是一名技术出色、能射能传、盘带突破犀利的
                边路球员，曾效力于广州恒大（及其前身广州日之泉、广州医药等），现在效力于广州富力足球俱乐部。2008年5月7日奥运火炬将在广州市进行传递。从广药俱乐部获得消息，广药中一队主教练沈祥福和球员卢琳非常幸运地成为了广州火炬传递手，他们将在7日手持祥云火炬传递奥运圣火。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/f-2.jpg" width="123" height="123" alt="" class="fl">
              <p><em>张远</em> 张远，1989年12月8日生，现役国青队员，百米速度达到了10秒
                9，和当年的“猎豹”姚夏几乎不相上下，外号“小飞侠”。
                现属广州富力足球俱乐部。2016年2月27日，北京人和宣布张远正式租借加盟，新赛季张远将身披8号战袍征战中甲联赛 。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/f-3.jpg" width="123" height="123" alt="" class="fl">
              <p><em>程月磊</em> 程月磊（1987年10月28日－），中国足球运动员，司职守
                门员，现效力于广州富力足球俱乐部。曾入选过国青，国奥和国家足球队。2012年1月，从深圳红钻足球俱乐部转会至广州富力足球俱乐部，签约5年，转会费500万。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/f-4.jpg" width="123" height="123" alt="" class="fl">
              <p><em>朴钟佑</em> 朴钟佑，韩国职业足球运动员，场上位置为中场，现效力
                于阿尔贾齐拉足球俱乐部。北京时间2012年8月12日，在奥运会男子足球三四名的争夺战中，韩国队以2-0完胜日本队。赛后，获得铜牌的韩国队球员朴钟佑手举一张写有“独岛是韩国领土”的标语绕场庆祝，结果遭到了国际奥委会的处罚，使得他无法与队友一起在温布利大球场领受铜牌。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/f-5.jpg" width="123" height="123" alt="" class="fl">
              <p><em>姜宁</em> 山东省青岛市人，1986年9月1日出生，中国足球运动员，场上
                位置为前锋。姜宁曾经是青岛中能足球俱乐部、广州恒大足球俱乐部、广州富力足球俱乐部的球员，现效力于河北华夏幸福足球俱乐部。2006赛季联赛第4轮客场对阵重庆的比赛中，姜宁上演帽子戏法，成为中国联赛上演帽子戏法最年轻的球员。
              </p>
            </li>
          </ul>
          <ul class="team-list two fr">
            <li class="tit-wrapper"><h2 class="title">德甲沙尔克 04 <sub>The bundesliga schalke 04</sub></h2> </li>
            <li class="box">
              <img src="/images/topic/match/s-1.jpg" width="123" height="123" alt="" class="fl">
              <p><em>拉尔夫·费尔曼</em> 2006年，他迎来了自己职业生涯中至
                今为止最美妙的一刻：和队友们一起在决赛里2:1战胜拜仁慕尼黑，赢得了当年U19的德国冠军。1年之后，他入选了德国U19队，并随队参加了U19欧洲杯。拉尔夫·费尔曼现在效力于沙尔克04足球俱乐部。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-2.jpg" width="123" height="123" alt="" class="fl">
              <p><em>内田笃人</em> 内田笃人（Atsuto Uchida，1988年3月27日－），
                出生于日本静冈县函南町，是一名足球运动员，现在效力于德国足球甲级联赛球队沙尔克04足球俱乐部，他是日本国家队的成员。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-3.jpg" width="123" height="123" alt="" class="fl">
              <p><em>博阿滕</em> 现效力AC米兰足球俱乐部，曾效力于沙尔克04足球俱乐
                部。凯文-普林斯·博阿滕是柏林赫塔队培养的年轻中场球员，1987年3月6日出生。博阿滕身体强壮，技术全面，加盟AC米兰队首个赛季就展现出了惊人的实力，并稳稳占据了主力中场的位置。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-4.jpg" width="123" height="123" alt="" class="fl">
              <p><em>德拉克斯勒</em> 德拉克斯勒1993年9月20日出生在德国，8岁
                进入沙尔克的青训体系，2010年底被马加特列入赛季冬训名单，并且很快就迎来了在德甲的首秀。朱利安·德拉克斯勒 （Julian Draxler，1993.09.20 － ），德国足球运动员，是一名中场球员。现效力于德甲沃尔夫斯堡足球俱乐部，曾效力于沙尔克04足球俱乐部。
              </p>
            </li>
            <li class="box">
              <img src="/images/topic/match/s-5.jpg" width="123" height="123" alt="" class="fl">
              <p><em>杰弗森·法尔范</em> 杰弗森·奥古斯丁·法尔范·瓜达鲁
                目前是秘鲁现役国脚，此前曾在秘鲁国内著名球队利马阿里安萨队效力，是名优秀的锋线新星，在2003年赛季他帮助该队获得了秘鲁联赛冠军，并在比赛中攻入13球。2004年6月25日法尔范与荷兰埃因霍温队签约四年 。
              </p>
            </li>
          </ul>
        </div>
        <div class="line"></div>
        <h2 class="title">球队简介 <sub>The Team Introduction</sub></h2>
        <div class="m-row">
          <div class="col-6 pr20 team-desc">
            <img src="/images/topic/match/fl-logo.png" alt="" width="58" class="fl pic">
            <p class="cnt">广州富力足球俱乐部，是一家位于亚洲中国广东省广州市的职业足球俱乐部。其前身为成立
              于1987年的沈阳足球队，曾参加历年的中国职业联赛，是中国职业联赛发起者之一。 <br><br>

              2011年6月25日，广州富力地产股份有限公司在深圳凤凰足球队濒临解散之际毅然收购了该球队并正式更名为广州富力足球队，注资成立广州富力足球俱乐部，并逐步完善俱乐部架构、各级队伍建设及训练基地、青训系统等建设，2011年获得中甲联赛亚军成功冲超。其主场依然是广州球迷所熟悉的越秀山体育场。
              <br><br>

              2014年荣膺中超联赛季军，成功获得次年征战亚冠联赛的资格。这也是广州富力建队史上第一次进入亚冠杯，广州成为中国第一座拥有两支亚冠球队的城市（广州恒大淘宝、广州富力）。<br><br>

              2011年9月，广州富力地产股份有限公司在“中国足球之乡”广东省梅州市梅县区人民政府签约合作共建“梅州市梅县区富力足球学校”，后与英超豪门切尔西在足校青训方面全方位深入合作，因此足校定名为“富力切尔西足球学校”。</p>
          </div>
          <div class="col-6 pl20 team-desc">
            <img src="/images/topic/match/sek-logo.png" alt="" width="58" class="fl pic">
            <p class="cnt">盖尔森基兴-沙尔克04足球俱乐部（德文：Futball-Club Gelsenkirchen-Schalke 04，简
              称：FC Schalke 04），位于德国西部北莱茵-威斯特法伦州盖尔森基兴市沙尔克地区，是
              德国和欧洲足坛的传统劲旅。 <br> <br>
              沙尔克04原来的花园体育场（Parkstadion）在90年代开始展现老旧，当球队在1997年历史性首夺欧洲联盟杯及2004年的百年会庆，球队决定兴建一座全新的多功能运动场，于1998年兴德国的建筑公司HBM签定价值1,860万欧元的承建合约。
              <br> <br>
              百年老店沙尔克04在1877年成立时使用左上角队徽，款式与上世纪90年代的柏林赫塔如出一辙；1924-1929年使用右上角队徽，显得非常随意；左下角为球队1945-1958年使用队徽；1959年至今沙尔克04一直使用现有队徽，德甲走入职业化后与顶级联赛锦标再无亲密接触。
              <br> <br>
              沙尔克04是一只非常有个性的球队，在德国全国范围内有着众多球迷，甚至包括联邦总理。沙尔克是一个有着悠久历史却散发着年轻魅力的球队。沙尔克也出德国历史上第一个黑人国脚:阿萨莫阿。</p>
          </div>
        </div>
      </div>
    </div>
    <div id="tab-tickets" class="tab-panel active">
      <div class="match-box">
        <div class="match-item box fl">
          <img src="/images/topic/match/fl-logo.png" class="fl" width="109" alt="">
          <img src="/images/topic/match/sek-logo.png" class="fr" width="109" alt="">
          <div>
            <h1>广州富力 VS 沙尔克04</h1>
            <p>抢票<span class="c-yellow">开始</span>时间：2016-06-22 00:00:00</p>
            <p>抢票 <span class="c-red">结束</span>时间：2016-06-26 00:00:00</p>
            <p class="mb20 mt20">地点：中国广州市</p>
            <input type="button" class="btn-click" id="j-btn-ticket1" value="立即抢票(60张)" disabled>
          </div>
        </div>
        <div class="match-item box fr">
          <img src="/images/topic/match/hd-logo.png" class="fl" width="98" height="116" alt="">
          <img src="/images/topic/match/sek-logo.png" class="fr" width="109" alt="">
          <div>
            <h1>广州恒大 VS 沙尔克04</h1>
            <p>抢票<span class="c-yellow">开始</span>时间：2016-06-27 00:00:00</p>
            <p>抢票<span class="c-red">结束</span>时间：2016-06-30 00:00:00</p>
            <p class="mt20 mb20">地点：中国佛山市</p>
            <input type="button" class="btn-click" id="j-btn-ticket2" disabled value="立即抢票(60张)">
          </div>
        </div>
      </div>

      <div class="box">
        <div id="j-clock" style="margin: -69px auto 0;width: 660px;"></div>
        <div class="text-center" style="position: relative;top: -60px"><img src="/images/topic/match/field-2.png" alt=""></div>
        <div class="m-content">
          <div class="data-list">
            <table class="table">
              <thead>
              <tr><th>排名</th>
                <th>游戏账号</th>
                <th>抢票时间</th></tr>
              </thead>
              <tbody id="j-data-list">
              <cache:cache key="headnewsCache2" scope="application" time="300">
                <%
                  List<QueryDataEuroVO>headnewsList2 = AxisSecurityEncryptUtil.queryDataEuro();
                  if (headnewsList2 == null) {
                    headnewsList2 = new ArrayList();

                  }
                  application.setAttribute("headnewsList2", headnewsList2);
                %>
                <s:url action="queryAnnObject" namespace="/asp" var="queryAnnObjectUrl"></s:url>
                <s:if test="#application.headnewsList2.size()<=0">
                  <tr><td colspan="3">无数据</td></tr>
                </s:if>
                <s:else>
                  <s:iterator value="#application.headnewsList2" var="indexTopNews"  status="index">
                    <tr>
                      <td><s:property value="%{#index.index+1}"/></td>
                      <td>${indexTopNews.loginname}</td>
                      <td>${indexTopNews.createtime}</td>
                    </tr>
                  </s:iterator>
                </s:else>
              </cache:cache>
              </tbody>
            </table>
          </div>

          <h3>中德体育限时抢门票</h3>
          <p>最强赛事即将开战，天威回馈广大玩家支持，提供限量门票让你进场观看现场足球对阵，享受绝对震撼的狂热氛围。先抢先得，抢完就没啦。</p>
          <p>最强赛事一:</p>
          <p class="c-white">广州富力      vs  沙尔克04    2016年7月5日   地点:中国广州市    (限量60张门票)</p>
          <p>最强赛事二:</p>
          <p class="c-white">广州恒大  vs  沙尔克04    2016年7月7日   地点:中国佛山市    (限量60张门票)</p>
          <h3>活动对象:</h3>
          <h3>抢票时间:</h3>
          <p class="c-white">广州富力VS沙乐克04    22号00：00：00——26号00：00：00<br>
            广州恒大VS沙尔克04    27号00：00：00——30号00：00：00</p>
          <h3>活动规则:</h3>
          <ol>
            <li><span class="c-white">抢票期间累积存款分别达到1288元，即可获得抢门票资格</span>，两场足球活赛事各提供60张门票，天威娱乐提供您进场观看比赛享受狂热氛围。赛事门票分为 广州富力ＶＳ沙尔克04 比赛地点为中国广州市 以及 广州恒大ＶＳ沙尔克04 比赛地点为中国佛山市，共限量120张门票，先抢先得，抢完为止。每一位玩家只可抢得一张门票</li>

            <li>官网10分钟更新一次抢票系统，您可以登陆账号查看，排名提示前60名，如没有在名单内即证明抢票未成功或票已经抢空</li>

            <li>申请限时门票流程:官网左侧点击”限时抢门票”，即可进入抢票页面延迟申请的视为玩家自动放弃申请。</li>

            <li>此项优惠活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</li>

            <li>天威娱乐城拥有本活动的最终解释权以及随时更改活动的权利。</li>
          </ol>
        </div>

      </div>
    </div>
    <div id="tab-activities" class="tab-panel">
      <div class="match-box">
        <div class="match-item box fl">
          <img src="/images/topic/match/fl-logo.png" class="fl" width="109" alt="">
          <img src="/images/topic/match/sek-logo.png" class="fr" width="109" alt="">
          <div>
            <h1>广州富力 VS 沙尔克04</h1>
            <p>比赛时间:2016-07-05 20:00:00</p>
            <p>地点:中国广州市</p>
          </div>
        </div>
        <div class="match-item box fr">
          <img src="/images/topic/match/hd-logo.png" class="fl" width="98" height="116" alt="">
          <img src="/images/topic/match/sek-logo.png" class="fr" width="109" alt="">
          <div>
            <h1>广州恒大 VS 沙尔克04</h1>
            <p>比赛时间:2016-07-07 20:00:00</p>
            <p>地点:中国佛山市</p>
          </div>
        </div>
      </div>

      <div class="m-content box">
        <div class="text-center" style="margin-top: -25px;;">
          <img src="/images/topic/match/field.png" alt="">
          <img src="/images/topic/match/tit-top.png" alt="" style="margin-top: -90px;">
        </div>
        <h3>活动对象:</h3>
        <p>天威娱乐所有会员</p>
        <h3>活动时间:</h3>
        <p>7月4日00:00:00-7月5日12:00</p>
        <h3>活动内容:</h3>
        <p>1.比赛开始前，玩家通过下注竞猜《第一个进球接触部位的帖子》，每位玩家只要下注独赢盘来竞猜本场比赛获胜的一方（注额需≥100元）以及在官网论坛竞猜页面竞猜第一个进球接触部位后，即为成功参与。
          <br>
          例如： <br>
          在天威娱乐体育页面投注·独赢盘·沙尔克04胜 <br>
          广州富力vs沙尔克04第一个进球部位为： <br>
          ①头 <br>
          ②脚 <br>
          ③躯干 <br>
          ④其他部位 <br>
          2.若玩家竞猜预测与结果相同，则可获得38元彩金</p>
        <h3>活动说明:</h3>
        <p class="c-white">1.盘口限定为早盘·全场·独赢； <br>
          2.单笔下注需≥100元 <br>
          3.两场比赛结束后，获得彩金的玩家，可以有一次加奖机会</p>
        <h3>具体如下:</h3>
        <p>1.获得彩金的玩家，我们会根据玩家投注竞猜的时间来进行排序，同时每位玩家会获得一组幸运数字，根据7月8日福彩3D开奖结果来进行派彩,若开奖结果与你的幸运数字相同，则可获得一下奖励</p>
        <table class="table">
          <tr>
            <td>开奖结果与幸运数字完全相同</td>
            <td>所获彩金 x5 倍</td>
          </tr>
          <tr>
            <td>开奖结果与幸运数字完全相同顺序不同</td>
            <td>所获彩金 x4 倍</td>
          </tr>
          <tr>
            <td>开奖结果个位十位与幸运数字相同</td>
            <td>所获彩金 x3 倍</td>
          </tr>
          <tr>
            <td>开奖结果个位十位与幸运数字相同顺序不同</td>
            <td>所获彩金 x2 倍</td>
          </tr>
          <tr>
            <td>开奖结果个位数字与幸运数字相同</td>
            <td>所获彩金 x1 倍</td>
          </tr>
        </table>
        <h3>其他事项:</h3>
        <p class="c-red">备注：每人只可翻倍奖励一次，若获得多次奖励，则会以倍数最高来进行奖励</p>
        <p class="c-red">彩金派发时间：比赛结束后第二个工作日下午18点前中奖玩家的相关彩金会自动派发至您的天威娱乐官网主账号内。</p>
      </div>
    </div>
    <div id="tab-bidbits" class="tab-panel"></div>
    <div id="tab-coverage" class="tab-panel"></div>
  </div>

  <!--抢票活动{-->
  <div class="modal fade" id="j-modal-ticket" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
       style="display: none;">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-hd">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title">抢票活动</h4>
        </div>
        <div class="modal-bd">
          <form action="" method="post" id="j-form-addr">
            <div class="ipt-group">
              <label for="" class="label rq-value">门票：</label>
              <input type="hidden"  id="j-giftid" name="giftID"/>
              <input type="text" name="address" class="ipt-txt" readonly  id="j-road"/>
            </div>
            <div class="ipt-group">
              <label for="" class="label rq-value">收货人姓名：</label>
              <input type="text" class="ipt-txt" name="addressee" required/>
            </div>
            <div class="ipt-group">
              <label for="" class="label rq-value">手机号码：</label>
              <input type="text" class="ipt-txt" name="cellphoneNo" data-rule-phone="true" required maxlength="11"/>
            </div>
            <div class="ipt-group">
              <label for="" class="label">&nbsp;</label>
              <input type="submit" class="btn btn-danger" value="确定"/>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <!--}抢票活动-->


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
          <form action="" method="post" >
            <div class="ipt-group">
              <label for="" class="label rq-value">用户名：</label>
              <input type="text" name="address" class="ipt-txt"  id="j-username" maxlength="20"/>
            </div>
            <div class="ipt-group">
              <label for="" class="label rq-value">密码：</label>
              <input type="password" class="ipt-txt" name="addressee" id="j-pwd"/>
            </div>
            <div class="ipt-group">
              <label for="" class="label rq-value">验证码：</label>
              <input type="text" class="ipt-txt" name="cellphoneNo"  id="j-code" maxlength="8"/>
              <img id="j-codeimg" src="/asp/validateCodeForIndex.aspx" onclick="this.src='/asp/validateCodeForIndex.aspx?'+Math.random();" alt="">
            </div>
            <div class="ipt-group">
              <label for="" class="label">&nbsp;</label>
              <input type="submit" class="btn btn-danger" id="j-login" value="确定"/>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <!--}登录-->
</div>

<input type="hidden" value="2016-08-01 10:00:00" id="j-date">
<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/lib/flipclock.min.js"></script>
<script src="/js/lib/jquery.magnific-popup.min.js"></script>
<script src="/js/plugins/jquery.validate.min.js"></script>
<script src="/js/plugins/jquery.validate.config.js"></script>
<script src="/js/main.js"></script>
<script src="/js/match.js?v=sdfioiopwer2016ewe"></script>

<c:choose>
  <c:when test="${session.customer==null}">
    <script>
      $(function () {
        function open(){
          var $loginOpen=$('#j-modal-login');
          $loginOpen.modal('show');
        }

        $('#j-btn-ticket1').on('click',open);
        $('#j-btn-ticket2').on('click',open);
      });
    </script>
  </c:when>
  <c:otherwise>
    <script>
      $(function () {
        match.isExistGift();
        match.saveData();
      });
    </script>
  </c:otherwise>
</c:choose>

<script>
  $(function(){

    $('#j-menu').find('a').on('click',function () {
      var href=$(this).attr('href')||'';
      var url=href.replace('#','')+'.html?v=qw12ds';
      if(href==='#tab-bidbits'||href==='#tab-coverage'){
        $(href).load(url);
      }
    });

    $('#tab-bidbits').magnificPopup({
      delegate: 'a.col',
      type: 'image',
      tLoading: '加载图片 #%curr%...',
      mainClass: 'mfp-img-mobile',
      gallery: {
        enabled: true,
        navigateByImgClick: true,
        preload: [0,1] // Will preload 0 - before current, and 1 after the current image
      },
      image: {
        tError: '<a href="%url%">图片 #%curr%</a> 加载失败.',
        titleSrc: function(item) {
          return '天威赞助';
        }
      }
    });

  });
</script>

</body>
</html>