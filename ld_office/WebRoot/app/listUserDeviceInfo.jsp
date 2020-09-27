<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户设备数据</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>记录&nbsp;--&gt;&nbsp;用户设备数据&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryUserDeviceInfoList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>设备ID：</td>
									<td>
										<s:textfield name="userSidName.sid" id="sid"></s:textfield>
									</td>
									<td>登陆用户名：</td>
									<td>
										<s:textfield name="userSidName.loginname" id="loginname"></s:textfield>
									</td>
									<td>操作系统：</td>
									<td>
										<s:textfield name="userSidName.os" id="content"></s:textfield>											
									</td>
									<td>操作系统版本：</td>
									<td>
										<s:textfield name="userSidName.osversion" id="osversion"></s:textfield>											
									</td>
									<td>机型：</td>
									<td>
										<s:textfield name="userSidName.mobilemodel" id="mobilemodel"></s:textfield>											
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
				<s:set name="by" value="'sid'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="pageIndex" />	
			</s:form>
		</div>
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
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">设备ID</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">登陆用户名</td>									
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">操作系统</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 8px;">操作系统版本</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 8px;">机型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 8px;">真机  / 模拟器</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center">
										<s:property value="#fc[0]" />
									</td>
									<td align="center">
										<s:property value="#fc[1]" />
									</td>
									<td align="center">
										<s:property value="#fc[2]" />
									</td>
									<td align="center">
										<s:property value="#fc[3]" />
									</td>
									<td align="center">
										<s:property value="#fc[4]" />
									</td>
									<td align="center">
										<s:property value="#fc[5]" />
									</td>
									<%-- <td align="center">
										<a href="javascript:void(0);" onclick="remove(<s:property value="#fc[0]"/>)">删除</a>
									</td> --%>
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
	    <script type="text/javascript">
	    
	    	var msg = "${requestScope.tipMessage}";

	    	if (msg != null && msg != "") {

	    		alert(msg);
	    	}
	    
	    	$("#checkAllBox").bind("click", function() {
				
				$("[name=item]:checkbox").attr("checked", $(this).is(':checked'));
			});
	    	
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
			
	    </script>
	</body>
</html>