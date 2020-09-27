/**
 * 
 */
function HeaderBar(config){
	var that = this;
	var _config = {
		role:false
	};
	$.extend(_config,config);
	
	var _event = {};
	var _$body;
	var _$scrollWrap;
	var _delay = 200;

	that.bind = _bind;
	that.unbind = _unbind;
	that.scrollHide = _scrollHide;
	that.scrollHide = _scrollHide;
	that.setTitle = _setTitle;
	that.hide = _hide;
	that.show = _show;
	that.onHide = false;
	that.onShow = false;
	that.defHeight = 0;
	
	function _init(){
		_$body = $('body');
		that.$el = _$body.find('>header[class="common-header"]');
		that.defHeight = that.$el.height();
	}

	/**
	 * 修改title
	 */
	function _setTitle(text){
		if(that.$el){
			that.$el.find('.header-title').html(text);
		}
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
		_event[event].push(handler);
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
		that.$el.css('top',-that.defHeight);
		_trigger('hide');
	}
	
	/**
	 * 显示
	 */
	function _show(){
		that.$el.css('top',0);
		_trigger('show');
	}
	
	_init();
}