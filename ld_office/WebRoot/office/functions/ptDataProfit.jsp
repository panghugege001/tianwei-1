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
		<title>PT平台游戏记录</title>
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
</script>
	</head>
	<body>
		<p>
			记录 --&gt; PT平台游戏记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getPtDataList" namespace="/office" name="mainform"
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
										<s:textfield name="start" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="%{start}" cssClass="Wdate" />
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
										代理账号:
										<s:textfield cssStyle="width:115px" name="agent" size="20" />
									</td>
									<td>
										结束时间:
										<s:textfield name="end" id="end" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="%{end}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'creattime'" />
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
										title="点击排序" onclick="orderby('playername');" width="80px">
										用户名
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('code');" width="90px">
										代码
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('currencycode');" width="90px">
										货币
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('activeplayers');" width="90px">
										玩家数量
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('balancechange');" width="90px">
										余额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('deposits');" width="90px">
										存款
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('withdraws');" width="90px">
										提款
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bonuses');" width="90px">
										奖金
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('comps');" width="90px">
										积分
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('progressivebets');" width="130px">
										奖池投注
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('progressivewins');" width="130px">
										奖池赢得
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bets');" width="130px">
										投注额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('houseearnings');" width="130px">
										净赚
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('wins');" width="130px">
										赢得
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('netpurchase');" width="130px">
										存入金额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('netgaming');" width="130px">
										使用金额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('netloss');" width="130px">
										净赚
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('rnum');" width="130px">
										无意义
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('starttime');" width="130px">
										开始时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('endtime');" width="130px">
										结束时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('creattime');" width="130px">
										更新时间
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr bgcolor="${bgcolor}">
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.playername" />
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.code}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.currencycode}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.activeplayers}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.balancechange}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.deposits}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.withdraws}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.bonuses}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.comps}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.progressivebets}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.progressivewins}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.bets}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.houseearnings}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.wins}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.netpurchase}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.netgaming}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.netloss}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.rnum}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.starttime}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.endtime}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.creattime}
										</td>
									</tr>
									
									<s:set var="progressivebetsValue" value="#fc.progressivebets" scope="request"></s:set>
									<c:set var="progressivebetsSum" value="${progressivebetsSum+progressivebetsValue}"
										scope="request"></c:set>	
										
									<s:set var="progressivewinsValue" value="#fc.progressivewins" scope="request"></s:set>
									<c:set var="progressivewinsSum" value="${progressivewinsSum+progressivewinsValue}"
										scope="request"></c:set>	
																			
									<s:set var="amountValue" value="#fc.bets" scope="request"></s:set>
									<c:set var="ztzeSum" value="${ztzeSum+amountValue}"
										scope="request"></c:set>

									<s:set var="amountPayOutValue" value="#fc.houseearnings" scope="request"></s:set>
									<c:set var="zsySum" value="${zsySum+amountPayOutValue}"
										scope="request"></c:set> 
								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="9">
										当页小计:
									</td>
									<td align="right">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.progressivebetsSum)" />
									</td>
									<td align="right">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.progressivewinsSum)" />
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
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">
									</td>
								</tr>

								<tr bgcolor="#e4f2ff">
								   <td align="right" colspan="9">
										总计:
									</td>
								    <td align="right" >
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics3)" />
									</td>
									<td align="right">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics4)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
									</td>
									<td align="center">
									     <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

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
		
