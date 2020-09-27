<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE >
<html>

	<head>
		<title>天威</title>>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="体验金" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<style>
			.form-tips {
				font-size: 15px;
				font-weight: bold;
				margin-bottom: 0!important;
				margin-left: 0.333333rem;
			}
			.form-tips small {
				font-weight: lighter;
				display: block;
				line-height: 0.533333rem;
				color: #666!important;
			}
			.img{height: 2.08rem;width: 100%;}
			.down-qrcode{line-height: 0.8rem; background: #fff;border-radius: 0.133333rem; text-align: center; padding: 0.333333rem 0;}
			.down-qrcode img{width: 2.426666rem;height: 2.426666rem;}
			.down-qrcode .save{display:block; margin: 0 auto; height: 0.933333rem;line-height:0.933333rem;text-align: center;
				border-radius: 0.133333rem;background: #dfa85a; width: 5.173333rem;background:#dfa85a;color:#fff}
		</style>
	</head>

	<body>
		<!-- <a onclick="appDownload()"><img class="img" src="/mobile/img/go.png" alt="" /></a>
		<br /> -->
		<div class="form-warp no-icon">
			<div class="form-tips">方式1<small>(手机任意浏览器输入网址)</small></div>
			<div class="form-group">
				<input class="form-control" value="tianwei4.com" id='website-txt'></input>
				<div class="form-code" onclick="copyNum('#website-txt')">复制</div>
			</div>
			<div class="form-tips">方式2<small>(扫描二维码轻松下载)</small></div>
			<div class="down-qrcode">
				<img src="/images/appxiazai/anzhuokaihu.png" alt="">
				<div>打开手机、扫一扫</div>
				<div>下载APP领彩金</div>
				<a target="_blank" class="save"  href="/images/appxiazai/anzhuokaihu.png">保存图片</a>
			</div>
			<div class="text-tips">
				<div class="h3">彩金申请攻略：</div>
				1、打开浏览器输入已复制的网址即可下载。<br /> 2、扫描天威APP二维码下载。
				<br /> 3、下载APP之后登录游戏账号后点击【账户】-【自助优惠】 -【自助体验金】即可领取。
			</div>
		</div>
		<div class="copy-done">
			<i class="iconfont icon-duihaocheckmark17"></i>
			<div>复制成功</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
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