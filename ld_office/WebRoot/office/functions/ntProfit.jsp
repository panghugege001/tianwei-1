<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>NT平台游戏输赢记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
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

$(function(){
	$("#single_update").click(function(thi){
		var me = $("#single_update");
		var single_date = $("#single_date").val();
		if (single_date==''){
			alert("指定日期不能为空!");
			return;
		}
		me.attr("disabled",true);
		me.val("更新中..");
		$.post("/office/singleUpdateqq.do",{"single_date":single_date},function(data){
			alert(data);
			me.val("更新");
			me.attr("disabled",false);
		});
	});
});
</script>
	</head>
	<body>
		<p>
			记录 --&gt; NT平台游戏记录
			<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getPtList" namespace="/office" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>帐号:<s:textfield cssStyle="width:115px" name="loginname" size="20" /></td>
									<td>开始时间:<s:textfield name="startNT" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startNT}" cssClass="Wdate" /></td>
									<td colspan="2">每页记录:<s:select cssStyle="width:90px" list="%{#application.PageSizes}" name="size"></s:select></td>
									<td rowspan="2"><s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit></td>
								</tr>
								<tr align="left">
									<td>指定日期:<s:textfield id="single_date" name="single_date" size="13" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" value="" cssClass="Wdate" /><input id="single_update" type="button" value="更新"/></td>
									<td>结束时间:<s:textfield name="endNT" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endNT}" cssClass="Wdate" /></td>
									<s:hidden name="pageIndex" />
									<s:set name="by" value="'amount'" />
									<s:set name="order" value="'desc'" />
									<s:hidden name="order" value="%{order}" />
									<s:hidden name="by" value="%{by}" />
									<s:hidden name="jobPno"></s:hidden>
									<s:hidden name="remark"></s:hidden>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</div>
		<br/>
		<br/>
		<br/>
		<br/>
		
		<div id="middle" style="position: absolute; top: 155px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="900px" border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('loginname');" width="130px">账号</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('amount');" width="90px">输赢值</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('betCredit');" width="90px">投注额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('payOut');" width="90px">赔付</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('starttime');" width="90px">开始时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('starttime');" width="90px">结束时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" width="90px">投注详情</td>
								</tr>
								<c:set var="amountSum" value="0" scope="request"></c:set>
								<c:set var="amountSumBet" value="0" scope="request"></c:set>
								<c:set var="amountPayOut" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								
						            <s:if test="#fc.overtime>0">
						            	<c:set var="duanbgcolor" value="#556b2f"/>
						            </s:if>
						            <s:else>
						            	<c:set var="duanbgcolor" value="#e4f2ff"/>
						            </s:else>
						            
						            <s:if test="#fc.amount>=500000">
						            	<c:set var="bgcolor" value="#FF9999"/>
						            </s:if>
						            <s:elseif test="#fc.amount>=50000">
						            	<c:set var="bgcolor" value="#D20000"/>
						            </s:elseif>
						            <s:elseif test="#fc.amount>=5000">
						            	<c:set var="bgcolor" value="#FFABCE"/>
						            </s:elseif>
						            <s:else>
						            	<c:set var="bgcolor" value="#e4f2ff"/>
						            </s:else>
						            
									<tr bgcolor="${bgcolor}">
										<td align="left" width="60px"><s:property value="#fc.loginname" /></td>
										<td align="right" width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" /></td>
										<td align="right" width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.betCredit)" /></td>
										<td align="right" width="90px"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.payOut)" /></td>
										<td align="right" width="90px"><fmt:formatDate value='${fc.starttime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
										<td align="right" width="90px"><fmt:formatDate value='${fc.endtime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
										<td align="center" bgcolor="#F0FFF0" align="center"><a href="${ctx}/office/getPtListInfo.do?username=${fc.loginname}&start=${fc.starttime}&end=${fc.endtime}" target="_blank">投注详情</a></td>
									</tr>
									<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            						<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
            						<s:set var="amountBetValue" value="#fc.betCredit" scope="request"></s:set>
            						<c:set var="amountSumBet" value="${amountSumBet+amountBetValue}"  scope="request"></c:set>
            						<s:set var="amountPayOutValue" value="#fc.payOut" scope="request"></s:set>
            						<c:set var="amountPayOut" value="${amountPayOut+amountPayOutValue}"  scope="request"></c:set>
								</s:iterator>
								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="1">当页小计:</td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSumBet)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountPayOut)" /></td>
									<td align="right"></td>
									<td align="right"></td>
									<td align="right"></td>
								</tr>
								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="1">总计:</td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics3)" /></td>
									<td align="right"></td>
									<td align="right"></td>
									<td align="right"></td>
								</tr>
								<tr>
									<td colspan="14" align="right" bgcolor="66b5ff" align="center">${page.jsPageCode}</td>
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

