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
<title>存款受理</title>
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
	//var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
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

function submitForCancel(btn,action,pno,useable){
	btn.disabled=true;
	var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
	if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&jobPno="+pno+"&useable="+useable+"&r="+Math.random(),
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
操作 --&gt; 存款受理 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
<s:form action="cashinautoexecute" namespace="/office" name="mainform" id="mainform" theme="simple">
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="400px">
				<tr align="left">
					<td>确认码:<s:textfield name="code" size="30" /></td>
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

<div id="middle" style="position:absolute; top:95px;left:0px">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="1160px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">用户名</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">存款金额</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;">银行名称</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;">真实姓名</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;">存入账号</td>
               <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;">存入时间</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center" width="160px"   style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="#e4f2ff">
              <td align="center" width="80px" ><s:property value="#fc.loginname"/></td>
              <td  align="center" width="70px" ><s:property value="#fc.money"/></td>
               <td  align="center" width="80px" ><s:property value="#fc.bankname"/></td>
               <td  align="center" width="80px" ><s:property value="#fc.aliasName"/></td>
                <td  align="center" width="80px" ><s:property value="#fc.bankaccount"/></td>
                 <td  align="center" width="80px" ><s:property value="#fc.createtime"/></td>
              <td  align="center"  width="140px"><s:property value="#fc.remark"/></td>
              <s:set var="jobPno" value="#fc.pno" scope="request"/>
              <td  align="center" width="35px" >
               <s:if test="#fc.flag==0">
              <c:url var="action" value="/office/cashinexecute.do" scope="request" />
              <input type="button" value="确认" onclick="submitForNewAction(this,'${action}','${jobPno}',1);"/>
               &nbsp;&nbsp;
               <c:url var="action" value="/office/cashincancel.do" scope="request" />
              <input type="button" value="取消" onclick="submitForCancel(this,'${action}','${jobPno}',-1);"/>
              
               </s:if>
                <s:if test="#fc.flag==-1">
               		<s:property value="#fc.operator"/>已取消
               </s:if>
               <s:if test="#fc.flag==1">
           			 <s:property value="#fc.operator"/>已处理
               </s:if>
                
               
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

