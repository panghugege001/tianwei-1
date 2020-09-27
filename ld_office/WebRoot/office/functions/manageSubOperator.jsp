<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>下级管理</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript">
	function deleteWorks(id) {
		if (window.confirm("确定吗?")) {
			var frm = document.getElementById("mainform");
			frm.action = "<c:url value='/office/deleteWords.do' />";
			frm.id.value = id;
			frm.submit();
		}
	}
	function auditing(id) {
		if (window.confirm("确定审核通过吗?")) {
			var frm = document.getElementById("mainform");
			frm.action = "<c:url value='/office/auditing.do' />";
			frm.id.value = id;
			frm.submit();
		}
	}
</script>
<script type="text/javascript">
	function gopage(val) {
		document.mainform.pageIndex.value = val;
		document.mainform.submit();
	}
</script>
	</head>
	<body>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<s:form action="queryWordsForBack" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div id="excel_menu_left">
				其它 --> 留言管理
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>
			<div id="excel_menu">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="1">
					<tr align="left">
						<td>
							开始时间:
							<s:textfield name="start" size="16"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{startTime}" />
						</td>
						<td>
							结束时间:
							<s:textfield name="end" size="16"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{endTime}" />
						</td>
						<td>
							关键词:
							<s:textfield name="keywords" size="50"></s:textfield>
						</td>
						<td>
							每页:
							<s:select list="%{#application.PageSizes}" name="size"></s:select>
						</td>
						<td>
							<s:submit value="查  询"></s:submit><a style="color:red" href='<s:url value="/office/functions/officeaddguestbook.jsp"></s:url>'>发 帖</a>
						</td>
					</tr>
				</table>
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:hidden name="id" id="guestid"></s:hidden>
				<s:hidden name="by" value="createtime"></s:hidden>
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
											留言标题
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											创建时间
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											状态
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											回复数
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											IP地址
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											留言用户
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											Email
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											联系电话
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											QQ
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											操作
										</td>
									</tr>
									<s:iterator var="guestbook"
										value="%{#request.page.pageContents}">
										
										<s:if test="#guestbook.referenceId!=null">
										<s:url namespace="/office" action="queryReference"
													var="urlqueryReference">
													<s:param name="id" value="#guestbook.referenceId"></s:param>
													<s:param name="start" value="%{#startTime}"></s:param>
													<s:param name="end" value="%{#endTime}"></s:param>
												</s:url>
										</s:if>
										<s:else>
										<s:url namespace="/office" action="queryReference"
													var="urlqueryReference">
													<s:param name="id" value="#guestbook.id"></s:param>
													<s:param name="start" value="%{#startTime}"></s:param>
													<s:param name="end" value="%{#endTime}"></s:param>
												</s:url>
										</s:else>
										

										<tr>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
													<s:a href="%{#urlqueryReference}"><s:property value="#guestbook.title" /></s:a> 
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:date name="#guestbook.createdate"
													format="yyyy-MM-dd HH:mm:ss" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:if test="#guestbook.flag==1">
													<span style="color: red;">未审核</span>
												</s:if>
												<s:else>
                                         已通过
              </s:else>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:if test="#guestbook.rcount!=null">
													<s:property value="#guestbook.rcount" />
												</s:if>
												<s:else>
              0
              </s:else>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#guestbook.ipaddress" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#guestbook.username" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#guestbook.email" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#guestbook.phone" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#guestbook.qq" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:a href="%{#urlqueryReference}">详细</s:a>
												<a href="javascript:deleteWorks(${requestScope.id});">删除</a>
												<s:if test="#guestbook.flag==1">
													<a href="javascript:auditing(${requestScope.id})">审核</a>
												</s:if>
											</td>
										</tr>
									</s:iterator>
									<tr>
										<td colspan="10" align="right" bgcolor="66b5ff" align="center"
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

