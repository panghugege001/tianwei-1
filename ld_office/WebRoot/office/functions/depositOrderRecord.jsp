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
		<title>存款订单附言记录</title>
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

//废弃秒存存款订单
function discardOrder(btn, id){
    btn.disabled=true;
    var action = "/office/updateDepositOrder.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"ids="+id,
            onComplete: responseMethod
        }
    );
}

function responseMethod(data){
    alert(data.responseText);
    var frm=document.getElementById("mainform");
    frm.submit();
}


function responseMethod(data){
	alert(data.responseText);
}

</script>
	</head>
	<body>
		<p>
			--&gt; 存款订单附言录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getDepositOrderRecords" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										附言:
										<s:textfield cssStyle="width:115px" name="notes" size="20" />
									</td>
									<td>
										玩家帐号:
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
									<td colspan="2">
										银行类型:
										<s:select cssStyle="width:115px"
											list="#{'':'','工商银行':'工商银行','招商银行':'招商银行','支付宝':'支付宝','农业银行':'农业银行','微信':'微信','通联转账':'通联转账','云闪付':'云闪付'}"
											name="platform" listKey="key" listValue="value"
											emptyOption="false"></s:select>
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
										真实姓名:
										<s:textfield cssStyle="width:115px" name="username" size="20" />
									</td>
									<td>
										<a href="/office/functions/createDepositOrder.jsp" target="_blank" style="color:red;font-size: 14px;">创建存款附言订单</a>
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
										title="点击排序" onclick="orderby('notes');" width="130px">
										附言
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bankname');" width="130px">
										银行类型
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('loginname');" width="130px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('loginname');" width="130px">
										真实姓名
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bankno');" width="130px">
										银行卡号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										  width="130px">
										额度
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('status');" width="130px">
										状态
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										更新时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										备注
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										操作
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									
									
									<tr >
										<td align="center" width="60px">
											<s:property value="#fc.depositId" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.bankname" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.realname" />
										</td>
										<td align="center" width="90px">
											<s:property value="@dfh.utils.StringUtil@subStrBefore(#fc.bankno, 4)" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.spare" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.status==0">未处理</s:if>
											<s:if test="#fc.status==1">成功</s:if>
											<s:if test="#fc.status==2">已过期</s:if>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.remark" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.status==0">
												<c:if test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_leader' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'sale'  or sessionScope.operator.authority eq 'sale_manager'    or sessionScope.operator.authority eq 'boss'}">
													<input type="button" value="作废" onclick="discardOrder(this,'${fc.depositId}');"/>
												</c:if>
											</s:if>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="10" align="right" bgcolor="66b5ff" align="center">
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

