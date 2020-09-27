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
		<title>代理地址记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
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


function responseMethod(data){
	alert(data.responseText);
}

function saveAgentAddress(){
	if(confirm("确认添加？")){
		var data = $("#_saveAgentAddressForm").serialize();
		console.log(data);
		$.post("/office/addAgentAddress.do?"+data,function(data){
			alert(data);
		});
	}
}

function modifyAddress(btn,oldaddress){
	btn.disabled=true;
	var address=window.prompt("您是否要提交，请填写新代理地址,否则请点取消:","");
	if(address){
		 $.post("/office/modifyAgentSite.do",{"address":address,"oldaddress":oldaddress},function(data){
			 alert(data);
		 });
	}else{
		btn.disabled=false;
	}	
}
function batchUpdate() {
	
	var loginname = $("#loginname").val();
	if (loginname == "" || loginname == null) {
	
		alert("请输入代理账号！");
		return;
	}
	
	var address = window.prompt("请输入新的代理域名前缀", "");
	
	if (address == "" || address == null) {
		
		alert("新的代理域名前缀不能为空！");
		return;
	}
	var myReg = /^[^@\/\'\\\"#$%&.!\^\*]+$/;
	if (!myReg.test(address)) {
		
		alert("新的代理域名前缀不能包含特殊字符！");
		return;
	}
	
	 $.post("/office/batchModifyAgentSite.do",{"loginname":loginname,"address":address},function(data){
		alert(data);
	}); 
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
			<s:form action="getAgentWebsiteRecords" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										代理地址:
										<s:textfield cssStyle="width:115px" name="address" size="30" />
									</td>
									<td>
										开始时间:
										<s:textfield name="startPt" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{startPt}" cssClass="Wdate" />
									</td>
									<td>
										代理帐号:
										<s:textfield cssStyle="width:115px" name="loginname" id="loginname"  size="20" />
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
										是否删除:
										<s:select cssStyle="width:115px" list="#{'':'','1':'已删除','0':'未删除'}" name="deleteflag" listKey="key" listValue="value" emptyOption="false"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="endPt" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endPt}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.username eq 'mkkim'}">
									<td>
										<input type="button" value="批量修改" onclick="batchUpdate()" />
									</td>
									</c:if>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<br />
		<div style="position: absolute; top: 120px; left: 0px">
		 <c:if test="${sessionScope.operator.authority eq  'market_manager' || sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.username eq 'johnjiang'}">
			 <fieldset>
	    		<legend>新增代理地址</legend>
	    		<form id="_saveAgentAddressForm">
				<table border="0" cellpadding="0" cellspacing="0" width="550px;">
					<tr align="left">
						<td>代理地址:</td>
						<td>
							<input type="text" name="address" />
						</td>
						<td>代理帐号:</td>
						<td>
							<input type="text" name="loginname" />
						</td>
						<td>
							<input type="button" value="新增" onclick="saveAgentAddress();"/>
						</td>
					</tr>
				</table>
				</form>
			</fieldset>
		</c:if>
		</div>
		<br />
		<div id="middle" style="position: absolute; top: 200px; left: 0px">
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
										title="点击排序" onclick="orderby('address');" width="130px">
										代理地址
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('loginname');" width="130px">
										代理账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('status');" width="130px">
										状态
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										创建时间
									</td>
									<td>
										修改地址
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									
									
									<tr >
										<td align="center" width="60px">
											<s:property value="#fc.address" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.deleteflag==0">未删除</s:if>
											<s:if test="#fc.deleteflag==1">已删除</s:if>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
										</td>
                                        <td>
											<c:if test="${sessionScope.operator.authority eq  'market_manager' ||  sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'boss' }">
												<input type="button" value="修改" onclick="modifyAddress(this , '<s:property value="#fc.address" />')"/>
											</c:if>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="7" align="right" bgcolor="66b5ff" align="center">
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

