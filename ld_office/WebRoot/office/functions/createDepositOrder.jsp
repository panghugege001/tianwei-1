<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
function create(){
	
	var loginname = document.mainform.loginname.value;
	var bankname = document.mainform.bankname.value;
	var bankcard = document.mainform.bankcard.value;
	var realname = document.mainform.realname.value;
	var amount = document.mainform.amount.value;
	
	if(loginname==""){
		alert("用户名不能为空");
		return false;
	}
	
	if(bankname==""){
		alert("银行类型不能为空");
		return false;
	}
	if(bankcard==""){
		alert("银行卡号不能为空");
		return false;
	}
	if(realname==""){
		alert("银行开户人不能为空");
		return false;
	}
	if(amount==""){
		alert("额度不能为空");
		return false;
	}
	
	if(confirm("确定？")){
	var action = "/office/createDepositOrder.do";
		var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"bankname="+bankname+"&loginname="+loginname+"&bankcard="+bankcard+"&realname="+realname+"&amount="+amount+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}
}

function responseMethod(data){

	alert(data.responseText);
	var _parentWin = window.opener;
	 _parentWin.mainform.submit();
	window.close();
}

function getBankCard(value){
	/* $.post("/office/getBankCard.do",{"type":value} , function(respData){
		console.log(respData);
	}); */
	
	var action = "/office/getBankCard.do";
	var xmlhttp = new Ajax.Request(    
			action,
	        {    
	            method: 'post',
	            parameters:"type="+value+"&r="+Math.random(),
	            onComplete: displayBankCard  
	        }
    	);
}
var jsonData ;
function displayBankCard(data){
	jsonData = data;
	var json = eval(data.responseText);
	$("#bankcard").empty();
	$.each(json,function(index , ele){
        var v=ele.bankcard;
        var x=v.substr(0,4);
        var b=v.slice(-4);
        var k=x+"****"+b;
		$("#bankcard").append("<option value="+ele.bankcard+">"+k+"</option>");
		if(json.length==1){
			$("#realname").val(ele.username);
		}
	});
}

function getUsername(value){
	var json = eval(jsonData.responseText);
	$.each(json,function(index , ele){
		if(ele.bankcard.trim() === value.trim()){
			$("#realname").val(ele.username);
			return ;
		}
	});
}

</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
	<input name="transfeId" value=<%=request.getParameter("transfeId")%> type="hidden"/>
		
	<table align="left">
		<tr>
			<td>游戏账号</td>
			<td><input name="loginname"  /><span style="color: red;">*</span></td>
		</tr>
		<tr></tr>
		<tr>
			<td>银行类型</td>
			<td><select name="bankname">
				<option value="工商银行">工商银行</option>
				<option value="网银">网银</option>
				<option value="农业银行">农业银行</option> -->
				<option value="支付宝">支付宝</option>
				<option value="微信">微信</option>
				<option value="通联转账">通联转账</option>
				<option value="云闪付">云闪付</option>
				</select><span style="color: red;">*</span>
			</td>
		</tr>
		<tr></tr>

<tr><td>银行种类</td><td><s:select list="%{#application.IssuingBankEnum}"
								emptyOption="true" listKey="issuingBank"
								listValue="issuingBankCode" onchange="getBankCard(this.value);"/><span style="color: red;">该下拉只是为了选择卡号</span></td></tr>
<tr></tr>
<tr><td>银行卡号</td><td><select name="bankcard" id="bankcard" onchange="getUsername(this.value);"></select><span style="color: red;">*</span></td></tr>
<tr></tr>
<tr><td>开户名</td><td><input name="realname" id="realname" /><span style="color: red;">*</span></td></tr>
<tr></tr>
<tr><td>存款额度</td><td><input name="amount"  /><span style="color: red;">*</span></td></tr>
<tr></tr>
<tr><td align="center"><input type="button" value="创建" onclick="create()"/></td><td></td></tr>

</table>
	</s:form>
</div>