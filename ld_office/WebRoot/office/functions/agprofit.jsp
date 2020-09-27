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
<title>游戏平台输赢记录</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
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
function updateEbetAgprofit(pno, td){
	var tr = td.parentElement.parentElement;
	var $tr = $(tr); 
	var ebetBet = $tr.find("input[name=ebetBet]").val();
	var ebetNet = $tr.find("input[name=ebetNetWin]").val();
	if($.trim(ebetBet)=='' || $.trim(ebetNet)=='')return;
	if(confirm("确定要修改EBET的输赢记录吗!")){
		var xmlhttp = new Ajax.Request("/office/updateEbetAgprofitRecord.do",
		        {    
		            method: 'post',
		            parameters:"pno=" + pno + "&ebetBet=" + ebetBet + "&ebetNet=" + ebetNet + "&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}
}
</script>
</head>
<body>
<p>
账户 --&gt; 游戏平台输赢记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
<s:form action="queryagprofit" namespace="/office" name="mainform" id="mainform" theme="simple">
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
<tr align="left">
	<td colspan="99"><font style="text-align:left;" color="red">[当你输入了提案号，时间不再起效。时间全部为北京时间]</font></td>
</tr>
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="1060px">
				<tr align="left">
					<td>游戏平台:<s:select cssStyle="width:115px" list="#{'0':'','ea':'ea','agin':'agin','newpt':'newpt','ebet':'ebet','ttg':'ttg','nt':'nt','qt':'qt','aginfish':'aginfish','ebetapp':'ebetapp','aginslot':'aginslot','dt':'dt','mg':'mg','png':'png','sba':'sba','n2live':'n2live','mwg':'mwg','ptsky':'ptsky','swfish':'swfish','hyg':'hyg','761':'761','bbinele':'bbin电子','bbinvid':'bbin真人','pb':'pb体育','fanya':'泛亚电竞','bit':'bit游戏','kyqp':'开元棋牌','vr':'vr官方彩','vrlive':'vr彩'}" name="platform" listKey="key" listValue="value" emptyOption="false"></s:select></td>
					<td>登录帐号:<s:textfield cssStyle="width:115px" name="loginname" size="7" /></td>

					<td>开始时间:<s:textfield name="start" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  cssClass="Wdate"/></td>
					<td>代理注册开始时间:<s:textfield name="startDate" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startDate}"  cssClass="Wdate"/></td>
					<td rowspan="2"><s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit></td>
				</tr>
				<tr align="left">
					<td>代理账号:<s:textfield cssStyle="width:90px" name="agent" size="7" /></td>
					
					<td>每页记录:<s:select cssStyle="width:90px" list="%{#application.PageSizes}" name="size"></s:select></td>
					
					<td>结束时间:<s:textfield name="end" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}"  cssClass="Wdate"/></td>
					<td>代理注册结束时间:<s:textfield name="endDate" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endDate}"  cssClass="Wdate"/></td>
				</tr>
				<tr align="left">
					<td>推荐码:<s:textfield cssStyle="width:90px" name="intro" size="7" /></td>
					<td>代理推荐码:<s:textfield cssStyle="width:90px" name="partner" size="7" /></td>
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
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('pno');">记录号</td>
              <td  align="center" width="130px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">会员帐号</td>
               <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('bettotal');" >投注额</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amount');" >输赢值</td>
              <td  align="center" width="130px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('createTime');">加入时间</td>
              <td  align="center" width="60px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('quickly');">会员等级</td>
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">操作</td>
             
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <s:if test="#fc.overtime>0"><c:set var="duanbgcolor" value="#556b2f"/></s:if>
            <s:else><c:set var="duanbgcolor" value="#e4f2ff"/></s:else>
            <s:if test="#fc.amount>=500000"><c:set var="bgcolor" value="#FF9999"/></s:if>
            <s:elseif test="#fc.amount>=50000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
            <s:elseif test="#fc.amount>=5000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            <tr bgcolor="${bgcolor}">
              <td align="center" width="80px" ><s:property value="#fc.pno"/></td>  
              <td  align="left"  width="130px"><s:property value="#fc.loginname"/></td>
              <s:if test="#fc.platform=='ebetapp' && (#session.operator.authority=='finance' || #session.operator.authority=='finance_manager')">
              	 <td><input type="text" name="ebetBet" value="<s:property value='@dfh.utils.NumericUtil@formatDouble(#fc.bettotal)'/>" /></td>
              	 <td><input type="text" name="ebetNetWin" value="<s:property value='@dfh.utils.NumericUtil@formatDouble(#fc.amount)'/>" /></td>
              </s:if>
              <s:else>
              	 <td  align="right"  width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.bettotal)"/></td>
              	 <td  align="right"  width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
              </s:else>
              <td  align="center" width="130px" ><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
              <td  align="left"  width="60px"><s:property value="@dfh.model.enums.VipLevel@getText(#fc.quickly)"/></td>
              <td  align="left"  width="140px"><s:property value="#fc.remark"/></td>
              <td  align="center"  width="140px">
              	<s:if test="#fc.platform=='ebetapp' && (#session.operator.authority=='finance' || #session.operator.authority=='finance_manager')" >
              		<input type="button" onclick="updateEbetAgprofit('<s:property value="#fc.pno"/>', this)" value="提交" />
				</s:if>
              </td>
              
              <s:set var="jobPno" value="#fc.pno" scope="request"/>
              
            
            </tr>
            <s:set var="bettotalValue" value="#fc.bettotal" scope="request"></s:set>
            <c:set var="bettotalSum" value="${bettotalSum+bettotalValue}"  scope="request"></c:set>
            
           	<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	 <s:if test="#session.operator.authority=='fnfh' ||  #session.operator.authority=='fnfh_leader' || #session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager' || (#request.loginname != null && #request.loginname != '')">
  	 	   	 <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="2">当页小计:</td>
              <td align="right" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.bettotalSum)"/></td>
              <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)"/></td>
              <td  align="center"   colspan="4"></td>
            </tr>
          
  	 	   <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="2">总计:</td>
              <td align="right" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)"/> </td>
               <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)"/></td>
              <td align="center"   colspan="4"></td>
            </tr>
          </s:if>
            <tr>
              <td colspan="15" align="right" bgcolor="66b5ff" align="center" >
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

