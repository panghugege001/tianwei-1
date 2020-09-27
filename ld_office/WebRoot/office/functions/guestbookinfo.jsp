<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>站内信管理</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
		<style type="text/css">
.label_search_td_play {
	font-family: Tahoma;
	font-size: 15px;
	/*font-size: 11px;*/
	line-height: 28px;
	font-weight: bold;
	/* text-align: center;*/
	text-transform: capitalize;
	color: #FFFFFF;
	text-decoration: none;
	padding-right: 1px;
}

.input {
	font-family: Tahoma;
	font-size: 18px;
	/*font-size: 11px;*/
	font-weight: normal;
	/*text-transform: capitalize;*/
	text-decoration: none;
	background-color: #FFFFFF;
	border: 1px solid #336699;
	line-height: 16px;
	height: 22px;
	float: left;
	margin-top: 2px;
}
</style>
		<script type="text/javascript">
	function deleteWorks(id) {
		if (window.confirm("确定吗?")) {
			var frm = document.getElementById("mainform");
			frm.action = "<c:url value='/office/deleteWords.do' />";
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
		<div id="excel_menu_left" style="margin-top: 20px;">
			站内信管理-->站内信详细
			<s:date name="#request.start" format="yyyy-MM-dd HH:mm:ss"
				var="startDate" />
			<s:date name="#request.end" format="yyyy-MM-dd HH:mm:ss"
				var="stopDate" />
			<s:url action="queryWordsForBack" namespace="/office"
				var="queryWordsForBackUrl">
				<s:param name="start" value="%{#startDate}"></s:param>
				<s:param name="end" value="%{#stopDate}"></s:param>
			</s:url>
			<s:a href="%{queryWordsForBackUrl}">
				<font color="red">返回站内信列表</font>
			</s:a>
		</div>
		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<s:iterator value="%{#request.page.pageContents}" var="guestbook">
								<table width="100%" align="center" border="1" cellpadding="4"
									cellspacing="1" style="margin-bottom: 8px; margin-top: 8px;">
									<s:if test="#guestbook.referenceId==null">
										<tr>
											<td width="300px" style="text-align: right; height: 50px;">
												标题:
											</td>
											<td>
												<s:if test="#guestbook.referenceId==null">
													<s:property value="#guestbook.title" />
												</s:if>
												<s:else>
													<s:property value="#guestbook.title" />
												</s:else>
												<!-- <a href="javascript:deleteWorks(${guestbook.id});">&nbsp;删除&nbsp;</a> -->
												<s:if test="#guestbook.flag==@dfh.utils.Constants@FLAG_TRUE">
											 已审核
											</s:if>
												<s:else>
													<s:url action="auditingInfo" namespace="/office"
														var="auditingInfoUrl">
														<s:param name="id" value="#guestbook.id"></s:param>
														<s:param name="referenceId"
															value="%{#request.referenceId}"></s:param>
														<s:param name="start" value="%{#startDate}"></s:param>
														<s:param name="end" value="%{#stopDate}"></s:param>
													</s:url>
													<s:a href="%{#auditingInfoUrl}">审核</s:a>
												</s:else>
											</td>
										</tr>
									</s:if>
									<tr>
										<td width="300px" style="text-align: right; height: 50px;" valign="top">
											<s:property value="#guestbook.username" />
											<br />
											<br />
											<s:property value="#guestbook.ipaddress" />
											<br />
											<br />
											<s:date format="yyyy-MM-dd HH:mm:ss"
												name="#guestbook.createdate"></s:date>
											<br />
										</td>
										<td valign="top" style="word-break: break-all;">
											<s:property escape="false"
												value="@dfh.utils.StringUtil@transform(#guestbook.content)" />
										</td>
									</tr>
								</table>
							</s:iterator>
							<table border="0" width="100%">
								<tr>
									<td align="right">
										${page.jsPageCode}
									</td>
								</tr>
							</table>
							<s:form action="leaveReplyWords"
								onsubmit="document.getElementById('submit_img').disabled=true;"
								namespace="/office" id="mainform" name="mainform"
								theme="css_xhtml" validate="false">
								<div class="padding_left_10">
									<table width="100%" align="center" border="0" cellpadding="4"
										cellspacing="1" style="margin-bottom: 8px; margin-top: 8px;">
										<tr>
											<td width="300px" height="25px;" style="text-align: right;">
												发帖时间:
											</td>
											<td>
												<input name="createdate" style="width: 200px;" class="input"
													id="createdate"
													onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
													My97Mark="false" />
												如果为空将自动获取
											</td>
										</tr>
										<tr>
											<td height="25px;" style="text-align: right;" valign="top">
												<span style="color: red">*</span>内容:
											</td>
											<td>
												<textarea name="content" class="input" id="content"
													style="width: 467px; height: 150px;"></textarea>
											</td>
										</tr>
										<tr>
											<td height="25px;"></td>
											<td>
												<br />
												<s:hidden name="referenceId" value="%{#request.id}"></s:hidden>
												<s:hidden name="size" value="10"></s:hidden>
												<s:hidden name="pageIndex" value="1"></s:hidden>
												<s:hidden name="id" value="0"></s:hidden>
												<s:hidden name="start" value="%{#startDate}"></s:hidden>
												<s:hidden name="end" value="%{#stopDate}"></s:hidden>
												<s:submit align="left" id="submit_img" value="回 复" />
											</td>
										</tr>
									</table>
								</div>
							</s:form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>

