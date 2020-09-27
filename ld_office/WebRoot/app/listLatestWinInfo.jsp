<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>中奖信息配置</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>手机App&nbsp;--&gt;&nbsp;中奖信息配置&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryLatestWinInfoList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>会员帐号：</td>
									<td>
										<s:textfield name="latestWinInfo.loginName" id="title"></s:textfield>
									</td>
									<td>游戏名称：</td>
									<td>
										<s:textfield name="latestWinInfo.gameTitle" id="title"></s:textfield>
									</td>
									<td>状态：</td>
									<td><s:select list="#{ '': '', '1': '启用', '0': '禁用' }" name="latestWinInfo.status" id="status" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select></td>
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
		<div style="position: absolute; top: 80px; left: 0px">
			<a href="/app/addLatestWinInfo.jsp" target="_blank" style="color: red; font-size: 14px; margin-left: 10px;">新增中奖信息</a>
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
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">会员账号</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">投注金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">中奖金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">游戏名称</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">游戏图标</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">论坛地址</td>									
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">状态</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 15px;">修改时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 15px;">创建时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">操作</td>
								</tr>
								<s:iterator var="vo" value="%{#request.page.pageContents}">
								<tr>
									<td align="center">
										<a href="/app/singleLatestWinInfo.do?latestWinInfo.id=<s:property value='#vo.id' />" target="_blank"><s:property value="#vo.loginName" /></a>
									</td>
									<td align="left">
										<s:property value="@dfh.utils.NumericUtil@formatDouble(#vo.betAmount)" />
									</td>
									<td align="left">
										<s:property value="@dfh.utils.NumericUtil@formatDouble(#vo.winAmount)" />
									</td>
									<td align="left">
										<s:property value="#vo.gameTitle" />
									</td>
									<td align="left">
										<s:property value="#vo.gameIcon" />
									</td>
									<td align="left">
										<s:property value="#vo.forumUrl" />
									</td>
									<td align="center">
										<s:if test="#vo.status==0"><font style="color:red;">禁用</font></s:if>
										<s:if test="#vo.status==1"><font style="color:green;">启用</font></s:if>
									</td>
									<td align="center">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#vo.modifyTime" />
									</td>
									<td align="center">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#vo.createTime" />
									</td>
									<td align="center">
										<s:if test='#vo.status==0'>
											<a href="javascript:void(0);" onclick="active(<s:property value="#vo.id"/>,1,'启用')">启用</a>
										</s:if>
										<s:if test='#vo.status==1'>
											<a href="javascript:void(0);" onclick="active(<s:property value="#vo.id"/>,0,'禁用')">禁用</a>
										</s:if>	
										<a href="javascript:void(0);" onclick="remove(<s:property value="#vo.id"/>)">删除</a>
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
			
			function active(id, status, message) {
				
				if (window.confirm('确认要' + message + '此记录吗？')) {
					
					var action = "/app/activeLatestWinInfo.do";
					
					var data = "latestWinInfo.id=" + id + "&latestWinInfo.status=" + status;
					
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
					
					var action = "/app/deleteLatestWinInfo.do";
					
					var data = "latestWinInfo.id=" + id;
					
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