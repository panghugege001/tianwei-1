<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>编辑礼品活动订单收货信息</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="excel_menu">
			<s:form name="mainform" id="mainform" theme="simple">
				<table align="left">
					<tr>
						<td>姓名:</td>
						<td>
							<s:textfield name="addressee" size="30" />
						</td>
					</tr>
					<tr>
						<td>地址:</td>
						<td>
							<s:textfield name="address" size="32" />
						</td>
					</tr>
					<tr>
						<td>手机号码:</td>
						<td>
							<s:textfield name="cellphoneNo" maxlength="11" />
						</td>
					</tr>
					<tr>
						<td align="center">
							<input type="button" value="提交" onclick="updateGiftApplyInfo()" />
						</td>
					</tr>
				</table>
				<s:hidden name="id" />
			</s:form>
		</div>
	</body>
	<c:import url="/office/script.jsp" />
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">
		
		// 获取查询串
		var query = location.search.substring(1);
    	var pairs = query.split("&");
    	
 		for (var i = 0, len = pairs.length; i < len; i++) {   
 			// 查找name=value
 			var pos = pairs[i].indexOf('=');   
 			
 			// 如果没有找到就跳过
 			if (pos == -1) {
 				
 				continue;
 			}
 			
 			// 提取name
        	var argname = pairs[i].substring(0, pos);
        	// 提取value  
			var value = pairs[i].substring(pos + 1);
        	
			document.getElementsByName(argname)[0].value = decodeURI(value);
 		}

		function updateGiftApplyInfo() {
	
			if (isNull(document.getElementsByName('id')[0].value)) {
				
				alert('id值为空，不能进行更新操作！');
				return;
			}
			
			if (isNull(document.getElementsByName('addressee')[0].value)) {
			
				alert('姓名不能为空！');
				return;
			}
			
			if (isNull(document.getElementsByName('address')[0].value)) {
			
				alert('地址不能为空！');
				return;
			}
			
			if (isNull(document.getElementsByName('cellphoneNo')[0].value)) {
				
				alert('手机号码不能为空！');
				return;
			}
			
			if (confirm("确定要做修改操作吗？")) {
			
				var data = $("#mainform").serialize();
				
				var action = "/office/modifyGiftApplyInfo.do";
	
				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(data) {
					
					alert(data.responseText);
					
					var _parentWin = window.opener;
					 _parentWin.mainform.submit();
					window.close();
					
				}});
			}
		};

		function isNull(value) {
		
			if (null == value || "" === value || "" === $.trim(value)) {
			
				return true;
			}
		
			return false;
		};
	</script>
</html>