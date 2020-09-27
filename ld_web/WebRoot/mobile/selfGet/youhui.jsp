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
					<label class="ui-label">存送优惠平台：</label>	
			  	  	<div id="youhui-platform"></div> 
			  	</div>
				<div class="ui-input-row zf-sele">
					<label class="ui-label">存送优惠类型：</label>	
			  	  	<div id="youhui-type"></div> 
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">红利金额：</label>	
			  	  	<input id="youhui-giftMoney" type="text" class="ui-ipt" readonly>
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">流水倍数：</label>	
			  	  	<input id="youhui-waterTimes" type="text" class="ui-ipt" readonly>
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">转账金额：</label>	
			  	  	<input id="youhui-money" type="text" class="ui-ipt" > 
			  	</div>
			  	<input id="youhui-id" type="hidden">
				<div class="ui-button-row center">
					<div class="btn-login block" id="youhui-submit" >提交</div>
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
		<script type="text/javascript">
			function YouhuiPage(){
				var that = this;
				
				//设定只能输入数字
				NumberInput('youhui-money');
				
				//优惠平台
				var platformData = [
					{value:'',name:'请选择'}
				];
				
				var data = [];

				//优惠类型
				var typeData = {
					"6001": [ { value: '', name: '请选择' } ],
				    "6002": [ { value: '', name: '请选择' } ],
				    "6003": [ { value: '', name: '请选择' } ],
				    "6004": [ { value: '', name: '请选择' } ],
				    "6005": [ { value: '', name: '请选择' } ],
				    "6006":	[ { value: '', name: '请选择' } ],
				    "6007":	[ { value: '', name: '请选择' } ],
				    "6008":	[ { value: '', name: '请选择' } ],
				    "6009":	[ { value: '', name: '请选择' } ]
				};
				
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
				
				if($('#youhui-platform').length>0){
					that.platform = new MobileComboBox({
						appendId:'youhui-platform',
						cls:'',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'',name:'请选择'},
						],
						onChange:function(e){
							that.type.loadData(typeData[that.platform.getValue()]);
							that.type.setValue('');
						}
					});
				}
				
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
						for (var i = 0, l = result.data.length; i < l; i++) {
							var obj = result.data[i];
							for (var k in typeData) {
								if (obj.platformId && obj.platformId.indexOf(k) != -1) {
									result.data[i].value = obj.id;
									typeData[k].push(result.data[i]);
								}
							}
						}
						platformData.push({ value: '6001', name: 'PT存送优惠' });
						platformData.push({ value: '6006', name: 'TTG存送优惠' });
						platformData.push({ value: '6009', name: '老虎机存送优惠(SW.MG.DT.PNG.QT.NT)' });
						platformData.push({ value: '6008', name: 'AG真人存送优惠' });
						
						that.platform.loadData(platformData);
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
						    if(result.message.indexOf('binding')>=0){
						        alert('请先绑定银行卡和真实姓名');
						        window.location.href='mobile/account.jsp';
						        return false;
							}
							alert(result.message);
						}
					});
				}
			}
		</script>
		
	</body>
</html>