<%@page import="dfh.model.Xima"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		<s:action name="queryProposalDetail" namespace="/office" id="bean" />
		<table align="left">
			<%-- <s:if test="#request.proposal.type==@dfh.model.enums.ProposalType@NEWACCOUNT.code">
				<tr><td>提案号:</td><td><s:property value="#request.newAccount.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.newAccount.title)"/></td></tr>
				<tr><td>会员帐号:</td><td><s:property value="#request.newAccount.loginname"/></td></tr>
				<tr><td>真实姓名:</td><td>
				<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
					<s:property value="#request.newAccount.aliasName"/>
				</s:if><s:else>
					<s:property value="#request.newAccount.aliasName.substring(0,1)+'**'"/>
				</s:else>
				<s:property value="#request.newAccount.aliasName"/>
</td></tr>
				<tr><td>备注:</td><td><s:property value="#request.newAccount.remark"/></td></tr>
			</s:if> --%>
			<s:if test="#request.proposal.type==@dfh.model.enums.ProposalType@CASHIN.code ||  #request.proposal.type==@dfh.model.enums.ProposalType@JANPAY.code">
				<tr><td>提案号:</td><td><s:property value="#request.cashin.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashin.title)"/></td></tr>
				<tr><td>会员帐号:</td><td><s:property value="#request.cashin.loginname"/></td></tr>
				<tr><td>存款姓名:</td><td>
				<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
					<s:property value="#request.cashin.aliasName"/>
				</s:if><s:else>
					<s:property value="#request.cashin.aliasName.substring(0,1)+'**'"/>
				</s:else> --%><s:property value="#request.cashin.aliasName"/></td></tr>
				<tr><td>存款金额:</td><td><s:property value="#request.cashin.money"/></td></tr>
				<tr><td>存款手续费:</td><td><s:property value="#request.cashin.fee"/></td></tr>
				<tr><td>存款账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				<tr><td>存款方式:</td><td><s:property value="#request.proposal.saveway"/></td></tr>
				<tr><td>收款银行:</td><td><s:property value="#request.cashin.corpBankName"/></td></tr>
				<s:if test="#request.proposal.whereisfrom=='前台'">
					<tr><td>存款时间:</td><td><s:property value="#request.cashin.cashintime"/></td></tr>
				</s:if>
				<tr><td>备注:</td><td><s:property value="#request.cashin.remark"/></td></tr>
			</s:if>
			<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@CASHOUT.code">
				<tr><td>提案号:</td><td><s:property value="#request.cashout.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashout.title)"/></td></tr>
				<tr><td>会员帐号:</td><td><s:property value="#request.cashout.loginname"/></td></tr>
				<tr><td>取款金额:</td><td><s:property value="#request.cashout.money"/></td></tr>
				<tr><td>支付账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				<tr><td>银行:</td><td><s:property value="#request.cashout.bank"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.cashout.accountNo"/></td></tr>
<tr><td>账户姓名:</td><td>
<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
	<s:property value="#request.cashout.accountName"/>
</s:if><s:else>
	<s:property value="#request.cashout.accountName.substring(0,1)+'**'"/>
