<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if(!"AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/account.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="账户中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/lib/swiper/swiper.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide">
					<img src="/mobile/img/banner/agent/agent1.jpg" style="width:100%" />
				</div>
				<div class="swiper-slide">
					<img src="/mobile/img/banner/agent/agent2.jpg" style="width:100%" />
				</div>
				<div class="swiper-slide">
					<img src="/mobile/img/banner/agent/agent3.jpg" style="width:100%" />
				</div>
			</div>
			<div class="swiper-pagination">
			</div>
		</div>
		<div class="form-warp no-icon" id="pane-default-0">
			<!--<label class="form-tips"><img src="/mobile/img/icon/websiet.png" alt="" />推广网站01</label>-->
			<!--<div class="form-group">
				<input type="text" class="form-control" value="${session.customer.referWebsite}" disabled="" readonly>
				<span class="form-code">复制</span>
			</div>-->
		</div>
		<div class="copy-done">
			<i class="iconfont icon-duihaocheckmark17"></i>
			<div>复制成功</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/lib/swiper/swiper.js"></script>
		<script>
			new Swiper('.swiper-container', {
				pagination: '.swiper-pagination'
			});
			var ary = [
				'<label class="form-tips"><img src="/mobile/img/icon/websiet.png" alt="" />推广网站0{0}</label>',
				'<div class="form-group">',
				'<input type="text" class="form-control" value="{1}" disabled="" readonly>',
				'<span class="form-code">复制</span>',
				'</div>'
			].join('')
			$.get("/asp/queryAgentAddress2.aspx", function(data) {
				if(data && data.length > 0) {
					var _html = ''
					$.each(data, function(i, item) {
						_html += ary.format(i+1, item);
					});
					$("#pane-default-0").append(_html)
				}
			})

			$("#pane-default-0").on('click', '.form-code', function() {
				copyNum($(this).prev().get(0));
			})
		</script>
		<script type="text/javascript">
			// 思路：要想复制到剪贴板，必须先选中这段文字。
			function copyNum(dom) {
				var NumClip = $(dom).get(0);
				var NValue = NumClip.value;
				var valueLength = NValue.length;
				selectText(NumClip, 0, valueLength);
				if(document.execCommand('copy', false, null)) {
					document.execCommand('copy', false, null) // 执行浏览器复制命令
					console.log("已复制,赶紧分享给朋友吧");

					$(".copy-done").fadeIn(1000)
					setTimeout(function() {
						$(".copy-done").fadeOut(1000)
					}, 1500)
				} else {
					console.log("不兼容");
				}
			}
			// input自带的select()方法在苹果端无法进行选择，所以需要自己去写一个类似的方法
			// 选择文本。createTextRange(setSelectionRange)是input方法
			function selectText(textbox, startIndex, stopIndex) {
				if(textbox.createTextRange) { //ie
					var range = textbox.createTextRange();
					range.collapse(true);
					range.moveStart('character', startIndex); //起始光标
					range.moveEnd('character', stopIndex - startIndex); //结束光标
					range.select(); //不兼容苹果
				} else { //firefox/chrome
					textbox.setSelectionRange(startIndex, stopIndex);
					textbox.focus();
				}
			}
		</script>
	</body>

</html>