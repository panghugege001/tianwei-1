<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<TITLE> 上下框架 </TITLE>
</HEAD>
<frameset rows="40%,*" frameborder="YES" border="0" framespacing="0">
  <frame name="topFrame" scrolling="YES" noresize src="<c:url value='/app/pakManage/appPackageVersionSomeRights.jsp' />" >
  <frame name="bottomFrame" id="bottomFrame" scrolling="YES" noresize src="<c:url value='/app/pakManage/appPackageVersionCustomSomeRights.jsp' />">
</frameset>
<noframes><body></body></noframes>
</HTML>