</s:else> --%><s:property value="#request.cashout.accountName"/></td></tr>
<tr><td>账户类型:</td><td><s:property value="#request.cashout.accountType"/></td></tr>
<tr><td>开户城市:</td><td><s:property value="#request.cashout.accountCity"/></td></tr>
<tr><td>银行网点:</td><td><s:property value="#request.cashout.bankAddress"/></td></tr>
<c:if test="${sessionScope.operator.authority eq 'boss'}">
<tr><td>邮件:</td><td><s:property value="#request.cashout.email"/></td></tr>
<%-- <tr><td>电话:</td><td><s:property value="#request.cashout.phone"/></td></tr> --%>
</c:if>
<tr><td>IP:</td><td><s:property value="#request.cashout.ip"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.proposal.remark"/></td></tr>
<tr><td colspan="2"></td></tr>
<tr><td><font color="red">审核结果:</font></td><td></td></tr>
<s:if test="#application.AUDIT_DETAIL[#request.cashout.pno].output==null">
<s:set var="lastTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].lastTotalAmount)"/>
<s:set var="localCreditChange" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].localCreditChange)"/>
<s:set var="currentTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].currentTotalAmount)"/>
<tr><td>上次提款后瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].lastTime"/>]:</td><td><s:property value="%{lastTotalAmount}"/>(local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterRemoteAmount"/>,agin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterAgInRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterKenoRemoteAmount"/>,sb=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterSbRemoteAmount"/>,pt=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterSkyRemoteAmount"/>)</td></tr>
<tr><td>上次到本次提款的本地额度变动值:</td><td><s:property value="%{localCreditChange}"/></td></tr>
<tr><td>本次提款前瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].currentTime"/>]:</td><td><s:property value="%{currentTotalAmount}"/>(credit=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.amount"/>,local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterRemoteAmount"/>,agin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterAgInRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterKenoRemoteAmount"/>,sb=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterSbRemoteAmount"/>,pt=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterSkyRemoteAmount"/>)</td></tr>
<tr><td>计算输赢值:</td><td><s:property value="%{currentTotalAmount}"/>&nbsp;- &nbsp;<s:property value="%{localCreditChange}"/>&nbsp;-&nbsp;<s:property value="%{lastTotalAmount}"/>=<s:property value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].loseAmount)"/></td></tr>
</s:if>
<s:else>
<tr><td>Output:</td><td><s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].output"/></td></tr>
</s:else>
<!-- 
<tr><td>是否通知:</td><td>
<s:if test="#request.cashout.notifyNote!=null and #request.cashout.notifyNote!=false">
短信通知
</s:if>
&nbsp;&nbsp;
<s:if test="#request.cashout.notifyPhone!=null and #request.cashout.notifyPhone!=false">
 电话通知
</s:if>
<s:if test="#request.cashout.notifyPhone==null and #request.cashout.notifyNote==null">
 不通知
</s:if>
</td></tr>
 -->
</s:elseif>	

<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@PROXYADVANCE.code">
				<tr><td>提案号:</td><td><s:property value="#request.cashout.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashout.title)"/></td></tr>
				<tr><td>会员帐号:</td><td><s:property value="#request.cashout.loginname"/></td></tr>
				<tr><td>预支金额:</td><td><s:property value="#request.cashout.money"/></td></tr>
				<tr><td>支付账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				<tr><td>银行:</td><td><s:property value="#request.cashout.bank"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.cashout.accountNo"/></td></tr>
<tr><td>账户姓名:</td><td>
<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
	<s:property value="#request.cashout.accountName"/>
</s:if><s:else>
	<s:property value="#request.cashout.accountName.substring(0,1)+'**'"/>
