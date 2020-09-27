<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>同略云订单记录</title>
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

function updateTlyEditAmount(pno, td){
	var tr = td.parentElement.parentElement;
	var $tr = $(tr); 
	var editamount = $tr.find("input[name=editAmount]").val();
	if($.trim(editamount)=='')return;
	if(confirm("确定额度记录!")){
		var xmlhttp = new Ajax.Request("/office/updateTlyEditAmount.do",
		        {    
		            method: 'post',
		            parameters:"pno=" + pno + "&amount=" + editamount + "&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}
}

function responseMethod(data){
	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}

</script>
		<s:form action="queryTlyPayOrder" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div>
				账户 --&gt; 同略云存款订单记录
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>

			<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">   

				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createtime'" />
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
										<td colspan="8" align="left">
											<table border="0" cellpadding="0" cellspacing="0"
												width="1000px">

												<tr>
                                                   <c:if test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'finance_leader' or sessionScope.operator.username eq 'ck'}">
                                                                <a href="/office/functions/tlyOrderModity.jsp" target="_blank">补单功能  </a>													
                                                  </c:if>													
                                                  <td align="right" width="60px">
														订单状态:
													</td>
													<td width="80px" align="left">
														<s:select cssStyle="width:80px" name="status"
															list="#{'':'全部','0':'未匹配','1':'已处理','2':'已过期'}" listKey="key" listValue="value" />
													</td>
													<td align="right" width="60px">
														开始时间:
													</td>
													<td width="110px" align="left">
														<s:textfield name="start" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													
													<td align="right" width="60px">
														会员帐号:
													</td>
													<td width="110px" align="left">
														<s:textfield name="loginname" size="15" />
													</td>
													<td align="right" width="60px">
														类型:
													</td>
													<td width="110px" align="left">
														<s:select cssStyle="width:100px" name="type"
															list="#{'':'全部','8':''}" listKey="key" listValue="value" />
													</td>
													
													
													<td rowspan="3" width="60px">
														<s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit>
													</td>
												</tr>
												<tr>
													<td align="right" width="60px">
														每页:
													</td>
													<td width="80px" align="left">
														<s:select cssStyle="width:80px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
													<td align="right" width="60px">
														结束时间:
													</td>
													<td width="110px" align="left">
														<s:textfield name="end" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														指定存款金额:
													</td>
													<td width="110px" align="left">
														<s:textfield name="amount" size="15" />
													</td>
													<td align="right" width="60px">
														存款姓名:
													</td>
													<td width="80px" align="left">
														<s:textfield name="username" size="15" />
													</td>
													
												</tr>

											</table>
										</td>
									</tr>
									</table>
									
									<table width="1250px" border="0" align="center" cellpadding="0"cellspacing="1" bgcolor="#99c8d7">
									<tr>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">
											单号
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">
											附言
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">
											会员帐号
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">
										    存款姓名
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >
											存款金额
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('createTime');">
											下单时间
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('status');">
											状态
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('arriveTime');">
											到帐时间
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >
											存入卡号
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >
											收款户名
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >
											类型
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >
											 备注
										</td>
										<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >
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
												ld_<s:property value="#fc.loginname" />_7_<s:property value="@dfh.utils.StringUtil@getSplitStirng(#fc.ubankname, '_',0)" />_<s:property value="#fc.depositId" /> 
											</td>
											<td bgcolor="${bgcolorValue }" align="center" >
												<s:property value="#fc.depositId" />
											</td>
											<td bgcolor="${bgcolorValue}" align="center">
												<s:property value="#fc.loginname" />
											</td>
											<td bgcolor="${bgcolorValue}" align="center">
												<s:property value="#fc.uaccountname" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
<%-- 												<s:if test="#fc.status==0 && (#session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance_leader' || #session.operator.authority=='boss')"> --%>
<%-- 											        <td><input type="text" name="editAmount" value="<s:property value='@dfh.utils.NumericUtil@formatDouble(#fc.amount)'/>" /></td> --%>
<%-- 											    </s:if> --%>
<%-- 											    <s:else> --%>
											        <s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" />
<%-- 											    </s:else> --%>
												
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:if test="#fc.status==0">
								              		未匹配
								              	</s:if>
								              	<s:elseif test="#fc.status==1">
								              		已处理
								             	</s:elseif>
								             	<s:else>
								              		过期/作废
	              								</s:else>
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updatetime" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="@dfh.utils.StringUtil@subStrBefore(#fc.bankno, 4)" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="#fc.accountname" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
	              								<s:if test="#fc.type==5">
								              		云闪付
	              								</s:if>
											</td>
											
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="#fc.remark" />
											</td>
											
											<td bgcolor="${bgcolorValue }" align="center"> 
												<s:if test="#fc.status==0">
												   <c:if test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'finance_leader' or sessionScope.operator.authority eq 'boss'}">
													    <input type="button" value="作废" onclick="discardOrder(this,'${fc.depositId}');"/>
													</c:if>
<%-- 													<c:if test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'finance_leader'  or sessionScope.operator.authority eq 'boss'}"> --%>
<%-- 													    <input type="button" value="额度修改" onclick="updateTlyEditAmount('<s:property value="#fc.depositId"/>', this)" /> --%>
<%-- 													</c:if> --%>
													
												</s:if>
											</td>
											<td></td>
											<td></td>
										</tr>
										<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
										<c:set var="amountSum" value="${amountSum+amountValue}"
											scope="request"></c:set>
									</s:iterator>
									<tr>
										<td bgcolor="#e4f2ff" align="center">
											当页小计:
										</td>
										<td bgcolor="#e4f2ff" align="center">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="11"></td>
									</tr>
									<tr>
										<td bgcolor="#e4f2ff" align="center">
											总计:
										</td>
										<td bgcolor="#e4f2ff" align="center">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="11"></td>
									</tr>
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

