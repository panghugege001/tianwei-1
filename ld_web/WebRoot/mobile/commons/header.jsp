<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	String cpuid=(String) request.getSession(true).getValue("cpuid");
	response.setHeader("pragma", "no-cache");
	response.setHeader("cache-control", "no-cache");
	response.setDateHeader("expires", 0);
	String serverName = request.getServerName();
	if (serverName.startsWith("www")) {
		serverName = serverName.substring(4);
	}
	String title = "";
	if (Constants.titles.containsKey(serverName)) {
		title = Constants.titles.get(serverName);
	} else {
		title = Constants.titles.get("e68.ph"); // old title
	}
	String infoValue=(String)session.getAttribute("infoValue4Live800");
    if(infoValue==null)infoValue="";

%>
<c:url value="/mobi/mobileValidateCode.aspx" scope="request" var="imgCode" />

<head>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<base href="${ctx}/" />
	<title>
		<%=title%>
	</title>
	<meta name="keywords" content="PT老虎机 TTG老虎机 NT老虎机 MG老虎机 QT老虎机 AG老虎机 DT老虎机" />
	<meta name="description" content="天威-亚洲最强老虎机娱乐城，旗下拥有众多知名国际老虎机平台，选择天威，尽享奢华体验。" />
	<meta name="keywords" content="PT老虎机 TTG老虎机 NT老虎机 MG老虎机 QT老虎机" />
	<link rel="stylesheet" href="/mobile/css/new/base.css" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="format-detection" content="telephone=no" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_226486_fqtbqjfka66iggb9.css" />
</head>
<!--头部内容-->
<div class="header" id='header'>
	<div class="left-btn">
		<c:if test="${param.menu==1}">
			<i class="iconfont icon-msnui-menu"></i>
		</c:if>
		<c:if test="${param.menu!=1}">
			<div id='back-btn'><i class="iconfont icon-arrow-left"></i></div>
		</c:if>
	</div>
	<span id='h-title'>${param.Title}</span>
	<a class="server-btn j-server">
		<i class="iconfont icon-cs"></i>
	</a>
