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
		<title>管理银行账户</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.2.js"></script>
		<script type="text/javascript">
		
		function checkCard(accountNo){
			  $.ajax({
				  url:"/bankinfo/checkCard.do",
				  type:"post",
				  dataType:"text",
				  data:"accountno="+accountNo,
				  async:false,
				  success : function(msg){
					  alert(msg);
				  }
			  });
			}
		function gopage(val)
		{
		    document.mainform.pageIndex.value=val;
		    document.mainform.submit();
		}

</script>
	</head>
	<body>
		<p>
			账户 --&gt; 管理银行卡
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="querybankcard" namespace="/bankinfo" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1100px">
								<tr align="left">
									<td>
										账户名称:
										<s:textfield name="loginname" size="30" />
									</td>
									<td>
										银行开户人:
										<s:textfield name="username" size="30" />
									</td>
								    <td>
										银行卡号:
										<s:textfield name="accountno" size="30" />
									</td>
									<td>
										银行名称:
										<s:textfield name="bankname" size="30" />
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
										银行名称
									</td>
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold;">
										账号名称
									</td>
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold;">
										银行开户人
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行卡号
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										卡类型
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										卡所在城市
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										创建时间
									</td>
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold;">
										备注
									</td>
									<td align="center" width="160px" colspan="3"
										style="color: #FFFFFF; font-weight: bold;">
										操作
									</td>
								</tr>
								<c:set var="amountSum" value="0" scope="request"></c:set>
								<c:set var="amountSumBisuo" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}" >
									<tr bgcolor="#e4f2ff">
										<td align="center" width="80px">
											<s:property value="#fc.bank" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="70px">
											<s:property value="#fc.accountName" />
										</td>
										
										<td align="center" width="80px">
										    <s:property value="#fc.accountNo" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.type" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.accountCity" />
										</td>
									
										<td align="center" width="80px">
											<s:property value="#fc.createTime" />
										</td>

										<td align="center" width="140px">
											<s:property value="#fc.remark" />
										</td>
										<td align="center" width="35px">
											<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='card' ">
											<c:url var="action" value="/office/auditProposal.do"
												scope="request" />
											<input type="button" value="检查卡" onclick="checkCard('${fc.accountNo}');" />
										</s:if>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="9" align="right" bgcolor="66b5ff" align="center">
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

