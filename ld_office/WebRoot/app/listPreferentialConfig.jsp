<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>自助优惠配置</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>手机App&nbsp;--&gt;&nbsp;自助优惠配置&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
			<s:form action="queryPreferentialConfigList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>游戏平台：</td>
									<td>
										<s:select list="#{}" name="preferentialConfig.platformId" id="platformId" listKey="key" listValue="value" cssStyle="width: 130px;"></s:select>
									</td>
									<td>优惠类型：</td>
									<td>
										<s:select list="#{}" name="preferentialConfig.titleId" id="titleId" listKey="key" listValue="value" cssStyle="width: 150px;"></s:select>
									</td>
									<td>优惠标题：</td>
									<td>
										<s:textfield name="preferentialConfig.aliasTitle" id="aliasTitle"></s:textfield>
									</td>
									<td>是否开启：</td>
									<td>
										<s:select list="#{ '': '', '0': '关闭', '1': '开启' }" name="preferentialConfig.isUsed" id="isUsed" listKey="key" listValue="value" cssStyle="width: 70px;"></s:select>
									</td>
									<td>机器码验证是否开启：</td>
									<td>
										<s:select list="#{ '': '', '0': '否', '1': '是' }" name="preferentialConfig.machineCodeEnabled" id="machineCodeEnabled" listKey="key" listValue="value" cssStyle="width: 70px;"></s:select>	
									</td>
									<td>申请通道：</td>
									<td>
										<s:select list="#{}" name="preferentialConfig.isPhone" id="isPhone" listKey="key" listValue="value" cssStyle="width: 90px;"></s:select>
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
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="pageIndex" />
			</s:form>
		</div>
		<div style="position: absolute; top: 87px; left: 0px">
			<a href="/app/addPreferentialConfig.jsp" target="_blank" style="color: red; font-size: 14px; margin-left: 10px;">新增优惠配置</a>
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
							<input type="button" value="开 启" id="open" />&nbsp;
							<input type="button" value="关 闭" id="close" />&nbsp;
							<input type="button" value="删 除" id="delete" />&nbsp;
							<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;"><input type="checkbox" id="checkAllBox" /></td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">游戏平台</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">优惠类型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">优惠标题</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">存送百分比</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">存送流水倍数</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">最大额度</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">启用开始时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">启用结束时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">次数</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">等级</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">创建时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">申请通道</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">优惠互斥组别</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">是否开启</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center"><input type="checkbox" name="item" value="<s:property value="#fc.id"/>" /></td>
									<td align="center"><a href="/app/editPreferentialConfig.jsp?id=<s:property value='#fc.id' />" target="_blank"><s:property value="#fc.platformName" /></a></td>
									<td align="left"><s:property value="#fc.titleName" /></td>
									<td align="left"><s:property value="#fc.aliasTitle" /></td>
									<td align="left"><s:property value="#fc.percent" /></td>
									<td align="left"><s:property value="#fc.betMultiples" /></td>
									<td align="left"><s:property value="#fc.limitMoney" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTime" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTime" /></td>
									<td align="center" width="70px"><s:property value="#fc.times" />次/
										<s:if test="#fc.timesFlag==1">天</s:if>
										<s:elseif test="#fc.timesFlag==2">周</s:elseif>
										<s:elseif test="#fc.timesFlag==3">月</s:elseif>
										<s:elseif test="#fc.timesFlag==4">年</s:elseif>
									</td>
									<td align="left"><s:property value="@dfh.model.enums.VipLevel@getTextStr(#fc.vip)" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" /></td>
									<td align="center" width="70px">
										<c:forTokens var="str" items="${fc.isPhone}" delims="," varStatus="status">
											<c:if test="${str == 1}">官网</c:if>
											<c:if test="${str == 2}">WEB</c:if>
											<c:if test="${str == 3}">安卓APP</c:if>
											<c:if test="${str == 4}">苹果APP</c:if>
										</c:forTokens>
									</td>
									<td align="left"><s:property value="#fc.groupId" /></td>
									<td align="center" width="50px">
										<s:if test="#fc.isUsed==0"><font style="color: red;">关闭</font></s:if>
										<s:if test="#fc.isUsed==1"><font style="color: green;">开启</font></s:if>
									</td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="15" align="right" bgcolor="66b5ff">${page.jsPageCode}</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="${ctx}/office/script.jsp" />
		<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
		<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
		<script type="text/javascript" src="${ctx}/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
		
		var msg = "${requestScope.tipMessage}";

		if (!isNull(msg)) {
			
			alert(msg);
		}
		
		$(document).ready(function() {
			
			var platform = $("#platformId");
			platform.empty();
			platform.append("<option value=''></option>");
			
			for (var i in preferential_platform) {

				platform.append("<option value='" + preferential_platform[i].value + "'>" + preferential_platform[i].text + "</option>");
			}
			
			platform.val("${preferentialConfig.platformId}");
			
			var applyPassage = $("#isPhone");
			applyPassage.empty();
			applyPassage.append("<option value=''></option>");
			
			for (var n = 0, len = preferential_passage.length; n < len; n++) {
				
				applyPassage.append("<option value='" + preferential_passage[n].value + "'>" + preferential_passage[n].text + "</option>");	
			}
			
			applyPassage.val("${preferentialConfig.isPhone}");
			
			$("#checkAllBox").bind("click", function() {

				$("[name='item']:checkbox").attr("checked", $(this).is(':checked'));
			});
			
			$("#open").bind("click", function() {

				active(1, '开启');
			});
			
			$("#close").bind("click", function() {

				active(0, '关闭');
			});
			
			$("#delete").bind("click", function() {
				
				remove();
			});
						
			platform.change(platformChange);
						
			platformChange();
		});
		
		function platformChange() {
			
			var value = $("#platformId").val();
			
			var title = $("#titleId");
			title.empty();
			title.append("<option value=''></option>");
			
			if (!isNull(value)) {

				var type = preferential_platform[value].type;
				
				for (var j = 0, len = type.length; j < len; j++) {

					title.append("<option value='" + type[j].value + "'>" + type[j].text + "</option>");
				}
				
				title.val("${preferentialConfig.titleId}");
			}
		};
		
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
		
		function active(isUsed, message) {
			
			if (window.confirm('确认要' + message + '所选中的数据吗？')) {

				var arr = [];
				
				$("input[name='item']:checked").each(function() {
					
					arr.push($(this).val());
				});

				if (arr.length == 0) {

					alert("未选中任何数据，请选择需要" + message + "的数据！");
					return;
				}
					
				var ids = arr.join(',');

				var action = "/app/activePreferentialConfig.do";

				var data = "preferentialConfig.ids=" + ids + "&preferentialConfig.isUsed=" + isUsed;

				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {

					var text = result.responseText;

					alert(text);

					if (text.indexOf('成功') != -1) {

						document.mainform.submit();
					}
				}});
			}
		};
		
		function remove() {

			if (window.confirm('确认要删除所选中的数据吗？')) {

				var arr = [];

				$("input[name='item']:checked").each(function() {

					arr.push($(this).val());
				});

				if (arr.length == 0) {

					alert("未选中任何数据，请选择需要删除的数据！");
					return;
				}

				var ids = arr.join(',');

				var action = "/app/deletePreferentialConfig.do";

				var data = "preferentialConfig.ids=" + ids;

				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(),onComplete: function(result) {

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