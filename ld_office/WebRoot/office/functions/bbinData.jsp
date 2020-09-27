<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
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
<title>BBIN平台数据查询(BBIN DATA QUERY)</title>
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
	function showAndHidden(eid) {
		if (eid == "a1") {
			document.getElementById("a1").style.display = "block";
			document.getElementById("a2").style.display = "none";
		} else if (eid == "a2") {
			document.getElementById("a1").style.display = "none";
			document.getElementById("a2").style.display = "block";
		}
	}
	function submitFrom1() {
		var loginname = $("#loginname1").val();
		if (loginname == "") {
			alert("玩家账号不能为空!(Player account cannot be empty!)");
			return false;
		}
		$("#form1").submit();
	}

    function submitFrom2() {
        var loginname = $("#loginname2").val();
        var billno = $("#billno").val();
        if (billno == "" && loginname == "") {
            alert("玩家账号不能为空!(Player account cannot be empty!)");
            return false;
        }
        $("#form2").submit();
    }
</script>
</head>
<body>
	<p>
		--&gt; BBIN平台数据查询 (BBIN DATA QUERY)<a href="javascript:history.back();"><font
			color="red">上一步</font> </a>
	</p>
	<div style="position: absolute; top: 50px; left: 50px; height: 25px; width: 300px; background: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a1');"><b>bbin转账记录(bbin transfer record)</b> </a>
		</div>
	</div>
	<div style="position: absolute; top: 50px; left: 352px; height: 25px; width: 300px; background-color: #09F">
		<div align=center style="margin-top: 4px;">
			<a style="text-decoration: none; text-decoration: none"
				href="javascript:showAndHidden('a2');"><b>bbin游戏记录(bbin game record)</b> </a>
		</div>
	</div>
	
<s:if test="%{#request.type ==0}">
	<div id="a1" style="position: absolute; display: block; top: 6px; left: 0px;">
</s:if>
<s:else>
	<div id="a1" style="position: absolute; display: none; top: 6px; left: 0px;">
</s:else>
	<div style="position: absolute; display: block; top: 76px; left: 50px;">
		<s:form action="queryBbinData" namespace="/office" name="form1" id="form1" theme="simple">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" width="1050px">
							<tr align="left">
								<td align="right" width="60px">玩家帐号: 
								</td>
								<td align="left" width="60px">
									<s:textfield cssStyle="width:120px" name="loginname" id="loginname1" size="30" />
								</td>
								<td align="right" width="60px">开始时间:</td>
								<td align="left" width="80px" ><s:textfield name="startTime"
										size="18"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										My97Mark="false" value="%{startTime}" cssClass="Wdate" /></td>
								<td align="right" width="70px">结束时间:</td>
								<td align="left" width="80px"><s:textfield name="endTime" size="18"
										cssStyle="width:150px"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										My97Mark="false" value="%{endTime}" cssClass="Wdate" /></td>
								<td align="right" width="70px">类别: 
								</td>
								<td align="left" width="120px">
									<s:select cssStyle="width:150px"
										list="#{'':'全部记录(all record)','IN':'转入(in)','OUT':'转出(out)'}"
										key="key" value="%{paytype}" name="paytype"></s:select>
								</td>
								<td rowspan="2">
									<s:submit cssStyle="width:65px; height:22px;" value="查询" onclick="return submitFrom1();"></s:submit>
								</td>
								<s:hidden name="type" value="0"/>
							</tr>
						</table>
					</td>
				</tr>

			</table>
		</s:form>
	</div>
	<br />
	<br />
	<div id="middle" style="position: absolute; top: 150px; left: 50px">
		<div id="right">
			<div id="right_01">
				<div id="right_001">
					<div id="right_04">
						<table width="950px" border="0" cellpadding="1" cellspacing="1"
							bgcolor="#99c8d7">
							<tr bgcolor="#0084ff">
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									玩家账号</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="180px">
									转账时间</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									转账类型</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									转账金额(元)</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									账户余额(元)</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="200px">
									交易ID</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									币种</td>
							</tr>
							<c:set var="amountSum" value="0" scope="request"></c:set>
							<s:iterator var="fc" value="%{#request.listt}">
					            <s:if test="#fc.Amount>=100000"><c:set var="bgcolor" value="#FF9999"/></s:if>
					            <s:elseif test="#fc.Amount>=10000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
					            <s:elseif test="#fc.Amount>=1000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
					            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            					<tr bgcolor="${bgcolor}">
									<td align="center" width="60px"><s:property value="#fc.UserName" /></td>
									<td align="center" width="180px"><s:property value="#fc.CreateTime" /></td>
									<td align="center" width="90px">
										<s:if test="#fc.TransType=='IN'">
											转入(IN)
										</s:if> 
										<s:elseif test="#fc.TransType=='OUT'">
											转出(OUT)
										</s:elseif>
									</td>
									<td align="center" width="60px"><s:property value="#fc.Amount" /></td>
									<td align="center" width="60px"><s:property value="#fc.Balance" /></td>
									<td align="center" width="60px"><s:property value="#fc.TransID" /></td>
									<td align="center" width="60px"><s:property value="#fc.Currency" /></td>
								</tr>
								<s:set var="amountValue" value="#fc.Amount" scope="request"></s:set>
            					<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
							</s:iterator>
							<tr>
								<td bgcolor="#e4f2ff" align="right" colspan="3">合计:</td>
								<td bgcolor="#e4f2ff" align="center">
									<s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)"/>
								</td>
								<td bgcolor="#e4f2ff" align="center"></td>
								<td bgcolor="#e4f2ff" align="center"></td>
								<td bgcolor="#e4f2ff" align="center"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:import url="/office/script.jsp" />
