<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.remote.RemoteCaller"%>
<%
	out.println("测试远程额度查询:"+RemoteCaller.checkClient("168168").toString());
%>
