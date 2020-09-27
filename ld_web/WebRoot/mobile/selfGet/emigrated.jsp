<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<div class="content">
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-0">
				<div class="mui-textfield">
			  	  	<input id="emigrated-apply" type="text" readonly />
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="emigrated-balance" type="text" readonly />
			    	<label>闯关奖金余额</label>
			  	</div>
				<div class="mui-select">
			  	  	<div id="emigrated-platform"></div>
			    	<label>目标账户</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="emigrated-money" type="text" />
			    	<label>转账金额</label>
			  	</div>
				<div class="mui-buttons">
					<button class="mui-btn mui-btn--raised mui-btn--primary" id="emigrated-submit">转账</button>
					<button class="mui-btn mui-btn--raised mui-btn--danger" id="emigrated-getAward">领取昨日奖励</button>
				</div>
			</div>
			<div class="panel mui-col-xs32-12 mui-col-xs64-6" >
				<div class="h3">温馨提示</div>
				<ol>
	              	<li>一共5个等级，存款<span style="color:red;">100</span>以后，自助报名选择一个等级闯关。一天只能选择一个等级，闯关一次。</li>
	              	<li>报名成功以后，达到对应的等级以及对应平台的流水要求，即可获得单项奖励，若四个平台流水全部达到，则获得额外奖金。 </li>
	              	<li>点击今日报名，成功达到投注额后，奖励次日（<span style="color:red;">10:00-24:00</span>）系统自动派发，次日点击“领取昨日奖励”奖金会显示新增的额度。超过24点则无法领取。奖励会累积在奖金余额内，奖金10元以上即可转入游戏（PT/TTG/NT/QT）平台，流水<span style="color:red;">20</span>倍。 </li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function EmigratedPage(){
				var that = this;
				var _isSign = true;
				NumberInput('emigrated-money');
				
				that.$apply = $('#emigrated-apply');
				that.$balance = $('#emigrated-balance');
				that.$money = $('#emigrated-money');
				that.$getAward = $('#emigrated-getAward');
				that.$submit = $('#emigrated-submit');
				
				that.$getAward.click(_getAward);
				that.$submit.click(_submit);
				
				
				if($('#emigrated-platform').get(0)){
					that.platform = new MobileComboBox({
						appendId:'emigrated-platform',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'pt',name:'PT账户'},
							{value:'ttg',name:'TTG账户'},
 							{value:'nt',name:'NT账户'},
							{value:'qt',name:'QT账户'}
						],
						onChange:function(e){
							
						}
					});
				}

				/**
				 * 查詢闯关参加状态与奖励余额
				 */
				function _getStatus(){
					that.$balance.val('0');
					mobileManage.getLoader().open('查询中');
					mobileManage.getEmigratedManage().getStatus(function(result){
						if(result.success){
							that.$apply.val(result.data.message);
							that.$balance.val(result.data.money);
							if(!result.data.isApply){
								that.$apply.parent().append('<div id="emigrated-apply-button" class="mui-btn mui-btn--raised mui-btn--danger" onclick="mobileManage.getModel().open(\'emigrated\')" >报名</div>');
							}
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}
				
				//领取奖励
				function _getAward(){
					mobileManage.getLoader().open('处理中');
					mobileManage.getEmigratedManage().getAward(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							that.$balance.val(result.data.money);
							alert(result.message);
						}else{
							alert(result.message);
						}
					});
				}
				
				//奖励转帐
				function _submit(){
					mobileManage.getLoader().open('执行中');
					mobileManage.getEmigratedManage().transfer({
						platform:that.platform.getValue(),
						money:that.$money.val()
					},function(result){
						mobileManage.getLoader().close();
						if(result.success){
							that.$balance.val(result.data.money);
							that.$money.val('');
							alert(result.message);
						}else{
							alert(result.message);
						}
					});
				}
				
				_getStatus();
			}
		</script>
	</body>
</html>