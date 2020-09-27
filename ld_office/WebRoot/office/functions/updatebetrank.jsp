<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>

<script type="text/javascript">
function loadInfo(){
	
	var id = document.mainform.id.value;
	var note = document.mainform.note.value;
	var amount = document.mainform.amount.value;
	if(amount==""){
		alert("金额不能为空");
		return false;
	}
	
	if(id==""||id==null){
		alert("系统出错，请联系管理员");
		return false;
	}
	
	
	var action = "/office/updateBetInfo.do";
		var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"id="+id+"&note="+note+"&amount="+amount+"&r="+Math.random(),
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
	<input name="id" value=<%=request.getParameter("id")%> type="hidden" />
		<table align="left">
<tr><td>玩家账号：</td><td><input name="loginname"  value=<%=request.getParameter("loginname")%> /></td></tr>			
<tr><td>修改金额：</td><td><input name="amount" value=<%=request.getParameter("amount")%> /></td></tr>
<tr><td>备注：</td><td><input name="note"  value=<%=request.getParameter("note")%> /></td></tr>
<tr><td align="center"><input type="button" value="提交" onclick="loadInfo()"/></td><td></td></tr>

</table>
	</s:form>
</div>