<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
	<style>
		.c-red{ margin: 0px !important;}
	</style>
</head>
<body>
	<div class="transfer-content ui-form">
		<div class="ui-input-row zf-sele mt2">
			<label class="ui-label">来源账户：</label>
			<div id="transfer-source"></div>
		</div>
		<div class="ui-input-row black">
			<label class="ui-label">来源余额：</label>
			<div class="text" ><div id="transfer-source-credit" class="c-red"></div><div class="load"></div></div>
		</div>
		<!--<div class="change-button flaticon-exchange1" id="transfer-change"></div>-->
		<div class="ui-input-row zf-sele mt2">
			<label class="ui-label">目标账户：</label>
			<div id="transfer-target"></div>
		</div>
		<div class="ui-input-row black">
			<label class="ui-label">目标余额：</label>
			<div class="text" ><div id="transfer-target-credit" class="c-red"></div><div class="load"></div></div>
		</div>
		<div class="ui-input-row mt2">
			<label class="ui-label">转账金额：</label>
			<div class="text" ><input type="text" id="transfer-money" class="ui-ipt" placeholder="0.00"/></div>
		</div>

		<div class="ui-button-row center">
			<div class="btn-login block" id="transfer-submit">确认</div>
		</div>
		<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-5 tishi">
		<div class="space-2"></div>
		<div class="h3"><strong>温馨提示：</strong></div>
		<ol>
			<li>老虎机账户"支持pt-SWwind、DT、MG、PNG、QT、NT，若要游玩以上平台请将额度转入"老虎机账户"即可。</li>
			<li>请在户内转账前进行平台激活方可转账成功。</li>
			<li>户内转账只支持整数转账。</li>
			<li>进行户内转账时，请先关闭正在进行的游戏页面，避免出现错误。</li>
		</ol>
		<div class="space-2"></div>
		</div>
	</div>
	<script type="text/javascript">
		function TransferPage(){
			var that = this;
			var _sourceAjax ;
			var _targetAjax ;
			//设定只能输入数字
			NumberInput('transfer-money');
			//下拉资料来源
			that.accountData = [
 				{value:'self',name:'天威账户'},
			    {value:'agin',name:'AG国际厅账户'},
			    {value:'newpt',name:'PT账户'},
			    {value:'ttg',name:'TTG账户'},
 			   //  {value:'nt',name:'NT账户'},
			    // {value:'qt',name:'QT账户'},
			    // {value:'dt',name:'DT账户'},
			    // {value:'mg',name:'MG账户'},
			    // {value:'png',name:'PNG账户'},
			    {value:'qd',name:'签到账户'},
			    {value:'sba',name:'沙巴体育账户'},
			    {value:'n2live',name:'N2Live账户'},
			    {value:'mwg',name:'MWG大满贯帐户'},
			    {value:'slot',name:'老虎机账户(SW,MG,DT,PNG,QT,NT)'},

			];


			//目标下拉资料来源
			that.xialaData = [
 				{value:'self',name:'天威账户'},
			    {value:'agin',name:'AG账户'},
			    {value:'newpt',name:'PT账户'},
			    {value:'ttg',name:'TTG账户'},
			    {value:'qd',name:'签到账户'},
			    {value:'sba',name:'沙巴体育账户'},
			    {value:'n2live',name:'N2Live账户'},
			    {value:'mwg',name:'MWG大满贯帐户'},
			    {value:'slot',name:'老虎机账户(SW,MG,DT,PNG,QT,NT)'},

			];

			//来原账户下拉
			that.transferSource = new MobileComboBox({
				appendId:'transfer-source',
				cls:'',
				valueName:'value',
				displayName:'name',
				datas:that.accountData,
				onChange:function(e){
                    var notNeedShowList = $('#m-transfer-target option:not([value="newpt"],[value="ttg"],[value="slot"])');
					if(that.transferSource.getValue()=='self'){
						that.transferTarget.enableAllItem()
						that.transferTarget.disableItemByValue('self');
						that.transferTarget.setValue('mg');
					}else{
						that.transferTarget.disableAllItem()
						that.transferTarget.enableItemByValue('self');
						that.transferTarget.setValue('self');
					}
					if(that.transferSource.getValue()=='qd'){
                        notNeedShowList.hide()
					}else{
                        notNeedShowList.show()
					}
					querySourceAmout(that.transferSource.getValue());
				}
			});
			//目标账户下拉
			that.transferTarget = new MobileComboBox({
				appendId:'transfer-target',
				cls:'',
				valueName:'value',
				displayName:'name',
				datas:that.xialaData,
				onChange:function(e){
					queryTargetAmout(that.transferTarget.getValue());
				}
			});

			that.transferMoney = $('#transfer-money');
			that.transferSubmit = $('#transfer-submit');
			that.transferSubmit.click(doTransger);
			//下拉初始值
			that.transferSource.setValue('self');


			//来源账户与目的账户互换
			/*that.transferChange = $('#transfer-change');
			that.transferChange.click(function(){
				var source = that.transferSource.getValue();
				var target = that.transferTarget.getValue();
				that.transferSource.setValue(target);
				that.transferTarget.setValue(source);
			});*/


			//转账
			function doTransger(){

				var formData = {
					transferGameIn:that.transferTarget.getValue(),
					transferGameOut:that.transferSource.getValue(),
					money:that.transferMoney.val()
				};
//				if(formData.transferGameIn == "newpt" && formData.money < 20){
//					alert("PT转入金额最低20元！");
//					return false;
//				}
				mobileManage.getLoader().open('转账中');
				mobileManage.getBankManage().transfer(formData, function(result){
					if(result.success){
						alert(result.message);
						//更新游戏平台余额
						querySourceAmout(that.transferSource.getValue());
						queryTargetAmout(that.transferTarget.getValue());
						//更新账户余额
						queryCredit();
						that.transferMoney.val('');
					}else{
						alert(result.message);
					}
					mobileManage.getLoader().close();
				});
			}


			//查询平台余额
			function queryCredit(){
				//先查询天威平台余额
				mobileManage.getUserManage().getCredit(
					function(result){
			    		if(result.success){
							$("#credit").html(result.message+" 元");
						}else{
							$("#credit").html('系统繁忙中');
							alert(result.message);
						}
						$('.refre').removeClass('credit-query');
					}
			    );
			}

			//查詢来源余额
			function querySourceAmout(code){
				if(_sourceAjax&&_sourceAjax.readyState!=0){
					_sourceAjax.abort();
				}
				var $credit = $('#transfer-source-credit');
				$credit.html('');
				$credit.next().css('display','block');

				_sourceAjax = $.post('${ctx}/mobi/gameAmount.aspx',{"gameCode":code},function(result){
					$credit.next().css('display','none');
					$credit.html(result.message);
					$credit = null;
				}).fail(function(result) {
					if(result.statusText!='abort'){
						$credit.next().css('display','none');
						$credit.html('系统繁忙中...');
					}
					$credit = null;
				});
			}

			//查詢目标余额
			function queryTargetAmout(code){
				if(_targetAjax&&_targetAjax.readyState!=0){
					_targetAjax.abort();
				}
				var $credit = $('#transfer-target-credit');
				$credit.html('');
				$credit.next().css('display','block');
				_targetAjax = $.post('${ctx}/mobi/gameAmount.aspx',{"gameCode":code},function(result){
					$credit.next().css('display','none');
					$credit.html(result.message);
					$credit = null;
				}).fail(function(result) {
					if(result.statusText!='abort'){
						$credit.next().css('display','none');
						$credit.html('系统繁忙中...');
					}
					$credit = null;
				});
			}

		}
	</script>
</body>
</html>