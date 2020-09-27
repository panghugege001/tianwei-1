<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
 
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/game/QTGame.css?v=9" />
		
	</head>
	<body>
		 <div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">
				<div class="tab-block">
					<div class="tab-button mui-col-xs32-6 active">QT游戏</div>
					<div class="tab-button mui-col-xs32-6">热门游戏</div> 
				</div>
				<div class="game-detail">
		           <div class="loader"><div class="loader-block"><div class="loading"></div><div class="text">载入中</div></div></div>
				</div>
			</div>
			<div class="footer-margin"></div>
		</div>

		 <jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			headerBar.setTitle('QT老虎机');
			
			//取得OS类型
			var mobileOS = getMobileKind();
			//console.log('OS : '+mobileOS);
			queryGames();
			//取得游戏
			function queryGames(){
				var gameTemplateHtml = [
				    '<div class="mui-col-xs32-6 mui-col-xs64-4"><div class="game-box {0}">',
					'<div class="load-circle"></div>',
				   
					'<img src="mobile/images/qtgames/{1}">',
					 '<div class="name">{2}</div>',
				    '<div class="buttons">',
						  '<a href="javascript:load_playngo(\'{3}\', 0, \'{4}\')">立即游戏</a>',
						  '<a href="javascript:load_playngo(\'{3}\', 1, \'{4}\')">免费试玩</a>',
				    '</div>',
					'</div></div>'].join('');

				var gameArray = new Array();

			    mobileManage.ajax({
			    	url:'mobile/json/game/qtPhone.json?v=1113',
			    	callback:function(result){
						if(result.success){
							var object;
							var type;
							var gids;
							var gid;
							
							for(var i in result.data){
								object = result.data[i];
								type = object.type;
								for(var j in object.gameList){
									
									if(object.gameList[j].pic&&'test.jpg'!=object.gameList[j].pic){
										gids = object.gameList[j].gid.split('|');
										if(gids.length>1){
											//根据OS 给不同的GID
											if('IOS'==mobileOS){
												gid = gids[0];
											}else if('Windows Phone'==mobileOS){
												gid = gids[2];
											}else{
												gid = gids[1];
											}
											gameArray.push(String.format(gameTemplateHtml,type,object.gameList[j].pic,object.gameList[j].name,gid,type));
										}else{
											gameArray.push(String.format(gameTemplateHtml,type,object.gameList[j].pic,object.gameList[j].name,object.gameList[j].gid,type));
										}
									}else{
										console.log(object.gameList[j].name);
									}
								}
							}
							$('.game-detail').prepend( $(gameArray.join('')) );
						  	// use ImagesLoaded
						  	$('.game-detail').imagesLoaded().progress( onProgress );
							gameEventInit();
							
							object=type=gids=gid=null;
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
			
			//开启游戏链结
			var gpi_window;
			function load_playngo(code, isfun, type){
				if(gpi_window){
					gpi_window.close();
				}
			    var gameType;
			    if(type=='qtGames')gameType='qtGames';
				else if(type=='hotGames')gameType='hotGames';
			    
			    mobileManage.ajax({
			    	url:'mobi/getQTGame.aspx',
			    	param:{gameCode:code,isfun:isfun,gameType:gameType,origin:window.location.href},
			    	callback:function(result){
						if(result.success){
							window.location.href = result.data.url;
							   
						}else{
							alert(result.message);
						}
					}
			    });
			}
			
			//初始Title Event
			function gameEventInit(){
				var gameTitles = $('.tab-block .tab-button');
				gameTitles.eq(0).click(function(){
					filterGames.apply(this,['qtGames']);
				});
				gameTitles.eq(1).click(function(){
					filterGames.apply(this,['hotGames']);
				});
				gameTitles.eq(0).click();
			}
			
			//过滤信息类型
			function filterGames(id){
				if(this instanceof Element){
					$('.tab-block .tab-button').removeClass('active');
					$(this).addClass('active');
				}
				$('.game-box').parent().css('display','none');
				$('.game-box.'+id).parent().css('display','inline-block');
			}
		</script>
	</body>
</html>