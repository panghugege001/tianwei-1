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
		<title>积分记录</title>
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


		
</script>
	</head>
	<body>
		<p>
			--&gt; 好友推荐
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getFriendintroduce" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1450px">
								<tr align="left">
									<td>
										玩家帐号:
										<s:textfield cssStyle="width:80px" name="topusername" size="20" />
									</td>
									<td>
										下线玩家帐号:
										<s:textfield cssStyle="width:80px" name="downusername" size="20" />
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
										<s:select cssStyle="width:60px"
											list="%{#application.PageSizes}" name="size"></s:select>
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td colspan="2">
										类别:
										<s:select cssStyle="width:150px" list="#{0:'玩家推荐记录',3:'玩家奖金余额','':'全部奖金记录',1:'获得奖金记录',2:'使用奖金记录'}" key="key"   value="%{type}"
								 name='type'></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:22px;" value="查询"></s:submit>
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
		<div id="middle" style="position: absolute; top: 100px; left: 0px">
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
										title="点击排序" onclick="orderby('toplineuser');" width="130px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序"  width="130px">
										下线账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="180px">
										更新时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('type');" width="130px">
										类别
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('sumamount');" width="130px">
										金额(元)
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										t width="960px">
										备注
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr >
										<td align="center" width="60px">
											<s:property value="#fc.toplineuser" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.downlineuser" />
										</td>
										<td align="center" width="180px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.type==0">
												推荐记录
											</s:if>
											<s:elseif test="#fc.type==1">
												获取奖金记录
											</s:elseif>
											<s:elseif test="#fc.type==2">
												消费奖金记录
											</s:elseif>
											<s:elseif test="#fc.type==3">
												奖金余额
											</s:elseif>
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.money" />
										</td>
										<td align="center" width="960px">
											<s:property value="#fc.remark" />
										</td>
									</tr>
									<s:if test="#fc.money != null">
									<c:set var="moneySum" value="${moneySum + fc.money}" scope="request"></c:set>
									</s:if>
								</s:iterator>
								<tr>
									<td colspan="4"  bgcolor="66b5ff" align="center">合计</td>
									<td   bgcolor="66b5ff" align="center">
									  	 <s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" /> 
									</td>
									<td colspan="2" align="center" bgcolor="66b5ff" align="center"></td>
								</tr>
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

