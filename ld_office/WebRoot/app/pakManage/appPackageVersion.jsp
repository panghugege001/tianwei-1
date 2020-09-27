<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>App定制打包管理</title>
<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
<style type="text/css"> 
	.trSelected{ 
		background-color: #51b2f6; 
		height: 18px; 
		text-align: center; 
		font-size: 12px; 
	} 
</style>
</head>
<body>
    <div style="width:100%;height:40px;">
		<p>
			手机App&nbsp;--&gt;&nbsp;App定制打包管理&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</p>
	</div>
	<div style="width:100%;height:60px;">
		<s:form action="queryAppPackageVersionList" namespace="/appPak"
			name="mainform" id="mainform" theme="simple" onsubmit="querySubmit()">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr align="left">
								<td>版本号：</td>
								<td><s:textfield name="appPackageVersion.versionCode" id="versionCode"></s:textfield></td>
								<td>App平台类型：</td>
								<td>
								    <s:select list="#{ '': '', 'android': '安卓', 'iOS': 'iOS' }" name="appPackageVersion.plat" id="plat" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select>
								</td>
								<td>状态：</td>
								<td>
								   	<s:select list="#{ '': '', '1': '启用', '0': '禁用' }" name="appPackageVersion.status" id="isActive" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select>
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
		</s:form>
	</div>
	<div style="width:100%;height:30px;">
		<a href="/app/pakManage/addAppPackageVersion.jsp" target="_blank" style="color: red; font-size: 14px; margin-left: 10px;">新增版本</a>
	</div>
	<div style="width: 100%; height: 300px; overflow: auto;">
		<table width="1180px" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#99c8d7">
			<tr bgcolor="#0084ff">
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">版本号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">版本标题</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">App平台</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">状态</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 15px;">修改时间</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 15px;">创建时间</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 80px;">操作</td>
			</tr>
			<s:iterator var="vo" value="%{#request.page.pageContents}">
				<tr class='clickable-row' data-id="<s:property value='#vo.id'/>" data-plat="<s:property value='#vo.plat'/>" onclick="selectRow(this);">
					<td align="center"><a
						href="/appPak/singleAppPackageVersion.do?appPackageVersion.id=<s:property value='#vo.id' />"
						target="_blank"><s:property value="#vo.versionCode" /></a></td>
					<td align="left"><s:property value="#vo.versionTitle" /></td>
					<td align="center">
					    <s:if test="#vo.plat=='android'">安卓</s:if> 
					    <s:if test="#vo.plat=='iOS'">iOS</s:if>
					</td>
					<td align="center">
					    <s:if test="#vo.status==0"><font style="color: red;">禁用</font></s:if> 
					    <s:if test="#vo.status==1"><font style="color: green;">启用</font></s:if>
					</td>
					<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#vo.modifyTime" /></td>
					<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#vo.createTime" /></td>
					<td align="center">
					    <a href="javascript:void(0);" onclick="addCopyVersion(this,<s:property value="#vo.id"/>)">复制新增</a>
					    <s:if test='#vo.status==0'>
							<a href="javascript:void(0);" onclick="activeVersion(<s:property value="#vo.id"/>,1,'启用') ">启用</a>
						</s:if> 
						<s:if test='#vo.status==1'>
							<a href="javascript:void(0);" onclick="activeVersion(<s:property value="#vo.id"/>,0,'禁用')">禁用</a>
						</s:if> 
						<a href="javascript:void(0);" onclick="deleteVersion(<s:property value="#vo.id"/>)">删除</a>
				    </td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="10" align="right" bgcolor="66b5ff" align="center">
					${page.jsPageCode}</td>
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
		
	    var curRow; //全局行号  
	    var curRowId; //选中行的记录信息的ID  
	    var curColor;  
	    function selectRow(tr){  
	        if(curRow){  
	            curRow.bgColor = curColor;  
	            curColor = tr.bgColor;  
	            tr.bgColor = "#FFE9B3";  
	        }else{  
	            curColor = tr.bgColor;  
	            tr.bgColor = "FFE9B3";  
	        }  
	        curRow = tr;  
	        curRowId = tr.id;   
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
		
		$(document).ready(function($) {
		    $(".clickable-row").click(function() {
		    	window.parent.frames[1].document.subform.versionId.value = $(this).data("id");
		    	window.parent.frames[1].document.subform.plat.value = $(this).data("plat");
		    	window.parent.frames[1].document.subform.submit();
		    });
		});
		
		function querySubmit(){
			window.parent.frames[1].location.href="<c:url value='/app/pakManage/appPackageVersionCustom.jsp' />"; //window.parent.frames[1].document.getElementById('bottomFrame').contentWindow.location.href="...";
			window.parent.frames[1].location.href.reload();
		}
		
		function activeVersion(id, status, message){
			if (window.confirm('确认要' + message + '此记录吗？')){
				var action = "/appPak/activeAppPackageVersion.do";
				var data = "appPackageVersion.id=" + id + "&appPackageVersion.status=" + status;
				var xmlhttp = new Ajax.Request(action,{
					method : 'post',
					parameters : data + "&r=" + Math.random(),
					onComplete : function(result) {
						var text = result.responseText;
						if (text.indexOf('成功') != -1) {
							document.mainform.submit();
						}
						else{
							alert(text);
						}
					}
				});
			}
		};
	
		function deleteVersion(id) {
			if (window.confirm('删除此版本，将删除此版本对应的所有定制包信息，确认要删除此版本吗？ ')) {
				var action = "/appPak/deleteAppPackageVersion.do";
				var data = "appPackageVersion.id=" + id;
				var xmlhttp = new Ajax.Request(action, {
					method : 'post',
					parameters : data + "&r=" + Math.random(),
					onComplete : function(result) {
						var text = result.responseText;
						if (text.indexOf('成功') != -1) {
							document.mainform.submit();
						}
						else{
							alert(text);
						}
					}
				});
			}
		};
		
		function addCopyVersion(control,id) {
			control.disabled=true;
			var url = "/appPak/singleCopyAppPackageVersion.do?appPackageVersion.id="+id;
			window.open(url, '_blank');
			control.disabled=false;
		};
		
	</script>
</body>
</html>