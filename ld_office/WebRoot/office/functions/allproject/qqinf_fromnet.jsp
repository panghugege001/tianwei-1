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
		<title>已收集qq信息查询</title>
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
/* 
function submitAction(btn){
   btn.disabled=true;
   var result = confirm("你确定要提交PT系统洗码?")
   if (result) {
     	 var action = "/batchxima/addPtXimaProposal.do";
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    );
  }
}

function responseMethod(data){
	alert(data.responseText);
}

function _UpdatePtData(){
	var executeTime = $("#_executeTime").val();
	$("#_executeUpdateBtn").attr("disabled", true);
	$.post("/office/updateJCDailyProfit.do",{"executeTime":executeTime},function(data){
		$("#_executeUpdateBtn").removeAttr("disabled");
			alert(data) ;
	});
} */
</script>
	</head>
	<body>
		<p>
			查询 --&gt; 已收集qq信息查询
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryQQInf" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<s:hidden name="pageIndex"/>
				<s:hidden name="by"/>
				<s:hidden name="order"/>
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										客户qq:
										<s:textfield id="qifqq" name="qif.qq" maxlength="12" size="10px;"></s:textfield>
									</td>
									<td>
										来源客服qq:
										<s:textfield id="qiffromcsqq" name="qif.fromCsQq" maxlength="12" size="10px;"></s:textfield>
									</td>
									<td>
										用户名:
										<s:textfield id="qifloginname" name="qif.loginname" maxlength="15" size="10px;"></s:textfield>
									</td>
									<td>
										每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										
									</td>

								</tr>
								<tr align="left">
									<td colspan="2">
										收集时间:
										<s:textfield name="queryTime_s" size="15" id="qifcollectTime"
											value="%{queryTime_s}"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											cssClass="Wdate"></s:textfield> -- 
										<s:textfield name="queryTime_e" size="15" id="qifcollectTime"
											value="%{queryTime_e }"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											cssClass="Wdate"></s:textfield>
									</td>
                                    <td colspan="2">
										<h3><p style="color: red;">${errorMsg }</p></h3>
										<!--  <input type="button" onclick="submitAction(this);" value="系统洗码"/> -->
									</td>
									<td>
										<s:submit cssStyle="width:60px; height:25px;" value="查询"></s:submit>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<div id="middle" style="position: absolute; top: 155px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1100px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="80px">
										客户qq
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="80px">
										客户email
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="80px">
										来源客服qq
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="100px">
										客户用户名
									</td>
									<!-- <td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('actual');" width="130px">
										投注额
									</td> -->
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('betSum');" width="80px">
										客户投注量
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('depositSum');" width="50px">
										客户存款量
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('win_los');" width="50px">
										输赢值
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('collectTime');" width="150px">
										收集时间
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr bgcolor="${bgcolor}">
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.qq" />
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.email"/>
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.fromCsQq"/>
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.loginname"/>
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.betSum"/>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.depositSum"/>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.win_los"/>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<fmt:formatDate value="${fc.collectTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
									</tr>
									<s:set var="pageOrderSumV" value="#fc.orderSum" scope="request"></s:set>
									<%-- <c:set var="ztzeSum" value="${ztzeSum+pageOrderSumV}"
										scope="request"></c:set>

									<s:set var="pageActualV" value="#fc.actual" scope="request"></s:set>
									<c:set var="zsySum" value="${zsySum+pageActualV}"
										scope="request"></c:set> 
										
									<s:set var="pageBonusV" value="#fc.bonus" scope="request"></s:set>
									<c:set var="bonusSum" value="${bonusSum+pageBonusV}"
										scope="request"></c:set>
										
									<s:set var="pageWinV" value="#fc.win" scope="request"></s:set>
									<c:set var="TztzeSum" value="${TztzeSum+pageWinV}"
										scope="request"></c:set> --%>
								</s:iterator>

								<%-- <tr bgcolor="#e4f2ff">
									<td align="right" colspan="3">
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
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.bonusSum)" />
									</td>
									<td align="right">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.TztzeSum)" />
									</td>
									<td align="right">

									</td>

								</tr>

								<tr bgcolor="#e4f2ff">
								<td align="right" colspan="3">
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
										 <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics3)" />
									</td>
									<td align="right">
											 <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics4)" />
									</td>
									<td align="right">

									</td>
								</tr> --%>

								<tr>
									<td colspan="21" align="right" bgcolor="66b5ff" align="center">
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

