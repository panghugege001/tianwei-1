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
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="领取记录" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>
	<body>
		<div class=" birthday-histiry  txt-form">
			<ul class="histiry-list">
				
			</ul>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			RedCouponPage()
			function RedCouponPage() {
				var _html=''
				//领取生日 礼金
				function _query() {
					mobileManage.getLoader().open('执行中');
					 $.post("/asp/queryBirthdayRecords.aspx",{}, function (result) {
						mobileManage.getLoader().close();
						var arr = JSON.parse(result)
					 	if(arr.length<=0){
							_html = '<li class="msg">没有领取记录</li>'

						}
						if(arr.length>0){
							for(var i=0;i<arr.length;i++){
								_html+= '<li>'
										+	'<div><b class="moeny-bag"></b></div>'
										+	'<div class="content">'
										+		'<p>'+arr[i].activityName+'</p>'
										+		'<span class="time">'+arr[i].remark+'</span>'
										+	'</div>'
										+	'<div>'
										+		'<span class="moeny">￥'+arr[i].activityPercent+'</span>'
										+	'</div>'
										+'</li>'
							}
						}
						$('.histiry-list').html(_html)
					 })
					
				}
				_query();
				
			}
		</script>
	</body>

</html>