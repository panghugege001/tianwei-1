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
	  <title>银行账户显示</title> 
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.2.js"></script>
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
</script>
	</head>
	<body>
		<p>
			账户 --&gt; 管理银行账户
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="showbankinfo" namespace="/bankinfo" name="mainform"
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
										银行名称:
										<s:select name="bankname"
											list="#{'网银':'网银','微信':'微信','支付宝':'支付宝','云闪付':'云闪付','同略云银行':'同略云银行','银行二维码':'银行二维码','微信二维码收款':'微信二维码收款','支付宝二维码收款':'支付宝二维码收款'}" emptyOption="true"
											listKey="key" listValue="value"/>
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
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										账户名称
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										账户类型
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行名称
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										银行卡号
									</td>

									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										vpn账号
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										vpn密码
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										玩家权限
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										存款大于
									</td>
									<td align="center" width="160px"
										style="color: #FFFFFF; font-weight: bold;">
										存款小于
									</td>
									<td align="center" width="200px"
										style="color: #FFFFFF; font-weight: bold;">
										备注
									</td>
								</tr>
								
								<c:set var="amountSum" value="0" scope="request"></c:set>
								<c:set var="amountSumBisuo" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}" >
									<tr bgcolor="#e4f2ff">
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
											<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #fc.banktype==1 ">
												<s:property value="#fc.bankcard" />
											</s:if><s:else>
												<s:if test="#fc.bankcard.length()>4">
													<s:property value="'************'+#fc.bankcard.substring(#fc.bankcard.length()-4,#fc.bankcard.length())" />
												</s:if>
											</s:else>
										</td>
										<%--
										<td align="center" width="80px">
											<s:property value="#fc.usb" />
										</td>
										--%>
										<td align="center" width="80px">
											<s:property value="#fc.vpnname" />
										</td>
										<td align="center" width="80px">
											<s:property value="#fc.vpnpassword" />
										</td>
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
											<s:property value="#fc.depositMin" />
										</td>
										<td align="center" width="140px">
											<s:property value="#fc.depositMax" />
										</td>
										<td align="center" width="140px">
											<s:property value="#fc.remark" />
										</td>
										<s:set var="jobPno" value="#fc.id" scope="request" />

						
									</tr>
								</s:iterator>
								
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

