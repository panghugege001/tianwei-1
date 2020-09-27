<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>二维码记录</title>
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

//删除 代理二维码
function deleteQrcode(ids){
	  var choice=confirm("您确认要删除吗？", function() { }, null);
	 if(choice){
		  $.ajax({
			  url:"/office/deleteQrcode.do",
			  type:"post",
			  dataType:"text",
			  data:"id="+ids,
			  async:false,
			  success : function(msg){
				  alert(msg);
				  location.href= '/office/queryQrcode.do';
			  }
		  })
    } 
}




</script>
		<s:form action="queryQrcode" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div>
				代理和推荐码标识 --&gt; 二维码记录
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>
			
			<div id="excel_menu"
				style="position: absolute; top: 25px; left: 0px;">
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'agent'" />
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
													<td align="right" width="60px">
														代理帐号:
													</td>
													<td width="110px" align="left">
														<s:textfield name="agent" size="15" />
													</td>
													<td align="right" width="60px">
														推荐码:
													</td>
													<td width="110px" align="left">
														<s:textfield name="recommendCode" size="15" />
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
									<c:if test="${ sessionScope.operator.authority eq 'sale_manager' ||  sessionScope.operator.authority eq 'boss'  }">
									<tr >
									<td colspan="12" align="left">
										<table border="0" cellpadding="0" cellspacing="0" width="860px" style="margin-left:50px;">
											<tr align="left">
											<td>
											<a href="/office/functions/setQrcode.jsp"><font color="blue">新增</font></a>
											</td>
											</tr>
										</table>
									</td>
									</tr>
									</c:if>
									<tr>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											代理账号
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											推荐码
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											网址
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											微信号
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											备注
										</td>
										<td bgcolor="#0084ff" align="center" width="60px"
											style="color: #FFFFFF; font-weight: bold">
											操作人
										</td>
										
										<td bgcolor="#0084ff" align="center" width="130px"
											style="color: #FFFFFF; font-weight: bold">
											操作
										</td>
									</tr>
									
									<s:iterator var="fc" value="%{#request.page.pageContents}">
											<%
												request.setAttribute("bgcolorValue", "#e4f2ff");
											%>
	

										<tr>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="#fc.agent" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property value="#fc.recommendCode" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property
													value="#fc.address" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<a href="/office/getQrcodeConfig.do?id=<s:property value='#fc.id' />" ><s:property value="#fc.qrcode" /></a>
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property
														value="#fc.remark" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property
													value="#fc.updateoperator" />
											</td>
											
											<td bgcolor="${bgcolorValue }" align="center">
												  <input type="button" value="删除" onclick="deleteQrcode('${fc.id}');"/>
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

