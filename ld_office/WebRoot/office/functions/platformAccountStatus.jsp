<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>游戏平台账号冻结/解冻</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	 <body>
	 	<s:hidden value="%{#session.operator.authority}" id="authorityText"></s:hidden>
		<p>账户&nbsp;--&gt;&nbsp;游戏平台账号冻结/解冻&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
	 <s:form action="queryPlatformAccountStatus" namespace="/office" name="mainform" id="mainform" theme="simple">
		<div>
		 	<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
		 		<tr>
		 			<td>游戏平台：
		 				<s:select style="height:21px; width: 173px;" id="platform" list="#{'sba':'sba'}" emptyOption="true" />
		 			</td>
		 			<td>玩家账号：
						 <s:textfield id="addLoginname"/>
		 			</td>
		 			<td>
						 <input id="addBtn" type="button" value="添加并禁用" onclick="addData(this);" />
		 			</td>
		 		</tr>
		 	</table>
		</div>
		</br>
		<div style="left: 0px">
		
			<table>
				<tr align="left">
					<td>账号:<s:textfield id="loginname" name="loginname"  /></td>
					<td>状态:
						<s:select style="height:21px; width: 173px;" id="status" name="status" list="#{'0':'已冻结','1':'已解冻'}" emptyOption="true" />
					</td>
					<td>操作时间（起）:
						<s:textfield name="startTime" id="operateStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{operateStartTime}" cssClass="Wdate" />
					</td>
					<td>操作时间（止）:
						<s:textfield name="endTime" id="operateEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{operateEndTime}" cssClass="Wdate" />
					</td>
				</tr>
				<tr align="center">
					<td colspan="15">
						<s:submit value="查询"></s:submit>
						<input id="resetBtn" type="button" value="重置" onclick="onReset();" />
					</td>	
				</tr>
			</table>
			<table width="1580px" border="1" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
				<tr bgcolor="#0084ff">
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">账号</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">游戏平台</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">状态</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">操作人</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">操作时间</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">操作</td>
				</tr>
				<s:iterator var="fc" value="%{#request.page.pageContents}">
				<tr>
					<td align="center">
						<s:property value="#fc[1]" />
					</td>
					<td align="center">
						<s:property value="#fc[2]" />
					</td>
					<td align="center">
						<s:if test="#fc[3]=='0'">已冻结</s:if>
						<s:if test="#fc[3]=='1'">已解冻</s:if>
					</td>
					<td align="center">
						<s:property value="#fc[4]" />
					</td>
					<td align="center">
						<s:property value="#fc[5]" />
					</td>
					<td align="center">
						<s:if test="#fc[3]=='0'">
							<input id="addBtn" type="button" value="解冻" onclick="operateAcc('<s:property value="#fc[0]" />','<s:property value="#fc[1]" />','<s:property value="#fc[2]" />','UnLock');" />
						</s:if>
						<s:if test="#fc[3]=='1'">
							<input id="addBtn" type="button" value="冻结" onclick="operateAcc('<s:property value="#fc[0]" />','<s:property value="#fc[1]" />','<s:property value="#fc[2]" />','Lock');" />
						</s:if>
						 
					</td>
				</tr>
				</s:iterator>
				<tr>
					<td colspan="15" align="right" bgcolor="66b5ff" align="center">
						${page.jsPageCode}
					</td>
				</tr>
			</table>
		</s:form>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">
		
		$(function(){
			var authorityText =$('#authorityText').val();
			if(authorityText=='boss'||authorityText=='admin' || authorityText=='sale_manager'||authorityText=='finance_leader'
				||authorityText=='finance_manager' ||authorityText=='market_manager'){
			}else{
				alert('您没有权限查看此页面');
				history.back();
			}
		}); 
	
		function queryArbitrageInfo(val){
			document.mainform.loginname.value = val
			document.mainform.submit();
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
		function addData(th)
		{
		    var self = $(th);
			self.attr("disabled", true);
			var loginname = $("#addLoginname").val();
			var platform = $("#platform").val();
			if(platform==null || platform == ""){
				alert("请选择游戏平台！");
				return;
			}
			
			if(loginname == null || loginname == ''){
				self.attr("disabled", false);
				alert("请输入账号！");
				return;
			}
			
			$.ajax({
				url: "${ctx}/office/addPlatformAccountStatus.do",
				type: "post",
				async : true,
				data:"loginname="+loginname+"&platform="+platform,
				timeout: 0,
				success: function (data) {
					document.mainform.loginname.value=loginname;
					document.mainform.submit();
					alert(data);
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
				}
			});
		}
		
		function operateAcc(id,loginname,platform,type)
		{
			$.ajax({
				url: "${ctx}/office/operateAcc.do",
				type: "post",
				async : true,
				timeout: 0,
				data:"id="+id+"&loginname="+loginname+"&platform="+platform+"&type="+type,
				success: function (data) {
					document.mainform.submit();
					alert(data);
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
				}
			});
		}
		
		function onReset(){
			$("#loginname").val("");
			$("#status").val("");
			$("#operateStartTime").val("");
			$("#operateEndTime").val("");
		}
		
	</script>
</html>