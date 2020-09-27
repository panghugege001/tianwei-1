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
	
		<title>自助体验金配置</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
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

function changeIsused(id , isused , btn){
	btn.disabled = true;
	if(confirm("确定？")){
		$.post("/office/modifyExperienceGold.do",{"id":id,"isused":isused},function(data){
			alert(data);
			$("#mainform").submit();
		});
	}
}
</script>
	</head>
	<body>
		<p>
			--&gt; 自助体验金配置
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getExperienceGold" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									
									<td>
										 是否开启:
										<s:select cssStyle="width:115px"
											list="#{'':'','1':'开启','0':'关闭'}"
											name="isused" listKey="key" listValue="value"
											emptyOption="false"></s:select>
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
										优惠类型:
										<s:select cssStyle="width:125px"
											list="#{'':'','自助体验金':'自助体验金','APP下载彩金':'APP下载彩金'}" 
											name="type" listKey="key" listValue="value"
											emptyOption="false"></s:select> 
									</td>
									<td>
										<a href="/office/functions/addExperienceGold.jsp" target="_blank" style="color:red;font-size: 14px;">新增优惠配置</a>
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
							<table width="1310px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('title');" width="130px">优惠类型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('title');" width="130px">优惠标题</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">体验金额度</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">启用开始时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">启用结束时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">限制最大金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">限制最小金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">次数(次/（天、周、月、年）)</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="180px">等级</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="180px">申请通道</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('isUsed');" width="130px">状态</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('createTime');" width="130px">创建时间</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<s:if test="#fc.isused==1">
										<tr style="color: red;">
									</s:if>
									<s:if test="#fc.isused==0">
										<tr>
									</s:if>
										<td align="left" nowrap="nowrap">
											<a href="/office/getExperienceGoldInfo.do?id=<s:property value='#fc.id' />" ><s:property value="#fc.title" /></a>
										</td>
										<td align="left" nowrap="nowrap">
											<s:property value="#fc.aliasTitle" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.amount" />
										</td>
										<td align="center" width="90px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTime" />
										</td>
										<td align="center" width="90px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTime" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.maxMoney" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.minMoney" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.times" />次/
											<s:if test="#fc.timesFlag==1">天</s:if>
											<s:elseif test="#fc.timesFlag==2">周</s:elseif>
											<s:elseif test="#fc.timesFlag==3">月</s:elseif>
											<s:elseif test="#fc.timesFlag==4">年</s:elseif>
										</td>
										<td align="center" width="90px">
										    <s:property value="@dfh.model.enums.VipLevel@getTextStr(#fc.vip)" />
										</td>
										<td>
											<c:forTokens var="str" items="${fc.isPhone}" delims="," varStatus="status">
												  <c:if test="${str == 1}">官网</c:if>
												  <c:if test="${str == 2}">WEB</c:if>
												  <c:if test="${str == 3}">安卓APP</c:if>
												  <c:if test="${str == 4}">苹果APP</c:if>
											</c:forTokens>
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.isUsed==0"><input type="button" value="未开启,点击开启" onclick="changeIsused(<s:property value="#fc.id" />,1 ,this)"/></s:if>
											<s:if test="#fc.isUsed==1"><input type="button" value="已开启,点击关闭" onclick="changeIsused(<s:property value="#fc.id" />,0 ,this)"/></s:if>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="18" align="right" bgcolor="66b5ff" align="center">
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

