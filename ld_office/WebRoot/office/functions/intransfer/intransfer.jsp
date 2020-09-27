<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>户内转账提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getUserInfo.do' />";
frm.submit();
}
</script>
</head>
<body>
<div id="excel_menu_left">
操作 --> 户内转账提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="addintransfer" onsubmit="submitonce(this);" namespace="/intransfer" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr>
	<td colspan="2"><span style="color: red;">*转出账户</span></td>
</tr>
<tr><td colspan="2">
<table> 
	<s:action name="getSaveBankAccount" namespace="/bankinfo" id="bean" />
<s:iterator value="#request.bankTypeList" id="bank">
	<tr>
		<td>
			<s:if test="type == 0"><img src="/images/pics/tminus.gif" onclick="nodeOpertor('bank${bank.type}',this)"/>内部账户</s:if>
			<s:if test="type == 1"><img src="/images/pics/tminus.gif" onclick="nodeOpertor('bank${bank.type}',this)"/>外部账户</s:if> 
		</td>
	</tr>
	<tr><td id="bank${bank.type}">
	<s:iterator value="item" id="bankType">
			<table style="width:110%;">
				<tr>
					<td><s:if test="type == 0"></s:if>
						<s:if test="type == 1">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>存款账户</s:if>
						<s:if test="type == 2">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>支付账户<br/></s:if>
						<s:if test="type == 3">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>存储账户<br/></s:if>
						<s:if test="type == 4">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[IPS]<br/></s:if>
						<s:if test="type == 40">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付(2030028802)]<br/></s:if>
						<s:if test="type == 41">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付1(2030000006)]<br/></s:if>
						<s:if test="type == 42">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付2(2030020118)]<br/></s:if>
						<s:if test="type == 43">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付3(2030020119)]<br/></s:if>
						<s:if test="type == 411">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[通用智付1(2000295699)]<br/></s:if>
						<s:if test="type == 412">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[通用智付2]<br/></s:if>
						<s:if test="type == 413">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付微信]<br/></s:if>
						<s:if test="type == 414">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付微信1(2000299861)]<br/></s:if>
						<s:if test="type == 450">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[乐富微信]<br/></s:if>
						<s:if test="type == 460">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[新贝微信]<br/></s:if>
						<s:if test="type == 470">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>口袋支付宝<br/></s:if>
						<s:if test="type == 471">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>口袋微信支付<br/></s:if>
						<s:if test="type == 474">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>口袋微信支付2<br/></s:if>
						<s:if test="type == 478">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>口袋微信支付3<br/></s:if>
						<s:if test="type == 494">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>口袋支付宝2<br/></s:if>
						<s:if test="type == 492">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>优付微信<br/></s:if>
						<s:if test="type == 495">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>千网微信<br/></s:if>
						<s:if test="type == 472">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>海尔支付<br/></s:if>
						<s:if test="type == 481">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>汇付宝微信<br/></s:if>
						<s:if test="type == 473">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>聚宝支付宝<br/></s:if>
						<s:if test="type == 485">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>迅联宝<br/></s:if>
						<s:if test="type == 486">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>迅联宝网银<br/></s:if>
						<s:if test="type == 497">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>迅联宝支付宝<br/></s:if>
						<s:if test="type == 488">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>优付支付宝<br/></s:if>
						<s:if test="type == 489">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>新贝支付宝<br/></s:if>
						<s:if test="type == 51">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>汇潮网银<br/></s:if>
						<s:if test="type == 491">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>银宝支付宝<br/></s:if>
						<s:if test="type == 493">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>千网支付宝<br/></s:if>
						<!-- 
						<s:if test="type == 4000">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付点卡]<br/></s:if>
						<s:if test="type == 4001">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付点卡1]<br/></s:if>
						<s:if test="type == 4002">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付点卡2]<br/></s:if>
						<s:if test="type == 4003">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付点卡3]<br/></s:if>
						<s:if test="type == 4010">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付点卡(2000295555)]<br/></s:if>
						 -->
						<s:if test="type == 4011">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[智付点卡1(2000295566)]<br/></s:if>
						
						<s:if test="type == 44">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付]<br/></s:if>
						<s:if test="type == 441">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付1(873098)]<br/></s:if>
						<s:if test="type == 442">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付2(873096)]<br/></s:if>
						<s:if test="type == 443">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付3(873102)]<br/></s:if>
						<s:if test="type == 444">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付4(873103)]<br/></s:if>
						<s:if test="type == 445">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付5(873100)]<br/></s:if>
						<s:if test="type == 446">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付6(873095)]<br/></s:if>
						<s:if test="type == 447">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇付7(873099)]<br/></s:if>
						<s:if test="type == 50">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[汇潮]<br/></s:if>
						<s:if test="type == 60">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[支付宝]<br/></s:if>
						<s:if test="type == 61">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[币付宝]<br/></s:if>
						<s:if test="type == 62">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>在线账户[国付宝1]<br/></s:if>
						<s:if test="type == 5">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>事务账户(人民币)<br/></s:if>
						<s:if test="type == 6">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>事务账户(比索)<br/></s:if>
						<s:if test="type == 7">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>VIP存款账户<br/></s:if>
						<s:if test="type == 8">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>中转账户<br/></s:if>
						<s:if test="type == 9">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktype${bankType.type}${bank.type}',this)"/>额度验证存款账户<br/></s:if>
					</td>
				</tr>
				<tr>
					<td id="banktype${bankType.type}${bank.type}" style="display:none;">
						<s:iterator value="item">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="bankform" value="${id}" onclick="setformBankId(${id});"/>${name}<br/>
						</s:iterator>
					</td>
				</tr>
			</table>   
	</s:iterator>
	</td></tr>
