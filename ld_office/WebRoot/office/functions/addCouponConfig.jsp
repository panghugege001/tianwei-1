<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>新增优惠券配置</title>
</head>
<body>
<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
	<form action="/office/addCouponConfig.do" name="mainform" id="mainform" method="post">
		<table>
			<tr>
				<td>优惠券类型：</td>
				<td>
					<select id="couponType" name="couponConfig.couponType" style="width: 110px;"></select>
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr id="cs1">
				<td>游戏平台：</td>
				<td>
					<select id="platformId" name="couponConfig.platformId" style="width: 110px;"></select>
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr id="cs2">
				<td>赠送百分比：</td>
				<td>
					<input type="text" id="percent" name="couponConfig.percent" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr id="hb1">
				<td>赠送金额：</td>
				<td>
					<input type="text" id="giftAmount" name="couponConfig.giftAmount" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>流水倍数：</td>
				<td>
					<input type="text" id="betMultiples" name="couponConfig.betMultiples" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr id="cs3">
				<td>最低转账金额：</td>
				<td>
					<input type="text" id="minAmount" name="couponConfig.minAmount" />
				</td>
			</tr>
			<tr id="cs4">
				<td>最高转账金额：</td>
				<td>
					<input type="text" id="maxAmount" name="couponConfig.maxAmount" />
				</td>
			</tr>
			<tr id="cs5">
				<td>赠送金额上限：</td>
				<td>
					<input type="text" id="limitMoney" name="couponConfig.limitMoney" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<!-- 单选框 -->
			<tr>
				<td>
					<input type="radio" name="isNumCount"  class="numCountRadio" value="1">数量
					<input type="radio" name="isNumCount" checked class="numCountRadio" value="0">账号
				</td>
			</tr>
			<tr id="timesTr">
				<td>数量：</td>
				<td>
					<input type="text" id="times" name="times" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr id="countsTr">
				<td>账号：</td>
				<td>
					<textarea rows="10" cols="22" id="counts" name="loginAccount"></textarea>
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td>
					<textarea rows="10" cols="22" id="remark" name="couponConfig.remark"></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="提 交" id="submitBtn" />
				</td>
			</tr>
		</table>
		<input type="hidden" id="platformName" name="couponConfig.platformName" />
	</form>
</div>
<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    //
    $(function(){
        var isPerValue = $('input[name="isNumCount"]:checked ').val();
        if(isPerValue == 1){
            $("#timesTr").show();
            $("#countsTr").hide();
        }
        if(isPerValue == 0){
            $("#countsTr").show();
            $("#timesTr").hide();
        }
        $(".numCountRadio").click(function(){
            var isPerValue = $('input[name="isNumCount"]:checked ').val();
            if(isPerValue == 1){
                $("#timesTr").show();
                $("#countsTr").hide();
            }
            if(isPerValue == 0){
                $("#countsTr").show();
                $("#timesTr").hide();
            }
        })
    })

    //
    var platform = $("#platformId");
    platform.empty();
    platform.append("<option value=''></option>");

    for (var i = 0, len = coupon_platform.length; i < len; i++) {

        platform.append("<option value='" + coupon_platform[i].value + "'>" + coupon_platform[i].text + "</option>");
    }

    var coupon = $("#couponType");
    coupon.empty();

    for (var i = 0, len = coupon_type.length; i < len; i++) {

        coupon.append("<option value='" + coupon_type[i].value + "'>" + coupon_type[i].text + "</option>");
    }

    coupon.change(couponChange);

    couponChange();

    $("#submitBtn").bind("click", submit);

    function couponChange() {

        var couponType = $("#couponType").val();

        if (couponType == "419") {

            $("tr[id^='hb']").show();
            $("tr[id^='cs']").hide();
            var arr = [ 'platformId', 'percent', 'minAmount', 'maxAmount', 'limitMoney', 'platformName' ];
            for (var i = 0, len = arr.length; i < len; i++) {
                $("#" + arr[i]).val("");
            }
        } else {

            $("tr[id^='cs']").show();
            $("tr[id^='hb']").hide();
            $("#giftAmount").val("");
        }
    }

    function submit() {

        var _this = this;

        var platformName = $("#platformId").find("option:selected").text();
        $("#platformName").val(platformName);

        var couponType = $("#couponType").val();

        if (couponType == "419") {

            var giftAmount = $("#giftAmount").val();

            if (isNull(giftAmount)) {

                alert("赠送金额不能为空！");
                return;
            }

            if (isNaN(giftAmount)) {

                alert("赠送金额只能为有效数字！");
                return;
            }
        } else {

            var platformId = $("#platformId").val();
            var percent = $("#percent").val();
            var minAmount = $("#minAmount").val();
            var maxAmount = $("#maxAmount").val();
            var limitMoney = $("#limitMoney").val();

            if (isNull(platformId)) {

                alert("游戏平台不能为空！");
                return;
            }

            if (isNull(percent)) {

                alert("赠送百分比不能为空！");
                return;
            }

            if (isNaN(percent)) {

                alert("赠送百分比只能为有效数字！");
                return;
            }

            if (percent > 1.0) {

                alert("赠送百分比不能大于1.0！");
                return;
            }

            if (!isNull(minAmount)) {

                if (isNaN(minAmount)) {

                    alert("最低转账金额只能为有效数字！");
                    return;
                }
            }

            if (!isNull(maxAmount)) {

                if (isNaN(maxAmount)) {

                    alert("最高转账金额只能为有效数字！");
                    return;
                }
            }

            if (isNull(limitMoney)) {

                alert("赠送金额上限不能为空！");
                return;
            }

            if (isNaN(limitMoney)) {

                alert("赠送金额上限只能为有效数字！");
                return;
            }
        }

        var betMultiples = $("#betMultiples").val();
        var times = $("#times").val();
        var counts = $("#counts").val();
        if (isNull(betMultiples)) {

            alert("流水倍数不能为空！");
            return;
        }

        if (isNaN(betMultiples)) {

            alert("流水倍数只能为有效数字！");
            return;
        }

        if (!isInteger(betMultiples)) {

            alert("流水倍数只能为有效整数！");
            return;
        }
//校验
        var isPerValue = $('input[name="isNumCount"]:checked ').val();
        if(isPerValue==1){
            $("#counts").val(null);
            if (isNull(times)) {

                alert("数量不能为空！");
                return;
            }

            if (isNaN(times)) {

                alert("数量只能为有效数字！");
                return;
            }

            if (!isInteger(times)) {

                alert("数量只能为有效整数！");
                return;
            }
        }else{
            $("#times").val(null);
            var counts = $("#counts").val();
            //得去除空格
            var str = counts.trim();
            if (isNull(str)) {
                alert("账号不能为空！");
                return;
            }
        }

        $(_this).attr("disabled", "disabled");

        $('#mainform').ajaxSubmit(function(data) {

            $(_this).removeAttr("disabled");

            alert(data);

            if (data.indexOf('成功') != -1) {

                var _parentWin = window.opener;
                _parentWin.mainform.submit();
                window.close();
            }
        });
    }
</script>
</body>
</html>