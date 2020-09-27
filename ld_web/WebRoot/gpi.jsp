<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="dfh.action.vo.AnnouncementVO"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<% 	Boolean loginMode = false;
    if(null!=request.getSession(true).getAttribute("customer")){
    	loginMode = true;
    }
%>
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
	<img src="${ctx}/images/slot-banner.jpg?v=1" alt="" style="width: 100%;"/>
</div>
<div class="game-wp wp cfx">

	<div class="game-top cfx">
		<div class="winner-msg-outer" id="j-marquee-winner-list">
			<ul class="winner-msg cfx" >
				<jsp:include page="/tpl/winnerList.jsp"></jsp:include>
			</ul>
		</div>
		<div class="winner" style="display: none;">
			<div id="j-count-num" class="count-num"></div>
		</div>
	</div>
	<ul class="game-btn-goup">
		<li><a href="/pt.asp"><i class="icon icon-game-pt"></i>PT老虎机</a></li>
		<li><a href="/asp/ttLogin.aspx"><i class="icon icon-game-ttg"></i>MG/TTG老虎机</a></li>
		<li class="active"><a href="/gpi.asp"><i class="icon icon-game-gpi"></i>GPI老虎机</a></li>
	</ul>
	<aside class="game-aside">
		<h2>GPI老虎机</h2>
		<ul class="tab-hd tab-game-catetory">
			<li class="active"><a data-toggle="tab" href="#tab_pngGames">经典游戏</a></li>
            <li><a data-toggle="tab" href="#tab_3DGames">3D游戏</a></li>
            <li><a data-toggle="tab" href="#tab_bsGames">特色游戏</a></li>
            <li><a data-toggle="tab" href="#tab_ctxmGames">热门游戏</a></li>
            <li><a data-toggle="tab" href="#tab_rslotGames">最新游戏</a></li>
            <li><a data-toggle="tab" href="#tab_history">游戏记录</a></li>
		</ul>
		<div class="slot-btn-wp" style="display: none;">
			<a href="#" class="btn-game small"><i class="iconfont icon-download-2"></i> 下载客户端</a>
			<a href="#" class="btn-game small"><i class="iconfont icon-user"></i> 免费注册</a>
		</div>
	</aside>
	<div class="tab-bd gamelist-wp">
		<div style="display: none;" class="recommend-game">
			<h2>推荐游戏 <a href="javascript:;" class="fr"><i class="iconfont icon-refresh"></i>换一批</a></h2>
			<ul class="gamelist">
				<li class="game-info">
					<div class="game-pic">
						<img src="${ctx}/images/game-pic.jpg" width="176" height="132" alt=""/>
					</div>
					<h4>蜘蛛侠</h4>
					<h4>经典老虎机</h4>
					<div class="game-play">
						<a href="#" class="btn-game play">进入游戏</a>
						<a href="#" class="btn-game demo">免费试玩</a>
					</div>
				</li>
				<li class="game-info">
					<div class="game-pic">
						<img src="${ctx}/images/game-pic-2.jpg" width="176" height="132" alt=""/>
					</div>
					<h4>蜘蛛侠</h4>
					<h4>经典老虎机</h4>
					<div class="game-play">
						<a href="#" class="btn-game play">进入游戏</a>
						<a href="#" class="btn-game demo">免费试玩</a>
					</div>
				</li>
				<li class="game-info">
					<div class="game-pic">
						<img src="${ctx}/images/game-pic.jpg" width="176" height="132" alt=""/>
					</div>
					<h4>蜘蛛侠</h4>
					<h4>经典老虎机</h4>
					<div class="game-play">
						<a href="#" class="btn-game play">进入游戏</a>
						<a href="#" class="btn-game demo">免费试玩</a>
					</div>
				</li>
				<li class="game-info">
					<div class="game-pic">
						<img src="${ctx}/images/game-pic-2.jpg" width="176" height="132" alt=""/>
					</div>
					<h4>蜘蛛侠</h4>
					<h4>经典老虎机</h4>
					<div class="game-play">
						<a href="#" class="btn-game play">进入游戏</a>
						<a href="#" class="btn-game demo">免费试玩</a>
					</div>
				</li>
			</ul>
			<img src="${ctx}/images/pt-ad.jpg" alt="" class="game-ad" width="203" height="282"/>
		</div>
		<div id="j-gamelist-tpl" ></div>
		<script id="tpl" type="text/x-handlebars-template">
			<li class="game-info">
				<div class="game-pic">
					<img src="${ctx}/images/gpigames/{{pic}}" width="156" height="180" alt=""/>
				</div>
				<div class="name"><h4>{{name}}</h4></div>
				<div class="ename">{{eName}}</div>
				<div class="game-play">
					<a href="/gameGPI.aspx?gameCode={{gid}}&isfun=0" target="_blank" class="btn-game play">进入游戏</a>
					<a href="/gameGPI.aspx?gameCode={{gid}}&isfun=1" target="_blank" class="btn-game play">免费试玩</a>

				</div>
			</li>
		</script>
        <script id="gpipopup" type="text/x-handlebars-template">
    	   <li class="game-info">
				<div class="game-pic">
					<img src="${ctx}/images/gpigames/{{pic}}" width="156" height="180" alt=""/>
				</div>
			   <div class="name"><h4>{{name}}</h4></div>
			   <div class="ename">{{eName}}</div>
				<div class="game-play">
				   <a href="javascript:load_playngo('{{id}}', 0, '{{type}}')" class="btn-game play">立即游戏</a>
				   <a href="javascript:load_playngo('{{id}}', 1, '{{type}}')" class="btn-game play">免费试玩</a>
                </div>
			</li>
	    </script>
	</div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script src="${ctx}/js/gpi.js?v=2"></script>
<script type="text/javascript">
	var gpi_window;
	var loginStatus = <%=loginMode %>;
	function load_playngo(code, isfun, type){
		if (loginStatus) {
		    try { gpi_window.close(); } catch (e) { };
		    var gameType;
		    if(type=='pngGames')gameType='png';
		    else if(type=='bsGames')gameType='bs';
		    else if(type=='ctxmGames')gameType='ctxm';
			gpi_window = window.open("/gamePNG.aspx?gameCode=" + code + "&isfun=" + isfun + "&type=" + gameType, 'gpi', 'width=1024,height=768,scrollbars=no,location=1,resizable=no');
		}else{alert('请登录后进入游戏');}
	}
</script>
</body>
</html>

