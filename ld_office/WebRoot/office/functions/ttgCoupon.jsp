<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>ttg优惠劵</title>
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
              if(yhj=="401"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="402"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else if(yhj=="403"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(10);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="404"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              }
              else if(yhj=="405"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else if(yhj=="406"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else if(yhj=="407"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(10);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="408"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else if(yhj=="425"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else if(yhj=="422"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              } else if(yhj=="423"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(10);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="424"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="426"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="427"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="428"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(10);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="429"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="430"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="431"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="432"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(10);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="433"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="434"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(15);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="435"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(13);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="436"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(10);
                  $("#amount").val(0);
                  $("#agent").val("");
              }else if(yhj=="437"){
                  $('#divMoney').css('display','none');
                  $('#divMoneyTwo').css('display','none');
                  $('#divbetMultiples').css('display','block');
                  $('#betMultiplesTwo').css('display','block');
                  $('#agentDiv').css('display','none');
                  $('#agentDivTwo').css('display','none');
                  $("#betMultiples").val(20);
                  $("#amount").val(0);
                  $("#agent").val("");
              }
          }	
          
         function submitFrom(){
             var type=$("#type").val();
             var yhj=$("#yhj").val();
             if(yhj==""){
            		alert("请选择优惠类型");
   		          	return false;
             }
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
			操作 --> TTG优惠劵
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<p align="left">
				<s:fielderror />
			</p>
			<s:form action="addPrizeProposalCouponTtg"
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
								<option value="">
									请选择
								</option>
								<option value="401">
									TTG100%存送优惠劵15倍流水
								</option>
								<option value="402">
									TTG88%存送优惠劵
								</option>
								<option value="403">
									TTG68%存送优惠劵
								</option>
								<option value="404">
									TTG100%存送优惠劵20倍流水
								</option>
								<option value="425">
									NT100%存送优惠劵15倍流水
								</option>
								<option value="422">
									NT88%存送优惠劵
								</option>
								<option value="423">
									NT68%存送优惠劵
								</option>
								<option value="424">
									NT100%存送优惠劵20倍流水
								</option>
								<option value="426">
									QT100%存送优惠劵15倍流水
								</option>
								<option value="427">
									QT88%存送优惠劵
								</option>
								<option value="428">
									QT68%存送优惠劵
								</option>
								<option value="429">
									QT100%存送优惠劵20倍流水
								</option>
								<option value="430">
									MG100%存送优惠劵15倍流水
								</option>
								<option value="431">
									MG88%存送优惠劵
								</option>
								<option value="432">
									MG68%存送优惠劵
								</option>
								<option value="433">
									MG100%存送优惠劵20倍流水
								</option>
								<option value="434">
									DT100%存送优惠劵15倍流水
								</option>
								<option value="435">
									DT88%存送优惠劵
								</option>
								<option value="436">
									DT68%存送优惠劵
								</option>
								<option value="437">
									DT100%存送优惠劵20倍流水
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
								name="userType" id="type" >
								<option value="0">
									个人
								</option>
							</select>
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
							数量:
						</td>
						<td>
							<div id="typeDiv0">
								<input name="shippingCount" style="width: 200px;" class="input"
									id="shippingCount" />
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

