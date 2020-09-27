<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>提案执行明细</title>
</head>
<body>
<div id="excel_menu_left">
记录 --> 提案执行明细 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<p align="left"><s:fielderror/></p>
<table align="left">
<tr><td>提案号:<s:property value="#request.pno"/></td><td>提交时间:<s:date name="#request.createTime" format="yyyy-MM-dd HH:mm:ss"/></td><td></td></tr>
<s:if test="#request.flag==@dfh.model.enums.ProposalFlagType@AUDITED.code || #request.flag==@dfh.model.enums.ProposalFlagType@EXCUTED.code">
<tr><td>审核人:<s:property value="#request.auditedTask.manager"/></td><td>审核时间:<s:date name="#request.auditedTask.agreeTime" format="yyyy-MM-dd HH:mm:ss"/></td><td>审核IP:<s:property value="#request.auditedTask.agreeIp"/></td></tr>
</s:if>
<s:if test="#request.flag==@dfh.model.enums.ProposalFlagType@AUDITED.code || #request.flag==@dfh.model.enums.ProposalFlagType@EXCUTED.code">
<tr><td>执行人:<s:property value="#request.excutedTask.manager"/></td><td>执行时间:<s:date name="#request.excutedTask.agreeTime" format="yyyy-MM-dd HH:mm:ss"/></td><td>执行IP:<s:property value="#request.excutedTask.agreeIp"/></td></tr>
</s:if>
<s:if test="#request.flag==@dfh.model.enums.ProposalFlagType@CANCLED.code">
<tr><td>取消人:<s:property value="#request.cancledTask.manager"/></td><td>取消时间:<s:date name="#request.cancledTask.agreeTime" format="yyyy-MM-dd HH:mm:ss"/></td><td>取消IP:<s:property value="#request.cancledTask.agreeIp"/></td></tr>
</s:if>
</table>
</div>

<br/>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

