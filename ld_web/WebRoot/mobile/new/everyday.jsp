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
<!DOCTYPE>
<html>
	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="每日任务" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>
	<body>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/everyday/newsign.jsp"><img src="/mobile/img/icon/sign.png" alt="" />每日签到<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<div class="list-group">
			<a class="list-item" href="/mobile/new/everyday/hongbao.jsp"><img src="/mobile/img/icon/red.png" alt="" />存款红包<i class="iconfont fr icon-downjiantou"></i></a>
		</div>
		<!-- <div class="list-group">
			<a class="list-item" href="/mobile/new/everyday/cunsong.jsp"><img src="/mobile/img/icon/win.png" alt="" />每日存送<i class="iconfont fr icon-downjiantou"></i></a>
		</div> -->		
		<!-- <div class="list-group">
			<a class="list-item" href="/activety/zzrw/index.jsp"><img src="/mobile/img/icon/everyday2.png" alt="" />任务周周送<i class="iconfont fr icon-downjiantou"></i></a>
		</div> -->
		<jsp:include page="/mobile/commons/menu.jsp" />
	</body>
</html>