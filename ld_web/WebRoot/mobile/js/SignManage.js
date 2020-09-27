/**
 * 负责所有帐户相关的操作
 */

function SignManage(base_url){
	var that = this;
	var _base_url = base_url;
	var _urlObj = {
		querySignMoney:'mobi/querySignMoney.aspx',
		doSign:'mobi/doSign.aspx',
		signStatus:'mobi/signStatus.aspx',
		transferInforSign:'mobi/transferInforSign.aspx'
	};
	
	/**
	 * 查询签到余额
	 * @param {function} 回调
	 */
	that.querySignMoney = function(callback){
		mobileManage.ajax({
			url:getUrl('querySignMoney'),
			callback:callback
		});
	};
	
	/**
	 * 签到
	 * @param {function} 回调
	 */
	that.doSign = function(callback){
		mobileManage.ajax({
			url:getUrl('doSign'),
			callback:callback
		});
	};

	/**
	 * 今日签到状态签到
	 * @param {Function} callback 回调
	 */
	that.signStatus = function(callback){
		mobileManage.ajax({
			url:getUrl('signStatus'),
			callback:callback
		});
	};

	/**
	 * 签到转帐
	 * @param {Object} formData 资料
	 * @param {Function} callback 回调
	 */
	that.transferInforSign = function(formData,callback){
		var _formData = {
			platform:'',
			money:''
		};
		$.extend(_formData,formData);
		//检查
		var err = _transferInforSignDataCheck(_formData);
		if(err){
			if(callback instanceof Function){
				callback({success:false,message:err});
			}
			return;
		}
		mobileManage.ajax({
			url:getUrl('transferInforSign'),
			param:_formData,
			callback:callback
		});
	};
	
	//驗證資料
	function _transferInforSignDataCheck(formData){
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
