<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>更新缓存数据</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div style="position: absolute; top: 20px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1180px" border="1" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<thead>
									<tr>
										<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">更新内容</td>
										<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 100px;">操&nbsp;作</td>
									</tr>
								</thead>
								<tbody id="tbody"></tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>					
	</body>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="/app/common/function.js"></script>
	<script type="text/javascript">
	
		function refreshCache(value, self) {
			
			if (isNull(value)) {
				
				alert("更新缓存名称不能为空！");
				return;
			}
			
			$(self).attr("disabled", "disabled");
			
			$.ajax({
				type: "GET",
				url: "/app/reloadCache.do?cacheMethodName=" + value + "&r=" + Math.random(),
				async: false,		
				data: {},
				success : function(data) {

					alert(data);
				}
			});
			
			$(self).removeAttr("disabled");
		};
		
		$(document).ready(function() {

			var arr = [
			    { "id": "1", "description": "更新约束地址配置缓存数据", "functionName": "reloadConstraintAddressCache" },
			    { "id": "2", "description": "更新活动专题配置缓存数据(APP首页banner图)", "functionName": "reloadSpecialTopicCache" },
			    { "id": "3", "description": "更新中奖信息配置缓存数据", "functionName": "reloadLatestWinInfoCache" },
			    { "id": "4", "description": "更新首页热门优惠缓存数据", "functionName": "reloadHotPreferentialCache" },
			    { "id": "5", "description": "更新乐虎风采缓存数据", "functionName": "reloadLhStyle" },
			    { "id": "6", "description": "更新APP定制打包缓存数据", "functionName": "reloadAppPakCustomCache" }
			];
			
			var str = "";

			for (var index = 0, len = arr.length; index < len; index++) {

				str += "<tr><td>" + arr[index]["description"] + "</td><td align='center'><input type='button' value='更 新' onclick=\"refreshCache(\'" + arr[index]["functionName"] + "\', this)\" /></td></tr>";
			}
			
			$("#tbody").html("").append(str);
		});
	</script>
</html>