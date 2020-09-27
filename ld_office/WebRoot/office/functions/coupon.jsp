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
              if(yhj=="531"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $('#userTypeDiv').css('display','block');
                  $('#userTypeDivTwo').css('display','block');
                  $("#betMultiples").val(5);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="532"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $('#userTypeDiv').css('display','block');
                  $('#userTypeDivTwo').css('display','block');
                  $("#betMultiples").val(8);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="533"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $('#userTypeDiv').css('display','block');
                  $('#userTypeDivTwo').css('display','block');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="534"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $('#userTypeDiv').css('display','block');
                  $('#userTypeDivTwo').css('display','block');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="535"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $('#userTypeDiv').css('display','block');
                  $('#userTypeDivTwo').css('display','block');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="536"){
                  $('#divMoney').css('display','block');
                  $('#divMoneyTwo').css('display','block');
                  $('#divbetMultiples').css('display','none');
                  $('#betMultiplesTwo').css('display','none');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $('#userTypeDiv').css('display','none');
                  $('#userTypeDivTwo').css('display','none');
                  $("#betMultiples").val(0);
                  $("#amount").val(100);
                  $("#agent").val("");
                  $('#typeDiv0').css('display','block');
                  $('#typeDiv').css('display','block');
              }else{
                  $('#divMoney').css('display','block');
                  $('#divMoneyTwo').css('display','block');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','block');
                  $('#agentDivTwo').css('display','block');
                  $("#betMultiples").val(15);
                  $("#amount").val(68);
                  $('#userTypeDiv').css('display','none');
                  $('#userTypeDivTwo').css('display','none');
                  $('#typeDiv0').css('display','block');
                  $('#typeDiv').css('display','block');
              }  
          }	
          function divTypeTwo(){
             var type=$("#type").val();
             if(type=="0"){
                $('#typeDiv0').css('display','block');
                $('#typeDiv').css('display','block');
             }else{
                $('#typeDiv0').css('display','none');
                $('#typeDiv').css('display','none');  
             }
         }
         function submitFrom(){
             var yhj=$("#yhj").val();
             if(yhj=="537"){
                 var agent=$("#agent").val();
             	 if(agent==""){
                  	alert("代理账号不能为空！");
   		          	return false;
             	}
             } 
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
			操作 --> 优惠劵
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<p align="left">
				<s:fielderror />
			</p>
			<s:form action="addPrizeProposalCoupon" onsubmit="submitonce(this);"
				namespace="/office" name="mainform" id="mainform" theme="simple">
				<table align="left">
					<tr>
						<td>
							优惠类型:
						</td>
						<td>
							<select style="width: 200px; height: 28px;" class="input"
								id="yhj" onchange="divType();" name="type">
								<option value="537">
									邀请码
								</option>
								<option value="536">
									红包优惠券
								</option>
								<option value="531">
									10%存送优惠券
								</option>
								<option value="532">
									20%存送优惠券
								</option>
								<option value="533">
									30%存送优惠券
								</option>
								<option value="534">
									40%存送优惠券
								</option>
								<option value="535">
									50%存送优惠券
								</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td>
							<div id="userTypeDiv" style="display: none;">
								用户类型:
							</div>
						</td>
						<td>
						   <div id="userTypeDivTwo" style="display: none;">
								<select style="width: 200px; height: 28px;" class="input"
									name="userType" id="type" onchange="divTypeTwo();">
									<option value="0">
										个人
									</option>
									<option value="1">
										群发
									</option>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="agentDiv">
								代理账号:
							</div>
						</td>
						<td>
							<div id="agentDivTwo">
								<input name="agent" style="width: 200px;" class="input"
									id="agent"  />
							</div>
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
								<input name="amount" readonly="readonly" style="width: 200px;" class="input"
									id="amount" value="68" />
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
								<input name="betMultiples" readonly="readonly" style="width: 200px;"
									class="input" value="15" id="betMultiples" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="typeDiv">
								数量:
							</div>
						</td>
						<td>
							<div id="typeDiv0">
								<input name="shippingCount" style="width: 200px;" class="input"
									id="shippingCount" />
							</div>
							<!-- 
							<div id="typeDiv1" style="display: none;">
								<select style="width: 200px; height: 28px;" class="input"
									name="usernameType" id="usernameType">
									<option value="0">
										初学者
									</option>
									<option value="1">
										忠实赌神
									</option>
									<option value="2">
										星级赌神
									</option>
									<option value="3">
										金牌赌神
									</option>
									<option value="4">
										白金赌神
									</option>
									<option value="5">
										钻石赌神
									</option>
									<option value="6">
										至尊赌神
									</option>
								</select>
							</div> -->
						</td>
					</tr>
					<tr>
						<td valign="top">
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

