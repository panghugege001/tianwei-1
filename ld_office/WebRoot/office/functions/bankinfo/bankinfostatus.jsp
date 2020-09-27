<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%@page import="java.util.Date"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>远程银行账号</title>
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

function remotebankinfo(btn,id,ip,remotetype){btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
   var xmlhttp = new Ajax.Request(    
			"/bankinfo/remotebankinfo.do",
		    {    
		         method: 'post',
		         parameters:"jobPno="+id+"&jobip="+ip+"&remotetype="+remotetype+"&r="+Math.random(),
		         onComplete: responseMethod  
		    }
	);
		}else{
		btn.disabled=false;
	}
}
function remotebankinfoStatus(btn,id){
	btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
	var xmlhttp = new Ajax.Request(    
			"/bankinfo/remotebankstatus.do",
		    {    
		         method: 'post',
		         parameters:"jobPno="+id+"&r="+Math.random(),
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

function responseMethodTwo(data){
	
}
</script>
	</head>
	<body>
		<p>
			账户 --&gt; 管理银行状态
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="bankinfostatus" namespace="/bankinfo" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1100px">
								<tr align="left">
									<td>
										账户名称:
										<s:textfield name="username" size="30" />
									</td>
									<td>
										账户状态:
										<s:select cssStyle="width:80px"
											list="#{'0':'正常','1':'停用','2':''}" name="useable"
											listKey="key" listValue="value" emptyOption="false"></s:select>
									</td>
									<td>
										账户性质:
										<s:select cssStyle="width:80px"
											list="#{'0':'','1':'存款账户','2':'支付账户','3':'存储账户','10':'付款储备','4':'在线账户[IPS]','111':'下发储备','40':'在线账户[智付(2030028801)]','41':'在线账户[智付1(2030000006)]','42':'在线账户[智付2(2030020117)]','411':'在线账户[通用智付1(2000299843)]','412':'在线账户[通用智付2(2000299844)]','413':'在线账户[智付微信(2000295588)]','414':'在线账户[智付微信1(2000299861)]','4010':'在线账户[智付点卡(2000295555)]','44':'在线账户[汇付]','50':'在线账户[汇潮]','60':'在线账户[支付宝]','61':'在线账户[币付宝]','62':'在线账户[国付宝1]','5':'事务账户(人民币)','6':'事务账户(比索)','7':'VIP存款账户','8':'中转账户','9':'额度验证存款账户','450':'在线账户[乐富微信]','460':'在线账户[新贝微信]','470':'口袋支付','471':'口袋微信支付','451':'在线账户[微信支付直连(暂时不用)]'}"

											name="type" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>
									<td>
										账户类型:
										<s:select name="bankInfoType"
											list="%{#application.BankInfoType}" id="type"
											emptyOption="true" listKey="code" listValue="text" />
									</td>
									<td>
										银行名称:
										<s:select name="bankname"
											list="%{#application.IssuingBankEnum}" emptyOption="true"
											listKey="issuingBank" listValue="issuingBankCode" />
									</td>
									<td>
										VPN密码:
										<s:textfield name="vpnpassword" size="30" cssStyle="width:120px" />
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
							<table width="1024px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										账户名称
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										账户性质
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行名称
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行本地额度
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行实际额度
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行更新时间
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										远程IP
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										vpn密码
									</td>
									<td align="center" width="160px" colspan="3"
										style="color: #FFFFFF; font-weight: bold;">
										状态
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<%
										request.setAttribute("todayTimeDeposit", (new java.util.Date().getTime()-180000));
										request.setAttribute("todayTimeWithdraw", (new java.util.Date().getTime()-90000));
										dfh.model.Bankinfo cs = (dfh.model.Bankinfo) request.getAttribute("fc");
										if (cs.getUpdatetime() != null && !cs.getUpdatetime().equals("")) {
											long updatetime = cs.getUpdatetime().getTime();
											int type = cs.getType();
											request.setAttribute("updatetime", updatetime);
											request.setAttribute("type", type);
										} else {
											request.setAttribute("updatetime", "");
											request.setAttribute("type", "");
										}
									%>
									<c:if test="${updatetime==''}">
										<c:set var="bgcolor" value="#e4f2ff" />
									</c:if>
									<c:if test="${updatetime!=''&&type==1}">
										<c:choose>
											<c:when test="${todayTimeDeposit>=updatetime}">
												<c:set var="bgcolor" value="#D20000" />
											</c:when>
											<c:otherwise>
												<c:set var="bgcolor" value="#e4f2ff" />
											</c:otherwise>
										</c:choose>
									</c:if>
									<c:if test="${updatetime!=''&&(type==2||type==8)}">
										<c:choose>
											<c:when test="${todayTimeWithdraw>=updatetime}">
												<c:set var="bgcolor" value="#D20000" />
											</c:when>
											<c:otherwise>
												<c:set var="bgcolor" value="#e4f2ff" />
											</c:otherwise>
										</c:choose>
									</c:if>

									<tr bgcolor="${bgcolor}">
										<td align="center" width="80px">
											<s:property value="#fc.username" />
										</td>
										<td align="center" width="70px">
											<s:property
												value="@dfh.model.enums.Banktype@getText(#fc.type)" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.bankname" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.amount" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.bankamount" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.updatetime" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.remoteip" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.vpnpassword" />
										</td>
										<s:set var="jobIp" value="#fc.remoteip" scope="request" />
										<s:set var="jobPno" value="#fc.id" scope="request" />
										<td align="center" width="35px">
											<s:if test="#fc.isactive==1">
												<input type="button" value="ACTIVE"
													onclick="remotebankinfoStatus(this,'${jobPno}')" />
											</s:if>
											<s:if test="#fc.isactive==0">
												<input type="button" value="SLEEP"
													onclick="remotebankinfoStatus(this,'${jobPno}')" />
											</s:if>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="11" align="right" bgcolor="66b5ff" align="center">
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

