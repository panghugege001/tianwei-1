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
		<title>SW洗码数据采集</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<%-- <script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script> --%>
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

function _UpdateSWData(){
	var executeTime = $("#_executeTime").val();
	$("#_executeUpdateBtn").attr("disabled", true);
	
	var index = layer.load(1, {
		 shade: [0.5,'#fff'] //0.1透明度的白色背景
	});   
	$.ajax({ 
       type: "post", 
       url: "/office/updateSwData.do", 
       cache: false, 
       data:{
         "executeTime" : executeTime
       },
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
			记录 --&gt; SW洗码数据采集
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getSwDatas" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										玩家账号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td>
										开始时间:
										<s:textfield name="start" id="start" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
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
										游戏类型:
										<s:select cssStyle="width:115px"
											list="#{'':'全部','slot':'老虎机','fish':'捕鱼'}"
											name="type" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="end" id="end" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{endTime}" cssClass="Wdate" />
									</td>
									<td colspan="2">
										游戏编码:
										<s:textfield cssStyle="width:90px" name="code" size="20" />
									</td>
									<s:hidden name="pageIndex" />
									<s:set name="by" value="'ts'" />
									<s:set name="order" value="'desc'" />
									<s:hidden name="order" value="%{order}" />
									<s:hidden name="by" value="%{by}" />
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<br />
		<br />
		<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'finance' || sessionScope.operator.authority eq 'finance_manager'}">
			<div  style="position: absolute; top: 120px; left: 0px;">
						更新时间:<s:textfield  id="_executeTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false"  cssClass="Wdate"  />
						<input type="button" id="_executeUpdateBtn" value="重新获取SW投注数据" onclick="_UpdateSWData();"/>
						<span style="color:red">温馨提示：系统定时自动获取SW数据，如果数据核对错误在点此按钮</span>
			</div>
		</c:if>
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
							<table width="1200px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="80px">
										交易id
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="80px">
										标记
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('mbrCode');" width="80px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="80px">
										游戏编号
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="80px">
										币种
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('amnt');" width="90px">
										投注
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="100px">
										派彩
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('balance');" width="130px">
										输赢
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('ts');" width="130px">
										下注时间
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="130px">
										结算前余额
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="130px">
										结算后余额
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="130px">
										终端
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="130px">
										事件记录数
									</td>
								</tr>
								<c:set var="betSum" value="0" scope="request"></c:set>
								<c:set var="winSum" value="0" scope="request"></c:set>
								<c:set var="revenueSum" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
						            <s:if test="#fc.revenue>=50000"><c:set var="bgcolor" value="#FF9999"/></s:if>
						            <s:elseif test="#fc.revenue>=5000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
						            <s:elseif test="#fc.revenue>=500"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
						            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
						            
									<tr bgcolor="${bgcolor}">
										<td align="center" width="90px"  style="cursor: pointer;">
											<s:property value="#fc.roundId" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.brandId" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.playerCode" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.gameCode" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.currency" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="@dfh.utils.NumericUtil@double2String(#fc.bet)" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="@dfh.utils.NumericUtil@double2String(#fc.win)" />
										</td>
										<td align="center" width="90px"  style=" cursor: pointer;">
											<s:property value="@dfh.utils.NumericUtil@double2String(#fc.revenue)" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="#fc.ts" />
										</td>
										<td align="center" width="90px"  style=" cursor: pointer;">
											<s:property value="#fc.balanceBefore" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="#fc.balanceAfter" />
										</td>
										<td align="center" width="90px"  style=" cursor: pointer;">
											<s:property value="#fc.device" />
										</td>
										<td align="center" width="90px"  style=" cursor: pointer;">
											<s:property value="#fc.insertedAt" />
										</td>
										<td align="center" width="90px"  style=" cursor: pointer;">
											<s:property value="#fc.totalEvents" />
										</td>
									</tr>
									<s:set var="betValue" value="#fc.bet" scope="request"></s:set>
									<c:set var="betSum" value="${betSum+betValue}" scope="request"></c:set>
									
									<s:set var="winValue" value="#fc.win" scope="request"></s:set>
									<c:set var="winSum" value="${winSum+winValue}" scope="request"></c:set>
									
									<s:set var="revenueValue" value="#fc.revenue" scope="request"></s:set>
									<c:set var="revenueSum" value="${revenueSum+revenueValue}" scope="request"></c:set>

								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="5">
										当页小计:
									</td>
									<td align="center">
									    <s:property value="@dfh.utils.NumericUtil@double2String(#request.betSum)" />
									</td>
									<td align="center">
									    <s:property value="@dfh.utils.NumericUtil@double2String(#request.winSum)" />
									</td>
									<td align="center">
									    <s:property value="@dfh.utils.NumericUtil@double2String(#request.revenueSum)" />
									</td>
									<td align="center" colspan="6">
									</td>

								</tr>

								<tr bgcolor="#e4f2ff">
								<td align="right" colspan="5">
										总计:
									</td>
									<td align="center">
										<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
									</td>
									<td align="center">
									     <s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
									</td>
									<td align="center">
									     <s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics3)" />
									</td>
									<td align="center" colspan="6">
									</td>
								</tr>

								<tr>
									<td colspan="14" align="right" bgcolor="66b5ff" align="center">
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

