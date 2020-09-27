/**
 * 
 */
function ModeControl(){
	var _this = this;
	var _key;
	var _nextMode;
	var _activeMode;
	var _lastMode;
	var _modes = {};
	var _events = {};
	var _eventReg = /^(beforChange||change)$/;
	var _isChange = false;
	
	_this.activeMode = function(key){
		if(!_isChange){
			_isChange = true;
			_nextMode = _modes[key];
			if(_nextMode&&_nextMode!=_activeMode){
				_trigger('beforChange');
				_lastMode = _activeMode;
				_activeMode = _nextMode;
				_nextMode = undefined;
				_key = key;
				_trigger('change');
			}
			_isChange = false
		}
	};
	
	_this.getModeByKey = function(id){
		return _modes[id];
	};
	
	_this.getNextMode = function(){
		return _nextMode;
	};
	
	_this.getActiveMode = function(){
		return _activeMode;
	};

	_this.getLastMode = function(){
		return _lastMode;
	};
	
	_this.getActiveKey = function(){
		return _key;
	};
	
	_this.putMode = function(key,conf){
		var _conf = {
			id:''
		};
		$.extend(_conf,conf);
		_modes[key] = _conf;
	};
	
	_this.on=function(event,fn){
		if(_eventReg.test(event)&&fn instanceof Function){
			_events[event] = fn;
		}
	};
	
	function _trigger(event){
		for(var e in _events){
			if(e==event){
				_events[e]();
			}
		}
	}
}