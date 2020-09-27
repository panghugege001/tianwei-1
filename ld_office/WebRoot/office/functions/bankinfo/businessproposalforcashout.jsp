<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@page import="dfh.model.BusinessProposal"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
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

</script>
<%
	HttpSession chksession=request.getSession(true);
	BusinessProposal proposal =(BusinessProposal)chksession.getValue("businessProposal");
 %>
  	<div id="excel_menu">
  	<c:import url="/office/scriptbusiness.jsp" />
  		<s:form action="excBusinessProposal" namespace="/batchxima" name="mainform" id="mainform" theme="simple" enctype="multipart/form-data">
  		<input name="pno"  type="hidden" value="<%=request.getParameter("pno")%>" />
<table align="left" >
<tr><td>提案号:</td><td><s:property value="#request.proposal.pno"/></td></tr>
				<tr><td>类别:</td><td><s:property value="@dfh.model.enums.BusinessProposalType@getText(#request.proposal.type)"/></td></tr>
				<tr><td>收款人姓名:</td><td><s:property value="#request.proposal.depositname"/></td></tr>
				<tr><td>收款人帐号:</td><td><s:property value="#request.proposal.depositaccount"/></td></tr>
				<tr><td>收款人银行:</td><td><s:property value="#request.proposal.depositbank"/></td></tr>
				<tr><td>预付金额:</td><td><s:property value="#request.proposal.amount"/></td></tr>
				<tr><td>支付帐号:</td><td><s:property value="#request.proposal.bankaccount"/></td></tr>
				
				<tr><td>备注:</td><td><s:property value="#request.proposal.remark"/></td></tr>
				<s:if test="#request.proposal.attachment!=null && #request.proposal.attachment!=''">
					 <tr><td>附件1:</td><td><a   href= "<%=basePath%>UploadFiles/<s:property value="#request.proposal.attachment"/>">右键目标另存为</a></td></tr>
				</s:if>
<tr><td>支付账户:<span style="color:red">*</span></td>
<td><s:action name="getBusinessBankAccount" namespace="/bankinfo" id="bean" />
<s:select  headerValue="请选择支付账户" headerKey="0"
             list="#bean.bankinfos" name="bankinfoid" listKey="id" listValue="username"></s:select>
       </td></tr>
<tr><td>当属月份:</td><td><select name="belong" id="belong"  style="width:50px">  
                <script language="javascript" type="text/javascript">  
                		document.write("<option value=''></option>");  
  			 			for(var i=1;i<=12;i++){  
                			document.write("<option value="+i+">"+i+"</option>");  
          				}
          				var date= new Date();
          				var monthvalue=date.getMonth();
          				document.getElementById("belong").value=<%=proposal.getBelong() %>;
				</script>
		<option value="不属于">不属于</option>  
        </select></tr>
<tr><td>实际金额 :</td><td><input type="text" name="actualmoney"  value=<%=proposal.getAmount() %> /></td></tr>
<tr><td>手续费 :</td><td><s:textfield name="fee" value="0" /></td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td>附件:</td><td><input type="file" id="file6" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交事务" />
</td></tr>
</table>
</s:form>
  	</div>
