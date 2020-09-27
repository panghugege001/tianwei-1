<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
		<title>玩家支付宝账号</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
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


function responseMethod(data){
	alert(data.responseText);
}
function saveAlipayAccount(){
	if(confirm("确认添加？")){
		var data = $("#_saveAlipayAccountForm").serialize();
		console.log(data);
		$.post("/office/addAlipayAccount.do?"+data,function(data){
			alert(data);
		});
	}
}
function _changeState(loginname){
	if(confirm("确认更新？")){
		$.post("/office/updateAlipayAccountState.do",{"loginname":loginname},function(data){
			document.mainform.submit();
			alert(data);
		});
	}
}
</script>
	</head>
	<body>
		<p>
			--&gt; 玩家支付宝账号
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 15%;">
			<s:form action="getAlipayAccounts" namespace="/office" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										支付宝账号:
										<s:textfield cssStyle="width:115px" name="alipayAccount" size="30"/>
									</td>
									<td>
										开始时间:
										<s:textfield name="startPt" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{startPt}" cssClass="Wdate" />
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
										玩家帐号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td>
										结束时间:
										<s:textfield name="endPt" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{endPt}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</div>
		<br />
		<div style="position: absolute; top: 115px; left: 15%">
		 <c:if test="${sessionScope.operator.authority eq  'finance_manager' || sessionScope.operator.authority eq 'finance' || sessionScope.operator.authority eq 'boss' }">
			 <fieldset>
	    		<legend>新增玩家支付宝存款账号</legend>
	    		<form id="_saveAlipayAccountForm">
				<table border="0" cellpadding="0" cellspacing="0" width="550px;">
					<tr align="left">
						<td>支付宝账号:</td>
						<td>
							<input type="text" name="alipayAccount" />
						</td>
						<td>游戏帐号:</td>
						<td>
							<input type="text" name="loginname" />
						</td>
						<td>
							<input type="button" value="新增" onclick="saveAlipayAccount();"/>
						</td>
					</tr>
				</table>
				</form>
			</fieldset>
		</c:if>
		</div>
		<br />
		<div id="middle" style="position: absolute; top: 180px; left: 15%;">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="910px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('alipayAccount');" width="130px">
										支付宝账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('loginname');" width="130px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('disable');" width="130px">
										状态
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('updatetime');" width="130px">
										修改时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="130px">
										操作
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr >
										<td align="center" width="60px">
										<c:choose>
											<c:when test="${sessionScope.operator.authority eq  'finance_manager' || sessionScope.operator.authority eq  'finance' || sessionScope.operator.authority eq 'boss' }">
												<s:property value="#fc.alipayAccount" />
											</c:when>
											<c:otherwise>
												<s:property value="#fc.alipayAccount.substring(0,5)+'*****'" />
											</c:otherwise>
										</c:choose>
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.disable==0">正常</s:if>
											<s:if test="#fc.disable==1">锁定</s:if>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updatetime" />
										</td>
										<td align="center" width="90px">
											<c:if test="${sessionScope.operator.authority eq  'finance_manager' || sessionScope.operator.authority eq 'finance' || sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'boss' }">
												<input type="button" value="更改状态" onclick="_changeState('<s:property value="#fc.loginname" />');"/>
											</c:if>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="7" align="right" bgcolor="66b5ff" align="center">
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

