<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户信息列表</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>手机App&nbsp;--&gt;&nbsp;用户信息列表&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryUserStatusList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>玩家账号：</td>
									<td>
										<s:textfield name="userStatus.loginname" id="loginname"></s:textfield>
									</td>
									<td>状态：</td>
									<td><s:select list="#{ '': '', '1': '开启', '0': '禁言' }" name="userStatus.discussflag" id="discussflag" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select></td>
									<td width="70px" style="text-align:right">起始时间：</td>
		    						<td>
		    							<s:textfield size="20" name="startTime" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" cssClass="Wdate"></s:textfield>
									</td>
									<td width="70px" style="text-align:right">结束时间：</td>
		    						<td>
		    							<s:textfield size="20" name="endTime" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" cssClass="Wdate"></s:textfield>
									</td>
									<td>每页记录：</td>
									<td>
										<s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										<s:submit value="查询"></s:submit>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<s:set name="by" value="'loginname'" />
				<s:set name="order" value="'asc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="pageIndex" />	
			</s:form>
		</div>
		<br />
		<div style="position: absolute; top: 115px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">玩家账号</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">状态</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">操作</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="left">
										<s:property value="#fc.loginname" />
									</td>
									<td align="center">
										<s:if test="#fc.discussflag==0"><font style="color:red;">禁言</font></s:if>
										<s:if test="#fc.discussflag==1 || #fc.discussflag==null"><font style="color: green;">开启</font></s:if>
									</td>
									<td align="center">
										<s:if test='#fc.discussflag==0'>
											<a href="javascript:void(0);" onclick="active('<s:property value="#fc.loginname"/>',1,'开启')">开启</a>
										</s:if>
										<s:if test='#fc.discussflag==1 || #fc.discussflag==null'>
											<a href="javascript:void(0);" onclick="active('<s:property value="#fc.loginname"/>',0,'禁言')">禁言</a>
										</s:if>	
									</td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="10" align="right" bgcolor="66b5ff" align="center">
										${page.jsPageCode}
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="${ctx}/office/script.jsp" />
		<script type="text/javascript" src="${ctx}/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	    <script type="text/javascript">
	    	
			function gopage(val) {
				
				document.mainform.pageIndex.value = val;
				document.mainform.submit();
			};
	
			function orderby(by) {
				if (document.mainform.order.value == "desc") {
					
					document.mainform.order.value = "asc";
				} else {
					
					document.mainform.order.value = "desc";
				}
				
				document.mainform.by.value = by;
				document.mainform.submit();
			};
			
			function active(loginname, discussflag, message) {
				
				if (window.confirm('确认要' + message + '此记录吗？')) {
					
					var action = "/app/activeUserDiscuss.do";
					
					var data = "userStatus.loginname=" + loginname + "&userStatus.discussflag=" + discussflag;
					
					var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function (result) {
						
						var text = result.responseText;
						
						alert(text);
						
						if (text.indexOf('成功') != -1) {
							
							document.mainform.submit();
						}
					}});	
				}
			};
			
	    </script>
	</body>
</html>