<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	String username = "";
	String tmpToken = "";
	try{
		username = request.getParameter("username").toUpperCase();
		tmpToken = request.getParameter("temptoken");
		if(null == tmpToken){tmpToken = "";}
	} catch(Exception e){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}
	
%>

<!DOCTYPE>
<html>
<head>
	<jsp:include page="commons/common.jsp" />
	<link rel="stylesheet" type="text/css" href="mobile/css/game/PTGame.css?v=9" />

</head>
<body>
<div class="main-wrap">
	<div class="header-margin"></div>
	<div class="content">
		<div class="space-2"></div>
		<div class="game-detail">
			<div class="loader"><div class="loader-block"><div class="loading"></div><div class="text">载入中</div></div></div>
		</div>
	</div>
	<div class="footer-margin"></div>
</div>

<jsp:include page="commons/footer.jsp" />
<script type="text/javascript">
	headerBar.setTitle('PT老虎机');
	queryGames();
	//取得游戏
	function queryGames(){
		var gameTemplateHtml = [
			'<div class="mui-col-xs32-6 mui-col-xs64-4"><div class="game-box {0}">',
			'<div class="load-circle"></div>',
			'<img src="mobile/images/ptgames/{1}">',
			'<div class="name">{2}</div>',
			'<span class="e-name">{5}</span>',
			'<div class="buttons">',
			'<a style="width: 100%" href="javascript:loadgame(\'{3}\', \'{4}\')">立即游戏</a>',
			/*'<a href="javascript:loadgame(\'{3}\',\'{4}\')">免费试玩</a>',*/
			'</div>',
			'</div></div>'].join('');

		var gameArray = [];

		mobileManage.ajax({
			url:'mobile/json/game/ptPhone.json?v=1',
			callback:function(result){
				if(result.success){
					var obj;
					for(var i=0;i<result.data.length;i++){
						obj = result.data[i];
						gameArray.push(String.format(gameTemplateHtml,obj.type,obj.pic,obj.name,obj.type,obj.id,obj.eName));

					}

					$('.game-detail').prepend( $(gameArray.join('')) );
					// use ImagesLoaded
					$('.game-detail').imagesLoaded().progress( onProgress );

					obj=null;

				}else{

				}
				gameTemplateHtml = gameArray = null;
			}
		});
	}

	// triggered after each item is loaded
	function onProgress( imgLoad, image ) {
		// change class if the image is loaded or broken
		var $item = $( image.img ).parent();
		$item.find('.load-circle').remove();
		if ( image.isLoaded ) {
			$item.addClass('loaded');
		}else{
			$item.addClass('broken');
		}
	}

	// 加载游戏
	function loadgame(type, game){
		if('<%=tmpToken%>' == ""){
			window.location = "http://i5g20f3as-31g4s.com/goldenrose/pth5loading.jsp?username=" + '<%=username %>' + "&type=" + type + "&game=" + game + "&baseUrl=" + '<%=request.getRequestURL().toString().replace(request.getServletPath(), "") %>' ;
		}else{
			launchMobileClient(type, game, '<%=tmpToken%>');
		}
	}

	function launchMobileClient(gametype, game, temptoken) {
		var baseurl = '<%=request.getRequestURL().toString().replace(request.getServletPath(), "") %>';
		if (gametype == "mps") {
			//var clientUrl=''+'?username=' + username + '&temptoken=' + temptoken + '&game=' + currentgame + '&real=1';
		} else if (gametype = "ngm") {
			//var clientUrl = 'http://hub.gf175388.com/igaming/' + '?gameId=' + game + '&real=1' + '&username=' + '<%=username %>' + '&lang=zh-cn&tempToken=' + temptoken + '&lobby=' + baseurl + '/mobile/ptlobby.jsp?' + '&support=' + baseurl + '/mobile/index.jsp' + '&logout=' + baseurl + '/mobile/index.jsp';
			var clientUrl = 'http://hub.ms226388.com/hosting/hosting.html?gameClientUrl=http://m.ms226388.com/casino/games/casinoclient.html&gameId=' + game + '&real=1&gamePlatform=NGM&username=' + '<%=username %>' + '&lang=zh-cn&tempToken=' + temptoken + '&lobbyPage=' + baseurl + '/mobile/ptlobby.jsp' + '&supportPage=' + baseurl + '/mobile/ptlobby.jsp' + '&logoutPage=' + baseurl + '/mobile/index.jsp';
		}
		document.location = clientUrl;
	}

</script>
</body>
</html>