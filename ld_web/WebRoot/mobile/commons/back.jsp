<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
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
<!DOCTYPE >
<html>
	<head>
	<base href="${ctx}/"/>
	<title><%=title%></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="description" content="天威" />
	<meta name="keywords" content="<%=title%>" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=2.0" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<link rel="stylesheet" href="//at.alicdn.com/t/font_ln120xfdk7k138fr.css?v=1"> 
	<link rel="shortcut icon" href="/favicon.ico?v=22" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href="mobile/css/mui.grid.system.css" />
	<link rel="stylesheet" type="text/css" href="mobile/css/lib/mui-0.2.1/mui.min.css?v=22" />
	<link rel="stylesheet" type="text/css" href="mobile/css/loader.css?v=10" />
	<link rel="stylesheet" type="text/css" href="mobile/css/common.css?v=20" />
	<link rel="stylesheet" type="text/css" href="mobile/css/footerContent.css?v=3" />
	<link rel="stylesheet" type="text/css" href="mobile/css/common.mui.css?v=11" />
	<link rel="stylesheet" type="text/css" href="mobile/css/grid.css?v=10" />
	<link rel="stylesheet" type="text/css" href="mobile/css/date.mui.css?v=10" />
 	<script type="text/javascript" src="mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="mobile/js/lib/jquery/imagesloaded.pkgd.min.js"></script>	
	<script type="text/javascript" src="mobile/js/lib/mui-0.2.1/mui.js"></script>
	<script type="text/javascript" src="mobile/js/util.js"></script>
	<script type="text/javascript" src="mobile/js/Loader.js?v=1019"></script>

 
	<script type="text/javascript" src="mobile/js/MUIDate.js?v=1"></script>
	<script type="text/javascript" src="mobile/js/MUIModel.js?v=1210"></script>

	<script type="text/javascript" src="mobile/js/UserManage.js?v=1020"></script> 
	<script type="text/javascript" src="mobile/js/BankManage.js?v=11"></script>
	<script type="text/javascript" src="mobile/js/AgentManage.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/TPPManage.js?v=18"></script>
	<script type="text/javascript" src="mobile/js/SelfGetManage.js?v=12"></script>
	<script type="text/javascript" src="mobile/js/SignManage.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/MobileManage.js?v=1033"></script>
	<script type="text/javascript" src="mobile/js/MobileGrid.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/MobileComboBox.js?v=10"></script>
 
	<script type="text/javascript" src="mobile/js/self/ExperienceManage.js?v=11"></script>
	<script type="text/javascript" src="mobile/js/self/EmigratedManage.js?v=10"></script>
	
	<script type="text/javascript" src="mobile/js/common/FooterBar.js?v=12"></script> 
	<script type="text/javascript" src="mobile/js/common/HeaderBar.js?v=10"></script>
	
	<script type="text/javascript" src="mobile/js/WebApp.js?v=1019"></script>
 
	</head>
	<body>
		<div id="touclick-container" style="height: 0;overflow: hidden;"></div>
		<div class="common-contact">
			<nav class="menu-side" id="toolbar">
				<div class="tool-close"><div class="iconfont icon-close"></div></div>
				<div class="item">
					<a class="iconfont icon-cs" id="onlineService"  href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank"></a>
					<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank">在线客服</a>
				</div>
				<!--<div class="item" onClick="openQQ();">
					<i class="iconfont icon-iconqq"></i>
					<span class="text">QQ客服<br/></span>
				</div>-->
				<div class="item " onClick="makeCall()">
					<i class="iconfont icon-phone1"></i>
					<span class="text">电话回播</span>
				</div>
				<div class="item" onClick="openEmail()">
					<i class="iconfont icon-mail2" ></i>
					<span class="text">客服邮箱<br/><span class="cee6">tianwei661@gmail.com</span></span>
				</div>
				<!--<div class="item" >-->
					<!--<i class="iconfont icon-wechat" ></i>-->
					<!--<span class="text">微信号<br/><span class="cee6">longdu66</span></span>-->
				<!--</div>-->
				<!--<div class="item qr"><img src="/images/qr/ld-wx-ewm.png" width="100%" alt="">    </div>-->
			</nav>
		</div>
		<div class="common-screen"></div>
		<header class="common-header">
        	<div class="nav-left header-nav fl">
                <a class="back-action" data-back-action="" href="javascript:;">
					<span class="nav-text">
						<i class="iconfont icon-arrow-left"></i>
						返回</span>
				</a>
            </div>
			<div class="header-title">
				<div class="logo"></div>
			</div>
			<%--<div  class="right-button flaticon-headset"></div>--%>
            <div class="nav-right header-nav fr" id="comm-other-button">
                <a href="javascript:;" data-live-action="">
                    <span class="nav-text">
                        <i class="iconfont icon-phone"></i>
                        联系客服</span>
                </a>
            </div>
		</header>
		<script type="text/javascript">
		$('.common-screen').css('opacity','0');
			window.mobileManage = new MobileManage('${ctx}/','${imgCode}');
			window.webapp = new WebApp();
			//mobileManage.getLoader().open('载入中');
			var headerBar = new HeaderBar({role:'${session.customer.role}'});
			var footerBar = new FooterBar({role:'${session.customer.role}'});
			var _pageId = mobileManage.getSessionStorage('common').pageId||'index';
 
			//开启关闭 联系我们
			$('.common-screen').click(otherButtonClick);
				headerBar.$el.find('#comm-other-button').click(otherButtonClick);
				headerBar.$el.find('#comm-login-button').click(function(){
				mobileManage.getModel().open('login',['index']);
			});
			headerBar.$el.find('#comm-logout-button').click(function(){
				mobileManage.getModel().open('logout');
			});
			$(window).load(function(){
				//后退
					$(document).on('click','[data-back-action]',function(){
						// 控制返回键是回退历史还是切换tab
						var $indexPage= $('.tab-panel[data-page-index]');
						if($indexPage.length){
							if($indexPage.hasClass('active')){
								history.go(-1);
								return false;
							}else{
								//$('.header').find('.header-title').text();
								$indexPage.addClass('active').siblings().removeClass('active');
							}
						}else{
							history.go(-1);
							return false;
						} 
					});
					var $toolbar=$('#toolbar');
					$toolbar.find('.tool-close').on('click',function () {
						otherButtonClick();
					});
					/*if(webapp.isWebApp()&&webapp.getVersion()){
						var $el1 = $([
							'<div class="item">',
							'	<i class="iconfont icon-voice" ></i>',
							'	<span class="text" >语音客服</span>',
							'</div>'
						].join(''));
						
						$el1.bind('click',function(){
							var name = '${session.customer.loginname}'||'游客';
							window.webapp.openAppKeFu(name);
						});
						
						$('.common-contact .menu-side').prepend($el1);
					}*/
	 			//下方浏览方式切换
				var NEEDMODIFY = '${session.NEEDMODIFY}';
			    if(NEEDMODIFY == "1" ){
			      	alert("您的密码安全指数较低，请修改以保障资金安全");
			      	mobileManage.getModel().open('modifyPassword');
			    }
			   mobileManage.getLoader().close();
			});
		
			/**
			 * 取得设备ID
			 */
		    function getDeviceId(deviceId, platform, version){
		    	/*if('${session.mobileDeviceID==null?true:false}'=='true'){
		    		$.post("${ctx}/asp/signDevice.aspx", {"cpuid":deviceId,"platform":platform
			        }, function (returnedData, status) {
			            if ("success" == status) {
			    			if('${session.customer==null?true:false}'=='true'){addRegister();}
			            }
			        });
		    	}else{
		    		if('${session.customer==null?true:false}'=='true'){addRegister();}
		    	}*/
		    };
			//开启关闭 联系我们
			function otherButtonClick(){
				if($('body').hasClass('show-contact')){
					$('body').removeClass('show-contact');
				}else{
					$('body').addClass('show-contact');
				}
			}
			//电话回播
			function makeCall(){
				otherButtonClick();
				mobileManage.getModel().open('makeCall');
			}
			//开启QQ
			function openQQ(){
				var download = getMobileKind()=='Android'?'http://gdown.baidu.com/data/wisegame/dc429998555b7d4d/QQ_398.apk':'https://itunes.apple.com/cn/app/qq-2011/id444934666?mt=8';
				webapp.redirect('mqq://im/chat?chat_type=wpa&uin=800134430','尚未安装QQ！',download);
// 				window.location.href = 'mqq://im/chat?chat_type=wpa&uin=800134430';
			}
			//开启email
			function openEmail(){
				//safri 无法用window.open开启
				webapp.redirect('mailto:tianwei661@gmail.com');
 
			}
			function onekeyMonery() {
			var jsonData = ajaxPost("/asp/oneKeyGameMoney.aspx");
			if (jsonData == null || jsonData == "" || typeof jsonData == "undefined") {
				 
				alert("一键回归成功!");
				window.location.reload();
			}else {
				alert(jsonData);
			}
		}
		
		
		function ajaxPost(url, parm){
			 
			var RESULT;
			$.ajax({
				url      : url,
				type     : "post",
				data     : parm,
				cache    : false,
				async    : false,
				timeout  : 3000,
				success: function(jsonData) {
					RESULT = jsonData;
					 
					return RESULT;
				}
			});

			return RESULT;
		}
		</script>
	</body>
</html>