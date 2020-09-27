<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
String infoValue=(String)session.getAttribute("infoValue4Live800");
if(infoValue==null)infoValue="";
%>
<!DOCTYPE>
<html>

<head>
    <title>天威</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <title>天威真人线上娱乐-百家乐、网络百家乐、在线百家乐、博彩、网络博彩</title>

    <link type="text/css" rel="stylesheet" href="style/basic.css">
    <script type="text/javascript" src="js/jquery18.js"></script>
</head>

<body>
<div class="forbidden_header">
    <img src="/images/logo.png?v=2">
</div>
<div class="bgg" >
    <div class="container" style="padding:70px 0 50px 0;  height:450px">
        <div class="pic">
            <div class="picbox"><img src="/images/wh.jpg" class="simg1" /></div>
        </div>
        <div class="weihu_box" style="margin: 10px 0 40px 0;">
            <h2>网站维护中 敬请期待......</h2>
            <p style="color:#bebebe">this website is temporary under maintenance</p>
            <p class="t">维护时间：</p>
            <h3>2018-08-24  06:00:00 到 10:00:00 </h3>
            <p class="t">尊敬的天威会员： </p>
            <p>天威官网已关闭，维护期间天威将与乐虎进行数据合并，暂时无法登陆账号。合并完毕后，请您在您的游戏账号前加ldo和您的账号，即可在乐虎进行登录游戏。如果您已经在乐虎有注册过游戏账号的我们将会关闭天威旧账号开启新账号。非常感谢您长久以来的陪伴，愿您好运常存！天威全体人员再次感谢您对网站的大力支持，乐虎国际期待您的光临</p>
            <p class="mt20">有任何疑问？请通过在线客服，联系全年无休的<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank" class="chat-service">在线客服</a>支持团队，或发送邮件至<a href="javascript:;">lehu88vip＃gmail.com </a>(请将#号替换成@)</p>
            <p class="mt30">我们将竭诚为您服务。</p>
            <p>谢谢。</p>
        </div>
    </div>
</div>

<script type="text/javascript">
    setInterval(function(){
        $(".simg1").stop(true).animate({top:'60px',opacity:'1.0'},2000,function(){
            $(this).animate({top:'40px'},2000);
        });
    },4000);
    //    $(".simg1").stop(true).animate({top:'40px',opacity:'1.0'},4000);
</script>
	</body>

</html>