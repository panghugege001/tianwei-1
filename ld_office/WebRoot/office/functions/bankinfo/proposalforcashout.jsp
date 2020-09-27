<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
//判断密码是必须是中英文组合
	function isNumAndStr(str){
     var regexpUperStr=/[A-Z]+/;
     var reexpLowerStr=/[a-z]+/;
    // var regexpNum=/\d+/; 
   //  var regexpNum=/^\d*$/; 
     var regexpNum=/^([1-9]\d*|0)(\.\d*[1-9])?$/; 
    var uperStrFlag = regexpUperStr.test(str);
     var lowerStrFlag = reexpLowerStr.test(str);
     var numFlag = regexpNum.test(str);
     if(numFlag)
        return true;
     return false;
   }
   
function loadInfo(){
	var jobPno = document.mainform.jobPno.value;
	var bankinfoid = document.mainform.bankinfoid.value;
	var remark = document.mainform.remark.value;
	var fee = document.mainform.fee.value;
	if(bankinfoid ==0){
		alert("请选择支付账户");
		return ;
	}
	
	if(fee ==null || fee==""){
		alert("请填写手续费，如果没有请填0");
		return ;
	}
	
	if(!isNumAndStr(fee)){
        alert("手续费只能是数字!");
        //document.forms[0].num.select();
        return false;
    }
	
	var action = "/office/excuteProposal.do";
	
	var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&jobPno="+jobPno+"&r="+Math.random()+"&bankaccount="+bankinfoid+"&fee="+fee,
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
/*function compareBankId(id){
	var bankinfoid = document.mainform.bankinfoid.value;
	var msSecondId = document.mainform.msSecondId.value;
	var msSecondMoney = document.mainform.msSecondMoney.value;
	if(bankinfoid==msSecondId){
		if(10000>=msSecondMoney){
			alert("你选择民生秒付1万元以下，请确认!");
		}else{
			alert("民生秒付不允许秒付超过1万以上!");
		}
	}
}*/

</script>

  	<div id="excel_menu">
  		<s:form name="mainform" id="mainform" theme="simple">
  		<input name="jobPno"  type="hidden"  value=<%=request.getParameter("pno")%> />
  		<s:action name="queryProposalDetail" namespace="/office" id="bean" />
  		<input name="msSecondId" value=<%=request.getAttribute("isMsSecondId")%> type="hidden" />
  		<input name="msSecondMoney" value=<s:property value="#request.cashout.money"/> type="hidden" />
<table align="left" >
<tr><td>会员帐号:</td><td><s:property value="#request.cashout.loginname"/></td></tr>
<tr><td>账户姓名:</td><td><s:property value="#request.cashout.accountName"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.cashout.accountNo"/></td></tr>
<tr><td>银行:</td><td><s:property value="#request.cashout.bank"/></td></tr>
<tr><td>取款金额:</td><td><s:property value="#request.cashout.money"/></td></tr>
<tr><td>支付账户:<span style="color:red">*</span></td>
<td><s:action name="getDefrayBankAccount" namespace="/bankinfo" id="bean" />
<s:select  headerValue="请选择支付账户" headerKey="0" 
             list="#bean.bankinfos" name="bankinfoid" listKey="id" listValue="username+remark"></s:select>
       </td></tr>
<tr><td>手续费 :</td><td><s:textfield name="fee" value="0" /></td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td align="center"><input type="button" value="提交" onclick="loadInfo()"/></td><td></td></tr>
</table>
</s:form>
  	</div>
