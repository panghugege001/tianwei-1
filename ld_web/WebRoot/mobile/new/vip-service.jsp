<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="VIP专属客服" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body class="email-page" style="background: #eeeeee;">
		<div id="tab-service-column">
			<img id="ewm-pic" src="" alt="">
			<p>专属客服提供您宾至如归服务,如情人般的呵护</p>
		</div>
		<style>
			#tab-service-column{
				text-align: center;
				padding:150px 0;
			}
			#tab-service-column p{
				margin-top:15px;
			}
			#ewm-pic{
				width:210px;
				height:210px;
				background:#666;
			}
		</style>
		<script>
			$.get('/asp/queryQRcode.aspx',function(data){
				if((data.length && data[0].address)){
					$('#ewm-pic').attr('src',data[0].address)
				}
			},'json')
		</script>
	</body>

</html>