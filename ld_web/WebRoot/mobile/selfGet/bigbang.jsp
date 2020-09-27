<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
<body>
	
	<div class="content">
		<div style="font-size:80%;color:red;">注意：请在派发后24小时内领取活动礼金，否则将自动失效</div>
		<table class="data-list" id="bigbang-grid" width="100%"></table>
		
	</div>
	<script type="text/javascript">
		function BigBangPage(){
			var that = this;
			
			that.bigbangGrid = new MobileGrid({
				appendId:'bigbang-grid',
				dataUrl:'${ctx}/mobi/queryPTBigBang.aspx',
				showIndex:false,
				showMore:true,
				showLoadMore:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'giftMoney',name:'活动礼金',cls:'m-grid-text',width:'30%',align:'center'}),
						new MobileGridColumn({value:'tempDistributeTime',name:'派发时间',cls:'m-grid-text',width:'30%',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}
						}),
						new MobileGridColumn({value:'status',name:'操作',cls:'m-grid-text',width:'100%',align:'center'
							,onRender:function(record,value){
								if(value==1){
									return '<a class="submit bigBang-submit" href="javascript:void(0);" tp-value="'+record.id+'">领取</a>';
								}else{
									return '已领取';
								}
							}
						})
					],
					contentColumns:[
						new MobileGridColumn({value:'netWinOrLose',name:'输赢金额：',cls:'m-grid-text',width:'1%',align:'center'}),
						new MobileGridColumn({value:'bonus',name:'扣除红利：',cls:'m-grid-text',width:'1%',align:'center'}),
						new MobileGridColumn({value:'giftMoney',name:'活动礼金：',cls:'m-grid-text',width:'1%',align:'center'}),
						new MobileGridColumn({value:'tempDistributeTime',name:'派发时间：',cls:'m-grid-text',width:'1%',align:'center'}),
						new MobileGridColumn({value:'status',name:'操作：',cls:'m-grid-text',width:'100%',align:'center'
							,onRender:function(record,value){
								if(value==1){
									return '<a class="submit bigBang-submit" href="javascript:void(0);" tp-value="'+record.id+'">领取</a>';
								}else{
									return '已领取';
								}
							}
						})       
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}else{
						$('.bigBang-submit').click(_getPTBigBangBonus);
					}
				}
			});
			that.bigbangGrid.init();
			that.bigbangGrid.load();
			//领取
			function _getPTBigBangBonus(id){
				mobileManage.getLoader().open('执行中...');
				mobileManage.getSelfGetManage().getPTBigBangBonus({
					ptBigBangId:$(this).attr('tp-value'),
				},function(result){
					mobileManage.getLoader().close();
					if(result.success){
						alert(result.message);
					}else{
						alert(result.message);
					}
					that.bigbangGrid.reload();
				});
			}
		}
	
	</script>
</body>
</html>