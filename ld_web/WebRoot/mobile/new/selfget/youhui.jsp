<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE >
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="自助存送" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>
	<style>
		#bcbsTable {
			display: none;
			margin-bottom: 0.3333333rem;
		}
		#bcbsTable table {
			width: 100%;
			font-size: 13px;
		}
		.bcbs-table td{
		    border:1px solid #eeeeee !important;
		    color: #000;
		    background-color: #ccc;
		    height:30px;
		    text-align: center;
		}		
		.bcbs-table th{
		    background-color: #dfa85a;
		    color: #fff;
		    font-weight: normal;
		    border: 1px solid #fff;
		    height:30px;
		    text-align: center;
		}
	</style>
	<body>
		<div class="form-warp txt-form">
			<div class="form-group ">
				<label class="form-label">存送优惠平台：</label>
				<select class="form-control" id="youhui-platform"></select>
			</div>
			<div class="form-group ">
				<label class="form-label">存送优惠类型：</label>
				<select id="youhui-type" class="form-control"></select>
			</div>
			<div class="form-group">
				<label class="form-label">红利金额：</label>
				<input id="youhui-giftMoney" type="text" class="form-control" disabled="" readonly>
			</div>
			<div class="form-group">
				<label class="form-label">流水倍数：</label>
				<input id="youhui-waterTimes" type="text" class="form-control" disabled="" readonly>
			</div>
			<div class="form-group">
				<label class="form-label">转账金额：</label>
				<input id="youhui-money" type="text" placeholder="请输入转账金额" class="form-control">
			</div>
			<input id="youhui-id" type="hidden">
			
			<div class="btn-submit " id="openBcbsModal">笔存笔送</div>
            <div id="bcbsTable" class="bcbs-table">
                <table class="table">
                    <tbody>
                    <tr>
                        <th>单笔存款金额</th>
                        <th>存送比率</th>
                        <th>PT/TTG/老虎机钱包</th>
                        <th>流水倍数</th>
                    </tr>
                    <tr>
                        <td>10 - 499.99</td>
                        <td>15%</td>
                        <td>各可5次</td>
                        <td rowspan="4">18倍</td>
                    </tr>
                    <tr>
                        <td>500-4999.99</td>
                        <td>20%</td>
                        <td>各可6次</td>
                    </tr>               
                    <tr>
                        <td>5000-9999.99</td>
                        <td>25%</td>
                        <td>各可7次</td>
                    </tr>               
                    <tr>
                        <td>10000及以上</td>
                        <td>30%</td>
                        <td>各可8次</td>
                    </tr>
                    </tbody>
                </table>
                <input type="button" class="btn-submit" value="收起" id="closeBcbsModal"/>
            </div>
			<div class="btn-submit " id="youhui-submit">提交</div>
			<div class="text-tips">
				<div class="h3">温馨提示：</div>
				<ol>
					<li>每天的00：00—01：00为系统结算时间，暂时无法使用自助存送。</li>
					<li>申请存送后彩金会自动添加到相应平台，您直接进入游戏即可。 </li>
					<li>老虎机存送优惠，存款10元后方可自助操作。</li>
				</ol>
			</div>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script>
		    // 自助存送内部弹窗
		    $('#openBcbsModal').on('click',function () {
		        $('#bcbsTable').slideDown(400);
		    })
		    $('#closeBcbsModal').add('#bcbs-mk').on('click',function () {
		        $('#bcbsTable').slideUp(400);
		    })

		</script>
		<script type="text/javascript">
			YouhuiPage()

			function YouhuiPage() {
				var that = this;
				//设定只能输入数字
				NumberInput('youhui-money');
				//优惠平台
				var platformData = [{
					value: '',
					name: '请选择'
				}];
				var data = [];

				//优惠类型
				var typeData = {
					"6001": [{
						value: '',
						name: '请选择'
					}],
					"6002": [{
						value: '',
						name: '请选择'
					}],
					"6003": [{
						value: '',
						name: '请选择'
					}],
					"6004": [{
						value: '',
						name: '请选择'
					}],
					"6005": [{
						value: '',
						name: '请选择'
					}],
					"6006": [{
						value: '',
						name: '请选择'
					}],
					"6007": [{
						value: '',
						name: '请选择'
					}],
					"6008": [{
						value: '',
						name: '请选择'
					}],
					"6009": [{
						value: '',
						name: '请选择'
					}]
				};
				that.$giftMoney = $('#youhui-giftMoney');
				that.$waterTimes = $('#youhui-waterTimes');
				that.$money = $('#youhui-money');
				that.$youhuiId = $("#youhui-id");
				that.$submit = $('#youhui-submit');
				//输入金额立即计算红利
				that.$money.keyup(function(e) {
					//延迟不重复处理同样的动作
					delayAction('youhui-money', 300, function() {
						if(that.type.getRecord()) {
							var giftMoney = that.$money.val() * that.type.getRecord().percent;
							giftMoney = giftMoney > that.type.getRecord().limitMoney ? that.type.getRecord().limitMoney : giftMoney;
							that.$giftMoney.val(giftMoney.toFixed(2));
						}
					});
				});
				that.$submit.click(_submit);
				if($('#youhui-platform').length > 0) {
					that.platform = new MobileComboBox({
						appendId: 'youhui-platform',
						valueName: 'value',
						displayName: 'name',
						datas: [],
						onChange: function(e) {
							console.log(typeData,typeData[$(this).val()])
							that.type.loadData(typeData[$(this).val()]);
							that.type.setValue('');
						}
					});
				}

				if($('#youhui-type').length > 0) {
					that.type = new MobileComboBox({
						appendId: 'youhui-type',
						cls: '',
						valueName: 'value',
						displayName: 'name',
						datas: [{
							value: '',
							name: '请选择'
						}],
						onChange: function(e) {
							if($(this).val()=="")return;
							that.$giftMoney.val('0.00');
							var record=that.type.getRecord()||{};
							console.log(record)
							that.$waterTimes.val(record.betMultiples||0);
							that.$money.val('');
							that.$youhuiId.val(record.id||0);
						}
					});
				}

				mobileManage.getLoader().open('查询中');
				//查询存送优惠类型
				mobileManage.getSelfGetManage().getYouhuiData(function(result) {
					if(result.success) {
						data = result.data;
						for(var i = 0, l = result.data.length; i < l; i++) {
							var obj = result.data[i];
							for(var k in typeData) {
								if(obj.platformId && obj.platformId.indexOf(k) != -1) {
									result.data[i].value = obj.id;
									typeData[k].push(result.data[i]);
								}
							}
						}
						platformData.push({
							value: '6001',
							name: 'PT存送优惠'
						});
						platformData.push({
							value: '6006',
							name: 'TTG存送优惠'
						});
						platformData.push({
							value: '6009',
							name: '老虎机存送优惠(SW.MG.DT.PNG.QT.NT)'
						});
						platformData.push({
							value: '6008',
							name: 'AG真人存送优惠'
						});

						that.platform.loadData(platformData);
					} else {
						alert(result.message);
					}
					mobileManage.getLoader().close();
				});

				//存送
				function _submit() {
					var o = {};
					for(var i = 0, l = data.length; i < l; i++) {
						if(data[i].id == that.type.getValue()) {
							o = data[i];
							break;
						}
					}
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().doYouhui({
						id: o.id,
						platformId: o.platformId,
						titleId: o.titleId,
						youhuiType: o.titleId,
						money: that.$money.val()
					}, function(result) {
						mobileManage.getLoader().close();
						if(result.success) {
							alert(result.message);
						} else {
							if(result.message.indexOf('binding') >= 0) {
								alert('请先绑定银行卡和真实姓名');
								window.location.href = 'mobile/account.jsp';
								return false;
							}
							alert(result.message);
						}
					});
				}
			}
		</script>

	</body>

</html>