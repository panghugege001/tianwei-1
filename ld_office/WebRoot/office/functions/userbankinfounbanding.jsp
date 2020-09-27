<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.service.interfaces.AnnouncementService"%>
<%@page import="dfh.model.Announcement"%>
<%@include file="/office/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>公告管理</title>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<style type="text/css" media="all">
   @import url("/css/maven-base.css"); 
   @import url("/css/maven-theme.css"); 
   @import url("/css/site.css"); 
   @import url("/css/screen.css");
</style>
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
<style type="text/css" >
table {
	border: 1px solid #666;
	margin: 20px 0 20px 0 !important;
	font-size:12px;
	width: 1000px;
}
div.showtable{
	position:absolute;
	top:20px;
	left:50px;
}
div.show{
	position:absolute;
	top:70px;
	left:50px;
}

span.pagebanner {
	background-color: #eee;
	border: 1px dotted #999;
	padding: 2px 4px 2px 4px;
	width: 1000px;
	margin-top: 0px;
	display: block;
	border-bottom: none;
	text-align:right;
	font-size:12px;
}
span.pagelinks {
	background-color: #eee;
	border: 1px dotted #999;
	padding: 2px 4px 2px 4px;
	width: 1000px;
	display: block;
	border-top: none;
	margin-bottom: -5px;
	text-align:right;
	font-size:12px;
}
</style>
</head>
<body style="background:#b6d9e4">

<div class="announcement_titleDiv">
账户 --&gt; 卡折号解绑
</div>
<div class="showtable">
	<s:form action="searchUserbankinfo" namespace="/office" method="post" theme="simple" name="userbankinfoForm" id="userbankinfoForm">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="100px" style="text-align:right" >会员账号：</td><td width="200px" align="left"><s:textfield name="username" size="30" theme="simple"/></td>
				<td width="100px" style="text-align:right">银行名称：</td><td width="100px" align="left"><s:select emptyOption="true" theme="simple" name="bankname" id="bankname" list="%{#application.IssuingBankEnum}" cssClass="transferinput requestselect" listKey="issuingBank" listValue="issuingBankCode" ></s:select></td>
				<td width="100px" style="text-align:right">卡折号状态：</td><td width="100px" align="left"><s:select name="flag" theme="simple" list="#{'0':'正常','1':'停用'}" listKey="key" listValue="value" emptyOption="true"></s:select></td>
				<td style="text-align:left" width="70px"><s:submit theme="simple" value="查  询"/></td><td>&nbsp;</td>
			</tr>
		</table>
	</s:form>
</div>

<div class="show">
	
	<s:if test="pagelist!=null">
	<display:table name="pagelist" style="margin: 0 0 20px 0 !important;" requestURI="/office/searchUserbankinfo.do" id="fc"  decorator="dfh.displaytag.util.UserbankinfoFormat">
	   
	    	<display:column title="序号" style="width:30px;text-align:center">${fc_rowNum}</display:column>
	    	<display:column property="loginname" title="会员账号" style="width:150px;text-align:left"></display:column>
	    	<display:column property="bankname" title="银行名称" style="width:150px;text-align:left" ></display:column>
	    	<display:column property="bankno" title="卡折号" style="text-align:left"></display:column>
	    	<display:column property="addtime" title="绑定时间" style="width:150px;text-align:center"></display:column>
	    	<display:column property="flag" title="状态" style="width:50px;text-align:center"></display:column>
	    	<display:column property="customscript" title="操作" style="width:70px;text-align:center"></display:column>
	    </display:table>
	</s:if>

</div>



<script language="javascript" type="text/javascript">

function unbanding(_bid){
	if(window.confirm("确定吗？")){
		 var url="/office/unbanding.do";
		 var xmlhttp = new Ajax.Request(    
		        url,
		        {    
		            method: 'post',
		            parameters:"id="+_bid+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}
}


function responseMethod(data){
	alert(data.responseText);
	document.userbankinfoForm.submit();
	
}


</script>


<c:import url="/office/script.jsp" />
</body>
</html>

