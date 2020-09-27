<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>最新优惠详情评论记录</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>记录&nbsp;--&gt;&nbsp;最新优惠详情评论记录&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryPreferentialCommentList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>玩家账号：</td>
									<td>
										<s:textfield name="preferentialComment.loginName" id="loginName"></s:textfield>
									</td>
									<td>玩家昵称：</td>
									<td>
										<s:textfield name="preferentialComment.nickName" id="nickName"></s:textfield>
									</td>
									<td>评论内容：</td>
									<td>
										<s:textfield name="preferentialComment.content" id="content"></s:textfield>											
									</td>
									<td>活动标题：</td>
									<td>
										<s:textfield name="preferentialComment.activityTitle" id="activityTitle"></s:textfield>											
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
				<s:set name="by" value="'reply_time'" />
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
							<input type="button" value="删 除" id="delete" />
							<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 5px;"><input type="checkbox" id="checkAllBox" /></td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">活动标题</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">玩家账号</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">玩家昵称</td>									
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">评论内容</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 8px;">评论时间</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center">
										<input type="checkbox" name="item" value="<s:property value="#fc[0]"/>" />
									</td>
									<td align="center">
										<a href="/app/singlePreferentialComment.do?preferentialComment.id=<s:property value='#fc[0]' />" target="_blank"><s:property value="#fc[6]" /></a>
									</td>
									<td align="left">
										<s:property value="#fc[2]" />
									</td>
									<td align="left">
										<s:property value="#fc[3]" />
									</td>
									<td align="left">
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
	    	
	    	$("#delete").bind("click", function() {
				
				remove();	
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
			
			function remove() {
				
				if (window.confirm('确认要删除此记录吗？')) {
					
					var arr = [];
					
					$("input[name='item']:checked").each(function() {
					
						arr.push($(this).val());
					});
					
					if (arr.length == 0) {
					
						alert("未选中任何数据，请选择需要删除的数据！");
						return;
					}
					
					var ids = arr.join(',');
					
					var action = "/app/deletePreferentialComment.do";
					
					var data = "preferentialComment.ids=" + ids;
					
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