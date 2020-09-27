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
		<title>SBA洗码数据采集</title>
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
       url: "/office/collectionSbaData.do", 
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
			记录 --&gt; SBA洗码数据采集
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
			<s:form action="getSbaDatas" namespace="/office" name="mainform"
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
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>

								</tr>
								<tr align="left">
									<td>
										每页记录:
										<s:select cssStyle="width:119px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="end" id="end" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{endTime}" cssClass="Wdate" />
									</td>
									<s:hidden name="pageIndex" />
									<s:set name="by" value="'winLostDateTime'" />
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
						<input type="button" id="_executeUpdateBtn" value="重新获取前一天的沙巴投注额" onclick="_UpdateSWData();"/>
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
										title="点击排序" onclick="orderby('playerName');" width="80px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="80px">
										交易时间
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="80px">
										比赛时间
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('ticketStatus');" width="90px">
										赛果
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="100px">
										投注
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('winLoseAmount');" width="130px">
										输赢
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										width="130px">
										结算后余额
									</td>
									<td align="center"
										style="color: #FFFFFF;  cursor: pointer;"
										title="点击排序" onclick="orderby('winLostDateTime');" width="130px">
										结算日期
									</td>
								</tr>
								<c:set var="betSum" value="0" scope="request"></c:set>
								<c:set var="winSum" value="0" scope="request"></c:set>
								<c:set var="revenueSum" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
						            <s:if test="#fc.winLoseAmount>=50000"><c:set var="bgcolor" value="#FF9999"/></s:if>
						            <s:elseif test="#fc.winLoseAmount>=5000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
						            <s:elseif test="#fc.winLoseAmount>=500"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
						            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
						            
									<tr bgcolor="${bgcolor}">
										<td align="center" width="90px"  style="cursor: pointer;">
											<s:property value="#fc.transId" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.playerName" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.transactionTime" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.matchDatetime" />
										</td>
										<td align="center"  align="center" width="60px" style=" cursor: pointer;">
											<s:property value="#fc.ticketStatus" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="#fc.stake" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="#fc.winLoseAmount" />
										</td>
										<td align="center" width="90px"  style=" cursor: pointer;">
											<s:property value="#fc.afterAmount" />
										</td>
										<td align="center" width="90px"   style=" cursor: pointer;">
											<s:property value="#fc.winLostDateTime" />
										</td>
									</tr>
									<s:set var="betValue" value="#fc.stake" scope="request"></s:set>
									<c:set var="betSum" value="${betSum+betValue}" scope="request"></c:set>
									
									<s:set var="winValue" value="#fc.winLoseAmount" scope="request"></s:set>
									<c:set var="winSum" value="${winSum+winValue}" scope="request"></c:set>

								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="5">
										当页小计:
									</td>
									<td align="center">
									    <s:property value="#request.betSum" />
									</td>
									<td align="center">
									    <s:property value="#request.winSum" />
									</td>
									<td align="center" colspan="2">
									</td>

								</tr>

								<tr bgcolor="#e4f2ff">
								<td align="right" colspan="5">
										总计:
									</td>
									<td align="center">
										<s:property value="#request.page.statics1" />
									</td>
									<td align="center">
									     <s:property value="#request.page.statics2" />
									</td>
									<td align="center" colspan="2">
									</td>
								</tr>

								<tr>
									<td colspan="9" align="right" bgcolor="66b5ff" align="center">
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

