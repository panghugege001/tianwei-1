<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		<s:action name="queryBusinessProposalDetail" namespace="/office" id="bean" />
		<table align="left">
			
				<tr><td>提案号:</td><td><s:property value="#request.proposal.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.BusinessProposalType@getText(#request.proposal.type)"/></td></tr>
				<tr><td>收款人姓名:</td><td><s:property value="#request.proposal.depositname"/></td></tr>
				<tr><td>收款人帐号:</td><td><s:property value="#request.proposal.depositaccount"/></td></tr>
				<tr><td>收款人银行:</td><td><s:property value="#request.proposal.depositbank"/></td></tr>
				<tr><td>预付金额:</td><td><s:property value="#request.proposal.amount"/></td></tr>
				<tr><td>实际金额:</td><td><s:property value="#request.proposal.actualmoney"/></td></tr>
				<tr><td>支付帐号:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				<tr><td>当属月份:</td><td><s:property value="#request.proposal.belong"/>月份</td></tr>
				<tr><td>备注:</td><td><s:property value="#request.proposal.remark"/></td></tr>
				<s:if test="#request.proposal.attachment!=null && #request.proposal.attachment!=''">
					 <tr><td>附件1:</td><td><a   href= "<%=basePath%>UploadFiles/<s:property value="#request.proposal.attachment"/>">右键目标另存为</a></td></tr>
				</s:if>
				<s:if test="#request.proposal.excattachment!=null && #request.proposal.excattachment!=''">
					 <tr><td>附件2:</td><td><a   href= "<%=basePath%>UploadFiles/<s:property value="#request.proposal.excattachment"/>">右键目标另存为</a></td></tr>
				</s:if>
				
			
			
			




</table>
	</s:form>
</div>
