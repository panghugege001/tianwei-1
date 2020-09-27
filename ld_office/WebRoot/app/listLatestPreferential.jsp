<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>最新优惠配置</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>记录&nbsp;--&gt;&nbsp;最新优惠配置&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryLatestPreferentialList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>优惠类型：</td>
									<td>
										<s:select list="#{ '000': '全部优惠', '001': '专题优惠', '002': '长期优惠','003':'限时优惠','004':'论坛优惠' }" 
										 name="latestPreferential.type" id="type" listKey="key" 
										 listValue="value" cssStyle="width: 100px;"></s:select>
									</td>
									<td>活动标题：</td>
									<td><s:textfield name="latestPreferential.activityTitle" id="activityTitle"></s:textfield></td>
									<td>首页显示：</td>
									<td><s:select list="#{ '': '', '1': '是', '0': '否' }" name="latestPreferential.isNew" id="isNew" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select></td>
									<td>是否开启：</td>
									<td><s:select list="#{ '': '', '1': '启用', '0': '禁用' }" name="latestPreferential.isActive" id="isActive" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select></td>
									<td>是否手机端优惠：</td>
									<td><s:select list="#{ '': '', '1': '是', '0': '否' }" name="latestPreferential.isPhone" id="isPhone" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select></td>
									<td>每页记录：</td>
									<td><s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select></td>
									<td><s:submit value="查询"></s:submit></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="pageIndex" />		
			</s:form>
		</div>
		<div style="position: absolute; top: 80px; left: 0px">
			<a href="/app/addLatestPreferential.jsp" target="_blank" style="color: red; font-size: 14px; margin-left: 10px;">新增优惠配置</a>
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
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">优惠类型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">活动标题</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">活动简介</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">活动开始时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">活动结束时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">首页显示</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">是否开启</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">是否手机端优惠</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">优惠领取人数</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">跳转地址(专题优惠、论坛优惠)</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">是否千亿风采</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">创建时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">操作</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center">
										<a href="/app/singleLatestPreferential.do?latestPreferential.id=<s:property value='#fc.id' />" target="_blank"><s:property value="#fc.typeName" /></a>
									</td>
									<td align="left">
										<s:property value="#fc.activityTitle" />
									</td>
									<td align="left">
										<s:property value="#fc.activitySummary" />
									</td>
									<td align="center">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.activityStartTime" />
									</td>
									<td align="center">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.activityEndTime" />
									</td>
									<td align="center">
										<s:if test="#fc.isNew==0">否</s:if>
										<s:if test="#fc.isNew==1">是</s:if>
									</td>
									<td align="center">
										<s:if test="#fc.isActive==0"><font style="color:red;">禁用</font></s:if>
										<s:if test="#fc.isActive==1"><font style="color: green;">启用</font></s:if>
									</td>
									<td align="center">
										<s:if test="#fc.isPhone==0">否</s:if>
										<s:if test="#fc.isPhone==1">是</s:if>
									</td>
									<td align="center">
										<s:property value="#fc.receiveNumber" />
									</td>
									<td align="center">
										<s:property value="#fc.openUrl" />
									</td>
									
									<td align="center">
										<s:if test="#fc.isQyStyle==0">否</s:if>
										<s:if test="#fc.isQyStyle==1">是</s:if>
									</td>									
									<td align="center">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
									</td>
									<td align="center">
										<s:if test='#fc.isActive==0'>
											<a href="javascript:void(0);" onclick="active(<s:property value="#fc.id"/>,1,'启用')">启用</a>
										</s:if>
										<s:if test='#fc.isActive==1'>
											<a href="javascript:void(0);" onclick="active(<s:property value="#fc.id"/>,0,'禁用')">禁用</a>
										</s:if>	
										<a href="javascript:void(0);" onclick="remove(<s:property value="#fc.id"/>)">删除</a>
									</td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="11" align="right" bgcolor="66b5ff" align="center">
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
		
			function active(id, active, message) {
				
				if (window.confirm('确认要' + message + '此记录吗？')) {
					
					var action = "/app/activeLatestPreferential.do";
					
					var data = "latestPreferential.id=" + id + "&latestPreferential.isActive=" + active;
					
					var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function (result) {
						
						var text = result.responseText;
						
						alert(text);
						
						if (text.indexOf('成功') != -1) {
							
							document.mainform.submit();
						}
					}});	
				}
			};
			
			function remove(id) {
				
				if (window.confirm('确认要删除此记录吗？')) {
					
					var action = "/app/deleteLatestPreferential.do";
					
					var data = "latestPreferential.id=" + id;
					
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