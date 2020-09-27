$(function() {
	var count = mobileManage.getSessionStorage('index')['firstPageCount'] || 0;
	if(count == 0) {
		mobileManage.setSessionStorage('index', {
			firstPageCount: 1
		});
		// firstPageCount();
	}
})
//进入第一个画面
function firstPageCount() {
	if(webapp&&webapp.isWebApp()) return;
	var html = [
		'<div class="first-page">',
		'<img src="/mobile/img/logo.png" alt="" />',
		'<div class="title">天威 奢华享受',
		'	<small>一切尽在您的掌握之中</small></div>',
		'<a href="" class="f-btn download"><img src="/mobile/img/icon/web.png"/>下载手机客户端</a>',
		'<div class="f-btn into"><img src="/mobile/img/icon/pc.png"/>立即进入网页版</div>',
		'<div class="bottom">天威执照由菲律宾娱乐及博彩公司（PAGCOR）所核发和监管</div>',
		'</div>'
	].join('');
	$('body').append(String.format(html, getMobileKind() == 'Android' ? 1 : 1));
	var $first = $('.first-page');
	$first.find('.download').click(function() {
		appDownload();
	});
	$first.find('.into').click(function() {
		$first.remove();
		//					 window.location.reload();
	});

	setTimeout(function() {
		$first.css({
			opacity: 1
		});
	}, 50);

	setTimeout(function() {
		$first.find('.hand').css({
			bottom: $first.find('.footer').height() + 50
		});
	}, 250);
}

//APP下载

function appDownload(){
	var isEmpty = 0;
	var version = getMobileKind().toUpperCase();
	// step 1
	if(version == 'OTHER') {
		alert('请用安卓或者苹果设备打开！！');
		return false;
	}
	// step 2 判断代理域名
	$.getJSON('/asp/checkAgentURLogin.aspx', function(data) {
		if(false == data) {
			// step 3 获取代理包
			$.getJSON('/app/getAppVersionCustomInfo.aspx', function(data) {
				if(data.length > 0) {
					// 代理域名，有代理包
					$.each(data, function(i, obj) {
						//根据移动平台获取下载链接
						if(obj.plat.toUpperCase() === version) {
							if(obj.packageUrl != "") {
								isEmpty = 0;
								window.location.href = obj.packageUrl;
								return false;
							} else {
								isEmpty = 1;
							}
						} else {
							isEmpty = 1;
						}
					});

					if(isEmpty == 1) {
						alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
						return false;
					}
				} else {
					// step 4 代理域名，无代理包
					if('true' == 'true') {
						alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
						return false;
					} else {
						webapp.redirect(window.location.origin + '/app.html?v=2');
					}
				}
			});
		} else {
			webapp.redirect(window.location.origin + '/app.html?v=2');
		}
	});
}



//首页公告
function queryNews() {
	var $newContainer = $('#index-news-marquee');
	var htmlArr = [];
	$.getJSON('/mobi/getAllNews.aspx', function(result) {
		if(result.success) {
			for(var i = 0; i < result.data.length; i++) {
				var obj = result.data[i];
				htmlArr.push('<div class="text">' + obj.title + '&nbsp <span class="fr"> [' + obj.time + ']</span></div>')
			}
			$newContainer.html(htmlArr.join(''));
		} else {
			$newContainer.html(result.message);
		}

		window.setInterval(function() {
			$newContainer.animate({
				top: "-0.666666rem"
			}, 500, function() {
				$newContainer.css({
					top: 0
				})
				$newContainer.find("div:eq(0)").appendTo($newContainer)
			})
		}, 2500)
	});
}

//最新优惠
function createPages() {
	var that = this;
	this.tpl = [
		'<a class="item" data-type="{{type}}" href="{{link}}" data-url="{{url}}">', '<img src="{{image}}">', '<div class="txt">{{startDate}}{{endDate}}</div>', '</a>'
	].join('');
	this.$container = $('#j-youhui');
	this.buildHtml = function(data) {
		var htmlArr = [];
		for(var i = 0; i < 4; i++) {
			var obj = data[i];
			htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm, function($0, $1) {
				if($1 === 'link') {
					return obj[$1] || '/mobile/new/preferential.jsp?'+obj['id'];
				}
				if($1 === 'startDate' && obj[$1] != '') {
					return '优惠时间&nbsp;&nbsp;&nbsp;' + obj[$1] + '至'
				}
				if($1 === 'endDate' && obj["startDate"] == '') {
					return obj['title'];
				}
				return obj[$1] || '';
			}));
		}
		return htmlArr.join('');
	};
	$.getJSON('/data/promotion/promotion_new.json?v=2030', function(data) {
		that.$container.html(that.buildHtml(data.data));
	});
}

function getWinner() {
	$.getJSON('/data/winner.json', function(data) {
		var htmlArr = [],
			tpl = [
				'<div class="item" >',
				'<img src="{{img}}" alt="" />',
				'<div class="rig-info">',
				'<div > 恭喜玩家 <span class = "color1" > {{winner}} </span> 在 {{type}}游戏</div > ',
				'<div><span class="color2">{{gamename}}</span> 赢得 <span class="color3">{{win}}</span>元</div>',
				// '<div class="color1">{{date}}</div>',
				'</div></div>'
			].join('');
		for(var i = 0; i < data.length; i++) {
			var obj = data[i];
			// obj.win = numberUpperFormat(obj.win || 0);
			htmlArr.push(tpl.replace(/{{\s*?(\w+)\s*?}}/gm, function($0, $1) {
				return obj[$1] || '';
			}));
		}
		$('#j-winner-box').html(htmlArr.join(''));
		window.setInterval(function() {
			$("#j-winner-box").animate({
				top: "-2rem"
			}, 500, function() {
				$("#j-winner-box").css({
					top: 0
				})
				$("#j-winner-box .item:eq(0)").appendTo('#j-winner-box')
			})
		}, 2500)
	});
}

//查询系统讯息 首页弹框
function querySystemMessage() {
	var systemMessageHtml = [
		'<div class="overlay">',
		'	<div class="overlay-content">',
		'			<div class="title">{0}<div class="close iconfont icon-close"></div></div>',
		'			<div class="content">{1}</div>',
		'	</div>',
		'</div>'
	].join('');
	mobileManage.ajax({
		url: 'mobi/systemConfig.aspx',
		param: {
			typeNo: 'type002',
			itemNo: '001',
		},
		callback: function(result) {
			if(result.success) {
				if($('.overlay').length > 0)
					$('.overlay').remove();

				var array = result.message.split('#');
				$('body').append(String.format(systemMessageHtml, array[0], array[1]));
				var $systemMessage = $('.overlay');

				$systemMessage.click(function(e) {
					if(!$(e.target).is(".overlay-content *:not(.close)")) {
						$(this).fadeOut(500);
					}
				})
				setTimeout(function() {
					$systemMessage.fadeIn(500);
				}, 100);
			}
		}
	});
}
new Swiper('.swiper-container', {
	pagination: '.swiper-pagination'
});
// querySystemMessage();
getWinner();
queryNews();
createPages();