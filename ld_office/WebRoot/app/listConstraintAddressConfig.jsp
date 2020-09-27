<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>约束地址配置</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
	<script type="text/javascript">
        function _downloadConstraintAddressConfig(){
            if(!confirm("确定下载？")){
                return ;
            }

            var data = $("form").serialize();
            var form = $("<form>");
            form.attr('style', 'display:none');
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', '/office/downloadConstraintAddressConfig.do?'+data);
            $('body').append(form);
            form.submit();
            form.remove();
        }
	</script>
		<p>手机App&nbsp;--&gt;&nbsp;约束地址配置&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
			<s:form action="queryConstraintAddressConfigList" namespace="/app" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>约束类型：</td>
									<td>
										<s:select list="#{ '': '', '1': '通过IP', '2': '拒绝IP', '3': '拒绝地区' }" name="constraintAddressConfig.typeId" id="searchTypeId" listKey="key" listValue="value" cssStyle="width: 100px;"></s:select>
									</td>
									<td>约束项值：</td>
									<td>
										<s:textfield cssStyle="width: 115px" name="constraintAddressConfig.value" size="20" />
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
		<div style="position: absolute; top: 88px; left: 0px;width:1100px;">
			<fieldset>
				<legend>新增约束记录</legend>
				<form id="constraintAddressConfigForm" name="constraintAddressConfigForm">
					<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
						<tr>
							<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr align="left">
										<td>约束类型：</td>
										<td>
											<select style="width: 100px;" name="constraintAddressConfig.typeId" id="typeId">
												<option value="1">通过IP</option>
												<option value="2">拒绝IP</option>
												<option value="3">拒绝地区</option>
											</select>
										</td>
										<td>约束项值：</td>
										<td>
											<input type="text" name="constraintAddressConfig.value" id="value" size="15" />
										</td>
										<td>备注：</td>
										<td><input type="text" name="constraintAddressConfig.remark" id="remark" size="15" /></td>
										<td>
											<input type="button" value="新 增" onclick="create()" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<input type="hidden" name="constraintAddressConfig.typeName" id="typeName" />
				</form>
				<c:choose>
					<c:when test="${sessionScope.operator.authority eq 'admin' || sessionScope.operator.authority eq  'market_manager' || sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'boss'}">
						<s:form action="importConstraintAddressConfig" namespace="/app" method ="post" enctype="multipart/form-data" style="margin-left:700px;top:-38px;position:relative">
							<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
								<tr>
									<td>
										文件:<input type="file" id="myFile" name="myFile"   size="4" style="width:200px"/>&nbsp;<input type="submit" value="批量导入" />
										&nbsp; <a href="#" onclick="_downloadConstraintAddressConfig()"><font color="blue">批量模板.xlsx </font></a>
									</td>
								</tr>
							</table>
						</s:form>
					</c:when>
				</c:choose>
			</fieldset>
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
							<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">约束类型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">约束项值</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">创建人</td>

									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">创建时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">备注</td>

									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 30px;">操作</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="left"><s:property value="#fc.typeName" /></td>
									<td align="left"><s:property value="#fc.value" /></td>
									<td align="center">
										<s:property value="#fc.createdUser" />
									</td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" /></td>
									<td align="center">
										<s:property value="#fc.remark" />
									</td>
									<td align="center">
										<a href="/app/queryConstraintAddressConfig.do?constraintAddressConfig.id=<s:property value='#fc.id' />" target="_blank">修改</a> 
										<a href="javascript:void(0);" onclick="remove(<s:property value="#fc.id"/>)">删除</a>
									</td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="11" align="right" bgcolor="66b5ff" align="center">${page.jsPageCode}</td>
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
	<script type="text/javascript" src="/app/common/function.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">
		
		/* var msg = "${requestScope.tipMessage}";
	
		if (msg != null && msg != "") {
	
			alert(msg);
		} */
		
		function gopage(val) {
			
			document.mainform.pageIndex.value = val;
			document.mainform.submit();
		};

        function remove(id) {
            if (window.confirm('确认要删除此记录吗？')) {

                var action = "/app/deleteConstraintAddressConfig.do";

                var data = "constraintAddressConfig.id=" + id;

                var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function (result) {

                    var text = result.responseText;

                    alert(text);

                    if (text.indexOf('成功') != -1) {

                        document.mainform.submit();
                    }
                }});
            }
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
			
			var typeId = $("#constraintAddressConfigForm").find("#typeId").val();
			var value = $("#constraintAddressConfigForm").find("#value").val();
			var typeName = $("#constraintAddressConfigForm").find("#typeId").find("option:selected").text();
			
			$("#constraintAddressConfigForm").find("#typeName").val(typeName);
			
			if (isNull(typeId)) {
				
				alert('约束类型不能为空！');
				return;
			}
			
			if (isNull(value)) {
				
				alert('约束项值不能为空！');
				return;
			}
			
			var data = $("#constraintAddressConfigForm").serialize();
			
			var action = "/app/addConstraintAddressConfig.do";
			
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
	</script>
</html>