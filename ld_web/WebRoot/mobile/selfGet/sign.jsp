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
			  	  	<input id="sign-day" type="text" readonly />
			    	<label>目前连续签到天数</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="sign-balance" type="text" readonly />
			    	<label>签到奖金账户余额</label>
			  	</div>
				<div class="mui-select">
			  	  	<div id="sign-platform"></div>
			    	<label>签到奖金账户</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="sign-money" type="text" />
			    	<label>签到金额</label>
			  	</div>
				<div class="mui-buttons">
					<button class="mui-btn mui-btn--raised mui-btn--primary" id="sign-submit">领取</button>
					<button class="mui-btn mui-btn--raised mui-btn--danger" id="sign-doSign" disabled>签到</button>
				</div>
			</div>
			<div class="panel mui-col-xs32-12 mui-col-xs64-6" >
				<div class="h3">温馨提示</div>
				<ol>
	              	<li>每日存款<span style="color:red">10</span>元以上，便会激活签到系统，每日仅能签到一次。</li>
	              	
	              	<li>每次签到，签到彩金会自动加总，彩金达到<span style="color:red">10</span>元以上，便可选择转入PT、QT、TTG、NT、MG、DT进行游戏。 </li>
	              	<li>此彩金无须流水限制。</li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function SignPage(){
				var that = this;
				var _isSign = true;
				
				that.$day = $('#sign-day');
				that.$balance = $('#sign-balance');
				that.$platform = $('#sign-platform');
				that.$money = $('#sign-money');
				that.$doSign = $('#sign-doSign');
				that.$submit = $('#sign-submit');
				
				that.$doSign.click(_doSign);
				that.$submit.click(_submit);
				
				
				if($('#sign-platform').get(0)){
					that.$platform = new MobileComboBox({
						appendId:'sign-platform',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'pt',name:'PT'},
							{value:'ttg',name:'TTG'},
 							{value:'nt',name:'NT'},
							{value:'qt',name:'QT'},
							{value:'mg',name:'MG'},
							{value:'dt',name:'DT'}
						],
						onChange:function(e){
							
						}
					});
				}
				
				/**
				 * 查詢簽到餘額、簽到次數
				 */
				function _getSignMoney(){
					that.$balance.val('');
					mobileManage.getSignManage().signStatus(function(result){
						if(result.success){
							that.$balance.val(result.data.signMoney);
							if(result.data.signContinue=='false'){
								that.$day.val(result.data.continueSignCount+"(连续签到已中断)");
							}else{
								that.$day.val(result.data.continueSignCount);
							}
							_isSign = result.data.status=='true';
							that.$doSign.attr('disabled',_isSign);
						}else{
							alert(result.message);
						}
					});
				}
				
				//签到
				function _doSign(){
					if(_isSign){
						alert('本日已签到！');
						return;
					}
					mobileManage.getLoader().open('处理中');
					mobileManage.getSignManage().doSign(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							alert(result.message);
						}else{
							alert(result.message);
						}
						_getSignMoney();
					});
				}
				
				//签到转帐
				function _submit(){
		
					mobileManage.getLoader().open('执行中');
					mobileManage.getSignManage().transferInforSign({
						platform:that.$platform.getValue(),
						money:that.$money.val()
					},function(result){
						mobileManage.getLoader().close();
						if(result.success){
							alert(result.message);
						}else{
							alert(result.message);
						}
						_getSignMoney();
					});
				}
				
				_getSignMoney();
			}
		</script>
	</body>
</html>