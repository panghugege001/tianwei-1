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
			<jsp:param name="Title" value="自助返水" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="form-warp txt-form">
			<div class="form-group">
				<label class="form-label">游戏平台：</label>
				<select id="xima-kind" class="form-control"></select>
			</div>

			<div id="otherList">
				<div class="form-group">
					<label class="form-label">起始时间：</label>
					<input id="xima-startDate" class="form-control" type="text" disabled="" readonly>
				</div>
				<div class="form-group">
					<label class="form-label">截止时间：</label>
					<input id="xima-endDate" class="form-control" type="text" disabled="" readonly>
				</div>
				<div class="form-group">
					<label class="form-label">有效投注额：</label>
					<input id="xima-bet" class="form-control" type="text" disabled="" readonly>
				</div>
				<div class="form-group">
					<label class="form-label">返水比率(%)：</label>
					<input id="xima-back" class="form-control" type="text" disabled="" readonly>
				</div>

				<div class="form-group">
					<label class="form-label">返水金额(元)：</label>
					<input id="xima-money" class="form-control" type="text" disabled="" readonly>
				</div>
				<br />
				<input type="button" class="btn-submit" id="xima-submit" value="提交">
			</div>
		</div>
		
		<div id="slotList" class="m-grid-table" style="display:none;">
			<table class="table" width="100%" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th>平台名称</th>
						<th>有效投注</th>
						<th>返水比例</th>
						<th>返水金额</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot class="total-count">
					<tr>
						<td>合计</td>
						<td></td>
						<td></td>
						<td><span class="totalCount">0</span></td>
					</tr>
				</tfoot>
			</table>

			<div class="form-warp">
				<p><span class="c-red">注：</span><span id="dtTime"></span><span id="ptswTime"></span></p>
			</div>
			<div class="form-warp" onclick="return newExecXimaSubmit();">
				<input type="button" class="btn-submit " value="领取">
			</div>
		</div>
		<div class="text-tips">
			<div class="h3">温馨提示：</div>
			<ol>
				<p>1.自助返水限定MG、CQ9、PG、DT、BBIN老虎机。（PT老虎机带有奖池类的无法进行自助返水，隔天进行系统返水。）</p>
				<p>2.自助返水的最低金额为1元。（要是您没达到自助返水的最低金额是不能自助返水的）。</p>
				<p>3.每天的00：00—03：00和12：00—15：00为系统结算时间，暂不能申请自助反水。 每日反水结算时间为：AGIN、体育从昨天12点到今天12点，MG、CQ9、PG、DT、BBIN从昨天0点到今天0点。PNG从昨天8点到今天8点。所产生的有效投注额*该金额对应的比例，未自助反水将通过系统反水进行结算。</p>
				<p>4.自助返水无次数限制，自助返水申请提交后，会在5分钟内通过审核，返水金额会自动添加到您的天威账号中。</p>
				<p>5.若要领取MG、CQ9、PG、DT、BBIN自助反水，请点击老虎机类型进行自助反水。</p>
			</ol>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			//自助返水
			function newExecXimaSubmit() {
				if(!window.confirm("确定吗？"))
					return;
				mobileManage.getLoader().open('载入中');
				$.post("/asp/execSlotXima.aspx", {
					"platform": 'slot'
				}, function(returnedData, status) {
					if("success" == status) {
						mobileManage.getLoader().close();
						alert(returnedData.message);
					}
				});
				return false;
			}
			XimaPage();

			function XimaPage() {
				var that = this;
				that.startDate = $('#xima-startDate');
				that.endDate = $('#xima-endDate');
				that.dtTime = $('#dtTime');
				that.ptswTime = $('#ptswTime');
				that.bet = $('#xima-bet');
				that.back = $('#xima-back');
				that.money = $('#xima-money');
				that.submit = $('#xima-submit');

				that.submit.click(_submit);
				//新老虎机反水
				function soltSelfGetEvent() {
					$('#otherList').hide();
					$('#slotList').show();
					mobileManage.getLoader().open('载入中');
					$.post("/asp/getAutoXimaSlotObject.aspx", {
						"platform": 'slot'
					}, function(returnedData, status) {
						if("success" == status) {
							mobileManage.getLoader().close();
							var html, message = returnedData.message;
							if(message == 'success') {
								var jsonData = eval('(' + returnedData.jsonResult + ')');
								var dtTimeHtml = jsonData['dt'] ? '您的DT游戏流水统计时间为（' + jsonData['dt'].startTimeStr + ' 至 ' + jsonData['dt'].endTimeStr + '），' : '您的DT游戏暂无流水，';
								var ptswTimeHtml = jsonData['ptsw'] ? '您的SW游戏流水统计时间为（' + jsonData['ptsw'].startTimeStr + ' 至 ' + jsonData['ptsw'].endTimeStr + '）。' : '您的SW游戏暂无流水。';
								that.dtTime.html(dtTimeHtml);
								that.ptswTime.html(ptswTimeHtml);
								var totalCount = 0;

								for(var key in jsonData) {
									if(key != 'message') {
										html += '<tr>' + '<td>' + key + '平台' + '</td>' + '<td>' + jsonData[key].validAmount + '</td>' + '<td>' + jsonData[key].rate + '</td>' + '<td>' + jsonData[key].ximaAmount + '</td>' + '</tr>'
									}
									totalCount += jsonData[key].ximaAmount
								}
								$('.totalCount').html(totalCount.toFixed(2))
								$('#slotList table tbody').html(html);
							} else {
								alert(message)
							}
						}
					});

				}

				if($('#xima-kind').get(0)) {
					that.kind = new MobileComboBox({
						appendId: 'xima-kind',
						cls: 'ui-select',
						valueName: 'value',
						displayName: 'name',
						datas: [{
								value: '',
								name: '请选择'
							},
							/* {
								value: 'pttiger',
								name: 'PT'
							},
							{
								value: 'ttg',
								name: 'TTG'
							}, */
				/* 			{
								value: 'slot',
								name: '老虎机账户(SW,MG,DT,PNG,QT,NT)'
							}, */
							{
								value: 'mg',
								name: 'MG老虎机'
							},
							{
								value: 'dt',
								name: 'DT老虎机'
							},
							{
								value: 'cq9',
								name: 'CQ9老虎机'
							},
							{
								value: 'pg',
								name: 'PG老虎机'
							}
						],
						onChange: function(e) {
							that.startDate.val('');
							that.endDate.val('');
							that.bet.val('');
							that.back.val('');
							that.money.val('');
							console.log(that.kind)
							if(that.kind.getValue() == '') return;
							if(that.kind.getValue() == 'slot') {
								soltSelfGetEvent();
							} else {
								$('#otherList').show();
								$('#slotList').hide();
								mobileManage.getLoader().open('载入中');
								mobileManage.getSelfGetManage().getXimaData({
									gameId: that.kind.getValue()
								}, function(result) {
									mobileManage.getLoader().close();
									if(result.success) {
										that.startDate.val(result.data.startDate);
										that.endDate.val(result.data.endDate);
										that.bet.val(result.data.validAmount);
										that.back.val(result.data.rate);
										that.money.val(result.data.ximaAmount);
									} else {
										if(result.data) {
											that.startDate.val(result.data.startDate);
											that.endDate.val(result.data.endDate);
										}
										alert(result.message);
									}
								});
							}
						}
					});
				}

				//洗马
				function _submit() {
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().executeXima({
						gameId: that.kind.getValue(),
						startDate: that.startDate.val(),
						endDate: that.endDate.val(),
						bet: that.bet.val()
					}, function(result) {
						mobileManage.getLoader().close();
						if(result.success) {
							alert(result.message);
						} else {
							alert(result.message);
						}
					});
				}
			}
		</script>
	</body>

</html>