</s:iterator>
	
</table>
</td></tr>

<tr>
	<td colspan="2"><span style="color: red;">*转入账号</span></td>
</tr>
<tr><td colspan="2">
<table> 
<s:action name="getSaveBankAccount" namespace="/bankinfo" id="bean" />
<s:iterator value="#request.bankTypeList" id="bank">
	<tr>
		<td>
			<s:if test="type == 0"><img src="/images/pics/tminus.gif" onclick="nodeOpertor('bankInfo${bank.type}',this)"/>内部账户</s:if>
			<s:if test="type == 1"><img src="/images/pics/tminus.gif" onclick="nodeOpertor('bankInfo${bank.type}',this)"/>外部账户</s:if> 
		</td>
	</tr>
	<tr><td id="bankInfo${bank.type}">
	<s:iterator value="item" id="bankType">
			<table style="width:110%;">
				<tr>
					<td><s:if test="type == 0"></s:if>
						<s:if test="type == 1">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>存款账户</s:if>
						<s:if test="type == 2">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>支付账户<br/></s:if>
						<s:if test="type == 3">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>存储账户<br/></s:if>
						<s:if test="type == 4">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[IPS]<br/></s:if>
						<s:if test="type == 40">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付(2030028802)]<br/></s:if>
						<s:if test="type == 41">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付1(2030000006)]<br/></s:if>
						<s:if test="type == 42">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付2(2030020118)]<br/></s:if>
						<s:if test="type == 43">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付3(2030020119)]<br/></s:if>
						<s:if test="type == 411">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[通用智付1(2000295699)]<br/></s:if>
						<s:if test="type == 412">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[通用智付2]<br/></s:if>
                        <s:if test="type == 413">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付微信]<br/></s:if>						
						<s:if test="type == 414">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付微信1(2000299861)]<br/></s:if>		
					    <s:if test="type == 450">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[乐富微信]<br/></s:if>				
					    <s:if test="type == 460">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[新贝微信]<br/></s:if>
					    <s:if test="type == 470">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>口袋支付宝<br/></s:if>				
					    <s:if test="type == 471">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>口袋微信支付<br/></s:if>
					    <s:if test="type == 474">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>口袋微信支付2<br/></s:if>			
					    <s:if test="type == 478">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>口袋微信支付3<br/></s:if>
					    <s:if test="type == 494">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>口袋支付宝2<br/></s:if>
					    <s:if test="type == 492">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>优付微信<br/></s:if>
					    <s:if test="type == 495">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>千网微信<br/></s:if>
					    <s:if test="type == 472">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>海尔支付<br/></s:if>
					    <s:if test="type == 481">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>汇付宝微信<br/></s:if>
					    <s:if test="type == 473">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>聚宝支付宝<br/></s:if>
					    <s:if test="type == 485">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>迅联宝<br/></s:if>
					    <s:if test="type == 486">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>迅联宝网银<br/></s:if>
					    <s:if test="type == 497">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>迅联宝支付宝<br/></s:if>
					    <s:if test="type == 488">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>优付支付宝<br/></s:if>
					    <s:if test="type == 489">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>新贝支付宝<br/></s:if>
					    <s:if test="type == 491">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>银宝支付宝<br/></s:if>
					    <s:if test="type == 493">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>千网支付宝<br/></s:if>
						<!-- 
						<s:if test="type == 4000">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付点卡]<br/></s:if>
						<s:if test="type == 4001">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付点卡1]<br/></s:if>
						<s:if test="type == 4002">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付点卡2]<br/></s:if>
						<s:if test="type == 4003">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付点卡3]<br/></s:if>
						<s:if test="type == 4010">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付点卡(2000295555)]<br/></s:if>
						-->
						<s:if test="type == 4011">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[智付点卡1(2000295566)]<br/></s:if>
						
						<s:if test="type == 44">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付]<br/></s:if>
						<s:if test="type == 441">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付1(873098)]<br/></s:if>
						<s:if test="type == 442">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付2(873096)]<br/></s:if>
						<s:if test="type == 443">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付3(873102)]<br/></s:if>
						<s:if test="type == 444">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付4(873103)]<br/></s:if>
						<s:if test="type == 445">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付5(873100)]<br/></s:if>
						<s:if test="type == 446">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付6(873095)]<br/></s:if>
						<s:if test="type == 447">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇付7(873099)]<br/></s:if>
						<s:if test="type == 50">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[汇潮)<br/></s:if>
						<s:if test="type == 60">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[支付宝]<br/></s:if>
						<s:if test="type == 61">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[币付宝]<br/></s:if>
						<s:if test="type == 62">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>在线账户[国付宝1]<br/></s:if>
						<s:if test="type == 5">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>事务账户(人民币)<br/></s:if>
						<s:if test="type == 6">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>事务账户(比索)<br/></s:if>
						<s:if test="type == 7">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>VIP存款账户<br/></s:if>
						<s:if test="type == 8">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>中转账户<br/></s:if>
						<s:if test="type == 9">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/pics/tplus.gif" onclick="nodeOpertor('banktypeinfo${bankType.type}${bank.type}',this)"/>额度验证存款账户<br/></s:if>
					</td>
				</tr>
				<tr>
					<td id="banktypeinfo${bankType.type}${bank.type}"  style="display:none;">
						<s:iterator value="item">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="bank" value="${id}" onclick="settoBankId(${id});"/>${name}<br/>
						</s:iterator>
					</td>
				</tr>
			</table>   
	</s:iterator>
	</td></tr>
