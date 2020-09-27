<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'announcement_success.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <style type="text/css" media="all">
        @import url("/css/announcement.css");
    </style>
</head>

<body>
<div class="announcement_errorDiv">
    <table width="100%" align="center" border="0">

        <tr align="center">
            <td><h3><font color="blue">操作成功</font></h3>
                <input type="button" value=" 返  回 " onclick="javascript:window.history.go(-1)"
                       style="font-size:15px; width:80px; height:30px" theme="simple">
            </td>
        </tr>
    </table>
</div>
</body>
</html>
