<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
 
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/game/QTGame.css" />		
		<script type="text/javascript" src="mobile/js/UserManage.js"></script>
	</head>
	<body> 
		 <div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">
				<div class="tab-block">
					<div data-tag="" class="tab-button mui-col-xs32-6 active">TTG游戏</div>
					<div data-tag="NEW" class="tab-button mui-col-xs32-6">最新游戏</div>
				</div>
				<div class="game-detail">
					<div class="loader"><div class="loader-block"><div class="loading"></div><div class="text">载入中</div></div></div>
				</div>
			</div>
			<div class="footer-margin"></div>
		</div>
		 <jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			headerBar.setTitle("TTG老虎机"); 
			
// 			//滑动时 隐藏header
// 			headerBar.scrollHide(true);
// 			headerBar.bind('hide',function(){
// 				$('.main-wrap .page-titles').css('top',0);
// 			});
// 			headerBar.bind('show',function(){
// 				$('.main-wrap .page-titles').css('top',headerBar.defHeight);
// 			});
			

            //console.log('OS : '+mobileOS);
			queryGames();
			//取得游戏
			function queryGames(){
				var gameTemplateHtml = [
				    '<div data-tag="{{tag}}" class="game-item mui-col-xs32-6 mui-col-xs64-4"><div class="game-box">',
					'<div class="load-circle"></div>',
					'<img src="https://dn-slotqiniu.qbox.me/slot/images/ttggames/{{pic}}">',  
					'<div class="name">{{name}}</div>',
				    '<div class="buttons">',
						  '<a href="javascript:play_game(\'{{id}}\',\'{{code}}\')" >立即游戏</a>',
						  '<a href="javascript:play_guest_game(\'{{id}}\',\'{{code}}\')" >免费试玩</a>',
				    '</div>',
					'</div></div>'].join('');

				var gameArray = new Array();

			    mobileManage.ajax({
			    	url:'mobile/json/game/ttgPhone.json?v=0103',
			    	callback:function(result){
						if(result.success){
                            var tmpData=result.data.gameList;
                            for (var i = 0; i < tmpData.length; i++) {
                                var gameObj = tmpData[i];
                                gameArray.push( gameTemplateHtml.replace(/\{\{name\}\}/g,gameObj.name)
                                    .replace(/\{\{code\}\}/g,gameObj.code)
                                    .replace(/\{\{id\}\}/g,gameObj.id)
                                    .replace(/\{\{tag\}\}/g,'|'+gameObj.tag.join('|'))
                                    .replace(/\{\{pic\}\}/g,gameObj.pic));

                            }
							$('.game-detail').prepend( $(gameArray.join('')) );
						  	// use ImagesLoaded
						  	$('.game-detail').imagesLoaded().progress( onProgress );
						  	gameEventInit();
						}else{
							
						}
						$('.main-wrap>.load-circle').remove();
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
			function play_game(id,code){
				if('${session.customer==null}'=='true'){
                    alert('请登录后进入游戏');
                }else {
                     var gameUri = "asp/ttLogin.aspx?gameName={{code}}&gameType=0&gameId={{id}}&lang=zh-cn&devicetype=mobile";
                gameUri = gameUri.replace(/\{\{id\}\}/g,id)
                    .replace(/\{\{code\}\}/g,code);
                window.location.href = gameUri;
                }
				
				
				
				
				
               
			}
//          試玩遊戲 
			function play_guest_game(id,code){
                var gameUri = "http://pff.ttms.co/casino/generic/game/game.html?gameSuite=flash&gameName={{code}}&lang=zh-cn&playerHandle=999999&gameId={{id}}&deviceType=mobile&account=FunAcct";
                gameUri = gameUri.replace(/\{\{id\}\}/g,id)
                    .replace(/\{\{code\}\}/g,code);
                window.location.href = gameUri;
			}

			
			//初始Title Event
			function gameEventInit(){
				var gameTitles = $('.tab-block .tab-button');
                gameTitles.on('click',function(){
                    var $this=$(this),
                            filter=$this.data('tag');
                    $this.addClass('active').siblings().removeClass('active');
                    filterGames(filter);
                });
				gameTitles.eq(0).click();
			}
			
			//过滤信息类型
			function filterGames(type){
                if(type){
                    $('.game-item').hide();
                    $('.game-item[data-tag*="'+type+'"]').css('display','inline-block');
                }else{
                    $('.game-item').css('display','inline-block');
                }
			}
		</script>
	</body>
</html>`