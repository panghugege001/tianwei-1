<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<base href="${ctx}/" />
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户清单" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="list-warp">
			<div class="list-group">
				<div class="list-item" data-key="#record-ck"><img src="/mobile/img/icon/history2.png" alt="" />在线存款记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-zz'><img src="/mobile/img/icon/history1.png" alt="" />户内转账记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-sgck'><img src="/mobile/img/icon/history3.png" alt="" />手工存款记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-tk'><img src="/mobile/img/icon/history4.png" alt="" />提款记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-yh'><img src="/mobile/img/icon/history6.png" alt="" />优惠活动记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-yhj'><img src="/mobile/img/icon/history7.png" alt="" />优惠卷记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-fs'><img src="/mobile/img/icon/history8.png" alt="" />自助返水记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-fy'><img src="/mobile/img/icon/history9.png" alt="" />存款附言记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-hb'><img src="/mobile/img/icon/history1.png" alt="" />红包雨记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<!--<div class="list-group">
				<div class="list-item" data-key='#record-jf'><img src="/mobile/img/icon/history9.png" alt="" />积分记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='#record-hy'><img src="/mobile/img/icon/history9.png" alt="" />好友推荐记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>
			<div class="list-group">
				<div class="list-item" data-key='record-zp'><img src="/mobile/img/icon/history9.png" alt="" />幸运转盘记录<i class="iconfont fr icon-downjiantou"></i></div>
			</div>-->
		</div>
		<!--在线存款记录-->
		<div id="record-ck" class=" tab-panel record">
			<div id="deposit-grid"></div>
		</div>
		<!--户内转账记录-->
		<div id="record-zz" class=" tab-panel record">
			<div id="transfer-grid"></div>
		</div>
		<!--手工存款记录-->
		<div id="record-sgck" class=" tab-panel record">
			<div id="cashin-grid"></div>
		</div>
		<!--提款记录-->
		<div id="record-tk" class=" tab-panel record">
			<div id="withdraw-grid"></div>
		</div>
		<!--优惠活动记录-->
		<div id="record-yh" class=" tab-panel record">
			<div id="concessionReccords-grid"></div>
		</div>
		<!--优惠卷记录-->
		<div id="record-yhj" class=" tab-panel record">
			<div id="couponRecords-grid"></div>
		</div>
		<!--自助返水记录-->
		<div id="record-fs" class=" tab-panel record">
			<div id="ximaDetail-grid"></div>
		</div>
		<!--存款附言记录-->
		<div id="record-fy" class=" tab-panel record">
			<div id="depositOrderRecord-grid"></div>
		</div>
		<!--红包雨记录-->
		<div id="record-hb" class=" tab-panel record">
			<div id="hongbao-grid"></div>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/selfget/birthday-histiry.jsp">
				<img src="/mobile/img/icon/red.png" alt="" />生日礼金领取记录
				<i class="iconfont fr icon-downjiantou"></i>
			</a>
		</div>
		<!--积分记录-->
		<%--<div id="record-jf" class=" tab-panel record" >
			<div id="point-grid"></div>
		</div>--%>
		<!--好友推荐记录-->
		<div id="record-hy" class="tab-panel record">
			<div class="ui-form">
				<div class="ui-input-row zf-sele">
					<label class="ui-label">记录类型：</label>
					<div id="friend-type"> </div>

				</div>
			</div>
			<div id="friend-grid"></div>
		</div>
		<!--幸运转盘记录-->
		<%--<div id="record-zp" class=" tab-panel record" >
			<div id="lottery-grid"></div>
		</div>--%>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script src="/mobile/js/MobileComboBox.js" type="text/javascript" charset="utf-8"></script>
		<script src="/mobile/js/MobileGrid.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
		</script>
		<script type="text/javascript">
			new HistoryPage();

			function HistoryPage() {
				var that = this;
				that.grids = {};
				var $titles = $('.main-wrap>.content>ul>li>.title');
				$titles.bind('click', _titleClickEvent);
				var list_warp = $(".list-warp")
				list_warp.find('.list-item').click(function() {
					window.header.setTitle($(this).text())
					var $key = $(this).data("key");
					$($key).addClass('active').siblings().removeClass('active')
					list_warp.hide();
					_titleClickEvent($key)
				})
				window.header.backFun = function() {
					if(list_warp.is(":hidden")) {
						window.header.setTitle('账户清单')
						$('.tab-panel').removeClass('active')
						list_warp.show();
					} else {
						history.back();
					}
				}
				
				
				function _titleClickEvent(key) {
					if(!that.grids[key]) {
						that.grids[key] = true;
						switch(key) {
							case '#record-ck':
								that.depositGrid.init();
								that.depositGrid.load();
								break;
							case '#record-sgck':
								that.cashinGrid.init();
								that.cashinGrid.load();
								break;
							case '#record-tk':
								that.withdrawGrid.init();
								that.withdrawGrid.load();
								break;
							case '#record-fy':
								that.depositOrderRecordGrid.init();
								that.depositOrderRecordGrid.load();
								break;
							case '#record-zz':
								that.transferGrid.init();
								that.transferGrid.load();
								break;
							case '#record-fs':
								that.ximaDetailGrid.init();
								that.ximaDetailGrid.load();
								break;
							case '#record-yh':
								that.concessionReccordsGrid.init();
								that.concessionReccordsGrid.load();
								break;
							case '#record-yhj':
								that.couponRecordsGrid.init();
								that.couponRecordsGrid.load();
								break;
							case '#record-jf':
								that.pointGrid.init();
								that.pointGrid.load();
								break;
							case '#record-hy':
								that.friendGrid.init();
								that.friendGrid.load();
								break;
							case '#record-zp':
								that.lotteryGrid.init();
								that.lotteryGrid.load();
								break;
							case '#record-hb':
								that.hongbaoGrid.init();
								that.hongbaoGrid.load();
								break;
							default:
						}
					}
				}

				//在线存款Grid
				that.depositGrid = new MobileGrid({
					appendId: 'deposit-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'billno',
								name: '编号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'money',
								name: '存款金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'flag',
								name: '状态',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '存款时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? value.substring(0, 10) : '';
								}
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'billno',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'money',
								name: '存款金额：'
							}),
							new MobileGridColumn({
								value: 'loginname',
								name: '存款帐号：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'deposit'
						};
					}
				});

				//在线存款Grid
				that.cashinGrid = new MobileGrid({
					appendId: 'cashin-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'amount',
								name: '存款金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'flag',
								name: '状态',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '存款时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? value.substring(0, 10) : '';
								}
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'pno',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '存款金额：'
							}),
							new MobileGridColumn({
								value: 'loginname',
								name: '存款帐号：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'cashin'
						};
					}
				});

				//提款Grid
				that.withdrawGrid = new MobileGrid({
					appendId: 'withdraw-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'pno',
								name: '编号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '提款金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'flag',
								name: '状态',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '提款时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? value.substring(0, 10) : '';
								}
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'pno',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '提款金额：'
							}),
							new MobileGridColumn({
								value: 'loginname',
								name: '提款账号：'
							}),
							new MobileGridColumn({
								value: 'flag',
								name: '状态：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'withdraw'
						};
					}
				});

				//轉帳Grid
				that.transferGrid = new MobileGrid({
					appendId: 'transfer-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'id',
								name: '编号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'remit',
								name: '转账金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '转账时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? value.substring(0, 10) : '';
								}
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'id',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'remit',
								name: '转账金额：'
							}),
							new MobileGridColumn({
								value: 'loginname',
								name: '转账账号：'
							}),
							new MobileGridColumn({
								value: 'remark',
								name: '状态：'
							}),
							new MobileGridColumn({
								value: 'source',
								name: '转账类型：',
								onRender: function(record, value) {
									if(value == 'longdu') {
										return '从天威账户转至' + record['target'] + '账户';
									} else if(value == 'REDRAIN' && record['target'] != 'USER') {
										return '红包雨转入游戏平台';
									} else if(record['target'] == 'REDRAIN') {
										return '主账户转到红包雨账户';
									} else if(record['target'] == 'USER') {
										return '红包雨转给好友';
									} else if(value == 'TOUSER') {
										return '收到好友转来红包';
									} else {
										return '从' + value + '转至天威账户';
									}
								}
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'transfer'
						};
					}
				});

				//附言存款Grid
				that.depositOrderRecordGrid = new MobileGrid({
					appendId: 'depositOrderRecord-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'depositId',
								name: '附言',
								width: '10%',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'bankname',
								name: '银行',
								width: '15%',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'status',
								name: '状态',
								width: '10%',
								align: 'center',
								onRender: function(record, value) {
									if(value == 0) {
										return '未处理';
									} else if(value == 1) {
										return '已处理';
									}
								}
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '创建时间',
								width: '20%',
								align: 'center',
								onRender: function(record, value) {
									return value ? value.substring(0, 10) : '';
								}
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'depositId',
								name: '附言：'
							}),
							new MobileGridColumn({
								value: 'bankname',
								name: '银行：'
							}),
							new MobileGridColumn({
								value: 'accountname',
								name: '账号名：'
							}),
							new MobileGridColumn({
								value: 'remark',
								name: '账号：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'depositOrderRecord'
						};
					}
				});

				//优惠活动记录
				that.concessionReccordsGrid = new MobileGrid({
					appendId: 'concessionReccords-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'type',
								name: '优惠类型',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '赠送金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '创建时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? value.substring(0, 10) : '';
								}
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'pno',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'loginname',
								name: '帐户：'
							}),
							new MobileGridColumn({
								value: 'remark',
								name: '备注：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'concessionReccords'
						};
					}
				});

				//优惠卷记录
				that.couponRecordsGrid = new MobileGrid({
					appendId: 'couponRecords-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'gifTamount',
								name: '赠送',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'amount',
								name: '存款',
								align: 'center'
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'pno',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'type',
								name: '类型：'
							}),
							new MobileGridColumn({
								value: 'remark',
								name: '转账：'
							}),
							new MobileGridColumn({
								value: 'betMultiples',
								name: '倍数：'
							}),
							new MobileGridColumn({
								value: 'shippingCode',
								name: '代码：'
							}),
							new MobileGridColumn({
								value: 'tempCreateTime',
								name: '执行时间：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'couponRecords'
						};
					}
				});

				//自助返水记录
				that.ximaDetailGrid = new MobileGrid({
					appendId: 'ximaDetail-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'validAmount',
								name: '有效投注额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'ximaAmount',
								name: '结算金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'ximaStatus',
								name: '状态',
								align: 'center'
							})
						],
						contentColumns: [
							new MobileGridColumn({
								value: 'pno',
								name: '编号：'
							}),
							new MobileGridColumn({
								value: 'ximaType',
								name: '洗码类型：'
							}),
							new MobileGridColumn({
								value: 'rate',
								name: '洗码率：'
							}),
							new MobileGridColumn({
								value: 'statisticsTimeRange',
								name: '统计时间段：'
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'ximaDetail'
						};
					}
				});

				//积分Grid
				that.pointGrid = new MobileGrid({
					appendId: 'point-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					showMore: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'points',
								name: '积分值',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'type',
								name: '积分类型',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'createday',
								name: '时间',
								align: 'center'
							})
						]
					},
					onLoad: function(result) {

						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'point'
						};
					}
				});

				if($('#friend-type').length > 0) {
					that.$friendType = new MobileComboBox({
						appendId: 'friend-type',
						valueName: 'value',
						displayName: 'name',
						datas: [{
								value: '0',
								name: '推荐注册成功玩家'
							},
							{
								value: '1',
								name: '推荐奖金收入'
							},
							{
								value: '2',
								name: '推荐奖金支出'
							}
						],
						onChange: function(e) {
							that.friendGrid.load();
						}
					});
				}

				//红包雨Grid
				that.hongbaoGrid = new MobileGrid({
					appendId: 'hongbao-grid',
					dataUrl: '${ctx}/redrain/queryRedRainCount.aspx',
					showIndex: true,
					showMore: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'typeId',
								name: '标题',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'times',
								name: '个数',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'money',
								name: '金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'date',
								name: '活动时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? new Date(value).format('yyyy-MM-dd') : value;
								}
							})
						]
					},
					onLoad: function(result) {
						//                    pageIndex:1
						//                    size:10
						//                    total:00.0
						console.log(result)
						return {
							data: {
								records: result
							}
						}
						//                    if(!result.success){
						//                        alert(result.message);
						//                    }
					}
				});

				//好友推荐Grid
				that.friendGrid = new MobileGrid({
					appendId: 'friend-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					showMore: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'downlineuser',
								name: '玩家账号',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'money',
								name: '金额',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'type',
								name: '类型',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'createtime',
								name: '时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? new Date(value).format('yyyy-MM-dd') : value;
								}
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'friend',
							flag: that.$friendType.getValue()
						};
					}
				});

				that.lotteryGrid = new MobileGrid({
					appendId: 'lottery-grid',
					dataUrl: '${ctx}/mobi/queryHistory.aspx',
					showIndex: true,
					showMore: false,
					columnModel: {
						recordColumns: [
							new MobileGridColumn({
								value: 'itemName',
								name: '获奖',
								align: 'center'
							}),
							new MobileGridColumn({
								value: 'winningDate',
								name: '时间',
								align: 'center',
								onRender: function(record, value) {
									return value ? new Date(value).format('yyyy-MM-dd') : value;
								}
							}),
							new MobileGridColumn({
								value: 'isReceive',
								name: '状态',
								align: 'center',
								onRender: function(record, value) {
									return value == '1' ? '已兑换' : '尚未兑换';
								}
							})
						]
					},
					onLoad: function(result) {
						if(!result.success) {
							alert(result.message);
						}
					},
					getParam: function() {
						return {
							historyType: 'lottery'
						};
					}
				});

			}
		</script>
	</body>

</html>