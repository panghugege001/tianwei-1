<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>错误消息页面</title>

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
    <table width="100%" align="center">
        <tr align="center">
            <td><h3><font color="blue">操作失败</font></h3>
                <input type="button" value=" 返  回 " onclick="javascript:window.history.go(-1)"
                       style="font-size:15px; width:80px; height:30px" theme="simple">
            </td>
        </tr>
        <tr align="center">
            <td>
                <br/><br/>
                <a href="javascript:showMessage()"><span id="pName">点击查看明细</span></a>
            </td>
        </tr>
        <tr align="center">
            <td>
                <div style="display:none" id="msg">
                    <hr/>
                    <font color="red" size="2px"><s:property value="errormsg"/></font>
                </div>
            </td>
        </tr>
    </table>
</div>
<SCRIPT type="text/javascript">
    var b = true;
    function showMessage() {
        if (b) {
            b = false;
            document.getElementById('pName').innerHTML = "点击隐藏明细";
            document.getElementById('msg').style.display = "";
        } else {
            b = true;
            document.getElementById('pName').innerHTML = "点击查看明细";
            document.getElementById('msg').style.display = "none";
        }
    }
</SCRIPT>
</body>
</html>
