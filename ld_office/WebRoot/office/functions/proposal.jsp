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
		<title>提案</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript">

var $j = jQuery.noConflict();
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function switchbutton(){
	var kaiguan=document.getElementById("kaiguan").value;
	if(kaiguan==1){
		
		document.getElementById("kaiguan").value=0;
		alert("声音及提示已经开启");
	}else{
		
		document.getElementById("kaiguan").value=1;
		var node=document.getElementById("tixing");
		alert("声音及提示已经关闭");  
  			if(node!=null)  
  			{  
     				node.stop();  
  			}
  		
	}
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

//优惠劵审核
function submitYhjAction(btn,pno){
   btn.disabled=true;
   var action = "/office/getSubmitYhjAction.do";
   var loginname=window.prompt("请填写会员账号","");
   if(loginname || loginname==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"loginname="+loginname+"&jobPno="+pno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}
function submitRpayAction(btn,action,pno){
   btn.disabled=true;
   var loginname=window.prompt("您是否要重新付款，并填写备注(可以默认为空),否则请点取消:","");
   if(loginname || loginname==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"loginname="+loginname+"&jobPno="+pno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}else{
		btn.disabled=false;
	}	
}
function submitYhjCancelAction(btn,pno){
   btn.disabled=true;
   var action = "/office/submitYhjCancelAction.do";
   var loginname=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
   if(loginname || loginname==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"loginname="+loginname+"&jobPno="+pno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}else{
		btn.disabled=false;
	}	
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

function getproposalforaudit(){
	var action = "/office/getproposalforaudit.do";
	var start  =document.getElementById("start").value;
	var end  =document.getElementById("end").value;
	var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"start="+start+"&end="+end+"&r="+Math.random(),
		            onComplete: responseGetproposalforaudit  
		        }
	    	);
}

function openproposaldetail(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/proposal_newdetail.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=850,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function cashoutaudit(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/cashoutaudit.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=850,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function cashoutauditforms(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/cashoutauditforms.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function responseMethod(data){

	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}

function responseGetproposalforaudit(data){

	//alert(data.responseText);
	if(data.responseText==1){
	   // alert("显示");
		
		if(document.getElementById("kaiguan").value==0){
			$j.messager.lays(200, 100);
			$j.messager.show(0, '有新的存提款提案待审核,请抓紧处理',55000);
			var node=document.getElementById("tixing");  
  			if(node!=null)  
  			{  
     				node.Play();  
  			}
			//document.getElementById("tixing").play();
		}
		
	}
	//var frm=document.getElementById("mainform");
	//frm.submit();
}

//$j(document).ready(function(){
	//alert($j("#start").val());
	//$j.messager.show(0,'送你一个Jquery Messager消息弹出插件！');
  //   getproposalforaudit()
    // window.setInterval("getproposalforaudit()",60000);//10秒刷新1次
      //});
     function confirmOfferFinished(btn,pno){
   btn.disabled=true;
   var action = "/office/confirmOfferFinished.do";
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"jobPno="+pno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
}  
</script>
	</head>
	<body>
		<p>
			账户 --&gt; 提案
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryProposal" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<s:hidden id="kaiguan" name="kaiguan" value="0"></s:hidden>
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7"  width="1190px">
					<tr align="left">
						<td colspan="99">
							<font style="text-align: left;" color="red">[当你输入了提案号，时间不再起效。时间全部为北京时间]</font>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1200px">
								<tr align="left">
									<td>
										提案类型:
										<s:select cssStyle="width:90px" name="proposalType"
											list="%{#application.ProposalType}" listKey="code"
											listValue="text" emptyOption="true" />
									</td>
									<td>
										提案状态:
										<s:select cssStyle="width:115px" name="proposalFlag"
											list="%{#application.ProposalFlagType}" listKey="code"
											listValue="text" emptyOption="true" />
									</td>
									<td>
										会员类型:
										<s:select cssStyle="width:100px" name="proposalRole"
											list="%{#application.UserRole}" listKey="code"
											listValue="text" emptyOption="true"></s:select>
									</td>
									<td>
										登录帐号:
										<s:textfield cssStyle="width:115px" name="loginname" size="7" />
									</td>


									<td>
										开始时间:
										<s:textfield name="start" id="start" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{startTime}" cssClass="Wdate" />
									</td>
									<td rowspan="3">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>
								</tr>
								<tr align="left">
									<td>
										代理账号:
										<s:textfield cssStyle="width:90px" name="agent" size="7" />
									</td>
									<td>
										代理网址:
										<s:textfield cssStyle="width:115px" name="referWebsite"
											size="10" />
									</td>
									<td>
										提 案 号:
										<s:textfield cssStyle="width:100px" name="pno" size="10" />
									</td>
									<td>
										存款方式:
										<s:select cssStyle="width:115px"
											list="#{'':'','网银':'网银','ATM':'ATM','现存':'现存','网转':'网转','汇款':'汇款'}"
											name="saveway" listKey="key" listValue="value"
											emptyOption="false"></s:select>
									</td>

									<td>
										结束时间:
										<s:textfield name="end" id="end" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{endTime}" cssClass="Wdate" />
									</td>
								</tr>
								<tr align="left">
									<td>
										每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										银行账户:
										<s:action name="getAllBankAccount" namespace="/bankinfo"
											id="bean" />
										<s:select cssStyle="width:115px" headerValue="" headerKey=""
											list="#bean.bankinfos" name="bankaccount" listKey="username"
											listValue="username"></s:select>
									</td>
									<td>
										客户银行:
										<s:select name="bankname" headerValue="" headerKey=""
											list="%{#application.IssuingBankEnum}" listKey="issuingBank"
											listValue="issuingBankCode" />
									</td>
									<td>
										是否超时:
										<s:select cssStyle="width:115px"
											list="#{'':'','0':'正常','1':'超时'}" name="overtime"
											listKey="key" listValue="value" emptyOption="false"></s:select>
									</td>
									<td>
<%--
										<c:if test="${sessionScope.operator.authority eq 'boss'}">
--%>
											<label><s:checkbox name="isExecute" title="是否查询执行时间">是否查询执行时间</s:checkbox></label>
<%--
										</c:if>
--%>
										<label>未完成再存优惠<s:checkbox name="unfinshedYouHui" title="未完成再存优惠"></s:checkbox></label>
									</td>
								</tr>
								<tr align="left">
									<td>
										优惠代码:
										<s:textfield cssStyle="width:100px" name="shippingCode"
											size="10" />
									</td>
									<td>
										优惠标题:
										<s:textfield cssStyle="width:100px" name="shippinginfo"
											size="10" />
									</td>
									<td>
										提 案 人:
										<s:textfield cssStyle="width:115px" name="proposer" size="20" />
									</td>
									<td>
										备注:
										<s:textfield cssStyle="width:115px" name="remarkRemark" size="40" />
									</td>
									<td>
										红包金额:
										<s:textfield cssStyle="width:115px" name="gifTamount" size="40" />
									</td>
					 <td>
						会员等级：
						<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/>
					  </td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="left">
						<td colspan="4">
							存送标题:
							<s:textfield cssStyle="width:115px" name="title" size="40" />
						</td>
						
					</tr>
				</table>

				<s:hidden name="pageIndex" />
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="jobPno"></s:hidden>
				<s:hidden name="remark"></s:hidden>
			</s:form>
		</div>
		<br />
		<br />
		<br />
		<br />
		<div id="middle" style="position: absolute; top: 185px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1200px" border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" width="80px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('pno');">
										提案号
									</td>
									<td align="center" width="80px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('proposer');">
										提案人
									</td>
									<td align="center" width="70px"
										style="color: #FFFFFF; font-weight: bold;">
										用户类型
									</td>
									<td align="center" width="70px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('type');">
										提案类型
									</td>
									<td align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('flag');">
										提案状态
									</td>
									<td align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('loginname');">
										会员帐号
									</td>
									<td align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('remark');">
										会员备注
									</td>
									<td align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;word-break: break-all;white-space: normal;"
										title="点击排序" onclick="orderby('amount');">
										额度
									</td>
									<td align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createTime');">
										加入时间
									</td>
									<td align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('executeTime');">
										执行时间
									</td>
									<td align="center" width="80px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										执行用时
									</td>
									<td align="center" width="80px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										是否秒付
									</td>
									<td align="center" width="80px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										是否成功
									</td>
									<td align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('quickly');">
										会员等级
									</td>
									<td align="center" width="80px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										审核且秒付
									</td>
									<td align="center" width="140px"
										style="color: #FFFFFF; font-weight: bold;">
										备注
									</td>
									<td align="center" width="30px"
										style="color: #FFFFFF; font-weight: bold;">
										明细
									</td>
									<td align="center" width="70px" colspan="2"
										style="color: #FFFFFF; font-weight: bold;">
										操作
									</td>
								</tr>
								<c:set var="amountSum" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<s:if test="#fc.amount>=500000">
										<c:set var="bgcolor" value="#FF9999" />
									</s:if>
									<s:elseif test="#fc.amount>=50000">
										<c:set var="bgcolor" value="#D20000" />
									</s:elseif>
									<s:elseif test="#fc.amount>=5000">
										<c:set var="bgcolor" value="#FFABCE" />
									</s:elseif>
									<s:else>
										<c:set var="bgcolor" value="#e4f2ff" />
									</s:else>

									<s:if test="#fc.overtime>0">
										<c:set var="duanbgcolor" value="#00ff00" />
									</s:if>
									<s:else>
										<c:set var="duanbgcolor" value="${bgcolor}" />
									</s:else>

									<s:if test="#fc.passflag==-1">
										<c:set var="auditbgcolor" value="#00ff00" />
									</s:if>
									<s:else>
										<c:set var="auditbgcolor" value="${bgcolor}" />
									</s:else>

									<tr bgcolor="${bgcolor}">
										<s:if test="#fc.type==430||#fc.type==431||#fc.type==432||#fc.type==433||#fc.type==434||#fc.type==435||#fc.type==436||#fc.type==437||#fc.type==531||#fc.type==532||#fc.type==533||#fc.type==534||#fc.type==535||#fc.type==536||#fc.type==537||#fc.type==571||#fc.type==572||#fc.type==573||#fc.type==575||#fc.type==581||#fc.type==582||#fc.type==583||#fc.type==584||#fc.type==401||#fc.type==402||#fc.type==403||#fc.type==404||#fc.type==405||#fc.type==406||#fc.type==407||#fc.type==408||#fc.type==409||#fc.type==410||#fc.type==411||#fc.type==412||#fc.type==413||#fc.type==414||#fc.type==419||#fc.type==425||#fc.type==422||#fc.type==423||#fc.type==424||#fc.type==426||#fc.type==427||#fc.type==428||#fc.type==429">
											<td align="center" width="80px">
												<s:url namespace="/office" action="queryTaskDetail" var="queryTaskDetailUrl" escapeAmp="false">
													<s:param name="pno" value="#fc.pno" />
												</s:url>
												<s:a target="_blank" href="%{queryTaskDetailUrl}">
													<s:property value="#fc.pno" />
												</s:a>
											</td>
											<td align="left" width="80px">
												<s:property value="#fc.proposer" />
											</td>
											<td align="center" width="70px">
												<s:if test="#fc.flag==2">
													<s:if test="#fc.agentType=='AGENT'">代理</s:if>
													<s:else>真钱会员</s:else>
												</s:if>
												<s:if test="#fc.flag==1">
													<s:if test="#fc.loginname!=''">
														<s:if test="#fc.agentType=='AGENT'">代理</s:if>
														<s:else>真钱会员</s:else>
													</s:if>
												</s:if>
												<s:if test="#fc.flag==0">
													<s:if test="#fc.loginname!=''">
														<s:if test="#fc.agentType=='AGENT'">代理</s:if>
													</s:if>
												</s:if>
											</td>
											<td align="center" width="70px"
												style="background-color: #FFABCE;">
												<s:property
													value="@dfh.model.enums.ProposalType@getText(#fc.type)" />
											</td>
											<td align="center" width="60px">
												<s:property
													value="@dfh.model.enums.ProposalFlagType@getText(#fc.flag)" />
											</td>
											<td align="left" width="130px">
												<s:property value="#fc.loginname" />
											</td>
											<td align="left" width="130px" style="word-break: break-all;white-space: normal;">
												<s:property value="#fc.userRemark" />
											</td>
											<td align="right" width="90px">
											   <s:if test="#fc.type==419">
												 	<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.gifTamount)" />
											    </s:if>
												<s:else>
												     <s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" />
											    </s:else>
											</td>
											<td align="center" width="130px">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
											</td>
											<td align="center" width="130px">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.executeTime" />
											</td>
											<td align="left" width="80px" bgcolor="${duanbgcolor}"></td>
											<td align="center" width="80px">
												否
											</td>
											<td align="center" width="80px">
												<s:if test="#fc.flag==2">
												   成功
											    </s:if>
											</td>
											<td align="left" width="60px">
														<s:property value="@dfh.model.enums.VipLevel@getText(#fc.quickly)" />
											</td>
											<td align="center" width="80px">
												<s:if test="#fc.mssgflag==1">是</s:if>
												<s:else>否</s:else>
											</td>
											<td align="left" width="150px" style="background-color: #FFABCE;">
												<s:if test="#fc.type==430||#fc.type==431||#fc.type==432||#fc.type==433||#fc.type==434||#fc.type==435||#fc.type==436||#fc.type==437||#fc.type==531||#fc.type==532||#fc.type==533||#fc.type==534||#fc.type==535||#fc.type==572||#fc.type==573||#fc.type==582||#fc.type==583||#fc.type==584||#fc.type==401||#fc.type==402||#fc.type==403||#fc.type==404||#fc.type==405||#fc.type==406||#fc.type==407||#fc.type==408||#fc.type==409||#fc.type==410||#fc.type==411||#fc.type==412||#fc.type==413||#fc.type==414||#fc.type==419||#fc.type==425||#fc.type==422||#fc.type==423||#fc.type==424||#fc.type==426||#fc.type==427||#fc.type==428||#fc.type==429">
													存:<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" />
													送:<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.gifTamount)" />
												</s:if>
												<s:if test="#fc.type==536||#fc.type==537||#fc.type==571||#fc.type==575||#fc.type==581">
													送:<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.gifTamount)" />
												</s:if>
												<s:if test="#fc.type==430||#fc.type==431||#fc.type==432||#fc.type==433||#fc.type==434||#fc.type==435||#fc.type==436||#fc.type==437||#fc.type==531||#fc.type==532||#fc.type==533||#fc.type==534||#fc.type==535||#fc.type==537||#fc.type==571||#fc.type==575||#fc.type==572||#fc.type==573||#fc.type==581||#fc.type==582||#fc.type==583||#fc.type==584||#fc.type==401||#fc.type==402||#fc.type==403||#fc.type==404||#fc.type==405||#fc.type==406||#fc.type==407||#fc.type==408||#fc.type==409||#fc.type==410||#fc.type==411||#fc.type==412||#fc.type==413||#fc.type==414||#fc.type==419||#fc.type==425||#fc.type==422||#fc.type==423||#fc.type==424||#fc.type==426||#fc.type==427||#fc.type==428||#fc.type==429">
												     倍:${fc.betMultiples}
												</s:if>
												<s:if test="#fc.flag==2">
												     券:${fc.shippingCode}
												</s:if>
												<s:else>
													<s:if test="#fc.type==430||#fc.type==431||#fc.type==432||#fc.type==433||#fc.type==434||#fc.type==435||#fc.type==436||#fc.type==437||#fc.type==531||#fc.type==532||#fc.type==533||#fc.type==534||#fc.type==535||#fc.type==536||#fc.type==537||#fc.type==571||#fc.type==572||#fc.type==573||#fc.type==581||#fc.type==582||#fc.type==583||#fc.type==584||#fc.type==401||#fc.type==402||#fc.type==403||#fc.type==404||#fc.type==405||#fc.type==406||#fc.type==407||#fc.type==408||#fc.type==409||#fc.type==410||#fc.type==411||#fc.type==412||#fc.type==413||#fc.type==414||#fc.type==419||#fc.type==425||#fc.type==422||#fc.type==423||#fc.type==424||#fc.type==426||#fc.type==427||#fc.type==428||#fc.type==429">
														<c:if test="${sessionScope.operator.authority ne 'finance' and sessionScope.operator.authority ne 'finance_leader' and sessionScope.operator.authority ne 'finance_manager'}">
												                             券:${fc.shippingCode}
												        </c:if>
													</s:if>
												</s:else>
											</td>
											<td align="center" width="40px">
												<s:if test="#fc.flag==2">
													<a target="_blank" href="/office/getProposalPno.do?pno=<s:property value="#fc.pno"/>" style="cursor: pointer" title="点击查看提案的明细信息">查询</a>
												</s:if>
											</td>
											<td align="center" width="40px">
												<s:if test="#fc.type==430||#fc.type==431||#fc.type==432||#fc.type==433||#fc.type==434||#fc.type==435||#fc.type==436||#fc.type==437||#fc.type==531||#fc.type==532||#fc.type==533||#fc.type==534||#fc.type==535||#fc.type==536||#fc.type==537||#fc.type==571||#fc.type==572||#fc.type==573||#fc.type==581||#fc.type==582||#fc.type==583||#fc.type==584||#fc.type==401||#fc.type==402||#fc.type==403||#fc.type==404||#fc.type==405||#fc.type==406||#fc.type==407||#fc.type==408||#fc.type==409||#fc.type==410||#fc.type==411||#fc.type==412||#fc.type==413||#fc.type==414||#fc.type==419||#fc.type==425||#fc.type==422||#fc.type==423||#fc.type==424||#fc.type==426||#fc.type==427||#fc.type==428||#fc.type==429">
													<c:if test="${ sessionScope.operator.authority ne 'finance' and sessionScope.operator.authority ne 'finance_leader' and sessionScope.operator.authority ne 'finance_manager'}">
														<s:if test="#fc.flag==0">
															<a onclick="submitYhjAction(this,'${fc.pno}');" style="cursor: pointer" title="审核">审核</a>
														</s:if>
													</c:if>
												</s:if>
											</td>
											<td align="center" width="40px">
												<s:if test="#fc.type==430||#fc.type==431||#fc.type==432||#fc.type==433||#fc.type==434||#fc.type==435||#fc.type==436||#fc.type==437||#fc.type==531||#fc.type==532||#fc.type==533||#fc.type==534||#fc.type==535||#fc.type==536||#fc.type==537||#fc.type==571||#fc.type==572||#fc.type==573||#fc.type==581||#fc.type==582||#fc.type==583||#fc.type==584||#fc.type==401||#fc.type==402||#fc.type==403||#fc.type==404||#fc.type==405||#fc.type==406||#fc.type==407||#fc.type==408||#fc.type==409||#fc.type==410||#fc.type==411||#fc.type==419||#fc.type==412||#fc.type==413||#fc.type==414||#fc.type==425||#fc.type==422||#fc.type==423||#fc.type==424||#fc.type==426||#fc.type==427||#fc.type==428||#fc.type==429">
													<c:if test="${ sessionScope.operator.authority ne 'finance' and sessionScope.operator.authority ne 'finance_leader' and sessionScope.operator.authority ne 'finance_manager'}">
														<s:if test="#fc.flag==0">
															<input type="button" value="取消" onclick="submitYhjCancelAction(this,'${fc.pno}');" />
														</s:if>
													</c:if>
												</s:if>
											</td>
										</s:if>
										<s:else>
											<td align="center" width="80px">
												<s:url namespace="/office" action="queryTaskDetail"
													var="queryTaskDetailUrl" escapeAmp="false">
													<s:param name="pno" value="#fc.pno" />
												</s:url>
												<s:a target="_blank" href="%{queryTaskDetailUrl}">
													<s:property value="#fc.pno" />
												</s:a>
											</td>
											<td align="left" width="80px">
												<s:property value="#fc.proposer" />
											</td>
											<td align="center" width="70px">
												<s:if test="#fc.agentType=='AGENT'">代理</s:if>
												<s:else>真钱会员</s:else>
											</td>
											<td align="center" width="70px">
												<s:property
													value="@dfh.model.enums.ProposalType@getText(#fc.type)" />
											</td>
											<td align="center" width="60px">
												<s:property
													value="@dfh.model.enums.ProposalFlagType@getText(#fc.flag)" />
											</td>
											<td align="left" width="130px">
												<s:property value="#fc.loginname" />
											</td>
											<td align="left" width="130px" style="word-break: break-all;white-space: normal;">
												<s:property value="#fc.userRemark" />
											</td>
											<td align="right" width="90px">
												<s:property
													value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" />
											</td>
											<td align="center" width="130px">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
											</td>
											<td align="center" width="130px">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.executeTime" />
											</td>
											<td align="left" width="80px" bgcolor="${duanbgcolor}">
												<s:if test="#fc.msflag==1">
													<s:property
														value="@dfh.utils.DateUtil@getTimecha(#fc.createTime,#fc.executeTime)" />
												</s:if>
												<s:else>
													<s:property value="#fc.timecha" />
												</s:else>
											</td>
											<s:if test="#fc.msflag==1">
												<td align="center" width="80px">
													是
												</td>
												<s:if test="#fc.mstype==2">
													<td align="center" width="80px">
														成功
													</td>
												</s:if>
												<s:elseif test="#fc.mstype==1">
													<s:if test="#fc.unknowflag==4">
														<td align="center" width="80px" bgcolor="#00ff00">
															未知情况
														</td>
													</s:if>
													<s:else>
														<td align="center" width="80px">
															失败
														</td>
													</s:else>
												</s:elseif>
												<s:else>
													<s:if test="#fc.flag==-1">
														<td align="center" width="80px">
															已取消
														</td>
													</s:if>
													<s:else>
														<td align="center" width="80px">
															待执行
														</td>
													</s:else>
												</s:else>

											</s:if>
											<s:else>
												<td align="center" width="80px">
													否
												</td>
												<td align="center" width="80px"></td>
											</s:else>

											<td align="left" width="60px">
													<s:property value="@dfh.model.enums.VipLevel@getText(#fc.quickly)" />
											</td>
											<td align="center" width="80px">
												<s:if test="#fc.mssgflag==1">是</s:if>
												<s:else>否</s:else>
											</td>
											<td align="left" width="140px" bgcolor="${auditbgcolor}">
												<s:property value="#fc.remark" />
											</td>
											<td align="center" width="40px">
												<s:url id="toquery" escapeAmp="false"
													action="queryProposalDetail" namespace="/office">
													<s:param name="pno" value="#fc.pno" />
													<s:param name="proposalType" value="#fc.type" />
												</s:url>
												<a onclick="openproposaldetail('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor: pointer" title="点击查看提案的明细信息">查询</a>
											</td>
											<s:set var="jobPno" value="#fc.pno" scope="request" />
											<s:if
												test="#fc.flag==@dfh.model.enums.ProposalFlagType@SUBMITED.code">
												<td align="center" width="35px">

													<s:if test="#fc.type==@dfh.model.enums.ProposalType@CASHOUT.code">
														<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
															<s:if test="#fc.agentType!='AGENT' || #session.operator.authority=='boss'">
																<s:if test="#fc.msflag==0">
																	<a onclick="cashoutaudit('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor: pointer" title="审核">审核</a>
																</s:if>
															</s:if>
														</s:if>
														<!-- 针对市场提供审核代理提款 -->
														<s:if test="#fc.agentType=='AGENT' && (#session.operator.authority=='market' || #session.operator.authority=='market_manager' || #session.operator.authority=='finance' || #session.operator.authority=='finance_leader' || #session.operator.authority=='finance_manager')">
															<s:if test="#fc.msflag==0">
																<a onclick="cashoutaudit('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor: pointer" title="审核">审核</a>
															</s:if>
														</s:if>
													</s:if>
													<s:elseif test="#fc.type==@dfh.model.enums.ProposalType@PROXYADVANCE.code">
														<!-- 预支只能是boss审核 -->
														<s:if test="#fc.agentType=='AGENT' && #session.operator.authority=='boss'">
															<s:if test="#fc.msflag==0">
																<a onclick="cashoutaudit('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor: pointer" title="审核">审核</a>
															</s:if>
														</s:if>
													</s:elseif>
													<s:else>
														<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
															<c:url var="action" value="/office/auditProposal.do" scope="request" />
															<input type="button" value="审核" onclick="submitForNewAction(this,'${action}','${jobPno}');" />
														</s:if>
													</s:else>
												</td>
												<td align="center" width="35px">
													<s:if test="@dfh.security.Authorities@canAccess(request,'cancle')">
														<s:if test="#fc.agentType!='AGENT' || #session.operator.authority=='boss'">
															<c:url var="action" value="/office/cancleProposal.do" scope="request" />
															<input type="button" value="取消" onclick="submitForNewAction(this,'${action}','${jobPno}');" />
														</s:if>
													</s:if>
													<!-- 针对市场提供审核代理提款 -->
													<s:if test="#fc.agentType=='AGENT' && (#session.operator.authority=='market' || #session.operator.authority=='market_manager' || #session.operator.authority=='finance' || #session.operator.authority=='finance_leader' || #session.operator.authority=='finance_manager')">
														<c:url var="action" value="/office/cancleProposal.do" scope="request" />
														<input type="button" value="取消" onclick="submitForNewAction(this,'${action}','${jobPno}');" />
													</s:if>
												</td>
											</s:if>
											<s:elseif test="#fc.flag==@dfh.model.enums.ProposalFlagType@AUDITED.code">
												<td align="center" width="35px">
													<s:if test="#fc.type==@dfh.model.enums.ProposalType@CASHOUT.code">
														<s:if test="@dfh.security.Authorities@canAccess(request,'excute')">
															<a onclick="submitForCashOut('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')" style="cursor: pointer" title="执行">执行</a>
														</s:if>
													</s:if>
													<s:elseif
														test="#fc.type==@dfh.model.enums.ProposalType@PROXYADVANCE.code">
														<s:if
															test="@dfh.security.Authorities@canAccess(request,'excute')">
															<a
																onclick="submitForCashOut('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')"
																style="cursor: pointer" title="执行">执行</a>
														</s:if>
													</s:elseif>
													<s:else>
														<s:if
															test="@dfh.security.Authorities@canAccess(request,'excute')">
															<s:if test="#fc.type!=@dfh.model.enums.ProposalType@PROFIT.code && #fc.type!=@dfh.model.enums.ProposalType@WEEKSENT.code">
																<c:url var="action" value="/office/excuteProposal.do"
																	scope="request" />
																<input type="button" value="执行"
																	onclick="submitForNewAction(this,'${action}','${jobPno}');" />
															</s:if>		
														</s:if>
													</s:else>

												</td>
												<td align="center" width="35px">
													<s:if
														test="@dfh.security.Authorities@canAccess(request,'cancle')">
														<s:if test="#fc.agentType!='AGENT' || #session.operator.authority=='boss'">
															<c:url var="action" value="/office/cancleProposal.do"
																scope="request" />
															<input type="button" value="取消"
																onclick="submitForNewAction(this,'${action}','${jobPno}');" />
														</s:if>
													</s:if>
													<!-- 针对市场提供审核代理提款 -->
													<s:if test="#fc.agentType=='AGENT' && (#session.operator.authority=='market' || #session.operator.authority=='market_manager' || #session.operator.authority=='finance' || #session.operator.authority=='finance_leader' ||#session.operator.authority=='finance_manager')">
														<c:url var="action" value="/office/cancleProposal.do" scope="request" />
														<input type="button" value="取消" onclick="submitForNewAction(this,'${action}','${jobPno}');" />
													</s:if>
												</td>
											</s:elseif>
											<%-- <s:elseif  test="#fc.flag==2">
												<s:if test="#fc.type==@dfh.model.enums.ProposalType@OFFER.code && #fc.remark=='BEGIN'+#fc.loginname">
													<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance'">
													<td>
														<input type="button" value="再存优惠完成" onclick="confirmOfferFinished(this,'${jobPno}')" />
													</td>
													</s:if>			
												</s:if>
											</s:elseif> --%>
											<s:elseif test="#fc.flag==-1">
												<s:if test="#fc.msflag==1">
														<s:if test="#fc.mstype==1">
															<s:if test="#fc.unknowflag!=4">
																<td align="center">
																<%-- <input type="button" value="rpay"
																	onclick="submitRpayAction(this,'/office/rPayProposal.do','${jobPno}');" /> --%>
																</td>
															</s:if>
															<s:else>
																<td align="center">
																</td>
															</s:else>
														</s:if>
														<td align="center">
														</td>
												</s:if>
											</s:elseif>
											<s:elseif test="#fc.flag==2">
												<s:if test="#fc.msflag==0">
													<s:if test="#fc.type==517||#fc.type==561||#fc.type==611||#fc.type==612||#fc.type==613||#fc.type==614||#fc.type==615||#fc.type==616||#fc.type==617||#fc.type==618||#fc.type==619||#fc.type==623||#fc.type==622||#fc.type==624||#fc.type==625||#fc.type==627||#fc.type==628||#fc.type==629||#fc.type==630||#fc.type==632">
														<!-- 秒反水 -->
														<s:if test="#fc.passflag==2">
															<s:if
																test="@dfh.security.Authorities@canAccess(request,'excute')">
																<td align="center">
																	<c:url var="action" value="/office/secondProposal.do"
																		scope="request" />
																	<a
																		onclick="submitForNewAction(this,'${action}','<s:property value="#fc.pno"/>')"
																		style="cursor: pointer" title="审核">审核</a>
																</td>
															</s:if>
															<s:else>
																<td align="center">
																</td>
															</s:else>
															<td align="center">
															</td>
														</s:if>
														<s:else>
															<td align="center">
															</td>
															<td align="center">
															</td>
														</s:else>
													</s:if>
													<s:else>
														<td align="center">
														</td>
														<td align="center">
														</td>
													</s:else>
												</s:if>
												<s:elseif test="#fc.msflag==1">
													<s:if test="#fc.mstype==2">
														<s:if test="#fc.passflag==0&&#fc.mssgflag!=1">
															<s:if
																test="@dfh.security.Authorities@canAccess(request,'excute')">
																<td align="center">
																	<a
																		onclick="cashoutauditforms('<s:property value="#fc.pno"/>','<s:property value="#fc.type"/>')"
																		style="cursor: pointer" title="审核">审核</a>
																</td>
															</s:if>
															<s:else>
																<td align="center">
																</td>
															</s:else>
															<td align="center">
															</td>
														</s:if>
														<s:else>
															<td align="center">
															</td>
															<td align="center">
															</td>
														</s:else>
													</s:if>
													<s:else>
														<td align="center">
														</td>
														<td align="center">
														</td>
													</s:else>
												</s:elseif>
												
												<s:if test="#fc.type==@dfh.model.enums.ProposalType@OFFER.code && #fc.remark=='BEGIN'+#fc.loginname">
													<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance_leader' || #session.operator.authority=='finance' ">
													<td>
														<input type="button" value="再存优惠完成" onclick="confirmOfferFinished(this,'${jobPno}')" />
													</td>
													</s:if>			
												</s:if>
												
											</s:elseif>
											<s:else>
												<td align="center">
												</td>
												<td align="center">
												</td>
											</s:else>

											<s:else>
												<td align="center">
												</td>
												<td align="center">
												</td>
											</s:else>
										</s:else>
									</tr>
									<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
									<s:set var="amountValuegifTamount" value="#fc.gifTamount"
										scope="request"></s:set>
									<c:set var="amountSum"
										value="${amountSum+amountValue}"
										scope="request"></c:set>
									<c:set var="amountSumYhj"
										value="${amountSumYhj+amountValuegifTamount}"
										scope="request"></c:set>	
								</s:iterator>
								<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_leader' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager' || (#request.loginname != null && #request.loginname != '')">  
									<tr bgcolor="#e4f2ff">
										<td align="right" colspan="7">
											当页小计:
										</td>
										<td align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
										</td>
										<td align="center" colspan="12"></td>
									</tr>
								
	                                <tr bgcolor="#e4f2ff">
										<td align="right" colspan="7">
											当页优惠小计:
										</td>
										<td align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.amountSumYhj)" />
										</td>
										<td align="center" colspan="12"></td>
									</tr>
								</s:if>
								<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_leader' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager' || (#request.loginname != null && #request.loginname != '') || (#request.agent != null && #request.agent != '')">  
								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="7">
										总计:
									</td>
									<td align="right">
										<s:set var="statics1" value="#request.page.statics1"
											scope="request"></s:set>
										<c:set var="amountSumAll" value="${statics1}"
											scope="request"></c:set>
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.amountSumAll)" />
									</td>
									<td align="center" colspan="12"></td>
								</tr>
                                <tr bgcolor="#e4f2ff">
									<td align="right" colspan="7">
										优惠总计:
									</td>
									<td align="right">
										<s:set var="statics2" value="#request.page.statics2"
											scope="request"></s:set>
										<c:set var="amountSumAll2" value="${statics2}"
											scope="request"></c:set>
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.amountSumAll2)" />
									</td>
									<td align="center" colspan="12"></td>
								</tr>
								</s:if>
								<tr>
									<td colspan="18" align="right" bgcolor="66b5ff" align="center">
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

