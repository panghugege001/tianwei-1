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
		<s:form action="queryAppPackageVersionCustomList" namespace="/appPak"
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
	<div style="width: 100%; height: 30px;">
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="selectAgents(this)">增加定制</a>
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="batchPak(this)">批量打包</a>
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="batchDelete(this)">批量删除</a>
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="batchPakingReset(this)">打包中状态重置</a>
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="batchSetEnableForceUpgrade(this)">强制升级</a>
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="batchSetDisableForceUpgrade(this)">不强制升级</a>
	</div>
	<div style="width: 100%; height: 500px; overflow: auto;table-layout: fixed; word-break:break-all">
		<table width="1180px" border="0" cellpadding="0" cellspacing="0"
			bgcolor="#99c8d7">
			<tr bgcolor="#0084ff">
				<td align="center"
					style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">
					<input type="checkbox" id="checkBoxAll" />
				</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">代理账号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">代理码</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">版本号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">App平台</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">状态</td>
<!-- 				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">需要升级</td> -->
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">强制升级</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">打包状态</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">包别名</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">包链接</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">二维码链接</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 80px;">操作</td>
			</tr>
			<s:iterator var="vo" value="%{#request.pageSub.pageContents}">
				<tr >
					<td><input type="checkbox" name="item"
						value="<s:property value="#vo.id"/>" /></td>
					<td align="center"><a
						href="/appPak/singleAppPackageVersionCustom.do?appPackageVersionCustom.id=<s:property value='#vo.id' />"
						target="_blank"> <s:property value="#vo.agentAccount" /></a></td>
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
<!-- 					<td align="center"> -->
<%-- 					    <s:if test="#vo.isUpgrade==0">否</s:if>  --%>
<%-- 					    <s:if test="#vo.isUpgrade==1">是</s:if> --%>
<!-- 					</td> -->
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
					<td align="center">
					    <a href="javascript:void(0);" onclick="pakCustom(<s:property value="#vo.id"/>)">打包</a>
					    <s:if test='#vo.status==0'>
					        <a href="javascript:void(0);" onclick="activePakPublish(<s:property value="#vo.id"/>,1,'启用')">启用</a>
						</s:if> 
						<s:if test='#vo.status==1'>
						    <a href="javascript:void(0);"onclick="activePakPublish(<s:property value="#vo.id"/>,0,'禁用')">禁用</a>
						</s:if> 
					    <a href="javascript:void(0);" onclick="deletePakCustom(<s:property value="#vo.id"/>)">删除</a>
					</td>
				</tr>
			</s:iterator>
			<tr >
				<td colspan="12" align="right" bgcolor="66b5ff" align="center">
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
		
		function activePakPublish(id, status, message) {
			if (window.confirm('确认要' + message + '此记录吗？')) {
				var action = "/appPak/activeAppPackageVersionCustom.do";
				var data = "appPackageVersionCustom.id=" + id + "&appPackageVersionCustom.status=" + status;
				var xmlhttp = new Ajax.Request(action, {
					method : 'post',
					parameters : data + "&r=" + Math.random(),
					onComplete : function(result) {
						var text = result.responseText;
						if (text.indexOf('成功') != -1) {
							document.subform.submit();
						}
						else{
							alert(text);
						}
					}
				});
			}
		};
		
		function deletePakCustom(id) {
			if (window.confirm('确认要删除此定制包信息吗？ ')) {
				var action = "/appPak/deleteAppPackageVersionCustom.do";
				var data = "appPackageVersionCustom.id=" + id;
				var xmlhttp = new Ajax.Request(action, {
					method : 'post',
					parameters : data + "&r=" + Math.random(),
					onComplete : function(result) {
						var text = result.responseText;
						if (text.indexOf('成功') != -1) {
							document.subform.submit();
						}
						else{
							alert(text);
						}
					}
				});
			}
		};
		
		function pakCustom(id) {
			if (window.confirm('确认要执行定制打包吗？ ')) {
				var action = "/appPak/processAppPackageVersionCustom.do";
				var plat = document.subform.plat.value;
				var data = "appPackageVersionCustom.id=" + id+"&appPackageVersionCustom.plat="+plat;
				var xmlhttp = new Ajax.Request(action, {
					method : 'post',
					parameters : data + "&r=" + Math.random(),
					onComplete : function(result) {
						var text = result.responseText;
						if (text.indexOf('成功') != -1) {
							document.subform.submit();
						}
						else{
							alert(text);
						}
					}
				});
			}
		};
		
		function selectAgents(control){
			if(isNull(document.subform.versionId.value)){
				alert("请先选择版本记录！");
				return;
			}
			control.disabled=true;
			var url = "/appPak/selectPackageVersionAgent.do?versionId="+document.subform.versionId.value;
			window.open(url, '_blank');
			control.disabled=false;
		}
		
		function batchDeletePakCustomX(ids) { //停用
			if (window.confirm('确认要删除选中的定制包记录吗？ ')) {
				var action = "/appPak/deleteAppPackageVersionCustomList.do";
				var data = "appPackageVersionCustomVO.ids=" + ids;
				var xmlhttp = new Ajax.Request(action, {
					method : 'post',
					parameters : data + "&r=" + Math.random(),
					onComplete : function(result) {
						var text = result.responseText;
						if (text.indexOf('成功') != -1) {
							document.subform.submit();
						}
						else{
							alert(text);
						}
					}
				});
			}
		};
		
		function batchDelete(btn){
			if (window.confirm('确认要删除选中的定制包记录吗？ ')) {
				var result = new Array();
				var ids ;
				$("[name = item][checked]:checkbox").each(function(){
					result.push($(this).attr("value"));
				});
				var len = result.length ;
				if(len>0){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    	    $.ajax({  
		    	        type : "post",  
		    	        url : "/appPak/deleteAppPackageVersionCustomList.do", 
		    	        data : {"ids":ids},
		    	        async : false,  
		    	        success : function(data){  
			    			btn.disabled=false;
							if (data.indexOf('成功') != -1) {
				    			document.subform.submit();
							}
							else{
								alert(data);
							}
		    	        }  
		    	    });
				}else{
					alert("请选择需要执行的记录");
				}
			}
		};
		
		function batchPak(btn){
			if (window.confirm('确认要对选中记录执行打包吗？ ')) {
				var result = new Array();
				var ids ;
				$("[name = item][checked]:checkbox").each(function(){
					result.push($(this).attr("value"));
				});
				var plat = document.subform.plat.value;
				var len = result.length ;
				if(len>0 && len<1000){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    	    $.ajax({  
		    	        type : "post",  
		    	        url : "/appPak/processAppPackageVersionCustomList.do", 
		    	        data : {"ids":ids,"plat":plat},
		    	        async : false,  
		    	        success : function(data){  
			    			btn.disabled=false;
							if (data.indexOf('成功') != -1) {
								document.subform.submit();
							}
							else{
								alert(data);
							}
		    	        }  
		    	    });
				}else if(len>=1000){
					alert("暂不支持1000以上的批量打包，请减少批量选择！");
				}else{
					alert("请选择需要执行的记录");
				}
			}
		};
		
		function batchPakingReset(btn){
			if (window.confirm('确认要对选中的打包中状态的记录执行重置吗？ ')) {
				var result = new Array();
				var ids ;
				$("[name = item][checked]:checkbox").each(function(){
					result.push($(this).attr("value"));
				});
				var plat = document.subform.plat.value;
				var len = result.length ;
				if(len>0){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    	    $.ajax({  
		    	        type : "post",  
		    	        url : "/appPak/resetPakingVersionCustomList.do", 
		    	        data : {"ids":ids,"plat":plat},
		    	        async : false,  
		    	        success : function(data){  
			    			btn.disabled=false;
							if (data.indexOf('成功') != -1) {
								document.subform.submit();
							}
							else{
								alert(data);
							}
		    	        }  
		    	    });
				}else{
					alert("请选择需要执行的记录");
				}
			}
		};
		
		function batchSetEnableForceUpgrade(btn){
			if (window.confirm('确认要对选中记录启用强制升级吗？ ')) {
				var result = new Array();
				var ids ;
				$("[name = item][checked]:checkbox").each(function(){
					result.push($(this).attr("value"));
				});
				var plat = document.subform.plat.value;
				var len = result.length ;
				if(len>0){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    	    $.ajax({  
		    	        type : "post",  
		    	        url : "/appPak/enableForceUpgradeVersionCustomList.do", 
		    	        data : {"ids":ids,"plat":plat},
		    	        async : false,  
		    	        success : function(data){  
			    			btn.disabled=false;
							if (data.indexOf('成功') != -1) {
								document.subform.submit();
							}
							else{
								alert(data);
							}
		    	        }  
		    	    });
				}else{
					alert("请选择需要执行的记录");
				}
			}
		};
		
		function batchSetDisableForceUpgrade(btn){
			if (window.confirm('确认要对选中记录禁用强制升级吗？ ')) {
				var result = new Array();
				var ids ;
				$("[name = item][checked]:checkbox").each(function(){
					result.push($(this).attr("value"));
				});
				var plat = document.subform.plat.value;
				var len = result.length ;
				if(len>0){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    		$.ajax({  
		    	        type : "post",  
		    	        url : "/appPak/disableForceUpgradeVersionCustomList.do", 
		    	        data : {"ids":ids,"plat":plat},
		    	        async : false,  
		    	        success : function(data){  
			    			btn.disabled=false;
							if (data.indexOf('成功') != -1) {
								document.subform.submit();
							}
							else{
								alert(data);
							}
		    	        }  
		    	    });
		    		
		    		
				}else{
					alert("请选择需要执行的记录");
				}
			}
		};
		
		function isNull(v) {
			if (v == null || v == "" || v == "null" || v == undefined) {
				return true;
			}

			return false;
		};
	</script>
</body>
</html>