</s:else> --%>
<s:property value="#request.cashout.accountName"/>
</td></tr>
<tr><td>账户类型:</td><td><s:property value="#request.cashout.accountType"/></td></tr>
<tr><td>开户城市:</td><td><s:property value="#request.cashout.accountCity"/></td></tr>
<tr><td>银行网点:</td><td><s:property value="#request.cashout.bankAddress"/></td></tr>
<c:if test="${sessionScope.operator.authority eq 'boss'}">
<tr><td>邮件:</td><td><s:property value="#request.cashout.email"/></td></tr>
<%-- <tr><td>电话:</td><td><s:property value="#request.cashout.phone"/></td></tr> --%>
</c:if>
<tr><td>IP:</td><td><s:property value="#request.cashout.ip"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.proposal.remark"/></td></tr>
<tr><td colspan="2"></td></tr>
<tr><td><font color="red">审核结果:</font></td><td></td></tr>
<s:if test="#application.AUDIT_DETAIL[#request.cashout.pno].output==null">
<s:set var="lastTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].lastTotalAmount)"/>
<s:set var="localCreditChange" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].localCreditChange)"/>
<s:set var="currentTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].currentTotalAmount)"/>
<tr><td>上次提款后瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].lastTime"/>]:</td><td><s:property value="%{lastTotalAmount}"/>(local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterRemoteAmount"/>,agin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterAgInRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterKenoRemoteAmount"/>,sb=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterSbRemoteAmount"/>,pt=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].lastCashout.afterSkyRemoteAmount"/>)</td></tr>
<tr><td>上次到本次提款的本地额度变动值:</td><td><s:property value="%{localCreditChange}"/></td></tr>
<tr><td>本次提款前瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].currentTime"/>]:</td><td><s:property value="%{currentTotalAmount}"/>(credit=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.amount"/>,local=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterLocalAmount"/>,ea=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterRemoteAmount"/>,agin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterAgInRemoteAmount"/>,ag=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterAgRemoteAmount"/>,bbin=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterBbinRemoteAmount"/>,keno=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterKenoRemoteAmount"/>,sb=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterSbRemoteAmount"/>,pt=<s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].currentCashout.afterSkyRemoteAmount"/>)</td></tr>
<tr><td>计算输赢值:</td><td><s:property value="%{currentTotalAmount}"/>&nbsp;- &nbsp;<s:property value="%{localCreditChange}"/>&nbsp;-&nbsp;<s:property value="%{lastTotalAmount}"/>=<s:property value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].loseAmount)"/></td></tr>
</s:if>
<s:else>
<tr><td>Output:</td><td><s:property value="#application.AUDIT_DETAIL[#request.cashout.pno].output"/></td></tr>
</s:else>
<!-- 
<tr><td>是否通知:</td><td>
<s:if test="#request.cashout.notifyNote!=null and #request.cashout.notifyNote!=false">
短信通知
</s:if>
&nbsp;&nbsp;
<s:if test="#request.cashout.notifyPhone!=null and #request.cashout.notifyPhone!=false">
 电话通知
</s:if>
<s:if test="#request.cashout.notifyPhone==null and #request.cashout.notifyNote==null">
 不通知
</s:if>
</td></tr>
 -->
</s:elseif>

<%-- <s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@CONCESSIONS.code">
<tr><td>提案号:</td><td><s:property value="#request.concessions.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.concessions.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.concessions.loginname"/></td></tr>
<tr><td>存款金额:</td><td><s:property value="#request.concessions.firstCash"/></td></tr>
<tr><td>申请额度:</td><td><s:property value="#request.concessions.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.concessions.remark"/></td></tr>
</s:elseif> --%>
<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@QTSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@XIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@AGSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@AGINSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@BBINSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@KGSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@SBSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@PTTIGERSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@PTOTHERSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@TTGSELFXIMA.code||#request.proposal.type==@dfh.model.enums.ProposalType@NTSELFXIMA.code ||#request.proposal.type==@dfh.model.enums.ProposalType@MGSELFXIMA.code ||#request.proposal.type==@dfh.model.enums.ProposalType@DTSELFXIMA.code || #request.proposal.type==@dfh.model.enums.ProposalType@PNGSELFXIMA.code ||#request.proposal.type==@dfh.model.enums.ProposalType@PTSKYSELFXIMA.code">
<tr><td>提案号:</td><td><s:property value="#request.xima.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.xima.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.xima.loginname"/></td></tr>
<tr><td>真实姓名:</td><td>
<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
	<s:property value="#request.ximaUser.accountName"/>
</s:if><s:else>
	<s:property value="#request.ximaUser.accountName.substring(0,1)+'**'"/>
