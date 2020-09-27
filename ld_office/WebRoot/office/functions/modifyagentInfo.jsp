<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改代理基本信息</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
			<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript">
	//得到用户信息
	function loadInfo() {
		var frm=document.getElementById("mainform");
		frm.action="<c:url value='/office/getAgentinfo.do' />";
		frm.submit();
	}
</script>
<style type="text/css">
		body{
			font-size:12px;
			background:#b6d9e4;
		}
</style>
	</head>
	<body>
		<div id="excel_menu_left">
			操作 --&gt; 修改代理资料
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<s:fielderror></s:fielderror>
		<s:form action="modifyagentRemark" onsubmit="submitonce(this);" namespace="/office" id="mainform" theme="simple">
			<table align="left" border="0">
				<tr>
					<td>
						<span style="color:red">*</span>代理账号:
					</td>
					<td>
						<s:textfield name="loginname" size="30" onblur="loadInfo();" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>真实姓名:
					</td>
					<td>
						<s:property value="%{#request.user.accountName}"/>
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>代理网址:
					</td>
					<td>
						<s:textfield name="referWebsite" size="50"  value="%{#request.user.referWebsite}" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>旧代理agcode:
					</td>
					<td>
						<s:textfield name="agcode" size="25"  value="%{#request.user.agcode}" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>新代理agcode:
					</td>
					<td>
						<s:textfield name="newagcode" size="25"  value="%{#request.user.agcode}" />
					</td>
				</tr>
				<c:choose>
					<c:when test="${ sessionScope.operator.authority eq 'boss'}">
				
			
				<tr>
					<td>
						<span style="color:red">*</span>Email:
					</td>
					<td>
						<s:textfield name="email" size="30" value="%{#request.user.email}" />
					</td>
				</tr>

				<tr>
					<td>
						<span style="color:red">*</span>联系电话:
					</td>
					<td>
						<s:textfield name="phone" size="30" value="%{#request.user.phone}" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>QQ:
					</td>
					<td>
						<s:textfield name="qq" size="30" value="%{#request.user.qq}" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>代理推荐码:
					</td>
					<td>
						<s:textfield name="partner" size="20" value="%{#request.user.partner}" />
					</td>
			    </tr>
				   </c:when>
			
				<c:otherwise>
				
				<c:choose>
					<c:when test="${ sessionScope.operator.agent ne null  && (not empty requestScope.user.partner)  && fn:containsIgnoreCase(sessionScope.operator.agent, requestScope.user.partner)}">
				
				<tr>
					<td>
						<span style="color:red">*</span>Email:
					</td>
					<td>
						<s:textfield name="email" size="30"     readonly="true"/>
					</td>
				</tr>

				<tr>
					<td>
						<span style="color:red">*</span>联系电话:
					</td>
					<td>
						<s:textfield name="phone" size="30"   readonly="true" />
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>QQ:
					</td>
					<td>
						<s:textfield name="qq" size="30" value="%{#request.user.qq}" readonly="true" />
					</td>
				</tr>
			
				 </c:when>
			
				<c:otherwise>
				
				<tr>
					<td>
						<span style="color:red">*</span>Email:
					</td>
					<td>
						<s:textfield name="email" size="30"    readonly="true" type="password"/>
					</td>
				</tr>

				<tr>
					<td>
						<span style="color:red">*</span>联系电话:
					</td>
					<td>
						<s:textfield name="phone" size="30"   readonly="true" type="password"/>
					</td>
				</tr>
				<tr>
					<td>
						<span style="color:red">*</span>QQ:
					</td>
					<td>
						<s:textfield name="qq" size="30" value="%{#request.user.qq}" readonly="true" type="password"/>
					</td>
				</tr>
				</c:otherwise>
					</c:choose>
				<tr>
					<td>
						<span style="color:red">*</span>代理推荐码:
					</td>
					<td>
						<s:textfield name="partner" size="20" value="%{#request.user.partner}" readonly="true" type="password"/>
					</td>
			    </tr>
				</c:otherwise>
					</c:choose>
				
				<tr>
					<td>
						<span style="color:red"></span>代理类型:
					</td>
					<td>
						<s:select name="agentType" list="#{1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="true" value="%{#request.inAgency.type }"></s:select><span style="color:red">空代表外部代理</span>
					</td>
				</tr>
				
				<c:if test="${sessionScope.operator.authority eq 'market_manager' || sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'sale_manager'}">
				<tr>
					<td>老虎机佣金比例：</td>
					<td>
						<s:select name="agentCommission" list="#{ '0.3': '30%', '0.35': '35%', '0.4': '40%', '0.45': '45%', '0.5': '50%' }" value="%{#request.userstatus.commission }"></s:select>
					</td>	
				</tr>
				<tr>
					<td>真人佣金比例：</td>
					<td>
						<s:select name="liverate" list="#{ '0.3': '30%', '0.35': '35%', '0.4': '40%', '0.45': '45%', '0.5': '50%' }" value="%{#request.userstatus.liverate }"></s:select>
					</td>	
				</tr>
				<tr>
					<td>体育佣金比例：</td>
					<td>
						<s:select name="sportsrate" list="#{ '0.3': '30%', '0.35': '35%', '0.4': '40%', '0.45': '45%', '0.5': '50%' }" value="%{#request.userstatus.sportsrate }"></s:select>
					</td>	
				</tr>
				<tr>
					<td>彩票佣金比例：</td>
					<td>
						<s:select name="lotteryrate" list="#{ '0.3': '30%', '0.35': '35%', '0.4': '40%', '0.45': '45%', '0.5': '50%' }" value="%{#request.userstatus.lotteryrate }"></s:select>
					</td>	
				</tr>

				</c:if>
				<tr>
					<td>
						备注:
					</td>
					<td>
					 <s:textarea cols="50" rows="10" name="remark" value="%{#request.user.remark}"></s:textarea>
					</td>
				</tr>
				<tr>
					<td>

					</td>
					<td>
						<s:submit value="更  新"></s:submit>
						<s:reset value="重 置"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
