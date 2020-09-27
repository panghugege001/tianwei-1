<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>

	<body>
		<div class="content mt5">
			<div class="ui-form">
				<div class="ui-input-row zf-sele">
					<label class="ui-label">存送优惠类型：</label> 
					<select name="youhuiType" id="youhuiType1" class="" onchange="youHuiTypeChange1(this.value);">
						<option value="">---请选择存送类型---</option>
					</select>
				</div>
				<div class="ui-input-row">
					<label class="ui-label">转账金额：</label>
					<input class="ui-ipt" type="text" name="transferMoney" id="transferMoney1" onblur="getSelfYouhuiAmount1(this.value);" />
				</div>
				<div class="ui-input-row">
					<label class="ui-label">红利金额：</label>
					<input class="ui-ipt" readonly type="text" name="giftMoney1" id="giftMoney1" />
				</div>
				<div class="ui-input-row">
					<label class="ui-label">流水倍数：</label>
					<input class="ui-ipt" type="text" name="waterTimes" id="waterTimes1" readonly="readonly" />
				</div>
				<input id="youhui-id" type="hidden">
				<div class="ui-button-row center">
					<div class="btn-login block" onclick="return checkSelfYouHuiSubmit1();">提交</div>
				</div>
				<div class="space-2"></div>
			</div>

			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
				<div class="h3">温馨提示：</div>
				<ol>
					<li><span style="color:red;">每天的00：00—01：00为系统结算时间，暂时无法使用自助存送。</span></li>
					<li>申请存送后彩金会自动添加到相应平台，您直接进入游戏即可。 </li>
					<li>老虎机存送优惠，存款10元后方可自助操作。</li>
				</ol>
			</div>
		</div>
	</body>
<script type="text/javascript">
/**自助存送功能代码开始处**/

var preferentialConfig = {};
var _runUrl = {};

getSelfDepositData();

// 获取存送优惠数据
function getSelfDepositData() {
	
	$.ajax({
		type: "post",
		url: "/mobi/getYouhuiData.aspx",
		data: {},
		success: function(result) {
			if(result.success){
				data = result.data;
				//console.log(data);
				$.each(data, function(index, ele) {
					preferentialConfig[ele.id] = ele; 
					// 每日任务页签存送优惠类型加载
					// 每日任务页签存送优惠类型加载
					if ((ele.titleName).indexOf("限时优惠") != -1) {					
	 						$("#youhuiType1").append("<option value='" + ele.id + "'>" + ele.name + "</option>");						
				     }
				});
			}
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
		
		$("#youhuiType").append("<option value='" + id + "'>" + ele.name + "</option>");		
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
	

	$.post(url, { "id": data.id, "platformId": data.platformId, "titleId": data.titleId, "remit": money }, function (respData) {
        
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

	$.post(url, { "id": data.id, "platformId": data.platformId, "titleId": data.titleId, "remit": money }, function (respData) {
        
    	_runUrl["everyday" + url] = false;
    	
    	alert(respData);        
    });
};
/**每日任务功能代码结束处**/	
</script>
</html>