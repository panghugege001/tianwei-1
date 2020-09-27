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
<title>支付开关</title>
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

function submitForNewAction(btn,action,pno,useable){
	btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"jobPno="+pno+"&useable="+useable+"&r="+Math.random(),
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
function searchforeditbankinfo(_aid){
	window.location="/bankinfo/searchforeditbankinfo.do?jobPno="+_aid;

}
</script>
</head>
<body>
<p>
账户 --&gt; 支付开关 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>


<div id="middle" style="position:absolute; top:125px;left:0px">
<s:form action="managemsbankswitch" namespace="/bankinfo" name="mainform" id="mainform" theme="simple">
</s:form>
  <div id="right">
    
	  
	  <div id="right_04">
		  <table width="960px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">内容</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">状态</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <c:set var="amountSumBisuo" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.list}">
            <tr bgcolor="#e4f2ff">
              <td align="center" width="80px" ><s:property value="#fc.id"/></td>
             
              <td  align="center"  width="140px">
              <s:if test="#fc.value==1">
              	开启状态
              </s:if>
              <s:if test="#fc.value==0">
              	关闭状态
              </s:if>
             </td>
              <s:set var="jobPno" value="#fc.id" scope="request"/>
 
              <td  align="center" width="35px" >
               <s:if test="#fc.value==1">
              <c:url var="action" value="/bankinfo/switchmsbank.do" scope="request" />
              <input type="button" value="关闭" onclick="submitForNewAction(this,'${action}','${jobPno}',0);"/>
               </s:if>
                <s:if test="#fc.value==0">
              <c:url var="action" value="/bankinfo/switchmsbank.do" scope="request" />
              <input type="button" value="开启" onclick="submitForNewAction(this,'${action}','${jobPno}',1);"/>
               </s:if>
               
              </td>
             
                      
            </tr>
           
            
            
  	 	 </s:iterator>
            
            
          </table>
	  </div>
	
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

