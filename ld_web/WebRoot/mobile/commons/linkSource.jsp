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

<base href="${ctx}/"/>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
    <meta name="full-screen" content="yes">
<title><%=title%></title>
<link rel="stylesheet" href="/mobile/css/lib/swiper/swiper.min.css">
<link rel="stylesheet" href="//at.alicdn.com/t/font_226548_x6c9lyjmgn20ggb9.css">

<%--<link rel="stylesheet" href="/mobile/css/base-new.css?v=0503">--%>
<script>
    (function(doc, win) {
        var docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function() {
                var clientWidth = docEl.clientWidth;
                if (!clientWidth) return;
                if (clientWidth >= 750) {
                    docEl.style.fontSize = '100px';
                } else {
                    docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
                }
            };

        if (!doc.addEventListener) return;
        win.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener('DOMContentLoaded', recalc, false);
    })(document, window);

</script>

<script>

<c:choose>
	<c:when test="${session.customer==null}">
		!(function(){
			sessionStorage.setItem('permission',0);
            sessionStorage.setItem('loginname','');
		})();
	</c:when>
	<c:when test="${session.customer.role eq 'MONEY_CUSTOMER'}">
		!(function(){
			sessionStorage.setItem('permission',1);
			sessionStorage.setItem('loginname','${session.customer.loginname}');
		})();
	</c:when>
	<c:otherwise>
		!(function(){
			sessionStorage.setItem('permission',2);
            sessionStorage.setItem('loginname','${session.customer.loginname}');
		})();
	</c:otherwise>
</c:choose>

</script>