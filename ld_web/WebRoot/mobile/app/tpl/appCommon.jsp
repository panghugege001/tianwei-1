<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dfh.utils.Constants"%>
	<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
	String infoValue=(String)session.getAttribute("infoValue4Live800");
    if(infoValue==null)infoValue="";
%>
<c:url value="/mobi/mobileValidateCode.aspx" scope="request" var="imgCode" />
<!DOCTYPE >
<html>
	<head>
	<base href="${ctx}/"/>
	<title>天威</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="keywords" content="PT老虎机 TTG老虎机 NT老虎机 MG老虎机 QT老虎机 AG老虎机 DT老虎机" />
	<meta name="description" content="天威-亚洲最强老虎机娱乐城，旗下拥有众多知名国际老虎机平台，选择天威，尽享奢华体验。" />
	<meta name="keywords" content="PT老虎机 TTG老虎机 NT老虎机 MG老虎机 QT老虎机" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1, maximum-scale=1.0" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="apple-mobile-web-app-capable" content="yes" />


	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="pragma" content="no-cache" />

	<link rel="stylesheet" type="text/css" href="mobile/css/mui.grid.system.css" />
	<link rel="stylesheet" type="text/css" href="mobile/css/lib/mui-0.2.1/mui.min.css?v=22" />
	<%--<link rel="stylesheet" type="text/css" href="mobile/css/flaticon/flaticon.css?v=2" />--%>
	<link rel="stylesheet" type="text/css" href="mobile/css/loader.css?v=1" />
	<link rel="stylesheet" type="text/css" href="mobile/app/css/appCommon.css?v=9" />
	<link rel="stylesheet" type="text/css" href="mobile/css/footerContent.css?v=3" />
	<link rel="stylesheet" type="text/css" href="mobile/css/common.mui.css?v=1" />
	<link rel="stylesheet" type="text/css" href="mobile/css/date.mui.css?v=1" />
	<link rel="stylesheet" type="text/css" href="mobile/css/grid.css" />
	<link rel="stylesheet" type="text/css" href="mobile/css/cssmarquee.css" />

	<script type="text/javascript" src="mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="mobile/js/lib/jquery/imagesloaded.pkgd.min.js"></script>
	<script type="text/javascript" src="mobile/js/lib/mui-0.2.1/mui.js"></script>

	</head>
	<body>

	<div class="my-alert-box j-alert1" style="display:none;">
		<div class="my-alert">
			<div class="my-alert-title">温馨提示</div>
			<div class="my-alert-con">我是提示文字</div>
			<div class="buttons">
				<span class="alert-btn alert-close" onclick='closeTips()'>点我关闭</span>
			</div>
		</div>
	</div>

	<header class="common-header" style="display: none; overflow: hidden;">
		<div id="comm-back-button" class="left-button" onClick="goFundsIndex();" style="margin-top:3px;"></div>
		<div id="comm-header-title" class="header-title">
			我要存款
		</div>
	</header>

