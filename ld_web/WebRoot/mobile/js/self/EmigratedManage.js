/**
 * 负责所有帐户相关的操作
 */

function EmigratedManage(base_url){
	var that = this;
	var _base_url = base_url;
	var _urlObj = {
		getEmigratedStatus:'mobi/getEmigratedStatus.aspx',
		applyEmigrated:'mobi/applyEmigrated.aspx',
		getEmigratedAward:'mobi/getEmigratedAward.aspx',
		transferEmigrated:'mobi/transferEmigrated.aspx'
	};
	
	/**
	 * 查询闯关状态
	 * @param {function} 回调
	 */
	that.getStatus = function(callback){
		mobileManage.ajax({
			url:getUrl('getEmigratedStatus'),
			callback:callback
		});
	};
	
	/**
	 * 参加闯关
	 * @param {function} 回调
	 */
	that.apply = function(formData,callback){
		var _formData = {
			emigratedeLevel:''
		};
		$.extend(_formData,formData);
		//检查
		var err = _applyDataCheck(_formData);
		if(err){
			if(callback instanceof Function){
				callback({success:false,message:err});
			}
			return;
		}
		mobileManage.ajax({
			url:getUrl('applyEmigrated'),
			param:_formData,
			callback:callback
		});
	};

	/**
	 * 领取闯关奖励
	 * @param {Function} callback 回调
	 */
	that.getAward = function(callback){
		mobileManage.ajax({
			url:getUrl('getEmigratedAward'),
			callback:callback
		});
	};

	/**
	 * 闯关奖励转帐
	 * @param {Object} formData 资料
	 * @param {Function} callback 回调
	 */
	that.transfer = function(formData,callback){
		var _formData = {
			platform:'',
			money:''
		};
		$.extend(_formData,formData);
		//检查
		var err = _transferEmigratedDataCheck(_formData);
		if(err){
			if(callback instanceof Function){
				callback({success:false,message:err});
			}
			return;
		}
		mobileManage.ajax({
			url:getUrl('transferEmigrated'),
			param:_formData,
			callback:callback
		});
	};
	
	//驗證資料
	function _applyDataCheck(formData){
		if(!formData.emigratedeLevel){
			return '[提示]请选择闯关等级！';
		}
		return undefined;
	}
	
	function _transferEmigratedDataCheck(formData){
		if(!formData.platform){
			return '[提示]请选择转入平台！';
		}
		if(!formData.money){
			return '[提示]请选择转入金额！';
		}
		return undefined;
	}
	
	/**
	 * get action url
	 * @param {String} name actin name
	 */
	function getUrl(name){
		if(!_urlObj[name]){
			alert(name+' 路径不存在！');
			return;
		}
		return _base_url+_urlObj[name];
	};
}
