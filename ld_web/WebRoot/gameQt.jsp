<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>
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
		<link rel="stylesheet" href="${ctx}/css/game.css?v=12"/>

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

		</div>
		<ul class="game-btn-goup">
			<li><a href="/pt.asp"><i class="icon icon-game-pt"></i>PT老虎机</a></li>
			<li><a href="/asp/ttLogin.aspx"><i class="icon icon-game-ttg"></i>MG/TTG老虎机</a></li>
			<li><a href="/gameNt.jsp"><i class="icon icon-game-nt"></i>NT老虎机</a></li>
			<li class="active"><a href="/gameQt.jsp"><i class="icon icon-game-qt"></i>QT老虎机</a></li>

		</ul>
		<aside class="game-aside">
			<h2>QT老虎机</h2>
			<ul class="tab-hd tab-game-catetory" id="j-game-catetory">
				<li class="active"><a data-toggle="tab" href="#tab-qtGames">全部游戏</a></li>
				<li><a data-toggle="tab" href="#tab-hotGames">热门游戏</a></li>
			</ul>
			<div class="slot-btn-wp cfx">
				<a href="javascript:void(0);" class="btn-game"><img class="img-qr" src="/images/qr/lehu-app.png" alt="" width="120" height="120"/>手机扫一扫进入游戏</a>
			</div>
		</aside>
		<div class="tab-bd gamelist-wp">
			<div id="j-gamelist-tpl"></div>
		</div>
	</div>
	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	<script id="tpl" type="text/x-handlebars-template">

			<li class="game-info">
				<div class="game-pic">
					<img src="${ctx}/images/qtgames/{{pic}}" width="156" height="153" alt=""/>
				</div>
				<div class="name"><h4>{{name}}</h4></div>
				<div class="ename">{{eName}}</div>
				<div class="game-play">
					<a onclick="load_playngo('{{code}}', 0, '{{type}}')"
					   target="_blank" class="btn-game play">进入游戏</a>
					<a onclick="load_playngo('{{code}}', 1, '{{type}}')"
					   target="_blank" class="btn-game demo">试玩游戏</a>
				</div>
			</li>

	</script>
	<script type="text/javascript">
		var gpi_window;
		var loginStatus = <%=loginMode %>;
		function load_playngo(code, isfun, type){
			if (loginStatus) {
				try { gpi_window.close(); } catch (e) { };
				var gameType;
				if(type=='qtGames')gameType='qt';
				else if(type=='hotGames')gameType='hot';

				gpi_window = window.open("/gameQT.aspx?gameCode=" + code + "&isfun=" + isfun + "&type=" + gameType, 'gpi', 'width=1024,height=768,scrollbars=no,location=1,resizable=no');
			}else{alert('请登录后进入游戏');}
		}

		$(function(){
			var html='',
					tplhtml=$('#tpl').html();
			$.getJSON('/js/json/qtgame.json',function(data){
				//模版填充数据
				if(data.success){
					builHtml(data.data);
					$('#j-gamelist-tpl').html(html);
					$('#j-gamelist-tpl .tab-panel:eq(0)').addClass('active in');
				}

			});

			function builHtml(data){
				var typeGame,typeGameList;
				for(var i=0;i<data.length;i++){
					typeGame=data[i];
					typeGameList=data[i].gameList;

					html +='<ul id="tab-{{type}}" class="tab-panel gamelist fade">'.replace(/\{\{type\}\}/g,typeGame.type);
					for(var j=0;j<typeGameList.length;j++){
						var  obj=typeGameList[j];
						html += tplhtml.replace(/\{\{pic\}\}/g,obj.pic)
								.replace(/\{\{name\}\}/g,obj.name)
								.replace(/\{\{code\}\}/g,obj.gid)
								.replace(/\{\{eName\}\}/g,obj.eName)
								.replace(/\{\{type\}\}/g,typeGame.type);
					}
					html +='</ul>';
				}

			}
		});

	</script>

</body>
</html>

