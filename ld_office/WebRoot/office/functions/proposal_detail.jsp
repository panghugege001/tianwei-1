<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.HashMap"%>
<%@page import="dfh.model.bean.AuditDetail"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>提案明细</title>
</head>
<body>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

var http_request;
function queryCredit(loginname) {
	if (window.XMLHttpRequest) { // if Mozilla, Safari etc
			http_request = new XMLHttpRequest();
			if (http_request.overrideMimeType) {
				http_request.overrideMimeType("text/xml");
			}
		} else {
			if (window.ActiveXObject) { // if IE
				try {
					http_request = new ActiveXObject("Msxml2.XMLHTTP");
				}
				catch (e) {
					try {
						http_request = new ActiveXObject("Microsoft.XMLHTTP");
					}
					catch (e) {
					}
				}
			}
		}
		http_request.onreadystatechange = process;
		http_request.open("POST",'<c:url value='/office/queryTotalCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

function process() {
	if (http_request.readyState == 4) {
		var chkResult = http_request.responseText;
		alert("用户实际的额度是:"+chkResult);
	}
}
</script>
<div id="excel_menu_left">
账户 --> 提案记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left"><s:fielderror/></p>
<table align="left">
<%
	String proposalType = request.getParameter("proposalType"); 
	System.out.println(proposalType);
%>

<s:if test="proposalType==@dfh.model.enums.ProposalType@NEWACCOUNT.code">
<tr><td>提案号:</td><td><s:property value="#request.newAccount.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.newAccount.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.newAccount.loginname"/></td></tr>
<tr><td>真实姓名:</td><td><s:property value="#request.newAccount.aliasname"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.newAccount.remark"/></td></tr>
</s:if>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@CASHIN.code">
<tr><td>提案号:</td><td><s:property value="#request.cashin.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashin.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.cashin.loginname"/></td></tr>
<tr><td>存款姓名:</td><td><s:property value="#request.cashin.aliasName"/></td></tr>
<tr><td>存款金额:</td><td><s:property value="#request.cashin.money"/></td></tr>
<tr><td>存款账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
<tr><td>存款方式:</td><td><s:property value="#request.proposal.saveway"/></td></tr>
<tr><td>收款银行:</td><td><s:property value="#request.cashin.corpBankName"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.cashin.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@CASHOUT.code">
<tr><td>提案号:</td><td><s:property value="#request.cashout.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.cashout.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.cashout.loginname"/></td></tr>
<tr><td>取款金额:</td><td><s:property value="#request.cashout.money"/></td></tr>
<tr><td>支付账户:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
<tr><td>银行:</td><td><s:property value="#request.cashout.bank"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.cashout.accountNo"/></td></tr>
<tr><td>账户姓名:</td><td><s:property value="#request.cashout.accountName"/></td></tr>
<tr><td>账户类型:</td><td><s:property value="#request.cashout.accountType"/></td></tr>
<tr><td>开户城市:</td><td><s:property value="#request.cashout.accountCity"/></td></tr>
<tr><td>银行网点:</td><td><s:property value="#request.cashout.bankAddress"/></td></tr>
<tr><td>邮件:</td><td><s:property value="#request.cashout.email"/></td></tr>
<tr><td>电话:</td><td><s:property value="#request.cashout.phone"/></td></tr>
<tr><td>IP:</td><td><s:property value="#request.cashout.ip"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.cashout.remark"/></td></tr>
<tr><td colspan="2"></td></tr>
<tr><td><font color="red">审核结果:</font></td><td></td></tr>
<s:if test="#application.AUDIT_DETAIL[#request.cashout.pno].output==null">
<s:set var="lastTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].lastTotalAmount)"/>
<s:set var="localCreditChange" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].localCreditChange)"/>
<s:set var="currentTotalAmount" value="@dfh.utils.NumericUtil@formatDouble(#application.AUDIT_DETAIL[#request.cashout.pno].currentTotalAmount)"/>
<tr><td>上次提款后瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].lastTime"/>]:</td><td><s:property value="%{lastTotalAmount}"/></td></tr>
<tr><td>上次到本次提款的本地额度变动值:</td><td><s:property value="%{localCreditChange}"/></td></tr>
<tr><td>本次提款前瞬时总额度[<s:date format="yyyy-MM-dd HH:mm:ss" name="#application.AUDIT_DETAIL[#request.cashout.pno].currentTime"/>]:</td><td><s:property value="%{currentTotalAmount}"/></td></tr>
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
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@CONCESSIONS.code">
<tr><td>提案号:</td><td><s:property value="#request.concessions.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.concessions.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.concessions.loginname"/></td></tr>
<tr><td>存款金额:</td><td><s:property value="#request.concessions.firstCash"/></td></tr>
<tr><td>申请额度:</td><td><s:property value="#request.concessions.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.concessions.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@XIMA.code||proposalType==@dfh.model.enums.ProposalType@SELFXIMA.code">
<tr><td>提案号:</td><td><s:property value="#request.xima.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.xima.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.xima.loginname"/></td></tr>
<tr><td>真实姓名:</td><td><s:property value="#request.ximaUser.accountName"/></td></tr>
<tr><td>电话号码:</td><td><s:property value="#request.ximaUser.phone"/></td></tr>
<tr><td>结算开始时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.xima.startTime"/></td></tr>
<tr><td>结算截至时间:</td><td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.xima.endTime"/></td></tr>
<tr><td>有效投注额度:</td><td><s:property value="#request.xima.firstCash"/></td></tr>
<tr><td>洗码比率:</td><td><s:property value="#request.xima.rate"/></td></tr>
<tr><td>申请额度:</td><td><s:property value="#request.xima.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.xima.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@REBANKINFO.code">
<tr><td>提案号:</td><td><s:property value="#request.rebankinfo.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.rebankinfo.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.rebankinfo.loginname"/></td></tr>
<tr><td>银行:</td><td><s:property value="#request.rebankinfo.bank"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.rebankinfo.accountNo"/></td></tr>
<tr><td>账户姓名:</td><td><s:property value="#request.rebankinfo.accountName"/></td></tr>
<tr><td>账户类型:</td><td><s:property value="#request.rebankinfo.accountType"/></td></tr>
<tr><td>开户城市:</td><td><s:property value="#request.rebankinfo.accountCity"/></td></tr>
<tr><td>银行网点:</td><td><s:property value="#request.rebankinfo.bankAddress"/></td></tr>
<tr><td>IP:</td><td><s:property value="#request.rebankinfo.ip"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.rebankinfo.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@OFFER.code">
<tr><td>提案号:</td><td><s:property value="#request.offer.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.offer.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.offer.loginname"/></td></tr>
<tr><td>再存金额:</td><td><s:property value="#request.offer.firstCash"/></td></tr>
<tr><td>优惠额度:</td><td><s:property value="#request.offer.money"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.offer.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@BANKTRANSFERCONS.code">
<tr><td>提案号:</td><td><s:property value="#request.bankTransferCons.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.bankTransferCons.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.bankTransferCons.loginname"/></td></tr>
<tr><td>转账金额:</td><td><s:property value="#request.bankTransferCons.firstCash"/></td></tr>
<tr><td>优惠额度:</td><td><s:property value="#request.bankTransferCons.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.bankTransferCons.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@PRIZE.code">
<tr><td>提案号:</td><td><s:property value="#request.prize.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.prize.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.prize.loginname"/></td></tr>
<tr><td>抽奖额度:</td><td><s:property value="#request.prize.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.prize.remark"/></td></tr>
</s:elseif>
<s:elseif test="proposalType==@dfh.model.enums.ProposalType@BIRTHDAY.code">
<tr><td>提案号:</td><td><s:property value="#request.prize.pno"/></td></tr>
<tr><td>类别:</td><td><s:property value="@dfh.model.enums.UserRole@getText(#request.prize.title)"/></td></tr>
<tr><td>会员帐号:</td><td><s:property value="#request.prize.loginname"/></td></tr>
<tr><td>生日礼金:</td><td><s:property value="#request.prize.tryCredit"/></td></tr>
<tr><td>备注:</td><td><s:property value="#request.prize.remark"/></td></tr>
</s:elseif>
</table>

</div>

<br/>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