</div>
<!--左侧菜单-->
<div class="left-nav">
	<div class="main">
		<div class="close iconfont icon-close"></div>
		<div class="top">
			<c:if test="${session.customer!=null}">
				<div class="level">
					<s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)" />
				</div>
				<div>${session.customer.loginname}</div>
			</c:if>
			<c:if test="${session.customer==null}">
				<img src="mobile/img/logo.png" alt="" />
				<div>欢迎登录天威</div>
			</c:if>

		</div>
		<ul class="item-list">
			<c:choose>
				<c:when test="${session.customer!=null}">
					<li>
						<a href="/mobile/new/email.jsp"><img src="/mobile/img/icon/rightnav3.png" alt="" />站内信
							<i class="iconfont icon-dian hidden c-ylow j-msgcount"></i>
						</a>
					</li>
					<c:if test="${session.customer.role!='AGENT'}">
						<li>
							<a href="/mobile/new/everyday.jsp"><img src="/mobile/img/icon/everyday2.png" alt="" />每日任务</a>
						</li>
						<!-- <li>
							<a href="/mobile/new/everyday/newsign.jsp"><img src="/mobile/img/icon/check.png" alt="" />每日签到</a>
						</li> -->
					</c:if>
					<!-- <li>
						<a href="/asp/bbsIndex.aspx"><img src="/mobile/img/icon/leftnav3.png" alt="" />官方论坛</a>
					</li> -->
					<li>
						<a href="/mobile/new/download.jsp"><img src="/mobile/img/icon/leftnav4.png" alt="" />客户端下载</a>
					</li>
					<li>
						<a href="/mobile/safeCenter.jsp"><img src="/mobile/img/icon/leftnav4.png" alt="" />自助中心</a>
					</li>
					<li>
						<a href="/sitemap.jsp"><img src="/mobile/img/icon/safe.png" alt="" />线路测试</a>
					</li>
					<li id="comm-logout-button" onclick="window.header.logout()">
						<img src="/mobile/img/icon/loginout.png" alt="" />安全退出
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<a href="/mobile/new/login.jsp"><img src="/mobile/img/icon/leftnav1.png" alt="" />用户登录</a>
					</li>
					<li>
						<a href="/mobile/new/register.jsp"><img src="/mobile/img/icon/leftnav2.png" alt="" />注册送彩金</a>
					</li>
					<!-- <li>
						<a href="/asp/bbsIndex.aspx"><img src="/mobile/img/icon/leftnav3.png" alt="" />官方论坛</a>
					</li> -->
					<li>
						<a href="/mobile/new/download.jsp"><img src="/mobile/img/icon/leftnav4.png" alt="" />客户端下载</a>
					</li>
					<li>
						<a href="/mobile/safeCenter.jsp"><img src="/mobile/img/icon/s-icon.png" alt="" />自助中心</a>
					</li>
					<li>
						<a href="/sitemap.jsp"><img src="/mobile/img/icon/safe.png" alt="" />线路测试</a>
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>
<!--右侧菜单-->
<div class="right-nav">
	<div class="main">
		<div class="top">
			<div class="close iconfont icon-close">
			</div>
		</div>
		<ul class="item-list">
			<li class="item">
				<a target="_blank" href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19"><img src="mobile/img/icon/rightnav1.png" alt="" />在线客服</a>
			</li>
			<!-- <li>
				<div class="item js-btn-cancel"><img src="mobile/img/icon/rightnav2.png" alt="" />电话回拨</div>
				<div class="call-back" id='js-call-back'>
					<input type="text" id='js-call-number' placeholder="请输入您的联系号码" class="input" />
					<div class="btn-warp">
						<div class="btn-cancel js-btn-cancel">取消</div>
						<div id='js-btn-call' class="btn-call">电话回拨</div>
					</div>
					<div class="txt">回拨为注册电话，点击号码回拨即可，如果回拨不是注册电话，请输入最新电话 ，点击回拨</div>
				</div>
			</li> -->
			<li class="item">
				<a href="mailto:tianwei661@gmail.com">
					<img src="mobile/img/icon/rightnav3.png" alt="" />
					<div class="other">客服邮箱
						<small>tianwei661@gmail.com</small>
					</div>
				</a>
			</li>
			<li class="item">
				<div>
				<a>
					<img src="/mobile/img/icon/leftnav3.png" alt="" />
					微信客服
				</a>
				</div>
				<div class="weixin_kef">
					<img src="/images/appxiazai/weixin.jpg">
				</div>
			</li>
		</ul>
		<!--<div class="bottom">
			<div class="title"><i class="iconfont icon-wechat"></i>关注微信</div>
			<img src="mobile/img/code.jpg" alt="" />
			<div>扫一扫关注</div>
		</div>-->
	</div>
</div>
<!--未读信息弹窗-->
<div class="massaage_box">
	<h3>温馨提示</h3>
	<span class="close_gb massage_close">X</span>
	<div class="massaage_div">
		<p>您有未读邮件请注意查收</p>
		<div>
			<a href="/mobile/new/email.jsp" class="clove1">查看详情</a>
			<a href="javascript:;" class="massage_close clove2">取消</a>
		</div>
	</div>
</div>	
<!--隐藏域-->
<textarea id='cpuid' style="display: none;"><%=cpuid %></textarea>
<input type="hidden" id="ioBB">
<input type="hidden" id="browsertype">
<input type="hidden" id="j-username" value="${session.customer.loginname}" />
<input id="j-isLogin" type="hidden" value="${session.customer!=null}">
<!--隐藏域-->

