<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择需要定制打包的代理</title>
<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<div style="width:100%;height:100px;">
		<s:form action="queryAppPackageAgentList" namespace="/appPak"
			name="mainform" id="mainform" theme="simple">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr align="left">
								<td style="width:100px;">代理帐号(允许逗号、分号、空格、换行、空行分隔，允许空格)：</td>
								<td><s:textarea name="user.loginname" id="loginname" cols="30" rows="5"/></td>
								<td>代理ID：</td>
								<td><s:textfield name="user.id" id="id"></s:textfield>
								</td>
								<td>每页记录：</td>
								<td><s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select></td>
								<td><s:submit value="查询"></s:submit></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<s:set name="by" value="'loginname'" />
			<s:set name="order" value="'asc'" />
			<s:hidden name="order" value="%{order}" />
			<s:hidden name="by" value="%{by}" />
			<s:hidden name="pageIndex" />
			<s:hidden name="appPackageVersion.id" id="versionId"/>
			<s:hidden name="appPackageVersion.versionCode" id="versionCode"/>
			<s:hidden name="appPackageVersion.plat" id="plat"/>
		</s:form>
	</div>
	<div style="width:100%;height:30px;">
		<a href="javascript:void(0);" style="color: red; font-size: 14px; margin-left: 10px;" onclick="batchSelected(this)">选择确定</a>
	</div>
	<br />
	<div style="width: 100%; height: auto; overflow: auto;;table-layout: fixed; word-break:break-all">
		<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7" >
			<tr bgcolor="#0084ff">
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">
				    <input type="checkbox" id="checkBoxAll" />
				</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">代理账号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">代理ID</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">代理人</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">代理码</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">最近一次登录</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">警告级别</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">邮件</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 70px;">地址</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">QQ号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">微信号</td>
				<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">电话</td>
			</tr>
			<s:iterator var="vo" value="%{#request.page.pageContents}">
				<tr>
					<td><input type="checkbox" name="item" value="<s:property value="#vo.loginname"/>" /></td>
					<td align="left"><s:property value="#vo.loginname" /></td>
					<td align="left"><s:property value="#vo.id" /></td>
					<td align="left"><s:property value="#vo.accountName" /></td>
					<td align="left"><s:property value="#vo.agcode" /></td>
					<td align="center">
						<s:date format="yyyy-MM-dd HH:mm:ss" name="#vo.lastLoginTime" />
					</td>
					<td align="center"><s:property value="#vo.warnflag" /></td>
					<td align="left"><s:property value="#vo.email" /></td>
					<td align="left"><s:property value="#vo.address" /></td>
					<td align="left"><s:property value="#vo.qq" /></td>
					<td align="left"><s:property value="#vo.microchannel" /></td>
					<td align="left"><s:property value="#vo.phone" /></td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="12" align="right" bgcolor="66b5ff" align="center">
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
						
			$(document).ready(function () {
				$('#versionId').val("${appPackageVersion.id}");
				$('#versionCode').val("${appPackageVersion.versionCode}");
				$('#plat').val("${appPackageVersion.plat}");
				
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
			function batchSelected(btn){
				var result = new Array();
				var ids ;
				$("[name = item][checked]:checkbox").each(function(){
					result.push($(this).attr("value"));
				});
				var len = result.length ;
				if(len>0){
					btn.disabled=true;
		    		var ids = result.join(",") ;
		    		var versionId = $('#versionId').val();
		    		var versionCode = $('#versionCode').val(); 
		    		var plat = $('#plat').val(); 
		    		$.ajax({  
		    	        type : "post",  
		    	        url : "/appPak/addAppPackageVersionCustomList.do", 
		    	        data : 
			    	        {
					    	    "ids":ids,
					    	    "appPackageVersion.id":versionId,
					    		"appPackageVersion.versionCode":versionCode,
					    		"appPackageVersion.plat":plat
					    	},
		    	        async : false,  
		    	        success : function(data){  
						    btn.disabled=false;
							if (data.indexOf('成功') != -1) {
							    var _parentWin = window.opener;
								_parentWin.subform.submit();
								window.close();
							}
							else{
								alert(data);
							}
		    	        }  
		    	    }); 
				}else{
					alert("请选择需要执行的数据");
				}
			}
	
	    </script>
</body>
</html>