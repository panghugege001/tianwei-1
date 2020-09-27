<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>188体育优惠记录</title>
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
function submitAction(btn,id){
   btn.disabled=true;
   var action = "/office/getSubmitSbAction.do";
   var remark=window.prompt("您是否要提交，并填写备注(可以默认为空)","");
   if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&id="+id+"&r="+Math.random(),
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
		<s:form action="querySb" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div>
				账户 --&gt; 188体育优惠记录
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>

			<div id="excel_menu"
				style="position: absolute; top: 25px; left: 0px;">
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
										<td colspan="12" align="left">
											<table border="0" cellpadding="0" cellspacing="0"
												width="100%">
												<tr>
													<td align="right" width="60px">
														188体育会员帐号:
													</td>
													<td width="110px" align="left">
														<s:textfield name="loginname" size="15" />
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
														结束时间:
													</td>
													<td width="110px" align="left">
														<s:textfield name="end" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														优惠代码:
													</td>
													<td width="110px" align="left">
														<s:textfield name="shippingCode" size="15" />
													</td>
													<td align="right" width="60px">
														每页:
													</td>
													<td width="80px" align="left">
														<s:select cssStyle="width:80px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
													<td rowspan="3" width="60px">
														<s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit>
													</td>
												</tr>

											</table>
										</td>
									</tr>
									<tr>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('loginname');">
											账号
										</td>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('shippingcode');">
											优惠码
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('status');">
											状态
										</td>
										<td bgcolor="#0084ff" align="center" width="120px"
											style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
											title="点击排序" onclick="orderby('createtime');">
											北京时间
										</td>
										<td bgcolor="#0084ff" align="center" 
											style="color: #FFFFFF; font-weight: bold">
											备注
										</td>
										<td bgcolor="#0084ff" align="center" width="50px"
											style="color: #FFFFFF; font-weight: bold">
											操作
										</td>
										<td bgcolor="#0084ff" align="center" width="50px"
											style="color: #FFFFFF; font-weight: bold">
											详情
										</td>
									</tr>
									<c:set var="amountSum" value="0" scope="request"></c:set>
									<s:iterator var="fc" value="%{#request.page.pageContents}">
										<tr>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.loginname" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.shippingcode" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:if test="#fc.status==0">
													未通过
												</s:if>
												<s:if test="#fc.status==1">
													通过
												</s:if>
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.createtime" />
											</td>
											<td align="center" bgcolor="#F0FFF0">
												<s:property value="#fc.remark" />
											</td>
											<td align="center" bgcolor="#F0FFF0" width="50px;">
												<s:if test="#fc.status==0">
													<c:if
														test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'boss'}">
														<input type="button" value="审核"
															onclick="submitAction(this,'${fc.id}');" />
													</c:if>
												</s:if>
											</td>
											<td align="center" bgcolor="#F0FFF0" width="50px;">
											  <a
													href="${ctx}/office/getSbListInfo.do?loginname=${fc.loginname}&start=2014-06-01 00:00:00&end=${endTime}"
													target="_blank">投注详情</a>
											</td>
										</tr>
									</s:iterator>
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

