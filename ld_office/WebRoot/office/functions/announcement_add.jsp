<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>发布公告</title>

<style type="text/css" media="all">
   		@import url("/css/announcement.css"); 
   </style>
   <s:head/>
   
  </head>
  
  <body>
<div class="announcement_titleDiv" >
其他 --> 发布公告
</div>  
<div>
<br/>
	<div align="center"><s:fielderror/></div>
	<s:form action="add" namespace="/announcement" theme="simple">
		<table align="center" border="0" cellpadding="0" cellspacing="0" style="font-size:13px" width="100%">
			<tr align="left">
				<td width="80px">公告类别:</td>
				<td >
					<s:select name="type" list="%{#application.AnnouncementType}" listKey="code" listValue="text" theme="simple"/>
				</td>
			</tr>
			<tr align="left">
				<td width="80px">公告标题:</td>
				<td>
					<s:textfield name="title" id="title" size="100" theme="simple"></s:textfield>
				</td>
			</tr>
			<tr>
				<td width="80px">公告内容:</td>
				<td>
					<s:textarea cols="50" rows="20" id="content" name="content" ></s:textarea>

				</td>
			</tr>
			<tr>
				<td></td>
				<td align="center">
					<s:submit value="发布公告" style="font-size:15px; width:80px; height:30px" theme="simple"/>
					<s:reset value="重新填写" style="font-size:15px; width:80px; height:30px" theme="simple"></s:reset>
				</td>
			</tr>
		</table>
	</s:form>
</div>

  </body>
</html>
