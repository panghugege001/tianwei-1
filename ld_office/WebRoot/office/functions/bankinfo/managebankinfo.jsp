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

function submitForNewAction(btn,action,pno,useable){
	btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"jobPno="+pno+"&useable="+useable+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function submitForNewShowAction(id){
   var xmlhttp = new Ajax.Request(    
			"/bankinfo/updageNewShow.do",
		    {    
		         method: 'post',
		         parameters:"jobPno="+id+"&r="+Math.random(),
		         onComplete: responseMethod  
		    }
	);
}

function submitForNewShowTransfer(id){
   var xmlhttp = new Ajax.Request(    
			"/bankinfo/updageNewTransfer.do",
		    {    
		         method: 'post',
		         parameters:"jobPno="+id+"&r="+Math.random(),
		         onComplete: responseMethod  
		    }
	);
}

function responseMethod(data){

	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}
function searchforeditbankinfo(_aid){
	window.location="/bankinfo/searchforeditbankinfo.do?jobPno="+_aid;

}

function functionCheckbox(id,userrole){
      var xmlhttp = new Ajax.Request(    
			"/bankinfo/updageuserrole.do",
		    {    
		         method: 'post',
		         parameters:"jobPno="+id+"&userrole="+userrole+"&r="+Math.random(),
		         onComplete: responseMethodTwo  
		    }
	  );
}

function changeSwitch(typeStr,id){
	if(confirm("你确认要执行此操作么？")){
		var xmlhttp = new Ajax.Request(    
				"/bankinfo/changeBankSwitch.do",
			    {
			         method: 'post',
			         parameters:"id="+id+"&switchtype="+typeStr+"&r="+Math.random(),
			         onComplete: responseMethod
			    }
		 );
	}
}

function responseMethodTwo(data){
	
}
</script>
	</head>
	<body>
		<p>
			账户 --&gt; 管理银行账户
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="managebankinfo" namespace="/bankinfo" name="mainform"
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
											list="#{'0':'正常','1':'停用','-1':'作废','2':''}" name="useable"
											listKey="key" listValue="value" emptyOption="false"></s:select>
									</td>
									<td>
										账户性质:
										<s:select cssStyle="width:80px"
											list="#{'0':'','1':'存款账户','2':'支付账户','3':'存储账户','10':'付款储备','4':'在线账户','111':'下发储备','40':'在线账户1[智付(2030028882)]','41':'在线账户2[智付1(2030000006)]','42':'在线账户3[智付2(2030020118)]','43':'在线账户4[智付3(2030020119)]','411':'在线账户[通用智付1(2000295699)]','412':'在线账户[通用智付2]','413':'在线账户[智付微信]','414':'在线账户[智付微信1(2000299861)]','44':'在线账户[汇付]','441':'在线账户[汇付1(873098)]','442':'在线账户[汇付2(873096)]','443':'在线账户[汇付3(873102)]','444':'在线账户[汇付4(873103)]','445':'在线账户[汇付5(873100)]','446':'在线账户[汇付6(873095)]','447':'在线账户[汇付7(873099)]','4000':'在线账户[智付点卡]','4001':'在线账户[智付点卡1]','4002':'在线账户[智付点卡2]','4003':'在线账户[智付点卡3]','4010':'在线账户[智付点卡(2030028884)]','4011':'在线账户[智付点卡1(2000295566)]','50':'在线账户[汇潮]','60':'在线账户[支付宝]','61':'在线账户[币付宝]','62':'在线账户[国付宝1]','5':'事务账户(人民币)','6':'事务账户(比索)','7':'VIP存款账户','8':'中转账户','9':'额度验证存款账户','450':'在线账户[乐富微信]','460':'在线账户[新贝微信]','470':'口袋支付宝','471':'口袋微信支付','474':'口袋微信支付2','478':'口袋微信支付3','472':'海尔支付','481':'汇付宝微信','473':'聚宝支付宝','485':'迅联宝','486':'迅联宝网银','497':'迅联宝支付宝','488':'优付支付宝','489':'新贝支付宝','451':'在线账户[微信支付直连(暂时不用)]','51':'汇潮网银','491':'银宝支付宝','492':'优付微信','493':'千网支付宝','494':'口袋支付宝2','495':'千网微信'}"
											name="type" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>
									<td>
										账户类型:
										<s:select name="bankInfoType" list="%{#application.BankInfoType}" id="type" emptyOption="true" listKey="code" listValue="text" />
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
										扫描账号:
										<s:select name="scanAccount" list="#{'0':'否','1':'是'}"  cssStyle="width:60px" emptyOption="true"/>
									</td>
									<td>
										每页:
										<s:select cssStyle="width:120px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									
									<td>开始时间:<s:textfield name="start" cssStyle="width:140px" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  cssClass="Wdate"/></td>
					
					                <td>结束时间: <s:textfield name="end" cssStyle="width:140px" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}"  cssClass="Wdate"/></td>

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
							<table width="1500px" border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" width="200px" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">账户名称</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">账户性质</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">账户类型</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">银行名称</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">账户余额</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">email账号</td>
									<td align="center" width="200px" style="color: #FFFFFF; font-weight: bold;">银行开户人</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">银行卡号</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">vpn账号</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">vpn密码</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">玩家权限</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">远程ip</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">存款大于</td>
									<td align="center" width="160px" style="color: #FFFFFF; font-weight: bold;">存款小于</td>
									<td align="center" width="200px" style="color: #FFFFFF; font-weight: bold;">备注</td>
									<td align="center" width="160px" colspan="4" style="color: #FFFFFF; font-weight: bold;">操作</td>
								</tr>
								<c:set var="amountSum" value="0" scope="request"></c:set>
								<c:set var="amountSumBisuo" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}" >
									<tr bgcolor="#e4f2ff">
										<td align="center" width="80px"><s:property value="#fc.username" /></td>
										<td align="center" width="70px"><s:property value="@dfh.model.enums.Banktype@getText(#fc.type)" /></td>
										<td align="center" width="80px"><s:property value="@dfh.model.enums.BankInfoType@getText(#fc.banktype)" /></td>
										<td align="center" width="80px"><s:property value="#fc.bankname" /></td>
										<td align="center" width="80px"><s:property value="#fc.amount" /></td>
										<td align="center" width="80px"><s:property value="#fc.accountno" /></td>
										<td align="center" width="80px"><s:property value="#fc.realname" /></td>
										<%--
										<td align="center" width="80px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.password" />
										</td>
										--%>
										<td align="center" width="80px">
											<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #fc.banktype==1 ">
												<s:property value="#fc.bankcard" />
											</s:if>
											<s:else>
												<s:if test="#fc.bankcard.length()>3">
													<s:property value="'************'+#fc.bankcard.substring(#fc.bankcard.length()-3,#fc.bankcard.length())" />
												</s:if>
											</s:else>
										</td>
										<%--
										<td align="center" width="80px">
											<s:property value="#fc.usb" />
										</td>
										--%>
										<td align="center" width="80px"><s:property value="#fc.vpnname" /></td>
										<td align="center" width="80px"><s:property value="#fc.vpnpassword" /></td>
										<td align="center" width="800px">
											<s:if test="#fc.type==1 || #fc.type==9">
												<s:if test="#fc.userrole.contains(\"0\")">天兵、</s:if>
												<s:if test="#fc.userrole.contains(\"1\")">天将、</s:if>
												<s:if test="#fc.userrole.contains(\"2\")">天王、</s:if>
												<s:if test="#fc.userrole.contains(\"3\")">星君、</s:if>
												<s:if test="#fc.userrole.contains(\"4\")">真君、</s:if>
												<s:if test="#fc.userrole.contains(\"5\")">仙君、</s:if>
												<s:if test="#fc.userrole.contains(\"6\")">帝君、</s:if>
												<s:if test="#fc.userrole.contains(\"7\")">天尊、</s:if>
												<s:if test="#fc.userrole.contains(\"8\")">天帝、</s:if>
										  </s:if>
										</td>
										<td align="center" width="140px">
											<s:property value="#fc.remoteip" />
										</td>
										<td align="center" width="140px">
											<s:property value="#fc.depositMin" />
										</td>
										<td align="center" width="140px">
											<s:property value="#fc.depositMax" />
										</td>
										<td align="center" width="140px">
											<s:property value="#fc.remark" />
										</td>
										<s:set var="jobPno" value="#fc.id" scope="request" />


										<td align="center" width="35px">
										<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='card' ">
											<c:url var="action" value="/office/auditProposal.do"
												scope="request" />
                                             <s:if test="#fc.useable!=-1 ">
											      <input type="button" value="编辑" onclick="searchforeditbankinfo('${jobPno}');" />
											</s:if>										
									    </s:if>


										</td>
										<td align="center" width="35px">
											<s:if test="#fc.useable==0">
												<c:url var="action" value="/bankinfo/canclebankinfo.do"
													scope="request" />
												<input type="button" value="禁用"
													onclick="submitForNewAction(this,'${action}','${jobPno}',1);" />
											</s:if>
											<s:if test="#fc.useable==1">
												<c:url var="action" value="/bankinfo/canclebankinfo.do"
													scope="request" />
												<input type="button" value="启用"
													onclick="submitForNewAction(this,'${action}','${jobPno}',0);" />
											</s:if>
											<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='audit'">
												 <s:if test="#fc.useable!=-1 ">
													<c:url var="action" value="/bankinfo/canclebankinfo.do" scope="request" />
													<input type="button" value="作废" onclick="submitForNewAction(this,'${action}','${jobPno}',-1);" />
												  </s:if>
											</s:if>
										</td>
										<td align="center" width="35px">
											<s:if test="#fc.type==1 || #fc.type==9">
												<s:if test="#fc.isshow==1">
													<input type="button" value="隐藏账号"
														onclick="submitForNewShowAction(${fc.id});" />
												</s:if>
												<s:if test="#fc.isshow==0">
													<input type="button" value="显示账号"
														onclick="submitForNewShowAction(${fc.id});" />
												</s:if>
											</s:if>
											<s:if test="#fc.type==1 || #fc.type==9 || #fc.type==8">
												<s:if test="#fc.transferswitch==0">
													<input type="button" value="开启转账" onclick="changeSwitch('transferswitch',${fc.id});" />
												</s:if>
												<s:if test="#fc.transferswitch==1">
													<input type="button" value="关闭转账" onclick="changeSwitch('transferswitch',${fc.id});" />
												</s:if>
												<s:if test="#fc.samebankswitch==0">
													<input type="button" value="开启同行" onclick="changeSwitch('samebankswitch',${fc.id});" />
												</s:if>
												<s:if test="#fc.samebankswitch==1">
													<input type="button" value="关闭同行" onclick="changeSwitch('samebankswitch',${fc.id});" />
												</s:if>
											</s:if>
										</td>
										<%-- <td align="center" width="35px">
											<s:if test="#fc.type==1 && #fc.bankname=='工商银行'">
												<s:if test="#fc.istransfer==1">
													<input type="button" value="关闭转账"
														onclick="submitForNewShowTransfer(${fc.id});" />
												</s:if>
												<s:if test="#fc.istransfer==0">
													<input type="button" value="启动转账"
														onclick="submitForNewShowTransfer(${fc.id});" />
												</s:if>
											</s:if>
										</td> --%>
									</tr>
									<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
									<s:if test="#fc.type!=6">
										<c:set var="amountSum" value="${amountSum+amountValue}"
											scope="request"></c:set>
									</s:if>
									<s:if test="#fc.type==6">
										<c:set var="amountSumBisuo"
											value="${amountSumBisuo+amountValue}" scope="request"></c:set>
									</s:if>
								</s:iterator>
								<tr>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="4">
										当页小计(人民币):
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
										<s:property
											value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)" />
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="12"></td>
								</tr>
								<tr>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="4">
										当页小计(比索):
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
										<s:property
											value="@dfh.utils.NumericUtil@formatDouble(#request.amountSumBisuo)" />
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"
										colspan="12"></td>
								</tr>
								<tr>
									<td colspan="16" align="right" bgcolor="66b5ff" align="center">
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

