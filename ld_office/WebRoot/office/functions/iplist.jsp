<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>ip流量统计</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
				<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
	</head>
	<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}
</script>
	<body>
		<s:form action="queryIplist" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div id="excel_menu_left">
				ip统计信管理
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>
			<div id="excel_menu">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="1">
					<tr align="left">
						<td>
							玩家账号:
						</td>
						<td>
							<s:textfield name="username"></s:textfield>
						</td>
						<td>
							代理账号:
						</td>
						<td>
							<s:textfield name="agent"></s:textfield>
						</td>
						<td>
							访问ip:
						</td>
						<td>
							<s:textfield name="ip"></s:textfield>
						</td>
						<td>
							来源地址:
						</td>
						<td>
							<s:textfield name="client_address"></s:textfield>
						</td>
						<td>
							代理网址:
						</td>
						<td>
							<s:textfield name="agent_website"></s:textfield>
						</td>
						<td>
							开始时间:
						</td>
						<td>
							<s:textfield name="start" size="16"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{startTime}" />
						</td>
						<td>
							结束时间:
						</td>
						<td>
							<s:textfield name="end" size="16"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{endTime}" />
						</td>
						<td colspan="2">
							<s:submit value="查  询"></s:submit>
						</td>
					</tr>
				</table>
				<s:hidden name="pageIndex" value="1"></s:hidden>
			</div>
			<br />
			<br />
			<br />
			<br />
			<div id="middle">
				<div id="right">
					<div id="right_01">
						<div id="right_001">
							<div id="right_02">
								<div id="right_03"></div>
							</div>
							<div id="right_04">
								<table width="98%" border="0" align="center" cellpadding="0"
									cellspacing="1" bgcolor="#99c8d7">
									<tr>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											玩家账号
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											代理账号
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											代理网址
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											访问ip
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											访问地址
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											来源地址
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											请求地址
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											时间
										</td>
									</tr>
									<s:iterator var="s" value="%{#request.page.pageContents}">
										<tr>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.loginname" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.agent" />
											</td>

											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="${s.agent_website}" target="_blank"><s:property value="#s.agent_website" /></a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="http://ip.chinaz.com/?IP=<s:property value='#s.client_ip' />&action=2" target="_blank" style="text-decoration: none;"><s:property value='#s.client_ip' /></a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.client_address" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
											    <s:if test="#s.source_come_url.contains(\"直接输入\")">
													<s:property value="#s.source_come_url" />
										        </s:if>
										        <s:else>
										            <a href="<s:property value="#s.source_come_url" />" target="_blank"><s:property value="#s.source_come_url" /></a>
										        </s:else>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="<s:property value="#s.source_go_url" />" target="_blank"><s:property value="#s.source_go_url" /></a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.createtime" />
											</td>
										</tr>
									</s:iterator>
									<tr>
										<td colspan="11" align="right" bgcolor="66b5ff" align="center"
											style="font-size: 13px;">
											${page.jsPageCode}
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>

