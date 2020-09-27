<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/account.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="list-warp">
			<div class="list-group">
				<div class="list-item" data-key="#agent-record"><img src="/mobile/img/icon/money2.png" alt="" />额度记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<!--<div class="list-group">
				<div class="list-item" data-key="#agent-hylb"><img src="/mobile/img/icon/everyday.png" alt="" />下线提案<i class="iconfont fr icon-downjiantou"></i></div>
			</div>-->
			<div class="list-group">
				<div class="list-item" data-key="#agent-hyzw"><img src="/mobile/img/icon/win.png" alt="" />会员帐务<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key="#agent-hylb"><img src="/mobile/img/icon/many.png" alt="" />会员列表<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#agent-yj'><img src="/mobile/img/icon/history2.png" alt="" />佣金列表<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
		</div>
		<!--佣金列表-->
		<div id="agent-yj" class="tab-panel">
			<!--日结佣金 -->
			<div class="date-warp">
				<div class="date-control date-select">
					<input id="ptcommission-startDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div>至</div>
				<div class="date-control date-select">
					<input id="ptcommission-endDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div id="ptcommission-search" class="btn-search">
					查询
				</div>
			</div>
			<div class="money-list">
				<div class="item">
					总日佣金：<span class="c-ylow" id="ptcommission-tXimafee">0</span> 元
				</div>
			</div>
			<div id="ptcommission-grid"></div>
		</div>
		<!--会员列表-->
		<div id="agent-hylb" class="tab-panel">
			<!--下线会员 -->
			<div class="date-warp">
				<div class="date-control date-select">
					<input id="subuser-startDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div>至</div>
				<div class="date-control date-select">
					<input id="subuser-endDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div id="subuser-search" class="btn-search">查询</div>
			</div>
			<div id="subuser-grid"></div>
		</div>
		<!--会员账务-->
		<div id="agent-hyzw" class="tab-panel">
			<div class="filter-warp">
				<div class="item">
					存款类型 <i class="iconfont  icon-downjiantou"></i>
					<ul class="item-info" id="proposal-type">
					</ul>
				</div>
				<div class="item">
					会员帐号 <i class="iconfont  icon-downjiantou"></i>
					<div class="item-info">
						<div class="form-acount">
							<div class="form-control">
								<label>会员账号查询</label>
								<input id="proposal-account" placeholder="请输入会员帐号" type="text">
							</div>
							<div class="btn_wrap">
								<a id="j-resetBtn" href="javascript:;" class="btn btn07">重置</a>
								<a id="j-slideup" href="javascript:;" class="btn btn06">完成</a>
							</div>
						</div>
					</div>
				</div>

			</div>
			<!--下线提案 -->
			<div class="date-warp">
				<div class="date-control date-select">
					<input id="proposal-startDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div>至</div>
				<div class="date-control date-select">
					<input id="proposal-endDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div id="proposal-search" class="btn-search">查询</div>
			</div>

			<div class="money-list">
				<div class="item">
					<span class="label">总额度：</span>
					<span class="c-ylow" id="proposal-tMoney">0</span> 元
				</div>
				<div class="item">
					<span class="label">总红利：</span>
					<span class="c-ylow" id="proposal-tGifTamount">0</span> 元
				</div>
			</div>
			<div id="proposal-grid"></div>
		</div>
		<!--额度记录-->
		<div id="agent-record" class="tab-panel">
			<!--额度记录 -->
			<div class="date-warp">
				<div class="date-control date-select">
					<input id="creditLog-startDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div>至</div>
				<div class="date-control date-select">
					<input id="creditLog-endDate" type="text" mui-date-format="date" readonly="readonly">
				</div>
				<div id="creditLog-search" class="btn-search">查询</div>
			</div>
			<div id="creditLog-grid"></div>
		</div>
		<script type="text/javascript" src="/mobile/js/AgentManage.js"></script>
		<script src="/mobile/js/MobileGrid.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="/mobile/js/lib/Mdate/Mdate.js"></script>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script>
			var agentManage = mobileManage.getAgentManage();
			$('.date-select input').each(function(i, item) {
				 new LazyPicker(item);
			});

			window.header.backFun = function() {
				if(list_warp.is(":hidden")) {
					window.header.setTitle('账户中心')
					$('.tab-panel').removeClass('active')
					list_warp.show();
				} else {
					history.back();
				}
			}
			$("#j-resetBtn").click(function() {
				$("#proposal-account").val('')
			})
			$("#j-slideup").click(function() {
				var thats = this;
				$("#proposal-search").click();
				setTimeout(function() {
					$('body').removeClass('overflow')
					$(thats).closest('.item-info').hide().parent().removeClass('active');
				})
			})

			$(".filter-warp .item").click(function() {
				$(this).addClass('active').siblings().removeClass('active');
				$('body').addClass('overflow')
				$(".filter-warp .item-info").hide();
				$(this).find('.item-info').show()
			})

			$(".filter-warp .item-info").on('click', ' li', function() {
				$(this).addClass('active').siblings().removeClass('active');
				var thats = this;
				setTimeout(function() {
					$('body').removeClass('overflow')
					$(thats).closest('.item-info').hide().parent().removeClass('active');
				})
			})

			var list_warp = $(".list-warp")
			list_warp.find('.list-item').click(function() {
				window.header.setTitle($(this).text())
				var $key = $(this).data("key");
				$($key).addClass('active').siblings().removeClass('active')
				list_warp.hide();
				_titleClickEvent($key)
			})

			function _titleClickEvent(key) {
				if(!agentManage[key]) {
					switch(key) {
						case '#agent-yj':
							agentManage[key] = new PtCommission();
							break;
						case '#agent-record':
							agentManage[key] = new CreditLog();
							break;
						case '#agent-hylb':
							agentManage[key] = new Subuser();
							break;
						case '#agent-hyzw':
							agentManage[key] = new Proposal();
							break;
						case 'agentHistory-platform':
							agentManage[key] = new Platform();
							break;
						case 'agentHistory-commission':
							agentManage[key] = new Commission();
							break;
						case 'agentHistory-betProfit':
							agentManage[key] = new BetProfit();
							break;
						default:
					}
				}
			}

			//额度记录
			function CreditLog() {
				var that = this;

				that.search = $('#creditLog-search');
				that.startDate = $('#creditLog-startDate');
				that.endDate = $('#creditLog-endDate');

				that.grid = new MobileGrid({
					appendId: 'creditLog-grid',
					dataUrl: 'mobi/queryCreditlogs.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'type',
								name: '操作类型',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'remit',
								name: '额度变量',
								align: 'center',
								onRender: function(record, value) {
									return '<div style="text-align:right">' + value + '</div>';
								}
							}),
							new MobileGridColumn({
								value: 'credit',
								name: '改变前额度',
								align: 'center',
								onRender: function(record, value) {
									return '<div style="text-align:right">' + value + '</div>';
								}
							}),
							new MobileGridColumn({
								value: 'newCredit',
								name: '改变后额度',
								align: 'center',
								onRender: function(record, value) {
									return '<div style="text-align:right">' + value + '</div>';
								}
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '加入时间',
								align: 'center',
								onRender: function(record, value) {
									return '<div style="text-align:right">' + value + '</div>';
								}
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							'startDate': that.startDate.val() + ' 00:00:00',
							'endDate': that.endDate.val() + ' 00:00:00'
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.endDate.val(nowDate.format('yyyy-MM-dd'));
					nowDate.setDate(nowDate.getDate() - 1);
					that.startDate.val(nowDate.format('yyyy-MM-dd'));

					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}

			//会员列表
			function Subuser() {
				//				debugger;
				var that = this;
				that.search = $('#subuser-search');
				that.startDate = $('#subuser-startDate');
				that.endDate = $('#subuser-endDate');

				that.grid = new MobileGrid({
					appendId: 'subuser-grid',
					dataUrl: 'mobi/queryAgentSubUserInfo.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'loginname',
								name: '会员帐号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'flag',
								name: '状态',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'credit',
								name: '账户额度',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'howToKnow',
								name: '来源网址',
								align: 'center',
								onRender: function(record, value) {
									return '<div style="text-align:left">' + value + '</div>';
								}
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						} else {
							$('#ptcommission-tXimafee').html(result.data.tXimafee || 0);
						}
					},
					getParam: function() {
						return {
							'startDate': that.startDate.val() + ' 00:00:00',
							'endDate': that.endDate.val() + ' 00:00:00'
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.endDate.val(nowDate.format('yyyy-MM-dd'));
					nowDate.setDate(nowDate.getDate() - 1);
					that.startDate.val(nowDate.format('yyyy-MM-dd'));

					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}
			//下线提案
			function Proposal() {
				var that = this;

				that.search = $('#proposal-search');
				that.startDate = $('#proposal-startDate');
				that.endDate = $('#proposal-endDate');
				that.account = $('#proposal-account');
				that.type = {
					val: function() {
						return $("#proposal-type .active").data('value')
					}
				};

				$.each([{
						value: '502',
						name: '存款'
					},
					{
						value: '1000',
						name: '在线支付'
					}
				], function(i, item) {
					$("#proposal-type").append('<li class="' + (i == 0 ? 'active' : '') + '" data-value="' + item.value + '">' + item.name + '</li>')
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
					appendId: 'proposal-grid',
					dataUrl: 'mobi/querySubProposal.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'loginname',
								name: '会员帐号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '创建时间',
								align: 'center',
								onRender: function(record, value) {
									return value ?value.toString().toDate().format('yyyy-MM-dd hh:mm:ss') : value;
								}
							}),
							new MobileGridColumn({
								value: 'money',
								name: '额度',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'type',
								name: '提案类型',
								align: 'center'
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						} else {
							$('#proposal-tMoney').html(result.data.tMoney || 0);
							$('#proposal-tGifTamount').html(result.data.tGifTamount || 0);
						}
					},
					getParam: function() {
						return {
							'startDate': that.startDate.val() + ' 00:00:00',
							'endDate': that.endDate.val() + ' 00:00:00',
							'account': that.account.val(),
							'proposalType': that.type.val()
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.endDate.val(nowDate.format('yyyy-MM-dd'));
					nowDate.setDate(nowDate.getDate() - 1);
					that.startDate.val(nowDate.format('yyyy-MM-dd'));

					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}

			//会员输赢
			function Platform() {
				var that = this;
				that.platformNames = {
					'ea': 'EA',
					'bbin': 'BBIN',
					'kg': 'KG快乐彩',
					'newpt': 'PT',
					'sb': '体育平台',
					'sixlottery': '六合彩',
					'ttg': 'TTG',
					'ebet': 'EBET',
					'gpi': 'GPI',
					'jc': '时时彩'
				};

				that.search = $('#platform-search');
				that.startDate = $('#platform-startDate');
				that.endDate = $('#platform-endDate');
				that.account = $('#platform-account');
				that.game = new MobileComboBox({
					appendId: 'platform-game',
					cls: '',
					valueName: 'value',
					displayName: 'name',
					datas: [{
							value: '',
							name: '全部'
						},
						{
							value: 'ea',
							name: 'EA'
						},
						{
							value: 'bbin',
							name: 'BBIN'
						},
						{
							value: 'kg',
							name: 'KG快乐彩'
						},
						{
							value: 'sb',
							name: '体育平台'
						},
						{
							value: 'newpt',
							name: 'PT'
						},
						{
							value: 'ttg',
							name: 'TTG'
						},
						{
							value: 'gpi',
							name: 'GPI'
						},
						{
							value: 'ebet',
							name: 'EBET'
						},
						{
							value: 'sixlottery',
							name: '六合彩'
						},
						{
							value: 'jc',
							name: '时时彩'
						}
					],
					onChange: function(e) {

					}
				});

				that.grid = new MobileGrid({
					appendId: 'platform-grid',
					dataUrl: 'mobi/queryPlatformDetails.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'loginname',
								name: '会员帐号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'platform',
								name: '平台',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'bettotal',
								name: '投注总额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '输赢值',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '创建时间',
								align: 'center',
								onRender: function(record, value) {
									return value ?value.toString().toDate().format('yyyy-MM-dd hh:mm:ss') : value;
								}
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						} else {

							$('#platform-tAmount').html(result.data.tAmount || 0);
							$('#platform-tBettotal').html(result.data.tBettotal || 0);
						}
					},
					getParam: function() {
						return {
							'startDate': that.startDate.val() + ' 00:00:00',
							'endDate': that.endDate.val() + ' 00:00:00',
							'account': that.account.val(),
							'platform': that.game.getValue()
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.endDate.val(nowDate.format('yyyy-MM-dd'));
					nowDate.setDate(nowDate.getDate() - 1);
					that.startDate.val(nowDate.format('yyyy-MM-dd'));

					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}

			//实时输赢
			function BetProfit() {
				var that = this;
				that.platformNames = {
					'ea': 'EA',
					'bbin': 'BBIN',
					'kg': 'KG快乐彩',
					'newpt': 'PT',
					'sb': '体育平台',
					'sixlottery': '六合彩',
					'ttg': 'TTG',
					'ebet': 'EBET',
					'gpi': 'GPI',
					'jc': '时时彩'
				};

				that.search = $('#betProfit-search');
				that.startDate = $('#betProfit-startDate');
				that.endDate = $('#betProfit-endDate');
				that.account = $('#betProfit-account');
				that.game = new MobileComboBox({
					appendId: 'betProfit-game',
					cls: '',
					valueName: 'value',
					displayName: 'name',
					datas: [{
							value: '',
							name: '全部'
						},
						{
							value: 'ea',
							name: 'EA'
						},
						{
							value: 'bbin',
							name: 'BBIN'
						},
						{
							value: 'kg',
							name: 'KG快乐彩'
						},
						{
							value: 'sb',
							name: '体育平台'
						},
						{
							value: 'newpt',
							name: 'PT'
						},
						{
							value: 'ttg',
							name: 'TTG'
						},
						{
							value: 'gpi',
							name: 'GPI'
						},
						{
							value: 'ebet',
							name: 'EBET'
						},
						{
							value: 'sixlottery',
							name: '六合彩'
						},
						{
							value: 'jc',
							name: '时时彩'
						}
					],
					onChange: function(e) {

					}
				});

				that.grid = new MobileGrid({
					appendId: 'betProfit-grid',
					dataUrl: 'mobi/queryAgentBetProfit.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'loginname',
								name: '会员帐号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'platform',
								name: '平台',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'bettotal',
								name: '投注总额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'profit',
								name: '输赢值',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempStarttime',
								name: '起始时间',
								align: 'center',
								onRender: function(record, value) {
									return value ?value.toString().toDate().format('yyyy-MM-dd hh:mm:ss') : value;
								}
							}),
							new MobileGridColumn({
								value: 'tempEndtime',
								name: '结束时间',
								align: 'center',
								onRender: function(record, value) {
									return value ?value.toString().toDate().format('yyyy-MM-dd hh:mm:ss') : value;
								}
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						} else {
							$('#betProfit-tbet').html(result.data.tbet || 0);
							$('#betProfit-tProfit').html(result.data.tProfit || 0);
						}
					},
					getParam: function() {
						return {
							'startDate': that.startDate.val() + ' 00:00:00',
							'endDate': that.endDate.val() + ' 00:00:00',
							'account': that.account.val(),
							'platform': that.game.getValue()
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.endDate.val(nowDate.format('yyyy-MM-dd'));
					nowDate.setDate(nowDate.getDate() - 1);
					that.startDate.val(nowDate.format('yyyy-MM-dd'));

					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}

			//佣金列表grid
			function Commission() {
				var that = this;

				that.search = $('#commission-search');
				that.yearmonth = $('#commission-yearmonth');

				that.grid = new MobileGrid({
					appendId: 'commission-grid',
					dataUrl: 'mobi/queryAgentCommission.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'yearmonth',
								name: '年月',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'loginname',
								name: '会员帐号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'ximaAmount',
								name: '洗码优惠',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'firstDepositAmount',
								name: '首存优惠',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'agAmount',
								name: '输赢值',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'otherAmount',
								name: '其他优惠',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'platform',
								name: '备注',
								align: 'center'
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						} else {
							$('#commission-tXima').html(result.data.tXima || 0);
							$('#commission-tAg').html(result.data.tAg || 0);
							$('#commission-tOther').html(result.data.tOther || 0);
						}
					},
					getParam: function() {
						return {
							'yearmonth': that.yearmonth.val()
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.yearmonth.val(nowDate.format('Y-m'));
					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}

			//日结佣金会员grid
			function PtCommission() {
				var that = this;

				that.search = $('#ptcommission-search');
				that.startDate = $('#ptcommission-startDate');
				that.endDate = $('#ptcommission-endDate');

				that.grid = new MobileGrid({
					appendId: 'ptcommission-grid',
					dataUrl: 'mobi/queryPtCommission.aspx',
					showIndex: true,
					showMore: false,
					showRefresh: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'platform',
								name: '平台',
								align: 'center',
								onRender: function(record, value) {
									return value == 'slotmachine' ? '老虎机佣金' : value == 'liveall' ? '其他佣金' : value;
								}
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '日佣金',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'createdate',
								name: '数据日期',
								align: 'center',
								onRender: function(record, value) {
									return value ? new Date(value).format('yyyy-MM-dd hh:mm:ss') : value;
								}
							})
						],
						contentColumns: []
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
						$('#ptcommission-tXimafee').html(result.data.tXimafee || 0);
					},
					getParam: function() {
						return {
							'startDate': that.startDate.val() + ' 00:00:00',
							'endDate': that.endDate.val() + ' 00:00:00'
						};
					}
				});

				//执行查询
				function _queryDetails() {
					that.grid.load();
				}

				//初始化值
				function _initValue() {
					var nowDate = new Date();
					that.endDate.val(nowDate.format('yyyy-MM-dd'));
					nowDate.setDate(nowDate.getDate() - 1);
					that.startDate.val(nowDate.format('yyyy-MM-dd'));

					nowDate = null;
				}

				//宣告事件
				function _initEvent() {
					that.search.click(_queryDetails);
					that.grid.init();
				}

				_initEvent();
				_initValue();
			}
		</script>
	</body>

</html>