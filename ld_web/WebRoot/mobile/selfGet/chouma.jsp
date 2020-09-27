<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
	<body>	
		<div class="content mt5">
			<div class="ui-form">
				<div class="ui-input-row">
					<label class="ui-label">会员等级：</label>
					<input style="color: #ed6325;" type="text" class="ui-ipt" readonly value="<s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)"/>">
			  </div>
				<div class="ui-input-row" style="display: none;">
					<label class="ui-label">转账金额：</label>	
					<label class="ui-label">目标帐户：</label>
						<select id="j-chipPaltform" class="ui-ipt">
						<!-- <option value="" selected="">请选择游戏平台</option>
						<option value="PT">PT</option>
						<option value="TTG">TTG</option>
						<option value="QT">QT</option>
						<option value="NT">NT</option>
						<option value="MG">MG</option>
						<option value="DT">DT</option>
						<option value="PNG">PNG</option> -->
						</select>
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">筹码金额：</label>
					<input style="color: #ed6325;" id="moneyVal" class="ui-ipt" type="text" readonly>
			  	</div>
				<div class="ui-input-row" style="display: none;">
					<label class="ui-label">流水倍数：</label>
					<input id="theMultiple" class="ui-ipt" type="text" readonly>
			  	</div>			  	
			  	<input id="youhui-id" type="hidden">
				<div class="ui-button-row center">
					<input type="button" class="btn btn-block btn-login" id="mfcm-submit" value="立即领取">
				</div>
				<div class="space-2"></div>
			</div>
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
				<div class="h3">温馨提示：</div>
					<ol>
						<li>忠实会员及以上会员，每月可领取一次VIP免费筹码。</li> 
						<li>免费筹码以您领取时的VIP等级来进行派发。</li>
						<li>免费筹码领取后，直接添加到天威主帐户。</li>
						<li>免费筹码无需流水即可提款。</li>
						<li>领取过程中有任何疑问，请咨询24小时在线客服。</li>
					</ol>
			</div>
		</div>		
		
	</body>
<script>
	!function (){
		// 领取免费筹码
		function chouma(){
			$.ajax({
			type: "post",
			url: "/asp/checkActivityInfo.aspx",
			cache: false,
			beforeSend:function(){$('#moneyVal').add($('#theMultiple')).val('加载中');},
			data:{titleId:'vipmonthfree'},
			success : function(data){
				if(!data.amount){
				$('#moneyVal').val(0 + '元');
				}else {
				$('#moneyVal').val(data.amount + '元');
				}
				$('#theMultiple').val(data.multiple + '倍');
				var htmlarr = ['<option value="" selected="">请选择游戏平台</option>'],platformarr;
				if (!data.platform){
					$('.need-hide').hide();
				}
				else {
					platformarr = data.platform.split(',');
					for (var i = 0; i < platformarr.length; i++){
						htmlarr.push('<option value="' + platformarr[i] + '">' + platformarr[i] +'</option>');
					}
				}
				$('#j-chipPaltform').html(htmlarr.join(''));
			},
			error: function(){("系统错误");},
			complete: function(){
			}
		});
		}
		chouma()
		$('#mfcm-submit').on('click',function (){
		    $.post('/asp/applyActivity.aspx',{titleId:'vipmonthfree',platform:$('#j-chipPaltform').val(),entrance:'pc'},function (data) {
		        alert(data);
		    })
		})
	}();
</script>	
</html>