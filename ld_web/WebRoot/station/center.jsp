<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-5-13
  Time: 下午12:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <style type="text/css">
        body {
            margin: 0px;
            overflow:hidden;
        }
    </style></head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed">
    <tr>
        <td background="${ctx}/images/admin/main_40.gif" style="width:3px;"></td>
        <td width="177"  style="border-right:solid 1px #9ad452;">
            <iframe name="I2" height="100%" width="177" border="0" frameborder="0" src="${ctx}/asp/leftBook.aspx">
            浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe>
        </td>
        <td><iframe name="I1" src="${ctx}/asp/bookfindAll.aspx" height="100%" width="100%" border="0" frameborder="0">
            浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe></td>
        <td background="${ctx}/images/admin/main_42.gif" style="width:3px;"></td>
    </tr>
</table>
</body>
</html>