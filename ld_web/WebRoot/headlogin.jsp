<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<s:url action="login" namespace="/asp" var="headloginUrl"></s:url>
<form action="${headloginUrl}" method="post" id="loginform" name="loginform" onsubmit="return checkLoginForm()">
	<s:if test="#session.customer==null">
	      <label>用户名：</label>
	      <div class="userinput"><input name="loginname"/></div>
	      <label class="password">密码：</label>
	      <div class="userinput"><input type="password" name="password"/></div>
	      <input type="submit" value="" style="cursor: pointer;" class="loginbtn"/>
	      <input type="button" style="cursor: pointer;" class="registration" onclick="window.location='/freeregistration.asp'">
     </s:if>
     <s:else>
     	 <label >欢迎回来&nbsp;&nbsp;<s:if test="#session.customer.level==0">新会员</s:if><s:elseif test="#session.customer.level==1">忠实vip</s:elseif><s:elseif test="#session.customer.level==2">星级vip</s:elseif><s:elseif test="#session.customer.level==3">黄金vip</s:elseif><s:elseif test="#session.customer.level==4">白金vip</s:elseif><s:elseif test="#session.customer.level==5">钻石vip</s:elseif><s:elseif test="#session.customer.level==6">至尊vip</s:elseif> <span style="color:#b20047;">&nbsp;&nbsp;${session.customer.aliasName}</span> <!--  反水率:<span style="color:#b20047;"><fmt:formatNumber  value="${session.customer.rate*100}"></fmt:formatNumber>%</span>-->  </label>
     	<label>
			&nbsp;&nbsp;站内信：<a href="/asp/bookHome.aspx" target="_blank"><span style="color: #b20047;"><SPAN id="bookCount" style="color: #b20047;">0封</SPAN></span></a>&nbsp;&nbsp;
		</label> 
		<!-- <label>
			站内信：<a href="/asp/bookHome.aspx" target="_blank"><span style="color: #b20047;">1封</span></a>&nbsp;&nbsp;
		</label>--> 
     	 <input type="button" style="cursor: pointer;" value="" class="managementbtn" onclick="window.location='/accountAmount_member.asp'" />
     	 <input type="button" style="cursor: pointer;" value="" class="exitbtn" onclick="window.location='/asp/logout.aspx'" />
     </s:else>
     
     
</form>
 
 <div class="clear"></div>
<script language="javascript" type="text/javascript">
	function checkLoginForm(){
		if(loginform.loginname.value.length<=0){
			alert("用户名不可为空");
			return false;
		}else if(loginform.password.value.length<=0){
			alert("用户密码不可为空");
			return false;
		}else{
			return true;
		}
	}
	$(document).ready(function () {
	    var loginname="${session.customer.loginname}";
	    if(loginname!=""){
	        $.post("asp/getGuestbookCount.aspx", {
        	}, function (returnedData, status) {
            	if ("success" == status) {
                	$("#bookCount").html(returnedData+"封");
            	}
        	});
	    }
    });
</script>