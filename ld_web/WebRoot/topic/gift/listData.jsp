﻿
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.utils.Constants" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<!doctype html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <link href="/css/util/reset.css" rel="stylesheet"/>
    <link href="/css/util/common.css" rel="stylesheet"/>
    <link href="/topic/gift/css/home.css" rel="stylesheet"/>
</head>
<body>
<form action="${ctx }/asp/queryGoldBetData.aspx" name="form1" id="mainform" method="post">
    <input type="hidden" name="pageIndex" value="1"/>
    <input type="hidden" name="size" value="${size }"/>
    <table class="table">
        <tr>
            <th>名次</th>
            <th>玩家账号</th>
            <th>流水</th>
        </tr>
        <s:iterator value="#request.page.pageContents" id="slots" status="st2">
            <tr>
                <td><s:if test="#request.searchname == null || #request.searchname == ''">
                    <s:property value="#st2.index + (10 * (#request.pageIndex-1)) + 1"/>
                </s:if></td>
                <td><s:property value="loginname"/></td>
                <td><s:number name="bet" groupingUsed="true" maximumFractionDigits="2"
                              minimumFractionDigits="2"/></td>
                    <%--<td><s:date name="startTime" format="yyyy-MM-dd"/>--%>
                    <%--~--%>
                    <%--<s:date name="endTime" format="yyyy-MM-dd"/></td>--%>
            </tr>
        </s:iterator>
    </table>
    <div class="text-center">${page.jsPageCode}</div>

</form>

<script type="text/javascript">
    function gopage(val) {
        document.form1.pageIndex.value = val;
        document.form1.submit();
    }
</script>
</body>
</html>
