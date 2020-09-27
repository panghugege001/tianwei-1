/**
 * 
 */
function AgentManage(base_url){
	var that = this;
	var _base_url = base_url;
	var _getProposalTypeUrl = _base_url+'mobi/getProposalType.aspx';
	var _platformTotalUrl =  _base_url+'mobi/queryPlatformTotal.aspx';
	var _platformDetailsUrl = _base_url+'mobi/queryPlatformDetails.aspx';

	//查询存款类型
	that.getProposalType = function(formData,callback){
		mobileManage.ajax({
			url:_getProposalTypeUrl,
			param:formData,
			callback:callback
		});
	};
	
	//查询平台输赢
	that.queryPlatformDetails = function(formData,callback){
		var _formData = {
			startDate:'',
			endDate:'',
			account:'',
			game:''
		};
		$.extend(_formData,formData);

		//檢查
		var err = _checkPlatformDetails(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		mobileManage.ajax({
			url:_platformDetailsUrl,
			param:_formData,
			callback:callback
		});
		_formData = err = null;
	};
	
	//询平台输赢資料驗證
	function _checkPlatformDetails(formData){
		if(!formData.startDate){
			return "[提示]起始时间不可为空";
		}
		if(!formData.endDate){
			return "[提示]结束时间不可为空";
		}
		return undefined;
	}
}