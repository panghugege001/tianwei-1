<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<title>龙都娱乐城--手机投注</title>
</head>

<body>
<div class="pay-page">
	<jsp:include page="/tpl/header.jsp"></jsp:include>

	<div class="pay-online-wp">
		 <jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>	
		<form id="dinpayRedirect" action="${ctx}/asp/pay_api.aspx" method=post name="dinpayRedirect" id="dinpayRedirect">
			<input type="hidden" name="platformId" id="platformId" value="${platformId}"/>
            <input type="hidden" name="loginName" id="loginName" value="${session.customer.loginname}"/>
            <input type="hidden" name="payUrl" id="payUrl" value="${payUrl}"/>
            <input type="hidden" name="usetype" value="2"/>

			<table class="account-info table-pay">
				<tr>
					<th>账户名:</th>
					<td>${session.customer.loginname}</td>
				</tr>
				<tr>
					<th>账户余额：</th>
					<td class="c-red">${session.customer.credit}元</td>
				</tr>
				<tr>
					<th>点卡类型：</th>
					<td>
						<div class="ipt-group">
							<select name="cardCode" id="bankco" class="ipt-txt">
								<option value="">－ 请选择点卡 －</option>
								<option value="YDSZX">移动神州行</option>
								<option value="DXGK">电信国卡</option>
								<option value="LTYKT">联通一卡通</option>
								<option value="QBCZK">QQ币充值卡</option>
								<option value="JWYKT">骏网一卡通</option>
								<option value="WMYKT">完美一卡通</option>
								<option value="ZTYKT">征途一卡通</option>
								<option value="WYYKT">网易一卡通</option>
								<option value="SFYKT">盛付通一卡通</option>
								<option value="SHYKT">搜狐一卡通</option>
								<option value="JYYKT">九游一卡通</option>
								<option value="THYKT">天宏一卡通</option>
								<option value="TXYKT">天下一卡通</option>
								<option value="TXYKTZX">天下一卡通专项</option>
							</select>
							<span class="c-red">* 必填</span>
						</div>
					</td>
				</tr>
				<tr>
					<th>请选择面额：</th>
					<td>
						<div class="ipt-group">
							<select name="orderAmount" id="orderAmount" class="ipt-txt"></select>
							<span class="c-red">* 必填</span>
						</div>
					</td>
				</tr>
				<tr>
					<th> 卡号：</th>
					<td>
						<div class="ipt-group">
							<input  name="cardNo" id="cardNo" type="text" maxlength="30" class="ipt-txt" />
							<span class="c-red">* 必填</span>
						</div>
					</td>
				</tr>
				<tr>
					<th> 密码：</th>
					<td>
						<div class="ipt-group">
							<input  name="cardPassword" id="cardPassword" type="password" maxlength="30" class="ipt-txt" />
							<span class="c-red">* 必填</span>
						</div>
					</td>
				</tr>
				<tr>
					<th>&nbsp;</th>
					<td>
						<div class="ipt-group">
							<input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_zf_onclick();" value="确定支付" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">温馨提示：为了避免掉单情况的发生，请您在支付完成后，需等“支付成功”页面跳转出来，点<input name="Submit22" type="button" class="pay-tip warning" value="商城取货" />或<input name="Submit2" type="button" class="pay-tip danger" value="通知客户" />，再关闭页面,以免掉单！感谢配合！！！</td>
				</tr>
			</table>
			<p class="mb20 c-red">温馨提示：建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的。到账金额比实际存款金额少。到账比例 请查看费率表。</p>

			<table class="table">
				<tbody id="cardTable">

				</tbody>
			</table>
		</form>
	</div>

	</div><jsp:include page="/tpl/footer.jsp"></jsp:include>
	<script type="text/javascript">
		
	    $(function () {

            $.getJSON('/asp/pay_bank.aspx?platformId=' +${platformId}, function (data) {

//            var data = '{"code":"10000","desc":"成功","data":[{"dictName":"100011","dictValue":"0.13","dictShow":"5,10,20,15,30,50","dictDesc":"网易一卡通"},{"dictName":"100010","dictValue":"0.06","dictShow":"20,30,50,100,300,500","dictDesc":"联通充值卡"},{"dictName":"100009","dictValue":"0.12","dictShow":"15,30,50,100","dictDesc":"完美一卡通"},{"dictName":"100008","dictValue":"0.06","dictShow":"50,100","dictDesc":"中国电信卡"},{"dictName":"100007","dictValue":"0.13","dictShow":"5,10,15,30,60,100,200","dictDesc":"腾讯Q币卡"},{"dictName":"100006","dictValue":"0.16","dictShow":"5,10,15,20,25,30,50,100","dictDesc":"久游一卡通"},{"dictName":"100005","dictValue":"0.16","dictShow":"5,6,9,10,14,15,20,30,50,100,200,300,500,1000","dictDesc":"骏网一卡通"},{"dictName":"100004","dictValue":"0.13","dictShow":"5,10,15,25,30,35,45,50,100,300,350,1000","dictDesc":"盛大一卡通"},{"dictName":"100003","dictValue":"0.13","dictShow":"10,15,20,25,30,50,60,100,300,468,500","dictDesc":"征途游戏卡"},{"dictName":"100002","dictValue":"0.05","dictShow":"10,20,30,50,100,300,500","dictDesc":"神州行充值卡"},{"dictName":"100001","dictValue":"0.16","dictShow":"10,15,30,50,100","dictDesc":"纵游一卡通"}]}';
//            data = JSON.parse(data);

                var $selectAmount = $('#orderAmount'), $selectCard = $('#bankco'), $cardTable = $("#cardTable");
                var cardhtml = '<option> － 请选择支付点卡 － </option>';
                var descHtml = "<tr><th colspan='4'>点卡支付业务费率</th></tr>";

                $.each(data.data, function (index, vo) {
                    cardhtml += '<option value="' + vo.dictName + '">' + vo.dictDesc + '</option>';
                    descHtml += "<tr><td>卡类型：</td><td>" + vo.dictDesc + "</td><td>费率：</td><td>" + vo.dictValue + "</td></tr>";
                });
                descHtml += "<tr><td colspan='4'><p class='c-red'>温馨提示：建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的。到账金额比实际存款金额少。到账比例</p></td></tr>";
                $selectCard.html(cardhtml);
                $cardTable.html(descHtml);
                cardData = data.data;

                $selectCard.on('change', function () {
                    var card = $(this).val();
                    var html = '<option> － 请选择面额 － </option>'
                    for (var i = 0; i < cardData.length; i++) {
                        if (card == cardData[i].dictName) {
                            var array = cardData[i].dictShow.split(",");
                            for (var j = 0; j < array.length; j++) {
                                html += '<option value="' + array[j] + '">' + array[j] + '</option>'
                            }
                        }
                    }
                    $selectAmount.html(html);
                });
            });
	    });
	
		function btnOK_zf_onclick(){
			var orderAmount = $("#orderAmount").val();
			var loginName = $("#loginName").val();
			var bankco =  $("#bankco").val();
			var cardNo =  $.trim($("#cardNo").val());
			var cardPassword =  $.trim($("#cardPassword").val());
			if(isNaN(orderAmount)){
				alert("[提示]存款额度非有效数字！");
				return false;
			}
			if(orderAmount<1){
				alert("[提示]1元以上或者1元才能存款！");
				return false;
			}
			if(orderAmount>5000){
				alert("[提示]存款金额不能超过5000！");
				return false;
			}
			if(loginName==null ||loginName==""){
				alert("[提示]系统检测你已经掉线,请重新登录！");
				return false;
			}
			if(bankco==null ||bankco==""){
				alert("[提示]点卡类型不能为空！");
				return false;
			}
			if(cardNo=="" ||cardPassword==""){
				alert("[提示]卡号和密码不能为空！");
				return false;
			}
			$("#dinpayRedirect").submit();
			return true;
		}
	</script>
	
</body>
</html>