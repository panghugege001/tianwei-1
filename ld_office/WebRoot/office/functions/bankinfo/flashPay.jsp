<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>秒付宝提款</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
			function gopage(val)
			{
			    document.mainform.pageIndex.value=val;
			    document.mainform.submit();
			}
			
			function updateFlashOrder(pno){
				$.post("/office/updateFlashPayOrder.do",{"jobPno":pno},function(data){
					alert(data);
					document.mainform.submit();
				});
			}
			
			//重新提交
			function resubmit(pno){
				$.post("/office/resubmit.do",{"jobPno":pno},function(data){
					alert(data);
					document.mainform.submit();
				});
			}
			
        </script>
	</head>
	<body>
		<p>
			账户 --&gt; 秒付宝提款记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryFlashPayOrder" namespace="/bankinfo" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1100px">
								<tr align="left">
									<td>
										玩家账户:
										<s:textfield name="username" size="15" />
									</td>
									<td>
										提案号:
										<s:textfield name="pno" size="25" />
									</td>
									
									<td>
										订单号:
										<s:textfield name="orderNo" size="25" />
									</td>
									<td>
										状态:
										<s:select list="#{'':'',0:'待处理' , 1:'已提交',2:'成功',-1:'提交失败',-2:'处理失败' }" listKey="key" listValue="value" name="flag"></s:select>
									</td>
									<td width="110px" align="left">
											开始时间:<s:textfield name="start" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{startTime}" cssClass="Wdate" />
									</td>
									<td width="110px" align="left">
										   结束时间:<s:textfield name="end" size="18"
										   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										   My97Mark="false" value="%{endTime}" cssClass="Wdate" />
									</td>
									
									<td>
										每页:
										<s:select cssStyle="width:120px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										<s:submit value="查询"></s:submit>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<s:hidden name="pageIndex" />
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="jobPno"></s:hidden>
				<s:hidden name="remark"></s:hidden>
			</s:form>
		</div>
		
		
		<div id="middle" style="position: absolute; top: 125px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1500px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										提案号
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										订单号
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										账号
									</td>
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold;">
										金额
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										持卡人姓名
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行名称
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行卡号
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										创建时间
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										提款状态
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										更新时间
									</td>
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold;">
										备注
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										操作
									</td>
								</tr>
								<c:set var="amountSum" value="0" scope="request"></c:set>
								<c:set var="amountSumBisuo" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}" >
									<tr bgcolor="#e4f2ff">
										<td align="center" width="80px">
											<s:property value="#fc.pno" />
										</td>
										<td align="center" width="70px">
											<s:property value="#fc.billno" />
										</td>
										<td align="center" width="80px">
										   <s:property value="#fc.loginname" />
										</td>
										<td align="center" width="80px">
											 <s:property value="#fc.amout" />
										</td>
										<td align="center" width="80px">
											 <s:property value="#fc.accountName" />
										</td>
										<td align="center" width="80px">
											 <s:property value="#fc.bankname" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.card_number" />
										</td>
										<td align="center" width="80px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
										</td>
										<td align="center" width="80px">
											<s:if  test="#fc.flag==0" >待处理</s:if>
											<s:else><s:if  test="#fc.flag==1" >已提交</s:if></s:else>
											<s:else><s:if  test="#fc.flag==2" >成功</s:if></s:else>
											<s:else><s:if  test="#fc.flag==-1" >提交失败</s:if></s:else>
											<s:else><s:if  test="#fc.flag==-2" >回调失败</s:if></s:else>
										</td>
										<td align="center" width="80px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updateTime" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.reamrk" />
										</td>
										<td align="center" width="80px">
											<s:if  test="#fc.flag == 1" ><input type="button" value="更新订单" onclick="updateFlashOrder('<s:property value="#fc.pno" />')"/></s:if>
											<s:if  test="#fc.flag == -1" >
											    <input type="button" value="重新提交" onclick="resubmit('<s:property value="#fc.pno" />')"/>
											</s:if>
											<%--  <s:if  test="#fc.flag == 0" >
											      <input type="button" value="重新提交" onclick="resubmit('<s:property value="#fc.pno" />')"/>
											 </s:if> --%>
										</td>
									</tr>
									<s:set var="amountValue" value="#fc.amout" scope="request"></s:set>
									<c:set var="amountSum" value="${amountSum+amountValue}" scope="request">
									</c:set>
								</s:iterator>
								<tr>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="3">
										当页小计:
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
										<s:property
											value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)" />
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="8"></td>
								</tr>
								<tr>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="3">
										总计:
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
										<s:property
											value="@dfh.utils.NumericUtil@formatDouble(#request.page.statics1)" />
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="8"></td>
								</tr>
								<tr>
									<td colspan="12" align="right" bgcolor="66b5ff" align="center">
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

