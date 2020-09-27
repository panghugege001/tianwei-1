<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
	<body>
		<div class="content">
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
				<div class="mui-select">
			  	  	<div id="youhui-type"></div>
			    	<label>存送优惠类型</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="youhui-money" type="text" >
			    	<label>转账金额</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="youhui-giftMoney" type="text" readonly>
			    	<label>红利金额</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="youhui-waterTimes" type="text" readonly>
			    	<label>流水倍数</label>
			  	</div>
			  	<input id="youhui-id" type="hidden">
				<div class="mui-btn mui-btn--raised mui-btn--primary block" id="youhui-submit" >提交</div>
				<div class="space-2"></div>
			</div>
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
				<div class="h3">温馨提示：</div>
				<ol>
	              	<li>每天<span style="color:red;"> 00:00 - 01:00 </span>系统结算时间,短暂时间无法使用自助存送。</li>
	              	<li>存送优惠申请成功后系统会自动派发您相应的游戏平台账户，请登录查看并游戏。 </li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function YouhuiPage(){
				var that = this;
				
				//设定只能输入数字
				NumberInput('youhui-money');
				var data = [];
				that.$giftMoney = $('#youhui-giftMoney');
				that.$waterTimes = $('#youhui-waterTimes');
				that.$money = $('#youhui-money');
				that.$youhuiId = $("#youhui-id");
				that.$submit = $('#youhui-submit');
				
				//输入金额立即计算红利
				that.$money.keyup(function(e){
					//延迟不重复处理同样的动作
					delayAction('youhui-money',300,function(){
						var giftMoney = that.$money.val()*that.type.getRecord().percent;
						giftMoney = giftMoney>that.type.getRecord().limitMoney?that.type.getRecord().limitMoney:giftMoney;
						that.$giftMoney.val(giftMoney.toFixed(2));
					});
				});
				
				that.$submit.click(_submit);
				
				if($('#youhui-type').length>0){
					that.type = new MobileComboBox({
						appendId:'youhui-type',
						cls:'',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'',name:'请选择'},
						],
						onChange:function(e){
							that.$giftMoney.val('0.00');
							that.$waterTimes.val(that.type.getRecord().betMultiples||0);
							that.$money.val('');
							that.$youhuiId.val(that.type.getRecord().id);
						}
					});
				}
				
				mobileManage.getLoader().open('查询中');
				//查询存送优惠类型
				mobileManage.getSelfGetManage().getYouhuiData(function(result){
					if(result.success){
						data = result.data;
						var _youhuiData = new Array();
						_youhuiData.push({value:'',name:'请选择'});
						for (var i = 0, l = result.data.length; i < l; i++) {
							if ((result.data[i].titleName).indexOf("次存优惠") != -1) {
								result.data[i].value = 	result.data[i].id;
								_youhuiData.push(result.data[i]);
							}
						}
						that.type.loadData(_youhuiData);
						that.type.setValue(that.type.getValue());
					}else{
						alert(result.message);
					}
					mobileManage.getLoader().close();
				});
				
				//存送
				function _submit(){
					var o = {};
					for (var i = 0, l = data.length; i < l; i++) {
						if (data[i].id == that.type.getValue()) {
							o = data[i];
							break;
						}
					}
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().doYouhui({
						id: o.id,
						platformId: o.platformId,
						titleId: o.titleId,
						youhuiType: o.titleId,
						money: that.$money.val()
					},function(result){
						mobileManage.getLoader().close();
						if(result.success){
							alert(result.message);
						}else{
							alert(result.message);
						}
					});
				}
			}
		</script>
		
	</body>
</html>