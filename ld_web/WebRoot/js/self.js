/**自助存送功能代码开始处**/

var preferentialConfig = {};
var _runUrl = {};

getSelfDepositData();

// 获取存送优惠数据
function getSelfDepositData() {
	
	$.ajax({
		type: "post",
		url: "/asp/getYouHuiConfig.aspx",
		data: {},
		success: function(data) {
			
			$.each(data, function(index, ele) {
				preferentialConfig[ele.id] = ele; 
				
				// 每日任务页签存送优惠类型加载
			    if ((ele.titleName).indexOf("限时优惠") != -1) {
					   $("#youhuiType1").append("<option value='" + ele.id + "'>" + ele.aliasTitle + "</option>");						
				}
			});
		}
	});
};

// 选择平台下拉事件
function youHuiNameChange(value) {
	
	$("#youhuiType").empty();
	$("#giftMoney").val('');
	$("#waterTimes").val('');
	
	if (undefined == value || null == value || '' == value) {
		
		return;
	}
	
	var data = [];
	
	for (var prop in preferentialConfig) {
	    
		if (preferentialConfig.hasOwnProperty(prop)) {
	        
			var p = preferentialConfig[prop];
			
			if (value == p.platformId) {
				
				data.push(p);
			}
		}
	}
	
	if (data.length == 0) {
		
		alert('未找到' + $('#youhuiName').find('option:selected').text() + '类型数据！');
		return;
	}
	
	$.each(data, function (index, ele) {
		
		var id = ele.id;
		
		$("#youhuiType").append("<option value='" + id + "'>" + ele.aliasTitle + "</option>");		
	});
	
	youHuiTypeChange(data[0].id);
};

// 优惠类型下拉事件
function youHuiTypeChange(id) {
	
	if (undefined == id || null == id || '' == id) {
	
		return;
	}
	
	var data = preferentialConfig[id];
	
	// 流水倍数
	$("#waterTimes").val(data.betMultiples);
	// 转账金额
	var transferMoney = $("#transferMoney").val();
	
	getSelfYouhuiAmount(transferMoney);
};

// 红利金额计算方法
function getSelfYouhuiAmount(value) {
	
	var money = 0.00;
	
	if (!(null == value || '' == value || isNaN(value))) {
		
		var id = $("#youhuiType").val();
		var data = preferentialConfig[id];
		
		// 计算红利金额
		money = value * (data.percent) > (data.limitMoney) ? (data.limitMoney) : value * (data.percent);
	}
	
	$("#giftMoney").val(money.toFixed(2));
};

// 提交方法
function checkSelfYouHuiSubmit() {
	
	// 选择平台
	var name = $("#youhuiName").val();
	// 优惠类型
	var type = $("#youhuiType").val();
	// 转账金额
	var money = $("#transferMoney").val();
	// 执行的请求地址
	var url = "/asp/getSelfYouHuiObject.aspx";
	
	if (null == name || '' == name) {
	
		alert("请选择存送优惠平台！");
		return;
	}
	
	if (null == type || '' == type) {
	
		alert("请选择存送优惠类型！");
		return;
	}
	
	if (null == money || '' == money) {
	
		alert("请输入转账金额！");
		return;
	}
	
	if (isNaN(money)) {
		
		alert("转账金额只能为数字！");
		return;
	}
	
	var rex = /^[1-9][0-9]+$/;
	
	if (!rex.test(money)) {
		
		alert("抱歉，存送金额只能是大于或等于10元的整数哦。");
		return;
	}
	
	if (_runUrl[url]) {
		
		alert('目前正在执行，请稍候再尝试！');
		return;
	}
	
	var data = preferentialConfig[type];
	
	_runUrl[url] = true;
	
	openProgressBar();

	$.post(url, { "id": data.id, "platformId": data.platformId, "titleId": data.titleId, "remit": money }, function (respData) {
        
    	closeProgressBar();
    	_runUrl[url] = false;
    	
    	alert(respData);        
    });
};
/**自助存送功能代码结束处**/

/**每日任务功能代码开始处**/

function youHuiTypeChange1(id) {
	
	$("#giftMoney1").val('');
	$("#waterTimes1").val('');
	
	if (undefined == id || null == id || '' == id) {
		
		return;
	}
	
	var data = preferentialConfig[id];
	
	// 流水倍数
	$("#waterTimes1").val(data.betMultiples);
	// 转账金额
	var transferMoney = $("#transferMoney1").val();
	
	getSelfYouhuiAmount1(transferMoney);
};

function getSelfYouhuiAmount1(value) {
	
	var money = 0.00;
	
	if (!(null == value || '' == value || isNaN(value))) {
		
		var id = $("#youhuiType1").val();
		
		if (!(undefined == id || null == id || '' == id)) {
		
			var data = preferentialConfig[id];
			// 计算红利金额
			money = value * (data.percent) > (data.limitMoney) ? (data.limitMoney) : value * (data.percent);
		}
	}
	
	$("#giftMoney1").val(money.toFixed(2));
};

function checkSelfYouHuiSubmit1() {
	
	// 优惠类型
	var type = $("#youhuiType1").val();
	// 转账金额
	var money = $("#transferMoney1").val();
	// 执行的请求地址
	var url = "/asp/getSelfYouHuiObject.aspx";
	
	if (null == type || '' == type) {
	
		alert("请选择存送优惠类型！");
		return;
	}
	
	if (null == money || '' == money) {
	
		alert("请输入转账金额！");
		return;
	}
	
	if (isNaN(money)) {
		
		alert("转账金额只能为数字！");
		return;
	}
	
	var rex = /^[1-9][0-9]+$/;
	
	if (!rex.test(money)) {
		
		alert("抱歉，存送金额只能是大于或等于10元的整数哦。");
		return;
	}
	
	if (_runUrl["everyday" + url]) {
		
		alert('目前正在执行，请稍候再尝试！');
		return;
	}
	
	var data = preferentialConfig[type];
	
	_runUrl["everyday" + url] = true;
	
	openProgressBar();

	$.post(url, { "id": data.id, "platformId": data.platformId, "titleId": data.titleId, "remit": money }, function (respData) {
        
    	closeProgressBar();
    	_runUrl["everyday" + url] = false;
    	
    	alert(respData);        
    });
};
/**每日任务功能代码结束处**/