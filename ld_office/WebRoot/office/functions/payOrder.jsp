<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>在线支付记录</title>
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

//优惠劵审核
function submitAction(btn,billno){
   btn.disabled=true;
   var action = "/office/getSubmitPayAction.do";
   var remark=window.prompt("您是否要提交，并填写备注(可以默认为空)","");
   if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&billno="+billno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function submitCancelAction(btn,billno){
   btn.disabled=true;
   var action = "/office/submitPayCancelAction.do";
   var remark=window.prompt("您是否要取消，并填写备注(可以默认为空)","");
   if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&billno="+billno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}else{
		btn.disabled=false;
	}	
}

function responseMethod(data){
	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}

</script>
		<s:form action="queryPayOrder" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div>
				账户 --&gt; 在线支付记录
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>

			<div id="excel_menu"
				style="position: absolute; top: 25px; left: 0px;">
				<!-- 
类型:<s:select name="newaccount" list="#{'1':'新开户','0':'充值'}" emptyOption="true"/>
 -->

				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createTime'" />
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
												width="1000px">

												<tr>
													<td width="80px" align="left">
														订单状态:<s:select cssStyle="width:80px" name="payOrderFlag"
															list="%{#application.PayType}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
													<td width="110px" align="left">
														开始时间:<s:textfield name="start" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td width="200px" align="left">
														支付单号:<s:textfield name="billno" size="30" />
													</td>
													<td width="110px" align="left">
														会员帐号:<s:textfield name="loginname" size="15" />
													</td>
													<td width="110px" align="left">
														会员姓名:<s:textfield name="accountName" size="15" />
													</td>
													<td rowspan="3" width="60px">
														<s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit>
													</td>
												</tr>
												<tr>
													<td width="80px" align="left">
														每页:<s:select cssStyle="width:80px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
													<td width="110px" align="left">
														结束时间:<s:textfield name="end" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
													</td>
													<td align="left" width="200px">
														代理网址:<s:textfield name="referWebsite" size="30"></s:textfield>
													</td>
													<td width="110px" align="left">
														商户类型:<s:select cssStyle="width:125px" name="billnotype"
															list="%{#application.NetPayBusinessList}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
												</tr>

											</table>
										</td>
									</tr>
									<tr>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('billno');">
											支付单号
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											支付平台
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											状态
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											支付类型
										</td>
										<td bgcolor="#0084ff" align="center" width="130px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('loginname');">
											会员帐号
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											会员姓名
										</td>
										<td bgcolor="#0084ff" align="center" width="90px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('money');">
											金额
										</td>
<%-- 										<c:if test="${sessionScope.operator.authority eq 'boss'}">
											<td bgcolor="#0084ff" align="center" width="80px"
												style="color: #FFFFFF; font-weight: bold">
												电话
											</td>
											<td bgcolor="#0084ff" align="center" width="150px"
												style="color: #FFFFFF; font-weight: bold">
												邮箱
											</td>
										</c:if> --%>
										<td bgcolor="#0084ff" align="center" width="90px"
											style="color: #FFFFFF; font-weight: bold">
											来源IP
										</td>
										<td bgcolor="#0084ff" align="center" width="130px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('createTime');">
											加入时间
										</td>
										<td bgcolor="#0084ff" align="center" width="130px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('returnTime');">
											支付时间
										</td>
										<td bgcolor="#0084ff" align="center" width="90px"
											style="color: #FFFFFF; font-weight: bold">
											说明
										</td>
										<td bgcolor="#0084ff" align="center" width="130px"
											style="color: #FFFFFF; font-weight: bold">
											操作
										</td>
									</tr>
									<c:set var="amountSum" value="0" scope="request"></c:set>
									<s:iterator var="fc" value="%{#request.page.pageContents}">
										<s:if test="#fc.newaccount==@dfh.utils.Constants@FLAG_TRUE">
											<%
												request.setAttribute("bgcolorValue", "#FF99FF");
											%>
										</s:if>
										<s:else>
											<%
												request.setAttribute("bgcolorValue", "#e4f2ff");
											%>
										</s:else>
										<s:if test="#fc.money>=50000">
											<%
												request.setAttribute("bgcolorValue", "#33FFFF");
											%>
										</s:if>
										<tr>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="#fc.billno" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="#fc.payPlatform" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property
													value="@dfh.model.enums.PayType@getText(#fc.type)" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:if test="#fc.newaccount==@dfh.utils.Constants@FLAG_TRUE"> 新开户</s:if>
												<s:else>充值</s:else>
											</td>
											<td bgcolor="${bgcolorValue }" align="left">
												<s:property value="#fc.loginname" />
											</td>
											<td bgcolor="${bgcolorValue }" align="left">
												<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
													<s:property value="#fc.aliasName"/>
												</s:if>
												<s:else>
													<s:property value="#fc.aliasName.substring(0,1)+'**'"/>
												</s:else> --%>
												<s:property value="#fc.aliasName"/>
											</td>
											<td bgcolor="${bgcolorValue }" align="right">
												<s:property
													value="@dfh.utils.NumericUtil@formatDouble(#fc.money)" />
											</td>
<%-- 											<c:if test="${sessionScope.operator.authority eq 'boss'}">
												<td bgcolor="${bgcolorValue }" align="left">
													<s:property value="#fc.phone" />
												</td>
												<td bgcolor="${bgcolorValue }" align="left">
													<s:property value="#fc.email" />
												</td>
											</c:if> --%>
											<td bgcolor="${bgcolorValue }" align="left">
												<s:property value="#fc.ip" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.returnTime" />
											</td>
											<td bgcolor="${bgcolorValue }" align="left">
												<s:property value="#fc.msg" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:if test="#fc.type==1">
												   <c:if test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'boss'}">
													    <input type="button" value="审核" onclick="submitAction(this,'${fc.billno}');"/>
													    <input type="button" value="取消" onclick="submitCancelAction(this,'${fc.billno}');"/>
													</c:if>
												</s:if>
											</td>
										</tr>
										<s:set var="amountValue" value="#fc.money" scope="request"></s:set>
										<c:set var="amountSum" value="${amountSum+amountValue}"
											scope="request"></c:set>
									</s:iterator>
									<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager' || (#request.loginname != null && #request.loginname != '')">
									<tr>
										<td bgcolor="#e4f2ff" align="right" colspan="8">
											当页小计:
										</td>
										<td bgcolor="#e4f2ff" align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="5"></td>
									</tr>
									<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager'">   --%>
									<tr>
										<td bgcolor="#e4f2ff" align="right" colspan="8">
											总计:
										</td>
										<td bgcolor="#e4f2ff" align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="5"></td>
									</tr>
									</s:if>
									<tr>
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

