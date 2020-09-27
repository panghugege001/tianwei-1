/**
 * 最下方的内容方块
 */
function FooterContent(selector){
	var that = this;
	var _$footer = false;
	var _$items = false;
	var _browseWay = '';
	
	var _footerText = '天威执照由菲律宾娱乐及博彩公司（PAGCOR）所核发和监管';
	var _itemData = [
		{id:'mobile',text:'手机网页版'},
		{id:'appDownload',text:'APP下载'},
		{id:'app',text:'APP版'},
		{id:'desktop',text:'电脑版'}
	];
	if(sessionStorage['webapp']==='true'){
		var pos = _itemData.map(function(e){return e.id;}).indexOf('app');
		_itemData.splice(pos, 1);
		var pos2 = _itemData.map(function(e){return e.id;}).indexOf('appDownload');
		_itemData.splice(pos2, 1);
	}
	var _footerHtml = [
		'<div class="footer-content">',
		'	<div class="items"></div>',
		'	<div class="text">{0}</div>',
        '</div>'
    ].join('');
	var _footerContentMarginHtml = '<div class="footer-content-margin"></div>';
	
	var _itemHtml = '<div class="item mui-col-xs32-{0} {1}">{2}</div>';
	
	_init();
	
	function _init(){
		if(_$footer)return;
		_browseWay = sessionStorage['browseWay'];
		
		if(!_browseWay){
			if(webapp.isWebApp()){
				_browseWay = 'app';
			}else if(isMobile()){
				_browseWay = 'mobile';
			}else{
				_browseWay = 'desktop';
			}
			sessionStorage['browseWay'] = _browseWay;
		}else{
			if(webapp.isWebApp()){
				_browseWay = 'app';
			}else{
				_browseWay = 'mobile';
			}
			sessionStorage['browseWay'] = _browseWay;
		}
		
		//如果是App开启
//		if(webapp.isWebApp()){
//			_itemData = [
//	     		{id:'mobile',text:'手机网页版'}
//	     	];
//		}
		
		_$footer = $(String.format(_footerHtml,_footerText));
		_$items = _$footer.find('.items');
		_$items.append(_getItemsHtml(_browseWay));
		_$items.find('.item').click(_active);
		_$items.find('.'+_browseWay).addClass('active');
		
		_$footerContentMargin = $(_footerContentMarginHtml);
		
		$(selector).append(_$footer);
		$(selector).append(_$footerContentMargin);

		//计算下面要空出多少高度
		_$footerContentMargin.height($('.footer-content').height()+20);
	}
	
	function _getItemsHtml(active){
		var array = [];
		var c = 12/_itemData.length;
		for(var i in _itemData){
			array.push(String.format(_itemHtml,c,_itemData[i].id,_itemData[i].text));
		}
		return array.join('');
	}
	
	/**
	 * 选取
	 */
	function _active(e){
		if(this.className.indexOf('appDownload')!=-1){
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
                                                } else {
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

			return;
		}
	
		_$items.find('.item').removeClass('active');
		$(this).addClass('active');
		if(this.className.indexOf('mobile')!=-1){
			sessionStorage['browseWay'] = 'mobile';
			webapp.redirect(window.location.origin+'/mobile');
		}else if(this.className.indexOf('desktop')!=-1){
			sessionStorage['browseWay'] = 'desktop';
			webapp.redirect(window.location.origin);
		}else if(this.className.indexOf('app')!=-1){
//			alert('功能尚未开放！');
//			return;
//			sessionStorage['browseWay'] = 'app';
			window.location.href = 'longduwebapp://';
		}
	}
	
	$("body").on("click",".")
}