<%@page pageEncoding="UTF-8" language="java" session="true" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
<script>
    var url = "${redirect_url}";
    if (url) {
        location.href = url;
    }
</script>
</body>
</html>

