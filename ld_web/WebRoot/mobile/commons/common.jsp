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
	<link rel="stylesheet" type="text/css" href="mobile/css/lib/mui-0.2.1/mui.min.css?v=33" />
	<link rel="stylesheet" type="text/css" href="mobile/css/loader.css?v=10" />
	<link rel="stylesheet" type="text/css" href="mobile/css/common.css?v=20" />
	<link rel="stylesheet" type="text/css" href="mobile/css/footerContent.css?v=3" />
	<link rel="stylesheet" type="text/css" href="mobile/css/common.mui.css?v=12" />
	<link rel="stylesheet" type="text/css" href="mobile/css/grid.css?v=10" />
	<link rel="stylesheet" type="text/css" href="mobile/css/date.mui.css?v=10" />
	<link rel="stylesheet" type="text/css" href="mobile/css/cssmarquee.css?v=10" />
	
	</head>
	<body>
		<div id="touclick-container" style="height: 0;overflow: hidden;"></div>
		<div class="common-contact">
			<nav class="menu-side" id="toolbar">
				<div class="tool-close"><div class="iconfont icon-close"></div></div>
				  <div class="item">
				    <%-- <c:if test="${session.customer==null}">
				            <a target="_blank" href="http://chat.l8serviceule.com/chat/chatClient/chatbox.jsp?companyID=467&configID=77&jid=">
				                <i class="iconfont icon-cs"></i>
				                <div class="txt">在线客服</div></a>
				        </c:if>--%>
				    <a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank"> <i class="iconfont icon-cs"></i>
				      <div class="txt" style="float: right;">在线客服</div>
				    </a> </div>
				<!--<div class="item" onClick="openQQ();">
					<i class="iconfont icon-iconqq"></i>
					<span class="text">QQ客服<br/><span class="cee6"></span></span>
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
                <c:choose>
                    <c:when test="${session.customer==null}">
                       <a href="mobile/login.jsp"><span class="nav-text"> <i class="iconfont icon-user"></i> 登录/注册</span></a>
                    </c:when>
                    <c:otherwise>
                    	<a href="javascript:;" id="comm-logout-button"><span class="nav-text"> <i class="iconfont icon-user"></i>登出</span></a>
                    </c:otherwise>
                </c:choose>
            </div>
			<div class="header-title">
				<div class="logo"></div>
			</div>
            <div class="nav-right header-nav fr" id="comm-other-button">
                <a href="javascript:;" data-live-action="">
                    <span class="nav-text">
                        <i class="iconfont icon-phone"></i>
                        联系客服</span>
                </a>
            </div>
		</header>
		
<!--未读信息弹窗-->
<input type="text" value="${session.customer==null}" hidden="hidden" id="massage" />
<div class="massaage_box">
	<h3>温馨提示</h3>
	<span class="close_gb massage_close">X</span>
	<div class="massaage_div">
		<p>您有未读邮件请注意查收</p>
		<div>
			<a href="/mobile/email.jsp" class="clove1">查看详情</a>
			<a href="javascript:;" class="massage_close clove2">取消</a>
		</div>
	</div>
</div>		
		
	</body>
<script type="text/javascript" src="../../dianying/js/jquery-1.11.1.min.js"></script>	
<input id="j-isLogin" type="hidden" value="${session.customer!=null}">
<script>
 	$(function(){
		$("#r_close").click(function(){
			window.location.reload();
		})
		
		var msg = sessionStorage.getItem('showmassage')
		$.get("/asp/getGuestbookCountNew.aspx",function(data){
			
			if($("#massage").val()==="true"){
				$(".massaage_box").hide();
				return false;
			} 		

        if(msg==="0" || msg===null){
       
        $(".massaage_box").show();	
        
	        }else{
	        
	        $(".massaage_box").hide();
	        return false;
        
        }
			
			if(data==="0"){
				$(".massaage_box").hide();
				return false;
			}else{
				$(".massaage_box").show();
			}
			
		})
		

		
		$(".massage_close").click(function(){
			$(".massaage_box").hide();
			  sessionStorage.setItem('showmassage', 1);				
		})
		
		$(".clove1").click(function(){
			$(".massaage_box").hide();	
			  sessionStorage.setItem('showmassage', 1);				
		}) 		
 	})
</script>
 

</html>