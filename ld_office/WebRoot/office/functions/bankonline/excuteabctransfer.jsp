<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>

<script type="text/javascript">
function loadInfo(){
	
	var transfeId = document.mainform.transfeId.value;
	var notes = document.mainform.notes.value;
	
	if(notes==""){
		alert("随机数不能为空");
		return false;
	}
	if(notes.length!=4){
		alert("随机数长度必须是4位数");
		return false;
	}
	if(transfeId==""||transfeId==null){
		alert("系统出错，请联系管理员");
		return false;
	}
	
	
	var action = "/office/excuteabctransfer.do";
		var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"id="+transfeId+"&loginname="+notes+"&r="+Math.random(),
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
	<s:form name="mainform" id="mainform" theme="simple" action="#">
	<input name="transfeId" value=<%=request.getParameter("transfeId")%> type="hidden"/>
		
		<table align="left">
			
<tr><td>用户的4位随机数：</td><td><input name="notes" maxlength="4" size=10/></td>

<td align="center"><input type="button" value="补单" onclick="loadInfo()"/></td></tr>

</table>
	</s:form>
</div>