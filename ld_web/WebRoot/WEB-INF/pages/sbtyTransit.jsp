<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<script type="text/javascript">
    window.onload = function(){
        document.getElementById("sbLoginForm").submit();
    };
</script>
<div id="content_bg" style="text-align: center; visibility:hidden;">
	<form action="https://sbtyloginTransitc.com/shaba/sbLogin.php" method="post" id="sbLoginForm">
        <input id="loginname" type="password" name="loginname" value="${sessionScope.customer.loginname}" style="display:none">
        <input id="domain" type="password" name="domain" value="sbtyloginTransitc.com" style="display:none">
        <input id="token" type="password" name="token" value="${token}" style="display:none">
    </form>
</div>
