<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>优惠劵</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<style type="text/css">
/*.search_margin{*/ /*margin-left:8px;*/ /*}*/
.label_search_td_play {
	font-family: Tahoma;
	font-size: 15px;
	/*font-size: 11px;*/
	line-height: 28px;
	font-weight: bold;
	/* text-align: center;*/
	text-transform: capitalize;
	color: #FFFFFF;
	text-decoration: none;
	padding-right: 1px;
}

.input {
	font-family: Tahoma;
	font-size: 18px;
	/*font-size: 11px;*/
	font-weight: normal;
	/*text-transform: capitalize;*/
	text-decoration: none;
	background-color: #FFFFFF;
	border: 1px solid #336699;
	line-height: 16px;
	height: 22px;
	float: left;
	margin-top: 2px;
}
</style>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
		<script>
		 function divType(){
              var yhj=$("#yhj").val();
              if(yhj=="584"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(12);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else{
                  $('#divMoney').css('display','block');
                  $('#divMoneyTwo').css('display','block');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','block');
                  $('#agentDivTwo').css('display','block');
                  $("#betMultiples").val(1);
                  $("#amount").val(28);
              }
               
          }	
          
           function divTypeTwo(){
             var type=$("#type").val();
             if(type=="0"){
                $('#typeDiv0').css('display','block');
                $('#typeDiv1').css('display','none');
             }else{
                $('#typeDiv0').css('display','none');
                $('#typeDiv1').css('display','block');  
             }
         }
         
         function submitFrom(){
             var type=$("#type").val();
             if(type=="0"){
                 var shippingCount=$("#shippingCount").val();
             	 if(shippingCount==""){
                  	alert("生成数量不能为空！请使用正整数");
   		          	return false;
             	 }
             	 if(isNaN(shippingCount)){
   		          	alert("生成数量非有效数字！请使用正整数");
   		          	return false;
             	 } 
             }
             $("#mainform").submit();
          }		
			</script>
	</head>
	<body>
		<div id="excel_menu_left">
			操作 --> SB优惠劵
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<p align="left">
				<s:fielderror />
			</p>
			<s:form action="addPrizeProposalCouponSb"
				onsubmit="submitonce(this);" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table align="left">
					<tr>
						<td>
							优惠类型:
						</td>
						<td>
							<select style="width: 200px; height: 28px;" class="input"
								id="yhj" onchange="divType();" name="type">
								<option value="581">
									SB红包优惠券
								</option>
								<!-- <option value="582">
									188体育存2900送580
								</option> -->
								<option value="584">
									188体育20%存送优惠劵
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">
							用户类型:
						</td>
						<td>
							<select style="width: 200px; height: 28px;" class="input"
								name="userType" id="type" onchange="divTypeTwo();">
								<option value="0">
									个人
								</option>
								<option value="1">
									群发
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<div id="divMoney">
								赠送金额:
							</div>
						</td>
						<td>
							<div id="divMoneyTwo">
								<input name="amount" readonly="readonly" style="width: 200px;"
									class="input" id="amount" value="28" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="divbetMultiples">
								投注倍数:
							</div>
						</td>
						<td>
							<div id="betMultiplesTwo">
								<input name="betMultiples" readonly="readonly"
									style="width: 200px;" class="input" value="1" id="betMultiples" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							类型或数量:
						</td>
						<td>
							<div id="typeDiv0">
								<input name="shippingCount" style="width: 200px;" class="input"
									id="shippingCount" />
							</div>
							<div id="typeDiv1" style="display: none;">
								<select style="width: 200px; height: 28px;" class="input"
									name="usernameType" id="usernameType">
									<option value="0">天兵</option>
									<option value="1">天将</option>
									<option value="2">天王</option>
									<option value="3">星君</option>
									<option value="4">真君</option>
									<option value="5">仙君</option>
									<option value="6">帝君</option>
									<option value="7">天尊</option>
									<option value="8">天帝</option>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">
							备注:
						</td>
						<td>
							<textarea name="remark" class="input"
								style="width: 300px; height: 100px;"></textarea>
						</td>
					</tr>
					<tr>
						<td align="center">
							<s:submit value="提交" onclick="return submitFrom();" />
						</td>
						<td></td>
					</tr>
				</table>
			</s:form>
		</div>
		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>