</div>	

<s:if test="%{#request.type ==1}">
	<div id="a2" style="position: absolute; display: block; top: 6px; left: 0px;">
</s:if>
<s:else>
	<div id="a2" style="position: absolute; display: none; top: 6px; left: 0px;">
</s:else>

	<div style="position: absolute; display: block; top: 76px; left: 50px;">
		<s:form action="queryBbinData" namespace="/office" name="form2" id="form2" theme="simple">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" width="1250px">
							<tr align="left">
								<td align="right">玩家帐号: 
								</td>
								<td align="left" width="60px">
									<s:textfield cssStyle="width:120px" id="loginname2" name="loginname" size="30" />
								</td>
								<td align="right">真人局号: 
								</td>
								<td align="left" width="60px">
									<s:textfield cssStyle="width:120px" name="billno" size="30" />
								</td>
								<td align="right" width="60px">开始时间:</td>
								<td align="left" width="80px" ><s:textfield name="startTime"
										size="18"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										My97Mark="false" value="%{startTime}" cssClass="Wdate" /></td>
								<td align="right" width="70px">结束时间:</td>
								<td align="left" width="80px"><s:textfield name="endTime" size="18"
										cssStyle="width:150px"
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										My97Mark="false" value="%{endTime}" cssClass="Wdate" /></td>
								<td align="right" width="70px">类别: 
								</td>
								<td align="left" width="120px">
									<s:select cssStyle="width:150px"
										list="#{'':'全部游戏(all data)','3':'BB真人数据(bb video data)','5':'BB电子数据(bb electronic data)'}"
										key="key" value="%{paytype}" name="paytype"></s:select>
								</td>
								<td rowspan="2">
									<s:submit cssStyle="width:65px; height:22px;" value="查询" onclick="return submitFrom2();"></s:submit>
								</td>
								<s:hidden name="type" value="1"/>
							</tr>
						</table>
					</td>
				</tr>

			</table>
		</s:form>
	</div>
	<br />
	<br />
	<div id="middle" style="position: absolute; top: 150px; left: 50px">
		<div id="right">
			<div id="right_01">
				<div id="right_001">
					<div id="right_04">
						<table width="1200px" border="0" cellpadding="1" cellspacing="1"
							bgcolor="#99c8d7">
							<tr bgcolor="#0084ff">
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									玩家账号</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									真人局号</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									注单号码</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="180px">
									下注时间</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="180px">
									游戏编号</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="180px">
									游戏种类</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									下注金额(元)</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									派彩金额(元)</td>
								<td align="center"
									style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
									有效投注额(元)</td>
							</tr>
							<c:set var="betAmountSum" value="0" scope="request"></c:set>
							<c:set var="payoffSum" value="0" scope="request"></c:set>
							<c:set var="commissionableSum" value="0" scope="request"></c:set>
							<s:iterator var="fc" value="%{#request.listb}">
					            <s:if test="#fc.payoff>=100000"><c:set var="bgcolor" value="#FF9999"/></s:if>
					            <s:elseif test="#fc.payoff>=10000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
					            <s:elseif test="#fc.payoff>=1000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
					            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            					<tr bgcolor="${bgcolor}">
									<td align="center" width="60px"><s:property value="#fc.userName" /></td>
									<td align="center" width="180px"><s:property value="#fc.serialID" /></td>
									<td align="center" width="180px"><s:property value="#fc.wagersID" /></td>
									<td align="center" width="180px"><s:property value="#fc.wagersDateStr"/></td>
									<td align="center" width="180px"><s:property value="#fc.gameType" /></td>
									<td align="center" width="90px">
										<s:if test="#fc.gamekind==3">
											BB真人(bb video)
										</s:if> 
										<s:elseif test="#fc.gamekind==5">
											BB电子(bb electronic)
										</s:elseif>
									</td>
									<td align="center" width="60px"><s:property value="#fc.betAmount" /></td>
									<td align="center" width="60px"><s:property value="#fc.payoff" /></td>
									<td align="center" width="60px"><s:property value="#fc.commissionable" /></td>
								</tr>
								<s:set var="betAmountValue" value="#fc.betAmount" scope="request"></s:set>
            					<c:set var="betAmountSum" value="${betAmountSum+betAmountValue}"  scope="request"></c:set>
            					
								<s:set var="payoffValue" value="#fc.payoff" scope="request"></s:set>
            					<c:set var="payoffSum" value="${payoffSum+payoffValue}"  scope="request"></c:set>
            					
								<s:set var="commissionableValue" value="#fc.commissionable" scope="request"></s:set>
            					<c:set var="commissionableSum" value="${commissionableSum+commissionableValue}"  scope="request"></c:set>
							</s:iterator>
							<tr>
								<td bgcolor="#e4f2ff" align="right" colspan="6">合计:</td>
								<td bgcolor="#e4f2ff" align="center">
									<s:property value="@dfh.utils.NumericUtil@double2String(#request.betAmountSum)"/>
								</td>
								<td bgcolor="#e4f2ff" align="center">
									<s:property value="@dfh.utils.NumericUtil@double2String(#request.payoffSum)"/>
								</td>
								<td bgcolor="#e4f2ff" align="center">
									<s:property value="@dfh.utils.NumericUtil@double2String(#request.commissionableSum)"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:import url="/office/script.jsp" />
</div>	
</body>
</html>

