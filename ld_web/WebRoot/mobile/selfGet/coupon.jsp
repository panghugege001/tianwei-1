<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<div class="content mt5">
			<div class="ui-form">
				<div class="ui-input-row zf-sele zf-sele1">
					<label class="ui-label">从天威帐户转帐到：</label>
			  	  	<div id="coupon-platform"></div>
			    	
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">存款金额：</label>	
			  	  	<input id="coupon-money" class="ui-ipt" type="text">
			     
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">优惠代码：</label>	
			  	  	<input id="coupon-code" class="ui-ipt" type="text">
			    	 
			  	</div>
				<div class="ui-button-row center">
					<div class="btn-login block" id="coupon-submit">领取</div>
				</div>
			</div>
			<div class="panel mui-col-xs32-12 mui-col-xs64-6" >
				<div class="h3">100%存送优惠券、88%存送优惠券、68%存送优惠券</div>
				<ol>
	              	<li>1.只限老虎机平台使用，需游戏账户低于5元才能使用存送优惠券，填写红利代码，确认游戏平台，提交后在相关的游戏里面会自动得到优惠礼金。</li>         	 
	              	<li>2.达到流水倍数或游戏账户低于5元即可进行转入转出。</li>
	              	<li>3.优惠券为30天有效期限，逾时未使用恕不进行补发。</li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function CouponPage(){
				var that = this;
				that.platform = $('#coupon-platform');
				that.code = $('#coupon-code');
				that.money = $('#coupon-money');
				that.submit = $('#coupon-submit');
				that.submit.click(_submit);
				if($('#coupon-platform').get(0)){
					that.platform = new MobileComboBox({
						appendId:'coupon-platform',
						cls:'',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'pt',name:'PT'},
							{value:'ttg',name:'TTG'},
							{value:'slot',name:'老虎机账户(SW,MG,DT,PNG,QT,NT)'}
						],
						onChange:function(e){
							
						}
					});
				}
				
				//优惠卷转帐
				function _submit(){
		
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().transferInforCoupon({
						platform:that.platform.getValue(),
						couponCode:that.code.val(),
						couponRemit:that.money.val()
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