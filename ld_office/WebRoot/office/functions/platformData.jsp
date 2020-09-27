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
		<title>提案</title>
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
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
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

function updateYesterdaySixlottery(){
	$(".updateBtn").attr("disabled",true);
	$.post("/office/updateYesterdaySixLottery.do",{},function(data){
		alert(data);
		$(".updateBtn").removeAttr("disabled");
	});
}

function updateYesterdayQT(){
	$(".updateBtn").attr("disabled",true);
	$.post("/office/updateYesterdayQT.do",{"type": $("#type").val()},function(data){
		alert(data);
		$(".updateBtn").removeAttr("disabled");
	});
}

function deletedata(id){
	if(confirm('你确认要执行此操作么？')){
		window.location.href="${ctx}/office/deletePlatformData.do?uuid="+id+"";
	}
}
</script>
	</head>
	<body>
		<p>
			风险控制 --&gt; 平台信息分析
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getPlatformDataList" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										游戏帐号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
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
										游戏平台:
										<s:select cssStyle="width:115px"
											list="#{'':'','ea':'ea','ag':'ag','agin':'agin','aginfish':'aginfish','aginslot':'aginslot','bbin':'bbin','pttiger':'pttiger','ptall':'ptall','keno':'keno','kg':'kg','ssc':'ssc','pk10':'pk10','sb':'sb','sixlottery':'sixlottery','ttg':'ttg',
												'bc':'bc','hk49':'hk49','qt':'qt','dt':'dt','mg':'mg','png':'png'}"
											name="platform" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="endPt" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{endPt}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'starttime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td>
										<input type="button" value="更新昨天六合彩" onclick="updateYesterdaySixlottery();" class="updateBtn"/>
										<s:textfield id="type" name="type" size="10" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="" cssClass="Wdate" />
										<input type="button" value="更新QT" onclick="updateYesterdayQT();" class="updateBtn"/>
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
										title="点击排序" onclick="orderby('platform');" width="130px">
										游戏平台
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('loginname');" width="130px">
										游戏账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bet');" width="130px">
										玩家总投注额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('profit');" width="130px">
										公司总输赢值
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('starttime');" width="130px">
										开始时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('endtime');" width="130px">
										结束时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('updatetime');" width="130px">
										更新时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
										操作
									</td>
								</tr>
								<c:set var="ztzeSum" value="0" scope="request"></c:set>
								<c:set var="zsySum" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<s:if test="#fc.profit<=-50000">
										<c:set var="bgcolor" value="#FF9999" />
									</s:if>
									<s:else>
										<c:set var="bgcolor" value="#e4f2ff" />
									</s:else>
									<tr bgcolor="${bgcolor}">
										<td align="center" width="60px">
											<s:property value="#fc.platform" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.bet)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.profit)" />
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.starttime" />
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endtime" />
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updatetime" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.platform == 'sixlottery'">
												<a href="${ctx}/office/getPlatformData.do?uuid=${fc.uuid}" target="_blank">修改</a>&nbsp;<a href="javascript:deletedata('${fc.uuid}');" >删除</a>
											</s:if>
										</td>
									</tr>
									<s:set var="amountValue" value="#fc.bet" scope="request"></s:set>
									<c:set var="ztzeSum" value="${ztzeSum+amountValue}"
										scope="request"></c:set>

									<s:set var="amountPayOutValue" value="#fc.profit"
										scope="request"></s:set>
									<c:set var="zsySum" value="${zsySum+amountPayOutValue}"
										scope="request"></c:set>
								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="2">
										当页小计:
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.ztzeSum)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.zsySum)" />
									</td>
									<td align="right">
									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
								</tr>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="2">
										总计:
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
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

