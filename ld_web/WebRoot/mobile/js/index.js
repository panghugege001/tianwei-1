$(function () {
			var htmlobj=$.ajax({url:"/asp/checkAgentUrl.aspx",async:false});
			var checkAgentUrl=htmlobj.responseText;
// 			if('${session.customer==null}'=='false'){
				//进入第一个画面
				var count = mobileManage.getSessionStorage('index')['firstPageCount']||0;
				if(count==0){
					mobileManage.setSessionStorage('index',{firstPageCount:1});
					firstPageCount();
				}
// 			}
//queryAgentCode();
				//查询公告
				queryNews();
				createPages();
				new Swiper('.swiper-container',{
					speed: 500,
					loop:true,
					autoplay:5000,
					grabCursor: true,
					paginationClickable: false,
			        autoplayDisableOnInteraction: false,  
					pagination: '.swiper-pagination'
				});
			var newsData = false;
			var newsMarquee = false;
			var count = mobileManage.getSessionStorage('index')['count']||0;
			if(count==0){
				$('.teach-screen').css('display','block');
				$('body').addClass('loader-hidden'); 
				//关闭教学
				$('.teach-screen').find('.button').click(function(){
					$('.teach-screen').remove();
					$('body').removeClass('loader-hidden');
				});
				mobileManage.setSessionStorage('index',{'count':++count});
			}else{
				$('.teach-screen').remove();
			}
			//进入第一个画面
			function firstPageCount(){
				if(webapp.isWebApp())return;
				var html = [
					'<div class="first-page">',
					'	<div class="buttons mui-col-xs32-12 mui-col-xs48-6">',
					'		<div class="space-3"></div>',
					'		<img class="title" src="mobile/images/firstpage/logo-title.png?v=2017">', 
					'	</div>',
					'	<div class="hand mui-col-xs32-12 mui-col-xs48-6">',
					'		<div class="space-3"></div>',
					'		<img class="download" src="mobile/images/firstpage/button{0}.png"/>',
					'		<div class="space-2"></div>',
					'		<img class="enter" src="mobile/images/firstpage/button2.png"/>',
					'	</div>',
					'	<div class="footer">',
					'   	<div class="text-2">',
					'		天威执照由由菲律宾娱乐及博彩公司（PAGCOR）所核发和监管',
					'		</div>',
					'	</div>',
					'</div>'
				].join('');
				$('body').append(String.format(html,getMobileKind()=='Android'?1:1));
				var $first = $('.first-page');
				
				$first.find('.download').click(function(){
                    var isEmpty = 0;
				  var version = getMobileKind().toUpperCase();
				        // step 1
				       if (version == 'OTHER') {
				            alert('请用安卓或者苹果设备打开！！');
				            return false;
				        }
				        // step 2 判断代理域名
				        $.getJSON('/asp/checkAgentURLogin.aspx', function (data) {
				            if (false == data) {
				                // step 3 获取代理包
				                $.getJSON('/app/getAppVersionCustomInfo.aspx', function (data) {
				                    if (data.length > 0) {
				                        // 代理域名，有代理包
				                        $.each(data, function (i, obj) {
				                            //根据移动平台获取下载链接
				                            if (obj.plat.toUpperCase() === version) {
                                                if (obj.packageUrl != "") {
                                                    isEmpty = 0;
                                                    window.location.href = obj.packageUrl;
                                                    return false;
                                                }else {
                                                    isEmpty = 1;
                                                }
                                            } else {
                                                isEmpty = 1;
                                            }
				                        });

                                        if (isEmpty == 1) {
                                            alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
                                            return false;
                                        }
				                    } else {
				                        // step 4 代理域名，无代理包
				                        if ('true' == 'true') {
				                            alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
				                            return false;
				                        } else {
				                             webapp.redirect(window.location.origin+'/app.html?v=2');
				                        }
				                    }
				                });
				            } else {
				                 webapp.redirect(window.location.origin+'/app.html?v=2');
				            }
				        });
				});
				$first.find('.enter').click(function(){
					$first.remove();
//					 window.location.reload();
				});
				
				setTimeout(function(){
					$first.css({opacity:1});
				},50);
				
				setTimeout(function(){
					$first.find('.hand').css({bottom:$first.find('.footer').height()+50});
				},250);
			}
			//出现系统信息 
			var count = mobileManage.getSessionStorage('index')['SystemMessageCount']||0;
 			if(count==0){
				mobileManage.setSessionStorage('index',{SystemMessageCount:1});

			} 
			$("#pro-more").click(function(){
				$(".selfGet-boxs").css("overflow","visible");
			})
			$(".setTabGroup").each(function(i,domEle){
				var _menu = $(domEle).find(".setTabMenu");
				var _con = $(domEle).find(".setTabCon");
				_menu.each(function(j,domEle2){
						$(domEle2).click(function(){
								_menu.removeClass("active");
								$(this).addClass("active");
								_con.hide().eq(j).show();
						}); 
					});
			});	
    
    querySystemMessage();
});


