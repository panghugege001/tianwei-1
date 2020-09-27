<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>站内信管理</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
			
<script type="text/javascript" src="/js/jquery-1.4.4.min.js"></script>			
<script type="text/javascript">
		$(function () {
		    $("#checkAllBox").bind("click", function () {
		    	if($(this).attr("checked") == "checked" || $(this).attr("checked") == true){
		        	$("[name = item]:checkbox").attr("checked", true);
		    	}else{
		    		$("[name = item]:checkbox").attr("checked", false);
		    	}
		    });
		    $("[name = item]:checkbox").bind("click", function () {
		    	if($(this).attr("checked") != "checked"){
		    		$("#checkAllBox").attr("checked", false);
		    	}
		    	var flag = true ;
		    	$("[name = item]:checkbox").each(function(){
		    		if($(this).attr("checked") == undefined){
		    			flag = false ;
		    		}else{
		    			flag = flag && $(this).attr("checked");
		    		}
		    	});
		    	if(flag){
		    		$("#checkAllBox").attr("checked", true);
		    	}
		    });
		});
		
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
		<s:form action="queryTopicUserList" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div id="excel_menu_left">
				其它 --> 查看用户信息	
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>
			<div id="excel_menu">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="1">
					<tr align="left">
						<td>
							收信人:
						</td>
						<td>
							<s:textfield name="adminname"></s:textfield>
						</td>
						<td>
							每页:
						</td>
						<td>
							<s:select list="%{#application.PageSizes}" name="size"></s:select>
						</td>
						<td colspan="2">
							<s:submit value="查  询"></s:submit>
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
									   		style="font-size:13px;;color: #FFFFFF;font-weight: bold"><input type="checkbox" id="checkAllBox" /></td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											标题
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											发信人
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											收信人
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											阅读状态
										</td>
									</tr>
									<s:iterator var="topicInfo"
										value="%{#request.page.pageContents}">
										<tr>
											<td align="center">
												<input type="checkbox" name="item" value="<s:property value="#topicInfo[0]"/>" />
            								</td> 
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
													<s:property value="#topicInfo[4]" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
													客服管理员
											</td>
											 <td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
													<s:property value="#topicInfo[2]" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<c:if test="${topicInfo[3] eq 0}">(<font
														color="red"><s:property value="#topicInfo[2]" /></font>)未读</c:if>
												<c:if test="${topicInfo[3] eq 1}">(<font
														color="red"><s:property value="#topicInfo[2]" /></font>)已读</c:if>
											</td>
										</tr>
									</s:iterator>
									<tr>
										<td colspan="12" align="right" bgcolor="66b5ff" align="center"
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

