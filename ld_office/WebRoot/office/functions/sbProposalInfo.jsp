<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>188体育优惠记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
	</head>
	<body>
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

</script>
		<s:form action="getSbListInfo" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div>
				账户 --&gt; 188体育优惠记录
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>

			<div id="excel_menu"
				style="position: absolute; top: 25px; left: 0px;">
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createtime'" />
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
								<table width="1250px" border="0" align="center" cellpadding="0"
									cellspacing="1" bgcolor="#99c8d7">
									<tr align="left">
										<td colspan="12" align="left">
											<table border="0" cellpadding="0" cellspacing="0"
												width="100%">
												<tr>
													<td align="right" width="60px">
														188体育帐号:
													</td>
													<td width="110px" align="left">
														<s:textfield name="loginname" size="15" />
													</td>
													<td align="right" width="60px">
														开始时间:
													</td>
													<td width="110px" align="left">
														<s:textfield name="start" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														结束时间:
													</td>
													<td width="110px" align="left">
														<s:textfield name="end" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														每页:
													</td>
													<td width="80px" align="left">
														<s:select cssStyle="width:80px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
													<td rowspan="3" width="60px">
														<s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit>
													</td>
												</tr>

											</table>
										</td>
									</tr>
									<tr>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('sbinfo');">
											单号
										</td>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('loginname');">
											账号
										</td>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('sbbets');">
											投注
										</td>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('createtime');">
											美东时间
										</td>
										<td bgcolor="#0084ff" align="center"
											style="color: #FFFFFF; font-weight: bold">
											备注
										</td>
									</tr>
									<c:set var="amountSum" value="0" scope="request"></c:set>
									<s:iterator var="fc" value="%{#request.page.pageContents}">
										<tr>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.sbinfo" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.loginname" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property
													value="@dfh.utils.NumericUtil@formatDouble(#fc.sbbets)" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.createtime" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.sbremark" />
											</td>
										</tr>
										<s:set var="amountValue" value="#fc.sbbets" scope="request"></s:set>
										<c:set var="amountSum" value="${amountSum+amountValue}"
											scope="request"></c:set>
									</s:iterator>
									<tr>
										<td bgcolor="#e4f2ff" align="right" colspan="2">
											当页小计:
										</td>
										<td bgcolor="#e4f2ff" align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="2"></td>
									</tr>
									<tr>
										<td bgcolor="#e4f2ff" align="right" colspan="2">
											总计:
										</td>
										<td bgcolor="#e4f2ff" align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="2"></td>
									</tr>
									<tr>
										<td colspan="13" align="right" bgcolor="66b5ff" align="center">
											${page.jsPageCode}
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>

