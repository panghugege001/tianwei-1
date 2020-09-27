<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	
	<div class="content mt5">
		<div class="mui-col-xs32-12 mui-col-xs48-10 mui-col-xs48-offset-1 mui-col-xs64-8 mui-col-xs64-offset-2 ">
			<div class="panel mui-tabs__pane mui--is-active" id="pane-help-month">
				<table id="upgrade-month-grid" width="100%"></table>
			</div>
		</div>
		<div class="ui-button-row center">
			<div class="btn-login block" id="upgrade-submit">检测升级</div>
		</div>
	</div>
	<script type="text/javascript">
		function UpgradePage(){
			var that = this;
		  	that.type = 'month';
			that.upgradeMonthGrid = false;
			
			that.submit = $('#upgrade-submit');
			that.submit.click(_checkUpgrade);
			
		  	initMonthGrid();
		  	
		  	//本月
			function initMonthGrid(){
				that.upgradeMonthGrid = new MobileGrid({
					appendId:'upgrade-month-grid',
					dataUrl:'mobi/queryBetOfPlatform.aspx',
					showIndex:true,
					showMore:false,
					showLoadMore:false,
					columnModel:{
						recordColumns:[
							new MobileGridColumn({value:'platform',name:'游戏平台',cls:'m-grid-text',width:'30%',align:'center'}),
							new MobileGridColumn({value:'bet',name:'投注额',cls:'m-grid-text',width:'30%',align:'center'})
						],
						contentColumns:[
						]
					},
					onLoad:function(result){
						if(!result.success){
							alert(result.message);
						}
					}
				});
				that.upgradeMonthGrid.init();
				that.upgradeMonthGrid.load();
			}
		  	
			
			//检测升级
			function _checkUpgrade(){
				mobileManage.getLoader().open('检测中');
				mobileManage.getUserManage().userUpgrade({helpType:that.type}, function(result){
					if(result.success){
						alert(result.message);
					}else{
						alert(result.message);
					}
					that.upgradeMonthGrid.load();
					mobileManage.getLoader().close();
				});
			}
		}
	</script>
</body>
</html>