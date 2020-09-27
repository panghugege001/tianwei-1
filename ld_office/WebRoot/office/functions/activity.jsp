<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<%@include file="/office/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>活动时间</title>
			<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
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
function functionOpen(id,activityStatus){
   var xmlhttp = new Ajax.Request(    
			"/office/updageActivityStatus.do",
		    {    
		         method: 'post',
		         parameters:"id="+id+"&type="+activityStatus+"&r="+Math.random(),
		         onComplete: responseMethod  
		    }
	);
}

function responseMethod(data){
	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}

</script>
	</head>
	<body>
	     <p>
		活动 --&gt; 活动时间管理
		<s:form action="queryActivity" onsubmit="submitonce(this);"
			namespace="/office" name="mainform" id="mainform" theme="simple">
			<div id="excel_menu"
				style="position: absolute; top: 25px; left: 0px;">
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
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
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="1" bgcolor="#99c8d7">
									<tr align="left">
										<td colspan="12" align="left">
											<table border="0" cellpadding="0" cellspacing="0"
												width="100%">
												<tr>
													<td align="right">
														活动名称:
													</td>
													<td width="80px" align="left">
														<s:textfield name="activityName" size="30" />
													</td>
													<td align="right">
														状态:
													</td>
													<td width="80px" align="left">
														<s:select cssStyle="width:80px"
											list="#{'':'','1':'开启','0':'关闭'}"
											name="type" listKey="key" listValue="value"
											emptyOption="false"></s:select>
													</td>
													<td align="right">
														创建开始时间:
													</td>
													<td width="110px" align="left">
														<s:textfield name="start" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right">
														创建结束时间:
													</td>
													<td width="200px" align="left">
														<s:textfield name="end" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
													</td>
													<td>
														<s:submit cssStyle="width:60px; height:40px;" value="查询"></s:submit>
													</td>
													<td>
														<a href="${ctx}/office/functions/addActivity.jsp">新增</a>
													</td>
												</tr>
											</table>
											<table width="100%" border="0" align="center"
												cellpadding="0" cellspacing="1" bgcolor="#99c8d7" style="margin-top: 10px;">
												<tr>
													<td bgcolor="#0084ff" align="center" width="60px"
														style="color: #FFFFFF; cursor: pointer; font-weight: bold" height="40px;">
														活动名称
													</td>
													<td bgcolor="#0084ff" align="center" width="60px"
														style="color: #FFFFFF; font-weight: bold; font-weight: bold">
														优惠比例
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; font-weight: bold; font-weight: bold">
														自助返水开始时间
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; cursor: pointer; font-weight: bold">
														自助返水结束时间
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; font-weight: bold; font-weight: bold">
														系统洗码开始时间
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; font-weight: bold; font-weight: bold">
														系统洗码结束时间
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; font-weight: bold;font-weight: bold">
														创建时间
													</td>
													<td bgcolor="#0084ff" align="center" width="90px"
														style="color: #FFFFFF; cursor: pointer; font-weight: bold">
														会员角色
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; cursor: pointer; font-weight: bold">
														备注
													</td>
													<td bgcolor="#0084ff" align="center" width="130px"
														style="color: #FFFFFF; cursor: pointer; font-weight: bold">
														操作
													</td>
													<td bgcolor="#0084ff" align="center" width="90px"
														style="color: #FFFFFF; font-weight: bold; font-weight: bold">
														操作
													</td>
												</tr>
												<s:iterator var="fc" value="%{#request.page.pageContents}">
													<tr>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;" height="30px;">
															${fc.activityName}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.activityPercent}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.activityStart}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.activityEnd}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.backstageStart}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.backstageEnd}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.createDate}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															<s:if test="#fc.userrole.contains(\"0\")">天兵、</s:if>
															<s:if test="#fc.userrole.contains(\"1\")">天将、</s:if>
															<s:if test="#fc.userrole.contains(\"2\")">天王、</s:if>
															<s:if test="#fc.userrole.contains(\"3\")">星君、</s:if>
															<s:if test="#fc.userrole.contains(\"4\")">真君、</s:if>
															<s:if test="#fc.userrole.contains(\"5\")">仙君、</s:if>
															<s:if test="#fc.userrole.contains(\"6\")">帝君、</s:if>
															<s:if test="#fc.userrole.contains(\"7\")">天尊、</s:if>
															<s:if test="#fc.userrole.contains(\"8\")">天帝、</s:if>
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															${fc.remark}
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															<a href="${ctx}/office/updateActivity.do?id=${fc.id}">修改</a>
														</td>
														<td align="center" bgcolor="#F0FFF0" align="center"
															width="60px" style="cursor: pointer;">
															<s:if test="#fc.activityStatus==0">
																<a href="javaScript:void;" onclick="functionOpen(${fc.id},1);">开启</a>
										    				</s:if>
															<s:if test="#fc.activityStatus==1">
															     <a href="javaScript:void;" onclick="functionOpen(${fc.id},0);">关闭</a>
										    				</s:if>
														</td>
													</tr>
												</s:iterator>
												<tr>
													<td colspan="11" align="right" bgcolor="66b5ff"
														align="center">
														${page.jsPageCode}
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:form>
	</body>
</html>
