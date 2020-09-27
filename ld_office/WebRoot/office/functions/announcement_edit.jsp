<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>修改公告</title>
    
<style type="text/css" media="all">
   		@import url("/css/announcement.css"); 
   </style>
<s:head/>
  </head>
  
  <bod>
<div class="announcement_titleDiv"  style="font-size:13px;">
其他 --> 编辑公告
</div> 
<br/>  
<div  >
	<p align="center"  style="font-size:13px;"><s:fielderror></s:fielderror></p>
	<s:form action="edit" namespace="/announcement" theme="simple">
		<s:hidden name="id" value="%{#request.id}"></s:hidden>
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr align="left">
				<td width="80px" style="font-size:13px;">公告类别:</td>
				<td >
					<select name="type" >
						<s:if test="type.equals('INDEX')">
							<option value="INDEX" selected="selected">首页公告</option>
							<option value="INDEX_TOP">置顶公告</option>
						</s:if>
						<s:else>
							<option value="INDEX">首页公告</option>
							<option value="INDEX_TOP" selected="selected">置顶公告</option>
						</s:else>
						
					</select>
					
				</td>
			</tr>
			<tr align="left">
				<td width="80px" style="font-size:13px;">公告标题:</td>
				<td>
					<s:textfield name="title" id="title" size="64" theme="simple"></s:textfield>
				</td>
			</tr>
			<tr>
				<td width="80px" style="font-size:13px;">公告内容:</td>
				<td>
					<s:textarea cols="50" rows="20" id="content" name="content" value="%{#request.content}"></s:textarea>
				</td>
			</tr>
			<tr>
				<td></td>
				<td align="center">
					<s:submit value="发布公告" style="font-size:15px; width:80px; height:30px" theme="simple"/>
					<input type="button" value=" 返  回 " onclick="javascript:window.history.go(-1)" style="font-size:15px; width:80px; height:30px" theme="simple">
				</td>
			</tr>
		</table>
	</s:form>
</div>
  </body>
  
</html>
