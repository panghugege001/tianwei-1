<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page import="dfh.model.Users" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
     <base href="<%=request.getRequestURL()%>"/>
   <s:include value="title.jsp"></s:include>
    <meta http-equiv="description" content="登录信息"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
<link type="text/css" rel="stylesheet" href="yilufa.css" />
<style type="text/css">
<!--
body {
	background-color: #0d0213;
}
-->
</style>
<%
	HttpSession chksession=request.getSession(true);
	Users user =(Users)chksession.getValue("customer");
	if(user == null){
		out.print("<script type=text/javascript>window.location.href='index.asp';</script>");
		out.flush();
	}
 %>
<script language="javascript">

//判断密码是必须是中英文组合
	function isNumAndStr(str){
     var regexpUperStr=/[A-Z]+/;
     var reexpLowerStr=/[a-z]+/;
     var regexpNum=/\d+/;
     var uperStrFlag = regexpUperStr.test(str);
     var lowerStrFlag = reexpLowerStr.test(str);
     var numFlag = regexpNum.test(str);
     if((uperStrFlag&&lowerStrFlag)||(lowerStrFlag&&numFlag)||(uperStrFlag&&numFlag))
        return true;
     return false;
   }
   
	 
function CheckInput()
{

  if (document.frmAction.password.value=='')
	  {
	    alert("[提示]用户旧密码不可为空！");
	    return false;
	  }
  if (document.frmAction.new_password.value=='')
  {
    alert("[提示]用户新密码不可为空！");
    return false;
  }
  if (document.frmAction.sPass2.value=='')
  {
    alert("[提示]用户确认新密码不可为空！");
    return false;
  }
  
  var spassword = document.frmAction.sPass2
  if (spassword.value != "" && (spassword.value.length < 8 || spassword.value.length >12)){
      alert("[提示]密码的长度请介于8-12字符之间！")
      
      return false;
  }
  
  if(!isNumAndStr(spassword.value)){
    		alert("[提示]密码必须同时包含字母和数字");
    		return false;
   	 }	
	
  if (document.frmAction.new_password.value!=document.frmAction.sPass2.value)
  {
    alert("[提示]两次输入的密码不一致，请核对后重新输入！");
    return false;
  }
  return true;  
}
function btnOK_onclick() {
  if (!CheckInput()) return false;
  
  document.frmAction.submit();
}



//-->
</script>

</head>

<body>
<div id="aftercontainercon">
     <div id="container">
             		<div id="header">
                  <div id="logincontainer">
                      <div id="bulletincon">
                         	<s:include value="headnews.jsp"></s:include>
                      </div>
                      <!--bulletincon-->
                     	<s:include value="headlogin.jsp"></s:include>
                  </div>
                  <!--logincontainer--> 
                  <div id="navcontainer">
                      <s:include value="headmenu2.jsp"></s:include>
                  </div>
                  <!--navcontainer-->
             </div>
             <!--header-->
             <div id="managementcontent">
                    <div class="managementitle"><img src="images/management01.jpg" /></div>
                    		<s:include value="memberFunctionList_member.jsp"></s:include>
                    		<s:include value="inventoryMenu_member.jsp"></s:include>
                    		
               
                    <s:url action="change_pws" namespace="/asp" var="changePwdUrl"></s:url>
                    <form  id="passwordform" name=frmAction method=post  action="${changePwdUrl }" onsubmit="return CheckInput()">
                        <label style="margin-left:18px;">上次登录时间：</label>
                        <span style="color:#b20047;">${session.time}</span><br />
                        <label style="margin-left:18px;">上次登录IP：</label>
                        <span style="color:#b20047;">${session.ip}</span><br />
                        <label style="margin-left:18px;">上次登录地点：</label>
                       <span style="color:#b20047;">${session.city}</span><br />
                       <label style="margin-left:18px;">登录次数：</label>
                       <span style="color:#b20047;"><% if(null!=user){out.print(user.getLoginTimes()+" 次");}%></span><br />
                       
                    </form> 
             </div>
             <!--managementcontent-->
             <div id="footer">
               			<s:include value="/tpl/footer.jsp"></s:include>
               <!--footermenu-->
             </div>
             <!--footer-->  
  </div>
        <!--container--> 
</div>
<!--containercon-->
</body>
</html>
