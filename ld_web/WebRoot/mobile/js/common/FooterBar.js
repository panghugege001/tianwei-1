/**
 * 
 */
function FooterBar(config){
	var that = this;
	var _config = {
		role:false
	};
	$.extend(_config,config);
	
	var _ROLE = _config.role;
	
	var _footerHtml = [
		'<footer class="common-footer">',
	    '</footer>'
	].join('');
	
	var _footerItemHtml = [
		'<div class="item {0}">',
		'	<div class="iconfont {1}"></div>',
		'	<div class="text">{2}</div>',
		'</div>'
	].join('');

	var _footerItems = {
		'index':{id:'index',text:'首页',icon:'icon-index'},
		'selfGet':{id:'selfGet',text:'自助优惠',icon:'icon-slice2'}, 
		'depost':{id:'depost',text:'存款',icon:'icon-gudingcunkuan'},
		'withdrawal':{id:'withdrawal',text:'提款',icon:'icon-tikuan'},
		'account':{id:'account',text:'我的',icon:'icon-wo'},
		'cooperation':{id:'cooperation',text:'代理',icon:'icon-agent'},
	 
		'agentHistory':{id:'agentHistory',text:'账户清单',icon:'icon-qingdan'},
		'agentWithdrawal':{id:'agentWithdrawal',text:'申请提款',icon:'icon-tikuan'},
		'agent':{id:'agent',text:'我的',icon:'icon-wo wo'}
		
		
	};
	
	var _customFooterItemAuth = ['index','selfGet','depost','withdrawal','account','cooperation'];
	var _agentFooterItemAuth = ['index','agentWithdrawal','agentHistory','agent','cooperation'];
	var _defultFooterItemAuth = ['index','selfGet','depost','withdrawal','account','cooperation'];
	

	var _event = {};
	var _$body;
	var _$scrollWrap;
	var _delay = 200;
	
	that.renderItem = _renderItem;
	that.scrollHide = _scrollHide;
	that.active = _active;
	that.hide = _hide;
	that.show = _show;
	that.onHide = false;
	that.onShow = false;
	that.defHeight = 0;
	
	//产生 footer
	function _init(){
		_$body = $('body');
		if(_$body.find('footer[class="common-footer"]').length>0){
			return;
		}
		_$body.append(_footerHtml);
		that.$el = _$body.find('footer[class="common-footer"]');
		_renderItem();
		
		that.defHeight = that.$el.height();
	}
	
	//产生footer item
	function _renderItem(){
		var itemArray = new Array();
		var itemAuth = _getItemAuth();
		var obj,$item;
		for(var i = 0,l = itemAuth.length;i<l;i++){
			obj = _footerItems[itemAuth[i]];
			if(obj){
				$item = $(String.format(_footerItemHtml,obj.id,obj.icon,obj.text));
				$item.bind('click',{itemObj:obj},_getClickEvent);
				itemArray.push($item);
			}
		}
		that.$el.append(itemArray);
		itemArray = itemAuth = obj = $item = null;
	}
	
	//取得item显示权限
	function _getItemAuth(){
		if(_ROLE=='MONEY_CUSTOMER')
			return _customFooterItemAuth;
		if(_ROLE=='AGENT')
			return _agentFooterItemAuth;
		return _defultFooterItemAuth;
	}
	
	//设定按钮事件
	function _getClickEvent(e){
		switch (e.data.itemObj.id){
			case 'index':
				mobileManage.redirect('index');
				break;
			case 'preferential':
				mobileManage.redirect('preferential');
				break;
			case 'cooperation':
				/*mobileManage.redirect('cooperation');*/
				window.location.href='/mobile/cooperation.jsp'; 
				break;
			case 'bbs':
				mobileManage.redirect('bbs');
				break;
			case 'email':
				if(!_ROLE){
					mobileManage.getModel().open('login',['email']);
				}else{
					mobileManage.redirect('email');
				}
				break;
			case 'depost':
				if(!_ROLE){
					webapp.redirect(window.location.origin+'/mobile/login.jsp');
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agent',{active:1});
				}else{
					window.location.href='/mobile/deposit.jsp'; 
					/*mobileManage.redirect('depost');*/
				}
				break;
			case 'selfGet':
				if(!_ROLE){
					webapp.redirect(window.location.origin+'/mobile/login.jsp');
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('index');
				}else{
					window.location.href='/mobile/selfGet.jsp';
					/*mobileManage.redirect('selfGet');*/
				}
				break;
			case 'daily':
				if(!_ROLE){
					mobileManage.getModel().open('login',['daily']);
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('index');
				}else{
					mobileManage.redirect('daily');
				}
				break;
			case 'account':
				if(!_ROLE){
					webapp.redirect(window.location.origin+'/mobile/login.jsp');
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agent',{active:0});
				}else{
					window.location.href='/mobile/account.jsp';
					/*mobileManage.redirect('account');*/
				}
				break;
			case 'accountHistory':
				if(!_ROLE){
					mobileManage.getModel().open('login',['accountHistory']);
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agentHistory');
				}else{
					mobileManage.redirect('accountHistory');
				}
				break;
			case 'agentWithdrawal':
				if(!_ROLE){
					mobileManage.getModel().open('login',['agentWithdrawal',{active:1}]);
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agentWithdrawal',{active:1});
				}else{
					mobileManage.redirect('fundsManage',{mode:0,depositMode:0});
				}
				break;
			case 'withdrawal':
				if(!_ROLE){
					webapp.redirect(window.location.origin+'/mobile/login.jsp');
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agent',{active:1});
				}else{
					window.location.href='/mobile/withdrawal.jsp';
					/*mobileManage.redirect('withdrawal');*/
				}
				break;
			case 'agent':
				if(!_ROLE){
					mobileManage.getModel().open('login',['agent',{active:0}]);
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agent',{active:0});
				}else{
					mobileManage.redirect('account');
				}
				break;
			case 'agentHistory':
				if(!_ROLE){
					mobileManage.getModel().open('login',['agentHistory']);
				}else if(_ROLE=='AGENT'){
					mobileManage.redirect('agentHistory');
				}else{
					mobileManage.redirect('accountHistory');
				}
				break;
			default:
		}
	}

	/**
	 * 选取item by key
	 */
	function _active(key){
		_activeItem(_footerItems[key]);
	}
	
	/**
	 * 选取item
	 */
	function _activeItem(obj){
		if(!obj||typeof obj !== 'object')return;
		var $active = that.$el.find('.'+obj.id);
		if($active.length==0){
			_activeItem(_footerItems['index']);
			return;
		}
		$active.addClass('active');
		that.$el.scrollLeft($active.position().left-$active.width()*2);
		$active = null;
	}
	

	/**
	 * 滑动时隐藏
	 * @param {Boolean} hide 
	 * 
	 */
	function _scrollHide(hide){
		_$scrollWrap = _$body.find('>.main-wrap');
		if(hide){
			_$scrollWrap.bind('scroll',_onScroll);
		}else{
			_$scrollWrap.unbind('scroll',_onScroll);
		}
	}
	
	/**
	 * 
	 */
	function _onScroll(e){
		delayAction('headerOnScorll',_delay,function(){
			var margin = _$body.find('>.main-wrap>.header-margin').height();
			if(_$body.find('>.main-wrap>.content').position().top==margin&&that.$el.position().top!=0){
				_show();
			}else if(_$body.find('>.main-wrap>.content').position().top!=margin&&that.$el.position().top==0){
				_hide();
			}
		});
	}
	
	/**
	 * 绑定事件
	 * @param {String} event 名称
	 * @param {Function} 处理方法
	 */
	function _bind(event,handler){
		if(typeof event !== 'string'){
			alert('event 参数格式错误！');
			return;
		}
		if(typeof handler !== 'function'){
			alert('handler 参数格式错误！');
			return;
		}
		
		if(!_event[event]){
			_event[event] = new Array();
		}
		_event.push(handler);
	}

	/**
	 * 解绑定事件
	 * @param {String} event 名称
	 * @param {Function} 处理方法
	 */
	function _unbind(event,handler){
		if(typeof event !== 'string'){
			alert('event 参数格式错误！');
			return;
		}
		if(typeof handler !== 'function'){
			alert('handler 参数格式错误！');
			return;
		}
		
		if(_event[event]){
			_event[event] = null;
		}
	}
	

	/**
	 * 绑定事件
	 * @param {String} event 名称
	 */
	function _trigger(event){
		if(typeof event !== 'string'){
			alert('event 参数格式错误！');
			return;
		}
		var array = _event[event];
		if(!array)return;
		
		for(var i = 0,l=array.length;i<l;i++){
			array[i]();
		}
	}
	
	/**
	 * 隐藏
	 */
	function _hide(){
		that.$el.css('bottom',-that.defHeight);
		_trigger('hide');
	}
	
	/**
	 * 显示
	 */
	function _show(){
		that.$el.css('bottom',0);
		_trigger('show');
	}
	_init();
}