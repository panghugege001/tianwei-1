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
		<title>玩家摇摇乐任务记录</title>
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

</script>
	</head>
	<body>
		<p>
			--&gt; 摇摇乐任务记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getUserTaskRecords" namespace="/office"
				name="mainform" id="mainform" theme="simple">
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
										任务类型:
										<s:select cssStyle="width:115px"
											list="#{'1':'PT次存','2':'TTG次存','3':'微信存款','4':'PT1万流水','5':'TTG三万流水','6':'PT3万流水','7':'存款5000'}"
											name="type" listKey="key" listValue="value"
											emptyOption="true"></s:select>
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
										任务状态:
										<s:select cssStyle="width:115px" list="#{'0':'未完成','1':'完成','2':'过期','3':'关闭'}" name="isAdd" listKey="key" listValue="value"
											emptyOption="true"></s:select>
									</td>
									<td>
										推荐码:
										<s:textfield cssStyle="width:115px" name="intro" size="20" />
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
									<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'sale_manager'}">
										<a href="/office/getTaskList.do" target="_blank" style="color:red;font-size: 14px;">查看任务列表</a>
									</c:if>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<br />
		<br />
		<div id="middle" style="position: absolute; top: 145px; left: 0px">
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
										title="点击排序" onclick="orderby('username');" width="130px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('title');" width="130px">
										任务标题
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('historyBet');" width="130px">
										当日历史数据
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('isAdd');" width="130px">
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
										更新时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('amount');" width="130px">
										累计金额
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<s:url action="getUserhavinginfo" namespace="/office" var="getUserhavinginfourl"><s:param name="loginname" value="%{#fc[0]}"/></s:url>
									<tr >
										<td align="center" width="60px">
              								<a target="_blank" href='<s:property value="%{getUserhavinginfourl}"/>' title="点击查看修改当前用户的基本信息"><s:property value="#fc[0]"/></a>
										</td>
										<td align="center" width="60px">
											<s:property value="#fc[8]" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc[3]" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc[4]==0">未完成</s:if>
											<s:if test="#fc[4]==1">成功</s:if>
											<s:if test="#fc[4]==2">过期</s:if>
											<s:if test="#fc[4]==3">关闭</s:if>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[5]" />
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[6]" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc[7]" />
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