<script src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script src="/mobile/js/new/base.js"></script>
<script src="/mobile/js/new/function.js"></script>
<script src="/mobile/js/Loader.js?v=5"></script>
<!--<script type="text/javascript" src="/mobile/js/lib/mui-0.2.1/mui.min.js?v=1210"></script>
<script type="text/javascript" src="/mobile/js/MUIModel.js?v=1210"></script>-->
<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1211"></script>
<script type="text/javascript" src="/mobile/js/UserManage.js?v=1210"></script>
<script type="text/javascript" src="/mobile/js/BankManage.js?v=1210"></script>
<script type="text/javascript" src="/mobile/js/WebApp.js?v=1210"></script>
<script>
	window.mobileManage = new MobileManage('${ctx}/', '${imgCode}');
	window.webapp = new WebApp();
	(function() {
		function header() {
			var _this = this;
			_this.setTitle = function(o) {
				$('#h-title').html(o);
			}
			_this.backFun = function() {

				return history.back();
			}
			_this.logout = function(jumpagent) {
				mobileManage.getLoader().open('登出中');
				mobileManage.getUserManage().logout(function(data) {
					if(data.success) {
						if(jumpagent){
							window.location.href = '/mobile/new/login.jsp?agent';
							return false;
						}
						window.location.reload();
					} else {
						alert(data.message);
						mobileManage.getLoader().close();
					}
				});
			}
		}
		window.header = new header();

	})(window)
	$("#header #back-btn").click(function() {
		window.header.backFun();
	})
	$('.left-nav,.right-nav').click(function(e) {
		if(!$(e.target).is(".main *:not(.close)")) {
			$(this).fadeOut(500);
		}
	})
	$('.header .icon-msnui-menu').click(function() {
		$('.left-nav').show();
	})
	$('.header .j-server').click(function() {
		$('.right-nav').show();
	})
	$(function() {
		$.post("/asp/getGuestbookCountNew.aspx", {}, function(data) {
			if(data != "0") {
				$(".j-msgcount").show()
				if(!sessionStorage.getItem('email')&&window.location.href.indexOf('index.jsp')>-1){
					$(".massaage_box").show();
					sessionStorage.setItem('email',true)
					$('.massaage_box .massage_close').click(function(){
						$(".massaage_box").hide();
					})
				}
			} else {
				$(".j-msgcount").hide()
			}
		});
		var call_number = $("#js-call-number")
		$(".js-btn-cancel").click(function() {
			$("#js-call-back").slideToggle();
		})
		$("#js-btn-call").click(function() {
			mobileManage.getLoader().open('处理中');
			mobileManage.getUserManage().makeCall({
				phone: call_number.val()
			}, function(result) {
				if(result.success) {
					call_number.val('')
					alert(result.message);
				} else {
					alert(result.message);
				}
				mobileManage.getLoader().close();
			});
		});
		call_number.bind("keyup", function(e) {
			if(e.which == '13') {
				$("#js-btn-call").click();
			}
		});
	})

	//APP下载

	function appDownload(){
	var isEmpty = 0;
	var version = getMobileKind().toUpperCase();
	// step 1
	if(version == 'OTHER') {
	alert('请用安卓或者苹果设备打开！！');
	return false;
	}
	// step 2 判断代理域名
	$.getJSON('/asp/checkAgentURLogin.aspx', function(data) {
	if(false == data) {
	// step 3 获取代理包
	$.getJSON('/app/getAppVersionCustomInfo.aspx', function(data) {
	if(data.length > 0) {
	// 代理域名，有代理包
	$.each(data, function(i, obj) {
	//根据移动平台获取下载链接
	if(obj.plat.toUpperCase() === version) {
	if(obj.packageUrl != "") {
	isEmpty = 0;
	window.location.href = obj.packageUrl;
	return false;
	} else {
	isEmpty = 1;
	}
	} else {
	isEmpty = 1;
	}
	});

	if(isEmpty == 1) {
	alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
	return false;
	}
	} else {
	// step 4 代理域名，无代理包
	if('true' == 'true') {
	alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
	return false;
	} else {
	webapp.redirect(window.location.origin + '/app.html?v=2');
	}
	}
	});
	} else {
	webapp.redirect(window.location.origin + '/app.html?v=2');
	}
	});
	}
</script>