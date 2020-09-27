<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
function loadInfo(){
	
	var remark = document.mainform.remark.value;
	var jobPno = document.mainform.jobPno.value;
	
	var changeAmount = document.mainform.changeAmount.value;
	
	remark = remark+" "+changeAmount;
	
	var action = "/office/auditProposal.do";
		var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&jobPno="+jobPno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
}

function loadSInfo(){
	
	var remark = document.mainform.remark.value;
	var jobPno = document.mainform.jobPno.value;
	var accountNo = document.mainform.accountNo.value;
	var accountName = document.mainform.accountName.value;
	var phone = document.mainform.phones.value;
	
	var changeAmount = document.mainform.changeAmount.value;
	
	remark = remark+" "+changeAmount;
	
	var action = "/office/auditSProposal.do";
		var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&jobPno="+jobPno+"&accountNo="+accountNo+"&accountName="+accountName+"&phone="+phone+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
}

function responseMethod(data){

	alert(data.responseText);
	var _parentWin = window.opener;
	 _parentWin.mainform.submit();
//    window.opener.location.href = window.opener.location.href;
//   	if(window.opener.progressWindow)
//    {
//		window.opener.progressWindow.close();
//	} 
	window.close();
}

</script>
<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
	<s:action name="queryProposalAuditDetail" namespace="/office" id="bean" />
	<input name="jobPno" value=<%=request.getParameter("pno")%> type="hidden"/>
	<input name="accountNo" value="<s:property value="#request.cashout.accountNo"/>" type="hidden"/>  
	<input name="accountName" value="<s:property value="#request.cashout.accountName"/>" type="hidden"/>   
	<input name="phones" value="<s:property value="#request.cashout.phone"/>" type="hidden"/>
		<table align="left">
			<s:if test="#request.proposal.type==@dfh.model.enums.ProposalType@CASHOUT.code">
				<tr><td>提案号:</td><td><s:property value="#request.cashout.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashout.title)"/></td></tr>
				<tr><td>会员帐号:</td><td><s:property value="#request.cashout.loginname"/></td></tr>
				<tr><td>取款金额:</td><td><s:property value="#request.cashout.money"/></td></tr>
				<s:if test="#request.userstatus.touzhuflag == 1">
					<tr style="color:#FF0000"><td>投注要求:</td><td><s:property value="#request.totalamount"/></td></tr>
					<tr style="color:#FF0000"><td>优惠申请时间:</td><td><s:property value="#request.userstatus.starttime"/></td></tr>
					<tr style="color:#FF0000"><td>存款金额:</td><td><s:property value="#request.userstatus.firstCash"/></td></tr>
					<tr style="color:#FF0000"><td>优惠金额:</td><td><s:property value="#request.userstatus.amount"/></td></tr>
				</s:if>
				<s:if test="#request.overshiwan == 1">
					<tr style="color:#FF0000"><td colspan="2">该客户总存减去总提超过10W</td></tr>
				</s:if>
				<tr><td>支付账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				<tr><td>银行:</td><td><s:property value="#request.cashout.bank"/></td></tr>
				<tr><td>银行帐号:</td><td><s:property value="#request.cashout.accountNo"/></td></tr>
				<tr><td>账户姓名:</td><td><s:property value="#request.cashout.accountName"/></td></tr>
				<tr><td>账户类型:</td><td><s:property value="#request.cashout.accountType"/></td></tr>
				<tr><td>开户城市:</td><td><s:property value="#request.cashout.accountCity"/></td></tr>
				<tr><td>银行网点:</td><td><s:property value="#request.cashout.bankAddress"/></td></tr>
				<tr><td>邮件:</td><td><s:property value="#request.cashout.email"/></td></tr>
				<tr><td>IP:</td><td><s:property value="#request.cashout.ip"/></td></tr>
				<tr><td>备注:</td><td><s:property value="#request.cashout.remark"/></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td><font color="red">审核结果:</font></td><td></td></tr>
				<s:if test="#application.AUDIT_DETAIL[#request.cashout.pno].output==null">
				<s:set var="lastTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].lastTotalAmount)"/>
				<s:set var="localCreditChange" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].localCreditChange)"/>
				<s:set var="currentTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].currentTotalAmount)"/>
				<tr><td>上次提款后瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].lastTime"/>]:</td><td><s:property value="%{lastTotalAmount}"/>(local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterKenoRemoteAmount"/>)</td></tr>
				<tr><td>上次到本次提款的本地额度变动值:</td><td><s:property value="%{localCreditChange}"/></td></tr>
				<tr><td>本次提款前瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].currentTime"/>]:</td><td><s:property value="%{currentTotalAmount}"/>(credit=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.amount"/>,local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterKenoRemoteAmount"/>)</td></tr>
				<tr><td>计算输赢值:</td><td><s:property value="%{currentTotalAmount}"/>&nbsp;- &nbsp;<s:property value="%{localCreditChange}"/>&nbsp;-&nbsp;<s:property value="%{lastTotalAmount}"/>=<s:property value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].loseAmount)"/></td></tr>
				</s:if>
				<s:else>
				<tr><td>Output:</td><td><s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].output"/></td></tr>
				</s:else>
				</s:if>	
	
	           <s:if test="#request.proposal.type==@dfh.model.enums.ProposalType@PROXYADVANCE.code">
				<tr><td>提案号:</td><td><s:property value="#request.cashout.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashout.title)"/></td></tr>
				<tr><td>会员帐号:</td><td><s:property value="#request.cashout.loginname"/></td></tr>
				<tr><td>取款金额:</td><td><s:property value="#request.cashout.money"/></td></tr>
				<s:if test="#request.userstatus.touzhuflag == 1">
					<tr style="color:#FF0000"><td>投注要求:</td><td><s:property value="#request.totalamount"/></td></tr>
					<tr style="color:#FF0000"><td>优惠申请时间:</td><td><s:property value="#request.userstatus.starttime"/></td></tr>
					<tr style="color:#FF0000"><td>存款金额:</td><td><s:property value="#request.userstatus.firstCash"/></td></tr>
					<tr style="color:#FF0000"><td>优惠金额:</td><td><s:property value="#request.userstatus.amount"/></td></tr>
				</s:if>
				<s:if test="#request.overshiwan == 1">
					<tr style="color:#FF0000"><td colspan="2">该客户总存减去总提超过10W</td></tr>
				</s:if>
				<tr><td>支付账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				<tr><td>银行:</td><td><s:property value="#request.cashout.bank"/></td></tr>
				<tr><td>银行帐号:</td><td><s:property value="#request.cashout.accountNo"/></td></tr>
				<tr><td>账户姓名:</td><td><s:property value="#request.cashout.accountName"/></td></tr>
				<tr><td>账户类型:</td><td><s:property value="#request.cashout.accountType"/></td></tr>
				<tr><td>开户城市:</td><td><s:property value="#request.cashout.accountCity"/></td></tr>
				<tr><td>银行网点:</td><td><s:property value="#request.cashout.bankAddress"/></td></tr>
				<tr><td>邮件:</td><td><s:property value="#request.cashout.email"/></td></tr>
				<tr><td>IP:</td><td><s:property value="#request.cashout.ip"/></td></tr>
				<tr><td>备注:</td><td><s:property value="#request.cashout.remark"/></td></tr>
				<tr><td colspan="2"></td></tr>
				<tr><td><font color="red">审核结果:</font></td><td></td></tr>
				<s:if test="#application.AUDIT_DETAIL[#request.cashout.pno].output==null">
				<s:set var="lastTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].lastTotalAmount)"/>
				<s:set var="localCreditChange" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].localCreditChange)"/>
				<s:set var="currentTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].currentTotalAmount)"/>
				<tr><td>上次提款后瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].lastTime"/>]:</td><td><s:property value="%{lastTotalAmount}"/>(local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterKenoRemoteAmount"/>)</td></tr>
				<tr><td>上次到本次提款的本地额度变动值:</td><td><s:property value="%{localCreditChange}"/></td></tr>
				<tr><td>本次提款前瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].currentTime"/>]:</td><td><s:property value="%{currentTotalAmount}"/>(credit=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.amount"/>,local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterKenoRemoteAmount"/>)</td></tr>
				<tr><td>计算输赢值:</td><td><s:property value="%{currentTotalAmount}"/>&nbsp;- &nbsp;<s:property value="%{localCreditChange}"/>&nbsp;-&nbsp;<s:property value="%{lastTotalAmount}"/>=<s:property value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].loseAmount)"/></td></tr>
				</s:if>
				<s:else>
				<tr><td>Output:</td><td><s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].output"/></td></tr>
				</s:else>
				</s:if>	
					

				<tr><td>变化额度</td><td><input name="changeAmount" size=10 /></td></tr>
				<tr><td>备注:</td><td><textarea name="remark" cols="30" rows="3"  ></textarea></td></tr>
				
				
				<tr><td align="center"><input type="button" value="审核" onclick="loadInfo()"/></td><td align="left"><input type="button" value="审核且秒提" onclick="loadSInfo()"/></td></tr>
				</table>
					</s:form>
				</div>
