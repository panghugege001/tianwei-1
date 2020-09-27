<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>编辑活动日历</title>
    <style type="text/css" media="all">
        @import url("/css/announcement.css");
    </style>
    <s:head/>
</head>

<body>
<div class="announcement_titleDiv">
    其他 --> 编辑活动日历
</div>
<div>
    <br/>
    <p align="center" style="font-size:13px;"><s:fielderror></s:fielderror></p>

    <s:form action="edit" namespace="/activityCalendar" theme="simple">
        <s:hidden name="id" value="%{#request.id}"></s:hidden>
        <table align="center" border="0" cellpadding="0" cellspacing="0" style="font-size:13px" width="100%">
            <tr align="left">
                <td width="100px">活动日历名称:</td>
                <td>
                    <s:textfield name="name" id="name" size="100" theme="simple"></s:textfield>
                </td>
            </tr>
            <tr align="left">
                <td width="100px">活动日历日期:</td>
                <td>
                    <s:textfield size="100" name="activityDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                                 cssClass="Wdate"></s:textfield>
                    <span style="color: red;">* 空代表长期有效</span>
                </td>
            </tr>
            <tr align="left">
                <td width="100px">是否禁用:</td>
                <td>
                    <select name="flag">
                        <option value="1" <s:if test="%{flag == 1}">selected="selected"</s:if>>启用</option>
                        <option value="2" <s:if test="%{flag == 2}">selected="selected"</s:if>>禁止</option>
                    </select>
                </td>
            </tr>
            <tr align="left">
                <td width="100px">排序:</td>
                <td>
                    <s:textfield name="orderBy" id="orderBy" size="10" theme="simple"></s:textfield>
                </td>
            </tr>
            <tr>
                <td width="100px">活动日历内容:</td>
                <td>
                    <s:textarea name="content" id="content" theme="simple" cols="60" rows="8"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td align="center">
                    <s:submit value="提交" style="font-size:15px; width:80px; height:30px" theme="simple"/>
                    <input type="button" value=" 返  回 " onclick="javascript:window.history.go(-1)"
                           style="font-size:15px; width:80px; height:30px" theme="simple">
                </td>
            </tr>
        </table>
    </s:form>
</div>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</body>
</html>
