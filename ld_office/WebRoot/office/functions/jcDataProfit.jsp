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
		<title>JC平台游戏输赢记录</title>
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
}
</script>
	</head>
	<body>
		<p>
			记录 --&gt; JC平台游戏输赢记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getJCDailyData" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										用户名:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td>
										开始时间:
										<s:textfield name="startDate" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false"  cssClass="Wdate" />
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

									</td>
									<td>
										结束时间:
										<s:textfield name="endDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false"  cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createTime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
                                    <td>
										<!--  <input type="button" onclick="submitAction(this);" value="系统洗码"/> -->
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
		<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'finance' || sessionScope.operator.authority eq 'finance_manager'}">
			<div  style="position: absolute; top: 120px; left: 0px;">
						更新时间:<s:textfield  id="_executeTime" size="18"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
						My97Mark="false"  cssClass="Wdate"  />
						
						<input type="button" id="_executeUpdateBtn" value="执行" onclick="_UpdatePtData();"/>
			</div>
		</c:if>
			<!-- <div  style="position: absolute; top: 120px; float: left; left:400px;">
				<s:form action="addPtTigerGameData" namespace="/office" method ="post" enctype="multipart/form-data">
					文件:<input type="file" name="file"/>		
					<input type="submit" value="上传"/>	
				</s:form>
			</div> -->
		<br />
		<br />
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
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('playerName');" width="80px">
										用户名
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('startTime');" width="130px">
										开始时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('endTime');" width="130px">
										结束时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('orderSum');" width="90px">
										总投注数
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('actual');" width="130px">
										投注额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bonus');" width="130px">
										中奖金额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('win');" width="130px">
										平台赢得
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('updateTime');" width="130px">
										更新时间
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr bgcolor="${bgcolor}">
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.playerName" />
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.startTime}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.endTime}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.orderSum}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.actual}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.bonus}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.win}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.updateTime}
										</td>
									</tr>
									<s:set var="pageOrderSumV" value="#fc.orderSum" scope="request"></s:set>
									<c:set var="ztzeSum" value="${ztzeSum+pageOrderSumV}"
										scope="request"></c:set>

									<s:set var="pageActualV" value="#fc.actual" scope="request"></s:set>
									<c:set var="zsySum" value="${zsySum+pageActualV}"
										scope="request"></c:set> 
										
									<s:set var="pageBonusV" value="#fc.bonus" scope="request"></s:set>
									<c:set var="bonusSum" value="${bonusSum+pageBonusV}"
										scope="request"></c:set>
										
									<s:set var="pageWinV" value="#fc.win" scope="request"></s:set>
									<c:set var="TztzeSum" value="${TztzeSum+pageWinV}"
										scope="request"></c:set>
										

								</s:iterator>

								<tr bgcolor="#e4f2ff">
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
								</tr>

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