</s:iterator>
	
</table>
</td></tr>
<tr><td>转账方式:<span style="color:red">*</span></td><td><input type="radio" name="transferflag" value="0" checked/>手工转账&nbsp;&nbsp; <input type="radio" name="transferflag" value="1"/>自动转账</td></tr>
<tr><td>转出金额:<span style="color:red">*</span></td><td><s:textfield name="amount" size="30"/></td></tr>
<tr><td>手续费:<span style="color:red">*</span></td><td><s:textfield name="fee" size="30" value="0"/></td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td align="center"><s:submit value="提交" /></td><td></td></tr>
<tr><td>
	<input type="hidden" name="from" value="" id="mainform_from"/>
	<input type="hidden" name="to" value="" id="mainform_to"/>
</td></tr>
</table>
</s:form>
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
<script>
	function nodeOpertor(id,t) {
		if (document.getElementById(id).style.display == "none") {
			document.getElementById(id).style.display = "block";
			t.src="/images/pics/tminus.gif";
		} else {
			document.getElementById(id).style.display = "none";
			t.src="/images/pics/tplus.gif";
		} 
	}
	
	function setformBankId(v) {
		document.getElementById("mainform_from").value=v;
	}
	
	function settoBankId(v) {
		document.getElementById("mainform_to").value=v;
	}
</script>
</body>
</html>

