<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<div class="content mt5">
			<div class="ui-form">
				<div class="ui-input-row">
					<label class="ui-label">优惠代码：</label>	
			  	  	<input id="redCoupon-code" class="ui-ipt" type="text">
			  	</div>
				<div class="ui-input-row zf-sele">
					<label class="ui-label">转帐到：</label>	
			  	  	<div id="redCoupon-platform"></div>
			  	</div>
				<div class="ui-button-row center">
					<div class="btn-login block" id="redCoupon-submit">提交</div>
				</div>
			</div>
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
				<div class="h3 bold">红包优惠说明 </div>
				<ol>
	              	<li>请选择正确的游戏平台。填写红利代码，确认提交， 红利金额会自动添加到您转到的游戏平台里。</li>
					<li>PT/DT/MG/QT/TTG/NT红包优惠券，需PT/DT/MG/QT/TTG/NT游戏账户低于5元才能使用红包优惠券。达到相应的有效投注额要求或PT/DT/MG/QT/TTG/NTT游戏账户低于5元，才能再次进行PT/DT/MG/QT/TTG/NT户内转账。</li>
					<li>红包优惠券有效期为30天，请您在有效期内进行使用。</li>
					<li>如何得到优惠券，请留意天威最新的相关优惠信息。</li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function RedCouponPage(){
				var that = this;
				that.platform = $('#redCoupon-platform');
				that.code = $('#redCoupon-code');
				that.submit = $('#redCoupon-submit');
				
				that.submit.click(_submit);
				
				if($('#redCoupon-platform').get(0)){
					that.platform = new MobileComboBox({
						appendId:'redCoupon-platform',
						cls:'',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'',name:'请选择'}, 
							{value:'ttg',name:'TTG'},
							{value:'pt',name:'PT'}, 
							{value:'slot',name:'老虎机账户(SW,MG,DT,PNG,QT,NT)'}
						],
						onChange:function(e){
							
						}
					});
				}
				
				//优惠卷转帐
				function _submit(){
		
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().transferInforRedCoupon({
						platform:that.platform.getValue(),
						couponCode:that.code.val()
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