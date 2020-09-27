t<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}
%>
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/selfGet.css?v=9" />
		
	</head>
<body>
	<div class="tab-bd">
		<div id="page-index" data-page-index class="tab-panel active">	
			<div class="main-wrap">
				<div class="header-margin"></div>
				<div class="content">
					<ul>
						<li>
							<div class="title" id="accountHistory-deposit"><a data-toggle="tab" href="#record-ck" aria-expanded="false">在线存款记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-transfer"><a data-toggle="tab" href="#record-zz" aria-expanded="false">户内转账记录</a></div>							
						</li>
						<li>
							<div class="title" id="accountHistory-cashin"><a data-toggle="tab" href="#record-sgck" aria-expanded="false">手工存款记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-withdraw"><a data-toggle="tab" href="#record-tk" aria-expanded="false">提款记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-concessionReccords"><a data-toggle="tab" href="#record-yh" aria-expanded="false">优惠活动记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-couponRecords"><a data-toggle="tab" href="#record-yhj" aria-expanded="false">优惠卷记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-ximaDetail"><a data-toggle="tab" href="#record-fs" aria-expanded="false">自助返水记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-depositOrderRecord"><a data-toggle="tab" href="#record-fy" aria-expanded="false">存款附言记录</a></div>
						</li>

						<li>
							<div class="title" id="accountHistory-hongbao">
								<a data-toggle="tab" href="#record-hb" aria-expanded="false">红包雨记录</a></div>
						</li>


						<%--<li>
							<div class="title" id="accountHistory-point"><a data-toggle="tab" href="#record-jf" aria-expanded="false">积分记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-friend"><a data-toggle="tab" href="#record-hy" aria-expanded="false">好友推荐记录</a></div>
						</li>
						<li>
							<div class="title" id="accountHistory-lottery"><a data-toggle="tab" href="#record-zp" aria-expanded="false">幸运转盘记录</a></div>
						</li>--%>
					</ul>
				</div>
				<div class="footer-margin" ></div>
			</div>
		</div>
		<!--在线存款记录-->
		<div id="record-ck" class="page tab-panel record" >
			<div id="deposit-grid"></div>
		</div>	
		<!--户内转账记录-->
		<div id="record-zz" class="page tab-panel record" >
			<div id="transfer-grid"></div>
		</div>
		<!--手工存款记录-->	
		<div id="record-sgck" class="page tab-panel record" >
			<div id="cashin-grid"></div>
		</div>
		<!--提款记录-->	
		<div id="record-tk" class="page tab-panel record" >
			<div id="withdraw-grid"></div>
		</div>
		<!--优惠活动记录-->	
		<div id="record-yh" class="page tab-panel record" >
			<div id="concessionReccords-grid"></div>
		</div>
		<!--优惠卷记录-->	
		<div id="record-yhj" class="page tab-panel record" >
			<div id="couponRecords-grid"></div>
		</div>
		<!--自助返水记录-->	
		<div id="record-fs" class="page tab-panel record" >
			<div id="ximaDetail-grid"></div>
		</div>
		<!--存款附言记录-->	
		<div id="record-fy" class="page tab-panel record" >
			<div id="depositOrderRecord-grid"></div>
		</div>
		<!--红包雨记录-->
		<div id="record-hb" class="page tab-panel record" >
			<div id="hongbao-grid"></div>
		</div>
		<!--积分记录-->	
		<%--<div id="record-jf" class="page tab-panel record" >
			<div id="point-grid"></div>
		</div>--%>
		<!--好友推荐记录-->	
		<div id="record-hy" class="page tab-panel record" >
			<div class="ui-form">
				<div class="ui-input-row zf-sele">
					<label class="ui-label">记录类型：</label>	
					<div id="friend-type"> </div>
				 
				</div>
			</div>
			<div id="friend-grid"></div>
		</div>
		<!--幸运转盘记录-->	
		<%--<div id="record-zp" class="page tab-panel record" >
			<div id="lottery-grid"></div>
		</div>--%>
	</div>
	<jsp:include page="commons/footer1.jsp" />
	<script type="text/javascript">
		headerBar.setTitle('账户清单');
		footerBar.active('accountHistory');
		
		new HistoryPage();
		function HistoryPage(){
			var that = this;
			that.grids = {};
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
				
				if(!that.grids[this.id]){
					that.grids[this.id] = true;
					switch(this.id){
						case 'accountHistory-deposit':
							that.depositGrid.init();
							that.depositGrid.load();
							break;
						case 'accountHistory-cashin':
							that.cashinGrid.init();
							that.cashinGrid.load();
							break;
						case 'accountHistory-withdraw':
							that.withdrawGrid.init();
							that.withdrawGrid.load();
							break;
						case 'accountHistory-depositOrderRecord':
							that.depositOrderRecordGrid.init();
							that.depositOrderRecordGrid.load();
							break;
						case 'accountHistory-transfer':
							that.transferGrid.init();
							that.transferGrid.load();
							break;
						case 'accountHistory-ximaDetail':
							that.ximaDetailGrid.init();
							that.ximaDetailGrid.load();
							break;
						case 'accountHistory-concessionReccords':
							that.concessionReccordsGrid.init();
							that.concessionReccordsGrid.load();
							break;
						case 'accountHistory-couponRecords':
							that.couponRecordsGrid.init();
							that.couponRecordsGrid.load();
							break;
						case 'accountHistory-point':
							that.pointGrid.init();
							that.pointGrid.load();
							break;
						case 'accountHistory-friend':
							that.friendGrid.init();
							that.friendGrid.load();
							break;
						case 'accountHistory-lottery':
							that.lotteryGrid.init();
							that.lotteryGrid.load();
							break;
                        case 'accountHistory-hongbao':
                            that.hongbaoGrid.init();
                            that.hongbaoGrid.load();
                            break;
						default:
					}
				}
			}
			
			//在线存款Grid
			that.depositGrid = new MobileGrid({
				appendId:'deposit-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'billno',name:'编号',align:'center'}),
						new MobileGridColumn({value:'money',name:'存款金额',align:'center'}),
						new MobileGridColumn({value:'flag',name:'状态',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'存款时间',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}
						})
					],
					contentColumns:[
						new MobileGridColumn({value:'billno',name:'编号：'}),
						new MobileGridColumn({value:'money',name:'存款金额：'}),
						new MobileGridColumn({value:'loginname',name:'存款帐号：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'deposit'
					};
				}
			});
			
			//在线存款Grid
			that.cashinGrid = new MobileGrid({
				appendId:'cashin-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'amount',name:'存款金额',align:'center'}),
						new MobileGridColumn({value:'flag',name:'状态',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'存款时间',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}
						})
					],
					contentColumns:[
						new MobileGridColumn({value:'pno',name:'编号：'}),
						new MobileGridColumn({value:'amount',name:'存款金额：'}),
						new MobileGridColumn({value:'loginname',name:'存款帐号：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'cashin'
					};
				}
			});
			
			//提款Grid
			that.withdrawGrid = new MobileGrid({
				appendId:'withdraw-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'pno',name:'编号',align:'center'}),
						new MobileGridColumn({value:'amount',name:'提款金额',align:'center'}),
						new MobileGridColumn({value:'flag',name:'状态',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'提款时间',align:'center',
							onRender:function(record,value){
								return value?value.substring(0,10):'';
							}
						})
					],
					contentColumns:[
						new MobileGridColumn({value:'pno',name:'编号：'}),
						new MobileGridColumn({value:'amount',name:'提款金额：'}),
						new MobileGridColumn({value:'loginname',name:'提款账号：'}),
						new MobileGridColumn({value:'flag',name:'状态：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'withdraw'
					};
				}
			});
			
			//轉帳Grid
			that.transferGrid = new MobileGrid({
				appendId:'transfer-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'id',name:'编号',align:'center'}),
						new MobileGridColumn({value:'remit',name:'转账金额',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'转账时间',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}
						})
					],
					contentColumns:[
						new MobileGridColumn({value:'id',name:'编号：'}),
						new MobileGridColumn({value:'remit',name:'转账金额：'}),
						new MobileGridColumn({value:'loginname',name:'转账账号：'}),
						new MobileGridColumn({value:'remark',name:'状态：'}),
						new MobileGridColumn({value:'source',name:'转账类型：',
							onRender:function(record,value){
								if(value=='longdu'){ 
									return '从天威账户转至'+record['target']+'账户';
								}else if(value == 'REDRAIN'&&record['target'] != 'USER'){
                                    return '红包雨转入游戏平台';
                                } else if(record['target'] == 'REDRAIN'){
                                    return '主账户转到红包雨账户';
                                }else if(record['target'] == 'USER'){
                                    return '红包雨转给好友';
                                }else if(value == 'TOUSER'){
                                    return '收到好友转来红包';
                                }else{
									return '从'+value+'转至天威账户';
								}
							}
						})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'transfer'
					};
				}
			});
			
			//附言存款Grid
			that.depositOrderRecordGrid = new MobileGrid({
				appendId:'depositOrderRecord-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'depositId',name:'附言',width:'10%',align:'center'}),
						new MobileGridColumn({value:'bankname',name:'银行',width:'15%',align:'center'}),
						new MobileGridColumn({value:'status',name:'状态',width:'10%',align:'center',onRender:function(record,value){
							if(value==0){
								return '未处理';
							}else if(value==1){
								return '已处理';
							}
						}}),
						new MobileGridColumn({value:'tempCreateTime',name:'创建时间',width:'20%',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}})
					],
					contentColumns:[
						new MobileGridColumn({value:'depositId',name:'附言：'}),
						new MobileGridColumn({value:'bankname',name:'银行：'}),
						new MobileGridColumn({value:'accountname',name:'账号名：'}),
						new MobileGridColumn({value:'remark',name:'账号：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'depositOrderRecord'
					};
				}
			});
	
			//优惠活动记录
			that.concessionReccordsGrid = new MobileGrid({
				appendId:'concessionReccords-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'type',name:'优惠类型',align:'center'}),
						new MobileGridColumn({value:'amount',name:'赠送金额',align:'center'}),
						new MobileGridColumn({value:'tempCreateTime',name:'创建时间',align:'center'
							,onRender:function(record,value){
								return value?value.substring(0,10):'';
							}})
					],
					contentColumns:[
						new MobileGridColumn({value:'pno',name:'编号：'}),
						new MobileGridColumn({value:'loginname',name:'帐户：'}),
						new MobileGridColumn({value:'remark',name:'备注：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'concessionReccords'
					};
				}
			});
			
	
			//优惠卷记录
			that.couponRecordsGrid = new MobileGrid({
				appendId:'couponRecords-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'gifTamount',name:'赠送',align:'center'}),
						new MobileGridColumn({value:'amount',name:'存款',align:'center'})
					],
					contentColumns:[
						new MobileGridColumn({value:'pno',name:'编号：'}),
						new MobileGridColumn({value:'type',name:'类型：'}),
						new MobileGridColumn({value:'remark',name:'转账：'}),
						new MobileGridColumn({value:'betMultiples',name:'倍数：'}),
						new MobileGridColumn({value:'shippingCode',name:'代码：'}),
						new MobileGridColumn({value:'tempCreateTime',name:'执行时间：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'couponRecords'
					};
				}
			});
			
	
			//自助返水记录
			that.ximaDetailGrid = new MobileGrid({
				appendId:'ximaDetail-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'validAmount',name:'有效投注额',align:'center'}),
						new MobileGridColumn({value:'ximaAmount',name:'结算金额',align:'center'}),
						new MobileGridColumn({value:'ximaStatus',name:'状态',align:'center'})
					],
					contentColumns:[
						new MobileGridColumn({value:'pno',name:'编号：'}),
						new MobileGridColumn({value:'ximaType',name:'洗码类型：'}),
						new MobileGridColumn({value:'rate',name:'洗码率：'}),
						new MobileGridColumn({value:'statisticsTimeRange',name:'统计时间段：'})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'ximaDetail'
					};
				}
			});
			
			//积分Grid
			that.pointGrid = new MobileGrid({
				appendId:'point-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				showMore:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'points',name:'积分值',align:'center'}),
						new MobileGridColumn({value:'type',name:'积分类型',align:'center'}),
						new MobileGridColumn({value:'createday',name:'时间',align:'center'})
					]
				},
				onLoad:function(result){

					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'point'
					};
				}
			});
			

			if($('#friend-type').length>0){
				that.$friendType = new MobileComboBox({
					appendId:'friend-type',
					valueName:'value',
					displayName:'name',
					datas:[
						{value:'0',name:'推荐注册成功玩家'},
						{value:'1',name:'推荐奖金收入'},
						{value:'2',name:'推荐奖金支出'}
					],
					onChange:function(e){
						that.friendGrid.load();
					}
				});
			}

			//红包雨Grid
            that.hongbaoGrid = new MobileGrid({
                appendId:'hongbao-grid',
                dataUrl:'${ctx}/redrain/queryRedRainCount.aspx',
                showIndex:true,
                showMore:false,
                columnModel:{
                    recordColumns:[
                        new MobileGridColumn({value:'typeId',name:'标题',align:'center'}),
                        new MobileGridColumn({value:'times',name:'个数',align:'center'}),
                        new MobileGridColumn({value:'money',name:'金额',align:'center'}),
                        new MobileGridColumn({value:'date',name:'活动时间',align:'center',
                            onRender:function(record,value){
                                return value?new Date(value).format('Y-m-d'):value;
                            }
                        })
                    ]
                },
                onLoad:function(result){
//                    pageIndex:1
//                    size:10
//                    total:00.0
					console.log(result)
					return {
					    data:{
                            records:result
						}
					}
//                    if(!result.success){
//                        alert(result.message);
//                    }
                }
            });

			//好友推荐Grid
			that.friendGrid = new MobileGrid({
				appendId:'friend-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				showMore:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'downlineuser',name:'玩家账号',align:'center'}),
						new MobileGridColumn({value:'money',name:'金额',align:'center'}),
						new MobileGridColumn({value:'type',name:'类型',align:'center'}),
						new MobileGridColumn({value:'createtime',name:'时间',align:'center',
							onRender:function(record,value){
			                	 return value?new Date(value).format('Y-m-d'):value;
							}
						})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'friend',
						flag:that.$friendType.getValue()
					};
				}
			});
			
			that.lotteryGrid = new MobileGrid({
				appendId:'lottery-grid',
				dataUrl:'${ctx}/mobi/queryHistory.aspx',
				showIndex:true,
				showMore:false,
				columnModel:{
					recordColumns:[
						new MobileGridColumn({value:'itemName',name:'获奖',align:'center'}),
						new MobileGridColumn({value:'winningDate',name:'时间',align:'center',
							onRender:function(record,value){
			                	 return value?new Date(value).format('Y-m-d'):value;
							}
						}),
						new MobileGridColumn({value:'isReceive',name:'状态',align:'center',
							onRender:function(record,value){return value=='1'?'已兑换':'尚未兑换';}})
					]
				},
				onLoad:function(result){
					if(!result.success){
						alert(result.message);
					}
				},
				getParam:function(){
					return {
						historyType:'lottery'
					};
				}
			});
			
		}
	</script>
</body>
</html>