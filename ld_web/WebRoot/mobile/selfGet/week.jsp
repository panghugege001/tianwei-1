<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="mobile/js/MobileGrid.js"></script>
</head>
<body>
	<div class="content">
       <table class="data-list" id="week-grid" width="100%"></table>
       
	</div>
	<script type="text/javascript">
		function WeekPage(){
			var that = this;
			//弹窗
			var _$winModel = false;
			//弹窗 html 
			var _winModelHtml = [
					'<div id="mui-self-week-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-8 mui-col-xs64-offset-2 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
			  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text">选择老虎机平台</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
		     		'<div class="mui-panel">',
		     			'<div class="mui-error-message"></div>',
		     		  	'<div class="mui-select">',
		     		    	'<select id="mui-self-week-platform"></select>',
		     		    	'<label>老虎机平台</label>',
		     		 	'</div>',
	     				'<div class="mui-message">注：确定后，我们不接受任何重新转至其他老虎机平台的申请</div>',
		     		  	'<div class="mui-btn mui-btn--raised mui-btn--danger right" id="mui-self-week-cancel">关闭</div>',
		     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary right" id="mui-self-week-submit">领取</div>',
		     		'</div>',
		     	'</div>'
		    ].join('');
			
			that.weekGrid = new MobileGrid({
				appendId:'week-grid',
				dataUrl:'${ctx}/mobi/queryWeekSentReccords.aspx',
				showIndex:true,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'promo',name:'金额',cls:'m-grid-num',width:'30%',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'发放时间',cls:'m-grid-text',width:'30%',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}
						}),
						new MobileGridColumn({value:'status',name:'操作',cls:'m-grid-text',width:'30%',align:'center'
							,onRender:function(record,value){
								if(value==0){
									return '<button class="mui-btn mui-btn--raised mui-btn--danger small week-open" tp-value="'+record.pno+'">领取</button>';
								}
								if(value==1||value==2){
									return '已领取';
								}
								if(value==3){
									return '已取消';
								}
								return '';
							}
						})
					],
					contentColumns:[
						new MobileGridColumn({value:'pno',name:'编号：',cls:'md-r'}),
						new MobileGridColumn({value:'promo',name:'金额：',cls:'mr-n'}),
						new MobileGridColumn({value:'tempCreateTime',name:'发放时间：',cls:'mr-s'}),
						new MobileGridColumn({value:'remark',name:'备注：',cls:'mr-s'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}else{
						$('.week-open').click(_open);
					}
				}
			});
			
			that.weekGrid.init();
			that.weekGrid.load();
			
			
			//开启选择老虎机平台视窗
			function _open(){
				if(!_$winModel){
					initWinModel();
				}
				_$winModel.pno = $(this).attr('tp-value');
				mui.overlay('on',_$winModel[0]);
			}
			
			//关闭选择老虎机平台视窗
			function _close(){
				_$winModel.pno = null;
				mui.overlay('off');
			}
			
			function initWinModel(){
				_$winModel = $(_winModelHtml);
				_$winModel.find('.mui-overlay-close').click(_close);
				_$winModel.find('#mui-self-week-cancel').click(_close);
				_$winModel.find('#mui-self-week-submit').click(_submit);
				
				var data = [
					{value:'pttiger',name:'PT老虎机'},
					{value:'ttg',name:'TTG老虎机'},
					{value:'nt',name:'NT老虎机'},
					{value:'qt',name:'QT老虎机'}
				]
				var optionHtml = '<option value="{0}">{1}</option>';
				var options = new Array();
				
				_$winModel.$platform = _$winModel.find('#mui-self-week-platform');
				for(var i =0;i<data.length;i++){
					options.push(String.format(optionHtml,data[i]['value'],data[i]['name']));
				}
				_$winModel.$platform.append(options);
				data = optionHtml = options = null;
			}
			
			//领取周周回馈
			function _submit(){
				mobileManage.getLoader().open('领取中');
				mobileManage.getSelfGetManage().getWeekSent({
					pno:_$winModel.pno ,
					platform:_$winModel.$platform.val(),
				},function(result){
					mobileManage.getLoader().close();
					_close();
					if(result.success){
						alert(result.message);
					}else{
						alert(result.message);
					}
					that.weekGrid.reload();
				});
			}
		}
	
	</script>
</body>
</html>