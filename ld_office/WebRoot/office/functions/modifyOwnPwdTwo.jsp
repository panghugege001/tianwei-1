<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%
	ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
	String image_path="/"+request.getContextPath()+"/images/";
%>

<s:url value="/images" var="image_path"></s:url>
<s:set var="start" value="@dfh.utils.DateUtil@getToday()" />
<s:set var="end" value="@dfh.utils.DateUtil@getTomorrow()" />
<s:set var="startPt" value="@dfh.utils.DateUtil@ptStart()" />
<s:set var="endPt" value="@dfh.utils.DateUtil@ptEnd()" />
<s:set var="startYyyyMM" value="@dfh.utils.DateUtil@startYyyyMM()" />
<s:set var="endYyyyMM" value="@dfh.utils.DateUtil@endYyyyMM()" />
<s:set var="year" value="@dfh.utils.DateUtil@getYear(@dfh.utils.DateUtil@getMontToDate(@dfh.utils.DateUtil@getToday(),-30))" />
<s:set var="month" value="@dfh.utils.DateUtil@getMonth(@dfh.utils.DateUtil@getMontToDate(@dfh.utils.DateUtil@getToday(),-30))" />
<s:set var="vyear" value="%{year}"/>
<s:set var="vmonth" value="%{month}"/>
<s:set var="monthEnd" value="%{month}"/>
<s:date name="start" format="yyyy-MM-dd HH:mm:ss" var="startTime"/>
<s:date name="end" format="yyyy-MM-dd HH:mm:ss" var="endTime"/>
<s:date name="startPt" format="yyyy-MM-dd HH:mm:ss" var="startPt"/>
<s:date name="endPt" format="yyyy-MM-dd HH:mm:ss" var="endPt"/>
<%    
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改密码</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color:#ca6433;
}
#admin { width:450px; padding-top:104px; margin:auto; }
.ctext { font-size:12px; color:#000000; text-align:left; letter-spacing:0.07em;}
.ctexttitle { color:#ffffff; text-align:left; letter-spacing:0.07em; font-weight:bold; text-align:center;}
-->
</style>
</head>
<body>
<div id="wrapper">
  <div id="admin">
    <div align="center" style=" border:1px dotted #cf9165; background:#f1d1a8">
     <div class="ctexttitle" style="background:#e76d06; height:30px; line-height:30px;">强制修改密码</div>
	  
	 <s:form action="modifyOwnPwdTwo" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
	  <table width="400px" border="0" align="center" cellpadding="0" cellspacing="0" class="ctext">
	 <tr>
          <td colspan="2"><span style="color:red"><s:fielderror></s:fielderror></span></td>
        </tr>
        <tr> 
          <td width="59" height="33" style="padding-left:2px">用户名:</td>
          <td colspan="2"><label>
	  <s:textfield id="loginname" name="loginname" size="30" disabled="true" value="%{#session.operatename}" cssStyle="border:1px solid #2a0e02"/>
          </label></td>
        </tr>
        <tr>
          <td width="59" height="33" style="padding-left:2px">旧密码:</td>
          <td height="33" colspan="2">
	  <s:password name="oldPassword" size="30"  cssStyle="border:1px solid #2a0e02" />
	  </td>
        </tr>
	 <tr>
          <td width="59" height="33" style="padding-left:2px">新密码:</td>
          <td height="33" colspan="2">
	  <s:password name="newPassword" size="30"  cssStyle="border:1px solid #2a0e02" />
	  </td><p style="color:red;">密码数字开头，至少8位，必须包含字母和特殊符号</p>
        </tr>
         <tr>
          <td width="59" height="33" style="padding-left:2px">重复密码:</td>
          <td height="33" colspan="2">
	  <s:password name="retypePassword" size="30"  cssStyle="border:1px solid #2a0e02" />
	  </td>
        </tr>
        
        <tr id="smstr" style="display: none">
          <td width="59" height="33" style="padding-left:2px">短信验证码:</td>
          <td height="33" colspan="2">
		  	<s:textfield id="smsValidPwd" name="smsValidPwd"  size="30" cssStyle="border:1px solid #2a0e02"  maxlength="10" />
		  </td>
        </tr>
        <tr  id="smstrbut" style="display: none">
        	<td></td>
        	<td width="79">
          		<input type="button" onclick="sendSms();" value="<s:text name="发送短信验证码"/>"/>
          	</td>
        </tr>
        
        <tr>
          <td height="33"><span style="padding-left:2px"><s:text name="验证码"/>:</span></td>
           <s:url action="validateCodeForIndex" namespace="/jsp" var="imgCode"></s:url>
          <td width="90" height="33"><s:textfield name="validateCode"  size="8" cssStyle="border:1px solid #2a0e02"  maxlength="10" onfocus="document.getElementById('imgCode').style.display='block';document.getElementById('imgCode').src='%{imgCode}?r='+Math.random();"/></td>
          <td width="79"><img id="imgCode" src=""  title="<s:text name="validateCode.tip"/>" onclick="document.getElementById('imgCode').src='<c:url value='/jsp/validateCodeForIndex.do' />?r='+Math.random();" style="cursor: pointer;display:none;" /></td>
        </tr>        
        
        
        <tr> 
          <td width="59" height="33">&nbsp;</td>
          <td height="33" colspan="2"><input type="submit" name="Submit" value="修改密码"  style="text-align:center"/></td>
        </tr>
        
        <tr> 
          <td height="33" colspan="3"><p style="color:red;">温馨提示：</p>密码强制修改，每7天一次，谢谢合作。</td>
        </tr>
      </table>
      </s:form>
    </div>
  </div>
</div>
<c:if test="${not empty errormsg}">
	<script type="text/javascript">
		alert("<c:out value="${errormsg}"/>");
	</script>
</c:if>
<script type="text/javascript">
function validSmsType(){
	
	var loginname = $("#loginname").val();

	if(loginname == null || loginname == ''){
		//alert("请先输入用户名");
		return;
	}
	
	$.ajax({ 
        type: "post", 
        url: "/office/validSmsType.do", 
        cache: false, 
        async: false,
        data:{
      	  "loginname" : loginname
        },
        timeout:600000, 
        
        success : function(data){
        	if(data == "success"){
        		$("#smstr").css("display","");
        		$("#smstrbut").css("display","");
        		$("#smsValidPwd").val("");
        	} else {
        		$("#smstr").css("display","none");
        		$("#smstrbut").css("display","none");
        		$("#smsValidPwd").val("");
        	}
        },
        error: function(){alert("系统错误");},
		complete: function(){
			/* $("#distributeBtn").attr("disabled", false); */
		}
  });
}

function sendSms(){
	var loginname = $("#loginname").val();
	
	if(loginname == null || loginname == ''){
		alert("请先输入用户名");
		return;
	}
	$.ajax({ 
        type: "post", 
        url: "/office/sendSmsPwd.do", 
        cache: false, 
        async: false,
        data:{
      	  "loginname" : loginname
        },
        timeout:600000, 
        
        success : function(data){console.log(data);alert(data);},
        error: function(){alert("系统错误");},
		complete: function(){
			/* $("#distributeBtn").attr("disabled", false); */
		}
  });
}


$(document).ready(function(){
  validSmsType();
});
	
</script>
</body>

</html>

