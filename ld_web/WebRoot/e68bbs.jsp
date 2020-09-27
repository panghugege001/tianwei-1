<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/js/jquery18.js" type="text/javascript"></script>
<script type="text/javascript">
      $(document).ready(function () {
          var userName="${session.customer.loginname}";
          var password="${session.customer.password}";
          if(userName!=""&&password!=""){
             $("#bbs").submit();
          }else{
             window.location.href="http://www.longdobbs.com/forum.php";
          }
      });
</script>
<body>
	<form id="bbs" action="http://www.longdobbs.com/member.php" method="post">
		<input type="hidden" name="mod" value="logging" />
		<input type="hidden" name="action" value="login" />
		<input type="hidden" name="loginsubmit" value="yes" />
		<input type="hidden" name="infloat" value="yes" />
		<input type="hidden" name="infloat" value="yes" />
		<input type="hidden" name="username" value="${session.customer.loginname}" />
		<input type="hidden" name="password" value="${session.customer.password}" />
	</form>