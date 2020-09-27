<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<head>
<title>AG试玩帐号</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}

function submitForNewAction(btn,action,agphone,agpassword){
	btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"PhoneNum="+agphone+"&Msg=密码:"+agpassword+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function responseMethod(data){
	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}
</script>
</head>
<body>
<p>
账户 --&gt; AG试玩帐号 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
<s:form action="manageAgTryinfo" namespace="/office" name="mainform" id="mainform" theme="simple">
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="1100px">
				<tr align="left">
					<td>AG试玩电话:<s:textfield name="agPhone" size="30" /></td></td>
					<td>AG账户名称:<s:textfield name="agName" size="30" /></td>
					<td>是否登录:<s:select cssStyle="width:80px" list="#{'2':'','0':'未登录','1':'已登录'}" name="agIsLogin" listKey="key" listValue="value" emptyOption="false"></s:select></td>
					<td>每页:<s:select cssStyle="width:120px" list="%{#application.PageSizes}" name="size"></s:select></td>
					<td ><s:submit  value="查询"></s:submit></td>
				
				</tr>
		</table>
	</td>
</tr>

</table>

<s:hidden name="pageIndex"/>
<s:set name="by" value="'createTime'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
<s:hidden name="jobPno"></s:hidden>
<s:hidden name="remark"></s:hidden>
</s:form>
</div>

<div id="middle" style="position:absolute; top:125px;left:0px">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="960px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">AG账户名称</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">AG账户密码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">AG试玩电话</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">IP</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">注册时间</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">是否登录</td>
              <td  align="center" width="160px" colspan="2"  style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="#e4f2ff">
              <td align="center" width="80px" ><s:property value="#fc.agName"/></td>
              <td  align="center" width="70px" ><s:property value="#fc.agPassword"/> </td>
               <td  align="center" width="80px" ><s:property value="#fc.agPhone"/></td>
               <td  align="center" width="80px" ><a target="_blank" href="http://www.ip138.com/ips.asp?ip=<s:property value="#fc.agIp"/>"><s:property value="#fc.agIp"/></a></td>
              <td  align="center"  width="140px"><s:property value="#fc.agRegDate"/></td>
              <td  align="center"  width="140px">
              <s:if test="#fc.agIsLogin==0">
              	未登录
              </s:if>
              <s:elseif test="#fc.agIsLogin==1">
              	已登录
              </s:elseif>
              </td>
              <td  align="center" width="35px" >
               <s:if test="#fc.agIsLogin==0">
              <c:url var="action" value="/office/sendAgSms.do" scope="request" />
              <input type="button" value="发送短信" onclick="submitForNewAction(this,'${action}','${fc.agPhone}','${fc.agPassword}');"/>
               </s:if>
               <s:elseif test="#fc.agIsLogin==1">
                无操作
               </s:elseif>
              </td>  
            </tr>
            
  	 	 </s:iterator>
            <tr>
              <td colspan="13" align="right" bgcolor="66b5ff" align="center" >
				${page.jsPageCode}
              </td>
            </tr>
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

