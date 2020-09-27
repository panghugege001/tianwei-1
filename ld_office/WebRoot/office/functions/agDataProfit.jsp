<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>AG平台游戏记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/js/layer/layer.js"></script>
		<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}

function _UpdateAgData(){
	$("#_executeUpdateBtn").attr("disabled", true);
	
	var index = layer.load(1, {
		 shade: [0.5,'#fff'] //0.1透明度的白色背景
	});   
	var platform = $("#mainform_platform").val()
	if(platform == ''){
		alert("请选择游戏类型");
		layer.close(index);
		$("#_executeUpdateBtn").attr("disabled", false);
		return;
	}
	var executeTime = $("#_executeTime").val();
	$.ajax({ 
       type: "post", 
       url: "/office/updateAgDataOnline.do", 
       cache: false, 
       data: {"platform":platform,"executeTime":executeTime},
       timeout:200000, 
       success : function(data){
	        alert(data);
       },
       complete: function(){
        	layer.close(index);
        	$("#_executeUpdateBtn").removeAttr("disabled");
   	}
   });
}

</script>
	</head>
	<body>
		<p>
			记录 --&gt; AG平台游戏记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
			<s:form action="getAgDataList" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										玩家帐号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td>
										开始时间:
										<s:textfield name="startTime" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="%{startTime}" cssClass="Wdate" />
									</td>
									<td colspan="2">
										每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>

								</tr>
								<tr align="left">
									<td>
										设备类型:
										<s:select cssStyle="width:115px"
											list="#{'':'','0':'PC','1':'手机'}"
											name="type" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="endTime" id="end" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'recalcuTime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td>
										游戏类型:
										<s:select cssStyle="width:115px"
											list="#{'':'','AGIN':'真人','SLOT':'老虎机','YPMONEY':'yp街机','AGINFISH':'捕鱼','AGSLOTOTHER':'其他电子游戏'}"
											name="platform" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'finance' || sessionScope.operator.authority eq 'finance_manager'}">
			<div  style="position: absolute; top: 120px; left: 0px;">
				更新时间:<s:textfield  id="_executeTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false"  cssClass="Wdate"  />
				<input type="button" id="_executeUpdateBtn" value="重新获取AG投注数据" onclick="_UpdateAgData(this);"/>
				<span style="color:red">温馨提示：系统定时自动获取AG数据，如果数据核对错误在点此按钮</span>
			</div>
		</c:if>
		<br />
		<br />
		<br />
		<br />
		<div id="middle" style="position: absolute; top: 155px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1100px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="80px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="90px">
										流水号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="90px">
										单号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('betAmount');" width="90px">
										投注额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('validBetAmount');" width="90px">
										有效投注额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('netAmount');" width="90px">
										输赢金额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('beforeCredit');" width="90px">
										下注前余额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('betTime');" width="90px">
										交易时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('recalcuTime');" width="90px">
										结算时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('deviceType');" width="90px">
										设备类型
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="90px">
										游戏类型
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('platformType');" width="90px">
										平台类型
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<s:if test="#fc.bettingAmount>=500000">
										<c:set var="bgcolor" value="#FF9999" />
									</s:if>
									<s:elseif test="#fc.bettingAmount>=50000">
										<c:set var="bgcolor" value="#D20000" />
									</s:elseif>
									<s:elseif test="#fc.bettingAmount>=5000">
										<c:set var="bgcolor" value="#FFABCE" />
									</s:elseif>
									<s:else>
										<c:set var="bgcolor" value="#e4f2ff" />
									</s:else>
									<tr bgcolor="${bgcolor}">
										<td align="center" width="60px">
											<s:property value="#fc.playName" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.billNo" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.gameCode" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@toDouble(#fc.betAmount)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@toDouble(#fc.validBetAmount)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@toDouble(#fc.netAmount)" />
										</td>
										<td align="center" width="90px">
											<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.beforeCredit)" />
										</td>
										<td align="center" width="180px">
											<s:property value="#fc.betTime" />
										</td>
										<td align="center" width="180px">
											<s:property value="#fc.recalcuTime" />
										</td>
										<td align="center" width="60px">
											<s:if test="#fc.deviceType=='0'">PC</s:if>
											<s:if test="#fc.deviceType=='1'">手机</s:if>
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.gameType" />
										</td>
										<td align="center" width="60px">
											<s:if test="#fc.platformType=='AGIN'">真人</s:if>
											<s:if test="#fc.platformType=='SLOT'">老虎机</s:if>
											<s:if test="#fc.platformType=='YPMONEY'">yp街机</s:if>
											<s:if test="#fc.platformType=='AGINFISH'">捕鱼</s:if>
										</td>
									</tr>
									<s:set var="BettingAmount" value="#fc.betAmount" scope="request"></s:set>
									<c:set var="baSum" value="${baSum+BettingAmount}" scope="request"></c:set>

									<s:set var="ValidAmount" value="#fc.validBetAmount" scope="request"></s:set>
									<c:set var="vdSum" value="${vdSum+ValidAmount}" scope="request"></c:set>
									
									<s:set var="WinLoseAmount" value="#fc.netAmount" scope="request"></s:set>
									<c:set var="waSum" value="${waSum+WinLoseAmount}" scope="request"></c:set>
									
								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="3">
										当页小计:
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String4(#request.baSum)" />
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String4(#request.vdSum)" />
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String4(#request.waSum)" />
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
								</tr>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="3">
										总计:
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String4(#request.page.statics1)" />
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String4(#request.page.statics2)" />
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String4(#request.page.statics3)" />
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
									<td align="right">
									</td>
								</tr>

								<tr>
									<td colspan="12" align="right" bgcolor="66b5ff" align="center">
										${page.jsPageCode}
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>
