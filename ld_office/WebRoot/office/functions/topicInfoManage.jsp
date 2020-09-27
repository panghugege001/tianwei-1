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
		
		function batchDelete(btn){
			var result = new Array();
			var ids ;
			$("[name = item][checked]:checkbox").each(function(){
				result.push($(this).attr("value"));
			});
			var len = result.length ;
			if(len>0){
				if(confirm("共选中"+len+"条数据，确认删除？删除后将不能恢复！")){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    		$.ajax({ 
				          type: "post", 
				          url: "/office/batchDeleteTopicInfo.do", 
				          cache: false,  
				          async: false,
				          data:{"ids":ids},
				          success : function(data){
				        	 btn.disabled=true;
				          	 alert("操作成功");
				          	 var frm=document.getElementById("mainform");
							 frm.submit();
				          },
				          error: function(){alert("系统错误");},
				    });
				}
			}else{
				alert("请选择需要删除的数据");
			}
		}
		function batchPushToApp(btn){
			var result = new Array();
			var ids ;
			$("[name = item][checked]:checkbox").each(function(){
				result.push($(this).attr("value"));
			});
			var len = result.length ;
			if(len>0){
				if(confirm("共选中"+len+"条数据，确认推送到手机客户端？")){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    		$.ajax({ 
				          type: "post", 
				          url: "/office/batchPushTopicToApp.do", 
				          cache: false,  
				          async: false,
				          data:{"ids":ids},
				          success : function(data){
				        	 btn.disabled=true;
				          	 alert(data);
				          	 var frm=document.getElementById("mainform");
							 frm.submit();
				          },
				          error: function(){alert("系统错误");},
				    });
				}
			}else{
				alert("请选择需要删除的数据");
			}
		}
		
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
				frm.action = "<c:url value='/office/auditingTopicInfo.do' />";
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
		<s:form action="queryTopicInfoList" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div id="excel_menu_left">
				其它 --> 站内信管理	
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>
			<div id="excel_menu">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="1">
					<tr align="left">
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
						<td>
							标题:
						</td>
						<td>
							<s:textfield name="title"></s:textfield>
						</td>
						<td>
							收发件人:
						</td>
						<td>
							<s:textfield name="adminname"></s:textfield>
						</td>
						<!-- 
						<td>
							发件人:
						</td>
						<td>
							<s:textfield name="username"></s:textfield>
						</td> -->
						<td colspan="2">
							<s:submit value="查  询"></s:submit>
							<a style="color: red"
								href='<s:url value="/office/functions/officeAddTopicInfoNew.jsp"></s:url>'>发
								帖</a>
						</td>
					</tr>
					<tr align="left">
					
					    <td>
							审核状态:
						</td>
						<td>
							<s:select cssStyle="width:80px"
								list="#{'':'','0':'已审核','1':'未审核'}" name="flag" listKey="key"
								listValue="value" emptyOption="false"></s:select>
						</td>
					
						<td>
							阅读状态:
						</td>
						<td>
							<s:select cssStyle="width:80px" list="#{'':'','0':'未读','1':'已读'}"
								name="isRead" listKey="key" listValue="value"
								emptyOption="false"></s:select>
						</td>
						<td>
							收发状态:
						</td>
						<td>
							<s:select cssStyle="width:80px"
								list="#{'':'','0':'已发信息','1':'接收信息'}" name="isadmin"
								listKey="key" listValue="value" emptyOption="false"></s:select>
						</td>
						<td>
							每页:
						</td>
						<td>
							<s:select list="%{#application.PageSizes}" name="size"></s:select>
						</td>
						<td colspan="2">
							<c:if test="${sessionScope.operator.authority eq  'sale_manager' || sessionScope.operator.authority eq 'boss'||sessionScope.operator.username eq 'sarah'}">
								<input type="button" value="批量删除" onclick="batchDelete(this);" />
							</c:if>
						</td>
						<td colspan="2">
