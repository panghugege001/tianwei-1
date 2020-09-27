<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="content_bg"
	style="text-align: center;background-color: #383431;">
	<label style="color: yellow;">
		<a href="${ctx}/gameKenoRedirect.aspx" target=" _blank" style="color: yellow;font-size: 20px;">全屏游戏</a>
	</label>
</div>
<div id="content_bg" style="text-align: center;">
	<iframe name="ifrm" id="ifrm" 
		src="${ctx}/gameKenoRedirect.aspx" frameborder="0"
		marginwidth="0" height="800" width="1024" ></iframe>
</div>