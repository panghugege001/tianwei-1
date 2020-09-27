<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<!DOCTYPE >
<html>
<head>
	<script type="text/javascript">
		if(window.location.protocol.indexOf('https')!=-1){
			window.location.href = 'http://lehu80.com/mobile/live.jsp';
		}
	</script>
	<jsp:include page="commons/common.jsp" />
	<meta name="keywords" content="妮妮,尤里,亭瑋,美女,龙8,直播,美女直播,主播,龙8娱乐城,龙8博彩,龙8百家乐,老虎机" />
	
	<link rel="stylesheet" type="text/css" href="mobile/css/live.css?v=1" />
	
</head>
<body>
	<div class="main-wrap">
		<div class="content">
			<div class="header-margin"></div>
		</div>
	</div>
	<jsp:include page="commons/footer.jsp" />
	
	<script type="text/javascript">
		headerBar.setTitle('诱惑女主播');
		headerBar.$el.find('#comm-logout-button').remove();
		headerBar.$el.find('#comm-register-button').remove();
		
		headerBar.$el.append('<div class="left-button" onclick="mobileManage.redirect(\'index\')">返回</div>');
		
		footerBar.hide();
		mobileManage.getLoader().open('加载中');
		
		
		$(window).load(function(){
// 			if('${session.customer==null}'=='true'){
// 				noVideo();
// 				mobileManage.getLoader().close();
// 			}else{
				//开放时间
				var openTimes = [
// 					{min:new Date('2016/09/12 18:00:00').getTime(),max:new Date('2016/09/12 22:00:00').getTime()},
// 					{min:new Date('2016/09/13 18:00:00').getTime(),max:new Date('2016/09/13 22:00:00').getTime()},
					{min:new Date('2016/09/24 19:00:00').getTime(),max:new Date('2016/09/24 23:00:00').getTime()}
				];
				var now = new Date().getTime();
				var open = false;
				for(var i in openTimes){
					if(now>=openTimes[i].min&&now<=openTimes[i].max){
						open = true;
						break;
					}
				}
				
				if(open){
					loadVideo();
				}else{
					noVideo();
					mobileManage.getLoader().close();
				}
// 			}
		});
		
		var tempHtml = [
			'<div class="mui-col-xs32-6">',
			'	<img src="mobile/images/live/{0}" style="width: 100%;" />',
			'</div>'
		].join('');
		

		/**
		 * 加载主播内容
		 */
		function noVideo(){
			var _html = [
			    '<div class="no-video">',
				'	<img src="/mobile/images/live/bg.jpg" width="100%">',
				'	<div class="text-box">',
				'		<div class="text1">天威宝贝秀</div><div class="text1">20:00~22:00</div><br>',
				'		<div class="text2">打个大SH❤W</div><br>',
				'		<div class="text2">直播约起来</div><br>',
				'		<div class="text1">相约9月24日</div><br>',
				'		',
				'	</div>',
				'</div>'
			].join('');
			
			var _tempHtml = [
    			'<div class="av-card">',
    			'	<div class="image"><img src="mobile/images/live/{0}"/></div>',
    			'	<div class="text">',
				'		<div class="name">{1}</div>',
				'		<div class="height">{2}</div>',
				'		<div class="weight">{3}</div>',
				'		<div class="three">{4}</div>',
    			'	</div>',
    			'</div>'
    		].join('');
			
			var _imageData = [
				{name:'妮妮',height:'166公分',weight:'46公斤',three:'32C/23/34',image:'g-3.jpg',self:'大家好~我是妮妮，平常担任Dancer、车模的工作，还有内衣走秀，很多人都说我胸部很漂亮喔！ （羞）个性认真活泼，好相处，喜欢直播分享我的生活，当然也不吝啬给粉丝们福利喔～姣好的身材和性感阳光兼具是我的招牌，希望你会喜欢^^'},
				{name:'尤里',height:'166公分',weight:'45公斤',three:'32E/24/32',image:'g-2.jpg',self:'我是尤里，很高興與大家見面喔～直播經驗非常豐富，許多大陸平台都看的到我！參加過很多展場的演出，也有參加火辣性感的成人展喔！很多人說我像蕭亞軒，你覺得像嗎？記得常常關注我來看我直播喔～保證超多福利大放送，香豔刺激火辣辣，不讓你睡！'},
			];

			$('.main-wrap .content').append(_html);
			var _$noVideo = $('.no-video');
			var _$image;
			var _$liveShow = false;
			_imageShow(0);
			
			for(var i in _imageData){
				_$image = $(String.format(_tempHtml,_imageData[i].image,_imageData[i].name,'身高： '+_imageData[i].height,'体重： '+_imageData[i].weight,'三围： '+_imageData[i].three));
				_$image.bind('click',{imageData:_imageData[i]},_imageClick);
				_$noVideo.append(_$image);
				_imageShow(parseInt(i)+1);
			}

			/**
			 * 图片延迟显示
			 */
			function _imageShow(i){
				setTimeout(function(){
					_$noVideo.find('img').eq(i).addClass('show');
				},i*300);
			}
			
			/**
			 * 图片延迟显示
			 */
			function _imageClick(e){
				var data = e.data.imageData;
				if(!data)return;
				
				if(!_$liveShow){
					_createLiveShow()
				}
				_$liveShow.find('.live-show-image img').attr('src','mobile/images/live/'+data.image);
				_$liveShow.find('.live-show-header').html('<strong>性感美女主播 '+data.name+'</strong>');
				_$liveShow.find('.live-show-self').html(data.self);
				
				setTimeout(function(){
					_$liveShow.addClass('show');
				},100);
			}
			
			function _createLiveShow(){
				var html  = [
					'<div class="live-show">',
					'	<div class="live-show-screen"></div>',
					'	<div class="live-show-block">',
					'		<div class="live-show-header"></div>',
					'		<div class="live-show-content">',
					'			<div class="live-show-image mui-col-xs32-6"><img width="100%"/></div>',
					'			<div class="mui-col-xs32-6">',
					'				<div ><strong>自我介绍</strong></div>',
					'				<div class="live-show-self"></div>',
					'			</div>',
					'		</div>',
					'		<div class="live-show-footer"></div>',
					'	</div>',
					'</div>'
				].join('');
				_$liveShow = $(html);
				$('body').append(_$liveShow);
				_$liveShow.find('.live-show-screen').click(function(){
				
					_$liveShow.removeClass('show');
				});
			}
		}
		
		/**
		 * 加载聊天
		 */
		function loadVideo(){
			var _html = [
				'<div class="video">',
				'	<video controls src="http://wowzaprodhd95-lh.akamaihd.net/i/bf193383_1@156208/master.m3u8" ></video>',
// 				'	<video controls webkit-playsinline src="/mobile/test.mp4" ></video>',
				'</div>',
				'<div class="chat-box">',
				'	<div id="rt-35673c28b877f628ef55d75257e2d285"></div>',
				'	<script type="text/javascript" src="https://www.rumbletalk.com/client/?HXxCp-3-" />',
				'</div>'
			].join('');
			
			$('.main-wrap .content').append(_html);

			var _$video = $('.video');
			var _$chatBox = $('.chat-box');

			$(window).resize(function(){
				delayAction('window_resize',300,_changeVideoWidth);
			});
			
			_$video.bind('DOMNodeInserted ', function(){
				delayAction('change_videoHeight',1000,_changeVideoWidth);
			});
			_$chatBox.bind('DOMNodeInserted ', function(){
				delayAction('change_videoHeight',1000,_changeVideoWidth);
			});


			/**
			 * 调整视窗大小
			 */
			function _changeVideoWidth(){
				var _windowWidth = $(window).width();
				_$video.find('video').attr('width',_windowWidth);
				_$chatBox.width(_windowWidth);
				_$chatBox.find('iframe').width(_windowWidth);
				setTimeout(_changeVideoHeight,100);
			}
			
			/**
			 * 调整视窗大小
			 */
			function _changeVideoHeight(){
				var _headerHeight = $('.header-margin').height();
				var _videoHeight =  _$video.find('video').height();
				var _windowHeight = $(window).height();
				var _boxHeight = _windowHeight - _videoHeight-_headerHeight;
				_boxHeight = _boxHeight>300?_boxHeight:300;
				_boxHeight+=50;
				
				if(_videoHeight==0){
					return;
				}
				if(_$chatBox.find('iframe').length==0){
					_$video.remove();
					_$chatBox.remove();
					noVideo();
				}else{
					_$video.height(_videoHeight);
					_$video.addClass('show');
					_$chatBox.addClass('show');
					setTimeout(function(){
						_$chatBox.height(_boxHeight);
						_$chatBox.find('iframe').height(_boxHeight);
						_$chatBox.addClass('show');
					},400);
				}
				
				mobileManage.getLoader().close();
			}
		}
		
		
		
	</script>
</body>
</html>