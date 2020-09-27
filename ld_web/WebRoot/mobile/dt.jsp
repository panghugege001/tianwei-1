<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
 
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/game/QTGame.css?v=2" />		
		<script type="text/javascript" src="mobile/js/UserManage.js"></script>
		<script type="text/javascript" src="../js/lib/countUp.js"></script>
	</head>
	<body> 
		 <div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">
				<div class="tab-block">
					<div data-tag="" class="tab-button mui-col-xs32-6 active">DT游戏</div>
					<div data-tag="DEM" class="tab-button mui-col-xs32-6">试玩游戏</div>
				</div>
				<div class="dt_gamebox">
					<div class="dt_jc">
						<div class="jackpot-div" id="j-jackpotCount"></div>
					</div>
				</div>
				<div class="game-detail">
					<div class="loader"><div class="loader-block"><div class="loading"></div><div class="text">载入中</div></div></div>
				</div>
			</div>
			<div class="footer-margin"></div>
		</div>
		 <jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			headerBar.setTitle("DT老虎机");
			
// 			//滑动时 隐藏header
// 			headerBar.scrollHide(true);
// 			headerBar.bind('hide',function(){
// 				$('.main-wrap .page-titles').css('top',0);
// 			});
// 			headerBar.bind('show',function(){
// 				$('.main-wrap .page-titles').css('top',headerBar.defHeight);
// 			});
			
			//取得OS类型
			var mobileOS = getMobileKind();
            var downloadUrl = getMobileKind()=='Android'?'http://down.dreamtech.asia/LEHU/android.html':'http://down.dreamtech.asia/LEHU/ios.html';

            //console.log('OS : '+mobileOS);
			queryGames();
			//取得游戏
			function queryGames(){
				var gameTemplateHtml = [
				    '<div data-tag="{6}" class="game-item mui-col-xs32-6 mui-col-xs64-4"><div class="game-box {0}">',
					'<div class="load-circle"></div>',
				    
					'<img src="/images/dtgames/{1}">',
					'<div class="name">{2}</div>',
				    '<div class="buttons">',
						  '<a href="javascript:play_game(\'{3}\',\'{4}\',\'{5}\', \'{7}\' )" >立即游戏</a>',
						  '<a href="javascript:play_guest_game(\'{3}\',\'{4}\',\'{5}\', \'{7}\' )" >免费试玩</a>',
				    '</div>',
					'</div></div>'].join('');

				var gameArray = new Array();

			    mobileManage.ajax({
			    	url:'mobile/json/game/dtPhone.json?v=1237',
			    	callback:function(result){
						if(result.success){
                            var tmpData=result.data;
                            for (var i = 0; i < tmpData.length; i++) {
                                var gameObj = tmpData[i];
                                gameArray.push(String.format(gameTemplateHtml,
                                        '',gameObj.pic,gameObj.name,gameObj.id,gameObj.subType,gameObj.language,'|'+gameObj.tag.join('|'),gameObj.state));

                            }
							$('.game-detail').prepend( $(gameArray.join('')) );
						  	// use ImagesLoaded
						  	$('.game-detail').imagesLoaded().progress( onProgress );
						  	gameEventInit();
							object=type=null;
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
			function play_game(gameCode,type,language,isfun){
                if(isfun=='DEM') { // 暂时可以试玩
                    alert('正式游戏,敬请期待');
                    return false;
                }
                
                window.location.href="/game/gameLoginDT.php?isfun=0&gameCode="+gameCode+"&language=zh_CN&clientType=1";
                
				//if(type=='DT-2'){
// 					var url = '${session.gameurl}';
// 					var key = '${session.slotKey}';
// 					var referWebsite = '${session.referWebsite}';
// 					var gameUrl = '/dtGames.aspx?slotKey={0}&language={1}&gameCode={2}&isfun=0&closeUrl={3}';
// 					if(key){
//                         window.location.href = url+String.format(gameUrl,key,language,gameCode,referWebsite);
//                         return;
// 					}
					
// 				    mobileManage.ajax({
// 				    	url:'mobi/loginDT.aspx',
// 				    	callback:function(result){
// 							if(result.success){
// 								//window.open(result.data.url+String.format(gameUrl,result.data.key,language,gameCode,result.data.referWebsite));
// 								window.location.href = result.data.url + String.format(gameUrl,result.data.key,language,gameCode,result.data.referWebsite);
// 							}else{
// 								alert(result.message);
// 							}
// 						}
// 				    });
				/*}*//*else{
                    mobileManage.getModel().open('download',[{
                        title:'DT老虎机',
                        content:[
                            '您当前的设备不支持游戏，请下载DT老虎机客户端立即体验！'
                        ].join('<br>'),
                        url:downloadUrl
                    }]);
				} */
			}
//          試玩遊戲			
			function play_guest_game(gameCode,type,isfun){
                if(type=='DT-2'){
                    //window.open('http://www.dreamtech8.net/publishr/guestgame.php?gameCode=' + gameCode + '&isfun=1&type=dt');
                    window.location.href = 'http://play.dreamtech8.com/playSlot.aspx?gameCode=' + gameCode + '&isfun=1&type=dt';
                    return false;
                }else if(type=='DT-1'){
                    mobileManage.getModel().open('download',[{
                        title:'DT老虎机',
                        content:[
                            '您当前的设备不支持游戏，请下载DT老虎机客户端立即体验！'
                        ].join('<br>'),
                        url:downloadUrl
                    }]);
                }
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
		
<script>
	
$(function(){
	
			setJackPot();
			function setJackPot(){

				$.post("/mobi/dtJackpot.aspx ", function (response) {
					console.log(response.pot);
					if(CountUp){
						var demo = new CountUp("j-jackpotCount", response.pot, 9194572, 2, 3000000000, {
							useEasing : true,
							useGrouping : true,
							separator : ',',
							decimal : '.',
							formate : true,
							suffix : ''
						});
						demo.start();
					}

				});
			}		
})
</script>
	</body>
</html>`