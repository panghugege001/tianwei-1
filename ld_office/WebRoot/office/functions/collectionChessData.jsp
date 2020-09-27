<%@page pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>采集前一天761平台输赢数据</title>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
p.tip span {
	font-weight:bold;
	color:#ff9955;
	}
</style>

</head>
<body>
	</br>
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<h4>采集前一天761平台输赢数据</h4>
	<input id="submit" type="button" value="采集数据" onclick="return ordsumit(this);" />
	</br>
	</br>
	</p>
	</br>
	</br>
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<h4>采集前一天泛亚电竞平台数据</h4>
	<input id="submit" type="button" value="采集数据" onclick="return fanyasumit(this);" />
	</br>
	</br>
	</p>
	</br>
</body>
<script type="text/javascript">

	function ordsumit(th) {
		if(confirm("你确认要执行此操作么？")){
			var self = $(th);
			self.attr("disabled", true);
			self.val("正在采集数据,请稍后....");
			
			$.ajax({
				url: "/office/collectionChessData.do",
				type: "post",
				async : true,
				timeout: 0,
				success: function (data) {
					alert(data);
					self.attr("disabled", false);
					self.val("采集数据");
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
					self.attr("disabled", false);
					self.val("采集数据");
				}
			});
		}
	};


    //泛亚电竞
    function fanyasumit(th) {
        if(confirm("你确认要执行此操作么？")){
            var self = $(th);
            self.attr("disabled", true);
            self.val("正在采集数据,请稍后....");

            $.ajax({
                url: "/office/fanyaLogData.do",
                type: "post",
                async : true,
                timeout: 0,
                success: function (data) {
                    alert(data);
                    self.attr("disabled", false);
                    self.val("采集数据");
                },
                error: function () {
                    alert("操作超时导致结果未知，请联系技术查看情况....");
                    self.attr("disabled", false);
                    self.val("采集数据");
                }
            });
        }
    };
</script>
</html>
