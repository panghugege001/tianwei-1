<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>棋乐游洗码数据采集</title>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
<script type="text/javascript" src="/js/jquery.messager.js"></script>
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/layer/layer.js"></script>

</head>
<body>
	<br/>
	<br/>
	<br/>
	<br/>
	<div  style="position: absolute; top: 180px; left: 0px;">
		更新时间:<s:textfield  id="_executeTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false"  cssClass="Wdate"  />
		<input type="button" id="_executeUpdateBtn" value="采集洗码数据" onclick="ordsumit(this);"/>
	</div>
	<!-- <input id="submit" type="button" value="采集前一天数据" onclick="return ordsumit(this);" /> -->
</body>
<script type="text/javascript">

	function ordsumit(th) {
		var executeTime = $("#_executeTime").val();
		if(confirm("你确认要执行此操作么？")){
			var self = $(th);
			self.attr("disabled", true);
			self.val("正在采集数据,请稍后....");
			
			$.ajax({
				url: "${ctx}/office/collection761Data.do",
				type: "post",
				data:{ "executeTime" : executeTime },
				async : true,
				timeout: 0,
				success: function (data) {
					alert(data);
					self.attr("disabled", false);
					self.val("采集洗码数据");
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
					self.attr("disabled", false);
					self.val("采集洗码数据");
				}
			});
		}
	};
</script>
</html>
