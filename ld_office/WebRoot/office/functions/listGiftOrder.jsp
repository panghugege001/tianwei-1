<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>活动订单记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>记录&nbsp;--&gt;&nbsp;活动订单记录&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryGiftOrderList" namespace="/office" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>活动类型: </td>
									<td><s:select list="#{ '0': '', '6': '礼品登记', '18': '限时礼品' }" name="id" listKey="key" listValue="value"></s:select></td>
									<td>玩家账号: </td>
									<td><s:textfield cssStyle="width: 115px" name="loginname" size="20" /></td>
									<td>每页记录: </td>
									<td><s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select></td>
									<td><s:submit value="查询"></s:submit></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<s:set name="by" value="'applyDate'" />
				<s:set name="order" value="'asc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="pageIndex" />		
			</s:form>
		</div>
		<div style="position: absolute; top: 105px; left: 0px">
			<c:if test="${sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'boss' || sessionScope.operator.username eq 'finance_manager' }">
				<fieldset>
					<legend>新增活动订单</legend>
					<form id="giftOrderForm" name="giftOrderForm">
						<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
							<tr>
								<td>
									<table border="0" cellpadding="0" cellspacing="0">
										<tr align="left">
											<td>活动类型: </td>
											<td>
												<select style="width: 80px;" name="id" id="id">
													<option value="6">礼品登记</option>
													<option value="18">限时礼品</option>
												</select>
											</td>
											<td>玩家账号:</td>
											<td><input type="text" name="loginname" id="loginname" /></td>
											<td>姓名:</td>
											<td><input type="text" name="addressee" id="addressee" /></td>
											<td>电话:</td>
											<td><input type="text" name="cellphoneNo" id="cellphoneNo" maxlength="11" /></td>
											<td>邮寄地址:</td>
											<td><input type="text" name="address" id="address" /></td>
											<td><input type="button" value="新增" onclick="create()" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>			
					</form>
				</fieldset>
			</c:if>
		</div>
		<br />
		<div style="position: absolute; top: 200px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1180px" border="1" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">活动类型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">玩家账号</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">姓名</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">电话</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 120px;">邮寄地址</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">申请时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">操作</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center">
										<s:if test="#fc.giftID==6">礼品登记</s:if>
										<s:if test="#fc.giftID==18">限时礼品</s:if>
									</td>
									<td align="left">
										<s:property value="#fc.loginname" />
									</td>
									<td align="left">
										<s:property value="#fc.realname" />
									</td>
									<td align="center">
										<s:property value="#fc.cellphoneNo" />
									</td>
									<td align="left">
										<s:property value="#fc.address" />
									</td>
									<td align="center">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.applyDate" />
									</td>
									<td align="center">
										<input type="button" value="删除" onclick="remove(<s:property value="#fc.id"/>)" />
									</td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="7" align="right" bgcolor="66b5ff" align="center">
										${page.jsPageCode}
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">
		
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
		
		function create() {
			
			if (isNull($("#id").val())) {
				
				alert('活动类型不能为空！');
				return;
			}
			
			if (isNull($("#loginname").val())) {
				
				alert('玩家账号不能为空！');
				return;
			}
			
			if (isNull($("#addressee").val())) {
				
				alert('姓名不能为空！');
				return;
			}
			
			if (isNull($("#cellphoneNo").val())) {
				
				alert('电话不能为空！');
				return;
			}
			
			if (isNull($("#address").val())) {
				
				alert('邮寄地址不能为空！');
				return;
			}
			
			var data = $("#giftOrderForm").serialize();
			
			var action = "/office/addGiftOrder.do";
			
			var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {
				
				var text = result.responseText;
				
				alert(text);
				
				if (text.indexOf('成功') != -1) {
					
					document.mainform.submit();
				}
			}});
		};
		
		function isNull(value) {
			
			if (null == value || "" === value || "" === $.trim(value)) {
				
				return true;
			}
			
			return false;
		};
		
		function remove(id) {
			
			if (window.confirm('确认要删除吗？')) {
				
				var action = '/office/deleteGiftOrder.do';
				
				var data = 'id=' + id ;
				
				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {
					
					var text = result.responseText;
					
					alert(text);
					
					if (text.indexOf('成功') != -1) {
						
						document.mainform.submit();
					}
				}});
			}	
		};
	</script>
</html>