<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<head>
	<script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
		<title></title>
		<style>
* {
	margin: 0;
	padding: 0;
	list-style-type: none;
}

a,img {
	border: 0;
}

.demo {
	margin: 100px auto 0 auto;
	width: 400px;
	text-align: center;
	font-size: 18px;
}

.demo .action {
	color: #3366cc;
	text-decoration: none;
	font-family: "微软雅黑", "宋体";
}

.overlay {
	position: fixed;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	z-index: 99998;
	width: 100%;
	height: 100%;
	_padding: 0 20px 0 0;
	background: #f6f4f5;
	display: none;
}

.showbox {
	position: fixed;
	top: 0;
	left: 50%;
	z-index: 99999;
	opacity: 0;
	filter: alpha(opacity = 0);
	margin-left: -80px;
	margin-top: -60px;
}

* html,* html body {
	background-image: url(about:blank);
	background-attachment: fixed;
}

* html .showbox,* html .overlay {
	position: absolute;
	top: expression(eval(document . documentElement . scrollTop) );
}

#AjaxLoading {
	border: 1px solid #8CBEDA;
	color: #37a;
	font-size: 12px;
	font-weight: bold;
}

#AjaxLoading div.loadingWord {
	width: 180px;
	height: 50px;
	line-height: 50px;
	border: 2px solid #D6E7F2;
	background: #fff;
}

#AjaxLoading img {
	margin: 10px 15px;
	float: left;
	display: inline;
}
</style>
		<script type="text/javascript" src="https://login.happypenguin88.com/jswrapper/integration.js.php?casino=happypenguin88"></script>
		<script type="text/javascript">
 iapiSetCallout('Login', calloutLogin);
 iapiSetCallout('Logout', calloutLogout);
 //登录游戏
 function login(realMode) {
    openProgressBar();
    var loginname="<%=request.getParameter("loginname") %>";
    loginname = loginname.toUpperCase();
    var password="<%=request.getParameter("password") %>";
    //alert(loginname+password);
    if(loginname==null || loginname == "" || password==null || password=="" ){
      alert("登录失效，请重新登录官网！");
      window.close();
      return;
    }
    var gameCode="<%=request.getParameter("ptgamecode") %>";
    if(gameCode==""){
      alert("游戏代码错误！请重新登录");
      window.close();
      return;
    }
    //判断pt是否登录
    iapiLogin(loginname, password, realMode, "zh-cn");
 }
 //登出游戏
 function logout(allSessions, realMode) {  
  	iapiLogout(allSessions, realMode);
 }
 //登录回调
 function calloutLogin(response) {
    closeProgressBar();
    var gameCode="<%=request.getParameter("ptgamecode") %>";
    if(gameCode==""){
      alert("游戏代码错误！请重新登录");
      window.close();
      return;
    }
 	if (response.errorCode !=null  && (response.errorCode ==0 || response.errorCode ==6)) {
       	window.location = "http://cache.download.banner.happypenguin88.com/casinoclient.html?language=zh-cn&game="+gameCode;
  	}else{ 
      	alert("进入游戏失败，错误编码："+response.errorCode+",错误信息："+response.errorText);
	    window.location.href="<%=request.getParameter("ptlobby") %>"+"/slotGame.jsp?showtype=PT";
  	}
 }
 //登出回调
 function calloutLogout(response) {
 	if (response.errorCode) {
 		alert("Logout failed, " + response.errorCode);
 	}else {
 		alert("Logout OK"); 
 	}
 }
 //打开进度条
  function openProgressBar(){
     var h = $(document).height();
     $(".showbox").css({"z-index": "99999" });
	 $(".overlay").css({"height": h });
	 $(".overlay").css({'display':'block','opacity':'0.8'});
	 $(".showbox").stop(true).animate({'margin-top':'300px','opacity':'1'},200);	
  }
  //关闭进度条 
  function closeProgressBar(){
     $(".showbox").css({"z-index": "-99999" });
     $(".showbox").stop(true).animate({'margin-top':'250px','opacity':'0'},400);
	 $(".overlay").css({'display':'none','opacity':'0'});	
  }
</script>
	</head>
	<body onload="login(1)">
		<div class="overlay"></div>
		<div id="AjaxLoading" class="showbox">
			<div class="loadingWord">
				<img src="${ctx}/images/waiting.gif">
				登录中，请稍候...
			</div>
		</div>
	</body>
</html>