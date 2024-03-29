<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<head>
<title>提案</title>
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

function submitForCashOut(pno,proposalType){
	//btn.disabled=true;
	//var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/bankinfo/proposalforcashout.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=350, width=400,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function submitForNewAction(btn,action,pno){
	btn.disabled=true;
	var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
	if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&jobPno="+pno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function openproposaldetail(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/proposal_newdetail.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function cashoutaudit(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/cashoutaudit.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
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
账户 --&gt; 赢点排名分析 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
<s:form action="querywinpoint" namespace="/office" name="mainform" id="mainform" theme="simple">
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="860px">
				<tr align="left">
					<td>游戏平台:<s:select cssStyle="width:115px" list="#{'0':'','ea':'ea','ag':'ag','agin':'agin','aginfish':'aginfish','aginslot':'aginslot','bbin':'bbin','keno':'keno','keno2':'keno2','sb':'sb','pt':'pt','newpt':'newpt','sixlottery':'sixlottery','ebet':'ebet','ttg':'ttg','gpi':'gpi','dt':'dt','mg':'mg','png':'png'}" name="platform" listKey="key" listValue="value" emptyOption="false"></s:select></td>
					<td>登录帐号:<s:textfield cssStyle="width:115px" name="loginname" size="7" /></td>
					
					
					<td>开始时间:<s:textfield name="start" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  cssClass="Wdate"/></td>
					<td rowspan="2"><s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit></td>
				</tr>
				<tr align="left">
					<td>代理账号:<s:textfield cssStyle="width:90px" name="agent" size="7" /></td>
					
					<td>每页记录:<s:select cssStyle="width:90px" list="%{#application.PageSizes}" name="size"></s:select></td>
					
					<td>结束时间:<s:textfield name="end" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}"  cssClass="Wdate"/></td>
				</tr>
				<tr align="left">
					<td>会员等级:<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/></td>
					<td>总投注额区间:<s:select name="betflag" list="#{'0':'5万-50万','1':'50万-100万','2':'100万-150万','3':'150万以上'}" listKey="key" listValue="value" emptyOption="true"/></td>
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
<br/><br/><br/><br/>
<div id="middle" style="position:absolute; top:155px;left:0px">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="900px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="130px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">会员帐号</td>
              <td  align="center" width="60px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('level');">会员等级</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('agent');" >上级代理</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amountTotal');" >总输赢值</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('betTotal');" >总投注额</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('winPointTotal');" >赢点率</td>
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <c:set var="amountSumBet" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <s:if test="#fc.overtime>0"><c:set var="duanbgcolor" value="#556b2f"/></s:if>
            <s:else><c:set var="duanbgcolor" value="#e4f2ff"/></s:else>
            <s:if test="#fc[2]>=500000"><c:set var="bgcolor" value="#FF9999"/></s:if>
            <s:elseif test="#fc[2]>=50000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
            <s:elseif test="#fc[2]>=5000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            <tr bgcolor="${bgcolor}">
              <td  align="left"  width="130px"><s:property value="#fc[0]"/></td>
              <td  align="left"  width="60px"><s:property value="@dfh.model.enums.VipLevel@getText(#fc[1])"/></td>
              <td  align="right"  width="90px"><s:property value="#fc[5]"/></td>
              <td  align="right"  width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[2])"/></td>
              <td  align="right"  width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[3])"/></td>
              <td  align="right"  width="90px"><fmt:formatNumber value="${fc[4]*100}"></fmt:formatNumber>%</td>
            </tr>
           	<s:set var="amountValue" value="#fc[2]" scope="request"></s:set>
           	<s:set var="amountBetValue" value="#fc[3]" scope="request"></s:set>
           	<s:set var="amountWinValue" value="#fc[4]" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
            <c:set var="amountSumBet" value="${amountSumBet+amountBetValue}"  scope="request"></c:set>
            <c:set var="amountWinValue" value="${amountWinValue+amountWinValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	
  	 	   	 <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="3">当页小计:</td>
              <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)"/></td>
              <td  align="right" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSumBet)"/></td>
              <td  align="right" ><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum/#request.amountSumBet*100)"/>%</td>
            </tr>
          
  	 	   <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="3">总计:</td>
              <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)"/></td>
              <td  align="right" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)"/></td>
              <td  align="right" ><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.statics1/#request.page.statics2*100)"/>%</td>
            </tr>
          
            <tr>
              <td colspan="14" align="right" bgcolor="66b5ff" align="center" >
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

