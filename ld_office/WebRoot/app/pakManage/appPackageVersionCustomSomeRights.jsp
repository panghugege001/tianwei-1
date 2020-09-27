<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
	<div style="width: 100%; height: 60px;">
		<s:form action="queryAppPackageVersionCustomListSomeRights" namespace="/appPak"
			name="subform" id="subform" theme="simple" onsubmit="return subSubmitCheck();">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr align="left">
								<td>代理账号：</td>
								<td><s:textfield name="appPackageVersionCustom.agentAccount" id="agentAccount"></s:textfield></td>
								<td>代理码：</td>
								<td><s:textfield name="appPackageVersionCustom.agentCode" id="agentCode"></s:textfield></td>
								<td>打包状态：</td>
								<td>
								   	<s:select list="#{ '': '', '0': '未打包', '1': '打包中','2' : '打包完成','11':'打包失败' }" name="appPackageVersionCustom.pakStatus" id="pakStatus" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select>
								</td>
								<td>状态：</td>
								<td>
								   	<s:select list="#{ '': '', '1': '启用', '0': '禁用' }" name="appPackageVersionCustom.status" id="isActive" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select>
								</td>
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
			<s:hidden name="versionId" />
			<s:hidden name="plat" />
		</s:form>
	</div>
	<div style="width: 100%; height: 500px; overflow: auto;table-layout: fixed; word-break:break-all">
		<table width="1180px" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#99c8d7">
			<tr bgcolor="#0084ff">
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">代理账号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">代理码</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">版本号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">App平台</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">状态</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">强制升级</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">打包状态</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">包别名</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">包链接</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">二维码链接</td>
			</tr>
			<s:iterator var="vo" value="%{#request.pageSub.pageContents}">
				<tr >
					<td align="center"><s:property value="#vo.agentAccount" /></td>
					<td align="center"><s:property value="#vo.agentCode" /></td>
					<td align="center"><s:property value="#vo.versionCode" /></td>
					<td align="center">
					    <s:if test="#vo.plat=='android'">安卓</s:if> 
					    <s:if test="#vo.plat=='iOS'">iOS</s:if>
					</td>
					<td align="center">
					    <s:if test="#vo.status==0"><font style="color: red;">禁用</font></s:if> 
					    <s:if test="#vo.status==1"><font style="color: green;">启用</font></s:if>
					</td>
					<td align="center">
					    <s:if test="#vo.isForceUpgrade==0">否</s:if> 
					    <s:if test="#vo.isForceUpgrade==1">是</s:if>
					</td>
					<td align="center">
					    <s:if test="#vo.pakStatus==0">未打包</s:if> 
					    <s:if test="#vo.pakStatus==1">打包中</s:if>
					    <s:if test="#vo.pakStatus==2">已打包</s:if> 
					    <s:if test="#vo.pakStatus==11">打包失败</s:if>
					</td>
					<td align="center"><s:property value="#vo.packageName" /></td>
					<td align="center"><s:property value="#vo.packageUrl" /></td>
					<td align="center"><s:property value="#vo.qrCodeUrl" /></td>
				</tr>
			</s:iterator>
			<tr >
				<td colspan="10" align="right" bgcolor="66b5ff" align="center">
					${pageSub.jsPageCode}</td>
			</tr>
		</table>
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
			document.subform.pageIndex.value = val;
			document.subform.submit();
		};
	
		function orderby(by) {
			if (document.subform.order.value == "desc") {
				document.subform.order.value = "asc";
			} else {
	
				document.subform.order.value = "desc";
			}
			document.subform.by.value = by;
			document.subform.submit();
		};
		
		function subSubmitCheck(){
			if(isNull(document.subform.versionId.value)){
				alert("请先选择版本记录！");
				return false;
			}
			
			return true;
		}
		
		$(document).ready(function () {		
		    $("#checkBoxAll").bind("click", function () {
		    	if($(this).attr("checked") == "checked" || $(this).attr("checked") == true){
		        	$("[name = item]:checkbox").attr("checked", true);
		    	}else{
		    		$("[name = item]:checkbox").attr("checked", false);
		    	}
		    });
		    $("[name = item]:checkbox").bind("click", function () {
		    	if($(this).attr("checked") != "checked"){
		    		$("#checkBoxAll").attr("checked", false);
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
		    		$("#checkBoxAll").attr("checked", true);
		    	}
		    });

		});
		
		function isNull(v) {
			if (v == null || v == "" || v == "null" || v == undefined) {
				return true;
			}

			return false;
		};
	</script>
</body>
</html>