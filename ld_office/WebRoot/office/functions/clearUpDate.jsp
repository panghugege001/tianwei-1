<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
	<head>
		<title>自动清理数据</title>
	</head>
	<body>
		<script type="text/javascript" src="/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript">
		  function submitUserAction(btn){
			  if(window.confirm('你确定?')){
			     btn.disabled=true;
			     $.post("/office/submitClearUserAction.do", {
			     }, function (returnedData, status) {
			        if ("success" == status) {
			            alert(returnedData);
			        }
			     });
			  }
		  }
		</script>
	<body>
		<table border="1" cellpadding="0" cellspacing="0" width="80%"
			align="center">
			<tr align="center">
				<th height="40px">
					自动清理数据
				</th>
			</tr>
			<tr align="center">
				<th height="40px">
					<input type="button"
						style="font: 14px/ 21px Arial; padding: 5px 10px; color: blue; text-decoration: none;"
						value="处理<fmt:formatDate value="${endTime}" type="both" pattern="yyyy-MM-dd" />之前的用户信息，无存款无记录玩家！"
						onclick="submitUserAction(this);">
				</th>
			</tr>
		</table>
	</body>
</html>
