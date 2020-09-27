<%
	session.invalidate();
	response.sendRedirect(request.getContextPath()+"/office/index.jsp");
%>
