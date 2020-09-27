<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>输赢信息分析</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.21.custom.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="excel_menu_left">客户分析&nbsp;-->&nbsp;输赢信息分析<a href="javascript:history.back();"><font color="red">上一步</font></a></div>
		<s:form action="queryCustomerInfoAnalysisNew2" namespace="/office" name="mainform" id="mainform" theme="simple">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" width="1280px">
							<tr align="left">
								<td>用户账号：<s:textfield name="loginname"></s:textfield></td>
								<td>会员等级：<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true" /></td>
								<td>起始时间：<s:textfield id="_startTime" name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" value="%{startTime}" /></td>
								<td>代理注册起始时间：<s:textfield name="startDate" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" value="%{startDate}" /></td>
								<td>代理账号：<s:textfield name="agent"></s:textfield></td>
								<td>会员状态：<s:select name="flag" emptyOption="true" list="#{'0':'启用','1':'禁用'}"></s:select></td>
								<td>未登录间隔(天)：<s:select name="nintvalday" emptyOption="true" list="#{'31':'一个月以上','58':'两个月以上','88':'三个月以上'}"></s:select></td>
								<td>代理类型：<s:select name="agentType" list="#{-1:'代理',1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="true"></s:select></td>
								<td rowspan="2"><s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit></td>
							</tr>
							<tr align="left">
								<td>推荐码：<s:textfield name="intro"></s:textfield></td>
								<td>代理推荐码：<s:textfield name="partner" cssStyle="width:60px;"></s:textfield></td>
								<td>结束时间：<s:textfield id="_endTime" name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" value="%{endTime}" /></td>
								<td>代理注册结束时间：<s:textfield name="endDate" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" value="%{endDate}" /></td>
								<td>警告等级：<s:select list="%{#application.WarnLevel}" listKey="code" listValue="text" name="warnflag" emptyOption="true"></s:select></td>
								<td>时间间隔(天)：<s:select name="intvalday" emptyOption="true" list="#{'7':'一周内','14':'两周内','31':'一个月内'}"></s:select></td>
								<td><input type="button" id="phoneQunHu" onclick="batchCall()" value="批量呼叫" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<input type="hidden" value="${startdate}" id="begin">
			<input type="hidden" value="${enddate}" id="end">

			<s:set name="by" value="'dtprofit'" />
			<s:set name="order" value="'desc'" />
			<s:hidden name="order" value="%{order}"/>
			<s:hidden name="by" value="%{by}"/>
		</s:form>
		<br />
		<div id="middle">
  			<div id="right">
    			<div id="right_01">
					<div id="right_001">
	  					<div id="right_02">
	    					<div id="right_03"></div>
	  					</div>
						<div id="right_04">
							<table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">序号</td>
									<td bgcolor="#0084ff" align="center"><input type="checkbox" onclick="checkAll(this)"></td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">会员账号</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">会员姓名</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">警告级别</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">会员等级</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">上级代理</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">推荐码</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('deposit')">存款总额度</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('pay')">在线支付总额度</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('withdrawal')">提款总额度</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">好友推荐金</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">用户注册时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">用户最后登录时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('intvalday')">时间间隔(天)</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('loginTimes')">用户登录次数</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('dtprofit')">盈利额</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('zdprofit')">总盈利额</td>
								</tr>
								<s:iterator value="#request.page" var="customerAnalysis" status="st">
								<tr>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value='#st.index+1' /></td>
									<td bgcolor="#e4f2ff" align="center"><input type="checkbox" name="item" value="<s:property value="#customerAnalysis[0]"/>"></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><a target="_blank" href='/office/getUserhavinginfo.do?loginname=<s:property value="#customerAnalysis[0]"/>'><s:property value="#customerAnalysis[0]" /></a></td>
									<td bgcolor="#e4f2ff" style="font-size: 13px;"><s:property value="#customerAnalysis[1]" /></td>
									<td 
										<c:choose>
            								<c:when test="${customerAnalysis[2] eq 0}">bgcolor="#e4f2ff"</c:when>
            								<c:when test="${customerAnalysis[2] eq 1}">bgcolor="#f3deac"</c:when>
            								<c:when test="${customerAnalysis[2] eq 2}">bgcolor="#fb8d8d"</c:when>
            								<c:otherwise>bgcolor="#9beda3"</c:otherwise>
            							</c:choose>
            							align="center"  style="font-size:13px;">
            						<s:property value="@dfh.model.enums.WarnLevel@getText(#customerAnalysis[2])"/>
            						</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="@dfh.model.enums.VipLevel@getText(#customerAnalysis[3])" /></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="#customerAnalysis[4]" /></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
										<c:if test="${sessionScope.operator.authority eq 'boss'}">
											<a href="#" onclick="editUserIntro('<s:property value="#customerAnalysis[0]"/>');" />
										</c:if> 
										<s:if test="#customerAnalysis[5] != null && #customerAnalysis[5] != ''">
											<s:property value="#customerAnalysis[5]" />
										</s:if>
										<s:else>无</s:else>
									</td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis[7])" /></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis[8])" /></td>
									<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis[9])"/></td>
									<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis[10])"/></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="#customerAnalysis[11]" /></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="#customerAnalysis[12]" /></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="#customerAnalysis[13]" /></td>
									<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"><s:property value="#customerAnalysis[14]" /></td>
									<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis[15])"/></td>
									<td
										<s:if test="#customerAnalysis[16] >= 100000">bgcolor="red"</s:if>
										<s:else>bgcolor="#e4f2ff"</s:else> align="center" style="font-size: 13px;">
										<s:property value="#customerAnalysis[16]" />
									</td>
								</tr>
								<s:set var="amountValue1" value="#customerAnalysis[7]" scope="request"></s:set>
								<c:set var="deposit" value="${deposit+amountValue1}" scope="request"></c:set>
								<s:set var="amountValue2" value="#customerAnalysis[8]" scope="request"></s:set>
								<c:set var="pay" value="${pay+amountValue2}" scope="request"></c:set>
								<s:set var="amountValue3" value="#customerAnalysis[9]" scope="request"></s:set>
								<c:set var="withdrawal" value="${withdrawal+amountValue3}" scope="request"></c:set>
								<s:set var="amountValue4" value="#customerAnalysis[10]" scope="request"></s:set>
								<c:set var="friendBonus" value="${friendBonus+amountValue4}" scope="request"></c:set>
								
								<s:set var="amountValue5" value="#customerAnalysis[15]" scope="request"></s:set>
								<c:set var="dtprofit" value="${dtprofit+amountValue5}" scope="request"></c:set>
								<s:set var="amountValue6" value="#customerAnalysis[16]" scope="request"></s:set>
								<c:set var="zdprofit" value="${zdprofit+amountValue6}" scope="request"></c:set>
							</s:iterator>
							<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager' || (#request.loginname!='' && #request.loginname!=null)">
								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="8">总计:</td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.deposit)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.pay)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.withdrawal)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.friendBonus)" /></td>
									<td align="center" colspan="4"></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.dtprofit)" /></td>
									<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.zdprofit)" /></td>
								</tr>
							</s:if>
						</table>	
						</div>
					</div>
				</div>
  			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">

		if($('#begin').val()!=''&&$('#end').val()!=''){
		    $('#_startTime').val('').val($('#begin').val());
            $('#_endTime').val('').val($('#end').val());
		}
	
		function editUserIntro(_loginname) {
			
			var value = window.prompt("修改用户【 " + _loginname + " 】推荐码！", "");
			
			var action = "/office/editUser.do";
			
			if (value) {
				
				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: "loginname=" + _loginname + "&intro=" + value + "&r=" + Math.random(), onComplete: responseMethod });
			} else {
				
				alert("参数不允许为空!");
			}
		}
				
		function responseMethod(data) {
			
			alert(data.responseText);
			
			document.getElementById("mainform").submit();
		}
		
		function checkAll(self) {

			$("[name='item']:checkbox").attr("checked", $(self).is(':checked'));
		}
		
		function batchCall() {
			
			var result = new Array();
			
			$("[name='item']:checkbox").each(function() {
				
				if ($(this).is(":checked")) {
					
					result.push($(this).attr("value"));
				}
			});
			
			var len = result.length;
			
			if (len > 0) {
				
				if (confirm("共选中" + len + "个手机号，确认群呼？")) {
					
					var ids = result.join(",");
					
					$.ajax({
						type : "post",
						url : "/office/executeCallDataAnalysis.do",
						data : "ids=" + ids,
						success : function(data) {

							alert(data);
						}
					});
				}
			} else {

				alert("请选择您要发送的数据！");
			}
		}
		
		function orderby(by) {

			if (document.mainform.order.value == "desc") {

				document.mainform.order.value = "asc";
			} else {

				document.mainform.order.value = "desc";
			}

			document.mainform.by.value = by;
			document.mainform.submit();
		}
	</script>
</html>