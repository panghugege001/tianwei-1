<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>

	<style>
		body .ui-button-row .btn_color {
			background: rgb(240, 71, 71);
		}
	</style>

	<body>
		<div class="content mt5">
			<div class="ui-form">
				<div class="ui-input-row">
					<label class="ui-label">红包余额：</label>
					<span id="hbMoney">0<span>元</span></span>
				</div>
				<div class="ui-input-row zf-sele">
					<label class="ui-label">红包类型：</label>
					<select id="hbSelect" name="hbSelect" class="input">
			  	  		
			  	  	</select>
				</div>
				<div class="ui-button-row center">
					<div class="btn-login block btn_color" onclick="return doHB();">领取红包</div>
				</div>
			</div>
			<div class="ui-form">
				<div class="ui-input-row zf-sele">
					<label class="ui-label">红包使用：</label>
					<select id="hbType" name="hbType" class="input">
                        <option value="0" selected="selected"> 请选择</option>
                        <option value="pt"> PT账户</option>
                        <option value="ttg"> TTG账户</option> 
                        <!--<option value="nt"> NT账户</option>
                        <option value="qt"> QT账户</option>
                        <option value="mg"> MG账户</option>
                        <option value="dt"> DT账户</option>-->
                        <option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
                    </select>
				</div>
				<div class="ui-input-row zf-sele">
					<label class="ui-label">转账金额：</label>
					<input id="hbRemit" class="ui-ipt" type="text">
				</div>
				<div class="ui-button-row center">
					<div class="btn-login block" onclick="return submitHBRemit();">提交</div>
				</div>
			</div>
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
				<div class="h3 bold">红包优惠说明 </div>
				<ol>
					<li>同时段红包只能领取一类红包</li>
					<li>满10元可以转到任意的平台，需5倍流水</li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			//---存提款红包
			
			//查询玩家红包余额
			function getHBMoney(type) {
			    $("#hbMoney").html("<img src='/images/waiting.gif'>");
			    $.post("/asp/getHBMoney.aspx", {"type": 0}, function (returnedData, status) {
			        if ("success" == status && returnedData !='') {
			            $("#hbMoney").html("" + returnedData + "元");
			        }
			    });
				queryHBSelect();
			}
			// 获取玩家可领红包
			

			 function queryHBSelect() {
			    $.post("/asp/queryHBSelect.aspx", function (returnedData, status) {
			    	console.log(returnedData);
			        if ("success" == status) {
			            $("#hbSelect").html("");
			            $("#hbSelect").html(returnedData);
			        }
			    });
			}			
			
			
			function doHB() {
			    var hbSelect = $("#hbSelect").val();
			    if (hbSelect == null || hbSelect == '') {
			        alert("请选择红包类型！");
			        return;
			    }
			    $.post("/asp/doHB.aspx", {
			        "sid": hbSelect
			    }, function (returnedData, status) {
			        if ("success" == status) {
			            getHBMoney();
			            alert(returnedData);
			        }
			    });
			    return false;
			
			}
			
			function submitHBRemit() {
			    var hbType = $("#hbType").val();
			    var hbRemit = $("#hbRemit").val();
			    if (hbType == "") {
			        alert("请选择平台！");
			        return false;
			    }
			    if (hbMoney != "") {
			        if (isNaN(hbRemit)) {
			            alert("转账金额非有效数字！");
			            return false;
			        }
			        if (hbRemit < 10) {
			            alert("转账金额必须大于10！");
			            return false;
			        }
			    }

			    $.post("/asp/submitHBRemit.aspx", {
			        "type": hbType,
			        "transferGameIn": hbRemit
			    }, function (returnedData, status) {
			        if ("success" == status) {
			            getHBMoney();
			            alert(returnedData);
			        }
			    });
			    return false;
			}
			
			getHBMoney();
			// ---存提款红包结束				
		</script>
	</body>

</html>