</s:else> --%>
<s:property value="#request.ximaUser.accountName"/>
</td></tr>
<c:if test="${sessionScope.operator.authority eq 'boss'}">
<%-- <tr><td>电话号码:</td><td><s:property value="#request.ximaUser.phone"/></td></tr> --%>
</c:if>
<tr><td>结算开始时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.xima.startTime"/></td></tr>
<tr><td>结算截至时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.xima.endTime"/></td></tr>
<tr><td>有效投注额度:</td><td><s:property value="#request.xima.firstCash"/></td></tr>
<tr><td>洗码比率:</td><td><s:property value="#request.xima.rate"/></td></tr>
<tr><td>申请额度:</td><td><s:property value="#request.xima.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.xima.remark"/></td></tr>
</s:elseif>
<%-- <s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@REBANKINFO.code">
<tr><td>提案号:</td><td><s:property value="#request.rebankinfo.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.rebankinfo.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.rebankinfo.loginname"/></td></tr>
<tr><td>银行:</td><td><s:property value="#request.rebankinfo.bank"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.rebankinfo.accountNo"/></td></tr>
<tr><td>账户姓名:</td><td>
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
	<s:property value="#request.rebankinfo.accountName"/>
</s:if><s:else>
	<s:property value="#request.rebankinfo.accountName.substring(0,1)+'**'"/>
</s:else>
<s:property value="#request.rebankinfo.accountName"/>
</td></tr>
<tr><td>账户类型:</td><td><s:property value="#request.rebankinfo.accountType"/></td></tr>
<tr><td>开户城市:</td><td><s:property value="#request.rebankinfo.accountCity"/></td></tr>
<tr><td>银行网点:</td><td><s:property value="#request.rebankinfo.bankAddress"/></td></tr>
<tr><td>IP:</td><td><s:property value="#request.rebankinfo.ip"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.rebankinfo.remark"/></td></tr>
</s:elseif> --%>
<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@OFFER.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFPT90.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFPT91.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFPT92.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFEBET98.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFEBET99.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFPT705.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFTTG706.code|| 
#request.proposal.type==@dfh.model.enums.ProposalType@SELFNTSPEC.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFNTFIRST.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFNTTWICE.code||
#request.proposal.type==@dfh.model.enums.ProposalType@SELFQTFIRST.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFQTTWICE.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFQTSPEC.code||
#request.proposal.type==@dfh.model.enums.ProposalType@SELFAGINFIRST.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFAGINTWICE.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFAGINSPEC.code||
#request.proposal.type==@dfh.model.enums.ProposalType@SELFDTFIRST.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFDTTWICE.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFDTSPEC.code||
#request.proposal.type==@dfh.model.enums.ProposalType@SELFMGFIRST.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFMGTWICE.code||#request.proposal.type==@dfh.model.enums.ProposalType@SELFMGSPEC.code">
<tr><td>提案号:</td><td><s:property value="#request.offer.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.offer.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.offer.loginname"/></td></tr>
<tr><td>再存金额:</td><td><s:property value="#request.offer.firstCash"/></td></tr>
<tr><td>优惠额度:</td><td><s:property value="#request.offer.money"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.offer.remark"/></td></tr>
</s:elseif>
<%-- <s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@BANKTRANSFERCONS.code">
<tr><td>提案号:</td><td><s:property value="#request.bankTransferCons.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.bankTransferCons.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.bankTransferCons.loginname"/></td></tr>
<tr><td>转账金额:</td><td><s:property value="#request.bankTransferCons.firstCash"/></td></tr>
<tr><td>优惠额度:</td><td><s:property value="#request.bankTransferCons.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.bankTransferCons.remark"/></td></tr>
</s:elseif> --%>
<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@PRIZE.code">
<tr><td>提案号:</td><td><s:property value="#request.prize.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.prize.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.prize.loginname"/></td></tr>
<tr><td>抽奖额度:</td><td><s:property value="#request.prize.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.prize.remark"/></td></tr>
</s:elseif>
<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@BIRTHDAY.code">
<tr><td>提案号:</td><td><s:property value="#request.prize.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.prize.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.prize.loginname"/></td></tr>
<tr><td>生日礼金:</td><td><s:property value="#request.prize.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.prize.remark"/></td></tr>
</s:elseif>

