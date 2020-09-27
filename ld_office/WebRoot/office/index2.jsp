<%@page errorPage="500.jsp" pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<s:if test="#session.operator!=null">
	<c:redirect url="/office/home.jsp" />
</s:if>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>9winOffice</title>
    <link rel="stylesheet" href="<c:url value='/css/lefttree.css' />" type="text/css">
  </head>
  <body>
  <div align="center">
  	<br/><br/><br/><br/><br/><br/><br/><br/><br/>
  	<p align="center"><strong>9win后台</strong></p>
  	<s:form action="officeLogin" namespace="/office" theme="css_xhtml" validate="true">
  	<s:fielderror></s:fielderror>
    <table cellpadding="0" cellspacing="0" align="center">
      <tr>
        <td>账号：</td>
        <td colspan="2"><s:textfield name="loginname"  size="20" maxlength="12" /></td>
      </tr>
       <tr><td colspan="3" height="5"></td></tr>
      <tr>
        <td>密码：</td>
        <td colspan="2"><s:password name="password"  size="20"  maxlength="20" /></td> 
      </tr>
      <tr><td colspan="3" height="5"></td></tr>
      <tr>
        <td>验证码：</td>
        <s:url action="validateCodeForIndex" namespace="/jsp" var="imgCode"></s:url>
        <td><s:textfield name="validateCode"  size="10"  maxlength="4" onfocus="document.getElementById('imgCode').style.display='block';document.getElementById('imgCode').src='%{imgCode}?'+Math.random();"/></td> 
        <td><img id="imgCode" src=""  title="如果看不清验证码，请点图片刷新" onclick="document.getElementById('imgCode').src='<c:url value='/jsp/validateCodeForIndex.do' />?'+Math.random();" style="cursor: pointer;display:none;" /></td>
      </tr>
      <tr><td colspan="3" height="20"></td></tr>
       <tr>
        <td><s:submit value="登录"/></td>
        <td><s:submit value="重置"/></td> 
        <td>&nbsp;</td>
      </tr>
      </table>
      </s:form>
      </div>
      <c:import url="/commons/office_script.jsp" />
  </body>
</html>
