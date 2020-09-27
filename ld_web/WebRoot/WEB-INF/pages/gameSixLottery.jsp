<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<base href="<%=request.getRequestURL()%>" />
		<s:include value="/title.jsp"></s:include>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<script type="text/javascript">
if(top.location != self.location)
{
	top.location = self.location;
}
</script>
<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<style type="text/css">
body {
	overflow-x: hidden;
	overflow-y: hidden;
}
</style>
	<script type="text/javascript">
  $(document).ready(function () {
	  var winWidth = 0;
      var winHeight = 0;
      //获取窗口宽度
      if(window.innerWidth)
         winWidth = window.innerWidth;
      else if ((document.body) && (document.body.clientWidth))
          winWidth = document.body.clientWidth;
      //获取窗口高度
      if (window.innerHeight)
          winHeight = window.innerHeight;
      else if ((document.body) && (document.body.clientHeight))
          winHeight = document.body.clientHeight;
      //通过深入Document内部对body进行检测，获取窗口大小
      if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
          winHeight = document.documentElement.clientHeight;
          winWidth = document.documentElement.clientWidth;
      }
      $("#ifrm").css("height",""+(winHeight-100));
      $("#ifrm").css("width",winWidth);
  });
</script>
	<body>
		<jsp:include page="${ctx }/tpl/header.jsp"></jsp:include>
		<jsp:include page="gameSixLotteryContent.jsp"></jsp:include>
		
		<%@include file="/tpl/footer.jsp" %>
	</body>
</html>