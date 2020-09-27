<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<script type="text/javascript">
    window.onload = function(){
        document.getElementById("sbLoginForm").submit();
    };
</script>
<div id="content_bg" style="text-align: center;">	
	<form action="https://sbtyloginTransitc.com/shaba/sbMobileLogin.php" method="post" id="sbLoginForm">
        <input id="loginname"  type="password" name="loginname" value="${sessionScope.customer.loginname}" style="display:none">
        <input id="domain" type="password" name="domain" value="sbtyloginTransitc.com" style="display:none">
        <input id="token" type="password" name="token" value="${token}" style="display:none">
        <input id="website" type="password" name="website" value="${website}" style="display:none">
        <input id="types" type="password" name="types" value="1,0,t" style="display:none">
    </form>
</div>
