<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>">
     <%@ page import="dfh.model.Users" %>
    <title>修改资料</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link type="text/css" rel="stylesheet" href="yilufa.css" />
<style type="text/css">
<!--
body {
	background-color: #0d0213;
}
-->
</style>
<script LANGUAGE="javascript">
<!--

function btnOK_onclick()
{
     if (document.checkform.aliasName.value.length>12)
  {
    alert("[提示]昵称长度不能大于12个字符！");
    document.checkform.aliasName.focus();
    return false;
  }

   if(document.checkform.qq.value.length>0){
	   if(isNaN(document.checkform.qq.value)==true)
	   {
	       alert("[提示]QQ号码必须为数字！");
	       document.checkform.qq.focus();
	       return false;
	   }
   }

   if(document.checkform.phone.value.length>0){
	   if(isNaN(document.checkform.phone.value)==true)
	   {
	       alert("[提示]电话非有效数字！");
	       document.checkform.phone.focus();
	       return false;
	   }
   }
     if(document.checkform.mailaddress.value.length>50){
   		 alert("[提示]邮寄地址不允许超过50个字符！");
   		 document.checkform.mailaddress.focus();
   		 return false;
   }
   document.checkform.submit();
}

//-->
</script>
  </head>
  
  <body>
   <%
	HttpSession chksession=request.getSession(true);
	Users user =(Users)chksession.getValue("customer");
	if(user == null){
	%>
		<script language="javascript">
			alert("你的登录已过期，请从首页重新登录");
		</script>
	<% 
		response.sendRedirect("login.jsp");
	}
 %>
     <div id="requstcontent_s" style="width:710px;">
                   <s:include value="cwtop.jsp"></s:include>
                    <h2>修改个人资料</h2>
                    <s:url action="change_infocw" namespace="/asp" var="modifyUserInfoUrl"></s:url>
                    <form id="modifyform" action="${modifyUserInfoUrl }" method=post name="checkform">
                        <!--  <p>天威账号：&nbsp;${session.customer.loginname}</p>
                        <p style="margin-left:11px;margin-left:12px\9;">注册货币：&nbsp;人民币</p>
                        <p style="margin-left:11px;margin-left:12px\9;">账户结余：&nbsp;<span style="color:#aa005c; font-weight:bold;">${session.customer.credit}</span></p>
                        <p style="margin-left:35px;">姓名：&nbsp;${session.customer.loginname}</p>
                        <p style="margin-left:11px;_margin-left:5px; margin-top:2px; float:left;">用户昵称：&nbsp;</p>
                        <input type="text" name="aliasName" class="transferinput nickname" value="${session.customer.aliasName}"/><div class="clear"></div>
                        <p style="margin-left:11px;margin-left:12px\9;_margin-left:9px;">电子邮箱：&nbsp;${session.customer.email}</p> 
                        <p style="margin-left:10px;margin-left:17px\9;_margin-left:7px; margin-top:2px; float:left;">QQ &nbsp;号码：&nbsp;</p>
                        <input type="text"  name="qq" class="transferinput modifyqq" value="${session.customer.qq}"/><div class="clear"></div>
                        <p style="margin-left:22px;margin-left:23px\9; margin-top:-4px;float:left;_margin-left:10px;">联络电话：&nbsp;</p>
                        <input type="text" class="transferinput telnum"  name="phone" value="${session.customer.phone}"/><div class="clear"></div>
                       <p style="margin-left:22px;margin-left:23px\9; margin-top:-4px;float:left;_margin-left:10px;">邮寄地址：&nbsp;</p>
                        <textarea class="transferinput telnum" style="width:300px;" name="mailaddress">${session.customer.mailaddress}</textarea><div class="clear"></div>
                        
                        <s:if test="#session.customer.sms==0">
	                       			<input type="checkbox" checked="checked" name="sms" value="male" class="modifycheck" /> 
	                       	</s:if>
	                       	<s:else>
	                       		<input type="checkbox" name="sms" value="male" class="modifycheck" /> 
	                       	</s:else>
	                       			
                        <p style="margin-left:2px; float:left; vertical-align:middle; padding-top:0; margin-top:-2px;margin-top:0px\9;#margin-top:4px;">我要接收会员通讯及最新优惠计划</p><div class="clear"></div>
	                    
                        <input type="button" class="modifysubmit" value=""  onclick="return btnOK_onclick()" style="cursor :pointer"/>
                       
                         <input type="reset" class="modifycancel" value="" style="cursor :pointer"/>
                        <div class="clear"></div> -->
                    </form>
             </div>
  </body>
</html>
