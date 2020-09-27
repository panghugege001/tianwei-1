<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/accountHistory.jsp");
	}
%>
<!DOCTYPE >
<html>
<head>
	<jsp:include page="commons/back.jsp" />
	<link rel="stylesheet" type="text/css" href="mobile/css/agent.css?v=9" />
	
</head>
<body>
	<div class="tab-bd">
		<div id="page-index" data-page-index class="tab-panel active">	
			<div class="main-wrap">
				<div class="header-margin"></div>
				<div class="content">
					<ul>
						<li>
							<div class="title" id="agentHistory-ptcommission"><a data-toggle="tab" href="#agent-yj" aria-expanded="false">佣金列表</a></div>
						</li>
						<li>
							<div class="title" id="agentHistory-subuser"><a data-toggle="tab" href="#agent-hylb" aria-expanded="false">会员列表</a></div>
						</li>
						<li>
							<div class="title" id="agentHistory-proposal"><a data-toggle="tab" href="#agent-hyzw" aria-expanded="false">会员帐务</a></div>
						</li>
						<li>
							<div class="title" id="agentHistory-creditLog"><a data-toggle="tab" href="#agent-record" aria-expanded="false">额度记录</a></div>
						</li>
					</ul>
				</div>
				<div class="footer-margin"></div>
			</div>
		</div>
		<!--佣金列表-->
		<div id="agent-yj" class="tab-panel page record" >
			<div class="space-3"></div>
			<!--日结佣金 -->
			<div class="search-wrap mui-col-xs32-12 mui-col-xs64-6">
				<div class="mui-textfield date-select mui-col-xs32-6">
					<input id="ptcommission-startDate" type="text" mui-date-format="date" readonly="readonly">
					<label>起始时间</label>
				</div>
				<div class="mui-textfield date-select mui-col-xs32-6">
					<input id="ptcommission-endDate" type="text" mui-date-format="date" readonly="readonly">
					<label>结束时间</label>
				</div>
			</div>
			<div class="mui-col-xs32-12 mui-col-xs64-6">
				<div class="form">
					<div class="row">
						<div class="label">总日佣金：</div>
						<div class="text"><span id="ptcommission-tXimafee">0</span>  元</div>
					</div>
				</div>
				<div class="mui-buttons">
					<div id="ptcommission-search" class="mui-btn mui-btn--raised mui-btn--primary" >
						查询
					</div>
				</div>
			</div>
			<div id="ptcommission-grid"></div>
		</div>	
		<!--会员列表-->
		 <div id="agent-hylb" class="tab-panel page record" >
			<div class="space-3"></div>
			<!--下线会员 -->
			<div class="search-wrap mui-col-xs32-12 mui-col-xs64-6">
				<div class="mui-textfield date-select mui-col-xs32-6">
					<input id="subuser-startDate" type="text" mui-date-format="date" readonly="readonly">
					<label>起始时间</label>
				</div>
				<div class="mui-textfield date-select mui-col-xs32-6">
					<input id="subuser-endDate" type="text" mui-date-format="date" readonly="readonly">
					<label>结束时间</label>
				</div>
			</div>
			<div class="mui-col-xs32-12 mui-col-xs64-6">
				<div class="mui-buttons">
					<div id="subuser-search" class="mui-btn mui-btn--raised mui-btn--primary" >
						查询
					</div>
				</div>
			</div>
			<div id="subuser-grid"></div>
		</div>
		<!--会员账务-->
		<div id="agent-hyzw" class="tab-panel page record" >
			<div class="space-3"></div>
			<!--下线提案 -->
			<div class="search-wrap mui-col-xs32-12 mui-col-xs64-6">
				<div class="mui-textfield date-select mui-col-xs32-6">
					<input id="proposal-startDate" type="text" mui-date-format="date" readonly="readonly">
					<label>起始时间</label>
				</div>
				<div class="mui-textfield date-select mui-col-xs32-6">
					<input id="proposal-endDate" type="text" mui-date-format="date" readonly="readonly">
					<label>结束时间</label>
				</div>
				<div class="mui-textfield mui-textfield--float-label mui-col-xs32-6">
					<input id="proposal-account" type="text" >
					<label>会员帐号</label>
				</div>
				<div class="mui-select mui-col-xs32-6">
					<div id="proposal-type"> </div>
					<label>存款类型</label>
				</div>
			</div>
			<div class="mui-col-xs32-12 mui-col-xs64-6">
				<div class="form">
					<div class="row">
						<div class="label">总额度：</div>
						<div class="text"><span id="proposal-tMoney">0</span>  元</div>
					</div>
					<div class="row">
						<div class="label">总红利：</div>
						<div class="text" ><span id="proposal-tGifTamount">0</span> 元</div>
					</div>
				</div>
				<div class="mui-buttons">
					<div id="proposal-search" class="mui-btn mui-btn--raised mui-btn--primary" >
						查询
					</div>
				</div>
			</div>
			<div id="proposal-grid"></div>
		</div>
		<!--额度记录-->
			<div id="agent-record" class="tab-panel page record" >
				<div class="space-3"></div>
				<!--额度记录 -->
				<div class="search-wrap mui-col-xs32-12 mui-col-xs64-6">
					<div class="mui-textfield date-select mui-col-xs32-6">
						<input id="creditLog-startDate" type="text" mui-date-format="date" readonly="readonly">
						<label>起始时间</label>
					</div>
					<div class="mui-textfield date-select mui-col-xs32-6">
						<input id="creditLog-endDate" type="text" mui-date-format="date" readonly="readonly">
						<label>结束时间</label>
					</div>
				</div>
				<div class=" mui-col-xs32-12 mui-col-xs64-6">
					<div class="mui-buttons">
						<div class="mui-btn mui-btn--raised mui-btn--primary" id="creditLog-search">
							查询
						</div>
					</div>
				</div>
				<div id="creditLog-grid"></div>
			</div>
	  </div>		
	<jsp:include page="commons/footer1.jsp" />
	<script type="text/javascript">
		headerBar.setTitle('账户清单');
		footerBar.active('agentHistory');
		
		//设定日期套件开启
		$('.mui-textfield.date-select').click(function(){
        	mui.datepicker.open($(this).find('input'));
		});
		$('.mui-textfield.date-select').append('<span class="date-icon flaticon-weekly3"></span>');
	
		var agentManage = mobileManage.getAgentManage();

		var $titles = $('.main-wrap>.content>ul>li>.title');

		$titles.bind('click',_titleClickEvent);
		
		function _titleClickEvent(){
			var $li = $(this).parent();
			if($li.hasClass('active')){
				$li.removeClass('active');
			}else{
				$titles.parent().removeClass('active');
				$li.addClass('active');
				$('body').scrollTop(0)
				$('body').scrollTop($li.position().top-50)
			}
			
			if(!agentManage[this.id]){
				switch(this.id){
					case 'agentHistory-ptcommission':
						agentManage[this.id] = new PtCommission();
						break;
					case 'agentHistory-creditLog':
						agentManage[this.id] = new CreditLog();
						break;
					case 'agentHistory-subuser':
						agentManage[this.id] = new Subuser();
						break;
					case 'agentHistory-proposal':
						agentManage[this.id] = new Proposal();
						break;
					case 'agentHistory-platform':
						agentManage[this.id] = new Platform();
						break;
					case 'agentHistory-commission':
						agentManage[this.id] = new Commission();
						break;
					case 'agentHistory-betProfit':
						agentManage[this.id] = new BetProfit();
						break;
					default:
				}
			}
		}
		

		//额度记录
		function CreditLog(){
			var that = this;
			
			that.search = $('#creditLog-search');
			that.startDate = $('#creditLog-startDate');
			that.endDate = $('#creditLog-endDate');
			
			that.grid = new MobileGrid({
				appendId:'creditLog-grid',
				dataUrl:'mobi/queryCreditlogs.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'type',name:'操作类型',align:'center'}),
						new MobileGridColumn({value:'remit',name:'额度变量',align:'center',
							onRender:function(record,value){
			                	 return '<div style="text-align:right">'+value+'</div>';
							}
						}),
						new MobileGridColumn({value:'credit',name:'改变前额度',align:'center',
							onRender:function(record,value){
			                	 return '<div style="text-align:right">'+value+'</div>';
							}
						}),
						new MobileGridColumn({value:'newCredit',name:'改变后额度',align:'center',
							onRender:function(record,value){
			                	 return '<div style="text-align:right">'+value+'</div>';
							}
						}),
						new MobileGridColumn({value:'tempCreateTime',name:'加入时间',align:'center',
							onRender:function(record,value){
			                	 return '<div style="text-align:right">'+value+'</div>';
							}
						})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
		        		'startDate':that.startDate.val()+' 00:00:00',
		            	'endDate':that.endDate.val()+' 00:00:00'
					};
				}
			});
			
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.endDate.val(nowDate.format('Y-m-d'));
				nowDate.setDate(nowDate.getDate()-1);
				that.startDate.val(nowDate.format('Y-m-d'));
				
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}

			_initEvent();
			_initValue();
		}
		
		//会员列表
		function Subuser(){
			var that = this;
			
			that.search = $('#subuser-search');
			that.startDate = $('#subuser-startDate');
			that.endDate = $('#subuser-endDate');
			
			that.grid = new MobileGrid({
				appendId:'subuser-grid',
				dataUrl:'mobi/queryAgentSubUserInfo.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'loginname',name:'会员帐号',align:'center'}),
						new MobileGridColumn({value:'flag',name:'状态',align:'center'}),
						new MobileGridColumn({value:'credit',name:'账户额度',align:'center'}),
						new MobileGridColumn({value:'howToKnow',name:'来源网址',align:'center',
							onRender:function(record,value){
			                	 return '<div style="text-align:left">'+value+'</div>';
							}
						})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
 					if(!result.success){
 						alert(result.message);
 					}else{
 						$('#ptcommission-tXimafee').html(result.data.tXimafee||0);
 					}
				},
				getParam:function(){
					return {
		        		'startDate':that.startDate.val()+' 00:00:00',
		            	'endDate':that.endDate.val()+' 00:00:00'
					};
				}
			});
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.endDate.val(nowDate.format('Y-m-d'));
				nowDate.setDate(nowDate.getDate()-1);
				that.startDate.val(nowDate.format('Y-m-d'));
				
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}

			_initEvent();
			_initValue();
		}
		
		//下线提案
		function Proposal(){
			var that = this;
			
			that.search = $('#proposal-search');
			that.startDate = $('#proposal-startDate');
			that.endDate = $('#proposal-endDate');
			that.account = $('#proposal-account');
			that.type = new MobileComboBox({
				appendId:'proposal-type',
				cls:'',
				valueName:'value',
				displayName:'name',
				datas:[
					{value:'502',name:'存款'},
					{value:'1000',name:'在线支付'}
				],
				onChange:function(e){
					
				}
			});
			
			//查询存款类型
// 			agentManage.getProposalType({},function(result){
// 				if(result.success){
// 					that.type.loadData(result.data);
// 				}else{
// 					alert(result.message)
// 				}
// 			});
			
			that.grid = new MobileGrid({
				appendId:'proposal-grid',
				dataUrl:'mobi/querySubProposal.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'loginname',name:'会员帐号',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'创建时间',align:'center',
							onRender:function(record,value){
			                	 return value?new Date(value).format('Y-m-d  h:i:s'):value;
							}
						}),
						new MobileGridColumn({value:'money',name:'额度',align:'center'}),
						new MobileGridColumn({value:'type',name:'提案类型',align:'center'})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
 					if(!result.success){
 						alert(result.message);
 					}else{
 						$('#proposal-tMoney').html(result.data.tMoney||0);
 						$('#proposal-tGifTamount').html(result.data.tGifTamount||0);
 					}
				},
				getParam:function(){
					return {
		        		'startDate':that.startDate.val()+' 00:00:00',
		            	'endDate':that.endDate.val()+' 00:00:00',
						'account':that.account.val(),
						'proposalType':that.type.getValue()
					};
				}
			});
			
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.endDate.val(nowDate.format('Y-m-d'));
				nowDate.setDate(nowDate.getDate()-1);
				that.startDate.val(nowDate.format('Y-m-d'));
				
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}

			_initEvent();
			_initValue();
		}
		
		//会员输赢
		function Platform(){
			var that = this;
			that.platformNames = {
				'ea':'EA',
				'bbin':'BBIN',
				'kg':'KG快乐彩',
				'newpt':'PT',
				'sb':'体育平台',
				'sixlottery':'六合彩',
				'ttg':'TTG',
				'ebet':'EBET',
				'gpi':'GPI',
				'jc':'时时彩'
			};
			
			that.search = $('#platform-search');
			that.startDate = $('#platform-startDate');
			that.endDate = $('#platform-endDate');
			that.account = $('#platform-account');
			that.game = new MobileComboBox({
				appendId:'platform-game',
				cls:'',
				valueName:'value',
				displayName:'name',
				datas:[
					{value:'',name:'全部'},
					{value:'ea',name:'EA'},
					{value:'bbin',name:'BBIN'},
					{value:'kg',name:'KG快乐彩'},
					{value:'sb',name:'体育平台'},
					{value:'newpt',name:'PT'},
					{value:'ttg',name:'TTG'},
					{value:'gpi',name:'GPI'},
					{value:'ebet',name:'EBET'},
					{value:'sixlottery',name:'六合彩'},
					{value:'jc',name:'时时彩'}
				],
				onChange:function(e){
					
				}
			});
			
			that.grid = new MobileGrid({
				appendId:'platform-grid',
				dataUrl:'mobi/queryPlatformDetails.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'loginname',name:'会员帐号',align:'center'}),
						new MobileGridColumn({value:'platform',name:'平台',align:'center'}),
						new MobileGridColumn({value:'bettotal',name:'投注总额',align:'center'}),
						new MobileGridColumn({value:'amount',name:'输赢值',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'创建时间',align:'center',
							onRender:function(record,value){
			                	 return value?new Date(value).format('Y-m-d  h:i:s'):value;
							}
						})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
 					if(!result.success){
 						alert(result.message);
 					}else{

 						$('#platform-tAmount').html(result.data.tAmount||0);
 						$('#platform-tBettotal').html(result.data.tBettotal||0);
 					}
				},
				getParam:function(){
					return {
		        		'startDate':that.startDate.val()+' 00:00:00',
		            	'endDate':that.endDate.val()+' 00:00:00',
						'account':that.account.val(),
						'platform':that.game.getValue()
					};
				}
			});
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.endDate.val(nowDate.format('Y-m-d'));
				nowDate.setDate(nowDate.getDate()-1);
				that.startDate.val(nowDate.format('Y-m-d'));
				
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}

			_initEvent();
			_initValue();
		}
		


		//实时输赢
		function BetProfit(){
			var that = this;
			that.platformNames = {
				'ea':'EA',
				'bbin':'BBIN',
				'kg':'KG快乐彩',
				'newpt':'PT',
				'sb':'体育平台',
				'sixlottery':'六合彩',
				'ttg':'TTG',
				'ebet':'EBET',
				'gpi':'GPI',
				'jc':'时时彩'
			};

			that.search = $('#betProfit-search');
			that.startDate = $('#betProfit-startDate');
			that.endDate = $('#betProfit-endDate');
			that.account = $('#betProfit-account');
			that.game = new MobileComboBox({
				appendId:'betProfit-game',
				cls:'',
				valueName:'value',
				displayName:'name',
				datas:[
					{value:'',name:'全部'},
					{value:'ea',name:'EA'},
					{value:'bbin',name:'BBIN'},
					{value:'kg',name:'KG快乐彩'},
					{value:'sb',name:'体育平台'},
					{value:'newpt',name:'PT'},
					{value:'ttg',name:'TTG'},
					{value:'gpi',name:'GPI'},
					{value:'ebet',name:'EBET'},
					{value:'sixlottery',name:'六合彩'},
					{value:'jc',name:'时时彩'}
				],
				onChange:function(e){
					
				}
			});
			
			that.grid = new MobileGrid({
				appendId:'betProfit-grid',
				dataUrl:'mobi/queryAgentBetProfit.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'loginname',name:'会员帐号',align:'center'}),
						new MobileGridColumn({value:'platform',name:'平台',align:'center'}),
						new MobileGridColumn({value:'bettotal',name:'投注总额',align:'center'}),
						new MobileGridColumn({value:'profit',name:'输赢值',align:'center'}),
						new MobileGridColumn({value:'tempStarttime',name:'起始时间',align:'center',
							onRender:function(record,value){
			                	 return value?new Date(value).format('Y-m-d  h:i:s'):value;
							}
						}),
						new MobileGridColumn({value:'tempEndtime',name:'结束时间',align:'center',
							onRender:function(record,value){
			                	 return value?new Date(value).format('Y-m-d  h:i:s'):value;
							}
						})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
 					if(!result.success){
 						alert(result.message);
 					}else{
 						$('#betProfit-tbet').html(result.data.tbet||0);
 						$('#betProfit-tProfit').html(result.data.tProfit||0);
 					}
				},
				getParam:function(){
					return {
		        		'startDate':that.startDate.val()+' 00:00:00',
		            	'endDate':that.endDate.val()+' 00:00:00',
						'account':that.account.val(),
						'platform':that.game.getValue()
					};
				}
			});
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.endDate.val(nowDate.format('Y-m-d'));
				nowDate.setDate(nowDate.getDate()-1);
				that.startDate.val(nowDate.format('Y-m-d'));
				
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}
			
			_initEvent();
			_initValue();
		}

		//佣金列表grid
		function Commission(){
			var that = this;
			
			that.search = $('#commission-search');
			that.yearmonth = $('#commission-yearmonth');
			
			that.grid = new MobileGrid({
				appendId:'commission-grid',
				dataUrl:'mobi/queryAgentCommission.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'yearmonth',name:'年月',align:'center'}),
						new MobileGridColumn({value:'loginname',name:'会员帐号',align:'center'}),
						new MobileGridColumn({value:'ximaAmount',name:'洗码优惠',align:'center'}),
						new MobileGridColumn({value:'firstDepositAmount',name:'首存优惠',align:'center'}),
						new MobileGridColumn({value:'agAmount',name:'输赢值',align:'center'}),
						new MobileGridColumn({value:'otherAmount',name:'其他优惠',align:'center'}),
						new MobileGridColumn({value:'platform',name:'备注',align:'center'})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}else{
						$('#commission-tXima').html(result.data.tXima||0);
						$('#commission-tAg').html(result.data.tAg||0);
						$('#commission-tOther').html(result.data.tOther||0);
					}
				},
				getParam:function(){
					return {
		        		'yearmonth':that.yearmonth.val()
					};
				}
			});
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.yearmonth.val(nowDate.format('Y-m'));
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}

			_initEvent();
			_initValue();
		}

		//日结佣金会员grid
		function PtCommission(){
			var that = this;
			
			that.search = $('#ptcommission-search');
			that.startDate = $('#ptcommission-startDate');
			that.endDate = $('#ptcommission-endDate');
			
			that.grid = new MobileGrid({
				appendId:'ptcommission-grid',
				dataUrl:'mobi/queryPtCommission.aspx',
				showIndex:true,
				showMore:false,
				showRefresh:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'platform',name:'平台',align:'center',
							onRender:function(record,value){
			                	 return value=='slotmachine'?'老虎机佣金':value=='liveall'?'其他佣金':value;
							}
						}),
						new MobileGridColumn({value:'amount',name:'日佣金',align:'center'}),
						new MobileGridColumn({value:'createdate',name:'数据日期',align:'center',
							onRender:function(record,value){
								return value?new Date(value).format('Y-m-d  h:i:s'):value;
							}
						})
					],
					contentColumns:[
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
					$('#ptcommission-tXimafee').html(result.data.tXimafee||0);
				},
				getParam:function(){
					return {
		        		'startDate':that.startDate.val()+' 00:00:00',
		            	'endDate':that.endDate.val()+' 00:00:00'
					};
				}
			});
			
			//执行查询
			function _queryDetails(){
				that.grid.load();
			}
			
			//初始化值
			function _initValue(){
				var nowDate = new Date();
				that.endDate.val(nowDate.format('Y-m-d'));
				nowDate.setDate(nowDate.getDate()-1);
				that.startDate.val(nowDate.format('Y-m-d'));
				
				nowDate = null;
			}
			
			//宣告事件
			function _initEvent(){
				that.search.click(_queryDetails);
				that.grid.init();
			}

			_initEvent();
			_initValue();
		}
	</script>
</body>
</html>