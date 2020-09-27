﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
				<%--<div class="tab-block">
					<div class="tab-button active">MG游戏</div>
				</div>--%>
				<div class="game-detail">
					<div class="loader"><div class="loader-block"><div class="loading"></div><div class="text">载入中</div></div></div>
				</div>
			</div>
			<div class="footer-margin"></div>
		</div>

		 <jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			headerBar.setTitle("MG老虎机");
			
		/*	//滑动时 隐藏header
			headerBar.scrollHide(true);
			headerBar.bind('hide',function(){
				$('.main-wrap .page-titles').css('top',0);
			});
			headerBar.bind('show',function(){
				$('.main-wrap .page-titles').css('top',headerBar.defHeight);
			});*/
			
			//取得OS类型
			var mobileOS = getMobileKind();
			//console.log('OS : '+mobileOS);
			queryGames();
			//取得游戏
			function queryGames(){
				var gameTemplateHtml = [
				    '<div class="mui-col-xs32-6 mui-col-xs64-4"><div class="game-box {0}">',
					'<div class="load-circle"></div>',
				   
					'<img src="mobile/images/mggames/{1}" style="height: 186px;overflow: hidden;">',
					 '<div class="name">{2}</div>',
				    '<div class="buttons">',
						  '<a href="javascript:play_real(\'{3}\')" >立即游戏</a>',
						  '<a href="javascript:play_fun(\'{3}\')" id="{3}" >免费试玩</a>',
				    '</div>',
					'</div></div>'].join('');

				var gameArray = new Array();

			    mobileManage.ajax({
			    	url:'mobile/json/game/mgPhone.json?v=2019',
			    	callback:function(result){
						if(result.success){
							var object;
							var type;
							for(var i in result.data){
								object = result.data[i];
								type = object.type;
								for(var j in object.gameList){
									var gameObj = object.gameList[j];
									if(gameObj.state==0) continue;  //隐藏不可以无效状态的
									gameArray.push(String.format(gameTemplateHtml,type,gameObj.pic,gameObj.name,gameObj.id));
								}
							}
							$('.game-detail').prepend( $(gameArray.join('')) );
						  	// use ImagesLoaded
						  	$('.game-detail').imagesLoaded().progress( onProgress );
						  	gameEventInit();
							object=type=null;
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
			
//			正式遊戲
			function play_real(code){
				if('${session.customer==null}'=='true'){
					alert('请登录后进入游戏');
				}else {
					var mgs_session='';
					mobileManage.ajax({
						url:'mobi/gameH5MGS.aspx',
						param:{gameCode:code},
					    callback:function(result){
							if(result.success){
								window.location.href = result.data.url;
							}else{
								alert(result.message);
							}
						}
					});
				}
			}

			function play_fun(code){
				
			    var gameUri = "https://mobile22.gameassists.co.uk/MobileWebServices_40/casino/game/launch/lehucom/"+ code +"/zh-cn?loginType=VanguardSessionToken&isPracticePlay=true&casinoId=2712&isRGI=true&authToken=&lobbyurl=" + window.location.host + "/mobile/mgs.jsp";
			    window.location.href = gameUri;
			}
			
			//初始Title Event
			function gameEventInit(){
				var gameTitles = $('.tab-block .tab-button');
				gameTitles.eq(0).click(function(){
					filterGames.apply(this,['mgsGames']);
				});
				/*gameTitles.eq(1).click(function(){
					filterGames.apply(this,['testGames']);
				});*/
				gameTitles.eq(0).click();
			}
			
			//过滤信息类型
			function filterGames(id){
				if(this instanceof Element){
					$('.page-titles .bar .title').removeClass('active');
					$(this).addClass('active');
				}
				$('.game-box').parent().css('display','none');
				$('.game-box.'+id).parent().css('display','inline-block');
			}
			
		</script>
	</body>
</html>