//首页公告
function queryNews(){
  var $newContainer=$('#index-news-marquee');
	var htmlArr=[];
	$.getJSON('/mobi/getAllNews.aspx',function(result){
		if(result.success){
			for (var i = 0; i < result.data.length; i++) {
				var obj = result.data[i];
				htmlArr.push('<span class="text">[公告]'+obj.title+'</span>')
			}
			$newContainer.find('.marquee-content').html(htmlArr.join(''));
			var newsMarquee = new CSSMarquee({id:'index-news-marquee'});
			newsMarquee.start();
		}else{
			$newContainer.find('.marquee-content').html(result.message);
		}
	});
}
	
function createPages(){
				var that=this;
				this.tpl=[
					'<div class="list promotion-info" data-type="{{type}}">',
					'<a href="{{link}}" data-url="{{url}}"><div class="text">{{title}}</div></a>',
					'</div>'
				].join('');

				this.$container=$('#j-youhui');
				 this.$navBtn=$('#j-prom-nav').find('a');
				  this.$pageIndex=$('#page-index');
				this.$pageDetail=$('#page-detail');

				this.getData=function () {
					return $.getJSON('/data/promotion/promotion.json?v=2030');
				};
				this.buildHtml=function(data){
					var htmlArr=[];
					for (var i = 0; i < data.length; i++) {
						var obj=data[i];
						htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
							if(obj&&$1==='link'){
								return obj['link']||'javascript:;';
							}
							return obj&&obj[$1]||'';
						}));
					}
					return htmlArr.join('');
				};

				this.eventHandle=function () {
					 

					$(document).on('click','.promotion-info a',function () {
						var url= $(this).data('url');
						 
						that.$pageIndex.removeClass('active');
						that.$pageDetail.addClass('active').find('.j-content').html('<div class="text-center">加载中...</div>');
						if(url){
							$.get(url,function (data) {
								 
								that.$pageDetail.find('.j-content').html('<div class="promotion-content">'+data+'</div>');
							});
							return false;
						}
					});
					 
				};


				this.init=function () {
					var def=that.getData();

					def.done(function (data) {
						that.$container.html(that.buildHtml(data.data));

						that.eventHandle();
					});
				};

				this.init();


 
		  
				
			 
			}
//查询系统讯息 首页弹框
function querySystemMessage(){
	var systemMessageHtml = [ 
		'<div class="system-message">',
		'	<div class="box mui-col-xs32-12 mui-col-xs64-10 mui-col-xs64-offset-1 mui-col-sm-8 mui-col-sm-offset-2 mui-col-md-6 mui-col-md-offset-3">',
		'		<div class="close"></div>',
		'		<div class="content">',
		'			<div class="title1">{0}</div>',
		'			<div class="text1">{1}</div>',
		'		</div>',
		'	</div>',
		'</div>'
	].join('');
	
	mobileManage.ajax({
		url:'mobi/systemConfig.aspx',
		param:{
			typeNo:'type002',
			itemNo:'001',
		},
		callback:function(result){
			if(result.success){
				if($('.system-message').length>0)
					$('.system-message').remove();
				
				var array = result.message.split('#');
				$('body').append(String.format(systemMessageHtml,array[0],array[1]));
				var $systemMessage = $('.system-message');
				$systemMessage.find('.close').click(function(){
					$systemMessage.remove();
				});
				$systemMessage.find('.box').click(function(e){
					e.stopPropagation();
				});
				$systemMessage.click(function(){
					$systemMessage.remove();
				});
				setTimeout(function(){
					$systemMessage.addClass('show');
				},100);
			}
		}
	});
}
