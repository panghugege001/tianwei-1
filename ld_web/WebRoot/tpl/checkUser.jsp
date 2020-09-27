<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
    var loginname="${session.customer.loginname}";
    if(loginname==null || loginname==""){
        window.location.href="${ctx}/"
    }
</script>