<%-- 							<c:if test="${sessionScope.operator.authority eq  'sale_manager' || sessionScope.operator.authority eq 'boss'||sessionScope.operator.username eq 'sarah'}"> --%>
<!-- 								<input type="button" value="批量推送到手机" onclick="batchPushToApp(this);" /> -->
<%-- 							</c:if> --%>
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
<!-- 										<td bgcolor="#0084ff" align="center" -->
<!-- 											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"> -->
<!-- 											收信人 -->
<!-- 										</td> -->
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											审核状态
										</td>
<!-- 										<td bgcolor="#0084ff" align="center" -->
<!-- 											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"> -->
<!-- 											阅读状态 -->
<!-- 										</td> -->
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											收发状态
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											回复状态
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
											创建时间
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											操作
										</td>
									</tr>
									<s:iterator var="topicInfo"
										value="%{#request.page.pageContents}">

<%-- 										<s:if test="#guestbook.referenceId!=null"> --%>
<%-- 											<s:url namespace="/office" action="queryReference" --%>
<%-- 												var="urlqueryReference"> --%>
<%-- 												<s:param name="start" value="%{#startTime}"></s:param> --%>
<%-- 												<s:param name="end" value="%{#endTime}"></s:param> --%>
<%-- 											</s:url> --%>
<%-- 										</s:if> --%>
<%-- 										<s:else> --%>
											<s:url namespace="/office" action="queryTopicReference"
												var="urlqueryReference">
												<s:param name="id" value="#topicInfo[0]"></s:param>
												<s:param name="start" value="%{#startTime}"></s:param>
												<s:param name="end" value="%{#endTime}"></s:param>
											</s:url>
											<s:url namespace="/office" action="queryTopicUserList"
												var="urlqueryTopicUserList">
												<s:param name="id" value="#topicInfo[0]"></s:param>
												<s:param name="start" value="%{#startTime}"></s:param>
												<s:param name="end" value="%{#endTime}"></s:param>
											</s:url>
<%-- 										</s:else> --%>
										<tr>
											<td align="center">
												<input type="checkbox" name="item" value="<s:property value="#topicInfo[0]"/>" />
            								</td> 
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:a href="%{#urlqueryReference}">
													<s:property value="#topicInfo[1]" />
												</s:a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
													<s:property value="#topicInfo[4]" />
											</td>
											<%-- <td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<c:if test="${topicInfo[8] eq 1}">
													客服管理员
												</c:if>
												<c:if test="${topicInfo[8] eq 0}">
													<s:property value="#topicInfo[11]" />
												</c:if>
											</td> --%>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:if test="#topicInfo[6]==1">
													<span style="color: red;">未审核</span>
												</s:if>
												<s:else>已通过</s:else>
											</td>
<!-- 											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;"> -->
<%-- 												<c:if test="${topicInfo[11] eq 0}">(<font --%>
<%-- 														color="red">客服管理员</font>)未读</c:if> --%>
<%-- 												<c:if test="${topicInfo[11] eq 1}">(<font --%>
<%-- 														color="red">客服管理员</font>)已读</c:if> --%>
<!-- 											</td> -->
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<c:if test="${topicInfo[8] eq 1}">
													接收信息
												</c:if>
												<c:if test="${topicInfo[8] eq 0}">
													已发信息
												</c:if>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<c:if
 													test="${topicInfo[11] eq 1}"> 
													(客服管理员)没有新的信息
												</c:if>
 												<c:if 
													test="${topicInfo[11] eq 0}"> 
												(<font color="red">
												客服管理员
												</font>)有新的信息
												</c:if> 
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:if test="#topicInfo[7]!=null">
													<s:property value="#topicInfo[7]" />
												</s:if>
												<s:else>0</s:else>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#topicInfo[5]" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:date name="#topicInfo[3]"
													format="yyyy-MM-dd HH:mm:ss" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
											    <s:if test="#topicInfo[8]==0">
											    	<s:a href="%{#urlqueryTopicUserList}">查看用户</s:a>
											    </s:if>
												<s:a href="%{#urlqueryReference}">详细</s:a>
												<!-- <a href="javascript:deleteWorks(${requestScope.id});">删除</a>  -->
												<s:if test="#topicInfo[6]==1">
													<a href="javascript:auditing(${topicInfo[0]})">审核</a>
												</s:if>
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

