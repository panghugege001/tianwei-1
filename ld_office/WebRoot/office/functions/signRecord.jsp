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
		<title>玩家签到记录</title>
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

function clearErrortimes(loginname){
	if(confirm("是否确定重置？")){
		$.post("/office/clearErrorTimes.do",{"loginname":loginname},function(data){
			alert(data);
		});
	}
}
		
</script>
	</head>
	<body>
		<p>
			--&gt; 签到记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getSignRecords" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1250px">
								<tr align="left">
									<td>
										玩家帐号:
										<s:textfield cssStyle="width:115px" name="username" size="20" />
									</td>
									<td align="left" width="60px">
														开始时间:
													</td>
													<td width="80px" align="left">
														<s:textfield name="startTime" size="18" 
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="70px">
														结束时间:
													</td>
													<td align="left">
														<s:textfield name="endTime" size="18" cssStyle="width:150px" 
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										</td>
									<td colspan="2">
										每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td colspan="2">
										奖金类型:
										<s:select cssStyle="width:150px" list="#{'':'所有奖金明细',0:'签到奖金',1:'存款奖金',2:'流水奖金','money':'玩家奖金池余额'}" key="key"   value="%{type}"
								 name='type'></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
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
							<table width="1210px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('username');" width="130px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('level');" width="130px">
										玩家等级
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('type');" width="130px">
										奖金类型
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										奖金
									</td>
		              				<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										玩家当月签到天数
									</td>
									<td align="center" onclick="orderby('updatetime');"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										  width="130px">
										更新时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="680px">
										备注
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									
									
									<tr >
										<td align="center" width="60px">
											<s:property value="#fc.username" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.isused" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.createtime" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.type==0">
												签到奖金
											</s:if>
											<s:elseif test="#fc.type==1">
												存款奖金
											</s:elseif>
											<s:elseif test="#fc.type==2">
												流水奖金
											</s:elseif>
											<s:else>
												奖金余额
											</s:else>
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.amount" />
										</td>
										<td align="center" width="90px">
											<%-- <s:if test="#fc.type==0">
												无内容
											</s:if>
											<s:else> --%>
												<s:property value="#fc.monthCount" />
											<%-- </s:else> --%>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updatetime" />
										</td>
										<td align="center" width="680px">
											<s:property value="#fc.remark" />
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="8" align="right" bgcolor="66b5ff" align="center">
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