<s:elseif test="#request.proposal.type==516">
<tr><td>提案号:</td><td><s:property value="#request.intransfer.pno"/></td></tr>
<tr><td>转出帐号:</td><td><s:property value="#request.intransfer.wherefrom"/></td></tr>
<tr><td>转入帐号:</td><td><s:property value="#request.intransfer.whereto"/></td></tr>
<tr><td>额度:</td><td><s:property value="#request.intransfer.amount"/></td></tr>
<tr><td>手续费:</td><td><s:property value="#request.intransfer.fee"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.intransfer.remark"/></td></tr>
</s:elseif>
<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@LEVELPRIZE.code">
<tr><td>提案号:</td><td><s:property value="#request.prize.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.prize.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.prize.loginname"/></td></tr>
<tr><td>晋级礼金:</td><td><s:property value="#request.prize.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.prize.remark"/></td></tr>
</s:elseif>

<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@PROFIT.code">
<tr><td>提案号:</td><td><s:property value="#request.losePromo.pno"/></td></tr>
<tr><td>平台:</td><td><s:property value="#request.losePromo.platform"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.losePromo.username"/></td></tr>
<tr><td>负盈利实际额度:</td><td><s:property value="#request.losePromo.amount"/></td></tr>
<tr><td>所得红利:</td><td><s:property value="#request.losePromo.deduct"/></td></tr>
<tr><td>负盈利反赠比率:</td><td><s:property value="#request.losePromo.rate"/></td></tr>
<tr><td>反赠金额:</td><td><s:property value="#request.losePromo.promo"/></td></tr>
<tr><td>派发时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.losePromo.createTime"/></td></tr>
<tr><td>领取时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.losePromo.getTime"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.losePromo.remark"/></td></tr>
</s:elseif>

<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@WEEKSENT.code">
<tr><td>提案号:</td><td><s:property value="#request.weekSent.pno"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.weekSent.username"/></td></tr>
<tr><td>上周日期:</td><td><s:date format="yyyy-MM-dd" name="#request.weekSent.promoDateStart"/>|<s:date format="yyyy-MM-dd" name="#request.weekSent.promoDateEnd"/></td></tr>
<tr><td>上周投注额:</td><td><s:property value="#request.weekSent.amount"/></td></tr>
<tr><td>赠送金额:</td><td><s:property value="#request.weekSent.promo"/></td></tr>
<tr><td>状态:</td><td>
<s:if test="#request.weekSent.status==0">未领取</s:if><s:elseif test="#request.weekSent.status==1">已领取</s:elseif><s:elseif test="#request.weekSent.status==2">已处理</s:elseif><s:elseif test="#request.weekSent.status==3">已取消</s:elseif>
</td></tr>
<tr><td>平台:</td><td><s:property value="#request.weekSent.platform"/></td></tr>
<tr><td>派发时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.weekSent.createTime"/></td></tr>
<tr><td>领取时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.weekSent.getTime"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.weekSent.remark"/></td></tr>
</s:elseif>

<s:elseif test="#request.proposal.type==@dfh.model.enums.ProposalType@PTBIGBANG.code">
<tr><td>会员帐号:</td><td><s:property value="#request.ptBigBang.username"/></td></tr>
<tr><td>输赢额度:</td><td><s:property value="#request.ptBigBang.netWinOrLose"/></td></tr>
<tr><td>活动时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.ptBigBang.startTime"/> ~ <s:date format="yyyy-MM-dd HH:mm:ss" name="#request.ptBigBang.endTime"/></td></tr>
<tr><td>扣除红利:</td><td><s:property value="#request.ptBigBang.bonus"/></td></tr>
<tr><td>活动礼金:</td><td><s:property value="#request.ptBigBang.giftMoney"/></td></tr>
<tr><td>流水倍数:</td><td><s:property value="#request.ptBigBang.times"/></td></tr>
<tr><td>派发时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.ptBigBang.distributeTime"/></td></tr>
<tr><td>领取时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.ptBigBang.getTime"/></td></tr>
</s:elseif>

</table>
	</s:form>
</div>
