<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="dfh.action.vo.AnnouncementVO"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
        <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<script type="text/javascript">
		if(top.location != self.location){
			top.location = self.location;
		}
		</script>
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
      $("#ifrm").css("height",""+winHeight-(winHeight*0.25));
      $("#ifrm").css("width",winWidth-(winWidth*0.2));
  });
</script>

	</head>
	<body>
	
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
  <div id="content_bg" style="text-align: center;background-color: #383431;">
      <label style="color: yellow;">
          <a href="${ctx}/loginGame.aspx?gameCode=${gameCode}" target=" _blank" style="color: yellow;font-size: 20px;">全屏游戏</a>
      </label>
  </div>
  <div id="content_bg" style="text-align: center;">
      <iframe name="ifrm" id="ifrm"
              src="${ctx}/loginGame.aspx?gameCode=${gameCode}" frameborder="0" scrolling-y="auto" scrolling-x="auto"
              marginwidth="0" height="800" width="100%" style="text-align: left;"></iframe>
  </div>

  
  <jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>


	</body>
</html>

