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
	window.open('http://'+window.location.host+'/office/functions/bankinfo/proposalforcashout.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=350, width=400,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
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
	window.open('http://'+window.location.host+'/office/functions/proposal_newdetail.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function cashoutaudit(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open('http://'+window.location.host+'/office/functions/cashoutaudit.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
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
记录 --&gt; 网银转账提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">

<s:form action="queryTransferProposal" namespace="/office" name="mainform" id="mainform" theme="simple">
<s:hidden name="proposalType" value="516"></s:hidden>
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
<tr align="left">
	<td colspan="99"><font style="text-align:left;" color="red">[当你输入了提案号，时间不再起效。时间全部为北京时间]</font></td>
</tr>
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="1080px">
				<tr align="left">
					<td>提 案 号(account for out):<s:textfield cssStyle="width:100px" name="pno" size="10" /></td>
					<td>存取款(Transfer type):<s:select cssStyle="width:115px" list="#{'0':'全部','1':'转出','2':'转入'}" name="proposalFlag" listKey="key" listValue="value" emptyOption="false"></s:select></td>
					
					<td>开始时间(start time):<s:textfield name="start" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  cssClass="Wdate"/></td>
					<td>提案状态(status):
							<s:select cssStyle="width:115px" name="proposalStatus"
								list="%{#application.ProposalFlagType}" listKey="code"
								listValue="text" emptyOption="true" />
					</td>
					<td></td>
					<td rowspan="4"><s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit></td>
				</tr>
				<tr align="left" >
					<td>每页记录(size):<s:select cssStyle="width:90px" list="%{#application.PageSizes}" name="size"></s:select></td>			
					<td>银行帐号(loginname):<s:textfield cssStyle="width:115px" name="loginname" size="7" /></td>
					<td>结束时间(end time):<s:textfield name="end" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}"  cssClass="Wdate"/></td>
					<td>转入账户性质:<s:select   list="#{'':'','1':'存款账户','2':'支付账户','3':'存储账户','10':'付款储备','4':'在线账户','111':'下发储备','8':'中转账户','5':'事务账户(人民币)','6':'事务账户(比索)'}" name="transferIn" listKey="key" listValue="value" emptyOption="false"></s:select></td>
				    <td>转出账户性质:<s:select   list="#{'':'','1':'存款账户','2':'支付账户','3':'存储账户','10':'付款储备','4':'在线账户','111':'下发储备','8':'中转账户','5':'事务账户(人民币)','6':'事务账户(比索)'}" name="transferOut" listKey="key" listValue="value" emptyOption="false"></s:select></td>
				    
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
		  <table width="1200px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('pno');">提案号</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('proposer');">提案人</td>
              
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('type');">提案类型</td>
              <td  align="center" width="130px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">转账方向</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序">转出</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序">转入</td>
              <td  align="center" width="90px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amount');" >额度</td>
              <td  align="center" width="130px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('createTime');">加入时间</td>
              <td  align="center" width="60px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('quickly');">会员等级</td>
              <td  align="center" width="60px" style="color: #FFFFFF; font-weight: bold;">是否秒付</td>
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center" width="30px" style="color: #FFFFFF;font-weight: bold;">明细</td>
              <td  align="center" width="70px" colspan="2"  style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <s:if test="#fc.amount>=500000"><c:set var="bgcolor" value="#FF9999"/></s:if>
            <s:elseif test="#fc.amount>=50000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
            <s:elseif test="#fc.amount>=5000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            <tr bgcolor="${bgcolor}">
              <td align="center" width="80px" ><s:url namespace="/office" action="queryTaskDetail" var="queryTaskDetailUrl" escapeAmp="false"><s:param name="pno" value="#fc.pno" /></s:url><s:a target="_blank" href="%{queryTaskDetailUrl}"><s:property value="#fc.pno"/></s:a></td>
              <td  align="left" width="80px" ><s:property value="#fc.proposer"/></td>
              
              <td  align="center" width="70px" >内部转账 </td>
              <td  align="left"  width="130px"><s:property value="#fc.loginname"/></td>
              <td  align="left"  width="70px"><s:property value="#fc.loginname.split('->')[0]"/></td>
              <td  align="left"  width="70px"><s:property value="#fc.loginname.split('->')[1]"/></td>
              <td  align="right"  width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
              <td  align="center" width="130px" ><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
              <td  align="left"  width="60px"><s:if test="#fc.type!=@dfh.model.enums.ProposalType@NEWACCOUNT.code"><s:property value="@dfh.model.enums.VipLevel@getText(#fc.quickly)"/></s:if></td>
              <td  align="center"  width="60px"><s:if test="#fc.msflag==1">是</s:if><s:else>否</s:else></td>
              <td  align="left"  width="140px"><s:property value="#fc.remark"/></td>
              <td  align="center" width="40px" >
              <s:url id="toquery" escapeAmp="false" action="queryProposalDetail" namespace="/office"><s:param name="pno" value="#fc.pno" /><s:param name="proposalType" value="#fc.type" /></s:url>
              <a onclick="openproposaldetail('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor:pointer" title="点击查看提案的明细信息">查询</a></td>
              <s:set var="jobPno" value="#fc.pno" scope="request"/>
              
 
              <s:if test="#fc.flag==@dfh.model.enums.ProposalFlagType@SUBMITED.code">
              <td  align="center" width="35px" >
              
              <s:if test="#fc.type==@dfh.model.enums.ProposalType@CASHOUT.code">
              		<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
              		
              		<a onclick="cashoutaudit('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor:pointer" title="审核">审核</a>
              		
              		</s:if>
              </s:if>
              
              <s:else>
              		<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
              		<c:url var="action" value="/office/auditProposal.do" scope="request" />
              		 <input type="button" value="审核(examine)" onclick="submitForNewAction(this,'${action}','${jobPno}');"/>
              		</s:if>
              </s:else>
              
              
              </td>
              <td  align="center" width="35px" >
              <s:if test="@dfh.security.Authorities@canAccess(request,'cancle')">
              <c:url var="action" value="/office/cancleProposal.do" scope="request" />
              <input type="button" value="取消(cancel)" onclick="submitForNewAction(this,'${action}','${jobPno}');"/>
              </s:if>
              </td>
              </s:if>
              <s:elseif test="#fc.flag==@dfh.model.enums.ProposalFlagType@AUDITED.code">
              <td  align="center" width="35px" >
              <s:if test="#fc.type==@dfh.model.enums.ProposalType@CASHOUT.code">
              		<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
              		
              		<a onclick="submitForCashOut('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor:pointer" title="执行">执行</a>
              		</s:if>
              </s:if>
              <s:else>
              		<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
              		<c:url var="action" value="/office/excuteProposal.do" scope="request" />
              		<input type="button" value="执行(excute)" onclick="submitForNewAction(this,'${action}','${jobPno}');"/>
              		</s:if>
              </s:else>
              
              </td>
              <td  align="center" width="35px" >
              <s:if test="@dfh.security.Authorities@canAccess(request,'cancle')">
              <c:url var="action" value="/office/cancleProposal.do" scope="request" />
              <input type="button" value="取消(cancel)" onclick="submitForNewAction(this,'${action}','${jobPno}');"/>
              </s:if>
              </td>
              </s:elseif>
                <s:else>
              <td  align="center"  >
              </td>
             <td  align="center"  >
              </td>
              </s:else>
              
                <s:else>
              <td  align="center"  >
              </td>
             <td  align="center"  >
              </td>
              </s:else>
              
            
            </tr>
           	<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	   	 <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="8">当页小计:</td>
              <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)"/></td>
              <td  align="center"   colspan="8"></td>
            </tr>
           <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_leader'  || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager'">  
  	 	   <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="8">总计:</td>
               <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)"/></td>
              <td align="center"   colspan="8"></td>
            </tr>
            </s:if>
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

