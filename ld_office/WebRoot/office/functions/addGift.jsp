<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<s:set name="start" value="" />
<s:set name="end" value="" />
<s:set name="startTime" value="" />
<s:set name="endTime" value="" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>新增礼品活动</title>
		<style type="text/css">
			table {
				border-collapse: separate;
				border-spacing: 10px;
			}
		</style>
	</head>
	<body>
		<div id="excel_menu">
			<s:form name="mainform" id="mainform" theme="simple" action="addGift" namespace="/office">
				<table align="center" border='0'>
					<tr>
						<td>优惠类型</td>
						<td>
							<select id='type' name="type" style="width: 90px;">
								<option value="礼品登记">礼品登记</option>
								<option value="限时礼品">限时礼品</option>
							</select>
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>标题</td>
						<td>
							<input type="text" name="title" id='title' style="width: 157px;" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>限制类型</td>
						<td>
							<select id="limitType" name="limitType" style="width: 90px;">
								<option value="存款">存款</option>
								<option value="投注">投注</option>
							</select>
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td id="td1">存款额要求</td>
						<td>
							<input type="text" id="deposit" name="deposit" style="width: 157px;" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td id="td2">开始时间（存款）</td>
						<td>
							<s:textfield id="start" name="start" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td id="td3">结束时间（存款）</td>
						<td>
							<s:textfield id="end" name="end" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>活动开始时间</td>
						<td>
							<s:textfield id="startTime" name="startTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>活动结束时间</td>
						<td>
							<s:textfield id="endTime" name="endTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr id="tr1">
						<td>限时礼品人数</td>
						<td>
							<input type="text" name="giftQuantity" id='giftQuantity' style="width: 157px;" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>等级</td>
						<td>
							<label><input type="checkbox" name="viplevel" value="0"><s:property value="@dfh.model.enums.VipLevel@getText(0)" /></label>
							<label><input type="checkbox" name="viplevel" value="1"><s:property value="@dfh.model.enums.VipLevel@getText(1)" /></label>
							<label><input type="checkbox" name="viplevel" value="2"><s:property value="@dfh.model.enums.VipLevel@getText(2)" /></label>
							<label><input type="checkbox" name="viplevel" value="3"><s:property value="@dfh.model.enums.VipLevel@getText(3)" /></label>
							<label><input type="checkbox" name="viplevel" value="4"><s:property value="@dfh.model.enums.VipLevel@getText(4)" /></label>
							<label><input type="checkbox" name="viplevel" value="5"><s:property value="@dfh.model.enums.VipLevel@getText(5)" /></label>
							<label><input type="checkbox" name="viplevel" value="6"><s:property value="@dfh.model.enums.VipLevel@getText(6)" /></label>
							<label><input type="checkbox" name="viplevel" value="7"><s:property value="@dfh.model.enums.VipLevel@getText(7)" /></label>
							<label><input type="checkbox" name="viplevel" value="8"><s:property value="@dfh.model.enums.VipLevel@getText(8)" /></label>
						</td>
					</tr>
					<tr>
						<td>备注</td>
						<td>
							<textarea rows="10" cols="19" id="remark" name="remark"></textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="button" value="创建" id="addGift" style="margin-left: 160px;"/>
						</td>
					</tr>
				</table>
				<input type="hidden" id="depositAmount" name="depositAmount" />
				<input type="hidden" id="startTimeDeposit" name="startTimeDeposit" />
				<input type="hidden" id="endTimeDeposit" name="endTimeDeposit" />
				<input type="hidden" id="betAmount" name="betAmount" />
				<input type="hidden" id="startTimeBet" name="startTimeBet" />
				<input type="hidden" id="endTimeBet" name="endTimeBet" />
				<input type="hidden" id="vip" name="vip" />
			</s:form>
		</div>		
	</body>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">
	$(function() {
		// 限时礼品人数默认隐藏，只有当优惠类型为限时礼品时才显示
		$("#tr1").hide();
		
		// 创建按钮事件处理方法
		$("#addGift").bind('click', function() {
			// 标题
			var title = $("#title").val();
			// 存款额要求/投注额要求
			var deposit = $('#deposit').val();
			// 限制类型
			var limitType = $('#limitType').val();
			// 开始时间（存款）/开始时间（投注）
			var start = $('#start').val();
			// 结束时间（存款）/结束时间（投注）
			var end = $('#end').val();
			// 活动开始时间
			var startTime = $('#startTime').val();
			// 活动结束时间
			var endTime = $('#endTime').val();
			// 限时礼品人数
			var giftQuantity = $('#giftQuantity').val();
			
			if (isNull(title)) {
				
				alert('标题不能为空！');
				return;
			}
			
			if (isNull(deposit)) {
			
				var al = '存款' === limitType ? '存款额要求不能为空！' : '投注额要求不能为空！';
				
				alert(al);
				return;
			}
			
			if (isNaN(deposit)) {
				
				var al = '存款' === limitType ? '存款额要求必须为数值！' : '投注额要求必须为数值！';
				
				alert(al);
				return;
			}
			
			if (isNull(start)) {
				
				var al = '存款' === limitType ? '存款开始时间不能为空！' : '投注开始时间不能为空！';
				
				alert(al);
				return;
			}
			
			if (isNull(end)) {
				
				var al = '存款' === limitType ? '存款结束时间不能为空！' : '投注结束时间不能为空！';
				
				alert(al);
				return;
			}
			
			var startDate = new Date(start.replace('-', '/').replace('-', '/'));
			var endDate = new Date(end.replace('-', '/').replace('-', '/'));
			
			if (endDate < startDate) {
				
				var al = '存款' === limitType ? '存款结束时间应大于存款开始时间！' : '投注结束时间应大于投注开始时间！';
				
				alert(al);
				return;
			}
			
			if (isNull(startTime)) {
			
				alert('活动开始时间不能为空！');
				return;
			}
			
			if (isNull(endTime)) {
				
				alert('活动结束时间不能为空！');
				return;
			}
			
			var activityStartDate = new Date(startTime.replace('-', '/').replace('-', '/'));
			var activityEndDate = new Date(endTime.replace('-', '/').replace('-', '/'));
			
			if (activityEndDate < activityStartDate) {
			
				alert('活动结束时间应大于活动开始时间！');
				return;
			}
			
			if ('限时礼品' === $("#type").val()) {
			
				if (isNull(giftQuantity)) {
					
					alert('限时礼品人数不能为空！');
					return;
				}
				
				var reg = /^\+?[1-9][0-9]*$/;
				var result = reg.test(giftQuantity);
				
				if (isNaN(giftQuantity) || !result) {
					
					alert('限时礼品人数必须为整数！');
					return;
				}
			}
			
			var str = "";
			
			$("input[name='viplevel']:checked").each(function() {
				
				str += $(this).val();
			});
			
			if (isNull(str)) {
				
				alert('活动等级不能为空！');
				return;
			}
			
			if ('存款' === limitType) {
			
				$('#depositAmount').val(deposit);
				$('#startTimeDeposit').val(start);
				$('#endTimeDeposit').val(end);
				$('#betAmount').val(0);
			} else {
				
				$('#betAmount').val(deposit);
				$('#startTimeBet').val(start);
				$('#endTimeBet').val(end);
				$('#depositAmount').val(0);
			}
			
			$('#vip').val(str);
			
			if (confirm("确定要创建此活动吗？")) {
				
				var data = $("#mainform").serialize();
				
				var action = "/office/addGift.do";
				
				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {
					
					var text = result.responseText;
					
					alert(text);
					
					if (text.indexOf('成功') != -1) {
						
						var _parent = window.opener;
						_parent.mainform.submit();
						window.close();
					}
				}});
			}
		});
		
		// 优惠类型下拉框事件处理方法
		$("#type").bind('change', function() {
			
			if ('限时礼品' === $(this).val()) {
				
				$("#tr1").show();
			} else {
				
				$("#tr1").hide();
			}
		});
		
		// 限制类型下拉框事件处理方法
		$("#limitType").bind('change', function() {
			
			if ('投注' === $(this).val()) {
				
				$("#td1").html('').html('投注额要求');
				$("#td2").html('').html('开始时间（投注）');
				$("#td3").html('').html('结束时间（投注）');
			} else {
				
				$("#td1").html('').html('存款额要求');
				$("#td2").html('').html('开始时间（存款）');
				$("#td3").html('').html('结束时间（存款）');
			}
		});
		
		function isNull(value) {
		
			if (null == value || "" === value || "" === $.trim(value)) {
				
				return true;
			}
			
			return false;
		};
	});
	</script>
</html>