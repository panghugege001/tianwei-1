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
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="每日存送" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>

		<div class="form-warp txt-form">
			<div class="text-tips">
				<p><span class="c-red">*</span>计算一周活动时间，彩金于活动结束后每日18点派发，达到10倍流水即可提款。</p>
			</div>
			<div class="text-tips">
				<div class="h3">活动内容：</div>
				<p>当周达到周存款活动或周流水活动任务，在未来七天（下周）即可领取周周送彩金活动，每日领取最高688彩金，一周有七天，天天领取688，简直暴力。</p>
				<ol>
					<li>6/4   00:00:00 -  6/10  23:59:59举行周存款活动</li>
					<li>6/11  00:00:00-  6/17  23:59:59举行周流水活动</li>
					<li>6/18  00:00:00-  6/24  23:59:59举行周存款活动</li>
					<li>6/25  00:00:00-  7/1   23:59:59举行周流水活动</li>
				</ol>
			</div>
			<table class="table">
				<thead>
					<tr><th>活动</th><th>范围</th><th>彩金</th><th>流水</th></tr>
				</thead>
				<tbody>
					<tr>
						<td rowspan="5">周存款</td><td>5001-20000</td><td>28</td><td rowspan="5">10</td>
					</tr>
					<tr>
						<td>20001-50000</td><td>38</td>
					</tr>
					<tr>
						<td>50001以上</td><td>58</td>
					</tr>						
					<tr>
						<td>100000以上</td><td>88</td>
					</tr>						
					<tr>
						<td>500000以上</td><td>688</td>
					</tr>
				</tbody>
			</table>
			<table class="table">
				<thead>
					<tr><th>活动</th><th>范围</th><th>彩金</th><th>流水</th></tr>
				</thead>
				<tbody>
					<tr>
						<td rowspan="5">周流水</td><td>50001-100000</td><td>28</td><td rowspan="5">10</td>
					</tr>
					<tr>
						<td>100001-500000</td><td>38</td>
					</tr>
					<tr>
						<td>500001以上</td><td>58</td>
					</tr>						
					<tr>
						<td>1000000以上</td><td>88</td>
					</tr>						
					<tr>
						<td>2000000以上</td><td>688</td>
					</tr>
				</tbody>
			</table>

		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
	</body>

</html>